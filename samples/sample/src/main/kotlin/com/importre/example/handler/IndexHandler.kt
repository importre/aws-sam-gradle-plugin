package com.importre.example.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import kotlinx.html.HEAD
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.hr
import kotlinx.html.html
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import kotlinx.html.title

class IndexHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent = APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withHeaders(mapOf("Content-Type" to "text/html"))
        .withBody(body)

    private val body: String = createHTML().html {
        head {
            initHeader(title = "Hello, Web!")
        }

        body(classes = "container") {
            div(classes = "jumbotron") {
                h1(classes = "display-4") { +"Hello, world!" }
                p(classes = "lead") { +"This is a simple hero unit, ..." }
                hr(classes = "my-4")
                p { +"It uses utility classes for typography and ..." }
                a(classes = "btn btn-primary btn-lg", href = "/") {
                    attributes += "role" to "button"
                    +"Learn more"
                }
            }
        }
    }

    private fun HEAD.initHeader(title: String) {
        meta(charset = "utf-8")
        meta(
            name = "viewport",
            content = "width=device-width, initial-scale=1, shrink-to-fit=no"
        )

        link(
            rel = "stylesheet",
            href = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        )

        title(content = title)
    }
}
