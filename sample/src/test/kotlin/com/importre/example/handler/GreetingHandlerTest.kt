package com.importre.example.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.importre.example.Response
import com.importre.example.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GreetingHandlerTest {

    @Test
    fun testSuccessfulResponse() {
        val app = GreetingHandler()
        val result = app.handleRequest(null, null)
        assertEquals(result.statusCode, 200)
        assertEquals(result.headers["Content-Type"], "application/json")
        result.body.let { content ->
            assertNotNull(content)
            assertEquals(
                expected = Response(message = "Hello, World!").json,
                actual = content
            )
        }
    }

    @Test
    fun testSuccessfulResponseWithName() {
        val app = GreetingHandler()
        val input = APIGatewayProxyRequestEvent()
            .apply { queryStringParameters = mapOf("name" to "heo") }
        val result = app.handleRequest(input, null)
        result.body.let { content ->
            assertNotNull(content)
            assertEquals(
                expected = Response(message = "Hello, Heo!").json,
                actual = content
            )
        }
    }
}
