package com.azuyamat.hydro

const val PREFIX = "[HYDRO] "

object Logger {

    private val logger = instance.server.logger

    fun info(message: String) {
        logger.info("$PREFIX$message")
    }

    fun warning(message: String) {
        logger.warning("$PREFIX$message")
    }

    fun severe(message: String) {
        logger.severe("$PREFIX$message")
    }
}