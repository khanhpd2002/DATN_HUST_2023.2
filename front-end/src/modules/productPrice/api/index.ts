import { garageService } from "@/utils/request";
import { IConfigPrice, IResponse } from "@/types";

const GARAGE_ID = localStorage.getItem("garageId");

export function getListPrices(query?: any): Promise<IResponse> {
    return garageService({
        url: `config-prices/${GARAGE_ID}`,
        method: "get",
        params: query,
    });
}

export function createPrice(payload: any): Promise<IResponse> {
    return garageService({
        url: `config-prices/${GARAGE_ID}`,
        method: "post",
        data: payload,
    });
}

export function updatePrice(
    payload: IConfigPrice,
    modelId: number,
    type: number
): Promise<IResponse> {
    return garageService({
        url: `config-prices/${GARAGE_ID}/update/${modelId}`,
        method: "patch",
        data: payload,
        params: {
            type: type
        }
    });
}

export function detailPrice(priceId: number): Promise<IResponse> {
    return garageService({
        url: `config-prices/${GARAGE_ID}/info/${priceId}`,
        method: "get",
    });
}

export function detailPriceByCustomerTypeAndProductAndService(
    customerTypeId: number | string,
    modelId: number | string,
    type: number | string,
): Promise<any> {
    return garageService({
        url: `config-prices/${customerTypeId}/prices`,
        method: "get",
        params: {
            modelId: modelId,
            type: type
        }
    });
}

export function getAllCustomerGroup() {
    return garageService({
        url: "customer-types/" + GARAGE_ID,
        params: {
            pageSize: 10000,
            pageNumber: 1,
        },
        method: "get",
    });
}
export function getAllProduct() {
    return garageService({
        url: "products",
        params: {
            pageSize: 10000,
            pageNumber: 1,
        },
        data: {
            garageId: GARAGE_ID,
        },
        method: "get",
    });
}
