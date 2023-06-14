package com.core.network

import com.core.download.RateLimiter
import com.core.download.Ticker
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalTime
import java.util.concurrent.TimeUnit


class RateLimiterTest {
    @Test
    fun testRateLimiter() {
        var rateLimiter = RateLimiter(0.5)

        runBlocking {
            (0 until 100).map {
                GlobalScope.launch(Dispatchers.IO, start = CoroutineStart.LAZY) {
                    var acquire = rateLimiter.acquire(1)
                    println("${Thread.currentThread().name} : acquire ret = ${acquire}, date = ${LocalTime.now()}")
                }
            }.let {

                it.forEach { it.start() }

                it
            }.joinAll()

        }
    }

    @Test
    fun testRateLimiter2() {
        var rateLimiter = RateLimiter(0.1)
        var myTicker = MyTicker()
        var element:Long = System.nanoTime()
        myTicker.time = listOf(element,element + TimeUnit.MILLISECONDS.toMicros(200))
        rateLimiter.ticker = myTicker
        runBlocking {
            var acquire = rateLimiter.acquire(1)
            println("${Thread.currentThread().name} : acquire ret = ${acquire}, date = ${LocalTime.now()}")
             acquire = rateLimiter.acquire(1)
            println("${Thread.currentThread().name} : acquire ret = ${acquire}, date = ${LocalTime.now()}")
        }
    }
    class MyTicker: Ticker(

    ){
        lateinit var time:List<Long>
        var i = 0
        override fun read(): Long {
            println("get .. ")
            return time[i++]
        }
    }
}