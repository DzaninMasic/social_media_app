package com.example.social_media.common.model

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View

class EmailMaskTransformation() : TransformationMethod {
    override fun getTransformation(p0: CharSequence?, p1: View?): CharSequence {
        val masked = StringBuilder()
        for (i in 0 until (p0?.length ?: 0)) {
            val c: Char? = p0?.get(i)
            if (c == '@') {
                masked.append(p0.subSequence(i, p0.length))
                break
            } else {
                masked.append("*")
            }
        }
        return masked
    }

    override fun onFocusChanged(p0: View?, p1: CharSequence?, p2: Boolean, p3: Int, p4: Rect?) {}

}