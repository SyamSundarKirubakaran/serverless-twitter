package app.syam.twitter.profile.state

import app.syam.twitter.profile.model.SignedUrl

sealed class SignedUrlState {
    data class Success(val result: SignedUrl): SignedUrlState()
    object Failed: SignedUrlState()
    object InFlight: SignedUrlState()
}