package com.chdorner.roboreader.tasks;

import com.chdorner.roboreader.util.MD5;

import android.os.AsyncTask;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImportEPUBTask extends AsyncTask<Uri, Intent, Integer> {
  private final static String TAG = "com.chdorner.roboreader.tasks.ImportBookTask";

  public static Integer STATE_OK = 0;
  public static Integer STATE_ERROR = 1;

  private Context context = null;

  public ImportEPUBTask(Context context) {
    super();

    this.context = context;
  }

  protected Integer doInBackground(Uri... uris) {
    try {
      Uri uri = uris[0];

      String fileName = MD5.hexdigest(uri.toString() + new Date().toString()) + ".epub";
      File outputFile = new File(context.getExternalFilesDir(null), fileName);
      Log.d(TAG, "Writing to "+outputFile.getPath());

      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      OutputStream outputStream = new FileOutputStream(outputFile);

      byte[] data = new byte[inputStream.available()];
      inputStream.read(data);
      outputStream.write(data);

      inputStream.close();
      outputStream.close();
    } catch(IOException e) {
      Log.e(TAG, e.getMessage());
    }
    return STATE_OK;
  }
}

