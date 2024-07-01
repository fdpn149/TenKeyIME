package com.crest247.tenkeyime

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.button.MaterialButton

class SwipeButton(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

//	constructor(context: Context) : super(context){
//
//	}

	init {
		val typedArray =
			context.obtainStyledAttributes(attrs, R.styleable.SwipeButton, 0, 0)
		inflate(context, R.layout.component_swipebutton, this)
		(getChildAt(0) as MaterialButton).text = typedArray.getString(R.styleable.SwipeButton_centerText) ?: "center"
		(getChildAt(1) as TextView).text = typedArray.getString(R.styleable.SwipeButton_startText) ?: "start"
		(getChildAt(2) as TextView).text = typedArray.getString(R.styleable.SwipeButton_topText) ?: "top"
		(getChildAt(3) as TextView).text = typedArray.getString(R.styleable.SwipeButton_endText) ?: "end"
		(getChildAt(4) as TextView).text = typedArray.getString(R.styleable.SwipeButton_bottomText) ?: "bottom"
	}

//	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//		context,
//		attrs,
//		defStyleAttr
//	) {
//		val typedArray =
//			context.obtainStyledAttributes(attrs, R.styleable.SwipeButton, defStyleAttr, 0)
//		inflate(context, R.layout.component_swipebutton, this)
//		var child = getChildAt(0)
//		if (child is TextView) {
//			child.text = typedArray.getString(R.styleable.SwipeButton_centerText) ?: centerText
//		}
//	}

//	constructor(
//		context: Context,
//		attrs: AttributeSet?,
//		defStyleAttr: Int,
//		defStyleRes: Int
//	) : super(context, attrs, defStyleAttr, defStyleRes) {
//		val typedArray =
//			context.obtainStyledAttributes(attrs, R.styleable.SwipeButton, defStyleAttr, defStyleRes)
//		inflate(context, R.layout.component_swipebutton, this)
//		var child = getChildAt(0)
//		if (child is TextView) {
//			child.text = typedArray.getString(R.styleable.SwipeButton_centerText) ?: centerText
//		}
//	}
}