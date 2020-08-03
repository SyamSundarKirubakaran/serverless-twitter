package app.syam.twitter.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.syam.twitter.R
import app.syam.twitter.common.fragment.EmptyFragment
import app.syam.twitter.common.fragment.InProgressFragment
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.LikeCallState
import app.syam.twitter.home.viewmodel.HomeViewModel
import app.syam.twitter.tweet.fragment.TweetsFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
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
                    fab.visibility = View.VISIBLE
                }
                HomeCallState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                    fab.visibility = View.GONE
                }
                HomeCallState.Failed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            EmptyFragment.newInstance(errorText = "Error Fetching Tweets")
                        )
                        .commitAllowingStateLoss()
                    fab.visibility = View.GONE
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
                    fab.visibility = View.GONE
                }
                LikeCallState.Failed -> {
                    viewModel.tweets()
                }
            }
        })

    }
}
