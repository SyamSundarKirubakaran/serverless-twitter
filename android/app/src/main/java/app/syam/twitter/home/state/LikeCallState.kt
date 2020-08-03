package app.syam.twitter.home.state

sealed class LikeCallState {
    object Success: LikeCallState()
    object Failed: LikeCallState()
    object InFlight: LikeCallState()
}