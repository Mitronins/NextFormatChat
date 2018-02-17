package com.addd.nextformatchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.addd.nextformatchat.*
import com.addd.nextformatchat.model.Chat
import java.util.*


/**
 * Created by addd on 25.12.2017.
 */
class AdapterChats(notesList: ArrayList<Chat>, private val listener: CustomAdapterCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mChatList: ArrayList<Chat> = notesList
    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    private var isDateAdded = false

    fun isEmpty() = mChatList.isEmpty()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                isDateAdded = false
                val viewHolder = holder as ViewHolder
                var chat = mChatList[position]
                viewHolder.nameChat.text = chat.name
                viewHolder.lastMessage.text = "Последнее сообщение"
                val time = formatTime(chat.last_date.toString())
                val calendarMessage: Calendar = GregorianCalendar(
                        time.substring(0, 4).toInt(), time.substring(5, 7).toInt() - 1,
                        time.substring(8, 10).toInt(), time.substring(11, 13).toInt(),
                        time.substring(14, time.length).toInt())
                val calendarCurrent: Calendar = GregorianCalendar()

                calendarMessage.add(Calendar.DAY_OF_MONTH, -1)
                calendarMessage.add(Calendar.DAY_OF_MONTH, 1)
                if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                    viewHolder.time.text = time.substring(11, time.length)
                    isDateAdded = true
                } else {
                    calendarCurrent.add(Calendar.DAY_OF_MONTH, -1)
                    if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                        viewHolder.time.text = "Вчера"
                        isDateAdded = true
                    }
                }
                if (!isDateAdded) {
                    viewHolder.time.text = time.substring(8, 10) + "." + time.substring(5, 7) + "." + time.substring(2, 4)
                }


            }

            LOADING -> {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        return if (viewType == ITEM) {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_chat, viewGroup, false)
            ViewHolder(v, listener)
        } else {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.progressbar_item, viewGroup, false)
            LoadingVH(v)
        }
    }


    override fun getItemCount(): Int {
        return if (mChatList == null) 0 else mChatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mChatList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: Chat) {
        mChatList.add(mc)
        notifyItemInserted(mChatList.size - 1)
        notifyDataSetChanged()
    }

    fun addAll(mcList: List<Chat>) {
        for (deal in mcList) {
            add(deal)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Chat())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = mChatList.size - 1
        val item = getItem(position)

        if (item != null) {
            mChatList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Chat? {
        return mChatList[position]
    }


    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        private val listener: AdapterChats.CustomAdapterCallback
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

        var nameChat: TextView
        var lastMessage: TextView
        var time: TextView

        constructor(itemView: View, listener: AdapterChats.CustomAdapterCallback) : super(itemView) {
            nameChat = itemView.findViewById(R.id.textViewChatName)
            lastMessage = itemView.findViewById(R.id.textViewLastMessage)
            time = itemView.findViewById(R.id.textViewTime)
            this.listener = listener
            this.itemView.setOnClickListener(this)
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