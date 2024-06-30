type OutboundProduct = {
    id: number;
    productId: number;
    outboundTicketId: number;
    unit?: string;
    distributorId?: number;
    requestQuantity?: number;
    exportQuantity?: number;
    note?: string;
    status: number;
}
