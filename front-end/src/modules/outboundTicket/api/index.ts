import {garageService} from "@/utils/request";
import type {IQuery, IResponse,} from "@/types";

const garageId = localStorage.getItem("garageId");


export function findAllOutboundTicket(query: IQuery): Promise<IResponse> {
    return garageService({
        url: `outbounds/${garageId}`,
        method: "get",
        params: { ...query },
    });
}

export function getDetailOutboundTicket(outboundTicketId: number): Promise<IResponse> {
    return garageService({
        url: `outbounds/${garageId}/info/${outboundTicketId}`,
        method: "get",
    });
}

export function confirmActionOutbound(outboundTicketId: number, payload: OutboundTicket): Promise<IResponse> {
    return garageService({
        url: `outbounds/${garageId}/confirm/${outboundTicketId}`,
        method: "patch",
        data: { ...payload }
    });
}

export function findAllRepairOrder(query?: any): Promise<any> {
    return garageService({
        url: `repair-orders/${garageId}`,
        method: "get",
        params: { ...query },
    });
}
