package com.madalv.webservice.handlers

import com.madalv.webservice.accountManager
import com.madalv.webservice.dependencies.vonageClient
import com.madalv.webservice.models.UserSession
import com.madalv.webservice.models.VerifyNumberResponse
import com.madalv.webservice.plugins.OriginalRequestURI
import com.vonage.client.verify.VerifyStatus
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

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

suspend fun handleVerifyPhone(call: ApplicationCall) {
    val phoneNumber = call.parameters["phoneNumber"]
    require(!phoneNumber.isNullOrBlank()) { "phoneNumber is missing" }

    val ongoingVerify = vonageClient.verify(phoneNumber, "VONAGE")

    val response = VerifyNumberResponse(ongoingVerify.requestId)
    call.respond(response)

}