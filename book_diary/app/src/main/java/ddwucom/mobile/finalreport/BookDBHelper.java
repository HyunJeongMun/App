package ddwucom.mobile.finalreport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "books.db";
    public static String TABLE_NAME = "book_table";
    public static String COL_ID = "_id";
    public static String COL_TITLE = "title";
    public static String COL_AUTHOR = "author";
    public static String COL_PRICE = "price";
    public static String COL_PUBLISHDATE = "publishDate";
    public static String COL_PUBLISHER = "publisher";
    public static String COL_COVER = "cover";

    public BookDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_TITLE + " TEXT, " + COL_AUTHOR + " TEXT, " + COL_PRICE + " INTEGER, " +
                COL_PUBLISHDATE + " TEXT, " + COL_PUBLISHER  + " TEXT, " + COL_COVER + " TEXT )";
        db.execSQL(sql);

        db.execSQL("insert into " + TABLE_NAME + " values (null,'백야행', '히가시노 게이고', 9000,'2006/01/27','태동출판사', " +  R.mipmap.one + ")");
        db.execSQL("insert into " + TABLE_NAME + " values (null,'둠스데이북1', '코니 월리스', 13320,'2018/02/22','아작', " +  R.mipmap.two + ")");
        db.execSQL("insert into " + TABLE_NAME + " values (null,'웃는 남자', '빅토르 위고',  22320,'2020/01/05','더스토리', " +  R.mipmap.three + ")");
        db.execSQL("insert into " + TABLE_NAME + " values (null,'돌이킬 수 없는 약속', '야쿠마루 가쿠', 13500,'2017/02/02','북플라자', " +  R.mipmap.four + ")");
        db.execSQL("insert into " + TABLE_NAME + " values (null,'파우스트', '요한 볼프강 폰 괴테', 11700,'2006/05/015','문학동네', " +  R.mipmap.five + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVar, int newVar){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
