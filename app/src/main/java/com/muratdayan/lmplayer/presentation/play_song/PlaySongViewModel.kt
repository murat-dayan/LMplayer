package com.muratdayan.lmplayer.presentation.play_song

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable.start
import javax.inject.Inject

@HiltViewModel
class PlaySongViewModel @Inject constructor(
): ViewModel() {

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> get() = _duration

    var mediaPlayer: MediaPlayer? = null
    private var currentSongPath: String? = null
    private var currentSongPosition: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    _currentPosition.value = it.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }
    }


    fun playSong(path: String) {
        if (path != currentSongPath) {
            stopSong() // Mevcut çalan şarkıyı durdur ve serbest bırak
            currentSongPath = path
            currentSongPosition = 0
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                seekTo(currentPosition)
                start()
                _duration.value = duration
                handler.post(updateRunnable)
                _isPlaying.value = true
                setOnCompletionListener {
                    _isPlaying.value = false
                }
            }
        } else {
            mediaPlayer?.start()
            handler.post(updateRunnable)
            _isPlaying.value = true
        }
    }

    fun pauseSong() {
        mediaPlayer?.pause()
        currentSongPosition = mediaPlayer?.currentPosition ?: 0
        _isPlaying.value = false
        handler.removeCallbacks(updateRunnable)
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
        currentSongPosition = 0
        handler.removeCallbacks(updateRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        stopSong()
    }
}