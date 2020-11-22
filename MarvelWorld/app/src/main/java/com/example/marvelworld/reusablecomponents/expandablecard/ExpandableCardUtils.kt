package com.example.marvelworld.reusablecomponents.expandablecard

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.marvelworld.R
import com.example.marvelworld.reusablecomponents.urlsdialog.UrlsBottomDialogFragment
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

object ExpandableCardUtils {
    fun initCard(view: View, card: Card, supportFragmentManager: FragmentManager) {
        val cardView: MaterialCardView = view.findViewById(R.id.expandable_card)
        val infoButton: ImageButton = view.findViewById(R.id.expandable_card_info_button)
        val cardImage: ImageView = view.findViewById(R.id.expandable_card_image)
        val cardTitle: TextView = view.findViewById(R.id.expandable_card_title)
        val cardDescription: TextView = view.findViewById(R.id.expandable_card_description)
        val cardDescriptionLayout: LinearLayout =
            view.findViewById(R.id.expandable_card_description_layout)
        val urlsBottomDialogFragment = UrlsBottomDialogFragment(card.urls)

        infoButton.setOnClickListener {
            urlsBottomDialogFragment.show(supportFragmentManager, "add_urls_dialog")
        }

        Picasso.get().load(card.image).into(cardImage)
        cardTitle.text = card.title
        if (card.description != null && card.description.isNotBlank()) {
            cardDescription.text = card.description
            cardTitle.setOnClickListener {
                if (cardDescriptionLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    cardDescriptionLayout.visibility = View.VISIBLE
                    cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_arrow_up_24,
                        0,
                        0,
                        0
                    )
                } else {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    cardDescriptionLayout.visibility = View.GONE
                    cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_arrow_down_24,
                        0,
                        0,
                        0
                    )
                }
            }
        } else {
            cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
        }
    }
}