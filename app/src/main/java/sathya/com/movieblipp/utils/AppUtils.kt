package sathya.com.movieblipp.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun toHours(minutes: Int): String {
    var min = minutes
    var time = ""

    if (min > 0) {
        time = "${min / 60} hrs"
        min %= 60
    }

    if (min > 0) {
        time = "$time $min mins"
    }
    return time
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {

    Toast.makeText(requireContext(), message, duration).show()

}