<template>
    <div class="flex flex-col lg:flex-row gap-4 p-[16px] bg-white rounded justify-between">
        <ul class="flex gap-5">
            <li v-for="item in tabs" :key="item.id" class="list-tabs">
                <p :class="[currentTab == item.id ? 'active' : '']" @click="() => handleActiveTab(item.id)">
                    {{ item.title }}
                </p>
            </li>
        </ul>
        <div class="flex flex-col lg:flex-row gap-4 sm:mt-4">
            <div class="flex gap-4 items-center">
                <span> {{ $t("module.debt.serviceTicket.filter.from") }}</span>
                <ACCDDatePicker size="md" v-model="filterConfig.fromDate.value" formatter="DD/MM/YY" />
            </div>
            <div class="flex gap-4 items-center">
                <span> {{ $t("module.debt.serviceTicket.filter.to") }}</span>
                <ACCDDatePicker size="md" v-model="filterConfig.toDate.value" formatter="DD/MM/YY" />
            </div>
        </div>
    </div>
    <Overview v-if="currentTab == 1" :fromDate="dayjs(filterConfig.fromDate.value, 'DD/MM/YY').format('YYYY-MM-DD')"
        :toDate="dayjs(filterConfig.toDate.value, 'DD/MM/YY').format('YYYY-MM-DD')"></Overview>
    <Customer v-if="currentTab == 2"></Customer>
    <Inventory v-if="currentTab == 3" :fromDate="dayjs(filterConfig.fromDate.value, 'DD/MM/YY').format('YYYY-MM-DD')"
        :toDate="dayjs(filterConfig.toDate.value, 'DD/MM/YY').format('YYYY-MM-DD')">
    </Inventory>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import Overview from "./Overview.vue";
import Customer from "./Customer.vue";
import Inventory from "./Inventory.vue";

import customParseFormat from "dayjs/plugin/customParseFormat";
import dayjs from "dayjs";

dayjs.extend(customParseFormat);
const tabs = ref([
    { title: "Tổng quan", id: 1 },
    { title: "Khách hàng", id: 2 },
    { title: "Tồn kho", id: 3 },
]);
const currentTab = ref(1);

const filterConfig = ref({
    toDate: {
        value: dayjs().format("DD/MM/YY"),
    },
    fromDate: {
        value: dayjs().startOf("month").format("DD/MM/YY"),
    },
});



watch(
    () => currentTab.value,
    () => {
        filterConfig.value = {
            toDate: {
                value: dayjs().format("DD/MM/YY"),
            },
            fromDate: {
                value: dayjs().startOf("month").format("DD/MM/YY"),
            },
        };
    },
    { deep: true }
);

const handleActiveTab = (id: number) => {
    currentTab.value = id;
};
</script>

<style lang="scss">
.list-tabs {
    color: "#C2C3C0";
    font-size: 16px;
    font-style: normal;
    font-weight: 400;
    line-height: 24px;
    /* 150% */
    letter-spacing: 0.2px;
}

.active {
    color: #4355f5;
    font-style: normal;
    font-weight: 700;
    border-bottom: 2px solid #4355f5;
}
</style>
