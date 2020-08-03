package app.syam.twitter.home.state

sealed class GeneralCallState {
    object Success: GeneralCallState()
    object Failed: GeneralCallState()
    object InFlight: GeneralCallState()
}