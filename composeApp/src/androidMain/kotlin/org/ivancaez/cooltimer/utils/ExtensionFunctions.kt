package org.ivancaez.cooltimer.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.getActivity(): ComponentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) {
            println("Returning activity")
            return context
        }
        context = context.baseContext
    }
    return null
}