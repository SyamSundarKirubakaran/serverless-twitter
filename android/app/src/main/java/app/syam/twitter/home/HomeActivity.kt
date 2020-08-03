package app.syam.twitter.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.fragment.EmptyFragment
import app.syam.twitter.common.fragment.InProgressFragment
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.item.HeaderItem
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.LikeCallState
import app.syam.twitter.home.viewmodel.HomeViewModel
import app.syam.twitter.profile.ProfileActivity
import app.syam.twitter.tweet.fragment.TweetsFragment
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        fab.setOnClickListener {
            MaterialDialog(this).show {
                input(maxLength = 60)
                title(R.string.compose)
                cornerRadius(10f)
                positiveButton(R.string.tweet)
            }
        }

        viewModel.tweets()

        viewModel.tweetListLiveData.observe(this, Observer {
            when (it) {
                is HomeCallState.Success -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            TweetsFragment.newInstance(it.result.result)
                        )
                        .commitAllowingStateLoss()
                }
                HomeCallState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                HomeCallState.Failed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            EmptyFragment.newInstance(errorText = "Error Fetching Tweets")
                        )
                        .commitAllowingStateLoss()
                }
            }
        })

        viewModel.tweetLikeLiveData.observe(this, Observer {
            when (it) {
                LikeCallState.Success -> {
                    viewModel.tweets()
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
                    viewModel.tweets()
                }
            }
        })

    }
}
