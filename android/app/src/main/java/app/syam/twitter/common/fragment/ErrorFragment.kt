package app.syam.twitter.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.syam.twitter.R
import kotlinx.android.synthetic.main.fragment_empty.*

const val ERROR_STRING = "ERROR_STRING"

class EmptyFragment : Fragment() {

    // TODO: Implement Retry here

    private val receivedText by lazy { requireArguments().getString(ERROR_STRING) ?: "" }

    companion object {
        fun newInstance(errorText: String) = EmptyFragment().apply {
            arguments = Bundle().apply {
                putString(ERROR_STRING, errorText)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_empty, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchingText.text = receivedText
    }

}