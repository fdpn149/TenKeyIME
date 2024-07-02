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
class SymbolKeyboard(inputMethod: InputMethod, manager: Manager, view: FrameLayout) :
    BaseKeyboard(inputMethod, view, createCharKeysMap(), createFuncKeysMap(), manager) {

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
                inputMethod.currentInputConnection.commitText(button.text, 1)
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
                    FuncKeys.Space -> inputMethod.currentInputConnection.commitText("　", 1)
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
    result[BaseKeyboard.FuncKeys.Mode2] = "中"
    result[BaseKeyboard.FuncKeys.Space] = "　"
    result[BaseKeyboard.FuncKeys.Enter] = "⏎"
    return result
}

private fun createCharKeysMap(): Map<String, String> {
    val symbols = arrayOf(
        "~",
        "_",
        "+",
        "{",
        "}",
        "|",
        ":",
        "\"",
        "?",
        "°",
        "`",
        "-",
        "=",
        "[",
        "]",
        "\\",
        "•",
        "'",
        "/",
        ";",
        "😂",
        "😮",
        "🥳",
        "🤔",
        "😢",
        "👍",
        "🈶",
        "🉑",
        "⁉",
        "🆒",
        "🤣",
        "🤯",
        "🤩",
        "🙃",
        "😵‍💫",
        "👌",
        "🈚",
        "💯",
        "❔",
        "🆗"
    )
    val symbolMapping = (11..50).associate { it.toString() to symbols[it - 11] }
    return symbolMapping
}
