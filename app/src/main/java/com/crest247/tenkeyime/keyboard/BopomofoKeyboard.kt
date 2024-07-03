package com.crest247.tenkeyime.keyboard

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.children
import com.crest247.tenkeyime.InputMethod
import com.crest247.tenkeyime.Manager
import com.google.android.material.button.MaterialButton

@SuppressLint("ClickableViewAccessibility")
class BopomofoKeyboard(
    inputMethod: InputMethod,
    manager: Manager,
    view: FrameLayout,
) :
    BaseKeyboard(inputMethod, view, createCharKeysMap(), createFuncKeysMap(), manager) {

    init {

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
                    FuncKeys.Back -> handler.removeCallbacks(backKeyDown)
                    FuncKeys.Space -> {
                        if (manager.candidatesAdapter.inputIsEmpty())
                            inputMethod.currentInputConnection.commitText(" ", 1)
                        else if (manager.candidatesAdapter.candidatesIsEmpty())
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

                    FuncKeys.Mode -> manager.changeMode(1)
                    FuncKeys.Mode2 -> manager.changeMode(2)

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
    result[BaseKeyboard.FuncKeys.Back] = "⌫"
    result[BaseKeyboard.FuncKeys.Mode] = "abc"
    result[BaseKeyboard.FuncKeys.Mode2] = "行"
    result[BaseKeyboard.FuncKeys.Space] = " "
    result[BaseKeyboard.FuncKeys.Enter] = "⏎"
    return result
}

private fun createCharKeysMap(): Map<String, String> {
    val list = arrayOf(
        "ㄅ",
        "ㄆ",
        "ㄇ",
        "ㄈ",
        "ㄉ",
        "ㄊ",
        "ㄋ",
        "ㄌ",
        "ㄍ",
        "ㄎ",
        "ㄏ",
        "ㄐ",
        "ㄑ",
        "ㄒ",
        "ㄓ",
        "ㄔ",
        "ㄕ",
        "ㄖ",
        "ㄗ",
        "ㄘ",
        "ㄙ",
        "ㄧ",
        "ㄨ",
        "ㄩ",
        "ㄚ",
        "ㄛ",
        "ㄜ",
        "ㄝ",
        "ㄞ",
        "ㄟ",
        "ㄠ",
        "ㄡ",
        "ㄢ",
        "ㄣ",
        "ㄤ",
        "ㄥ",
        "ㄦ",
        "ˇ",
        "ˋ",
        "ˊ",
        "˙"
    )
    val letterMapping = (1..41).associate { it.toString() to list[it - 1] }
    return letterMapping
}

