package com.cnkaptan.steppersample.views

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

fun dpToFloat(dp: Float, metrics: DisplayMetrics = Resources.getSystem().displayMetrics): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
}

fun spToFloat(dp: Float, metrics: DisplayMetrics = Resources.getSystem().displayMetrics): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, metrics)
}