import {ICar} from "@/types/Car";

export interface ICustomer {
    id: number;
    customerTypeId?: number | string;
    customerTypeName?: string;
    fullName: string;
    phoneNumber: string;
    address: string;
    driverId: number;
    garageId: number;
    cars: ICar[];
}