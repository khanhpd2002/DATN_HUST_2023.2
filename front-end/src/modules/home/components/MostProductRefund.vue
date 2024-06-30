<template>
    <div class="rounded-md bg-white p-4">
        <div class="flex flex-col justify-between lg:flex-row gap-4">
            <p class="font-bold mb-5">
                {{ $t("module.home.service.returnProduct") }}
            </p>
        </div>

        <Bar :data="chartData" :options="options1" />
    </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "@/composables/useI18n";
import { getMostProductRefundeds } from "../api/index";
import { onMounted } from "vue";
import { computed } from "vue";

import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
} from "chart.js";
import { Bar } from "vue-chartjs";
import { cloneDeep } from "lodash";

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const options1 = ref<{
    responsive: boolean;
    maintainAspectRatio: boolean;
    indexAxis: string;
}>({
    responsive: true,
    maintainAspectRatio: true,
    indexAxis: "y",
});

const data = ref<{
    labels: string[];
    datasets: {
        label: string;
        backgroundColor: string;
        data: number[];
    }[];
}>({
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
        {
            label: "Data One",
            backgroundColor: "#f87979",
            data: [40, 39, 10, 40, 39, 80, 40],
        },
    ],
});

const chartData = computed(() => {
    return cloneDeep(data.value);
});

const { $t } = useI18n();

const getInfoOutputOrderDistributor = async () => {
    const res = await getMostProductRefundeds();
    if (res.code == 1) {
        data.value = res.data;
    }
};

onMounted(() => {
    getInfoOutputOrderDistributor();
});
</script>
