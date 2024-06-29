package com.muratdayan.lmplayer.presentation.play_song

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muratdayan.lmplayer.R
import com.muratdayan.lmplayer.databinding.FragmentPlaySongBinding


class PlaySongFragment : Fragment() {

    private var _binding: FragmentPlaySongBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentPlaySongBinding.inflate(inflater, container, false)




        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}