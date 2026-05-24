package com.example.bibliothequeapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// BONUS : version 2 pour le nouveau champ anneePublication
@Database(entities = {Livre.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract LivreDao livreDao();

    // Migration de la version 1 vers la version 2 :
    // on ajoute la colonne anneePublication avec 0 comme valeur par défaut
    // pour les livres déjà existants en base.
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE livres ADD COLUMN anneePublication INTEGER NOT NULL DEFAULT 0"
            );
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "bibliotheque_database"
                            )
                            .addMigrations(MIGRATION_1_2) // ✅ migration propre, les données existantes sont conservées
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}