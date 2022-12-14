# Lab 5 Report

### Course: Cryptography & Security

### Author: Magal Vlada, FAF-203

----

## Overview:

&ensp;&ensp;&ensp; Authentication & authorization are 2 of the main security goals of IT systems and should not be used interchangibly. Simply put, during authentication the system verifies the identity of a user or service, and during authorization the system checks the access rights, optionally based on a given user role.

&ensp;&ensp;&ensp; There are multiple types of authentication based on the implementation mechanism or the data provided by the user. Some usual ones would be the following:
- Based on credentials (Username/Password);
- Multi-Factor Authentication (2FA, MFA);
- Based on digital certificates;
- Based on biometrics;
- Based on tokens.

&ensp;&ensp;&ensp; Regarding authorization, the most popular mechanisms are the following:
- Role Based Access Control (RBAC): Base on the role of a user;
- Attribute Based Access Control (ABAC): Based on a characteristic/attribute of a user.

## Objectives:
1. Take what you have at the moment from previous laboratory works and put it in a web service / serveral web services.
2. Your services should have implemented basic authentication and MFA (the authentication factors of your choice).
3. Your web app needs to simulate user authorization and the way you authorise user is also a choice that needs to be done by you.
4. As services that your application could provide, you could use the classical ciphers. Basically the user would like to get access and use the classical ciphers, but they need to authenticate and be authorized.

## Implementation Description:

### Authentication

The authentication scheme used is Basic Authentication + 2FA using the a Vonage Client.
The user first sends a request to `/verify-number`, then to `/login` with all the usual info (credentials, roles, code).
When trying to access a route, such as `/caesar`, the app first checks if the user's session exists (a.k.a is logged in).
If yes, it then checks if the user has the necessary roles (more about authorization in the next part). If no, it redirects 
to the `/login` page.

The flow in details:

#### Verify Number

This function handles the request sent to `/verify-number`, using a 2FA client called Vonage. 
Pretty self-explanatory stuff. `vonageClient` can be found in `webservice/dependencies`.

```kt
suspend fun handleVerifyPhone(call: ApplicationCall) {
    val phoneNumber = call.parameters["phoneNumber"]
    require(!phoneNumber.isNullOrBlank()) { "phoneNumber is missing" }

    val ongoingVerify = vonageClient.verify(phoneNumber, "VONAGE")

    val response = VerifyNumberResponse(ongoingVerify.requestId)
    call.respond(response)
}
```

#### Login

This function handles the request sent to `/login`. It extracts all the parameters from the request,
then the Vonage client checks the 2FA code. If the `accountManager` (also found in `webservice/dependencies`) manages
to verify the account and the 2FA code is correct, the user's session is set (also the user is redirected back 
to their original route, if they were once redirected to `/login`). 

```kt
suspend fun handleLogin(call: ApplicationCall) {
    val params = call.receiveParameters()
    val username = params["username"]
    val password = params["password"]
    val roles = params.getAll("roles")?.toSet() ?: emptySet()

    val code = call.parameters["code"]
    val requestId = call.parameters["requestId"]

    val checkResponse = vonageClient.check(requestId!!, code!!)

    password?.let { pass ->
        username?.let { name ->
            if (accountManager.verifyPassword(name, pass)
                && checkResponse.status == VerifyStatus.OK) {
                call.sessions.set(UserSession(name, roles))
                val redirectURL = call.sessions.get<OriginalRequestURI>()?.also {
                    call.sessions.clear<OriginalRequestURI>()
                }
                call.respondRedirect(redirectURL?.uri ?: "/")
            } else {
                call.respondRedirect("/login")
            }
        }
    }
}
```

### Authorization

After the user is authenticated, their roles (passed in the request to `/login`) are verified against the required role.
Each route can be set to require certain roles, using `withRoles`:

```kt
withRoles("vigenere") {
    get("vigenere") {
        try {
            handleVigenere(call)
        } catch (e: Exception) {
            e.message?.let { call.respondText(it) }
        }
    }
}
```

In this example, if the user is both authenticated and has the `vigenere` role, they have access to the `/vigenere`
 route.

Details about the internals of how RBAC works can be found in `webservice/plugins/RoleBasedAuthorization.kt`.

### Accessing Services

If the user has finally been authenticated and authorized, they can access the cipher the want (or have access to, more 
like). The request body contains data such as the text, key, and whether to encrypt/decrypt. They get the result in response.

```kt
suspend fun handleCaesar(call: ApplicationCall) {
    val cipher = classicalCipherFactory.getCipher("caesar")
    val input = call.receive<CipherInput>()
    val result = if (input.type == "encrypt") cipher.encrypt(input.text, input.key)
                        else cipher.decrypt(input.text, input.key)

    call.respondText(result)
}
```

## Conclusions

In this lab I learned how to create a Ktor Webservice that has Basic + 2FA Authentication 
and simulate Authorization with Role Based Access Control. As services there's encryption/decryption
using the Classical Ciphers from Lab 1.