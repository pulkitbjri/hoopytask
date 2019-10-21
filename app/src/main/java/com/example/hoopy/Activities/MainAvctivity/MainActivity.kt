package com.example.hoopy.Activities.MainAvctivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoopy.Activities.FormActivity.FormActivity
import com.example.hoopy.Adapters.MainListAdapter
import com.example.hoopy.R
import com.example.hoopy.database.Database
import com.example.hoopy.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var userlist : ArrayList<User> = ArrayList()
    private lateinit var adapter : MainListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListners()
        recycler.layoutManager=LinearLayoutManager(this)
        adapter = MainListAdapter(userlist)
        recycler.adapter=adapter
        listenDB()
    }

    private fun listenDB() {
        Database.getDatabase(this).Dao().users.observe(this, Observer {
            count.setText("Number of Users ${it.size}")
            userlist.clear()
            userlist.addAll(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setListners() {
        add.setOnClickListener{
            startActivity(Intent(this@MainActivity, FormActivity::class.java))
        }
    }
}
