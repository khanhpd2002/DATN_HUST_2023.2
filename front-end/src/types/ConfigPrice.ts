export interface IConfigPrice {
    type: number;
    productId: number;
    productName: string;
    productCode: string;
    garageServiceId: number;
    garageServiceName: string;
    garageServiceCode: string;
    configPrices: IDetailConfigPrice[];
    garageId: number;
}

export interface IDetailConfigPrice {
    id: number;
    modelId: number;
    customerTypeId: number;
    customerTypeName: string;
    productId: number;
    productName: string;
    productCode: string;
    garageServiceId: number;
    garageServiceName: string;
    garageServiceCode: string;
    price: number;
    unit: number;
    garageId: number;
}
