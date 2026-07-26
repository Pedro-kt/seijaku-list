package com.yumedev.seijakulist.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_14_15 = object : Migration(startVersion = 14, endVersion = 15) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Eliminar tabla mangas si existe (por si hubo intentos previos)
        db.execSQL("DROP TABLE IF EXISTS mangas")

        // Crear tabla mangas limpia
        db.execSQL(
            """
            CREATE TABLE mangas (
                malId INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                titleEnglish TEXT,
                titleJapanese TEXT,
                imageUrl TEXT,
                bannerImage TEXT,
                typeManga TEXT NOT NULL,
                source TEXT,
                chapters INTEGER,
                volumes INTEGER,
                status TEXT NOT NULL,
                published TEXT,
                score REAL,
                scoreBy INTEGER,
                rank INTEGER,
                synopsis TEXT,
                background TEXT,
                genres TEXT NOT NULL,
                demographics TEXT,
                authors TEXT,
                serializations TEXT,
                userScore REAL NOT NULL,
                statusUser TEXT NOT NULL,
                userOpinion TEXT NOT NULL,
                chaptersRead INTEGER NOT NULL,
                volumesRead INTEGER NOT NULL,
                rereadCount INTEGER NOT NULL,
                startDate INTEGER,
                endDate INTEGER,
                plannedPriority TEXT,
                plannedNote TEXT,
                lastModified INTEGER NOT NULL
            )
            """
        )

        // Crear índices para mejorar performance en queries frecuentes
        db.execSQL(
            """
            CREATE INDEX index_mangas_statusUser ON mangas (statusUser)
            """
        )

        db.execSQL(
            """
            CREATE INDEX index_mangas_lastModified ON mangas (lastModified)
            """
        )
    }
}
