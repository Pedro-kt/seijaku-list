package com.example.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(startVersion = 4, endVersion = 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE animes ADD COLUMN totalEpisodes INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE animes ADD COLUMN episodesWatched INTEGER NOT NULL DEFAULT 0")
    }
}