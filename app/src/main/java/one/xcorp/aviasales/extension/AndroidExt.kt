package one.xcorp.aviasales.extension

import android.content.Context
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes

@AnyRes
fun Context.getThemeAttribute(@AttrRes resId: Int, @AnyRes default: Int): Int =
    theme.obtainStyledAttributes(intArrayOf(resId)).run {
        val id = getResourceId(0, default)
        recycle()
        id
    }
