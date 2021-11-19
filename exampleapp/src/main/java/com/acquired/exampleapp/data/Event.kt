package com.acquired.exampleapp.data

/**
 * Event is an event that can be retrieved once, after that it returns null
 */
sealed class Event<out T>(private val content: T? = null) {
    private var handled = false

    fun get(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }
}

class ToastEvent(message: String) : Event<String>(message)
class WebViewEvent(active: Boolean) : Event<Boolean>(active)
