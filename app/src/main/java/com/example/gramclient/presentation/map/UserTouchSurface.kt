package com.example.gramclient.presentation.map

import TwoFingerDrag
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class UserTouchSurface : View {
    private var a: TwoFingerDrag? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return a?.onTouchEvent(event) ?: false
    }

    fun setCallback(a: TwoFingerDrag?) {
        this.a = a
    }
}