package com.example.fragmenttest.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragmenttest.Adapters.ListFragmentAdapter;
import com.example.fragmenttest.AddTicketActivity;
import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.Interface.ItemClickListenerTicket;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ListFragment extends Fragment {

    private View v;
    private Context mContext;
    private String TAG = ListFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ListFragmentAdapter listFragmentAdapter;
    private ArrayList<Ticket> ticketArrayList;
    private ArrayList<Ticket> filteredTicketArrayList;
    private ProgressDialog progressDialog;

    private String timeStamp;

    private ArrayList<String> categoriesList;
    ArrayAdapter<String> adapter1;
    private Button btn_addTicket;

    private EditText et_filterText;
    private String filterText = "";

    private Spinner categoryFilter;
    private String categoryFilterText = "";

    AlertDialog.Builder builder;
    AlertDialog dialog;

    private DatabaseHelper db;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current date and time and store it in string timeStemp................................
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        ticketArrayList = new ArrayList<>();
        db = new DatabaseHelper(getContext());


        getTickets();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(ListFragment.class.getSimpleName()+": ", "Started");
        v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recylerView_ticket_list);
        btn_addTicket = (Button) v.findViewById(R.id.button_add_ticket);
        categoryFilter = (Spinner) v.findViewById(R.id.spinner_category_filter);
        et_filterText = (EditText) v.findViewById(R.id.editText_searchText);

        adapter1 = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, categoriesList);


        btn_addTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTicket();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get Categories from db
        categoriesList = new ArrayList<>();
        getCategories();
        adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
        categoryFilter.setAdapter(adapter1);


        categoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                categoryFilterText = adapterView.getSelectedItem().toString();

                //Filter By Category
                if (adapterView.getSelectedItem().toString().equals("Category...")) {
                    categoryFilterText = "";
                    filteredTicketArrayList = filter(ticketArrayList, categoryFilterText, filterText);
                    Log.e("Category Select ", "Get all category data");
                }else{
                    filteredTicketArrayList = filter(ticketArrayList, categoryFilterText, filterText);
                    Log.e("Category Select ", adapterView.getSelectedItem().toString());
                }

                listFragmentAdapter = new ListFragmentAdapter(getContext(), filteredTicketArrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(listFragmentAdapter);
                listFragmentAdapter.notifyDataSetChanged();

                //Update Ticket
                listFragmentAdapter.setOnItemClickListener(new ItemClickListenerTicket() {
                    @Override
                    public void OnItemClickTicket(int position, Ticket ticket) {

                        builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Update User Info");
                        builder.setCancelable(false);
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_dialog_update,null,false);
                        InitUpdateDialog(ticket,position,view);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterText = et_filterText.getText().toString();
                filteredTicketArrayList = filter(ticketArrayList, categoryFilterText, filterText);

                listFragmentAdapter = new ListFragmentAdapter(getContext(), filteredTicketArrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(listFragmentAdapter);
                listFragmentAdapter.notifyDataSetChanged();

                //Update Ticket
                listFragmentAdapter.setOnItemClickListener(new ItemClickListenerTicket() {
                    @Override
                    public void OnItemClickTicket(int position, Ticket ticket) {

                        builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Update Ticket Info");
                        builder.setCancelable(false);
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_dialog_update,null,false);
                        InitUpdateDialog(ticket, position, view);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });

    }

    private void settings(){
        Cursor res;

        Log.e(TAG, "Settings...");
        //check if the default is set
        res = db.getAllSettingsData();
        while (res.moveToNext()){
            if (res.getCount() == 0){
                db.insertDefaultSettingsData();
                Log.e(TAG, "Default Settings settings applied");
            }else {
                Log.e(TAG, "Default Settings settings already applied");
            }
        }



        res = db.getAllCategoriesData();
        while (res.moveToNext()){
            //if (res.getCount() == 0){

                Log.e(TAG, "Default Category settings status: id = "+res.getInt(0));
                Log.e(TAG, "Default Category settings status: name = "+res.getInt(1));
                Log.e(TAG, "Default Category settings status: default = "+res.getInt(2));

                db.insertDefaultCategory();
                Log.e(TAG, "Default Category settings applied");
            //}else {
                Log.e(TAG, "Default Category settings already applied");
            //}
        }

        Log.e(TAG, "Settings... finished");
    }

    private void getCategories(){
        Cursor res = db.getAllCategoriesData();

        categoriesList.clear();
        categoriesList.add("Category...");
        while (res.moveToNext()){
            categoriesList.add(res.getString(1));
        }
    }

    private void getTickets(){
        Cursor res = db.getAllTicketData();

        ticketArrayList.clear();
        if (res.getCount() == 0){
            Toast.makeText(getContext(), "ERROR : No Ticket(s) Found", Toast.LENGTH_SHORT).show();
            ticketArrayList.add(new Ticket(1,"Welcome","You have no ticket(s), press the add button to get started !!!","-",10.26,"€",timeStamp));
            return;
        }

        while (res.moveToNext()){
            Ticket ticket = new Ticket();
            ticket.setId(res.getInt(0));
            ticket.setName(res.getString(1));
            ticket.setCategory(res.getString(2));
            ticket.setTicketType(res.getString(3));
            ticket.setCurrency(res.getDouble(4));
            ticket.setCurrencyType(res.getString(5));
            ticket.setDate(res.getString(6));
            ticketArrayList.add(ticket);
        }
    }

    //Add new Tickets
    private void openAddTicket(){
        Intent intent = new Intent(ListFragment.this.getContext(), AddTicketActivity.class);
        startActivity(intent);
    }

    //Ticket Update popup
    private void InitUpdateDialog(final Ticket ticket, final int position, View view) {

        final EditText et_update_name = (EditText) view.findViewById(R.id.fragment_list_dialog_update_et_update_name);
        final EditText et_update_currency = (EditText) view.findViewById(R.id.fragment_list_dialog_update_et_update_currency);
        Spinner sp_update_category = (Spinner) view.findViewById(R.id.fragment_list_dialog_update_sp_update_category);
        Button btn_update = (Button) view.findViewById(R.id.fragment_list_dialog_update_btn_update_ticket);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_list_dialog_update_btn_update_cancel);

        et_update_name.setText(ticket.getName());
        et_update_currency.setText(String.valueOf(ticket.getCurrency()));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
        sp_update_category.setAdapter(adapter);

        for (int i=0; i< categoriesList.size(); i++){
            if (ticket.getCategory().equals(categoriesList.get(i))){
                sp_update_category.setSelection(i);
            }
        }

        sp_update_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ticket.setCategory(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name;
                double currency;

                name = et_update_name.getText().toString();
                currency = Double.parseDouble(et_update_currency.getText().toString());

                Ticket ticket1;

                ticket1 = ticket;
                ticket1.setName(name);
                ticket1.setCategory(ticket.getCategory());
                ticket1.setCurrency(currency);

                listFragmentAdapter.UpdateData(position,ticket1);
                Toast.makeText(getContext(),"User Updated..",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private ArrayList<Ticket> filter(ArrayList<Ticket> list, String category, String text){
        ArrayList<Ticket> filteredTicketArrayList = new ArrayList<>();

        //if both filter arguments are empty
        if (category.equals("") && text.equals("")){
            filteredTicketArrayList = list;
        }else{
            for (int i=0; i<list.size(); i++){

                //Filter by category
                if(!category.equals("") && text.equals("")){
                    if (list.get(i).getCategory().equals(category)){
                        filteredTicketArrayList.add(list.get(i));
                        Log.e("Find by Category: "+i+" ==> ", "Category |==> "+list.get(i).getCategory());
                    }
                }

                //Filter by text
                if (!text.equals("") && category.equals("")){
                    if (list.get(i).getName().contains(text) || list.get(i).getDate().contains(text)){
                        filteredTicketArrayList.add(list.get(i));
                        Log.e("Find by String : "+i+" ==> ", "String search: "+text+" |==> Name : "+list.get(i).getName()+" | Date :"+list.get(i).getDate());
                    }
                }

                //Filter by category and text
                if (!category.equals("") && !text.equals("")){
                    if (list.get(i).getCategory().equals(category) && (list.get(i).getName().contains(text) || list.get(i).getDate().contains(text))){
                        filteredTicketArrayList.add(list.get(i));
                        Log.e("Find by Category & Text", " "+i+" ==>\n" +
                                "Category |==> "+list.get(i).getCategory()+"\n" +
                                "Name |==> "+list.get(i).getName()+"\n" +
                                "Date |==> "+list.get(i).getDate());
                    }
                }
            }
        }
        return filteredTicketArrayList;
    }

}
