package com.chdorner.roboreader;

import com.chdorner.roboreader.RoboReaderApplication;
import com.chdorner.roboreader.loaders.LibraryBookCursorLoader;
import com.chdorner.roboreader.persistence.DatabaseHelper;
import com.chdorner.roboreader.persistence.Book;
import com.chdorner.roboreader.tasks.ImportEPUBTask;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.Context;
import android.content.Loader;
import android.util.Log;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class LibraryActivity
  extends OrmLiteBaseListActivity<DatabaseHelper>
  implements ActionBar.OnNavigationListener, 
             LoaderManager.LoaderCallbacks<Cursor> {

  private final String TAG = getClass().getName();

  private SimpleCursorAdapter mCursorAdapter;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.library);
    setupActionBar();
    setupListView();

    Intent intent = getIntent();
    if("application/epub+zip".equals(getIntent().getType())) {
      importEPUB(this, intent);
    }
  }

  protected void onListItemClick (ListView l, View v, int position, long id) {
    SQLiteCursor cursor = (SQLiteCursor)getListAdapter().getItem(position);
    String book_id = cursor.getString(cursor.getColumnIndex(Book.FIELD_IDENTIFIER));
    Log.d(TAG, "clicked on book id: " + book_id);
  }

  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    if(v.getId() == android.R.id.list) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

      SQLiteCursor cursor = (SQLiteCursor)getListAdapter().getItem(info.position);
      String book_title = cursor.getString(cursor.getColumnIndex(Book.FIELD_TITLE));
      menu.setHeaderTitle(book_title);

      String[] menuItems = getResources().getStringArray(R.array.library_book_list_context_menu);
      for(String menuItem : menuItems) {
        menu.add(menuItem);
      }
    }
  }

  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    int selectedIndex = getActionBar().getSelectedNavigationIndex();
    int state = Book.STATE_READING;
    switch(selectedIndex) {
      case Book.STATE_FINISHED: state = Book.STATE_FINISHED;
      case Book.STATE_ABANDONED: state = Book.STATE_ABANDONED;
    }
    return new LibraryBookCursorLoader(state, this, getHelper());
  }

  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursorAdapter.swapCursor(data);
  }

  public void onLoaderReset(Loader<Cursor> loader) {
    mCursorAdapter.swapCursor(null);
  }

  private void updateListViewData() {
    getLoaderManager().restartLoader(0, null, this);
  }

  public boolean onNavigationItemSelected (int itemPosition, long itemId) {
    updateListViewData();
    return true;
  }

  private void setupActionBar() {
    ActionBar bar = getActionBar();
    bar.setDisplayShowTitleEnabled(false);
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    ArrayAdapter<CharSequence> bookStates = ArrayAdapter.createFromResource(this, R.array.book_states, android.R.layout.simple_dropdown_item_1line);
    bookStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    bar.setListNavigationCallbacks(bookStates, this);
  }

  private void setupListView() {
    String[] fromColumns = {Book.FIELD_TITLE, Book.FIELD_AUTHOR};
    int[] toViews = {R.id.book_title, R.id.book_author};
    mCursorAdapter = new SimpleCursorAdapter(this,
        R.layout.library_book_list_item,
        null,
        fromColumns,
        toViews,
        0);
    setListAdapter(mCursorAdapter);
    getLoaderManager().initLoader(0, null, this);

    registerForContextMenu(getListView());
  }

  private void importEPUB(Context context, Intent intent) {
    if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      Toast.makeText(context, "Cannot import Book at the moment", Toast.LENGTH_LONG).show();
      return;
    }

    Toast.makeText(context, "Importing book...", Toast.LENGTH_SHORT).show();
    RoboReaderApplication application = (RoboReaderApplication)getApplication();
    new ImportEPUBTask(this).execute(intent.getData());
  }
}

