package in.vilik.kps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vili on 23/03/2017.
 */

public class HighScores {
    HighScoreDbHelper dbHelper;

    public HighScores(Context context) {
        dbHelper = new HighScoreDbHelper(context);
    }

    public class Entry {
        public String name;
        public int score;

        public Entry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public void add(String name, int score) {
        ContentValues values = new ContentValues();
        values.put(HighScoreDbHelper.COLUMN_NAME, name);
        values.put(HighScoreDbHelper.COLUMN_SCORE, score);

        dbHelper.getWritableDatabase().insert(HighScoreDbHelper.TABLE_NAME, null, values);
    }

    public ArrayList<Entry> getBestScores(int amount) {
        ArrayList<Entry> results = new ArrayList<>();

        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + HighScoreDbHelper.TABLE_NAME + " " +
                "ORDER BY " + HighScoreDbHelper.COLUMN_SCORE + " DESC LIMIT " + amount + ";", null);

        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(HighScoreDbHelper.COLUMN_NAME));
            int score = c.getInt(c.getColumnIndex(HighScoreDbHelper.COLUMN_SCORE));
            System.out.println(name + " " + score);
            results.add(new Entry(name, score));
        }

        c.close();

        return results;
    }
}
