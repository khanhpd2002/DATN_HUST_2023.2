import { garageService } from "@/utils/request";

const garageId = localStorage.getItem("garageId");
export function getListOrder(query?: any): Promise<any> {
    return garageService({
        url: `repair-orders/${garageId}`,
        method: "get",
        params: { ...query },
    });
}

export function createOrder(data: any): Promise<any> {
    return garageService({
        url: "repair-orders/garage",
        method: "post",
        data: { ...data, garageId: garageId },
    });
}

export function getDetailOrder(id: string): Promise<any> {
    return garageService({
        url: "repair-orders/" + garageId + "/info/" + id,
        method: "get",
    });
}

export function updateOrder(id: string, data: any): Promise<any> {
    return garageService({
        url: "repair-orders/garage/" + garageId + "/update/" + id,
        method: "patch",
        data: { ...data, garageId: garageId },
    });
}
