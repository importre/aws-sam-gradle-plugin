package com.importre.example.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.importre.example.Response
import com.importre.example.json

class GreetingHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent = APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withHeaders(mapOf("Content-Type" to "application/json"))
        .withBody(Response(message = "Hello, ${input["name", "world"].capitalize()}!").json)

    private operator fun APIGatewayProxyRequestEvent?.get(
        key: String,
        default: String
    ): String = this?.queryStringParameters?.get(key) ?: default
}
