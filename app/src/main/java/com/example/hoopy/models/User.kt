package com.example.hoopy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
 class User(
    @PrimaryKey
    var id: Long?,
    var name: String?,
    var username: String?,
    @SerializedName("contact")
    var mobile: Long?,
    @SerializedName("image_url")
    var imageUrl: String?,
    var email: String?
) : Serializable