package com.analyst.fragmenttest.Database.entity;

/**
 * Created by JDevs on 03/08/2019.
 */

public class SettingsEntity {
  private int id;
  private double start_amount;
  private String currency_type;
  private int save_storage_status;
  private int default_;
  private long dateInLong;

  public SettingsEntity() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getStart_amount() {
    return start_amount;
  }

  public void setStart_amount(double start_amount) {
    this.start_amount = start_amount;
  }

  public String getCurrency_type() {
    return currency_type;
  }

  public void setCurrency_type(String currency_type) {
    this.currency_type = currency_type;
  }

  public int getSave_storage_status() {
    return save_storage_status;
  }

  public void setSave_storage_status(int save_storage_status) {
    this.save_storage_status = save_storage_status;
  }

  public int getDefault_() {
    return default_;
  }

  public void setDefault_(int default_) {
    this.default_ = default_;
  }

  public long getDateInLong() {
    return dateInLong;
  }

  public void setDateInLong(long dateInLong) {
    this.dateInLong = dateInLong;
  }
}