package com.remcoil.endpoins.app

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.appModule() {
    routing {
        static("/") {
            files("static")
            default("static/index.html")
        }

        static("manual") {
            default("manual/Manual.pdf")
        }
    }
}
