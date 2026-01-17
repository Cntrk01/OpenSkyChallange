package com.challange.openskychallange.common.extensions

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Context.bitmapDescriptorFromResource(resId: Int, width: Int? = null, height: Int? = null): BitmapDescriptor {
    val bitmap = BitmapFactory.decodeResource(resources, resId)
    val scaledBitmap = if (width != null && height != null) {
        bitmap.scale(width, height, false)
    } else bitmap
    return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}