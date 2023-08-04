package com.example.motusgame.ui.gameScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.motusgame.databinding.ItemCharacterBinding
import com.example.motusgame.ui.CharacterInfo


class MotusGameAdapter(
    private val listOfCharacter : MutableList<CharacterInfo>
) :
    RecyclerView.Adapter<MotusGameAdapter.WordCharacterViewHolder>(){




    @SuppressLint("NotifyDataSetChanged")
    fun addToList(list : List<CharacterInfo>){
        listOfCharacter.addAll(list)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordCharacterViewHolder =
        WordCharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int {
      return  listOfCharacter.size
    }

    override fun onBindViewHolder(holder: WordCharacterViewHolder, position: Int) {
        holder.bind(listOfCharacter[position])
    }

    inner class WordCharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterInfo) {
            with(binding) {
                tvChar.setBackgroundColor(ContextCompat.getColor(tvChar.context, character.state.stateColor))
                tvChar.text = character.characterValue.toString()
            }
        }
    }


}


