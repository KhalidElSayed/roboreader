package com.chdorner.roboreader;

import android.app.Application;
import android.util.Log;

import com.chdorner.roboreader.persistence.DatabaseHelper;

public class RoboReaderApplication extends Application {
  protected final String TAG = this.getClass().getName();

  private DatabaseHelper databaseHelper;

  public void onCreate() {
    super.onCreate();

    this.databaseHelper = new DatabaseHelper(this);
  }

  public DatabaseHelper getDatabaseHelper() {
    return this.databaseHelper;
  }
}

