package app.syam.twitter.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.profile.item.ProfileHeader
import app.syam.twitter.tweet.fragment.USER
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val profileAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.extras?.getString(USER)

        // Get user with the received userId and also get all his tweets

        val storedUser = SharedPreferenceManager.getLoggedInUser(this)

        profileRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
            isNestedScrollingEnabled = false
        }

        profileAdapter.apply {

        }
    }
}
