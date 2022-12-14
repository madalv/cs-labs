package com.madalv.webservice.plugins


import com.madalv.webservice.handlers.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


data class OriginalRequestURI(val uri: String)

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("Hello CS Lab 5!")
        }

        get("/login") {
            call.respondText("Send a post to /login with email and password and roles.")
        }

        post("/login") {
            try {
                handleLogin(call)
            } catch (e: Exception) {
                e.message?.let { call.respondText(it) }
            }
        }

        get("verify-number") {
            try {
                handleVerifyPhone(call)
            } catch (e: Exception) {
                e.message?.let { call.respondText(it) }
            }
        }

        authenticate {
            route("classical") {
                withRoles("caesar"){
                    get("caesar") {
                        try {
                            handleCaesar(call)
                        } catch (e: Exception) {
                            e.message?.let { call.respondText(it) }
                        }
                    }

                    get("caesar-permutation") {
                        try {
                            handleCaesarPermutation(call)
                        } catch (e: Exception) {
                            e.message?.let { call.respondText(it) }
                        }
                    }
                }

                withRoles("vigenere") {
                    get("vigenere") {
                        try {
                            handleVigenere(call)
                        } catch (e: Exception) {
                            e.message?.let { call.respondText(it) }
                        }
                    }
                }

                withRoles("playfair") {
                    get("playfair") {
                        try {
                            handlePlayfair(call)
                        } catch (e: Exception) {
                            e.message?.let { call.respondText(it) }
                        }
                    }
                }
            }
        }
    }
}
