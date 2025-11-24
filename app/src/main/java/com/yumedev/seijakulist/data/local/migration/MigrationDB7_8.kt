package com.yumedev.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8 = object : Migration(startVersion = 7, endVersion = 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE user_profile ADD COLUMN top5AnimeIds TEXT DEFAULT NULL
            """
        )
    }
}
