export interface IDistributor {
    id: number;
    code: string;
    name: string;
    provinceId: number | undefined;
    provinceName: string;
    districtId: number | undefined;
    districtName: string;
    wardId: number | undefined;
    wardName: string;
    address: string;
    contactName: string;
    contactPhone: string;
    garageId?: number;
    isDelete?: number;
}
