import {defineStore} from "pinia";
import {getListDistributors, getListInventories} from "@/modules/product/api";

export const useProducts = defineStore("useProducts", {
  state: () => {
    return {
      listInventories: [],
      listDistributors: [],
      listServiceTypes: [
        {
          id: 1,
          code: 'product'
        },
        {
          id: 2,
          code: 'service'
        },
      ],
      listSparePartType: ['SERVICE_GARAGE', 'ENGINE', 'EXHAUST', 'FILTER', 'BREAK', 'HEAT_DISSIPATION', 'POWER', 'AIR_CONDITION', 'SHELL_BODY', 'STEERING',
      'FUEL', 'TRANSMISSION', 'LIGHTING', 'OILS', 'TOOL'].map((item, index) => ({id: index, code: item}))
    };
  },
  actions: {
    async getListInventories() {
      if (this.listInventories?.length) return
      const result = await getListInventories({
        pageSize: 1000,
        pageNumber: 1
      })
      this.listInventories = result.data?.map((item: any) => ({...item, value: item.name}) || [])
    },
    async getListDistributors() {
      if (this.listDistributors?.length) return
      const result = await getListDistributors(348, {
        pageSize: 1000,
        pageNumber: 1
      })
      this.listDistributors = result.data?.map((item: any) => ({...item, value: item.name}) || [])
    }
  }
});
