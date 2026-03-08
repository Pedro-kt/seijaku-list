package com.yumedev.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_12_13 = object : Migration(startVersion = 12, endVersion = 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE animes ADD COLUMN plannedPriority TEXT DEFAULT NULL
            """
        )
        db.execSQL(
            """
            ALTER TABLE animes ADD COLUMN plannedNote TEXT DEFAULT NULL
            """
        )
    }
}