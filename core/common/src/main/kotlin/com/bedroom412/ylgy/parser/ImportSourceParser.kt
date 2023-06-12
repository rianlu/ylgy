package com.bedroom412.ylgy.parser

class ImportSourceParser(
    private var content: String

) {
    var result: Any? = null

    fun parse() {
        if (content.startsWith("http")) {
            parseUrls();
        } else if (content.trimStart().startsWith("{")) {
            parseJson();
        }
    }


    private fun parseJson() {
        //
    }

    private fun parseUrls() {
        val urls: List<String> = content.trim().lines().filter { it.startsWith("http") }
        result = urls
    }
}