package com.atemeo.lovechat.wadii.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_COMMENT};

    private static final String TAG = "DBDEMO";

    public LocationsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Locations createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Locations newComment = cursorToComment(cursor);

        // Log the comment stored
        Log.d(TAG, "comment = " + cursorToComment(cursor).toString()
                + " insert ID = " + insertId);

        cursor.close();
        return newComment;
    }

    public void deleteComment(Locations comment) {
        long id = comment.getId();
        Log.d(TAG, "delete comment = " + id);
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllComments() {
        System.out.println("Comment deleted all");
        Log.d(TAG, "delete all = ");
        database.delete(MySQLiteHelper.TABLE_COMMENTS, null, null);
    }

    public List<Locations> getAllComments() {
        List<Locations> comments = new ArrayList<Locations>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Locations comment = cursorToComment(cursor);
            Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
            comments.add(comment);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Locations cursorToComment(Cursor cursor) {
        Locations comment = new Locations();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }
}