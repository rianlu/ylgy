package com.core.download

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.TimeUnit

open class RateLimiter {


    var ticker: Ticker = Ticker()

    constructor(permitsPerSecond: Double) {
        runBlocking { setRate(permitsPerSecond); }
    }

    /**
     * The currently stored permits.
     */
    var storedPermits = 0.0

    /**
     * The maximum number of stored permits.
     */
    var maxPermits = 0.0

    /**
     * The interval between two unit requests, at our stable rate. E.g., a stable rate of 5 permits
     * per second has a stable interval of 200ms.
     */
    @Volatile
    var stableIntervalMicros = 0.0

    private val mutex = Mutex()

    /**
     * The time when the next request (no matter its size) will be granted. After granting a request,
     * this is pushed further in the future. Large requests push this further than small requests.
     */
    private var nextFreeTicketMicros = 0L // could be either in the past or future


    suspend fun setRate(permitsPerSecond: Double) {
        mutex.withLock {
            resync(ticker.read())
            val stableIntervalMicros =
                TimeUnit.SECONDS.toMicros(1L).toDouble() / permitsPerSecond
            this.stableIntervalMicros = stableIntervalMicros
            doSetRate(permitsPerSecond, stableIntervalMicros)
        }
    }

    fun doSetRate(permitsPerSecond: Double, stableIntervalMicros: Double) {
        val oldMaxPermits = maxPermits
        maxPermits = permitsPerSecond
        storedPermits = if (oldMaxPermits == 0.0) 0.0 // initial state
        else storedPermits * maxPermits / oldMaxPermits
    }


    open suspend fun acquire(permits: Int): Double {
        var microsToWait: Long
        mutex.withLock {
            microsToWait = this.reserveNextTicket(permits.toDouble(), ticker.read())
        }
        delay(TimeUnit.MICROSECONDS.toMillis(microsToWait))
        return 1.0 * microsToWait / TimeUnit.SECONDS.toMicros(1L);
    }


    private fun reserveNextTicket(requiredPermits: Double, nowMicros: Long): Long {
        this.resync(nowMicros)
        val microsToNextFreeTicket = nextFreeTicketMicros - nowMicros
        val storedPermitsToSpend = Math.min(requiredPermits, storedPermits)
        val freshPermits = requiredPermits - storedPermitsToSpend
        val waitMicros: Long = (freshPermits * stableIntervalMicros).toLong()
        nextFreeTicketMicros += waitMicros
        storedPermits -= storedPermitsToSpend
        return microsToNextFreeTicket
    }


    private fun resync(nowMicros: Long) {
        if (nowMicros > nextFreeTicketMicros) {
            storedPermits = Math.min(
                maxPermits,
                storedPermits + (nowMicros - nextFreeTicketMicros).toDouble() / stableIntervalMicros
            )
            nextFreeTicketMicros = nowMicros
        }
    }
}


open class Ticker {
    open fun read(): Long {
        return System.nanoTime()
    }
}