package com.muratdayan.lmplayer.presentation.songs

import androidx.lifecycle.ViewModel
import com.muratdayan.lmplayer.domain.use_cases.GetAllSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val getAllSongsUseCase: GetAllSongsUseCase
) : ViewModel(){


}