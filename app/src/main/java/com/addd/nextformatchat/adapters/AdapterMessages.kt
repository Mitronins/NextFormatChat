package com.addd.nextformatchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.addd.nextformatchat.R
import com.addd.nextformatchat.model.MyMessage
import com.addd.nextformatchat.model.User
import java.util.*

/**
 * Created by addd on 14.02.2018.
 */
class AdapterMessages(notesList: ArrayList<MyMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mMessagesList: ArrayList<MyMessage> = notesList
    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false

    fun isEmpty() = mMessagesList.isEmpty()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as ViewHolder
                var message = mMessagesList[position]
                viewHolder.text.text = message.text
                viewHolder.name.text = message.user.username
            }

            LOADING -> {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        return if (viewType == ITEM) {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_message, viewGroup, false)
            ViewHolder(v)
        } else {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.progressbar_item, viewGroup, false)
            LoadingVH(v)
        }
    }


    override fun getItemCount(): Int {
        return if (mMessagesList == null) 0 else mMessagesList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mMessagesList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: MyMessage) {
        mMessagesList.add(mc)
        notifyItemInserted(mMessagesList.size - 1)
        notifyDataSetChanged()
    }

    fun addNewMessage(mc: MyMessage) {
        mMessagesList.add(mc)
        notifyItemInserted(mMessagesList.size - 1)
    }

    fun addAll(mcList: List<MyMessage>) {
        for (deal in mcList) {
            add(deal)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(MyMessage(0,"","", User("",0,"",""),0))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = mMessagesList.size - 1
        val item = getItem(position)

        if (item != null) {
            mMessagesList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): MyMessage? {
        return mMessagesList[position]
    }


    class ViewHolder : RecyclerView.ViewHolder{

        var text: TextView
        var name: TextView

        constructor(itemView: View) : super(itemView) {
            text = itemView.findViewById(R.id.textViewText)
            name = itemView.findViewById(R.id.textViewName)
        }

    }

    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val proressBar: ProgressBar

        init {
            proressBar = itemView.findViewById(R.id.progressBar2)
        }
    }

    interface CustomAdapterCallback {
        fun onItemClick(pos: Int)
    }
}