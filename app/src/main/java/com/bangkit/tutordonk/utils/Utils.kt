package com.bangkit.tutordonk.utils

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.model.FailResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.full.memberProperties


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

fun <T> Response<T>.handleResponse(context: Context): Boolean {
    return if (this.isSuccessful) true
    else {
        val errorBody = this.errorBody()?.string()
        var message = "Unknown error"

        errorBody?.let {
            message = try {
                val jsonElement = JsonParser.parseString(it)
                if (jsonElement.isJsonObject) {
                    val failResponse = Gson().fromJson(jsonElement, FailResponse::class.java)
                    failResponse.message
                } else {
                    it
                }
            } catch (e: JsonSyntaxException) {
                it
            }
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        false
    }
}

fun Any.isAllFieldsNotEmpty(): Boolean {
    val properties = this::class.memberProperties
    for (property in properties) {
        when (val value = property.getter.call(this)) {
            is String -> if (value.isEmpty()) return false
            is Int? -> if (value == null) return false
            is List<*> -> if (value.isEmpty()) return false
        }
    }
    return true
}

fun String.isTextMatchingKeywords(keywords: String = "https://drive.google.com"): Boolean {
    val keywordList = this.split(",").map { it.trim() }
    return keywordList.all { keyword -> keyword.contains(keywords, ignoreCase = true) }
}

fun EditText.addMoneyTextWatcher(locale: Locale = Locale("in", "ID")) {
    val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = 0
        isGroupingUsed = true
    }

    var current = ""

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {/* no-op */
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {/* no-op */
        }

        override fun afterTextChanged(s: Editable?) {
            if (s.toString() != current) {
                this@addMoneyTextWatcher.removeTextChangedListener(this)

                try {
                    val cleanString = s.toString().replace("[Rp,.]".toRegex(), "")
                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble()
                        val formatted = numberFormat.format(parsed)

                        current = formatted
                        this@addMoneyTextWatcher.setText(formatted)
                        this@addMoneyTextWatcher.setSelection(formatted.length)
                    } else {
                        current = ""
                        this@addMoneyTextWatcher.text?.clear()
                        this@addMoneyTextWatcher.setSelection(0)
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                this@addMoneyTextWatcher.addTextChangedListener(this)
            }
        }
    }

    this.addTextChangedListener(textWatcher)
}

fun Long.toFormattedDateString(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault()
): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(date)
}