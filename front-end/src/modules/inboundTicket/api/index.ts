import { garageService } from "@/utils/request";
import type { IQuery, IResponse } from "@/types";

const garageId = localStorage.getItem("garageId");

export function createInboundTicket(
    payload: InboundTicket
): Promise<IResponse> {
    return garageService({
        url: `inbounds/${garageId}`,
        method: "post",
        data: { ...payload },
    });
}

export function findAllInboundTicket(query: IQuery): Promise<IResponse> {

    return garageService({
        url: `inbounds/${garageId}`,
        method: "get",
        params: { ...query },
    });
}

export function getDetailInboundTicket(
    outboundTicketId: number
): Promise<IResponse> {
    return garageService({
        url: `inbounds/${garageId}/info/${outboundTicketId}`,
        method: "get",
    });
}

export function confirmActionInbound(
    inboundTicketId: number,
    payload: InboundTicket
): Promise<IResponse> {
    return garageService({
        url: `inbounds/${garageId}/confirm/${inboundTicketId}`,
        method: "patch",
        data: { ...payload },
    });
}

export function findAllRepairOrder(query?: any): Promise<any> {
    return garageService({
        url: `repair-orders/${garageId}`,
        method: "get",
        params: { ...query },
    });
}
