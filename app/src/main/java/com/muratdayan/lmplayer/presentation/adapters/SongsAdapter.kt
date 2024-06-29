package com.muratdayan.lmplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.muratdayan.lmplayer.R
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import com.muratdayan.lmplayer.databinding.SongsRowBinding

class SongsAdapter(
    private val songs: List<SongModel>,
    private val onItemClicked: (SongModel) -> Unit
): RecyclerView.Adapter<SongsAdapter.SongsRowHolder>()  {

    inner class SongsRowHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = SongsRowBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsRowHolder {
        val design = LayoutInflater.from(parent.context).inflate(R.layout.songs_row,parent,false)
        return SongsRowHolder(design)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongsRowHolder, position: Int) {
        val song = songs[position]

        holder.binding.apply {
            txtViewSongsRowTitle.text = song.title
            cvSongsRow.setOnClickListener {
                onItemClicked(song)
            }
        }
    }
}