package com.muratdayan.lmplayer.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.lmplayer.core.common.Resource
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import com.muratdayan.lmplayer.domain.use_cases.GetAllSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase
) : ViewModel() {

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val _duration = MutableLiveData<Int>()
    val duration: LiveData<Int> get() = _duration

    private val _currentSong = MutableLiveData<SongModel>()
    val currentSong: LiveData<SongModel> get() = _currentSong

    var mediaPlayer: MediaPlayer? = null
    private var currentSongPath: String? = null
    private var currentSongPosition: Int = 0
    private var songsList: List<SongModel> = emptyList()
    private var currentSongIndex: Int = -1

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

    fun getAllSongsAndInitialize(initialSongIndex: Int) {
        viewModelScope.launch {
            getAllSongsUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        songsList = result.data ?: emptyList()
                        if (songsList.isNotEmpty()) {
                            currentSongIndex = initialSongIndex
                            currentSongPath = songsList[currentSongIndex].path
                            _currentSong.value = songsList[currentSongIndex]
                            initializeMediaPlayer()
                        }
                    }
                    is Resource.Error -> {
                        // Handle error if needed
                    }
                    is Resource.Loading -> {
                        // Handle loading state if needed
                    }
                }
            }
        }
    }

    private fun initializeMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(currentSongPath)
            prepare()
            seekTo(currentSongPosition)
            start()
            _duration.value = duration
            handler.post(updateRunnable)
            _isPlaying.value = true
            setOnCompletionListener {
                playNextSong()
            }
        }
    }

    fun playNextSong() {
        if (songsList.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songsList.size
            currentSongPath = songsList[currentSongIndex].path
            _currentSong.value = songsList[currentSongIndex]
            initializeMediaPlayer()
        }
    }

    fun playPreviousSong() {
        if (songsList.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex > 0) {
                currentSongIndex - 1
            } else {
                songsList.size - 1
            }
            currentSongPath = songsList[currentSongIndex].path
            _currentSong.value = songsList[currentSongIndex]
            initializeMediaPlayer()
        }
    }

    fun playOrPauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            pauseSong()
        } else {
            mediaPlayer?.start()
            _isPlaying.value = true
            handler.post(updateRunnable)
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