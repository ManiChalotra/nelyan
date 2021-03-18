package com.nelyan_live.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.adapter.DescriptionRepeatAdapter.DescriptionRepeatViewHolder
import com.nelyan_live.ui.TraderActivity
import com.yanzhenjie.album.AlbumFile
import java.io.File
import java.util.*

class DescriptionRepeatAdapter(var context: Context, var image: HashMap<String, Bitmap>, descriptionRepeatListener: TraderActivity, returnItemView: Int) : RecyclerView.Adapter<DescriptionRepeatViewHolder>() {
    var returnItemView = 1
    var descriptionRepeatListener: TraderActivity
    var selectedImage = ""
    var file: File? = null
    var Title = HashMap<String, String>()
    var price = HashMap<String, String>()
    var Description = HashMap<String, String>()
    var mAlbumFiles = ArrayList<AlbumFile>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionRepeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_description_repeat, parent, false)
        return DescriptionRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionRepeatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return returnItemView
    }

    inner class DescriptionRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAdd: TextView
        var edtDesc: EditText
        var edtProductTitle: EditText
        var edtProductPrice: EditText
        var ivEvent: ImageView
        var ivCam: ImageView
        fun bind(pos: Int) {
            if (pos == returnItemView - 1) {
                tvAdd.visibility = View.VISIBLE
            } else {
                tvAdd.visibility = View.GONE
            }
            try {
                ivEvent.setImageBitmap(image[pos.toString()])
                // ivCam.setVisibility(View.GONE);
                Log.e("kmdkmdkedcmk", "ss c--" + pos + "   " + image["0"])
            } catch (e: Exception) {
            }
            try {
                edtProductTitle.setText(Title[pos.toString()])
            } catch (e: Exception) {
            }
            try {
                edtDesc.setText(Description[pos.toString()])
            } catch (e: Exception) {
            }
            try {
                edtProductPrice.setText(price[pos.toString()])
            } catch (e: Exception) {
            }
            ivEvent.setOnClickListener {

                //  descriptionRepeatListener.productImageClick(pos);

                // descriptionRepeatListener.imageClick(pos, returnItemView)

                //  descriptionRepeatListener.imageClick( pos,returnItemView);
            }
            tvAdd.setOnClickListener {
                Title[pos.toString()] = edtProductTitle.text.toString()
                price[pos.toString()] = edtProductPrice.text.toString()
                Description[pos.toString()] = edtDesc.text.toString()
                returnItemView = returnItemView + 1
                notifyDataSetChanged()
            }
        }

        init {
            tvAdd = itemView.findViewById(R.id.tvAdd)
            edtDesc = itemView.findViewById(R.id.edtDesc)
            edtProductPrice = itemView.findViewById(R.id.edtProductPrice)
            edtProductTitle = itemView.findViewById(R.id.edtProductTitle)
            ivEvent = itemView.findViewById(R.id.ivProductImage)
            ivCam = itemView.findViewById(R.id.ivCam)
        }
    }

    interface DescriptionRepeatListener {
        fun productImageClick(pos: Int, returnItemView: Int)
    }

    init {
        this.returnItemView = returnItemView
        this.descriptionRepeatListener = descriptionRepeatListener
    }
}