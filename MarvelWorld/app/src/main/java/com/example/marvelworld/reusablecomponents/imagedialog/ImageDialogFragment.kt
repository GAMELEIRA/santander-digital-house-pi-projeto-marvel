package com.example.marvelworld.reusablecomponents.imagedialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.marvelworld.R
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.squareup.picasso.Picasso


class ImageDialogFragment(private val card: Card) : DialogFragment() {

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as LayoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.image_dialog, container, false)

        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            this.dismiss()
        }

        val title = view.findViewById<TextView>(R.id.image_dialog_title)
        title.text = card.title

        val image = view.findViewById<ImageView>(R.id.image_dialog_image)
        Picasso.get().load(card.imageDialog).into(image)

        return view
    }
}