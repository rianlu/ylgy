package com.bedroom412.ylgy.util

import java.io.File


object LyricsParser {
    fun parse(file: File): Pair<Metadata, List<Lyric>> {
        val metadata = Metadata(null, null, null, null, null)
        val regexTime = "\\[(\\d{2}):(\\d{2})\\.(\\d{2})]".toRegex()
        val lyrics = mutableListOf<Lyric>()
        file.forEachLine { line ->
            when {
                line.startsWith("[ti:") -> {
                    metadata.title = line.substringAfter("[").substringBefore("]")
                }

                line.startsWith("[ar:") -> {
                    metadata.artist = line.substringAfter("[").substringBefore("]")
                }

                line.startsWith("[al:") -> {
                    metadata.album = line.substringAfter("[").substringBefore("]")
                }

                line.startsWith("[au:") -> {
                    metadata.author = line.substringAfter("[").substringBefore("]")
                }

                line.startsWith("[offset:") -> {
                    metadata.offset = line.substringAfter("[").substringBefore("]").toIntOrNull()
                }

                else -> {
                    regexTime.findAll(line).forEach { matchResult ->
                        val (minute, second, millisecond) = matchResult.destructured
                        val time =
                            minute.toLong() * 60000 + second.toLong() * 1000 + millisecond.toLong() * 10
                        val text = line.substringAfter("]").trim()
                        lyrics.add(Lyric(time, text))
                    }
                }
            }
        }
        lyrics.sortBy { it.time }
        return Pair(metadata, lyrics)
    }
}