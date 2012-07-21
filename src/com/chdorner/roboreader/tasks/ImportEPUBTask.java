package com.chdorner.roboreader.tasks;

import com.chdorner.roboreader.persistence.Book;
import com.chdorner.roboreader.persistence.DatabaseHelper;

import com.j256.ormlite.dao.Dao;

import android.os.AsyncTask;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.text.TextUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Author;

public class ImportEPUBTask extends AsyncTask<Uri, Intent, Integer> {
  private final static String TAG = "com.chdorner.roboreader.tasks.ImportBookTask";

  public static Integer STATE_OK = 0;
  public static Integer STATE_ERROR = 1;

  private Context mContext = null;

  public ImportEPUBTask(Context context) {
    super();

    this.mContext = context;
  }

  protected Integer doInBackground(Uri... uris) {
    try {
      DatabaseHelper databaseHelper = new DatabaseHelper(mContext);

      Uri uri = uris[0];

      Book book = new Book();
      book.generateIdentifier(uri.toString());
      File outputFile = book.getEPUBFile(mContext);

      copyFile(uri, book, outputFile);
      Metadata metadata = getEPUBMetadata(outputFile);

      book.setAuthor(buildAuthorString(metadata));
      book.setTitle(buildTitleString(metadata));

      Dao<Book, String> bookDao = databaseHelper.getBookDao();
      bookDao.create(book);

      databaseHelper.close();
    } catch(IOException e) {
      Log.e(TAG, e.getMessage());
    } catch(SQLException e) {
      Log.e(TAG, e.getMessage());
    }
    return STATE_OK;
  }

  private void copyFile(Uri uri, Book book, File outputFile) throws IOException {
    Log.d(TAG, "Writing to "+outputFile.getPath());

    InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
    OutputStream outputStream = new FileOutputStream(outputFile);

    byte[] data = new byte[inputStream.available()];
    inputStream.read(data);
    outputStream.write(data);

    inputStream.close();
    outputStream.close();
  }

  private Metadata getEPUBMetadata(File outputFile) throws IOException {
    InputStream inputStream = new FileInputStream(outputFile);
    EpubReader reader = new EpubReader();
    nl.siegmann.epublib.domain.Book epubBook = reader.readEpub(inputStream);

    Metadata metadata = epubBook.getMetadata();
    return metadata;
  }

  private String buildAuthorString(Metadata metadata) {
    ArrayList<String> authors = new ArrayList<String>();
    for(Author author : metadata.getAuthors()) {
      String authorName = author.getFirstname() + " " + author.getLastname();
      authors.add(authorName);
    }

    return TextUtils.join(", ", authors);
  }

  private String buildTitleString(Metadata metadata) {
    return metadata.getTitles().get(0);
  }
}

