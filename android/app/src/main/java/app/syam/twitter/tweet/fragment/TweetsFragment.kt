package app.syam.twitter.tweet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.syam.twitter.R
import app.syam.twitter.common.item.DividerItem
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.item.HeaderItem
import app.syam.twitter.home.viewmodel.HomeViewModel
import app.syam.twitter.listing.ListingActivity
import app.syam.twitter.profile.ProfileActivity
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.Tweet
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_tweets.*

const val TWEETS_LIST = "TWEETS_LIST"
const val USER = "USER"
const val TWEET = "TWEET"
const val SOURCE = "SOURCE"

class TweetsFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    private val tweetsAdapter = GroupAdapter<ViewHolder>()

    private val tweetsList by lazy {
        requireArguments().getParcelableArrayList<Tweet>(
            TWEETS_LIST
        ) ?: arrayListOf()
    }

    companion object {
        fun newInstance(tweetsList: List<Tweet>?) = TweetsFragment().apply {
            arguments = Bundle().apply {
                tweetsList?.let {
                    putParcelableArrayList(TWEETS_LIST, tweetsList as ArrayList)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tweets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tweetsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetsAdapter
            isNestedScrollingEnabled = false
        }

        swipeContainer.setColorSchemeResources(
            R.color.progress_orange,
            R.color.progress_green,
            R.color.progress_blue
        )

        swipeContainer.setOnRefreshListener {
            viewModel.tweets()
        }

        val user = SharedPreferenceManager.getLoggedInUser(context)

        tweetsAdapter.apply {
            add(HeaderItem(storedUser = user) {
                startActivity(
                    Intent(
                        context,
                        ProfileActivity::class.java
                    ).putExtra(
                        USER,
                        user?.userId
                    )
                )
            })
            add(DividerItem())
        }

        tweetsList.forEach {
            tweetsAdapter.apply {
                add(TweetHeader(
                    user = it.user,
                    optionsVisibility = View.GONE,
                    profileClicked = {
                        startActivity(
                            Intent(
                                context,
                                ProfileActivity::class.java
                            ).putExtra(
                                USER,
                                user?.userId
                            )
                        )
                    },
                    createdAt = it.createdAt ?: "Sometime in the past",
                    optionsClicked = {}
                ))
                add(
                    TweetBodyText(
                        textContent = it.tweet ?: "Unable to Fetch Tweet Contents"
                    )
                )
                it.imageUrl?.let { imageUrl ->
                    add(
                        TweetBodyImage(
                            url = imageUrl
                        )
                    )
                }
                add(TweetFooter(
                    isLiked = (it.likeList?.map { it.userId } ?: listOf<String>()).contains(user?.userId),
                    likedList = it.likeList ?: listOf(),
                    likeClicked = {
                        val mutableLikeList = it.likeList as MutableList
                        mutableLikeList.add(
                            LightWeightUser(
                                userId = user?.userId,
                                name = user?.name,
                                email = user?.email,
                                imageUrl = user?.imageUrl,
                                isVerified = user?.isVerified
                            )
                        )
                        viewModel.likeTweet(
                            tweetId = it.id.orEmpty(),
                            likeList = mutableLikeList
                        )
                    },
                    viewLikedClicked = {
                        startActivity(
                            Intent(
                                context,
                                ListingActivity::class.java
                            ).putExtra(
                                TWEET,
                                it
                            ).putExtra(
                                SOURCE,
                                "tweet"
                            )
                        )
                    }
                ))
                add(DividerItem())
            }
        }

    }


}