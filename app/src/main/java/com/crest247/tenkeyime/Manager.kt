package com.crest247.tenkeyime

import android.inputmethodservice.InputMethodService
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.crest247.tenkeyime.keyboard.ArrayKeyboard
import com.crest247.tenkeyime.keyboard.BopomofoKeyboard
import com.crest247.tenkeyime.keyboard.EnglishKeyboard
import com.crest247.tenkeyime.keyboard.SymbolKeyboard

class Manager(
    private val inputMethod: InputMethod
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

    private val arrayKeyboard: ArrayKeyboard =
        ArrayKeyboard(inputMethod, this, baseView.getChildAt(2) as FrameLayout)

    private val symbolKeyboard: SymbolKeyboard =
        SymbolKeyboard(inputMethod, this, baseView.getChildAt(3) as FrameLayout)

    private val bopomofoKeyboard: BopomofoKeyboard =
        BopomofoKeyboard(inputMethod, this, baseView.getChildAt(4) as FrameLayout)

    fun changeMode(mode: Int) {
        englishKeyboard.view.visibility = (1 shl (mode + 2)).inv().and(8)
        arrayKeyboard.view.visibility = (1 shl (mode + 1)).inv().and(8)
        symbolKeyboard.view.visibility = (1 shl (mode + 0)).inv().and(8)
        bopomofoKeyboard.view.visibility = (1 shl (mode - 1)).inv().and(8)
        inputMethod.keyboardType = mode
    }
}