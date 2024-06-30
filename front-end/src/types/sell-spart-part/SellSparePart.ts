type SellSparePart = {
    id: number;
    sellCode?: string;
    customerId?: number;
    customerName?: string;
    customerPhone?: string;
    customerTypeId?: number;
    createdAt?: Date;
    discount?: number;
    tax?: number;
    totalPrice?: number;
    originalPrice?: number;
    deliveryStatus?: number;
    paymentStatus?: number;
    discountType?: number;
    products: SellSparePartProduct[];
    addProducts: SellSparePartProduct[];
    removeProducts: SellSparePartProduct[];
  
};
