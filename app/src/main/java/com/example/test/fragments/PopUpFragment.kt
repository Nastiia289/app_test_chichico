package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.test.R


class PopUpFragment(private val title: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.popup, container, false)
        rootView.setBackgroundResource(R.drawable.shape) //
        return rootView
    }

    companion object {
        fun newInstance(title: String): PopUpFragment {
            return PopUpFragment(title)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val closePopUp = view.findViewById<Button>(R.id.popup_button)

        val popUpTitleText = view.findViewById<TextView>(R.id.popup_text)
        popUpTitleText.text = title

        closePopUp.setOnClickListener{
            dismiss()
        }
    }
}