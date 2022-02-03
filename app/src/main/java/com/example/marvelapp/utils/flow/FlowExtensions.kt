package com.example.marvelapp.utils.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect


/**
 * Check if the channel is not closed and try to emit a value, catching [CancellationException] if the corresponding
 * has been cancelled. This extension is used in call callbackFlow.
 */
@ExperimentalCoroutinesApi
fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    if (isClosedForSend) return false
    return try {
        offer(value)
    } catch (e: CancellationException) {
        false
    }
}

/**
 * Terminal flow operator that collects the given flow with a provided [action] and catch [CancellationException]
 */
suspend inline fun <T> Flow<T>.safeCollect(crossinline action: suspend (value: T) -> Unit): Unit =
    collect { value ->
        try {
            action(value)
        } catch (e: CancellationException) {
            // Do nothing
        }
    }

/**
 * Terminal flow operator that [launches][launch] the [collection][collect] of the given flow in the [scope] and catch
 * [CancellationException]
 * It is a shorthand for `scope.launch { flow.safeCollect {} }`.
 */
fun <T> Flow<T>.safeLaunchIn(scope: CoroutineScope) = scope.launch {
    this@safeLaunchIn.safeCollect { /* Do nothing */ }
}
