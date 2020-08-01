package app.syam.twitter.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import app.syam.twitter.HomeActivity
import app.syam.twitter.R
import app.syam.twitter.util.EXTRA_ACCESS_TOKEN
import app.syam.twitter.util.EXTRA_CLEAR_CREDENTIALS
import app.syam.twitter.util.EXTRA_ID_TOKEN
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val auth0 by lazy { Auth0(this) }
    private val credentialsManager by lazy {
        SecureCredentialsManager(
            this,
            AuthenticationAPIClient(auth0),
            SharedPreferencesStorage(this)
        )
    }
    private val authenticationAPIClient by lazy { AuthenticationAPIClient(auth0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton.setOnClickListener { doLogin() }
        auth0.isOIDCConformant = true

        if (intent.getBooleanExtra(EXTRA_CLEAR_CREDENTIALS, false)) {
            doLogout()
            return
        }
        if (credentialsManager.hasValidCredentials()) {
            showNextActivity()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (credentialsManager.checkAuthenticationResult(requestCode, resultCode)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showNextActivity() {
        credentialsManager.getCredentials(object :
            BaseCallback<Credentials, CredentialsManagerException?> {
            override fun onSuccess(credentials: Credentials) {
                getProfile(credentials)
            }

            override fun onFailure(error: CredentialsManagerException?) {
                finish()
            }
        })
    }

    private fun getProfile(credentials: Credentials) {
        val accessToken = credentials.accessToken.orEmpty()
        val usersClient = UsersAPIClient(auth0, accessToken)
        authenticationAPIClient.userInfo(accessToken)
            .start(object : BaseCallback<UserProfile, AuthenticationException?> {
                override fun onSuccess(userinfo: UserProfile) {
                    usersClient.getProfile(userinfo.id)
                        .start(object : BaseCallback<UserProfile, ManagementException?> {
                            override fun onSuccess(profile: UserProfile) {
                                // TODO: Here we have both `profile` and `credentials` -> write it to shared preference

                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                finish()

                            }

                            override fun onFailure(error: ManagementException?) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "User Profile Request Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                }

                override fun onFailure(error: AuthenticationException?) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "User Info Request Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun doLogin() {
        WebAuthProvider.login(auth0)
            .withScheme("demo")
            .withAudience(
                String.format(
                    "https://%s/api/v2/",
                    getString(R.string.com_auth0_domain)
                )
            )
            .withScope("openid profile email offline_access read:current_user update:current_user_metadata")
            .start(this, loginCallback)
    }

    private fun doLogout() {
        WebAuthProvider.logout(auth0)
            .withScheme("demo")
            .start(this, logoutCallback)
    }

    private val loginCallback: AuthCallback = object : AuthCallback {
        override fun onFailure(@NonNull dialog: Dialog) {
            runOnUiThread { dialog.show() }
        }

        override fun onFailure(exception: AuthenticationException) {
            runOnUiThread {
                Toast.makeText(
                    this@LoginActivity,
                    "Log In - Error Occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onSuccess(@NonNull credentials: Credentials) {
            credentialsManager.saveCredentials(credentials)
            showNextActivity()
        }
    }

    private val logoutCallback: VoidCallback = object : VoidCallback {
        override fun onSuccess(payload: Void) {
            credentialsManager.clearCredentials()
        }

        override fun onFailure(error: Auth0Exception) {
            showNextActivity()
        }
    }
}
