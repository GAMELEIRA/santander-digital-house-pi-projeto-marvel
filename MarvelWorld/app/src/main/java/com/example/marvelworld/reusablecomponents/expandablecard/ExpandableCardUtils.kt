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
import com.example.marvelworld.characterlist.models.Character
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

object ExpandableCardUtils {
    fun initCard(view: View, character: Character, supportFragmentManager: FragmentManager) {
        val card: MaterialCardView = view.findViewById(R.id.expandable_card)
        val infoButton: ImageButton = view.findViewById(R.id.expandable_card_info_button)
        val cardImage: ImageView = view.findViewById(R.id.expandable_card_image)
        val cardTitle: TextView = view.findViewById(R.id.expandable_card_title)
        val cardDescription: TextView = view.findViewById(R.id.expandable_card_description)
        val cardDescriptionLayout: LinearLayout =
            view.findViewById(R.id.expandable_card_description_layout)
        val urlsBottomDialogFragment = UrlsBottomDialogFragment(character.urls)

        infoButton.setOnClickListener{
            urlsBottomDialogFragment.show(supportFragmentManager,"add_urls_dialog")
        }

        Picasso.get().load(character.thumbnail.getImagePath()).into(cardImage)
        cardTitle.text = character.name
        if(character.description.isNotBlank()) {
            cardDescription.text = character.description
            cardTitle.setOnClickListener {
                if (cardDescriptionLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
                    cardDescriptionLayout.visibility = View.VISIBLE
                    cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_arrow_up_24,
                        0,
                        0,
                        0
                    )
                } else {
                    TransitionManager.beginDelayedTransition(card, AutoTransition())
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