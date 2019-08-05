package com.analyst.fragmenttest.pages.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.analyst.fragmenttest.Database.DatabaseHelper;
import com.analyst.fragmenttest.R;
import com.analyst.fragmenttest.pages.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTicketFragment extends Fragment {
    private String TAG = SettingsFragment.class.getSimpleName();

    private Context mContext;
    private DatabaseHelper db;

    private ArrayList<String> categoriesList;
    private ArrayList<String> ticketTypeList;

    private Button btn_cancel;
    private Button btn_save;

    private EditText et_name;
    private EditText et_currency;

    private Spinner sp_category;
    private Spinner sp_ticketType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_ticket, container, false);

        btn_cancel = (Button) v.findViewById(R.id.button_add_ticket_cancel);
        btn_save = (Button) v.findViewById(R.id.button_add_ticket_save);
        et_name = (EditText) v.findViewById(R.id.editText_add_ticket_name);
        et_currency = (EditText) v.findViewById(R.id.editText_add_ticket_currency);
        sp_category = (Spinner) v.findViewById(R.id.spinner_add_ticket_category);
        sp_ticketType = (Spinner) v.findViewById(R.id.spinner_add_ticket_ticketType);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get Categories TicketsEntity
        categoriesList = new ArrayList<>();
        getCategories();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, categoriesList);
        sp_category.setAdapter(adapter1);

        //get Ticket types
        ticketTypeList = new ArrayList<>();
        getTicketType();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, ticketTypeList);
        sp_ticketType.setAdapter(adapter2);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HomeActivity.class);
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
        String currencyType;
        int colorCurrencyTicket = 0;

        boolean allChecked = true;

        if (currency.isEmpty()){
            Toast.makeText(mContext, "Currency field is Empty", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (name.isEmpty()){
            Toast.makeText(mContext, "Name field is Empty", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (categorySelected.equals("Select Category") || categorySelected.equals("No Category Data Found")){
            Toast.makeText(mContext, "Select a category", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }
        if (ticketTypeSelected.equals("Select Transaction Type")){
            Toast.makeText(mContext, "Select a transaction type", Toast.LENGTH_SHORT).show();
            allChecked = false;
        }

        if (allChecked){
            if (ticketTypeSelected.equals("Credit")){
                ticketTypeSelected = "+";
                colorCurrencyTicket = Color.GREEN;//-16711936;
            }else{
                ticketTypeSelected = "-";
                colorCurrencyTicket = Color.RED;
            }

            Cursor res = db.getAllSettingsData();
            res.move(1);
            currencyType = res.getString(2);

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            long currentDateTime = Calendar.getInstance().getTime().getTime();

            db.insertTicket(name,categorySelected,ticketTypeSelected, Double.parseDouble(currency),currencyType,timeStamp, currentDateTime, colorCurrencyTicket);

            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void getCategories(){
        Cursor res = db.getAllCategoriesData();

        if (res.getCount() == 0){
            Toast.makeText(mContext, "ERROR : No Category Data Found", Toast.LENGTH_SHORT).show();
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
