package com.analyst.fragmenttest.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.analyst.fragmenttest.objects.Ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper";

    private static final String DATABASE_NAME ="Analyst.db";
    private static final String TABLE_NAME_TICKETS = "Tickets";
    private static final String TABLE_NAME_SETTINGS = "Settings";
    private static final String TABLE_NAME_CATEGORIES = "Categories";
    private static final String TABLE_NAME_GRAPHVALUES = "GraphValues";

    private static final String TABLE_NAME_TICKETS_COL_1 ="ID";
    private static final String TABLE_NAME_TICKETS_COL_2 ="name";
    private static final String TABLE_NAME_TICKETS_COL_3 ="category";
    private static final String TABLE_NAME_TICKETS_COL_4 ="ticket_type";
    private static final String TABLE_NAME_TICKETS_COL_5 ="currency";
    private static final String TABLE_NAME_TICKETS_COL_6 ="currency_type";
    private static final String TABLE_NAME_TICKETS_COL_7 ="date";
    private static final String TABLE_NAME_TICKETS_COL_8 ="dateInLong";
    private static final String TABLE_NAME_TICKETS_COL_9 ="currency_color";

    private static final String TABLE_NAME_SETTINGS_COL_1 ="ID";
    private static final String TABLE_NAME_SETTINGS_COL_2 ="start_amount";
    private static final String TABLE_NAME_SETTINGS_COL_3 ="currency_type";
    private static final String TABLE_NAME_SETTINGS_COL_4 ="save_storage_status";
    private static final String TABLE_NAME_SETTINGS_COL_5 ="default_";
    private static final String TABLE_NAME_SETTINGS_COL_6 ="dateInLong";

    private static final String TABLE_NAME_CATEGORIES_COL_1 ="ID";
    private static final String TABLE_NAME_CATEGORIES_COL_2 ="name";
    private static final String TABLE_NAME_CATEGORIES_COL_3 ="default_";

    private static final String TABLE_NAME_GRAPHVALUES_COL_1 ="ID";
    private static final String TABLE_NAME_GRAPHVALUES_COL_2 ="transactions";
    private static final String TABLE_NAME_GRAPHVALUES_COL_3 ="currency";
    private static final String TABLE_NAME_GRAPHVALUES_COL_4 ="dateInLong";


    //Create Query table strings
    private final String createTicketTable = "create table "+TABLE_NAME_TICKETS+" ("+
            TABLE_NAME_TICKETS_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_TICKETS_COL_2+" TEXT, "+
            TABLE_NAME_TICKETS_COL_3+" TEXT, "+
            TABLE_NAME_TICKETS_COL_4+" TEXT, "+
            TABLE_NAME_TICKETS_COL_5+" DOUBLE, "+
            TABLE_NAME_TICKETS_COL_6+" TEXT, "+
            TABLE_NAME_TICKETS_COL_7+" TEXT, " +
            TABLE_NAME_TICKETS_COL_8+" INTEGER, " +
            TABLE_NAME_TICKETS_COL_9+" INTEGER)";

    private final String createSettingsTable = "create table "+TABLE_NAME_SETTINGS+" ("+
            TABLE_NAME_SETTINGS_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_SETTINGS_COL_2+" DOUBLE, "+
            TABLE_NAME_SETTINGS_COL_3+" TEXT, "+
            TABLE_NAME_SETTINGS_COL_4+" INTEGER, " +
            TABLE_NAME_SETTINGS_COL_5+" INTEGER, " +
            TABLE_NAME_SETTINGS_COL_6+" INTEGER)";

    private final String createCategoriesTable = "create table "+TABLE_NAME_CATEGORIES+" ("+
            TABLE_NAME_CATEGORIES_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_CATEGORIES_COL_2+" TEXT, " +
            TABLE_NAME_CATEGORIES_COL_3+" INTEGER)";

    private final String createGraphValuesTable = "create table "+TABLE_NAME_GRAPHVALUES+" (" +
            TABLE_NAME_GRAPHVALUES_COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABLE_NAME_GRAPHVALUES_COL_2+" TEXT, " +
            TABLE_NAME_GRAPHVALUES_COL_3+" DOUBLE, " +
            TABLE_NAME_GRAPHVALUES_COL_4+" INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
        Log.e("DB: ", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTicketTable);
        db.execSQL(createSettingsTable);
        db.execSQL(createCategoriesTable);
        db.execSQL(createGraphValuesTable);
        Log.e("DB: ", "Tables created");

        db.execSQL("insert into "+TABLE_NAME_SETTINGS+" values(1, 0.0, '€', 0, 1, 0.0)");
        db.execSQL("insert into "+TABLE_NAME_CATEGORIES+" values(1, 'Other', 1)");
        Log.e("DB: ", "Default Settings created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_GRAPHVALUES);
        Log.e("DB: ", "Tables Deleted");
        onCreate(db);
    }


    // CRUD Base Ticket Table
    //Add
    public boolean insertTicket(String name, String category, String ticketType, double currency, String currencyType, String date, long dateInLong, int currencyColor){
        SQLiteDatabase db = this.getWritableDatabase();

        if (!name.isEmpty() && !category.isEmpty() && !ticketType.isEmpty() && currency != 0 && !currencyType.isEmpty() && !date.isEmpty() && (dateInLong != 0)){
            db.execSQL("insert into "+TABLE_NAME_TICKETS+
                    " values(null, '"+name+"', '"+category+"', '"+ticketType+"', '"+currency+"', '"+currencyType+"', '"+date+"', "+dateInLong+", "+currencyColor+")");
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

    public int getTicketDataCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_TICKETS, null);
        return res.getCount();
    }

    public Cursor getAllTicketDataOfTheLastYear(){
        SQLiteDatabase db = this.getWritableDatabase();
        long actualDate = Calendar.getInstance().getTime().getTime();
        long yearBefore = (actualDate - 315360000); //in milliseconds

        Cursor res = db.rawQuery("select * " +
                "from "+TABLE_NAME_TICKETS+" " +
                "where "+TABLE_NAME_TICKETS_COL_8+" between "+yearBefore+" and "+actualDate, null);

        Log.e(TAG, " Row= "+res.getCount()+" ; Query => select * from "+TABLE_NAME_TICKETS+" where "+TABLE_NAME_TICKETS_COL_8+" between "+yearBefore+" and "+actualDate);
        return res;
    }

    public Cursor getAllTicketDataOfTheLastMonth(){
        SQLiteDatabase db = this.getWritableDatabase();
        long actualDate = Calendar.getInstance().getTime().getTime();
        long monthBefore = (actualDate - 262974600); //in milliseconds

        Cursor res = db.rawQuery("select * " +
                "from "+TABLE_NAME_TICKETS+" " +
                "where "+TABLE_NAME_TICKETS_COL_8+" between "+monthBefore+" and "+actualDate, null);

        Log.e(TAG, " Row= "+res.getCount()+" ; Query => select * from "+TABLE_NAME_TICKETS+" where "+TABLE_NAME_TICKETS_COL_8+" between "+monthBefore+" and "+actualDate);
        return res;
    }

    public Cursor getAllTicketDataOfTheLastWeek(){
        SQLiteDatabase db = this.getWritableDatabase();
        long actualDate = Calendar.getInstance().getTime().getTime();
        long weekBefore = (actualDate - 604800000); //in milliseconds

        Cursor res = db.rawQuery("select * " +
                "from "+TABLE_NAME_TICKETS+" " +
                "where "+TABLE_NAME_TICKETS_COL_8+" between "+weekBefore+" and "+actualDate, null);

        Log.e(TAG, " Row= "+res.getCount()+" ; Query => select * from "+TABLE_NAME_TICKETS+" where "+TABLE_NAME_TICKETS_COL_8+" between "+weekBefore+" and "+actualDate);
        return res;
    }

    public Cursor getAllTicketDataOfTheLast24H(){
        SQLiteDatabase db = this.getWritableDatabase();
        long actualDate = Calendar.getInstance().getTime().getTime();
        long _24h_before = (actualDate - 86400000); //in milliseconds

        Cursor res = db.rawQuery("select * " +
                "from "+TABLE_NAME_TICKETS+" " +
                "where "+TABLE_NAME_TICKETS_COL_8+" between "+_24h_before+" and "+actualDate, null);

        Log.e(TAG, " Row= "+res.getCount()+" ; Query => select * from "+TABLE_NAME_TICKETS+" where "+TABLE_NAME_TICKETS_COL_8+" between "+_24h_before+" and "+actualDate);
        return res;
    }

    //Update
    public boolean updateTicketData(Ticket newTicketData) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!newTicketData.getName().isEmpty() && !newTicketData.getCategory().isEmpty() && !newTicketData.getTicketType().isEmpty()
                && newTicketData.getCurrency() != 0.0 && !newTicketData.getCurrencyType().isEmpty() && !newTicketData.getDate().isEmpty()
                && newTicketData.getDateInLong() != 0 && newTicketData.getCurrencyColor() != 0){
            db.execSQL("update "+TABLE_NAME_TICKETS+
                    " set "+TABLE_NAME_TICKETS_COL_2+" = '"+newTicketData.getName()+"', "+
                    TABLE_NAME_TICKETS_COL_3+" = '"+newTicketData.getCategory()+"', "+
                    TABLE_NAME_TICKETS_COL_4+" = '"+newTicketData.getTicketType()+"', "+
                    TABLE_NAME_TICKETS_COL_5+" = '"+newTicketData.getCurrency()+"', "+
                    TABLE_NAME_TICKETS_COL_6+" = '"+newTicketData.getCurrencyType()+"', "+
                    TABLE_NAME_TICKETS_COL_7+" = '"+newTicketData.getDate()+"', " +
                    TABLE_NAME_TICKETS_COL_8+" = '"+newTicketData.getDateInLong()+"', " +
                    TABLE_NAME_TICKETS_COL_9+" = '"+newTicketData.getCurrencyColor()+"' where "+TABLE_NAME_TICKETS_COL_1+" = "+newTicketData.getId());
            Log.e("DB: ", "ID: "+newTicketData.getId()+" is updated");
            return true;
        }else {
            Log.e("DB: ", "ID: "+newTicketData.getId()+" failed to update");
            return false;
        }
    }

    //Update ticket category
    public boolean updateTicketCategoryData(int id, String newCategory){
        SQLiteDatabase db = this.getWritableDatabase();

        if (!newCategory.isEmpty()){
            db.execSQL("update "+TABLE_NAME_TICKETS+" set "+TABLE_NAME_TICKETS_COL_3+" = '"+newCategory+"' where "+TABLE_NAME_TICKETS_COL_1+" = "+id);
            Log.e("DB: ", "Ticket ID: "+id+", set new category : "+newCategory+" is updated");
            return true;
        }else {
            Log.e("DB: ", "Ticket ID: "+id+", set new category : "+newCategory+" failed to update");
            return false;
        }
    }

    //Delete
    public Integer deleteTicketData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_TICKETS, TABLE_NAME_TICKETS_COL_1+" = ?", new String[] {String.valueOf(id)});
    }

    //Delete all tickets
    public boolean deleteAllTicketData(){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean status;

        try {
            db.execSQL("delete from "+TABLE_NAME_TICKETS);
            status = true;
        } catch (SQLException s){
            Log.e(TAG, " deleteAllTicketData => "+s.getMessage());
            status = false;
        } catch (Exception e){
            Log.e(TAG, " deleteAllTicketData => "+e.getMessage());
            status = false;
        }
        return status;
    }


    //CRUD Base Settings
    //Add Default
    public void insertDefaultSettingsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into "+TABLE_NAME_SETTINGS+" values(1, 0.0, '€', 0, 1, 0.0)");
        Log.e("DB: ", TABLE_NAME_SETTINGS+" => ID: 1 Default Settings Data insert is done");
    }

    //View Settings
    public Cursor getAllSettingsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_SETTINGS+" where "+TABLE_NAME_SETTINGS_COL_1+" = 1", null);
        return res;
    }

    //Update Settings
    public boolean updateStartAmout(double number, long dateInLong){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME_SETTINGS+
                " set "+TABLE_NAME_SETTINGS_COL_2+" = "+number+", " +
                TABLE_NAME_SETTINGS_COL_6+" = "+dateInLong+" where "+TABLE_NAME_SETTINGS_COL_1+" = 1");
        Log.e("DB: ", TABLE_NAME_SETTINGS_COL_2+" && "+TABLE_NAME_SETTINGS_COL_6+" => ID: 1 is updated");
        return true;
    }

    public boolean updateCurrencyType(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update "+TABLE_NAME_SETTINGS+
                " set "+TABLE_NAME_SETTINGS_COL_3+" = '"+type+"' where "+TABLE_NAME_SETTINGS_COL_1+" = 1");
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

    public Integer deleteAllSettings(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SETTINGS, TABLE_NAME_SETTINGS_COL_1+" = ?", new String[] {String.valueOf(id)});
    }


    // CRUD Base Categories Table
    public boolean insertDefaultCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into "+TABLE_NAME_CATEGORIES+" values(1, 'Other', 1)");
        Log.e("DB: ", TABLE_NAME_CATEGORIES+" => insert is done");
        return true;
    }

    //Add
    public boolean insertCategories(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if (!name.isEmpty()){
            db.execSQL("insert into "+TABLE_NAME_CATEGORIES+" values(null, '"+name+"', 0)");
            Log.e("DB: ", TABLE_NAME_CATEGORIES+" => insert is done");
            return true;
        }else {
            Log.e("DB: ", "insert failed");
            return false;
        }
    }

    public Cursor getDefaultCategoryData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_CATEGORIES+" where id = 1",null);
        return res;
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

    //Delete everything
    public void deleteEveryThing(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE from "+TABLE_NAME_TICKETS,null);
        db.rawQuery("DELETE from "+TABLE_NAME_SETTINGS,null);
        db.rawQuery("DELETE from "+TABLE_NAME_CATEGORIES,null);
        db.rawQuery("DELETE from "+TABLE_NAME_GRAPHVALUES,null);
    }

    public void getLocalDatabase(Context context){
        Log.e(TAG, "getLocalDatabase()");

        File f=new File("/data/data/"+context.getPackageName()+"/databases/"+DATABASE_NAME);
        Log.e(TAG, "File f: "+f.getAbsolutePath());
        FileInputStream fis=null;
        FileOutputStream fos=null;

        try
        {
            fis=new FileInputStream(f);
            fos=new FileOutputStream("/mnt/sdcard/db_dump.db");
            while(true)
            {
                int i=fis.read();
                if(i!=-1)
                {fos.write(i);}
                else
                {break;}
            }
            fos.flush();
            Toast.makeText(context, "DB dump OK", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "DB dump ERROR", Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                fos.close();
                fis.close();
            }
            catch(IOException ioe)
            {}
        }


        /*
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Log.e(TAG, "getExternalStorageDirectory : "+sd.getName()+" || "+sd.getAbsolutePath());

            if (sd.canWrite()) {
                String currentDBPath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
                String backupDBPath = DATABASE_NAME+"_backup.db";
                Log.e(TAG, "currentDBPath : "+currentDBPath);
                //previous wrong  code
                // **File currentDB = new File(data,currentDBPath);**
                // correct code
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e(TAG, "currentDB File : "+currentDB.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
