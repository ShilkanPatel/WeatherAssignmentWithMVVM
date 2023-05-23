package  com.test.myandroidassignment.repository

import android.content.Context
import android.content.SharedPreferences
import com.test.myandroidassignment.common.Constant.Companion.PREFERENCE_NAME
import  com.test.myandroidassignment.common.PrefConstant.Companion.PREF_LOGGED_IN
import  com.test.myandroidassignment.models.UserInfo

class PrefRepository(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }

    private fun String.getLong() = pref.getLong(this, 0)

    private fun String.getInt() = pref.getInt(this, 0)

    private fun String.getString() = pref.getString(this, "")!!

    private fun String.getStringForLat() = pref.getString(this, "21.257381")!!
    private fun String.getStringForLong() = pref.getString(this, "431.059570")!!

    private fun String.getBoolean() = pref.getBoolean(this, false)

    fun setLoggedIn(isLoggedIn: Boolean) {
        PREF_LOGGED_IN.put(isLoggedIn)
    }

    fun getLoggedIn() = PREF_LOGGED_IN.getBoolean()

    fun saveLoginData(userInfo: UserInfo) {
        "email".put(userInfo.email)
        "userName".put(userInfo.userName)
        "shortBio".put(userInfo.shortBio)
        "uploadUrl".put(userInfo.uploadUrl)
    }

    fun saveLocationData(lat: Double, long: Double) {
        "lat".put(lat.toString())
        "long".put(long.toString())
    }
    fun getLatitude(): Double {
        return "lat".getStringForLat().toDouble()
    }

    fun getLongitude(): Double {
        return "long".getStringForLong().toDouble()
    }


    fun getLoginData(): UserInfo {
        val userInfo = UserInfo()
        userInfo.shortBio = "shortBio".getString()
        userInfo.email = "email".getString()
        userInfo.userName = "userName".getString()
        userInfo.uploadUrl = "uploadUrl".getString()
        return userInfo
    }
}
