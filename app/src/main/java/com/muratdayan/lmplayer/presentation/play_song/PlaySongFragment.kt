package com.muratdayan.lmplayer.presentation.play_song

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.muratdayan.lmplayer.R
import com.muratdayan.lmplayer.databinding.FragmentPlaySongBinding
import com.muratdayan.lmplayer.presentation.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaySongFragment : Fragment() {

    private var _binding: FragmentPlaySongBinding? = null
    private val binding get() = _binding!!
    private val args: PlaySongFragmentArgs by navArgs()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val song = args.song

        binding.txtViewSongTitle.text = song.title

        val songPosition = args.position

        sharedViewModel.getAllSongsAndInitialize(songPosition)

        sharedViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_stop)
            } else {
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_play)
            }
        }

        sharedViewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            binding.seekBar.progress = position
            binding.txtViewRemindDuration.text = formatTime(position)
        }

        sharedViewModel.duration.observe(viewLifecycleOwner) { duration ->
            binding.seekBar.max = duration
            binding.txtViewRemindDuration.text = formatTime(duration)
        }

        sharedViewModel.currentSong.observe(viewLifecycleOwner) { currentSong ->
            binding.txtViewSongTitle.text = currentSong.title
        }


        binding.imgViewPlayStopIcon.setOnClickListener {
            if (sharedViewModel.isPlaying.value == true) {
                sharedViewModel.pauseSong()
            } else {
                sharedViewModel.playOrPauseSong()
            }
        }

        binding.imgViewNextSongIcon.setOnClickListener {
            sharedViewModel.playNextSong()
        }

        binding.imgViewPreviousSongIcon.setOnClickListener {
            sharedViewModel.playPreviousSong()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    sharedViewModel.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // not needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // not needed
            }

        })
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}