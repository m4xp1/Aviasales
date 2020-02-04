package one.xcorp.mvvm.model

sealed class InputState<out T> {

    object NotEntered : InputState<Nothing>()

    data class Entered<out T>(
        val value: T
    ) : InputState<T>()

    open class Error : InputState<Nothing>()

    companion object {

        operator fun <T> invoke(value: T?): InputState<T> {
            return if (value == null) NotEntered else Entered(value)
        }
    }
}
