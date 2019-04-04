package com.example.fragmenttest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Ticket;

public class SettingsFragment extends Fragment {

    private View v;
    private Context mContext;

    private Button startAmouce_edit_btn;
    private Button ccurrencyType_edit_btn;
    private Button category_edit_btn;

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
        category_edit_btn = (Button) v.findViewById(R.id.textView_fragment_settings_category_btn);

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        category_edit_btn.setOnClickListener(new View.OnClickListener() {
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

        final EditText et_update_currency_type = (EditText) view.findViewById(R.id.fragment_settings_dialog_update_et_update_currency_type);
        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_currency_type);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel_currency_type);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currencyType = et_update_currency_type.getText().toString();
                db.updateCurrencyType(currencyType);
                Toast.makeText(mContext, "Currency Type Updated!", Toast.LENGTH_SHORT).show();
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
    private void InitCategoryUpdateDialog(View view) {

        final EditText et_update_category = (EditText) view.findViewById(R.id.fragment_settings_dialog_update_et_update_category);
        RecyclerView category_rv = (RecyclerView) view.findViewById(R.id.fragment_settings_dialog_update_recylerView_category_list);
        Button btn_update = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_category);
        Button btn_cancel = (Button) view.findViewById(R.id.fragment_settings_dialog_update_btn_update_cancel_category);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = et_update_category.getText().toString();
                db.insertCategories(category);
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
}
