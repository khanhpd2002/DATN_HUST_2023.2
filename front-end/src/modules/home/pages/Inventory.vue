<template>
    <div class="flex flex-col rounded-md p-4 mt-4 bg-white">
        <div class="flex flex-col lg:flex-row gap-4 w-full">
            <Result v-for="item in inventory" :key="item.color" :data="item" />
        </div>

        <div class="flex flex-col gap-4 lg:flex-row mt-4 w-full">
            <div class="w-full lg:w-1/2">
                <ValueInventory />
            </div>
            <div class="w-full lg:w-1/2">
                <MostProductRefund />
            </div>
        </div>

        <div class="flex flex-col lg:flex-row gap-4 mt-4">
            <div class="w-full lg:w-1/3"><OrderOutput /></div>
            <div class="w-full lg:w-1/3">
                <LowInventoryProduct />
            </div>
            <div class="w-full lg:w-1/3">
                <LongInventory />
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import OrderOutput from "../components/OrderOutput.vue";
import LowInventoryProduct from "../components/LowInventoryProduct.vue";
import LongInventory from "../components/LongInventory.vue";
import MostProductRefund from "../components/MostProductRefund.vue";
import ValueInventory from "../components/ValueInventory.vue";

import Result from "../components/Result.vue";
import { useI18n } from "@/composables/useI18n";

import { getSummaryInventory } from "../api";
import { onMounted } from "vue";
const { $t } = useI18n();

type QueryProps = {
    fromDate: string;
    toDate: string;
};

const props = withDefaults(defineProps<QueryProps>(), {
    fromDate: "",
    toDate: "",
});
const inventory = ref<any[]>([
    {
        name: "totalProductCode",
        color: "#15AA2C",
        background: "#C6F8CE",
        title: $t("module.home.service.spareCode"),
        width: "w-full sm:w-1/2",
        result: "",
    },
    {
        name: "totalProductQuantity",
        color: "#315BF1",
        background: "#D6DEFC",
        title: $t("module.home.service.quantitySpare"),
        width: "w-full sm:w-1/2",
        result: "",
    },
    {
        name: "inventoryValue",
        color: "#ED1F42",
        background: "#FBD2D9",
        title: $t("module.home.service.valueInventory"),
        width: "w-full sm:w-1/2",
        result: "",
    },
    {
        name: "totalTicketHasNotDelivery",
        color: "#07BAC6",
        background: "#C6FFFD",
        title: $t("module.home.service.undeliveredOrder"),
        width: "w-full sm:w-1/2",
        result: "",
    },
]);

watch(
    () => props,
    () => {
   
        getInfoSummaryInventory(props);
    },
    { deep: true }
);

const getInfoSummaryInventory = async (query?: any) => {
    const res = await getSummaryInventory(query);
  
    if (res.code == 1) {
        let temp: any[] = [];
        inventory.value.forEach((item) => {
            Object.keys(res.data).forEach((el: any) => {
                if (el == item.name) {
                    temp.push({
                        ...item,
                        result: res.data[el],
                    });
                }
            });
        });

        inventory.value = temp;
    }
};

onMounted(() => {
    getInfoSummaryInventory(props);
});
</script>
