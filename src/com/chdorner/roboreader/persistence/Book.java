package com.chdorner.roboreader.persistence;

import com.chdorner.roboreader.util.MD5;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import android.content.Context;

import java.io.File;
import java.util.Date;

@DatabaseTable(tableName = "books")
public class Book {
  public static final Integer STATE_READING = 1;
  public static final Integer STATE_FINISHED = 2;
  public static final Integer STATE_ABANDONED = 3;

  @DatabaseField(id = true)
  private String identifier;
  @DatabaseField
  private String title;
  @DatabaseField
  private String author;
  @DatabaseField(canBeNull = false)
  private Integer state = STATE_READING;

  public Book() {
  }

  public File getEPUBFile(Context context) {
    String fileName = getIdentifier() + ".epub";
    return new File(context.getExternalFilesDir(null), fileName);
  }

  public void generateIdentifier() {
    generateIdentifier(null);
  }

  public void generateIdentifier(String salt) {
    if(salt == null) {
      salt = "";
    }
    this.identifier = MD5.hexdigest(salt + new Date().toString());
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}

