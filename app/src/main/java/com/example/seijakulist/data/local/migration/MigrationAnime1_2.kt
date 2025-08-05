package com.example.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("ALTER TABLE animes RENAME COLUMN score TO userScore")

        db.execSQL("ALTER TABLE animes ADD COLUMN statusUser TEXT NOT NULL DEFAULT ''")
    }
}