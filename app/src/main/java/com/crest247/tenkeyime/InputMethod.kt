package com.crest247.tenkeyime

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo


class InputMethod : InputMethodService() {

	private lateinit var manager: Manager

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreateInputView(): View {
		manager = Manager(this)
		return manager.baseView
	}

	override fun onStartInputView(editorInfo: EditorInfo?, restarting: Boolean) {
		super.onStartInputView(editorInfo, restarting)

	}

	fun commitText(text: String) {
		val inputConnection = currentInputConnection
		inputConnection?.commitText(text, text.length)
	}
}