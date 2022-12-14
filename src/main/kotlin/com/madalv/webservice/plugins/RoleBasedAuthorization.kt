package com.madalv.webservice.plugins

import com.madalv.webservice.models.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

typealias Role = String

enum class AuthType {
    ALL,
    ANY,
    NONE,
}

class AuthConfig {
    var roles: Set<String> = emptySet()
    lateinit var getRoles : (user: Principal) -> Set<Role>
    lateinit var type: AuthType
}


val RoleBasedAuthorization = createRouteScopedPlugin(
    name = "AuthorizationPlugin",
    createConfiguration = ::AuthConfig
) {
    val requiredRoles = pluginConfig.roles
    val type = pluginConfig.type
    val getUserRoles = pluginConfig.getRoles
    on(AuthenticationChecked) {call ->
        val user = call.principal<UserSession>() ?: throw Exception("Unauthenticated User")
        val userRoles = getUserRoles(user)
        val denyReasons = mutableListOf<String>()

        when(type) {
            AuthType.ALL -> {
                val missing = requiredRoles - userRoles
                if (missing.isNotEmpty()) {
                    denyReasons += "Principal $user lacks required role(s) ${missing.joinToString(" and ")}"
                }
            }
            AuthType.ANY -> {
                if (userRoles.none { it in requiredRoles }) {
                    denyReasons += "Principal $user has none of the sufficient role(s) ${
                        requiredRoles.joinToString(
                            " or "
                        )
                    }"

                }
            }
            AuthType.NONE -> {
                if (userRoles.any{ it in requiredRoles}) {
                    denyReasons += "Principal $user has forbidden role(s) ${
                        (requiredRoles.intersect(userRoles)).joinToString(
                            " and "
                        )
                    }"

                }
            }
        }

        if (denyReasons.isNotEmpty()) {
            val message = denyReasons.joinToString(". ")
            throw Exception(message)
        }

    }
}

class AuthorizedRouteSelector(private val description: String) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize ${description})"
}


fun Route.withRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(requiredRoles = roles.toSet(),authType= AuthType.ALL, build = build)

fun Route.withAnyRole(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(requiredRoles = roles.toSet(),authType = AuthType.ANY, build = build)

fun Route.withoutRoles(vararg roles: Role, build: Route.() -> Unit) =
    authorizedRoute(requiredRoles = roles.toSet(), authType = AuthType.NONE,build = build)

private fun Route.authorizedRoute(
    requiredRoles: Set<Role>,
    authType: AuthType,
    build: Route.() -> Unit
): Route {

    val description = requiredRoles.joinToString(",")
    val authorizedRoute = createChild(AuthorizedRouteSelector(description))

    authorizedRoute.install(RoleBasedAuthorization) {
        roles = requiredRoles
        type = authType
        getRoles = {
            (it as UserSession).roles
        }
    }
    authorizedRoute.build()
    return authorizedRoute
}