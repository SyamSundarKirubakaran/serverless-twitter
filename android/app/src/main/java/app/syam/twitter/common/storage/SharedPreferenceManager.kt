package app.syam.twitter.common.storage

import android.content.Context
import android.content.SharedPreferences
import app.syam.twitter.R
import app.syam.twitter.common.model.StoredUser
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable

object SharedPreferenceManager {

    private var prefName: String? = null
    private const val USER = "user"

    private fun initializePreferences(context: Context?): SharedPreferences? {
        prefName = context?.getString(R.string.app_name)
        return context?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    fun setUser(context: Context?, storedUser: StoredUser?): Completable {
        context?.let {
            val sharedPreferences = initializePreferences(context)
            sharedPreferences?.edit()?.apply {
                if (storedUser == null) {
                    putString(USER, null)
                } else {
                    putString(USER, Gson().toJson(storedUser))
                }
                apply()
            }
        }
        return Completable.complete()
    }

    fun getLoggedInUser(context: Context?): StoredUser? {
        val sharedPreferences = initializePreferences(context)
        return Gson().fromJson(sharedPreferences?.getString(USER, null), StoredUser::class.java)
    }

}