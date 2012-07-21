package com.chdorner.roboreader.persistence;

import com.chdorner.roboreader.util.MD5;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

@DatabaseTable(tableName = "books")
public class Book {
  @DatabaseField(id = true)
  private String identifier;
  @DatabaseField
  private String title;
  @DatabaseField
  private String author;

  public Book() {
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

