package com.muratdayan.lmplayer.presentation.play_song

import android.media.MediaPlayer
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

    private var mediaPlayer: MediaPlayer? = null
    private var currentSongPath: String? = null
    private var currentPosition: Int = 0

    fun playSong(path: String) {
        if (path != currentSongPath) {
            stopSong() // Mevcut çalan şarkıyı durdur ve serbest bırak
            currentSongPath = path
            currentPosition = 0
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                seekTo(currentPosition)
                start()
                _isPlaying.value = true
            }
        } else {
            mediaPlayer?.start()
            _isPlaying.value = true
        }
    }

    fun pauseSong() {
        mediaPlayer?.pause()
        currentPosition = mediaPlayer?.currentPosition ?: 0
        _isPlaying.value = false
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        currentPosition = 0
    }

    override fun onCleared() {
        super.onCleared()
        stopSong()
    }
}