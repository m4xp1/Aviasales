package one.xcorp.aviasales.extension

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout

val Activity.rootView: View
    get() = findViewById<View>(android.R.id.content)

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
    isErrorEnabled = false
    error = null
}
