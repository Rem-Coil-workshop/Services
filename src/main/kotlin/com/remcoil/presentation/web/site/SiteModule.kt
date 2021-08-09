package com.remcoil.presentation.web.site

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.siteModule() {
    routing {
        static("/") {
            files("static")
            default("static/index.html")
        }
//        static("/") {
//            resources(resourcePackage = "static")
//            defaultResource(resource = "index.html", resourcePackage = "static")
//        }
    }
}