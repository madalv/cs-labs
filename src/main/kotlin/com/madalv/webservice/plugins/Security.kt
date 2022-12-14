package com.madalv.webservice.plugins

import com.madalv.webservice.models.UserSession
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<UserSession>("user_session_cookie", SessionStorageMemory())
        cookie<OriginalRequestURI>("original_request_cookie")
    }

    authentication {
        session<UserSession> {
            challenge {
                call.sessions.set(OriginalRequestURI(call.request.uri))
                call.respondRedirect("/login")
            }
            validate {
                it
            }
        }
    }
}
