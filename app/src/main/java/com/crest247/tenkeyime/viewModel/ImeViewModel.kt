package com.crest247.tenkeyime.viewModel

import android.util.TypedValue
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.button.MaterialButton
import java.io.Closeable

class ImeViewModel(vararg closeables: Closeable?) : ViewModel(*closeables) {
	val keyboardHeight: MutableLiveData<Int> = MutableLiveData()
	val fontSize: MutableLiveData<Float> = MutableLiveData()
}

object ImeBindingAdapters {
	@JvmStatic
	@BindingAdapter("android:layout_height")
	fun setLayoutHeight(view: View, heightDp: Int) {
		val heightPx = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, heightDp.toFloat(), view.context.resources.displayMetrics
		).toInt()
		view.layoutParams.height = heightPx
		view.requestLayout()
	}

	@JvmStatic
	@BindingAdapter("android:textSize")
	fun setTextSize(view: View, value: Float) {
		val button = view as MaterialButton
		button.textSize = value
	}
}