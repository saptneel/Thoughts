package com.saptneel.thoughts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NHandler extends DBHelper {
    public NHandler(Context context) {
        super(context);
    }

    public boolean create(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean check = db.insert("Notes", null, values) > 0;
        db.close();
        return check;
    }

    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Notes Order By id ASC", null);

        if(cursor.moveToFirst()) {
            do{
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                Note note = new Note(title, description);
                note.setId(id);
                notes.add(note);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return notes;
    }

    public Note readSingleNote(int id) {
        String query = "Select * from Notes Where id='" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Note note = null;
        if(cursor.moveToFirst()) {
                int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                note = new Note(title, description);
                note.setId(noteId);
        }
        cursor.close();
        db.close();
        return note;
    }

    public boolean update(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("description", note.getDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        boolean check = db.update("Notes", values, "id='" + note.getId()+ "'", null) > 0;
        db.close();
        return check;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean check = db.delete("Notes", "id='" + id + "'", null) > 0;
        db.close();
        return check;
    }
}
