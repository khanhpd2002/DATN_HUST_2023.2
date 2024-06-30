import { defineStore } from "pinia";
import { getCustomerType } from "@/modules/customerType/api";
const GARAGE_ID = localStorage.getItem("garageId");
const MAX_INTEGER = import.meta.env.VITE_MAX_INTEGER;
export const useProducts = defineStore("useCustomerType", {
    state: () => {
        return {
            listCustomerType: [],
        };
    },
    actions: {
        async getCustomerType() {
            if (this.listCustomerType?.length) return;
            const result = await getCustomerType(
                GARAGE_ID !== null ? Number(GARAGE_ID) : 0,
                {
                    pageSize: MAX_INTEGER,
                    pageNumber: 1,
                }
            );
            this.listCustomerType = result.data?.map(
                (item: any) => ({ ...item, value: item.name } || [])
            );
        },
    },
});
