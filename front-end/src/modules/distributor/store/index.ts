import { defineStore } from "pinia";
import { getListDistributors, getListInventories } from "@/modules/product/api";
const GARAGE_ID = localStorage.getItem("garageId");
const MAX_INTEGER = import.meta.env.VITE_MAX_INTEGER;
export const useProducts = defineStore("useDistributors", {
    state: () => {
        return {
            listDistributors: [],
            // listServiceTypes: [
            //     {
            //         id: 1,
            //         code: "product",
            //     },
            //     {
            //         id: 2,
            //         code: "service",
            //     },
            // ],
            // listSparePartType: [
            //     "ENGINE",
            //     "EXHAUST",
            //     "FILTER",
            //     "BREAK",
            //     "HEAT_DISSIPATION",
            //     "POWER",
            //     "AIR_CONDITION",
            //     "SHELL_BODY",
            //     "STEERING",
            //     "FUEL",
            //     "TRANSMISSION",
            //     "LIGHTING",
            //     "OILS",
            //     "TOOL",
            // ].map((item, index) => ({ id: index + 1, code: item })),
        };
    },
    actions: {
        async getListDistributors() {
            if (this.listDistributors?.length) return;
            const result = await getListDistributors(
                GARAGE_ID !== null ? Number(GARAGE_ID) : undefined,
                {
                    pageSize: MAX_INTEGER,
                    pageNumber: 1,
                }
            );
            this.listDistributors = result.data?.map(
                (item: any) => ({ ...item, value: item.name } || [])
            );
        },
    },
});
