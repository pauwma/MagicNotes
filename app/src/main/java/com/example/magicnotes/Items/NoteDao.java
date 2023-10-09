package com.example.magicnotes.Items;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    //? Insert note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    //? Update note
    @Update
    int update(Note note);

    //? Delete note
    @Delete
    int delete(Note note);

    @Query("SELECT * FROM notes ORDER BY editedTimestamp DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId")
    LiveData<Note> getNoteById(int noteId);

    @Query("SELECT * FROM notes ORDER BY title COLLATE NOCASE ASC")
    LiveData<List<Note>> getAllNotesOrderedByTitleAsc();

    @Query("SELECT * FROM notes ORDER BY title COLLATE NOCASE DESC")
    LiveData<List<Note>> getAllNotesOrderedByTitleDesc();

    @Query("SELECT * FROM notes ORDER BY creationTimestamp ASC")
    LiveData<List<Note>> getAllNotesOrderedByDateAsc();

    @Query("SELECT * FROM notes ORDER BY creationTimestamp DESC")
    LiveData<List<Note>> getAllNotesOrderedByDateDesc();

    @Query("SELECT * FROM notes WHERE title LIKE :query")
    LiveData<List<Note>> searchNotesByTitle(String query);

}