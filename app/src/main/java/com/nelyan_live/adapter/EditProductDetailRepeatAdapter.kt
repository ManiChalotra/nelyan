package com.nelyan_live.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.ProductDetailDataModel
import com.nelyan_live.modals.myAd.TraderProductMyAds
import java.util.*

class EditProductDetailRepeatAdapter(internal var context: Context, internal var list: ArrayList<TraderProductMyAds>,
                                     internal var productRepeatListener: ProductRepeatListener) : RecyclerView.Adapter<EditProductDetailRepeatAdapter.EditProductDetailRepeatHolder>() {
   // var returnItemView = 1
    /*var productRepeatListener: TraderActivity
    var selectedImage = ""
    var file: File? = null
    var Title = HashMap<String, String>()
    var price = HashMap<String, String>()
    var Description = HashMap<String, String>()
    var mAlbumFiles = ArrayList<AlbumFile>()*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProductDetailRepeatHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_details_repeat, parent, false)
        return EditProductDetailRepeatHolder(view, productRepeatListener)
    }

    override fun onBindViewHolder(holder: EditProductDetailRepeatHolder, position: Int) {
        holder.bind(list, position)

    }



/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProductDetailRepeatAdapter {


    }
*/

   /* override fun onBindViewHolder(holder: EditProductDetailRepeatAdapter, position: Int) {
    }*/

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EditProductDetailRepeatHolder(var view: View, var productRepeatListener: ProductRepeatListener) : RecyclerView.ViewHolder(view) {
        var tvAdd: TextView
        var edtDesc: EditText
        var edtProductTitle: EditText
        var edtProductPrice: EditText
        var ivEvent: ImageView
        var ivCam: ImageView

        init {
            tvAdd = itemView.findViewById(R.id.tv_myAdd)
            edtDesc = itemView.findViewById(R.id.edtDesc)
            edtProductPrice = itemView.findViewById(R.id.edtProductPrice)
            edtProductTitle = itemView.findViewById(R.id.edtProductTitle)
            ivEvent = itemView.findViewById(R.id.ivProductImage)
            ivCam = itemView.findViewById(R.id.ivCam)
        }

        fun bind(list: ArrayList<TraderProductMyAds>, position: Int) {

            if (position == list.size - 1) {
                tvAdd.visibility = View.VISIBLE
            } else {
                tvAdd.visibility = View.GONE
            }


            Glide.with(context).asBitmap().load(list.get(position).image).into(ivEvent)
            edtProductTitle.setText(list.get(position).title)
            edtDesc.setText(list.get(position).description)
            edtProductPrice.setText("$"+ list.get(position).price.toString())

            ivCam.setOnClickListener{
                productRepeatListener!!.addCameraGelleryImage(list, position)
            }


            tvAdd.setOnClickListener {
                edtDesc.clearFocus()
                edtProductPrice.clearFocus()
                edtProductTitle.clearFocus()
                productRepeatListener!!.onITEEMClick(list, position)
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


/*            if (pos == returnItemView - 1) {
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
*/
      /*      try {
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
      */
            /*ivEvent.setOnClickListener {

                //  ProductRepeatListener.productImageClick(pos);

                // ProductRepeatListener.imageClick(pos, returnItemView)

                //  ProductRepeatListener.imageClick( pos,returnItemView);
            }*/
        /*    tvAdd.setOnClickListener {
                Title[pos.toString()] = edtProductTitle.text.toString()
                price[pos.toString()] = edtProductPrice.text.toString()
                Description[pos.toString()] = edtDesc.text.toString()
                returnItemView = returnItemView + 1
                notifyDataSetChanged()
            }*/
        }

    }

/*
    interface ProductRepeatListener {
        fun onITEEMClick(pos: Int, returnItemView: Int)
    }
*/
    interface ProductRepeatListener {
        fun onITEEMClick(list: ArrayList<TraderProductMyAds>, pos: Int)
        fun addCameraGelleryImage(list: ArrayList<TraderProductMyAds>, pos: Int)

}

    init {

        /*this.returnItemView = returnItemView
        this.productRepeatListener = ProductRepeatListener*/
    }



}