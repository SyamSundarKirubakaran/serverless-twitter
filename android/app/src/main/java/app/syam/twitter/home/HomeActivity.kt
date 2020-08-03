package app.syam.twitter.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.syam.twitter.R
import app.syam.twitter.common.fragment.EmptyFragment
import app.syam.twitter.common.fragment.InProgressFragment
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.model.CreateTweet
import app.syam.twitter.home.state.HomeCallState
import app.syam.twitter.home.state.GeneralCallState
import app.syam.twitter.home.viewmodel.HomeViewModel
import app.syam.twitter.tweet.fragment.TweetsFragment
import app.syam.twitter.tweet.model.LightWeightUser
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
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
                title(R.string.compose)
                cornerRadius(10f)
                positiveButton(R.string.tweet)
                input(maxLength = 120) { dialog, text ->
                    val newTweet = CreateTweet(
                        tweet = text.toString(),
                        user = LightWeightUser(
                            userId = user?.userId,
                            name = user?.name,
                            email = user?.email,
                            imageUrl = user?.imageUrl,
                            isVerified = user?.isVerified
                        )
                    )
                    viewModel.createTweet(newTweet = newTweet)
                }
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

        viewModel.generalLiveData.observe(this, Observer {
            when (it) {
                GeneralCallState.Success -> {
                    viewModel.tweets()
                }
                GeneralCallState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.homeFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                    fab.visibility = View.GONE
                }
                GeneralCallState.Failed -> {
                    viewModel.tweets()
                }
            }
        })

    }
}
