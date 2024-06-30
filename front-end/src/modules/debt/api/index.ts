import { garageService } from "@/utils/request";
const garageId = localStorage.getItem("garageId");

export function getOrderDistributor(query: any) {
    query.garageId = garageId;
    return garageService({
        method: "get",
        url: "orders-distributor",
        params: query,
    });
}

export function updatePaymentStatusServiceTicket(
    id: string,
    status: number
): Promise<any> {
    return garageService({
        url: "repair-orders/" + id + "/change-payment-status",
        method: "patch",
        params: { paymentStatus: status },
    });
}

export function updateSellOrderPaymentStatus(
    id: string | number | null,
    status: number | undefined
): Promise<any> {
    return garageService({
        url: "sell-spare-parts/" + id + "/change-payment-status",
        method: "patch",
        params: { paymentStatus: status },
    });
}

export function getOrderDistributorControllerList(query: any): Promise<any> {
    return garageService({
        url: "orders-distributor",
        method: "get",
        params: { ...query, garageId: garageId },
    });
}

export function getDetailOrderDistributorController(
    id: string | number
): Promise<any> {
    return garageService({
        url: "orders-distributor/info/" + id,
        method: "get",
    });
}

export function updateOrderDistributorController(
    id: string | number | null,
    data: any
): Promise<any> {
    return garageService({
        url: "orders-distributor/" + garageId + "/update/" + id,
        method: "patch",
        data: data,
    });
}

export function updatePaymentStatusDistributorController(
    id: string | number,
    status: number
): Promise<any> {
    return garageService({
        url: "orders-distributor/" + id + "/change-payment-status",
        method: "patch",
        params: {
            paymentStatus: status,
        },
    });
}
