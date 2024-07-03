package com.crest247.tenkeyime

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import java.io.BufferedReader
import java.io.InputStreamReader

class CandidatesAdapter(
    private val inputMethod: InputMethod,
    private val view: LinearLayout,
    private val inflater: LayoutInflater
) {

    private var candidateList = mutableListOf<String>()
    private val arrayWords: List<String>
    private val bopomofoWords: List<String>


    private var input = ""

    init {
        arrayWords = initWordsList(R.raw.array)
        bopomofoWords = initWordsList(R.raw.bopomofo)
    }

    private fun initWordsList(id: Int): List<String> {
        val inputStream = inputMethod.resources.openRawResource(id)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val text = reader.use { it.readText() }
        return text.lines()
    }

    fun updateInput(key: Char) {
        input += key

        when(inputMethod.keyboardType) {
            2 -> updateArrayCandidates()
            4 -> updateBopomofoCandidates()
            else -> {}
        }
    }

    private fun updateArrayCandidates() {
//        val regex = Regex("^(${input})\t(.+)")
        candidateList.clear()
        for (line in arrayWords) {
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

    private fun updateBopomofoCandidates() {
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

        when(inputMethod.keyboardType) {
            2 -> updateArrayCandidates()
            4 -> updateBopomofoCandidates()
            else -> {}
        }
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