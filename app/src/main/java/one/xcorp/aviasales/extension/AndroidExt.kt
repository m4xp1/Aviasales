package one.xcorp.aviasales.extension

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes

val Activity.rootView: View
    get() = findViewById<View>(android.R.id.content)

@AnyRes
fun Context.getThemeAttribute(@AttrRes resId: Int, @AnyRes default: Int): Int =
    theme.obtainStyledAttributes(intArrayOf(resId)).run {
        val id = getResourceId(0, default)
        recycle()
        id
    }
