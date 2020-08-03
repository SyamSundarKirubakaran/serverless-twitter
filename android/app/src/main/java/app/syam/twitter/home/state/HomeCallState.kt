package app.syam.twitter.home.state

import app.syam.twitter.home.model.TweetResult

sealed class HomeCallState {
    data class Success(val result: TweetResult): HomeCallState()
    object Failed: HomeCallState()
    object InFlight: HomeCallState()
}