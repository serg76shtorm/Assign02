package com.sserhiichyk.assign02.extensions

import androidx.core.net.toUri
import android.widget.ImageView
import coil.load
import com.bumptech.glide.Glide
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.squareup.picasso.Picasso

fun ImageView.loadImageLibraries(url: String, lib: Int) {
    when (lib) {
        // some magic numbers
        //loadImageWithGlide
        0 -> loadImageWithGlide(url)
        //loadImageWithPicasso
        1 -> loadImageWithPicasso(url)
        //loadImageWithUImageLoader
        2 -> loadImageWithUImageLoader(url)
        //loadImageWithCoin
        3 -> loadImageWithCoin(url)
    }
}

fun ImageView.loadImageWithGlide(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

fun ImageView.loadImageWithPicasso(url: String) {
    Picasso.get()
        .load(url)
        .into(this)
}

fun ImageView.loadImageWithUImageLoader(url: String) {
    val imageLoader: ImageLoader = ImageLoader.getInstance()
    imageLoader.init(ImageLoaderConfiguration.createDefault(context))

    imageLoader.displayImage(url, this)
}

fun ImageView.loadImageWithCoin(url: String) {
    load(url)
}

fun ImageView.loadImageWithFresco(url: String) {
    setImageURI(url.toUri())
}

fun ImageView.loadImage(url: String) {
    loadImageWithPicasso(url)
}
