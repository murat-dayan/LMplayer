package com.muratdayan.lmplayer.presentation.songs

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratdayan.lmplayer.core.common.Resource
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import com.muratdayan.lmplayer.domain.use_cases.GetAllSongsUseCase
import com.muratdayan.lmplayer.domain.use_cases.InsertSongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val application: Application,
    private val getAllSongsUseCase: GetAllSongsUseCase,
    private val insertSongUseCase: InsertSongUseCase
) : ViewModel() {

    init {
        getAllSongs()
    }


    private val _permissionRequest = MutableLiveData(false)
    val permissionRequest: LiveData<Boolean> = _permissionRequest

    private var _songsState = MutableStateFlow(SongsState())
    val songsState: StateFlow<SongsState>
        get() = _songsState

    private var songsList: List<SongModel> = emptyList()


    fun checkPermissionAndFetchMp3Files() {
        _permissionRequest.value = true
    }

    fun fetchAndSaveMp3Files() {
        viewModelScope.launch {
            if (hasStoragePermission()) {
                val songsInDevice = fetchSongsFilesFromDevice()
                songsInDevice.forEach { song ->
                    val result = insertSongUseCase(song)
                    println(result)
                }

                getAllSongs()
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchSongsFilesFromDevice(): List<SongModel> {
        val songsList = mutableListOf<SongModel>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        application.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val data = cursor.getString(dataColumn)
                val duration = cursor.getLong(durationColumn)
                val song = SongModel(id,title, artist, data, duration)
                songsList.add(song)
            }


        }
        return songsList
    }

    private fun getAllSongs(){
        getAllSongsUseCase().onEach { result->
            when(result){
                is Resource.Success->{
                    songsList = result.data ?: emptyList()
                    _songsState.value = SongsState(songs = result.data)
                }
                is Resource.Error-> {
                    _songsState.value = SongsState(error = result.msg)
                }
                is Resource.Loading->{
                    //_songsState.value = SongsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}

