import { IResponse } from "@/types";
import { garageService } from "@/utils/request";
import { ecommerceService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");
const inventoryId = localStorage.getItem("inventoryId");

export function createAccessary(data: any): Promise<IResponse> {
    return garageService({
        url: `/products/${inventoryId}`,
        method: "post",
        data,
    });
}

export function getAccessaries(data: any): Promise<IResponse> {
    return garageService({
        url: `/products/${inventoryId}`,
        method: "get",
        params: { ...data, type: 1 },
    });
}

export function getParentProduct(data: any): Promise<IResponse> {
    return garageService({
        url: `/products/${inventoryId}/parent`,
        method: "get",
        params: { ...data },
    });
}

export function getAccessaryById(id: number): Promise<IResponse> {
    return garageService({
        url: `/products/${GARAGE_ID}/info/${id}`,
        method: "get",
    });
}

export function updateAccessaryrById(
    data: any,
    id: number | undefined
): Promise<IResponse> {
    return garageService({
        url: `/products/${inventoryId}/update/${id}`,
        method: "patch",
        data,
    });
}

export function getAccessaryFeatures(params: any): Promise<IResponse> {
    return ecommerceService({
        url: `/brand-data`,
        method: "get",
        params,
    });
}

export function getProductByCode(code: string): Promise<IResponse> {
    return garageService({
        url: `/products/${inventoryId}/parent-info-by-code/${code}`,
        method: "get",
    });
}
