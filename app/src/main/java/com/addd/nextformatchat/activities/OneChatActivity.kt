package com.addd.nextformatchat.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.addd.nextformatchat.*
import com.addd.nextformatchat.adapters.AdapterMessages
import com.addd.nextformatchat.model.EventMessage
import com.addd.nextformatchat.model.MyMessage
import com.addd.nextformatchat.model.SendMsg
import com.addd.nextformatchat.network.NetworkController
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_one_chat.*
import java.util.*


class OneChatActivity : AppCompatActivity(), NetworkController.MessagesCallback, MyWebSocket.SocketCallback {
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var TOTAL_PAGES = 4
    private var idChat = 0
    private var idUser = 0
    private lateinit var messages: ArrayList<MyMessage>
    private lateinit var adapter: AdapterMessages
    private lateinit var myWebSocket: MyWebSocket
    private lateinit var myMessage: MyMessage
    private lateinit var chatName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.addd.nextformatchat.R.layout.activity_one_chat)
        setSupportActionBar(toolbarOneChat)
        if (intent.hasExtra(CHAT_NAME)) {
            chatName = intent.getStringExtra(CHAT_NAME)
            title = chatName
        }
        toolbarOneChat.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbarOneChat.setNavigationOnClickListener { finish() }
        NetworkController.registerMessagesCallback(this)
//        NetworkController.registerSendMsgCallback(this)
        if (intent.hasExtra(ID_CHAT)) {
            idChat = intent.getIntExtra(ID_CHAT, 0)
            NetworkController.getChatMeasseges(1, false, idChat)
        }
        sendBtn.setOnClickListener { sendMsg() }
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        idUser = sp.getInt(ID_USER, 0)
        myWebSocket = MyWebSocket(idUser)
        myWebSocket.registerSocketCallback(this)
        myWebSocket.run()
        recyclerMessage.addOnLayoutChangeListener({ _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerMessage.post({
                    if (messages.isNotEmpty()) {
                        recyclerMessage.smoothScrollToPosition(
                                recyclerMessage.adapter.itemCount - 1)

                    }
                })
            }
        })
    }


    private fun sendMsg() {
        if (!editText.text.isEmpty()) {
            NetworkController.sendMsg(SendMsg(editText.text.toString()), idChat)
            editText.text.clear()
        }
    }

//    override fun resultSend(result: MyMessage?) {
//        if (result != null) {
//            toast("Отправил")
//        }
//    }

    override fun result(listMessage: ArrayList<MyMessage>, result: Boolean, count: Int, pagination: Boolean) {
        if (!pagination) {
            TOTAL_PAGES = if (count % 20 == 0) {
                count / 20
            } else {
                (count / 20) + 1
            }
            Collections.reverse(listMessage)
            messages = listMessage


            adapter = AdapterMessages(listMessage)
            recyclerMessage.adapter = adapter
            val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerMessage.layoutManager = layoutManager
            recyclerMessage.scrollToPosition(recyclerMessage.adapter.itemCount - 1)

        } else {
            //пиздатая пагинация вверх типа
        }
        progressBar3.visibility = View.GONE
    }

    override fun message(text: String) {
        val type = object : TypeToken<EventMessage>() {
        }.type
        val event = gson.fromJson<EventMessage>(text, type)
        if (event.event == "on_message") {
            if (event.data.idChat == idChat) {
                runOnUiThread {
                    adapter.addNewMessage(event.data)
                    recyclerMessage.scrollToPosition(recyclerMessage.adapter.itemCount - 1)
                }
            }
        }
        setResult(200)
    }

    override fun onDestroy() {
        myWebSocket.registerSocketCallback(null)
        super.onDestroy()
    }

}
