package com.example.motusgame.ui

import com.example.motusgame.R



enum class CharacterState (val stateColor : Int){
    CORRECT(R.color.correct_character_color),
    INCORRECT(R.color.incorrect_character_color),
    MAL_PLACED(R.color.not_well_placed_character_color),
}