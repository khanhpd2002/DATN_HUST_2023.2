import { IResponse } from "@/types";
import { garageService } from "@/utils/request";
import { ecommerceService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");

export function createCar(data: any): Promise<IResponse> {
    return garageService({
        url: `/cars/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function getCars(data: any): Promise<IResponse> {
    return garageService({
        url: `/cars/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function getCarById(carId: number): Promise<IResponse> {
    return garageService({
        url: `/cars/${GARAGE_ID}/info/${carId}`,
        method: "get",
    });
}

export function updateCarById(data: any, carId: number): Promise<IResponse> {
    return garageService({
        url: `/cars/${GARAGE_ID}/update/${carId}`,
        method: "patch",
        data,
    });
}

export function getCarFeatures(params: any): Promise<IResponse> {
    return ecommerceService({
        url: `/brand-data`,
        method: "get",
        params,
    });
}