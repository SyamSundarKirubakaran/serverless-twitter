package app.syam.twitter.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.fragment.EmptyFragment
import app.syam.twitter.common.fragment.InProgressFragment
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.LikeCallState
import app.syam.twitter.home.viewmodel.HomeViewModel
import app.syam.twitter.profile.fragment.ProfileFragment
import app.syam.twitter.profile.item.ProfileHeader
import app.syam.twitter.profile.state.ProfileState
import app.syam.twitter.profile.viewmodel.ProfileViewModel
import app.syam.twitter.tweet.fragment.TweetsFragment
import app.syam.twitter.tweet.fragment.USER
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val profileAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.extras?.getString(USER)

        val storedUser = SharedPreferenceManager.getLoggedInUser(this)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        viewModel.initProfileLoad(userId.orEmpty())

        viewModel.profileLiveData.observe(this, Observer {
            when (it) {
                is ProfileState.Success -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            ProfileFragment.newInstance(
                                tweetsList = it.result.first.result,
                                user = it.result.second.result
                            )
                        )
                        .commitAllowingStateLoss()
                }
                ProfileState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                ProfileState.Failed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            EmptyFragment.newInstance(errorText = "Error Fetching Profile")
                        )
                        .commitAllowingStateLoss()
                }
            }
        })

        viewModel.profileTweetLikeLiveData.observe(this, Observer {
            when (it) {
                LikeCallState.Success -> {
                    viewModel.initProfileLoad(userId.orEmpty())
                }
                LikeCallState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                LikeCallState.Failed -> {
                    viewModel.initProfileLoad(userId.orEmpty())
                }
            }
        })
    }
}
