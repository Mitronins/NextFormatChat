package com.addd.nextformatchat.activities

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.addd.nextformatchat.*
import com.addd.nextformatchat.adapters.AdapterChats
import com.addd.nextformatchat.model.Chat
import com.addd.nextformatchat.model.EventMessage
import com.addd.nextformatchat.network.NetworkController
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_chats.*

class ChatsActivity : AppCompatActivity(), NetworkController.ChatsCallback, AdapterChats.CustomAdapterCallback,
        MyWebSocket.SocketCallback {
    private lateinit var chats: ArrayList<Chat>
    private lateinit var adapter: AdapterChats
    private var isLoading = false
    private var isShow = true
    private var isLastPage = false
    private var currentPage = 1
    private var TOTAL_PAGES = 4
    private var idUser = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        setSupportActionBar(toolbarChats)
        NetworkController.registerChatCallback(this)
        NetworkController.getChats(1, false)
        floatingActionButton.setOnClickListener { startActivityForResult(Intent(applicationContext, PeopleActivity::class.java), 100) }
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        idUser = sp.getInt(ID_USER, 0)
        myWebSocket = MyWebSocket(idUser)
        myWebSocket.registerSocketCallback(this)
        myWebSocket.run()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        myWebSocket.registerSocketCallback(this)
        if ((requestCode == 100 || requestCode == 10) && resultCode == 200) {
            adapter = AdapterChats(ArrayList(), this)
            progressBar.visibility = View.VISIBLE
            recyclerChat.adapter = adapter
            adapter.notifyDataSetChanged()
            NetworkController.getChats(1, false)
        }
    }

    override fun message(text: String) {
        val type = object : TypeToken<EventMessage>() {
        }.type
        val event = gson.fromJson<EventMessage>(text, type)
        if (event.event == "on_message") {
            adapter = AdapterChats(ArrayList(), this)
            progressBar.visibility = View.VISIBLE
            recyclerChat.adapter = adapter
            adapter.notifyDataSetChanged()
            NetworkController.getChats(1, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.action_exit -> {
                exitFromAccount()
            }
            R.id.profile -> {
                startActivity(Intent(applicationContext, ProfileActivity::class.java))
            }
        }
        return true
    }

    override fun onItemClick(pos: Int) {
        val intent = Intent(applicationContext, OneChatActivity::class.java)
        intent.putExtra(ID_CHAT, chats[pos].id)
        intent.putExtra(CHAT_NAME, chats[pos].name)
        startActivityForResult(intent, 10)
    }

    private fun exitFromAccount() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit_account)
                .setMessage(R.string.realy_exit_account)
                .setCancelable(false)
                .setPositiveButton(R.string.yes)
                { _, _ ->
                    val mSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val editor = mSettings.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun result(listChats: ArrayList<Chat>, result: Boolean, count: Int, pagination: Boolean) {
        if (!pagination) {
            TOTAL_PAGES = if (count % 20 == 0) {
                count / 20
            } else {
                (count / 20) + 1
            }
            chats = listChats


            adapter = AdapterChats(listChats, this)
            recyclerChat.adapter = adapter
            val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerChat.layoutManager = layoutManager

            recyclerChat.addOnScrollListener(object : ScrollWithFAB(recyclerChat.layoutManager as LinearLayoutManager) {
                override fun fabHide() {
                    floatingActionButton.hide()
                }

                override fun fabShow() {
                    floatingActionButton.show()
                }

                override fun isShown(): Boolean {
                    return isShow
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    currentPage += 1

                    loadNextPage()
                }

                override fun getTotalPageCount(): Int {
                    return TOTAL_PAGES
                }

            })
            addFooter()
        } else {
            if (!adapter.isEmpty()) {
                adapter.removeLoadingFooter()
                isLoading = false

                if (!listChats.isEmpty()) {
                    adapter.addAll(listChats)
                } else {
                    currentPage -= 1
                }

                if (currentPage != TOTAL_PAGES) {
                    adapter.addLoadingFooter()
                } else {
                    isLastPage = true
                }
            }
            isLoading = false
        }
        progressBar.visibility = View.GONE
    }

    private fun loadNextPage() {
        NetworkController.getChats(currentPage, true)
    }

    private fun addFooter() {
        if (currentPage < TOTAL_PAGES) {
            adapter.addLoadingFooter()
        } else {
            isLastPage = true
        }
    }

    override fun onDestroy() {
        NetworkController.registerChatCallback(null)
        myWebSocket.registerSocketCallback(null)
        super.onDestroy()
    }
}
