import { defineStore } from "pinia";
import { getAllCustomerGroup, getAllProduct } from "../api/index";

export const usePrices = defineStore("usePrices", {
    state: () => {
        return {
            allCustomerGroup: [],
            allProduct: [],
        };
    },
    actions: {
        async getAllCustomerGroup() {
            let res = (await getAllCustomerGroup()) as any;
            this.allCustomerGroup = res.data;
            return res;
        },
        async getAllProduct() {
            let res = (await getAllProduct()) as any;
            this.allProduct = res.data;
            return res;
        },
    },
});
