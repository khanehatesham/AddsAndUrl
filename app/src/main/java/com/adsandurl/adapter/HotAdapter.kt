package com.adsandurl.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsandurl.R
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.listener.OnBottomReachedListener
import com.adsandurl.listener.RecyclerViewClickListener
import com.bumptech.glide.Glide
import com.wang.avi.AVLoadingIndicatorView
import java.util.*

class HotAdapter(
    private var listOfHotData: List<HotChildren>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val TYPE_FOOTER = 1
    private val TYPE_ITEM = 2
    private var onBottomReachedListener: OnBottomReachedListener? = null
    private var recyclerViewClickListener: RecyclerViewClickListener? = null
    private var mFilteredList: List<HotChildren>? = null

    private var isBottomError: Boolean = false
    private var isBottomLoading: Boolean = false


    private inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail)
        var tvName: TextView = view.findViewById(R.id.tv_name)
        var tvTitle: TextView = view.findViewById(R.id.tv_title)

    }

    private inner class FooterViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view) {
        internal var avlBottomLoadingDots: AVLoadingIndicatorView
        internal var ivBottomReload: ImageView

        init {
            avlBottomLoadingDots = view.findViewById(R.id.avl_loading)
            ivBottomReload = view.findViewById(R.id.iv_bottom_reload)

            ivBottomReload.setOnClickListener {
                recyclerViewClickListener!!.onClick(
                    R.id.iv_bottom_reload,
                    adapterPosition
                )
            }
        }
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = null
        if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_layout, parent, false)
            return ItemViewHolder(itemView)
        } else if (viewType == TYPE_FOOTER) {
            val footerView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            return FooterViewHolder(footerView)
        }
        return ItemViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.run {
                tvName.setText(getItem(position).getChildrenObject()!!.getName()!!)
                tvTitle.setText(getItem(position).getChildrenObject()!!.getTitle()!!)
            }
            Glide.with(context).load(getItem(position).getChildrenObject()!!.getThumbnail())
                .into(holder.ivThumbnail);
            if (position == listOfHotData.size - 1 && !isBottomError && !isBottomLoading) {
                onBottomReachedListener!!.onBottomReached(position)
            }
        } else if (holder is FooterViewHolder) {

            if (isBottomError) {
                holder.avlBottomLoadingDots.visibility = View.GONE
                holder.ivBottomReload.visibility = View.VISIBLE
            } else if (isBottomLoading) {
                holder.avlBottomLoadingDots.visibility = View.VISIBLE
                holder.ivBottomReload.visibility = View.GONE
            } else {
                holder.avlBottomLoadingDots.visibility = View.GONE
                holder.ivBottomReload.visibility = View.GONE
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<HotChildren>()

                if (mFilteredList == null)
                    mFilteredList = listOfHotData

                if (constraint != null) {
                    if (mFilteredList != null && mFilteredList!!.size > 0) {
                        for (hotChildren in mFilteredList!!) {
                            if (hotChildren.getChildrenObject()!!.getTitle()!!.toLowerCase().contains(
                                    constraint.toString().toLowerCase()
                                )
                                || hotChildren.getChildrenObject()!!.getName()!!.toLowerCase().contains(
                                    constraint.toString().toLowerCase()
                                )
                            )
                                results.add(hotChildren)
                        }
                    }
                    oReturn.count = results.size
                    oReturn.values = results
                } else {
                    oReturn.count = listOfHotData.size
                    oReturn.values = listOfHotData
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listOfHotData = results.values as List<HotChildren>
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        return listOfHotData.size + 1
    }

    fun getItem(position: Int): HotChildren {
        return listOfHotData[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listOfHotData.size) {
            TYPE_FOOTER
        } else TYPE_ITEM

    }


    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

    fun setList(list: List<HotChildren>) {
        this.listOfHotData = list
        notifyDataSetChanged()
    }

    fun footerVisibility(isBottomError: Boolean, isBottomLoading: Boolean) {
        this.isBottomError = isBottomError
        this.isBottomLoading = isBottomLoading
    }


}