import { garageService } from "@/utils/request";
import { contentService } from "@/utils/request";

export function getAllGarage(data: any) {
    return garageService({
        url: "garages",
        method: "get",
        params: data,
    });
    //api.get(garageServiceBaseURL + "garages", data);
}
export function getGarageInforById(id: string) {
    return garageService({
        url: "garages/" + id,
        method: "get",
    });

    //api.get(garageServiceBaseURL + "garages/" + id);
}
export async function getAddressInfo(data: any) {
    const dataRes = {
        province: [],
        district: [],
        ward: [],
    };
    const provinceId = data.provinceId;
    const districtId = data.districtId;
    let res: any;
    if (provinceId == 0) {
        res = await contentService({
            url: "commons/areas/?type=PROVINCE",
            method: "get",
        });
        dataRes.province = res.data.data;
    } else {
        res = await contentService({
            url: "commons/areas/?type=DISTRICT&parentId=" + provinceId,
            method: "get",
        });
        // res = await api.get(
        //     provinceBaseURL + "?type=DISTRICT&parentId=" + provinceId,
        // );
        dataRes.district = res.data.data;
        if (districtId != 0) {
            res = await contentService({
                url: "commons/areas/?type=WARD&parentId=" + districtId,
                method: "get",
            });
            // res = await api.get(
            //     provinceBaseURL + "?type=WARD&parentId=" + districtId,
            // );
            dataRes.ward = res.data.data;
        }
    }
    return dataRes;
}
export async function getAddressDetail(data: any) {
    const provinceId = data.provinceId;
    const districtId = data.districtId;
    const wardId = data.wardId;
    const dataRes = {
        province: "",
        district: "",
        ward: "",
    };
    let res: any;
    if (provinceId != "") {
        res = await contentService({
            url: provinceId + "commons/areas/?type=PROVINCE",
            method: "get",
        });
        if (res.status == 200) {
            dataRes.province = res.data.data.name;
        }
    }
    if (districtId != "") {
        res = await contentService({
            url: districtId + "commons/areas/?type=DISTRICT",
            method: "get",
        });

        if (res.status == 200) {
            dataRes.district = res.data.data.name;
        }
    }
    if (wardId != "") {
        res = await contentService({
            url: wardId + "commons/areas/?type=WARD",
            method: "get",
        });

        if (res.status == 200) {
            dataRes.ward = res.data.data.name;
        }
    }
    return dataRes;
}
export function updateGarage(garageId: string, data: any) {
    return garageService({
        url: "garages/" + garageId,
        method: "patch",
        data,
    });
}
export function createGarage(data: any) {
    return garageService({
        url: "garages",
        method: "post",
        data,
    });
}
export function getRescues() {
    return garageService({
        url: "rescues",
        method: "get",
    });
}
export function createGarageOwner(data: any) {
    return garageService({
        url: "garage-owners",
        method: "post",
        data,
    });
}
export function acceptGarages(data: any) {
    return garageService({
        url: "garages/accept",
        method: "post",
        data,
    });
}
export function rejectGarage(data: any) {
    return garageService({
        url: "garages/reject",
        method: "post",
        data,
    });
}

export function getListSubSystem(data: any) {
    return garageService({
        url: "car-sub-systems",
        method: "get",
        params: data,
    });
}

export function getAllRescues(data: any) {
    return garageService({
        url: "rescues",
        method: "get",
        params: data,
    });
}
