export interface IAccessary {
    id?: number;
    type: number;
    code: string;
    name: string;
    distributorId: number;
    distributorCode: string;
    distributorName: string
    purchasePrice: number;
    sparePartType: number;
    quantity: number;
    unit: string;
    inventoryId?:number;
    productCompatibilities: [];
}
