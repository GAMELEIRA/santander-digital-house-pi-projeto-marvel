package com.example.marvelworld.detailcard.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.marvelworld.R
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.repository.DetailCardRepository
import com.example.marvelworld.detailcard.viewmodel.DetailCardViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class DetailCardFragment(
    private val detailCard: DetailCard
) : Fragment() {

    private lateinit var cardView: MaterialCardView
    private lateinit var infoButton: ImageButton
    private lateinit var cardImage: ImageView
    private lateinit var cardTitle: TextView
    private lateinit var cardDescription: TextView
    private lateinit var cardDescriptionLayout: LinearLayout
    private lateinit var cardFavoriteButton: ImageButton
    private lateinit var detailCardViewModel: DetailCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardView = view.findViewById(R.id.expandable_card)
        infoButton = view.findViewById(R.id.detail_card_info_button)
        cardImage = view.findViewById(R.id.detail_card_image)
        cardTitle = view.findViewById(R.id.detail_card_title)
        cardDescription = view.findViewById(R.id.detail_card_description)
        cardDescriptionLayout = view.findViewById(R.id.detail_card_description_layout)
        cardFavoriteButton = view.findViewById(R.id.detail_card_favorite_button)

        detailCardViewModel = ViewModelProvider(
            this,
            DetailCardViewModel.DetailCardViewModelFactory(
                DetailCardRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(DetailCardViewModel::class.java)

        initDetailCard()
        initUrls()
        initImageDialog()

        cardFavoriteButton.setOnClickListener {
            detailCardViewModel.isFavorite(detailCard.resourceId, detailCard.type)
                .observe(viewLifecycleOwner, { isFavorite ->
                    if (isFavorite) {
                        detailCardViewModel.removeFavorite(detailCard.resourceId, detailCard.type)
                            .observe(viewLifecycleOwner, {
                                cardView.setCardBackgroundColor(context?.let {
                                    ContextCompat.getColorStateList(
                                        it,
                                        R.color.dark_grey
                                    )
                                })
                            })
                    } else {
                        detailCardViewModel.addFavorite(detailCard.resourceId, detailCard.type)
                            .observe(viewLifecycleOwner, {
                                cardView.setCardBackgroundColor(context?.let {
                                    ContextCompat.getColorStateList(
                                        it,
                                        R.color.colorPrimary
                                    )
                                })
                            })
                    }
                })
        }
    }

    private fun initDetailCard() {
        cardTitle.text = detailCard.title

        if (detailCard.description != null && detailCard.description.isNotBlank()) {
            cardDescription.text = detailCard.description
            cardTitle.setOnClickListener {
                if (cardDescriptionLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    cardDescriptionLayout.visibility = View.VISIBLE
                    cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_arrow_up_24, 0, 0, 0
                    )
                } else {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    cardDescriptionLayout.visibility = View.GONE
                    cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_baseline_keyboard_arrow_down_24, 0, 0, 0
                    )
                }
            }
        } else {
            cardTitle.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, 0, 0
            )
        }

        cardView.setCardBackgroundColor(context?.let {
            ContextCompat.getColorStateList(
                it,
                if (detailCard.isFavorite) R.color.colorPrimary else R.color.dark_grey
            )
        })

    }

    private fun initUrls() {
        if (detailCard.urls != null) {
            val urlsBottomDialogFragment = UrlsBottomDialogFragment(detailCard.urls)
            infoButton.setOnClickListener {
                urlsBottomDialogFragment.show(parentFragmentManager, "add_urls_dialog")
            }
            infoButton.visibility = View.VISIBLE
        }
    }

    private fun initImageDialog() {
        if (detailCard.imageCard != null) {
            Picasso.get().load(detailCard.imageCard).into(cardImage)
            cardImage.setOnClickListener {
                ImageDialogFragment(detailCard).show(parentFragmentManager, "add_image_dialog")
            }
            cardImage.visibility = View.VISIBLE
        }
    }
}