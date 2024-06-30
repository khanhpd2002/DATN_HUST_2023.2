type OutboundTicket = {
    id: number;
    code?: string;
    type?: number;
    distributorId?: number;
    ticketId?: number;
    products?: string;
    note?: string;
    status?: number;
    createdAt?: Date;
    updatedAt?: Date;
}
