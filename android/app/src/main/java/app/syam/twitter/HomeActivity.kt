package app.syam.twitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.syam.twitter.common.storage.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val user = SharedPreferenceManager.getLoggedInUser(this)

        Picasso.get()
            .load(user?.imageUrl)
            .placeholder(R.drawable.twitter_png)
            .error(R.drawable.twitter_png)
            .into(profileImage)

        name.text = user.toString()

    }
}
