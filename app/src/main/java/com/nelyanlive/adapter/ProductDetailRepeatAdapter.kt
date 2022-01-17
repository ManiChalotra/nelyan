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
import com.nelyanlive.adapter.ProductDetailRepeatAdapter.ProductDetailsRepeatViewHolder
import com.nelyanlive.modals.ProductDetailDataModel
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_product_details_repeat.view.*
import java.util.*

class ProductDetailRepeatAdapter(
    internal var context: Context, internal var list: ArrayList<ProductDetailDataModel>,
    internal var productRepeatListener: ProductRepeatListener
) : RecyclerView.Adapter<ProductDetailsRepeatViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailsRepeatViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_product_details_repeat, parent, false)
        return ProductDetailsRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDetailsRepeatViewHolder, position: Int) {
        holder.bind(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ProductDetailsRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAdd: TextView = itemView.findViewById(R.id.tv_myAdd)
        var edtDesc: EditText = itemView.findViewById(R.id.edtDesc)
        var edtProductTitle: EditText = itemView.findViewById(R.id.edtProductTitle)
        var edtProductPrice: EditText = itemView.findViewById(R.id.edtProductPrice)
        var ivEvent: ImageView = itemView.findViewById(R.id.ivProductImage)
        var ivCam: ImageView = itemView.findViewById(R.id.ivCam)

        val txtdlttrader = itemView.ivdlttrader

        //        val txtaddtraderproduct = itemView.tvAddtarderproduct
        val lltraderproduct = itemView.ll_traderproduct

        fun bind(list: ArrayList<ProductDetailDataModel>, position: Int) {

            if (position == list.size - 1) {
                tvAdd.visibility = View.VISIBLE
            } else {
                tvAdd.visibility = View.GONE
            }

            Glide.with(context).asBitmap().load(image_base_URl + list[position].image).into(ivEvent)
            edtProductTitle.setText(list[position].productTitle)
            edtDesc.setText(list[position].description)
            edtProductPrice.setText(list[position].productPrice)

            ivCam.setOnClickListener {
                productRepeatListener.addCameraGalleryImage(list, position)
            }

            tvAdd.setOnClickListener {
                edtDesc.clearFocus()
                edtProductPrice.clearFocus()
                edtProductTitle.clearFocus()
                productRepeatListener.ontraderItemClick(list, position)
            }

            /* txtdlttrader.setOnClickListener {
                 lltraderproduct.visibility = View.GONE
                 productRepeatListener.onRemoveEventItem(position)
 //                txtaddtraderproduct.visibility = View.VISIBLE

 //                if (position == 0) {
 //                    txtaddtraderproduct.visibility = View.VISIBLE
 //                } else {
 //
 //                }
             }*/

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
            edtProductTitle.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].productTitle = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            edtProductPrice.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].productPrice = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            edtDesc.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].description = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    interface ProductRepeatListener {
        fun ontraderItemClick(list: ArrayList<ProductDetailDataModel>, pos: Int)
        fun addCameraGalleryImage(list: ArrayList<ProductDetailDataModel>, pos: Int)

        fun onSingleTraderProduct(list: ArrayList<ProductDetailDataModel>, position: Int)
        fun onRemoveTraderProductItem(position: Int, list: Int)
    }


}