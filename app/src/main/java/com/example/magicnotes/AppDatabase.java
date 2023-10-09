package com.example.magicnotes;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.magicnotes.Items.Note;
import com.example.magicnotes.Items.NoteDao;

@Database(entities = {Note.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract NoteDao noteDao();

    //? Singleton para evitar múltiples instancias de la base de datos abiertas al mismo tiempo.
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "notes_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}