import { IResponse } from "@/types";
import { garageService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");

export function getCustomers(data: any): Promise<IResponse> {
    return garageService({
        url: `customers/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function getCustomerById(
    customerId: number | undefined
): Promise<IResponse> {
    return garageService({
        url: `customers/${GARAGE_ID}/info/${customerId}`,
        method: "get",
    });
}

export function createCustomer(data: any): Promise<IResponse> {
    return garageService({
        url: `/customers/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function updateCustomerById(
    data: any,
    customerId: number
): Promise<IResponse> {
    return garageService({
        url: `/customers/${GARAGE_ID}/update/${customerId}`,
        method: "patch",
        data,
    });
}
