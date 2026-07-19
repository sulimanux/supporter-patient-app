package com.app.sanad.util

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Html
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

enum class Status{
   Init,
    Start,
    Error,
    Done,

}

class TextToSpeechUtil ( val  textToSpeech: TextToSpeech) {


    private val _status = MutableLiveData<Status>()
    var status:LiveData<Status> = _status


    fun resetStatus(){
        _status.value = Status.Init
    }

    fun debug(){
        log("start debugging")
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                log("Speech started")
                _status.value = Status.Start

            }

            override fun onDone(utteranceId: String?) {
                log("Speech done")
                _status.value= Status.Done
            }

            override fun onError(utteranceId: String?) {
                log("Speech error for utterance: $utteranceId")
                _status.value = Status.Error
            }

        }
        )

    }

    private fun htmlToText(html: String): String {
        log("htmlToText")
        val spanned: Spanned =
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        return spanned.toString()
    }


    fun speakText(html: String){
        if (html.isEmpty()) return
        if (textToSpeech.isSpeaking) textToSpeech.stop()
        try {
            val plainText = htmlToText(html)
            textToSpeech.speak(plainText, TextToSpeech.QUEUE_FLUSH, null, "chunk_")

        } catch (e: Exception) {
            log("speakText Exception ${e.message}")
        }

    }


    fun release(){
        textToSpeech.stop()
        textToSpeech.shutdown()
        resetStatus()
    }
}