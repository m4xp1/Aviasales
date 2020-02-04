package one.xcorp.didy.provider

import java.lang.ref.WeakReference

class WeakProvider<T>(value: T? = null) : MutableProvider<T> {

    private var reference = value?.let { WeakReference(it) }

    override fun get(): T? = reference?.get()

    override fun set(value: T?) {
        reference = value?.let { WeakReference(it) }
    }
}
