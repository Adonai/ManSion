package com.adonai.manman.misc

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.adonai.manman.R
import com.adonai.manman.service.Config


fun FragmentActivity.setupTheme() {
    when (Config.appTheme) {
        // select theme that user chose in settings
        "light" -> setTheme(R.style.AppThemeLight)
        "dark" -> setTheme(R.style.AppThemeDark)
        "green" -> setTheme(R.style.AppThemeDarkGreen)
        else -> { // "default"
            // select theme based on system UI mode
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> setTheme(R.style.AppThemeLight)
                Configuration.UI_MODE_NIGHT_YES -> setTheme(R.style.AppThemeDark)
            }
        }
    }
}

/**
 * Overlays main view of the activity with the specified fragment.
 */
fun FragmentActivity.showFullscreenFragment(frag: Fragment) {
    supportFragmentManager.beginTransaction()
        .addToBackStack("Showing fragment: $frag")
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(R.id.main_layout, frag)
        .commit()
}

fun Context.resolveAttr(attr: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}