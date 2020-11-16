package com.hend.searchproductschallenge.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hend.searchproductschallenge.R
import kotlinx.coroutines.Job

/**
 * Extension to cancel current job if active
 */
fun Job?.cancelIfActive() {
    if (this?.isActive == true)
        cancel()
}

/**
 * Extension to render and cash imageUrl into ImageView
 */
fun ImageView.loadImageUrl(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.placeholder)
        .into(this)
}

