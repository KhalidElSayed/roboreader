package com.chdorner.roboreader;

import com.chdorner.roboreader.tasks.ImportEPUBTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LibraryActivity extends Activity {
  private final String TAG = getClass().getName();

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Intent intent = getIntent();
    if("application/epub+zip".equals(getIntent().getType())) {
      importBook(this, intent);
    }
  }

  private void importBook(Context context, Intent intent) {
    if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      Toast.makeText(context, "Cannot import Book at the moment", Toast.LENGTH_LONG).show();
      return;
    }

    Toast.makeText(context, "Importing book...", Toast.LENGTH_SHORT).show();
    new ImportEPUBTask(this).execute(intent.getData());
  }
}
