package com.nelyanlive.chat

import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.BR
import kotlinx.android.synthetic.main.chat_text_left.view.*
import kotlinx.android.synthetic.main.chat_text_right.view.*

class RecyclerAdapterChat<T : AbstractModel>(
    @LayoutRes val layoutId: Int,
    @LayoutRes val layoutId2: Int,
    @LayoutRes val layoutId3: Int,
    @LayoutRes val layoutId4: Int
) : RecyclerView.Adapter<RecyclerAdapterChat.VH<T>>() {

    private val items = mutableListOf<T>()
    private var inflater: LayoutInflater? = null
    private var onItemClick: OnItemClick? = null

    private var orientationType: Int = 0

    var IMAGE_RIGHT = 0
    var IMAGE_LEFT = 1
    var TEXT_RIGHT = 2
    var TEXT_LEFT = 3

    var setAnimOrNot: Boolean = true
    private val animatedPosition: HashSet<Int> = HashSet()

    fun withoutClearAddItems(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addAtPosition(items: List<T>, position: Int) {
        this.items.add(items[items.size - 1])
        notifyItemInserted(position)
    }

    fun removeAtPosition(position: Int) {
        this.items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    fun setOnItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }


    private fun setAnimation(holder: RecyclerView.ViewHolder, position: Int) {

        if (orientationType == 1) {
            if (this.animatedPosition.contains(Integer.valueOf(position))) {
                holder.itemView.clearAnimation()
                return
            }
        }
        if (!setAnimOrNot)
            return

        this.animatedPosition.add(Integer.valueOf(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)

        when (viewType) {
            TEXT_RIGHT -> {
                Log.e("dfdasfaf", "=========111111=======")
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutId2,
                    parent,
                    false
                )
                return VH(binding)
            }
            TEXT_LEFT -> {

                Log.e("dfdasfaf", "=========222222=======")

                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutId,
                    parent,
                    false
                )
                return VH(binding)
            }
            IMAGE_RIGHT -> {

                Log.e("dfdasfaf", "=========222222=======")

                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutId3,
                    parent,
                    false
                )
                return VH(binding)
            }
            IMAGE_LEFT -> {

                Log.e("dfdasfaf", "=========222222=======")

                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutId4,
                    parent,
                    false
                )
                return VH(binding)
            }
            else -> {
                Log.e("dfdasfaf", "=========333333=======")

                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    layoutInflater,
                    layoutId,
                    parent,
                    false
                )
                return VH(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val data = (items[position] as ChatData)
        return if (data.senderId == data.myID) {
            if (data.messageType == "0") TEXT_RIGHT else IMAGE_RIGHT
        } else {
            if (data.messageType == "0") TEXT_LEFT else IMAGE_LEFT
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        val model = items[position]
        val model2 = items[position] as ChatData


        model.adapterPosition = position
        onItemClick?.let { model.onItemClickChat = it }
        holder.bind(model)

        setAnimation(holder, position)
    }

    class VH<T : AbstractModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: T) {
            binding.setVariable(BR.model, model)
            binding.executePendingBindings()
        }
    }

    interface OnItemClick {
        fun onClick(view: View, position: Int, type: String)
    }
}