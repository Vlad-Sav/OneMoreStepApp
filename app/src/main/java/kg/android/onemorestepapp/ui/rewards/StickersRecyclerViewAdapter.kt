package kg.android.onemorestepapp.ui.rewards

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kg.android.onemorestepapp.R

class StickersRecyclerViewAdapter(private val stickers: List<String?>): RecyclerView.Adapter<StickersRecyclerViewAdapter.StickerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sticker_recycler_view_item, parent, false)
        return StickerViewHolder(v)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(stickers[position] ?: "")
    }

    override fun getItemCount(): Int {
        return stickers.count()
    }

    class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stickerIV: ImageView = itemView.findViewById(R.id.sticker_item_iv)
        fun bind(sticker: String) {
            if(sticker.isNotEmpty()){
                val imageAsBytes: ByteArray = Base64.decode(sticker, Base64.DEFAULT)
                stickerIV.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size))
            }
        }
    }
}