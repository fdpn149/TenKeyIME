package com.crest247.tenkeyime.keyboard

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.crest247.tenkeyime.InputMethod
import com.crest247.tenkeyime.Manager
import com.google.android.material.button.MaterialButton

@SuppressLint("ClickableViewAccessibility")
abstract class BaseKeyboard(
	protected val inputMethod: InputMethod,
	val view: FrameLayout,
	protected val charKeysMap: Map<String, String>,
	funcKeysMap: Map<FuncKeys, String>,
	protected val manager: Manager
) {

	val constraintLayout = view.getChildAt(0) as ConstraintLayout

	protected val handler = Handler(Looper.getMainLooper())
	protected val delayMillis = 50L

	enum class FuncKeys {
		Shift, Back, Mode, Mode2, Space, Enter
	}

	init {
		for (child in constraintLayout.children) {
			if (child is MaterialButton) {
				val tag = child.tag.toString()
				val typeName = splitButtonTag(tag)
				val type = typeName.first
				val name = typeName.second

				when (type) {
					"char" -> {
						child.text = charKeysMap[name]
						child.setOnTouchListener { _: View, motionEvent: MotionEvent ->
							onCharButtonTouch(child, motionEvent, name)
						}
					}

					"func" -> {
						val funcKey = enumValueOf<FuncKeys>(name)
						child.text = funcKeysMap[funcKey].toString()
						child.setOnTouchListener { _: View, motionEvent: MotionEvent ->
							onFuncButtonTouch(child, motionEvent, funcKey)
						}
					}
				}
			}
		}
	}

	open fun onCharButtonTouch(
		button: MaterialButton,
		motionEvent: MotionEvent,
		name: String
	): Boolean {
		return false
	}

	open fun onFuncButtonTouch(
		button: MaterialButton,
		motionEvent: MotionEvent,
		type: FuncKeys
	): Boolean {
		return false
	}

	protected fun deleteLastInput() {
		if(manager.candidatesAdapter.inputIsEmpty())
			inputMethod.sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL)
		else
			deleteCandidateInput()
	}

	private fun deleteCandidateInput() {
		manager.candidatesAdapter.deleteLast()
	}

	protected val backKeyDown = object : Runnable {
		override fun run() {
			deleteLastInput()
			handler.postDelayed(this, delayMillis * 2)
		}
	}

	protected fun splitButtonTag(tag: String): Pair<String, String> {
		val type = tag.substringBefore('_')
		val name = tag.substringAfter('_')
		return type to name
	}
}