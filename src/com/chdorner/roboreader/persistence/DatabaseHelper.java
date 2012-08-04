package com.chdorner.roboreader.persistence;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.dao.Dao;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
  protected final String TAG = this.getClass().getName();

  private static final String DATABASE_NAME = "roboreader.db";
  private static final int DATABASE_VERSION = 1;

  private Dao<Book, String> mBookDao = null;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    Log.d(TAG, "onCreate");
    try {
      TableUtils.createTable(connectionSource, Book.class);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
      int oldVersion, int newVersion) {
    Log.d(TAG, "onUpgrade");
  }

  public Dao<Book, String> getBookDao() throws SQLException {
    if (mBookDao == null) {
      this.mBookDao = getDao(Book.class);
    }
    return mBookDao;
  }
}
