package app.syam.twitter.home.state

import app.syam.twitter.home.model.HomeResult

sealed class LikeCallState {
    object Success: LikeCallState()
    object Failed: LikeCallState()
    object InFlight: LikeCallState()
}