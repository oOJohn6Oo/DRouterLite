package com.example.mylibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import io.john6.router.drouterlite.annotation.Router


@Router(path = "/mylibrary/test_dialog")
class MyDialogFragment : DialogFragment(){
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            addView(TextView(context).apply {
                text = "Test Dialog\nmsg:${arguments?.getString("msg")}"
            })
        }
    }
}