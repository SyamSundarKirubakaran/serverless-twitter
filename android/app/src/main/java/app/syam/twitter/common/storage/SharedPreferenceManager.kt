package app.syam.twitter.common.storage

import android.content.Context
import android.content.SharedPreferences
import app.syam.twitter.R
import app.syam.twitter.common.model.User
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable

object SharedPreferenceManager {

    private var prefName: String? = null
    private const val USER = "user"

    private fun initializePreferences(context: Context?): SharedPreferences? {
        prefName = context?.getString(R.string.app_name)
        return context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    fun setUser(context: Context?, user: User?): Completable {
        context?.let {
            val sharedPreferences = initializePreferences(context)
            sharedPreferences?.edit()?.apply {
                if (user == null) {
                    putString(USER, null)
                } else {
                    putString(USER, Gson().toJson(user))
                }
                apply()
            }
        }
        return Completable.complete()
    }

    fun getLoggedInUser(context: Context?): User? {
        val sharedPreferences = initializePreferences(context)
        return Gson().fromJson(sharedPreferences?.getString(USER, null), User::class.java)
    }

}