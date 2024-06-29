package com.muratdayan.lmplayer.presentation.play_song

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.muratdayan.lmplayer.R
import com.muratdayan.lmplayer.databinding.FragmentPlaySongBinding


class PlaySongFragment : Fragment() {

    private var _binding: FragmentPlaySongBinding? = null
    private val binding get() = _binding!!
    private val args: PlaySongFragmentArgs by navArgs()
    private val playSongViewModel : PlaySongViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var songPath = args.songPath

        playSongViewModel.playSong(songPath)

        playSongViewModel.isPlaying.observe(viewLifecycleOwner){isPlaying->
            if (isPlaying){
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_stop)
            }else{
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_play)
            }
        }

        binding.imgViewPlayStopIcon.setOnClickListener {
            if (playSongViewModel.isPlaying.value == true){
                playSongViewModel.pauseSong()
            }else{
                playSongViewModel.playSong(songPath)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}