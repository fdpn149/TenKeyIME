package com.crest247.tenkeyime

import android.inputmethodservice.InputMethodService
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crest247.tenkeyime.databinding.ActivityImeBinding
import com.crest247.tenkeyime.keyboard.ArrayKeyboard
import com.crest247.tenkeyime.keyboard.EnglishKeyboard
import com.crest247.tenkeyime.viewModel.ImeViewModel

class Manager(
	inputMethod: InputMethod
) {
	private var contextThemeWrapper: ContextThemeWrapper =
		ContextThemeWrapper(inputMethod, R.style.Theme_TenKeyIME)
	private var inflater: LayoutInflater =
		contextThemeWrapper.getSystemService(InputMethodService.LAYOUT_INFLATER_SERVICE) as LayoutInflater

	// Views
	val baseView: LinearLayout = inflater.inflate(R.layout.activity_ime, null) as LinearLayout
	private val candidatesView: LinearLayout = baseView.findViewById(R.id.candidatesView)
	val candidatesAdapter = CandidatesAdapter(inputMethod, candidatesView, inflater)

	private val englishKeyboard =
		EnglishKeyboard(inputMethod, this, baseView.getChildAt(1) as FrameLayout)
	private val arrayKeyboard: ArrayKeyboard

	private val varsBinding: ActivityImeBinding = DataBindingUtil.bind(baseView)!!
	private val imeViewModel = ImeViewModel()

	init {
//		candidatesView.adapter = candidatesAdapter
//		candidatesView.layoutManager =
//			LinearLayoutManager(inputMethod, LinearLayoutManager.HORIZONTAL, false)

		varsBinding.viewModel = imeViewModel
		imeViewModel.keyboardHeight.value = 300

		arrayKeyboard = ArrayKeyboard(inputMethod, this, baseView.getChildAt(2) as FrameLayout, varsBinding.keyboardArray)
	}

	fun changeMode(mode: Int) {
		englishKeyboard.view.visibility = (1 shl (mode + 2)).inv().and(8)
		arrayKeyboard.view.visibility = (1 shl (mode + 1)).inv().and(8)
	}
}