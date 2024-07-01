package com.crest247.tenkeyime

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class CandidatesAdapter(private val ime: InputMethod) :
    RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder>() {

    private var candidateList = mutableListOf<String>()
    private val words: List<String>

    private var input = ""

    init {
        val inputStream = ime.resources.openRawResource(R.raw.words)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val text = reader.use { it.readText() }
        words = text.lines()
    }

    class CandidatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.candidateTextView)

        fun setData(data: String) {
            textView.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatesViewHolder {
        return CandidatesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_candidate, parent, false)
        )
    }

    override fun getItemCount() = candidateList.size

    override fun onBindViewHolder(holder: CandidatesViewHolder, position: Int) {
        holder.setData(candidateList[position])
        holder.itemView.setOnClickListener {
            ime.commitText(candidateList[position].toString())
            resetInput()
        }
    }

    fun updateInput(key: Char) {
        input += key
        updateCandidates()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCandidates() {
        val regex = Regex("^(${input})\t(.+)")
        candidateList.clear()
        for (line in words) {
            val matched = regex.matchAt(line, 0)
            if (matched != null) {
                val groups = matched.groupValues
                candidateList.add(groups[2])
            }
        }
        notifyDataSetChanged()
    }

    fun deleteLast() {
        candidateList.clear()
        input = input.dropLast(1)
        updateCandidates()
    }

    fun resetInput() {
        candidateList.clear()
        input = ""
        notifyDataSetChanged()
    }

    fun inputIsEmpty(): Boolean {
        return input.isEmpty()
    }
}