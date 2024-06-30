import { IResponse } from "@/types";
import { ICustomerType } from "@/types/CustomerType";
import { garageService } from "@/utils/request";
import { ecommerceService } from "@/utils/request";

const GARAGE_ID = localStorage.getItem("garageId");

export function getCustomerTypes(data: any): Promise<IResponse> {
    // return garageService({
    //     url: `/customer-types/${GARAGE_ID}`,
    //     method: "get",
    //     params: data,
    // });

     return garageService({
         url: `/customer-types`,
         method: "get",
         params: data,
     });
}

export function updateCustomerType(data: ICustomerType, typeId: number): Promise<IResponse> {
    return garageService({
        url: `/customer-types/${GARAGE_ID}/update/${typeId}`,
        method: "patch",
        data,
    });
}

export function createCustomerType(data: ICustomerType): Promise<IResponse> {
    return garageService({
        url: `/customer-types/${GARAGE_ID}`,
        method: "post",
        data,
    });
}

