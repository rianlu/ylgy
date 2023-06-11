package com.core.common

import com.bedroom412.ylgy.parser.ImportSourceParser
import org.junit.Test

class ImportSourceParserTests {

    @Test
    fun testParseUrls() {
        var urls = "http://music.163.com/song/media/outer/url?id=441491828\n" +
                "http://music.163.com/song/media/outer/url?id=436346833\n" +
                "http://music.163.com/song/media/outer/url?id=1867217766\n"


        val importSourceParser = ImportSourceParser(urls)
        importSourceParser.parse()
        assert(importSourceParser.result != null && importSourceParser.result is List<*> && (importSourceParser.result as List<*>).size == 3)
    }
}