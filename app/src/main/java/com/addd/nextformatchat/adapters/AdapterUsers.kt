package com.addd.nextformatchat.adapters

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.addd.nextformatchat.MyApp
import com.addd.nextformatchat.R
import com.addd.nextformatchat.model.User

/**
 * Created by addd on 17.02.2018.
 */

class AdapterUsers(notesList: ArrayList<User>, private val listener: AdapterUsers.CustomAdapterCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mUserList: ArrayList<User> = notesList
    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false

    fun isEmpty() = mUserList.isEmpty()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as ViewHolder
                var user = mUserList[position]
                viewHolder.nickname.text = user.username
                viewHolder.firstName.text = user.firstName
                viewHolder.lastName.text = user.lastName
                if (position == mUserList.size - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        viewHolder.view.setBackgroundColor(MyApp.instance.getColor(R.color.background))
                    }
                }
            }

            LOADING -> {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        return if (viewType == ITEM) {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_user, viewGroup, false)
            ViewHolder(v, listener)
        } else {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.progressbar_item, viewGroup, false)
            LoadingVH(v)
        }
    }


    override fun getItemCount(): Int {
        return if (mUserList == null) 0 else mUserList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mUserList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: User) {

        mUserList.add(mc)
        notifyItemInserted(mUserList.size - 1)
        notifyDataSetChanged()
    }

    fun addAll(mcList: List<User>) {
        for (deal in mcList) {
            add(deal)
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(User("",0, "", ""))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = mUserList.size - 1
        val item = getItem(position)

        if (item != null) {
            mUserList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): User? {
        return mUserList[position]
    }


    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        private val listener: AdapterUsers.CustomAdapterCallback
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

        var firstName: TextView
        var lastName: TextView
        var nickname: TextView
        var view: View

        constructor(itemView: View, listener: AdapterUsers.CustomAdapterCallback) : super(itemView) {
            firstName = itemView.findViewById(R.id.textViewLastMessage)
            lastName = itemView.findViewById(R.id.textViewLastName)
            nickname = itemView.findViewById(R.id.textViewNickname)
            view = itemView.findViewById(R.id.view)
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