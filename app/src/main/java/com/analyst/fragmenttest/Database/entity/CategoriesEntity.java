package com.analyst.fragmenttest.Database.entity;

/**
 * Created by JDevs on 03/08/2019.
 */

public class CategoriesEntity {
  private int id;
  private String name;
  private int default_;

  public CategoriesEntity() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDefault_() {
    return default_;
  }

  public void setDefault_(int default_) {
    this.default_ = default_;
  }
}