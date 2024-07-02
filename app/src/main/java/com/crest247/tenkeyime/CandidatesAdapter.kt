package com.crest247.tenkeyime

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import java.io.BufferedReader
import java.io.InputStreamReader

class CandidatesAdapter(
    private val inputMethod: InputMethod,
    private val view: LinearLayout,
    private val inflater: LayoutInflater
) {

    private var candidateList = mutableListOf<String>()
    private val words: List<String>

    private var input = ""

    init {
        val inputStream = inputMethod.resources.openRawResource(R.raw.words)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val text = reader.use { it.readText() }
        words = text.lines()

        val textView = inflater.inflate(R.layout.item_input, view, false) as TextView
        textView.text = input
        view.addView(textView)
    }

    fun updateInput(key: Char) {
        input += key
        updateCandidates()
    }

    private fun updateCandidates() {
//        val regex = Regex("^(${input})\t(.+)")
        candidateList.clear()
        for (line in words) {
//            val matched = regex.matchAt(line, 0)
//            if (matched != null) {
//                val groups = matched.groupValues
//                candidateList.add(groups[2])
//            }
            if (line.startsWith("${input.lowercase()}\t")) {
                val word = line.substringAfter('\t')
                candidateList.add(word)
            }
        }
        updateView()
    }

    private fun updateView() {
        view.removeAllViews()

        val textView = inflater.inflate(R.layout.item_input, view, false) as TextView
        textView.text = input
        textView.setOnClickListener{
            inputMethod.commitText(textView.text.toString())
            resetInput()
        }
        view.addView(textView)

        for (candidate in candidateList) {
            val button = inflater.inflate(R.layout.item_candidate, view, false) as MaterialButton
            button.text = candidate
            button.setOnClickListener {
                inputMethod.commitText(button.text.toString())
                resetInput()
            }
            view.addView(button)
        }
    }

    fun deleteLast() {
        candidateList.clear()
        input = input.dropLast(1)
        updateCandidates()
    }

    private fun resetInput() {
        candidateList.clear()
        input = ""
        view.removeAllViews()
    }

    fun inputIsEmpty(): Boolean {
        return input.isEmpty()
    }

    fun candidatesIsEmpty(): Boolean {
        return view.childCount < 2
    }

    fun commitFirstCandidate() {
        view.getChildAt(1).callOnClick()
    }
}