package com.yumedev.seijakulist.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Throttler para coordinar peticiones a la API y respetar los rate limits.
 * Jikan API permite 3 peticiones por segundo, usamos 500ms de delay para estar seguros.
 */
@Singleton
class RequestThrottler @Inject constructor() {
    private val mutex = Mutex()
    private var lastRequestTime = 0L
    private val minDelayBetweenRequests = 500L // 500ms = máx 2 req/seg (conservador)

    /**
     * Ejecuta una petición respetando el rate limit.
     * @param block La función suspendida que realiza la petición
     * @return El resultado de la petición o null si falla
     */
    suspend fun <T> throttle(block: suspend () -> T): T? {
        mutex.withLock {
            val now = System.currentTimeMillis()
            val timeSinceLastRequest = now - lastRequestTime

            if (timeSinceLastRequest < minDelayBetweenRequests) {
                val delayTime = minDelayBetweenRequests - timeSinceLastRequest
                delay(delayTime)
            }

            lastRequestTime = System.currentTimeMillis()
        }

        return try {
            block()
        } catch (e: Exception) {
            null
        }
    }
}
