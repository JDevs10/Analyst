package com.example.fragmenttest.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.Interface.ItemClickListenerCategories;
import com.example.fragmenttest.Interface.ItemClickListenerTicket;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Categories;

import java.util.ArrayList;

public class SettingsFragmentCategoryAdapter extends RecyclerView.Adapter<SettingsFragmentCategoryAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Categories> categoryList;
    private ItemClickListenerCategories itemClickListenerCategories;

    public SettingsFragmentCategoryAdapter(Context mContext, ArrayList<Categories> data){
        this.context = mContext;
        this.categoryList = data;
    }

    @NonNull
    @Override
    public SettingsFragmentCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_category_settings, viewGroup, false);
        return new SettingsFragmentCategoryAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsFragmentCategoryAdapter.MyViewHolder myViewHolder,  int i) {

        final int position = myViewHolder.getAdapterPosition();
        final Categories categoryData = categoryList.get(i);

        myViewHolder.tv_name.setText(categoryList.get(i).getName());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerCategories.OnItemClickCategoryUpdate(position, categoryData);
            }
        });

        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerCategories.OnItemClickCategoryDelete(position, categoryData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_edit, tv_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.custom_category_tv_title_item);
            tv_delete = (TextView) itemView.findViewById(R.id.custom_category_tv_delete_item);
        }
    }

    public void setOnItemClickListener(ItemClickListenerCategories itemClickListenerCategories){
        this.itemClickListenerCategories = itemClickListenerCategories;
    }

    public void AddData(String text){
        DatabaseHelper db = new DatabaseHelper(context);

        //add the new categorry
        db.insertCategories(text);

        Categories newCategoryData = new Categories();

        //get the new category from db
        Cursor res = db.getAllCategoriesData();
        while (res.moveToNext()){
            if (res.getString(1).equals(text)){
                newCategoryData.setId(res.getInt(0));
                newCategoryData.setName(res.getString(1));
            }
        }

        //set changes to the list
        categoryList.add(newCategoryData);
        notifyDataSetChanged();
    }

    public void UpdateData(int position, Categories newCategoryData, Categories oldCategoryData){

        //update db data
        DatabaseHelper db = new DatabaseHelper(context);
        db.insertCategories(newCategoryData.getName());

        Cursor res = db.getAllTicketData();
        while (res.moveToNext()){
            if (res.getString(1).equals(newCategoryData.getName())){
                newCategoryData.setId(res.getInt(0));
            }
        }

        //update all the tickets with the old category to the new one
        res = db.getAllTicketData();
        while (res.moveToNext()){
            if (res.getString(2).equals(oldCategoryData.getName())){
                db.updateTicketCategoryData(res.getInt(0), newCategoryData.getName());
            }
        }

        //delete the oldCategory
        db.deleteCategoriesData(oldCategoryData.getId());

        //set changes to the list
        categoryList.remove(position);
        categoryList.add(newCategoryData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void DeleteData(int position, Categories newCategoryData, Categories oldCategoryData){
        DatabaseHelper db = new DatabaseHelper(context);

        //replace all tickets category from the selected category to "Other"
        Cursor res = db.getAllTicketData();
        while (res.moveToNext()){
            if (res.getString(2).equals(oldCategoryData.getName())){
                db.updateTicketCategoryData(res.getInt(0), newCategoryData.getName());
            }
        }

        //Now delete the selected category
        db.deleteCategoriesData(oldCategoryData.getId());

        //set changes to the list
        categoryList.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
