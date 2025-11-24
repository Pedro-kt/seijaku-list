package com.yumedev.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(startVersion = 5, endVersion = 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE animes ADD COLUMN rewatchCount INTEGER NOT NULL DEFAULT 0")
    }
}