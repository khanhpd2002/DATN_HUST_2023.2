export type OrderDistributor = {
    id: number;
    orderCode?: string;
    deliveryStatus?: number;
    paymentStatus?: number;
    garageId?: number;
    distributorId?: number;
    distributorCode?: string;
    distributorName?: string;
    distributorPhone?: string;
    deliveryType?: string | number;
    discount?: number;
    discountType?: number;
    tax?: number;
    totalPrice?: number;
    originalPrice?: number;
    inventoryId?: number;
    products?: OrderDistributorProduct[];
    addProducts?: OrderDistributorProduct[];
    removeProducts?: OrderDistributorProduct[];
};

export type OrderDistributorProduct = {
    id: number;
    orderDistributorId: number;
    productId: number;
    unit: string;
    quantity: number;
    unitPrice: number;
    discount: number;
    tax: number;
    originalPrice: number;
    price: number;
    status: string | number;
    inboundProductId: string | number;
    instanceKey: number
};

// export type OrderDistributorResponse = {
//     distributorId: number | string;
//     distributorCode?: string;
//     distributorName?: string;
//     distributorPhone?: string;
//     deliveryType?: string;
//     discount?: number;
//     tax?: number;
//     totalPrice?: number;
//     inventoryId?: number;
//     products?: OrderDistributorProduct[];
//     addProducts?: OrderDistributorProduct[];
//     removeProducts?: OrderDistributorProduct[];
// };
