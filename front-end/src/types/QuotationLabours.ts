import { ISelectOption } from ".";

export interface IQuotationLabours {

    garageServiceTypeId: number | string;
    garageServiceId: number | string;
    employeeId: number | string;
    unitPrice: number;
    quantity: number;
    discount: number;
    tax: number;
    price: number;
    garageServiceName: ISelectOption[];

}