package com.madalv.webservice.handlers

import com.madalv.webservice.dependencies.classicalCipherFactory
import com.madalv.webservice.models.CipherInput
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun handleCaesar(call: ApplicationCall) {
    val cipher = classicalCipherFactory.getCipher("caesar")
    val input = call.receive<CipherInput>()
    val result = if (input.type == "encrypt") cipher.encrypt(input.text, input.key)
                        else cipher.decrypt(input.text, input.key)

    call.respondText(result)
}

suspend fun handleCaesarPermutation(call: ApplicationCall) {
    val cipher = classicalCipherFactory.getCipher("caesar-permutation")
    val input = call.receive<CipherInput>()
    val result = if (input.type == "encrypt") cipher.encrypt(input.text, input.key)
                        else cipher.decrypt(input.text, input.key)

    call.respondText(result)
}

suspend fun handleVigenere(call: ApplicationCall) {
    val cipher = classicalCipherFactory.getCipher("vigenere")
    val input = call.receive<CipherInput>()
    val result = if (input.type == "encrypt") cipher.encrypt(input.text, input.key.toString())
                        else cipher.decrypt(input.text, input.key.toString())

    call.respondText(result)
}

suspend fun handlePlayfair(call: ApplicationCall) {
    val cipher = classicalCipherFactory.getCipher("playfair")
    val input = call.receive<CipherInput>()
    val result = if (input.type == "encrypt") cipher.encrypt(input.text, input.key.toString())
                        else cipher.decrypt(input.text, input.key.toString())

    call.respondText(result)
}
