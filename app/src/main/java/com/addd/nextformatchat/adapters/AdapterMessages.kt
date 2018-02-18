package com.addd.nextformatchat.adapters

import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.addd.nextformatchat.ID_USER
import com.addd.nextformatchat.MyApp
import com.addd.nextformatchat.R
import com.addd.nextformatchat.formatTime
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
    private val ME = 2
    private var isLoadingAdded = false
    val sp = PreferenceManager.getDefaultSharedPreferences(MyApp.instance)
    private var idUser = sp.getInt(ID_USER, 0)
    private var isMe = false

    fun isEmpty() = mMessagesList.isEmpty()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val viewHolder = holder as ViewHolder
                var message = mMessagesList[position]
                viewHolder.text.text = message.text
                viewHolder.name.visibility = View.VISIBLE
                viewHolder.name.text = message.user.username

                val time = formatTime(message.date)
                val calendarMessage: Calendar = GregorianCalendar(
                        time.substring(0, 4).toInt(), time.substring(5, 7).toInt() - 1,
                        time.substring(8, 10).toInt(), time.substring(11, 13).toInt(),
                        time.substring(14, time.length).toInt())
                val calendarCurrent: Calendar = GregorianCalendar()
                calendarMessage.add(Calendar.DAY_OF_MONTH, -1)
                calendarMessage.add(Calendar.DAY_OF_MONTH, 1)


                viewHolder.time.text = time.substring(11, time.length)
                if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                    viewHolder.date.visibility = View.VISIBLE
                    viewHolder.date.text = MyApp.instance.getString(R.string.today)
                } else {
                    calendarCurrent.add(Calendar.DAY_OF_MONTH, -1)
                    if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                        viewHolder.date.visibility = View.VISIBLE
                        viewHolder.date.text = MyApp.instance.getString(R.string.yesterday)
                    } else {
                        viewHolder.date.visibility = View.VISIBLE
                        viewHolder.date.text = time.substring(8, 10) + "." + time.substring(5, 7) + "." + time.substring(2, 4)
                    }
                }

                if (position != 0) {
                    val timeP = formatTime(mMessagesList[position - 1].date)
                    val calendarP = GregorianCalendar(
                            timeP.substring(0, 4).toInt(), timeP.substring(5, 7).toInt() - 1,
                            timeP.substring(8, 10).toInt(), timeP.substring(11, 13).toInt(),
                            timeP.substring(14, timeP.length).toInt())
                    calendarP.add(Calendar.DAY_OF_MONTH, -1)
                    calendarP.add(Calendar.DAY_OF_MONTH, 1)
                    if (mMessagesList[position - 1].user.username == mMessagesList[position].user.username) {
                        viewHolder.name.visibility = View.GONE
                    }
                    if (calendarP.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                        viewHolder.date.visibility = View.GONE
                    } else {
                        viewHolder.name.visibility = View.VISIBLE
                    }

                }
            }

            ME -> {
                val viewHolder = holder as MeVH
                var message = mMessagesList[position]
                viewHolder.text.text = message.text
                viewHolder.name.visibility = View.VISIBLE
                viewHolder.name.text = message.user.username

                val time = formatTime(message.date)
                val calendarMessage: Calendar = GregorianCalendar(
                        time.substring(0, 4).toInt(), time.substring(5, 7).toInt() - 1,
                        time.substring(8, 10).toInt(), time.substring(11, 13).toInt(),
                        time.substring(14, time.length).toInt())
                val calendarCurrent: Calendar = GregorianCalendar()
                calendarMessage.add(Calendar.DAY_OF_MONTH, -1)
                calendarMessage.add(Calendar.DAY_OF_MONTH, 1)


                viewHolder.time.text = time.substring(11, time.length)
                if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                    viewHolder.date.visibility = View.VISIBLE
                    viewHolder.date.text = MyApp.instance.getString(R.string.today)
                } else {
                    calendarCurrent.add(Calendar.DAY_OF_MONTH, -1)
                    if (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                        viewHolder.date.visibility = View.VISIBLE
                        viewHolder.date.text = MyApp.instance.getString(R.string.yesterday)
                    } else {
                        viewHolder.date.visibility = View.VISIBLE
                        viewHolder.date.text = time.substring(8, 10) + "." + time.substring(5, 7) + "." + time.substring(2, 4)
                    }
                }

                if (position != 0) {
                    val timeP = formatTime(mMessagesList[position - 1].date)
                    val calendarP = GregorianCalendar(
                            timeP.substring(0, 4).toInt(), timeP.substring(5, 7).toInt() - 1,
                            timeP.substring(8, 10).toInt(), timeP.substring(11, 13).toInt(),
                            timeP.substring(14, timeP.length).toInt())
                    calendarP.add(Calendar.DAY_OF_MONTH, -1)
                    calendarP.add(Calendar.DAY_OF_MONTH, 1)
                    if (mMessagesList[position - 1].user.username == mMessagesList[position].user.username) {
                        viewHolder.name.visibility = View.GONE
                    }
                    if (calendarP.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR)) {
                        viewHolder.date.visibility = View.GONE
                    } else {
                        viewHolder.name.visibility = View.VISIBLE
                    }

                }
            }

            LOADING -> {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        if (viewType == ITEM) {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_message, viewGroup, false)
            return ViewHolder(v)
        } else if (viewType == LOADING) {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.progressbar_item, viewGroup, false)
            return LoadingVH(v)
        } else {
            v = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_message_me, viewGroup, false)
            return MeVH(v)
        }

    }


    override fun getItemCount(): Int {
        return if (mMessagesList == null) 0 else mMessagesList.size
    }

    override fun getItemViewType(position: Int): Int {
        isMe = mMessagesList[position].user.id == idUser
        return if (position == mMessagesList.size - 1 && isLoadingAdded) LOADING
        else if (!isMe) ITEM
        else ME
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
        add(MyMessage(0, "", "", User("", 0, "", ""), 0))
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


    class ViewHolder : RecyclerView.ViewHolder {

        var text: TextView
        var name: TextView
        var time: TextView
        var date: TextView

        constructor(itemView: View) : super(itemView) {
            text = itemView.findViewById(R.id.textViewText)
            name = itemView.findViewById(R.id.textViewName)
            time = itemView.findViewById(R.id.textViewTime)
            date = itemView.findViewById(R.id.tvDate)
        }

    }

    class MeVH : RecyclerView.ViewHolder {

        var text: TextView
        var name: TextView
        var time: TextView
        var date: TextView

        constructor(itemView: View) : super(itemView) {
            text = itemView.findViewById(R.id.textViewText)
            name = itemView.findViewById(R.id.textViewName)
            time = itemView.findViewById(R.id.textViewTime)
            date = itemView.findViewById(R.id.tvDate)
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