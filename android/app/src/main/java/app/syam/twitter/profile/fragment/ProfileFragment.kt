package app.syam.twitter.profile.fragment

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
import app.syam.twitter.listing.ListingActivity
import app.syam.twitter.profile.ProfileActivity
import app.syam.twitter.profile.item.ProfileHeader
import app.syam.twitter.profile.model.UpdateUser
import app.syam.twitter.profile.viewmodel.ProfileViewModel
import app.syam.twitter.tweet.fragment.*
import app.syam.twitter.tweet.item.TweetBodyImage
import app.syam.twitter.tweet.item.TweetBodyText
import app.syam.twitter.tweet.item.TweetFooter
import app.syam.twitter.tweet.item.TweetHeader
import app.syam.twitter.tweet.model.LightWeightUser
import app.syam.twitter.tweet.model.Tweet
import app.syam.twitter.tweet.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_tweets.swipeContainer

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()

    private val profileAdapter = GroupAdapter<ViewHolder>()

    private val tweetsList by lazy {
        requireArguments().getParcelableArrayList<Tweet>(
            TWEETS_LIST
        ) ?: arrayListOf()
    }

    private val receivedUser by lazy {
        requireArguments().getParcelable<User>(
            USER
        )
    }

    companion object {
        fun newInstance(tweetsList: List<Tweet>?, user: User?) = ProfileFragment().apply {
            arguments = Bundle().apply {
                tweetsList?.let {
                    putParcelableArrayList(TWEETS_LIST, tweetsList as ArrayList)
                    putParcelable(USER, user)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storedUser = SharedPreferenceManager.getLoggedInUser(context)

        profileRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
            isNestedScrollingEnabled = false
        }

        swipeContainer.setColorSchemeResources(
            R.color.progress_orange,
            R.color.progress_green,
            R.color.progress_blue
        )

        swipeContainer.setOnRefreshListener {
            viewModel.initProfileLoad(receivedUser?.userId.orEmpty())
        }

        val user = SharedPreferenceManager.getLoggedInUser(context)

        profileAdapter.apply {
            add(
                ProfileHeader(
                    user = receivedUser,
                    following = (receivedUser?.followerList?.map { it.userId } ?: listOf()).contains(user?.userId),
                    explicitFollowBlock = receivedUser?.userId == storedUser?.userId,
                    followersClicked = {
                        startActivity(
                            Intent(
                                context,
                                ListingActivity::class.java
                            ).putExtra(
                                USER,
                                receivedUser
                            ).putExtra(
                                SOURCE,
                                "profile"
                            ).putExtra(
                                TARGET,
                                "follower"
                            )
                        )
                    },
                    followingClicked = {
                        startActivity(
                            Intent(
                                context,
                                ListingActivity::class.java
                            ).putExtra(
                                USER,
                                receivedUser
                            ).putExtra(
                                SOURCE,
                                "profile"
                            ).putExtra(
                                TARGET,
                                "following"
                            )
                        )
                    },
                    followClicked = { type ->
                        val mutableFollowerList =
                            receivedUser?.followerList as MutableList<LightWeightUser>
                        if (type) {
                            mutableFollowerList.add(
                                LightWeightUser(
                                    userId = storedUser?.userId,
                                    name = storedUser?.name,
                                    email = storedUser?.email,
                                    imageUrl = storedUser?.imageUrl,
                                    isVerified = storedUser?.isVerified
                                )
                            )
                            viewModel.updateUser(
                                userId = storedUser?.userId.orEmpty(),
                                updateUser = UpdateUser(
                                    targetUserId = receivedUser?.userId,
                                    followerList = mutableFollowerList,
                                    followingList = mutableListOf()
                                ),
                                lightWeightUser = LightWeightUser(
                                    userId = receivedUser?.userId,
                                    name = receivedUser?.name,
                                    email = receivedUser?.email,
                                    imageUrl = receivedUser?.imageUrl,
                                    isVerified = receivedUser?.isVerified
                                )
                            )
                        } else {
                            mutableFollowerList.remove(mutableFollowerList.find { it.userId == storedUser?.userId })
                            viewModel.reverseUpdateUser(
                                userId = storedUser?.userId.orEmpty(),
                                updateUser = UpdateUser(
                                    targetUserId = receivedUser?.userId,
                                    followerList = mutableFollowerList,
                                    followingList = mutableListOf()
                                ),
                                receivedUserId = receivedUser?.userId.orEmpty()
                            )
                        }
                    }
                )
            )
            add(DividerItem())
        }

        tweetsList.forEach {
            profileAdapter.apply {
                add(TweetHeader(
                    user = it.user,
                    uploadVisibility = if (receivedUser?.userId == storedUser?.userId && it.imageUrl == null) View.VISIBLE else View.GONE,
                    profileClicked = {},
                    createdAt = it.createdAt ?: "Sometime in the past",
                    uploadClicked = {
                        // TODO: Write Image Upload logic here
                        viewModel.getUploadImageUrl(it.id.orEmpty())
                    }
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
                    isLiked = (it.likeList?.map { it.userId }
                        ?: listOf<String>()).contains(user?.userId),
                    likedList = it.likeList ?: listOf(),
                    likeClicked = { type ->
                        val mutableLikeList = it.likeList as MutableList
                        if (type)
                            mutableLikeList.add(
                                LightWeightUser(
                                    userId = user?.userId,
                                    name = user?.name,
                                    email = user?.email,
                                    imageUrl = user?.imageUrl,
                                    isVerified = user?.isVerified
                                )
                            )
                        else
                            mutableLikeList.remove(mutableLikeList.find { it.userId == user?.userId })
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