package com.example.fragmenttest.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmenttest.Adapters.SettingsFragmentCategoryAdapter;
import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.Interface.ItemClickListenerCategories;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Categories;
import com.example.fragmenttest.objects.Ticket;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private View v;
    private Context mContext;

    private ArrayList<Categories> categoryList;

    private Button startAmouce_edit_btn;
    private Button ccurrencyType_edit_btn;
    private Button category_add_btn;
    private TextView tv_startAmouce;
    private TextView tv_currencyType;

    private RecyclerView category_rv;
    private SettingsFragmentCategoryAdapter categoryAdapter;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private DatabaseHelper db;


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
        Log.e("Testing :", this.getClass().getSimpleName()+" is enable !!!!!!!!!");
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        startAmouce_edit_btn = (Button) v.findViewById(R.id.textView_fragment_settings_startAmouce_btn);
        ccurrencyType_edit_btn = (Button) v.findViewById(R.id.textView_fragment_settings_ccurrencyType_btn);
        category_add_btn = (Button) v.findViewById(R.id.button_fragment_settings_category_add);
        category_rv = (RecyclerView) v.findViewById(R.id.RecyclerView_fragment_settings_category_list);

        tv_startAmouce = (TextView) v.findViewById(R.id.textView_fragment_settings_startAmouce);
        tv_currencyType = (TextView) v.findViewById(R.id.textView_fragment_settings_ccurrencyType);

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        categoryList = new ArrayList<>();
        categoryList = getCategory();

        categoryAdapter = new SettingsFragmentCategoryAdapter(getContext(), categoryList);
        category_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        category_rv.setAdapter(categoryAdapter);

        startAmouce_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Start Amount");
                builder.setCancelable(false);
                View startAmouceView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_dialog_start_amount_update,null,false);
                InitStartAmouceUpdateDialog(startAmouceView);
                builder.setView(startAmouceView);
                dialog = builder.create();
                dialog.show();
            }
        });

        ccurrencyType_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Currency Type");
                builder.setCancelable(false);
                View startAmouceView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_dialog_currency_type_update,null,false);
                InitCurrencyTypeUpdateDialog(startAmouceView);
                builder.setView(startAmouceView);
                dialog = builder.create();
                dialog.show();
            }
        });

        category_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Category");
                builder.setCancelable(false);
                View startAmouceView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_dialog_category_update,null,false);
                InitCategoryUpdateDialog(startAmouceView);
                builder.setView(startAmouceView);
                dialog = builder.create();
                dialog.show();
            }
        });

        // When user click in the category area
        categoryAdapter.setOnItemClickListener(new ItemClickListenerCategories() {
            @Override
            public void OnItemClickCategoryUpdate(int position, Categories categoryData) {

                if (!categoryData.getName().equals("Other")) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Update Category");
                    builder.setCancelable(false);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings_dialog_category_update,null,false);
                    updateSelectedCategory(categoryData, position, view);
                    builder.setView(view);
                    dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(mContext, "Category : '"+categoryData.getName()+"' cannot be updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void OnItemClickCategoryDelete(int position, Categories categoryData) {

                if (!categoryData.getName().equals("Other")) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete Category");
                    builder.setCancelable(false);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_notification, null, false);
                    deleteSelectedCategory(categoryData, position, view);
                    builder.setView(view);
                    dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(mContext, "Category : '"+categoryData.getName()+"' cannot be deleted", Toast.LENGTH_SHORT).show();
                }
            }

        });
        getDefaultSettings();
    }

    //get Default settings
    private void getDefaultSettings(){
        Cursor res = db.getAllSettingsData();

        while (res.moveToNext()){

            if (res.getInt(4) == 1){
                if (res.getInt(0) == 1) {
                    tv_startAmouce.setText("Start Amount : " + String.valueOf(res.getDouble(1)));
                    tv_currencyType.setText("Currency Type : " + res.getString(2));
                    Log.e("getDefaultSettings ", "Default Settings set");
                }
            }
        }
    }

    //get all the categories
    private ArrayList<Categories> getCategory(){
        ArrayList<Categories> list = new ArrayList<>();
        Cursor res = db.getAllCategoriesData();

        while (res.moveToNext()){
            Categories cat = new Categories();
            cat.setId(res.getInt(0));
            cat.setName(res.getString(1));

            list.add(cat);
        }
        return list;
    }

    //Start Smount Insert/Update popup
    private void InitStartAmouceUpdateDialog(View view) {

        final EditText et_update_currency = (EditText) view.findViewById(R.id.fragment_settings_dialog_update_et_update_startAmouce);
        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_ticket);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double currency = Double.parseDouble(et_update_currency.getText().toString());
                db.updateStartAmout(currency);
                tv_startAmouce.setText("Start Amount : "+currency);
                Toast.makeText(mContext, "Start Amount Updated!", Toast.LENGTH_SHORT).show();
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

    //Currency Type Insert/Update popup
    private void InitCurrencyTypeUpdateDialog(View view) {
        final String[] currencyTypeSelected = {""};

        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_currency_type);
        final Button[] btn_cancel = {(Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel_currency_type)};

        Spinner sp_currencyType = (Spinner) view.findViewById(R.id.fragment_settings_dialog_update_sp_currency_type);
        String[] currencyTypeList = {"EUR : €", "USA : $"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, currencyTypeList);
        sp_currencyType.setAdapter(adapter1);
        sp_currencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currencyTypeSelected[0] = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //split the Currency Type Selected to get the symbol
                String[] symbol;
                String separator = ": ";
                symbol = currencyTypeSelected[0].split(separator);

                //Update db
                db.updateCurrencyType(symbol[1]);
                tv_currencyType.setText("Currency Type : "+symbol[1]);
                Toast.makeText(mContext, "Currency Type Updated!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btn_cancel[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //Category Insert popup
    private void InitCategoryUpdateDialog(View view) {

        final EditText et_update_category = (EditText) view.findViewById(R.id.fragment_settings_dialog_update_et_update_category);
        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_category);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel_category);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get user text
                String category = et_update_category.getText().toString();

                categoryAdapter.AddData(category);
                Toast.makeText(mContext, "Category Inserted!", Toast.LENGTH_SHORT).show();
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

    //Update selected category
    private void updateSelectedCategory(final Categories oldCategoryData, final int position, View view){
        final EditText et_update_category = (EditText) view.findViewById(R.id.fragment_settings_dialog_update_et_update_category);
        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_category);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel_category);

        et_update_category.setText(oldCategoryData.getName());
        btn_update.setText("Update");
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get new category data
                Categories newCategoryDate = new Categories();
                newCategoryDate.setId(oldCategoryData.getId());
                newCategoryDate.setName(et_update_category.getText().toString());

