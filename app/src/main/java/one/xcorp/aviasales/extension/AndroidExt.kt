package one.xcorp.aviasales.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

private val CurrentDrawable = ColorDrawable(Color.TRANSPARENT)

fun Activity.getRootView(): View =
    findViewById<View>(android.R.id.content)

fun Activity.getDisplayRect(): Rect =
    Rect().also { windowManager.defaultDisplay.getRectSize(it) }

@AnyRes
fun Context.getThemeAttribute(@AttrRes resId: Int, @AnyRes default: Int): Int =
    theme.obtainStyledAttributes(intArrayOf(resId)).run {
        val id = getResourceId(0, default)
        recycle()
        id
    }

fun TextInputLayout.showError(@StringRes resId: Int) {
    isErrorEnabled = true
    error = resources.getString(resId)
}

fun TextInputLayout.hideError() {
    if (isErrorEnabled) {
        isErrorEnabled = false
        error = null
    }
}

fun TextView.setOnEditorActionListener(imeAction: Int, block: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == imeAction) {
            block.invoke()
            true
        } else false
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
