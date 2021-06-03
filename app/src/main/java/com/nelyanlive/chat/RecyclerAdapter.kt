package com.nelyanlive.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.BR

class RecyclerAdapter<T : AbstractModel>(@LayoutRes val layoutId: Int) :
    RecyclerView.Adapter<RecyclerAdapter.VH<T>>() {

    private val items = mutableListOf<T>()
    private var inflater: LayoutInflater? = null
    private var onItemClick: OnItemClick? = null

    private var orientationType: Int = 0
    var setAnimOrNot: Boolean = true
    private val animatedPosition: HashSet<Int> = HashSet()

    fun withoutClearAddItems(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addAtPosition(items: List<T>,position: Int) {
        this.items.add(items[items.size-1])
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

    fun setItem(position: Int, item: T) {
        this.items[position] = item
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

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            layoutId,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /*override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }*/

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH<T>, position: Int) {
        val model = items[position]
        model.adapterPosition = position
        onItemClick?.let { model.onItemClick = it }
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