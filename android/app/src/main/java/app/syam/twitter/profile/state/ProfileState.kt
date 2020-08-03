package app.syam.twitter.profile.state

import app.syam.twitter.home.model.TweetResult
import app.syam.twitter.profile.model.ProfileResult

sealed class ProfileState {
    data class Success(val result: Pair<TweetResult, ProfileResult>): ProfileState()
    object Failed: ProfileState()
    object InFlight: ProfileState()
}