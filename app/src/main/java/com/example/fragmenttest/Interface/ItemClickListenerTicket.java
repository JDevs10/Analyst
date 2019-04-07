package com.example.fragmenttest.Interface;

import com.example.fragmenttest.objects.Ticket;

public interface ItemClickListenerTicket {

    void OnItemClickTicketUpdate(int position, Ticket ticketData);
    void OnItemClickTicketDelete(int position, Ticket categoryData);
}