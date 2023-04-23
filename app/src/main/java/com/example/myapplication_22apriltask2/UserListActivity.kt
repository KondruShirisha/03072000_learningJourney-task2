package com.example.myapplication_22apriltask2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserListActivity : AppCompatActivity()  {
    private lateinit var userDao: UserDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        /*retrieve the User object from the Intent extras
         using the key "USER-data...........By using filterNotNull(), we create a new list of non-null User objects
         from the original list with null values removed. We then assign this non-null list
          to the users variable with the List<User> type.*/
        val userList: List<User> = intent.getParcelableArrayListExtra<User>("USER-LIST")?.filterNotNull() ?: emptyList()

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.rv_users)
        adapter = UserAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set the UserAdapter on the RecyclerView
        recyclerView.adapter = adapter


        // Get an instance of the UserDao
        val db = AppDatabase.getDatabase(application)
        userDao = db.userDao()


        // Observe changes to the user list in the Room database
        lifecycleScope.launch {
            try {
                val users = withContext(Dispatchers.IO) {
                    userDao.getAllUsers()
                }
                adapter = UserAdapter(users)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                println(e.message)
                // Handle the exception here
            }
        }
        // Set click listener for add_user_button
        findViewById<AppCompatImageButton>(R.id.add_user_button).setOnClickListener {
            startActivity(Intent(this@UserListActivity, MainActivity::class.java))
        }


    }
}

