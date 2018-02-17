package com.addd.nextformatchat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.addd.nextformatchat.PaginationScrollListener
import com.addd.nextformatchat.R
import com.addd.nextformatchat.adapters.AdapterUsers
import com.addd.nextformatchat.model.User
import com.addd.nextformatchat.network.NetworkController
import kotlinx.android.synthetic.main.activity_people.*

class PeopleActivity : AppCompatActivity(), NetworkController.CallbackUsers, AdapterUsers.CustomAdapterCallback {
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var TOTAL_PAGES = 4
    private lateinit var users: ArrayList<User>
    private lateinit var adapter: AdapterUsers


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        setSupportActionBar(toolbarPeople)
        toolbarPeople.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbarPeople.setNavigationOnClickListener { finish() }
        title = getString(R.string.users)
        NetworkController.registrationUsersCallback(this)
        NetworkController.getChatUsers(1, false)
    }

    override fun onItemClick(pos: Int) {

    }

    override fun resultUsers(listUsers: ArrayList<User>, result: Boolean, count: Int, pagination: Boolean) {
        if (!pagination) {
            TOTAL_PAGES = if (count % 20 == 0) {
                count / 20
            } else {
                (count / 20) + 1
            }
            users = listUsers


            adapter = AdapterUsers(listUsers, this)
            recyclerPeople.adapter = adapter
            val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerPeople.layoutManager = layoutManager

            recyclerPeople.addOnScrollListener(object : PaginationScrollListener(recyclerPeople.layoutManager as LinearLayoutManager) {
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

                if (!listUsers.isEmpty()) {
                    adapter.addAll(listUsers)
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
        progressBarPeople.visibility = View.GONE
    }

    private fun loadNextPage() {
        NetworkController.getChatUsers(currentPage, true)
    }

    private fun addFooter() {
        if (currentPage < TOTAL_PAGES) {
            adapter.addLoadingFooter()
        } else {
            isLastPage = true
        }
    }
}
