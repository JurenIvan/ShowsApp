package com.example.shows_jurenivan.data.dataStructures

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
@Entity(tableName = "shows")
class Show(

    @PrimaryKey
    @Json(name = "_id")
    val id: String,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String?,

    @ColumnInfo(name = "imageURL")
    @Json(name = "imageUrl")
    val imageURL: String?,

    @ColumnInfo(name = "description")
    @Json(name = "description")
    var description: String?,

    @ColumnInfo(name = "likesCount")
    @Json(name = "likesCount")
    var likesCount:Int,

    @ColumnInfo(name="likeStatus")
    var likeStatus:Int?

): Serializable