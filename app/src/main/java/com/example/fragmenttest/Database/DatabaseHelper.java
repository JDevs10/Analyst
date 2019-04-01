package com.example.fragmenttest.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper";

    private static final String DATABASE_NAME ="Analyst.db";
    private static final String TABLE_NAME_TICKETS = "Tickets";
    private static final String TABLE_NAME_SETTINGS = "Settings";
    private static final String TABLE_NAME_CATEGORIES = "Categories";

    private static final String TABLE_NAME_TICKETS_COL_1 ="ID";
    private static final String TABLE_NAME_TICKETS_COL_2 ="name";
    private static final String TABLE_NAME_TICKETS_COL_3 ="category";
    private static final String TABLE_NAME_TICKETS_COL_4 ="ticket_type";
    private static final String TABLE_NAME_TICKETS_COL_5 ="currency";
    private static final String TABLE_NAME_TICKETS_COL_6 ="currency_type";
    private static final String TABLE_NAME_TICKETS_COL_7 ="date";

    private static final String TABLE_NAME_SETTINGS_COL_1 ="ID";
    private static final String TABLE_NAME_SETTINGS_COL_2 ="start_amount";
    private static final String TABLE_NAME_SETTINGS_COL_3 ="currency_type";
    private static final String TABLE_NAME_SETTINGS_COL_4 ="save_storage_status";

    private static final String TABLE_NAME_CATEGORIES_COL_1 ="ID";
    private static final String TABLE_NAME_CATEGORIES_COL_2 ="name";


    //Create Query table strings
    private final String createTicketTable = "create table "+TABLE_NAME_TICKETS+" ("+
            TABLE_NAME_TICKETS_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_TICKETS_COL_2+" TEXT, "+
            TABLE_NAME_TICKETS_COL_3+" TEXT, "+
            TABLE_NAME_TICKETS_COL_4+" TEXT, "+
            TABLE_NAME_TICKETS_COL_5+" DOUBLE, "+
            TABLE_NAME_TICKETS_COL_6+" TEXT, "+
            TABLE_NAME_TICKETS_COL_7+" TEXT)";

    private final String createSettingsTable = "create table "+TABLE_NAME_SETTINGS+" ("+
            TABLE_NAME_SETTINGS_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_SETTINGS_COL_2+" DOUBLE, "+
            TABLE_NAME_SETTINGS_COL_3+" TEXT, "+
            TABLE_NAME_SETTINGS_COL_4+" INTEGER)";

    private final String createCategoriesTable = "create table "+TABLE_NAME_CATEGORIES+" ("+
            TABLE_NAME_CATEGORIES_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_CATEGORIES_COL_2+" TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.e("DB: ", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTicketTable);
        db.execSQL(createSettingsTable);
        db.execSQL(createCategoriesTable);
        Log.e("DB: ", "Tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CATEGORIES);
        Log.e("DB: ", "Tables Deleted");
        onCreate(db);
    }

    // CRUD Base Ticket Table
    //Add
    public boolean insertTicket(String name, String category, String ticketType, double currency, String currencyType, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        if (!name.isEmpty() && !category.isEmpty() && !ticketType.isEmpty() && currency != 0 && !currencyType.isEmpty() && !date.isEmpty()){
            db.execSQL("insert into "+TABLE_NAME_TICKETS+
                    " values(null, '"+name+"', '"+category+"', '"+ticketType+"', '"+currency+"', '"+currencyType+"', '"+date+"')");
            Log.e("DB: ", "insert is done");
            return true;

        }else {
            Log.e("DB: ", "insert failed");
            return false;
        }
    }

    //View
    public Cursor getAllTicketData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_TICKETS, null);
        return res;
    }

    //Update
    public boolean updateTicketData(int id, String name, String category, String ticketType, double currency, String currencyType, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!name.isEmpty() && !category.isEmpty() && !ticketType.isEmpty() && currency != 0 && !currencyType.isEmpty() && !date.isEmpty()){
            db.execSQL("update "+TABLE_NAME_TICKETS+
                    " set "+TABLE_NAME_TICKETS_COL_2+" = "+name+", "+
                    TABLE_NAME_TICKETS_COL_3+" = "+category+", "+
                    TABLE_NAME_TICKETS_COL_4+" = "+ticketType+", "+
                    TABLE_NAME_TICKETS_COL_5+" = "+currency+", "+
                    TABLE_NAME_TICKETS_COL_6+" = "+currencyType+", "+
                    TABLE_NAME_TICKETS_COL_7+" = "+date+" where "+TABLE_NAME_TICKETS_COL_1+" = "+id);
            Log.e("DB: ", "ID: "+id+" is updated");
            return true;
        }else {
            Log.e("DB: ", "ID: "+id+" failed to updated");
            return false;
        }
    }

    //Delete
    public Integer deleteTicketData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_TICKETS, TABLE_NAME_TICKETS_COL_1+" = ?", new String[] {String.valueOf(id)});
    }


    //CRUD Base Settings
    //Add Default
    public void insertDefaultSettingsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into "+TABLE_NAME_SETTINGS+" values(1, 0.0, '€', 0)");
        Log.e("DB: ", TABLE_NAME_SETTINGS+" => ID: 1 insert is done");
    }

    //View Settings
    Cursor getAllSettingsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_SETTINGS, null);
        return res;
    }

    //Update Settings
    public boolean updateStartAmout(double number){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME_SETTINGS+
                " set "+TABLE_NAME_SETTINGS_COL_2+" = "+number+" where "+TABLE_NAME_SETTINGS_COL_1+" = 1");
        Log.e("DB: ", TABLE_NAME_SETTINGS_COL_2+" => ID: 1 is updated");
        return true;
    }

    public boolean updateCurrencyType(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME_SETTINGS+
                " set "+TABLE_NAME_SETTINGS_COL_3+" = "+type+" where "+TABLE_NAME_SETTINGS_COL_1+" = 1");
        Log.e("DB: ", TABLE_NAME_SETTINGS_COL_3+" => ID: 1 is updated");
        return true;
    }

    public boolean updateSaveStorageStatus(int status){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME_SETTINGS+
                " set "+TABLE_NAME_SETTINGS_COL_4+" = "+status+" where "+TABLE_NAME_SETTINGS_COL_1+" = 1");
        Log.e("DB: ", TABLE_NAME_SETTINGS_COL_4+" => ID: 1 is updated");
        return true;
    }



    // CRUD Base Categories Table
    //Add
    public boolean insertCategories(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if (!name.isEmpty()){
            db.execSQL("insert into "+TABLE_NAME_TICKETS+" values(null, '"+name+"')");
            Log.e("DB: ", TABLE_NAME_CATEGORIES+" => insert is done");
            return true;
        }else {
            Log.e("DB: ", "insert failed");
            return false;
        }
    }

    //View
    public Cursor getAllCategoriesData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_CATEGORIES, null);
        return res;
    }

    //Delete
    public Integer deleteCategoriesData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_CATEGORIES, TABLE_NAME_CATEGORIES_COL_1+" = ?", new String[] {String.valueOf(id)});
    }


}
