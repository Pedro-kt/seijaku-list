package com.example.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(startVersion = 6, endVersion = 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE user_profile (
                uid TEXT NOT NULL,
                username TEXT,
                profilePictureUrl TEXT,
                PRIMARY KEY(uid)
            )
            """
        )
    }
}