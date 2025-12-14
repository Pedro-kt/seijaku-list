package com.yumedev.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_9_10 = object : Migration(startVersion = 9, endVersion = 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE user_profile ADD COLUMN bio TEXT DEFAULT NULL
            """
        )
    }
}
