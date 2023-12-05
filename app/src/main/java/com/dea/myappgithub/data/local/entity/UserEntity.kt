package com.dea.myappgithub.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorite_users")
data class UserEntity(
    @field:PrimaryKey
    val id: Int,

    @ColumnInfo
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
) : Serializable