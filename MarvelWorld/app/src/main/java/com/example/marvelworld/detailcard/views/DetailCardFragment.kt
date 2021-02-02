package com.example.marvelworld.detailcard.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.marvelworld.BuildConfig
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.repository.DetailCardRepository
import com.example.marvelworld.detailcard.viewmodel.DetailCardViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream


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
    private lateinit var cardShareButton: ImageButton
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
        cardShareButton = view.findViewById(R.id.detail_card_share_button)

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
                                cardFavoriteButton.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_star_border_24
                                    )
                                cardView.setCardBackgroundColor(
                                    ContextCompat.getColorStateList(
                                        requireContext(),
                                        R.color.dark_grey
                                    )
                                )
                            })
                    } else {
                        detailCardViewModel.addFavorite(
                            detailCard.resourceId,
                            detailCard.type,
                            detailCard.title,
                            detailCard.thumbnail?.path,
                            detailCard.thumbnail?.extension
                        )
                            .observe(viewLifecycleOwner, {
                                cardFavoriteButton.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_star_24
                                    )
                                cardView.setCardBackgroundColor(
                                    ContextCompat.getColorStateList(
                                        requireContext(),
                                        R.color.colorPrimary
                                    )
                                )
                            })
                    }
                })
        }

        cardShareButton.setOnClickListener {
            shareResourceDetails()
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
        if (detailCard.isFavorite) {
            cardFavoriteButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_star_24)
            cardView.setCardBackgroundColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        } else {
            cardFavoriteButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_star_border_24)
            cardView.setCardBackgroundColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.dark_grey
                )
            )
        }
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
        if (detailCard.thumbnail != null) {
            Picasso.get().load(detailCard.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE))
                .into(cardImage)
            cardImage.setOnClickListener {
                ImageDialogFragment(detailCard).show(parentFragmentManager, "add_image_dialog")
            }
            cardImage.visibility = View.VISIBLE
        }
    }

    private fun shareResourceDetails() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        var extraText = detailCard.title + "\n\n"
        extraText += if (!detailCard.description.isNullOrEmpty()) {
            "${detailCard.description}\n\n"
        } else {
            ""
        }
        extraText += getString(R.string.share_msg)

        if (detailCard.thumbnail != null) {
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                    val uri = getImageUri(bitmap!!)
                    intent.putExtra(Intent.EXTRA_TEXT, extraText)
                    intent.putExtra(Intent.EXTRA_SUBJECT, extraText)
                    intent.type = "image/jpg"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    startActivity(Intent.createChooser(intent, "Share"))
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }

            Picasso.get().load(detailCard.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE))
                .into(target)
        } else {
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, extraText)
            startActivity(Intent.createChooser(intent, "Share"))
        }
    }

    @SuppressLint("SetWorldReadable")
    private fun getImageUri(bitmap: Bitmap): Uri {
        val child = File.separator + detailCard.title.replace("\\s".toRegex(), "") + ".jpg"
        val file = File(requireContext().externalCacheDir, child)
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        file.setReadable(true, false)

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }
}