package com.nelyanlive.adapter


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.TraderProductMyAds
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_product_details_repeat.view.*
import java.util.*

class EditProductDetailRepeatAdapter(internal var context: Context, internal var list: ArrayList<TraderProductMyAds>,
                                     internal var productRepeatListener: ProductRepeatListener) : RecyclerView.Adapter<EditProductDetailRepeatAdapter.EditProductDetailRepeatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProductDetailRepeatHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_details_repeat, parent, false)
        return EditProductDetailRepeatHolder(view, productRepeatListener)
    }

    override fun onBindViewHolder(holder: EditProductDetailRepeatHolder, position: Int) {
        holder.bind(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EditProductDetailRepeatHolder(var view: View, var productRepeatListener: ProductRepeatListener) : RecyclerView.ViewHolder(view) {
        var tvAdd: TextView = itemView.findViewById(R.id.tv_myAdd)
        var edtDesc: EditText = itemView.findViewById(R.id.edtDesc)
        var edtProductTitle: EditText = itemView.findViewById(R.id.edtProductTitle)
        var edtProductPrice: EditText = itemView.findViewById(R.id.edtProductPrice)
        private var ivEvent: ImageView = itemView.findViewById(R.id.ivProductImage)
        var ivCam: ImageView = itemView.findViewById(R.id.ivCam)
        val txtdlttrader = itemView.ivdlttrader

        fun bind(list: ArrayList<TraderProductMyAds>, position: Int) {

            if (position == list.size - 1) {
                tvAdd.visibility = View.VISIBLE
            } else {
                tvAdd.visibility = View.GONE
            }

            Glide.with(context).asBitmap().load(image_base_URl+list[position].image).into(ivEvent)
            edtProductTitle.setText(list[position].title)
            edtDesc.setText(list[position].description)
            edtProductPrice.setText(list[position].price)

            ivCam.setOnClickListener{
                productRepeatListener.addCameraGalleryImage(list, position)
            }

            tvAdd.setOnClickListener {
                edtDesc.clearFocus()
                edtProductPrice.clearFocus()
                edtProductTitle.clearFocus()
                productRepeatListener.ontraderItemClick(list, position)
            }

            // add text watcher  for age To
            edtProductPrice.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].price = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            edtProductTitle.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].title = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            edtDesc.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].description = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            txtdlttrader.setOnClickListener {
                list.removeAt(position)
                var ListSize = list.size
                productRepeatListener.onRemoveTraderProductItem(position, ListSize)

                notifyDataSetChanged()
                Log.d(
                    ProductDetailRepeatAdapter::class.java.name,
                    "ProductDetailsRepeatAdapter  " + position
                )
            }

        }

    }

    interface ProductRepeatListener {
        fun ontraderItemClick(list: ArrayList<TraderProductMyAds>, pos: Int)
        fun addCameraGalleryImage(list: ArrayList<TraderProductMyAds>, pos: Int)

        fun onRemoveTraderProductItem(position: Int, list: Int)

    }
}

