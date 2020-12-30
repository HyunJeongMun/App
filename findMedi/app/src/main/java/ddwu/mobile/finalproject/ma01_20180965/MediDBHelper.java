package ddwu.mobile.finalproject.ma01_20180965;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MediDBHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "medi_db";
    public final static String TABLE_NAME = "medi_table";
    public final static String COL_ID  = "_id";
    public final static String COL_TYPE = "type";
    public final static String COL_ADDR = "addr";
    public final static String COL_DIVNAME = "divName";
    public final static String COL_NAME = "name";
    public final static String COL_TEL = "tel";
    public final static String COL_START = "start";
    public final static String COL_END = "close";
    public final static String COL_LAT = "lat";
    public final static String COL_LNG = "lng";
    public final static String COL_MEMO = "memo";
    public final static String COL_PHOTO = "photo";

    public MediDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement, " +
                COL_TYPE + " TEXT, " + COL_ADDR + " TEXT, " + COL_DIVNAME + " TEXT, " + COL_NAME + " TEXT, "
                + COL_TEL + " TEXT, " + COL_START + " TEXT, " + COL_END + " TEXT, " + COL_LAT + " TEXT, " + COL_LNG + " TEXT, " + COL_MEMO + " TEXT, " + COL_PHOTO + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
