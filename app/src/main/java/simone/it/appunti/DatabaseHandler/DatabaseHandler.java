package simone.it.appunti.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import simone.it.appunti.Models.Note;

/**
 * Created by Simone on 13/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String COLUMN_NAME_ID ="ID";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_NAME_TEXT = "text";
    private static final String COLUMN_NAME_DATE = "date";
    private static final String COLUMN_NAME_COLOR = "color";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Note.db";
    public static final String TABLE_NAME = "Note";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ DatabaseHandler.TABLE_NAME+ "("+ DatabaseHandler.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
            DatabaseHandler.COLUMN_NAME_TITLE + " TEXT,"+ DatabaseHandler.COLUMN_NAME_TEXT + " TEXT," + DatabaseHandler.COLUMN_NAME_DATE + " TEXT," + DatabaseHandler.COLUMN_NAME_COLOR + " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseHandler.TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addNote (Note Note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, Note.getTitle());
        values.put(COLUMN_NAME_TEXT, Note.getText());
        values.put(COLUMN_NAME_DATE, Note.getDate());
        values.put(COLUMN_NAME_COLOR, Note.getColor());

        long i=db.insert(DatabaseHandler.TABLE_NAME, null, values);
        Note.setId((int)i);
        db.close();
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> NotesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note Note = new Note();
                Note.setId(Integer.parseInt(cursor.getString(0)));
                Note.setTitle(cursor.getString(1));
                Note.setText(cursor.getString(2));
                Note.setDate(cursor.getString(3));
                Note.setColor(cursor.getString(4));
                NotesList.add(Note);
            } while (cursor.moveToNext());
        }

        return NotesList;
    }

    public int updateNote(Note Note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, Note.getTitle());
        values.put(COLUMN_NAME_TEXT, Note.getText());
        values.put(COLUMN_NAME_DATE, Note.getDate());
        values.put(COLUMN_NAME_COLOR, Note.getColor());
        return db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(Note.getId())});
    }

    public void deleteNote(Note Note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?",
                new String[]{String.valueOf(Note.getId())});
        db.close();
    }

    public ArrayList<Note> getSearchNotes(CharSequence s) {
        ArrayList<Note> NotesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_TITLE + " LIKE '" + s + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note Note = new Note();
                Note.setId(Integer.parseInt(cursor.getString(0)));
                Note.setTitle(cursor.getString(1));
                Note.setText(cursor.getString(2));
                Note.setDate(cursor.getString(3));
                Note.setColor(cursor.getString(4));
                NotesList.add(Note);
            } while (cursor.moveToNext());
        }

        return NotesList;
    }

    public ArrayList<Note> getOrderDescNotes() {
        ArrayList<Note> NotesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_TITLE + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note Note = new Note();
                Note.setId(Integer.parseInt(cursor.getString(0)));
                Note.setTitle(cursor.getString(1));
                Note.setText(cursor.getString(2));
                Note.setDate(cursor.getString(3));
                Note.setColor(cursor.getString(4));
                NotesList.add(Note);
            } while (cursor.moveToNext());
        }

        return NotesList;
    }

    public ArrayList<Note> getOrderAscNotes() {
        ArrayList<Note> NotesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_TITLE + " ASC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note Note = new Note();
                Note.setId(Integer.parseInt(cursor.getString(0)));
                Note.setTitle(cursor.getString(1));
                Note.setText(cursor.getString(2));
                Note.setDate(cursor.getString(3));
                Note.setColor(cursor.getString(4));
                NotesList.add(Note);
            } while (cursor.moveToNext());
        }

        return NotesList;
    }
}
