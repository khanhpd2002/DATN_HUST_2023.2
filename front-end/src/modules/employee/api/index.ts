import { IEmployee, IResponse } from "@/types";
import { garageService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");

export function getGarageEmployees(data: any): Promise<IResponse> {
    return garageService({
        url: `/garages/employees/${GARAGE_ID}`,
        method: "get",
        params: data,
    });
}

export function getEmployeeById(employeeId: number): Promise<IResponse> {
    return garageService({
        url: `/garages/employees/${GARAGE_ID}/info/${employeeId}`,
        method: "get",
    });
}

export function createEmployee(data: any): Promise<IResponse> {
    return garageService({
        url: `/garages/employees/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

export function updateEmployee(data: IEmployee, employeeId: number): Promise<IResponse> {
    return garageService({
        url: `/garages/employees/${GARAGE_ID}/update/${employeeId}`,
        method: "patch",
        data,
    });
}
