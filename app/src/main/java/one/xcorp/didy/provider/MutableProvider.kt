package one.xcorp.didy.provider

import javax.inject.Provider

interface MutableProvider<T> : Provider<T> {

    fun set(value: T?)

    companion object {

        operator fun <T> invoke(value: T? = null) = object : MutableProvider<T> {

            private var instance = value

            override fun get(): T? = instance

            override fun set(value: T?) {
                instance = value
            }
        }
    }
}
