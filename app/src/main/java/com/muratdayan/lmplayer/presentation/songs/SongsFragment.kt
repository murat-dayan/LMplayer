package com.muratdayan.lmplayer.presentation.songs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.muratdayan.lmplayer.databinding.FragmentSongsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongsFragment : Fragment() {

    private var _binding: FragmentSongsBinding? = null
    private val binding get() = _binding!!
    private val songsViewModel : SongsViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted->
            if (isGranted){
                songsViewModel.checkPermissionAndFetchMp3Files()
            }else{
                // not granted
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songsViewModel.permissionRequest.observe(viewLifecycleOwner) { request ->
            if (!request) {
                requestStoragePermissions()
            }else{
                songsViewModel.fetchAndSaveMp3Files()
            }
        }

        lifecycleScope.launch {
            songsViewModel.songsState.collect{songState->
                when{
                    songState.isLoading ->{
                        Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                    }
                    songState.error?.isNotEmpty() == true ->{
                        Toast.makeText(requireContext(),songState.error,Toast.LENGTH_SHORT).show()
                    }
                    songState.songs?.isNotEmpty() == true ->{
                        println(songState.songs)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun requestStoragePermissions(){
        when{
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )==    PackageManager.PERMISSION_GRANTED ->{
                // permission already granted
            }
            else->{
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }


}