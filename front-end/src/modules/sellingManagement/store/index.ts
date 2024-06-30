import { defineStore } from "pinia";

import { getCustomersByGarageId } from "@/modules/sellingManagement/api/index";
export const useCustomerStore = defineStore("Result", {
    state: () => {
        return {
            demoList: {},
            overviewData: {
                title: "",
                content: "",
            },
        };
    },
    getters: {
        // demoList: state => state.demoList
    },
    actions: {
        async getCustomersByGarageId(query: any, garageId: string) {
            const res = await getCustomersByGarageId(query, garageId);

            return this.filterResponse(res, null, ({ data }) => {
                this.overviewData = data;
            });
        },
    },
});
