package com.example.fragmenttest;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragmenttest.Database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTicketActivity extends AppCompatActivity {

    private ArrayList<String> categoriesList;
    private ArrayList<String> ticketTypeList;

    private Button btn_cancel;
    private Button btn_save;

    private EditText et_name;
    private EditText et_currency;

    private Spinner sp_category;
    private Spinner sp_ticketType;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_ticket);

        btn_cancel = (Button) findViewById(R.id.button_add_ticket_cancel);
        btn_save = (Button) findViewById(R.id.button_add_ticket_save);
        et_name = (EditText) findViewById(R.id.editText_add_ticket_name);
        et_currency = (EditText) findViewById(R.id.editText_add_ticket_currency);
        sp_category = (Spinner) findViewById(R.id.spinner_add_ticket_category);
        sp_ticketType = (Spinner) findViewById(R.id.spinner_add_ticket_ticketType);

        db = new DatabaseHelper(this);

        //get Categories Tickets
        categoriesList = new ArrayList<>();
        getCategories();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoriesList);
        sp_category.setAdapter(adapter1);

        //get Ticket types
        ticketTypeList = new ArrayList<>();
        getTicketType();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ticketTypeList);
        sp_ticketType.setAdapter(adapter2);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTicketActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savingTicket();
            }
        });
    }

    private void savingTicket() {
        String currency = et_currency.getText().toString();
        String name = et_name.getText().toString();
        String categorySelected = sp_category.getSelectedItem().toString();
        String ticketTypeSelected = sp_ticketType.getSelectedItem().toString();
        String currencyType = "€";

        boolean allChecked = true;

        if (currency.isEmpty()){
            Toast.makeText(this, "Currency field is Empty", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (name.isEmpty()){
            Toast.makeText(this, "Name field is Empty", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (categorySelected.equals("Select Category") || categorySelected.equals("No Category Data Found")){
            Toast.makeText(this, "Select a category", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (ticketTypeSelected.equals("Select Transaction Type")){
            Toast.makeText(this, "Select a transaction type", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }

        if (allChecked){
            if (ticketTypeSelected.equals("Credit")){
                ticketTypeSelected = "+";
            }else{
                ticketTypeSelected = "-";
            }

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            long currentDateTime = Calendar.getInstance().getTime().getTime();

            db.insertTicket(name,categorySelected,ticketTypeSelected, Double.parseDouble(currency),currencyType,timeStamp, currentDateTime);

            Intent intent = new Intent(AddTicketActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void getCategories(){
        Cursor res = db.getAllCategoriesData();

        if (res.getCount() == 0){
            Toast.makeText(this, "ERROR : No Category Data Found", Toast.LENGTH_SHORT).show();
            categoriesList.add("No Category Data Found");
            return;
        }

        categoriesList.clear();
        categoriesList.add("Select Category");
        while (res.moveToNext()){
            categoriesList.add(res.getString(1));
        }
    }

    private void getTicketType(){
        ticketTypeList.clear();
        ticketTypeList.add("Select Transaction Type");
        ticketTypeList.add("Credit");
        ticketTypeList.add("Debit");
    }
}
