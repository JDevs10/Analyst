package com.example.fragmenttest.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

import com.example.fragmenttest.Adapters.ListFragmentAdapter;
import com.example.fragmenttest.AddTicketActivity;
import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.HomeActivity;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View v;
    private Context mContext;
    private RecyclerView recyclerView;
    private ListFragmentAdapter listFragmentAdapter;
    private ArrayList<Ticket> ticketArrayList;
    private ProgressDialog progressDialog;

    private String timeStamp;

    private ArrayList<String> categoriesList;
    ArrayAdapter<String> adapter1;
    private Button btn_addTicket;
    private EditText filterText;

    private Spinner categoryFilter;

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


        ticketArrayList.add(new Ticket(1,"Food Delux","Food","-",26.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Max","Food","-",6.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Me","House","-",46.10,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Hello","Food","-",999.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Delux","House","-",2699.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Max","Food","-",99995.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Me","Food","-",46.10,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Hello","Electronics","-",2.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Delux","Food","-",26.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Max","Food","-",6.50,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Me","Electronics","-",46.10,"€",timeStamp));
        ticketArrayList.add(new Ticket(1,"Food Hello","Bank","-",2.50,"€",timeStamp));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(ListFragment.class.getSimpleName()+": ", "Started");
        v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recylerView_ticket_list);
        btn_addTicket = (Button) v.findViewById(R.id.button_add_ticket);
        categoryFilter = (Spinner) v.findViewById(R.id.spinner_category_filter);
        filterText = (EditText) v.findViewById(R.id.editText_searchText);

        listFragmentAdapter = new ListFragmentAdapter(getContext(), ticketArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listFragmentAdapter);

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
        categoryFilter.setOnItemSelectedListener(this);
        categoryFilter.setAdapter(adapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Filter By Category
        if (!adapterView.getSelectedItem().toString().equals("Filter by category ???")) {
            // i am resetting the initial list of tickets with the filter one which is not what i wanted !!!!!!!!!!!!
            ticketArrayList = filterByCategory(adapterView.getSelectedItem().toString());
            Log.e("Category Select ", adapterView.getSelectedItem().toString());

        }
        Log.e("Refresh ", " onItemSelected");
        listFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getCategories(){
        Cursor res = db.getAllCategoriesData();

        categoriesList.clear();
        categoriesList.add("Filter by category ???");
        while (res.moveToNext()){
            categoriesList.add(res.getString(1));
        }
        categoriesList.add("Food");
        categoriesList.add("House");
    }

    //Add new Tickets
    private void openAddTicket(){
        Intent intent = new Intent(ListFragment.this.getContext(), AddTicketActivity.class);
        startActivity(intent);
    }

    //Filter by category
    private ArrayList<Ticket> filterByCategory(String categorySelected){
        ArrayList<Ticket> filteredTicketArrayList = new ArrayList<>();

        for (int i=0; i<ticketArrayList.size(); i++){
            if(ticketArrayList.get(i).getCategory().equals(categorySelected)){
                filteredTicketArrayList.add(ticketArrayList.get(i));
                Log.e("Find ", ticketArrayList.get(i).getCategory());
            }
        }

        return filteredTicketArrayList;
    }


}
