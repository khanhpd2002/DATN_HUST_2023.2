import { garageService } from "@/utils/request";
import { contentService } from "@/utils/request";
const GARAGE_ID = localStorage.getItem("garageId");

import type { IQuery, IDistributor, IResponse } from "@/types";

export function getListDistributors(query?: IQuery): Promise<IResponse> {
    return garageService({
        url: `distributors/${GARAGE_ID}`,
        method: "get",
        params: query,
    });
}

export function createDistributor(payload: IDistributor): Promise<IResponse> {
    return garageService({
        url: `distributors/${GARAGE_ID}`,
        method: "post",
        data: payload,
    });
}

export function updateDistributor(
    payload: IDistributor,
    distributorId: number
): Promise<IResponse> {
    return garageService({
        url: `distributors/${GARAGE_ID}/update/${distributorId}`,
        method: "patch",
        data: payload,
    });
}

export function deleteDistributor(distributorId: number): Promise<IResponse> {
    return garageService({
        url: `distributors/${GARAGE_ID}/delete/${distributorId}`,
        method: "delete",
    });
}

export function getProvinceList() {
    return contentService({
        url: "commons/areas/?type=PROVINCE",
        method: "get",
    });
}

export function getDistrictList(provinceId: number) {
    return contentService({
        url: "commons/areas/?type=DISTRICT&parentId=" + provinceId,
        method: "get",
    });
}

export function getWardList(districtId: number) {
    return contentService({
        url: "commons/areas/?type=WARD&parentId=" + districtId,
        method: "get",
    });
}
