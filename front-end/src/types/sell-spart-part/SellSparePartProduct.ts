type SellSparePartProduct = {
    productId: number;
    status: number;
    quantity: number;
    unit: string;
    unitPrice: number;
    originalPrice: number;
    discount: number;
    tax: number;
    price: number;
    outboundProductId: number | null;
    disable?: boolean;
    instanceKey: number
};
