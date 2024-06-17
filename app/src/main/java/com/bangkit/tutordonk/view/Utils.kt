package com.bangkit.tutordonk.view

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.bangkit.tutordonk.R

fun NavController.navigateWithAnimation(
    destinationId: Int,
    inclusive: Boolean = false,
    popUpTo: Int? = null,
    args: Bundle? = null
) {
    val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_right
            popExit = R.anim.slide_out_left
        }
        popUpTo?.let {
            popUpTo(it) {
                this.inclusive = inclusive
            }
        }
    }
    this.navigate(destinationId, args, options)
}

fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()
}

fun View.setReadOnly() {
    this.isEnabled = false
    this.isClickable = false
    this.isFocusable = false
    this.isFocusableInTouchMode = false
}