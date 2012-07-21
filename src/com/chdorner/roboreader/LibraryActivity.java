package com.chdorner.roboreader;

import com.chdorner.roboreader.tasks.ImportEPUBTask;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LibraryActivity extends ListActivity implements ActionBar.OnNavigationListener {
  private final String TAG = getClass().getName();

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.library);
    setupActionBar();

    Intent intent = getIntent();
    if("application/epub+zip".equals(getIntent().getType())) {
      importEPUB(this, intent);
    }
  }

  public boolean onNavigationItemSelected (int itemPosition, long itemId) {
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
