import { garageService } from "@/utils/request";

export function getListProducts(query?: any): Promise<any> {
    return garageService({
        url: `products`,
        method: "get",
        params: query,
    });
}

export function getListProductsByGarageId(
    garageId: number | string,
    query?: any
): Promise<any> {
    return garageService({
        url: `products/${garageId}`,
        method: "get",
        params: query,
    });
}

export function getListInventories(query?: any): Promise<any> {
    return garageService({
        url: `inventories/348`,
        method: "get",
        params: query,
    });
}

export function createProduct(inventoryId: number, payload: any): Promise<any> {
    return garageService({
        url: `products/${inventoryId}`,
        method: "post",
        data: payload,
    });
}

export function updateProduct(
    inventoryId: number,
    payload: any,
    productId: number
): Promise<any> {
    return garageService({
        url: `products/${inventoryId}/update/${productId}`,
        method: "patch",
        data: payload,
    });
}

export function detailProduct(
    inventoryId: number,
    productId: number
): Promise<any> {
    return garageService({
        url: `products/${inventoryId}/info/${productId}`,
        method: "get",
    });
}

export function getListDistributors(
    garageId?: number,
    query?: any
): Promise<any> {
    return garageService({
        url: `distributors/${garageId || "348"}`,
        method: "get",
        params: query,
    });
}