//                Log.e("updateSelectedCategory ", "OldCategory : "+oldCategoryData.getName()+" | NewCategory : "+newCategoryDate.getName());
                categoryAdapter.UpdateData(position, newCategoryDate, oldCategoryData);

                Toast.makeText(mContext, "Category Updated!", Toast.LENGTH_SHORT).show();
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

    //Delete selected category
    private void deleteSelectedCategory(final Categories oldCategoryData, final int position, View view) {
        final TextView text_tv = (TextView) view.findViewById(R.id.custom_notification_tv_text);
        Button btn_yes = (Button) view.findViewById(R.id.custom_notification_btn_positive);
        Button btn_no = (Button) view.findViewById(R.id.custom_notification_btn_negative);

        text_tv.setText("You're about to delete "+oldCategoryData.getName()+" category, are you sure ?");
        btn_yes.setText("Yes");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {

                //get default category data
                Categories defaultCategory = new Categories();
                Cursor res = db.getDefaultCategoryData();
                while (res.moveToNext()){
                    defaultCategory.setId(res.getInt(0));
                    defaultCategory.setName(res.getString(1));
                }

                categoryAdapter.DeleteData(position, defaultCategory, oldCategoryData);

                Toast.makeText(mContext, "Category Deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btn_no.setText("Non ");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
