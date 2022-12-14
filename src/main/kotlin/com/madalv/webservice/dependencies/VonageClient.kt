package com.madalv.webservice.dependencies

import com.vonage.client.VonageClient
import com.vonage.client.verify.CheckResponse
import com.vonage.client.verify.VerifyResponse

val vonageClient = VonageClient()

class VonageClient {
    private val client: VonageClient = VonageClient.builder()
        .apiKey("API_KEY")
        .apiSecret("API_SECRET")
        .build()

    fun verify(phoneNumber: String, brand: String): VerifyResponse {
            return client.verifyClient.verify(phoneNumber, brand)
        }

    fun check(requestId: String, code: String): CheckResponse {
        return client.verifyClient.check(requestId, code)
    }
}