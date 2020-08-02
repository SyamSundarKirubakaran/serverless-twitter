package app.syam.twitter.common.model

data class User(
    val userId: String,
    val token: String,
    val name: String,
    val email: String,
    val imageUrl: String,
    val isVerified: Boolean
)