<template>
    <div class="rounded-md bg-white p-4 h-full">
        <p class="font-bold whitespace-nowrap mb-5">
            {{ $t("module.home.service.longStock") }}
        </p>

        <div class="border rounded-lg border-black">
            <div class="flex lg:flex-row justify-between rounded-t-lg p-2 bg-color-header font-bold">
                <p>Phụ tùng</p>
                <p>Ngày nhập</p>
            </div>
            <div class="flex lg:flex-row justify-between items-center py-1 p-2" v-for="(item, i) in productList"
                :key="item.productId" :class="i == productList.length - 1 ? '' : 'border-b-2'">
                <div class="w-3/4">
                    <h3 class="font-bold text-base">{{ item.productName }}</h3>
                    <p class="text-xs">{{ item.productCode }}</p>
                </div>

                <div>
                    <p>{{ dayjs(item.inboundDate).format("DD/MM/YYYY") }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { onMounted, ref } from "vue";

import { getLongTermInventoryProducts } from "../api/index";
import { dayjs } from "element-plus";

const { $t } = useI18n();

type LongInventoryProps = {
    inboundDate: string;
    productCode: string;
    productId: number;
    productName: string;
};

const productList = ref<LongInventoryProps[]>([]);
const getInforLongTernInventoryProduct = async () => {
    const params = {
        page: 0,
        size: 5,
    };
    const res = await getLongTermInventoryProducts(params);
    if (res.code == 1) {
        productList.value = res.data.content;
    }
};

onMounted(() => {
    getInforLongTernInventoryProduct();
});
</script>
