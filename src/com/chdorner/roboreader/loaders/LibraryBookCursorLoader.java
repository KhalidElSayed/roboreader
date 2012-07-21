package com.chdorner.roboreader.loaders;

import com.chdorner.roboreader.loaders.SimpleCursorLoader;
import com.chdorner.roboreader.persistence.DatabaseHelper;
import com.chdorner.roboreader.persistence.Book;

import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;

import java.sql.SQLException;

public class LibraryBookCursorLoader extends SimpleCursorLoader {
  private DatabaseHelper mDatabaseHelper;
  private int mBookState;

  public LibraryBookCursorLoader(int bookState, Context context, DatabaseHelper helper) {
    super(context);
    this.mDatabaseHelper = helper;
    this.mBookState = bookState;
  }

  public Cursor loadInBackground() {
    Cursor cursor = null;
    try {
      Dao<Book, String> bookDao = mDatabaseHelper.getBookDao();

      QueryBuilder<Book, String> queryBuilder = bookDao.queryBuilder();
      queryBuilder.where().eq(Book.FIELD_STATE, mBookState);

      PreparedQuery<Book> preparedQuery = queryBuilder.prepare();

      AndroidCompiledStatement compiledStatement = (AndroidCompiledStatement)preparedQuery.compile(mDatabaseHelper.getConnectionSource().getReadOnlyConnection(), StatementType.SELECT);

      cursor = compiledStatement.getCursor();
      cursor.getCount();
    } catch(SQLException e) {
      e.printStackTrace();
    }

    return cursor;
  }
}

