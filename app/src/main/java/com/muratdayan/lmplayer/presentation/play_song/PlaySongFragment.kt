package com.muratdayan.lmplayer.presentation.play_song

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaySongFragment : Fragment() {

    private var _binding: FragmentPlaySongBinding? = null
    private val binding get() = _binding!!
    private val args: PlaySongFragmentArgs by navArgs()
    private val playSongViewModel: PlaySongViewModel by viewModels()

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

        val songPosition = args.position

        playSongViewModel.getAllSongsAndInitialize(songPosition)

        playSongViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_stop)
            } else {
                binding.imgViewPlayStopIcon.setImageResource(R.drawable.ic_play)
            }
        }

        playSongViewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            binding.seekBar.progress = position
            binding.txtViewRemindDuration.text = formatTime(position)
        }

        playSongViewModel.duration.observe(viewLifecycleOwner) { duration ->
            binding.seekBar.max = duration
            binding.txtViewRemindDuration.text = formatTime(duration)
        }

        binding.imgViewPlayStopIcon.setOnClickListener {
            if (playSongViewModel.isPlaying.value == true) {
                playSongViewModel.pauseSong()
            } else {
                playSongViewModel.playOrPauseSong()
            }
        }

        binding.imgViewNextSongIcon.setOnClickListener {
            playSongViewModel.playNextSong()
        }

        binding.imgViewPreviousSongIcon.setOnClickListener {
            playSongViewModel.playPreviousSong()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    playSongViewModel.mediaPlayer?.seekTo(progress)
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