package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundManager {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playClick(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            playTone(800.0, 30, 0.4f)
        }
    }

    fun playCorrect(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            // Ascending major chime (E5 -> G#5 -> B5)
            playTone(659.25, 70, 0.5f)
            playTone(830.61, 70, 0.6f)
            playTone(987.77, 120, 0.7f)
        }
    }

    fun playIncorrect(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            // Low dissonant tone (F#3 -> D3)
            playTone(185.00, 100, 0.6f)
            playTone(146.83, 160, 0.7f)
        }
    }

    fun playProteinComplete(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            // Triumphant arpeggio (C5 -> E5 -> G5 -> C6)
            playTone(523.25, 80, 0.5f)
            playTone(659.25, 80, 0.6f)
            playTone(783.99, 80, 0.7f)
            playTone(1046.50, 250, 0.8f)
        }
    }

    fun playLevelUp(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            // Level up fanfare (G4 -> C5 -> E5 -> G5)
            playTone(392.00, 70, 0.5f)
            playTone(523.25, 70, 0.6f)
            playTone(659.25, 90, 0.7f)
            playTone(783.99, 280, 0.85f)
        }
    }

    fun playAchievement(enabled: Boolean) {
        if (!enabled) return
        scope.launch {
            playTone(587.33, 90, 0.6f)
            playTone(739.99, 90, 0.65f)
            playTone(880.00, 90, 0.75f)
            playTone(1174.66, 260, 0.85f)
        }
    }

    private fun playTone(freqHz: Double, durationMs: Int, volume: Float) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
            if (numSamples <= 0) return

            val buffer = ShortArray(numSamples)
            val fadeSamples = (numSamples * 0.1).toInt().coerceAtLeast(1)

            for (i in 0 until numSamples) {
                val angle = 2.0 * Math.PI * i * freqHz / sampleRate
                var sample = sin(angle)

                // Envelope attack/decay to prevent audio pop/click
                val env = when {
                    i < fadeSamples -> i.toFloat() / fadeSamples
                    i > numSamples - fadeSamples -> (numSamples - i).toFloat() / fadeSamples
                    else -> 1.0f
                }

                buffer[i] = (sample * Short.MAX_VALUE * volume * env).toInt().toShort()
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            Thread.sleep(durationMs.toLong() + 10)
            track.stop()
            track.release()
        } catch (_: Exception) {
            // Gracefully ignore audio glitches if device audio is busy
        }
    }
}
