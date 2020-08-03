package app.syam.twitter.home.state

import app.syam.twitter.home.model.HomeResult

sealed class HomeCallState {
    data class Success(val result: HomeResult): HomeCallState()
    object Failed: HomeCallState()
    object InFlight: HomeCallState()
}