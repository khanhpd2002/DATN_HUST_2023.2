import { IResponse } from "@/types";
import { garageService } from "@/utils/request";
import { ecommerceService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");

export function getCustomersByGarageId(data: any): Promise<IResponse> {
    return garageService({
        url: `customers/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function getGarageCustomerInfo(customerId: number): Promise<IResponse> {
    return garageService({
        url: `customers/${GARAGE_ID}/info/${customerId}`,
        method: "get",
        // params: data,
    });
}

export function getGarageCustomerType(data: any): Promise<IResponse> {
    return garageService({
        url: `/customer-types/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function addNewGarageCustomer(data: any): Promise<IResponse> {
    return garageService({
        url: `/customers/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function updateGarageCustomerById(data: any, customerId: number) {
    return garageService({
        url: `/customers/${GARAGE_ID}/update/${customerId}`,
        method: "patch",
        data,
    });
}

export function updateCustomerType(data: any, typeId: number) {
    return garageService({
        url: `/customer-types/${GARAGE_ID}/update/${typeId}`,
        method: "patch",
        data,
    });
}

export function addNewCustomerType(data: any) {
    return garageService({
        url: `/customer-types/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function addNewGarageCar(data: any) {
    return garageService({
        url: `/cars/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function getGarageCars(data: any) {
    return garageService({
        url: `/cars/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function updateGarageCar(data: any, carId: number) {
    return garageService({
        url: `/cars/${GARAGE_ID}/update/${carId}`,
        method: "patch",
        data,
    });
}

export function getCarFeatures(params: any) {
    return ecommerceService({
        url: `/brand-data`,
        method: "get",
        params,
    });
}
