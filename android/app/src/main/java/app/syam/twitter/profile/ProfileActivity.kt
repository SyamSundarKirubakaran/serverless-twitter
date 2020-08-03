package app.syam.twitter.profile

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.syam.twitter.R
import app.syam.twitter.common.fragment.EmptyFragment
import app.syam.twitter.common.fragment.InProgressFragment
import app.syam.twitter.common.storage.SharedPreferenceManager
import app.syam.twitter.home.state.GeneralCallState
import app.syam.twitter.profile.fragment.ProfileFragment
import app.syam.twitter.profile.state.ProfileState
import app.syam.twitter.profile.state.SignedUrlState
import app.syam.twitter.profile.viewmodel.ProfileViewModel
import app.syam.twitter.tweet.fragment.USER
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import java.io.File
import java.io.IOException


const val PICK_IMAGE = 931

class ProfileActivity : AppCompatActivity() {

    lateinit var viewModelCopy: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.extras?.getString(USER)

        val storedUser = SharedPreferenceManager.getLoggedInUser(this)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        val viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        viewModelCopy = viewModel

        viewModel.initProfileLoad(userId.orEmpty())
        viewModel.createImageDir(cacheDir)

        viewModel.profileLiveData.observe(this, Observer {
            when (it) {
                is ProfileState.Success -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            ProfileFragment.newInstance(
                                tweetsList = it.result.first.result,
                                user = it.result.second.result
                            )
                        )
                        .commitAllowingStateLoss()
                }
                ProfileState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                ProfileState.Failed -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            EmptyFragment.newInstance(errorText = "Error Fetching Profile")
                        )
                        .commitAllowingStateLoss()
                }
            }
        })

        viewModel.updateLiveData.observe(this, Observer {
            when (it) {
                GeneralCallState.Success -> {
                    viewModel.initProfileLoad(userId.orEmpty())
                }
                GeneralCallState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                GeneralCallState.Failed -> {
                    viewModel.initProfileLoad(userId.orEmpty())
                }
            }
        })

        viewModel.signedUrlLiveData.observe(this, Observer {
            when (it) {
                is SignedUrlState.Success -> {
                    dispatchImagePicker(viewModel)
                }
                SignedUrlState.InFlight -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.profileFrame,
                            InProgressFragment()
                        )
                        .commitAllowingStateLoss()
                }
                SignedUrlState.Failed -> {
                    viewModel.initProfileLoad(userId.orEmpty())
                }
            }
        })

        viewModel.uploadLiveData.observe(this, Observer {
            viewModel.initProfileLoad(userId.orEmpty())
        })
    }

    private fun dispatchImagePicker(viewModel: ProfileViewModel) {
        try {
            var photoFile: File? = null
            try {
                photoFile = viewModel.createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this,
                    "app.syam.twitter.fileprovider",
                    photoFile)

                val getIntent = Intent(Intent.ACTION_GET_CONTENT)
                getIntent.type = "image/*"

                val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(getIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickIntent)

                chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(chooserIntent, PICK_IMAGE)
            }
        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data == null) {
                    Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
                    return
                }
                viewModelCopy.processLocalImage(contentResolver = contentResolver,
                    token = SharedPreferenceManager.getLoggedInUser(this)?.token.orEmpty(),
                    uri = data.data)
            }
        }
    }

}
