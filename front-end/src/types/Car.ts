import {ICarRepairHistory} from "@/types/CarRepairHistory";

export interface ICar {
    id: number;
    carName: string;
    carBrandId: number | undefined;
    carBrandName: string;
    carModelId: number | undefined;
    carModelName: string;
    carYearId: number | undefined;
    carYearName: string;
    carVersionId: number | undefined;
    carVersionName: string;
    licensePlate: string;
    vinNumber: string;
    customerId: number;
    carRepairHistories: ICarRepairHistory[];
}