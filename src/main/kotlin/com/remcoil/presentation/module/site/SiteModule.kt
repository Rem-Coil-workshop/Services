package com.remcoil.presentation.module.site

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.siteModule() {
    routing {
        static("/") {
            resources(resourcePackage = "static")
            defaultResource(resource = "index.html", resourcePackage = "static")
        }
    }
}