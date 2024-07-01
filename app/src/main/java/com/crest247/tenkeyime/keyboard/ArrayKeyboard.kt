package com.crest247.tenkeyime.keyboard

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.FrameLayout
import com.crest247.tenkeyime.InputMethod
import com.crest247.tenkeyime.Manager
import com.crest247.tenkeyime.databinding.KeyboardArrayBinding
import com.crest247.tenkeyime.viewModel.ImeViewModel
import com.google.android.material.button.MaterialButton

@SuppressLint("ClickableViewAccessibility")
class ArrayKeyboard(
	inputMethod: InputMethod,
	manager: Manager,
	view: FrameLayout,
	varsBinding: KeyboardArrayBinding
) :
	BaseKeyboard(inputMethod, view, createCharKeysMap(), createFuncKeysMap(), manager) {

	private val arrayViewModel = ImeViewModel()

	init {
		varsBinding.viewModel = arrayViewModel
		arrayViewModel.fontSize.value = 36.0f
	}

	override fun onCharButtonTouch(
		button: MaterialButton,
		motionEvent: MotionEvent,
		name: String
	): Boolean {
		return when (motionEvent.action) {
			MotionEvent.ACTION_DOWN -> {
				button.isPressed = true
				true
			}

			MotionEvent.ACTION_UP -> {
				manager.candidatesAdapter.updateInput(button.text[0])
				button.isPressed = false
				true
			}

			else -> {
				false
			}
		}
	}

	override fun onFuncButtonTouch(
		button: MaterialButton,
		motionEvent: MotionEvent,
		type: FuncKeys
	): Boolean {
		return when (motionEvent.action) {
			MotionEvent.ACTION_DOWN -> {
				button.isPressed = true
				when (type) {
					FuncKeys.Back -> handler.postDelayed(backKeyDown, delayMillis)
					else -> {}
				}
				true
			}

			MotionEvent.ACTION_UP -> {
				button.isPressed = false
				when (type) {
					FuncKeys.Back -> handler.removeCallbacks(backKeyDown)
					FuncKeys.Mode2 -> manager.changeMode(1)
					else -> {}
				}
				true
			}

			else -> {
				false
			}
		}
	}
}

private fun createFuncKeysMap(): Map<BaseKeyboard.FuncKeys, String> {
	val result = mutableMapOf<BaseKeyboard.FuncKeys, String>()
	result[BaseKeyboard.FuncKeys.Shift] = "⇧"
	result[BaseKeyboard.FuncKeys.Back] = "⌫"
	result[BaseKeyboard.FuncKeys.Mode] = "!#1"
	result[BaseKeyboard.FuncKeys.Mode2] = "abc"
	result[BaseKeyboard.FuncKeys.Space] = " "
	result[BaseKeyboard.FuncKeys.Enter] = "⏎"
	return result
}

private fun createCharKeysMap(): Map<String, String> {
	val letterMapping = ('a'..'z').associate { it.toString() to it.lowercase() }
	val specialCharMapping = mapOf(
		"Semicolon" to ";",
		"Comma" to ",",
		"Period" to ".",
		"Slash" to "/"
	)
	val digitMapping = ('0'..'9').associate { it.toString() to it.toString() }
	return letterMapping + specialCharMapping + digitMapping
}
