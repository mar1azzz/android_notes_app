package com.example.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.notes.models.Note;
import java.util.ArrayList;
import java.util.List;

public class NotesDAO {
    private NotesDatabaseHelper dbHelper;

    public NotesDAO(Context context) {
        dbHelper = new NotesDatabaseHelper(context);
    }

    // Add a new note
    public long addNote(String title, String content, String category, long timestamp) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDatabaseHelper.COLUMN_TITLE, title);
        values.put(NotesDatabaseHelper.COLUMN_CONTENT, content);
        values.put(NotesDatabaseHelper.COLUMN_CATEGORY, category);
        values.put(NotesDatabaseHelper.COLUMN_TIMESTAMP, timestamp);

        long id = db.insert(NotesDatabaseHelper.TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    // Update an existing note
    public int updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesDatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(NotesDatabaseHelper.COLUMN_CONTENT, note.getContent());
        values.put(NotesDatabaseHelper.COLUMN_CATEGORY, note.getCategory());
        values.put(NotesDatabaseHelper.COLUMN_TIMESTAMP, note.getTimestamp());
        int rowsUpdated = db.update(NotesDatabaseHelper.TABLE_NOTES, values,
                NotesDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rowsUpdated;
    }

    // Get all notes
    public List<Note> getAllNotes(String sortOrder) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + NotesDatabaseHelper.TABLE_NOTES + " ORDER BY " + sortOrder;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_CATEGORY)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_TIMESTAMP))
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }

    // Delete a note by ID
    public void deleteNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("notes", "id=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    public void updateCategory(int noteId, String newCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category", newCategory);
        db.update("notes", values, "id=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

}