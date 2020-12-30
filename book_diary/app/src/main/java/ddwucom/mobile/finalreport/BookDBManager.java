package ddwucom.mobile.finalreport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class BookDBManager {
    BookDBHelper bookDBHelper = null;
    Cursor cursor = null;

    public BookDBManager(Context context) {
        bookDBHelper = new BookDBHelper(context);
    }

    public ArrayList<BookDTO> getAllBook() {
        ArrayList<BookDTO> bookList = new ArrayList<BookDTO>();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            String publishDate = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHDATE));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            int cover = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_COVER));

            bookList.add(new BookDTO (id, title, author, price, publishDate, publisher, cover) );
        }

        cursor.close();
        bookDBHelper.close();

        return bookList;
    }

    //    DB 에 새로운 food 추가
    public boolean addNewBook(BookDTO newBook) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(bookDBHelper.COL_TITLE, newBook.getTitle());
        value.put(bookDBHelper.COL_AUTHOR, newBook.getAuthor());
        value.put(bookDBHelper.COL_PRICE, newBook.getPrice());
        value.put(bookDBHelper.COL_PUBLISHDATE, newBook.getPublishDate());
        value.put(bookDBHelper.COL_PUBLISHER, newBook.getPublisher());
        value.put(bookDBHelper.COL_COVER, R.mipmap.book_simple);

        long result = sqLiteDatabase.insert(BookDBHelper.TABLE_NAME, null, value);
        bookDBHelper.close();

        if(result > 0) return true;

        return false;
    }

    //    _id 를 기준으로 food 의 이름과 nation 변경
    public boolean modifyBook(BookDTO book) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(BookDBHelper.COL_TITLE, book.getTitle());
        row.put(BookDBHelper.COL_AUTHOR, book.getAuthor());
        row.put(BookDBHelper.COL_PRICE, book.getPrice());
        row.put(BookDBHelper.COL_PUBLISHDATE, book.getPublishDate());
        row.put(BookDBHelper.COL_PUBLISHER, book.getPublisher());

        String whereClause = BookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(book.get_id())};
        Log.d("UPDATE", whereArgs[0]);

        int result = sqLiteDatabase.update(BookDBHelper.TABLE_NAME, row, whereClause, whereArgs);

        bookDBHelper.close();

        if(result > 0) return true;

        return false;
    }

    public boolean removeBook(long id) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();
        String whereClause = BookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        int result  = sqLiteDatabase.delete(BookDBHelper.TABLE_NAME, whereClause, whereArgs);

        bookDBHelper.close();
        if(result > 0)  return true;

        return false;
    }

    public ArrayList<BookDTO> getBookById(long id) {
        ArrayList<BookDTO> bookSearch = new ArrayList<BookDTO>();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME + " where _id = " + id, null);

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            String publishDate = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHDATE));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            int cover = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_COVER));

            Log.d("SEARCH", String.valueOf(id));
            bookSearch.add(new BookDTO (id, title, author, price, publishDate, publisher, cover));
        }

        cursor.close();
        bookDBHelper.close();

        return bookSearch;
    }

    public ArrayList<BookDTO> getBookByTitle(String title) {
        ArrayList<BookDTO> bookSearch = new ArrayList<BookDTO>();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME + " where title = '" + title + "'", null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            String publishDate = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHDATE));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            int cover = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_COVER));

            bookSearch.add(new BookDTO (id, title, author, price, publishDate, publisher, cover) );

        }

        cursor.close();
        bookDBHelper.close();

        return bookSearch;
    }

    public ArrayList<BookDTO> getBookByAuthor(String author) {
        ArrayList<BookDTO> bookSearch = new ArrayList<BookDTO>();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME + " where author = '" + author + "'", null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_TITLE));
            int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            String publishDate = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHDATE));
            String publisher = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHER));
            int cover = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_COVER));

            bookSearch.add(new BookDTO (id, title, author, price, publishDate, publisher, cover));
        }

        cursor.close();
        bookDBHelper.close();

        return bookSearch;
    }

    public ArrayList<BookDTO> getBookByPublisher(String publisher) {
        ArrayList<BookDTO> bookSearch = new ArrayList<BookDTO>();
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BookDBHelper.TABLE_NAME + " where publisher = '" + publisher + "'", null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_AUTHOR));
            int price = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_PRICE));
            String publishDate = cursor.getString(cursor.getColumnIndex(BookDBHelper.COL_PUBLISHDATE));
            int cover = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COL_COVER));

            bookSearch.add(new BookDTO (id, title, author, price, publishDate, publisher, cover));
        }

        cursor.close();
        bookDBHelper.close();

        return bookSearch;
    }

    //    close 수행
    public void close() {
        if (bookDBHelper != null) bookDBHelper.close();
        if (cursor != null) cursor.close();
    };
}
