package com.example.fragmenttest.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fragmenttest.Database.DatabaseHelper;
import com.example.fragmenttest.Interface.ItemClickListenerTicket;
import com.example.fragmenttest.R;
import com.example.fragmenttest.objects.Ticket;

import java.util.ArrayList;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Ticket> ticketList;
    private ItemClickListenerTicket itemClickListenerTicket;

    public ListFragmentAdapter(Context mContext, ArrayList<Ticket> data){
        this.context = mContext;
        this.ticketList = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_ticket, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = myViewHolder.getAdapterPosition();
        final Ticket ticketData = ticketList.get(i);

        myViewHolder.tv_name.setText(ticketList.get(i).getName());
        myViewHolder.tv_category.setText(ticketList.get(i).getCategory());
        myViewHolder.tv_ticketType.setText(ticketList.get(i).getTicketType()+" ");
        myViewHolder.tv_currency.setText(String.valueOf(ticketList.get(i).getCurrency()));
        myViewHolder.tv_currency.setTextColor(ticketList.get(i).getCurrencyColor());
        myViewHolder.tv_currencyType.setText(ticketList.get(i).getCurrencyType());
        myViewHolder.tv_date.setText(String.valueOf(ticketList.get(i).getDate()));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerTicket.OnItemClickTicketUpdate(position, ticketData);
            }
        });

        myViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListenerTicket.OnItemClickTicketDelete(position, ticketData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_category, tv_ticketType, tv_currency, tv_currencyType, tv_date, tv_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.custom_ticket_title);
            tv_category = (TextView) itemView.findViewById(R.id.custom_ticket_category);
            tv_ticketType = (TextView) itemView.findViewById(R.id.custom_ticket_type);
            tv_currency = (TextView) itemView.findViewById(R.id.custom_ticket_currency);
            tv_currencyType = (TextView) itemView.findViewById(R.id.custom_ticket_currency_type);
            tv_date = (TextView) itemView.findViewById(R.id.custom_ticket_date);
            tv_delete = (TextView) itemView.findViewById(R.id.custom_ticket_tv_delete_item);
        }
    }

    public void setOnItemClickListener(ItemClickListenerTicket itemClickListenerTicket){
        this.itemClickListenerTicket = itemClickListenerTicket;
    }

    public void UpdateData(int position, Ticket ticketData){
        DatabaseHelper db = new DatabaseHelper(context);
        db.updateTicketData(ticketData);

        // set changes to the list
        ticketList.remove(position);
        ticketList.add(ticketData);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    public void DeleteData(int position, Ticket ticketData){
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteTicketData(ticketData.getId());

        // set changes to the list
        ticketList.remove(position);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
