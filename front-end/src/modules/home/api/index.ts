import { garageService } from "@/utils/request";
import type { IResponse } from "@/types";

const GARAGE_ID = localStorage.getItem("garageId");

export function getDetailDashboard(query?: any): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}`,
        method: "get",
        params: {
            ...query,
            fromDate: query.fromDate ? query.fromDate : null,
            toDate: query.toDate ? query.toDate : null,
        },
    });
}

export function getTypeTrendService(params?: any): Promise<IResponse> {
    return garageService({
        url: `dashboard/type-service-popular/${GARAGE_ID}`,
        method: "get",
        params,
    });
}

export function getRenevueRepair(params?: any): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/renevue-repair`,
        method: "get",
        params,
    });
}

export function getOutputOrderDistributor(): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/output-order-distributor?quantityLabel=5`,
        method: "get",
    });
}

export function getLowInventoryProducts(): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/low-inventory-products?quantityLabel=5`,
        method: "get",
    });
}

export function getSummaryInventory(query?: any): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/summary-inventory`,
        method: "get",
        params: {
            ...query,
            fromDate: query && query.fromDate ? query.fromDate : null,
            toDate: query && query.toDate ? query.toDate : null,
        },
    });
}

export function getMostProductRefundeds(): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/most-product-refunded`,
        method: "get",
    });
}

export function getLongTermInventoryProducts(params?: any): Promise<IResponse> {
    return garageService({
        url: `dashboard/${GARAGE_ID}/long-term-inventory-products`,
        method: "get",
        params,
    });
}
