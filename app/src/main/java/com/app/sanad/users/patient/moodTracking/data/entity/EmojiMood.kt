package com.app.sanad.users.patient.moodTracking.data.entity
/**
 * Purpose: Defines a mood type with its UI text, emoji icon, and theme colors.
 * Used to present mood insights and personalized suggestions to the user.
 */
class EmojiMood
    (
    val name: String,
    val title:String,
    val subTitle:String,
    val titleSuggestion:String,
    val suggestion:List<SuggestionToDo>,
    val emoji: Int,
    val backgroundColor: String,
    val buttonColor: String,
    val tipBackgroundColor :String

)
