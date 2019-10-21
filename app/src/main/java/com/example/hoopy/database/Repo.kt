package com.example.hoopy.database


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.hoopy.models.User

@Dao
interface Repo {

    @get:Query("select * from user")
    val users: LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)
}
