package com.analyst.fragmenttest.Interface;

import com.analyst.fragmenttest.objects.Ticket;

public interface ItemClickListenerTicket {

    void OnItemClickTicketUpdate(int position, Ticket ticketData);
    void OnItemClickTicketDelete(int position, Ticket categoryData);
}