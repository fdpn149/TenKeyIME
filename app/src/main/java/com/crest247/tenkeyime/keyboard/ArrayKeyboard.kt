package com.crest247.tenkeyime.keyboard

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.children
import com.crest247.tenkeyime.InputMethod
import com.crest247.tenkeyime.Manager
//import com.crest247.tenkeyime.databinding.KeyboardArrayBinding
//import com.crest247.tenkeyime.viewModel.ImeViewModel
import com.google.android.material.button.MaterialButton

@SuppressLint("ClickableViewAccessibility")
class ArrayKeyboard(
	inputMethod: InputMethod,
	manager: Manager,
	view: FrameLayout,
//	varsBinding: KeyboardArrayBinding
) :
	BaseKeyboard(inputMethod, view, createCharKeysMap(), createFuncKeysMap(), manager) {

	private var shifted = false
	private var shiftKeysMap: Map<String, String>
//	private val arrayViewModel = ImeViewModel()

	init {
//		varsBinding.viewModel = arrayViewModel
//		arrayViewModel.fontSize.value = 36.0f

		val symbols = arrayOf(")", "!", "@", "#", "$", "%", "^", "&", "*", "(")
		val shiftDigitsMap = (0..9).associate { it.toString() to symbols[it] }
		val shiftCharsMap = ('a'..'z').associate { it.toString() to it.uppercase() }.toMutableMap()
		shiftKeysMap =
			shiftDigitsMap + shiftCharsMap + mapOf<String, String>("," to "<", "." to ">", ";" to ":", "/" to "?")
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
					FuncKeys.Shift -> {
						shifted = true
						caseChange()
					}

					FuncKeys.Back -> handler.postDelayed(backKeyDown, delayMillis)
					FuncKeys.Enter -> inputMethod.currentInputConnection?.sendKeyEvent(
						KeyEvent(
							KeyEvent.ACTION_DOWN,
							KeyEvent.KEYCODE_ENTER
						)
					)

					else -> {}
				}
				true
			}

			MotionEvent.ACTION_UP -> {
				button.isPressed = false
				when (type) {
					FuncKeys.Shift -> {
						shifted = false
						caseChange()
					}

					FuncKeys.Back -> handler.removeCallbacks(backKeyDown)
					FuncKeys.Space -> {
						if(manager.candidatesAdapter.inputIsEmpty())
							inputMethod.currentInputConnection.commitText(" ", 1)
						else if(manager.candidatesAdapter.candidatesIsEmpty())
							manager.candidatesAdapter.updateInput(button.text[0])
						else
							manager.candidatesAdapter.commitFirstCandidate()
					}
					FuncKeys.Enter -> inputMethod.currentInputConnection?.sendKeyEvent(
						KeyEvent(
							KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_ENTER
						)
					)

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

	private fun caseChange() {
		for (child in constraintLayout.children) {
			if (child is MaterialButton) {
				val tag = child.tag.toString()
				val type = tag.substringBefore('_')
				val name = tag.substringAfter('_')

				when (type) {
					"char" -> {
						if (shifted)
							child.text = shiftKeysMap[charKeysMap[name]]
						else
							child.text = charKeysMap[name]
					}
				}
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

