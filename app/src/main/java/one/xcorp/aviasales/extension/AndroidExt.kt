package one.xcorp.aviasales.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

private val CurrentDrawable = ColorDrawable(Color.TRANSPARENT)

@AnyRes
fun Context.getThemeAttribute(@AttrRes resId: Int, @AnyRes default: Int): Int =
    theme.obtainStyledAttributes(intArrayOf(resId)).run {
        val id = getResourceId(0, default)
        recycle()
        id
    }

fun Activity.getRootView(): View =
    findViewById<View>(android.R.id.content)

fun Activity.getDisplayRect(): Rect =
    Rect().also { windowManager.defaultDisplay.getRectSize(it) }

fun View.watchFocusChanges(block: (Boolean) -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        block.invoke(hasFocus)
    }
}

fun <T> View.watchClicks(block: () -> T) {
    setOnClickListener { block.invoke() }
}

fun View.watchTouches(block: () -> Unit) {
    setOnTouchListener { _, event ->
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> block.invoke()
        }
        false
    }
}

fun TextView.updateCompoundDrawable(
    start: Drawable? = CurrentDrawable,
    top: Drawable? = CurrentDrawable,
    end: Drawable? = CurrentDrawable,
    bottom: Drawable? = CurrentDrawable
) {
    val drawables = compoundDrawablesRelative
    if (start !== CurrentDrawable) {
        drawables[0] = start
    }
    if (top !== CurrentDrawable) {
        drawables[1] = top
    }
    if (end !== CurrentDrawable) {
        drawables[2] = end
    }
    if (bottom !== CurrentDrawable) {
        drawables[3] = bottom
    }
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawables[0],
        drawables[1],
        drawables[2],
        drawables[3]
    )
}

fun TextView.watchEditorAction(imeAction: Int, block: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == imeAction) {
            block.invoke()
            true
        } else false
    }
}

fun TextView.watchTextChanges(block: (String) -> Unit) {
    addTextChangedListener(afterTextChanged = { block.invoke(text.toString()) })
}

fun TextInputLayout.showError(@StringRes resId: Int) {
    isErrorEnabled = true
    error = resources.getString(resId)
}

fun TextInputLayout.showError(message: String? = null) {
    isErrorEnabled = true
    error = message
}

fun TextInputLayout.hideError() {
    if (isErrorEnabled) {
        isErrorEnabled = false
        error = null
    }
}

fun AutoCompleteTextView.showCompletion() {
    if (!text.isNullOrBlank() && !isPopupShowing) showDropDown()
}

fun AutoCompleteTextView.changeCompletionVisibility(isVisible: Boolean) {
    if (isVisible) showCompletion() else dismissDropDown()
}

fun AutoCompleteTextView.watchItemClick(block: (Int) -> Unit) {
    setOnItemClickListener { _, _, position, _ -> block.invoke(position) }
}
