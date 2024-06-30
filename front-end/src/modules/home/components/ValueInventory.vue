<template>
    <div class="rounded-md bg-white p-4 h-full">
        <div class="flex flex-col justify-between lg:flex-row gap-4">
            <p class="font-bold">
                {{ $t("module.home.service.valueInventory") }}
            </p>
            <ACCDTabs :tabs="tabs" v-model="currentTab"> </ACCDTabs>
        </div>

        <div class="relative min-h-20 lg:h-full">
            <p class="no-data">
                {{ $t("module.home.service.noData") }}
            </p>
        </div>

        <!-- <Line :data="chartData" :options="options1" /> -->
    </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "@/composables/useI18n";

import { onMounted } from "vue";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";
import { Line } from "vue-chartjs";
import { watch } from "vue";
import { computed } from "vue";
import { dayjs } from "element-plus";
import { cloneDeep } from "lodash";
ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

const options1 = {
    responsive: true,
    maintainAspectRatio: true,
};

const data = ref<{
    labels: string[];
    datasets: { label: string; backgroundColor: string; data: number[] }[];
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

const tabs = ref([
    {
        title: "1 tuần",
    },
    {
        title: "1 tháng",
    },
    {
        title: "1 năm",
    },
]);
const currentTab = ref(1);

const chartData = computed(() => {
    return cloneDeep(data.value);
});

// watch(
//     () => currentTab.value,
//     async () => {
//         if (currentTab.value == 1) {
//             filter.value.groupByType = "month";
//             filter.value.toDate = dayjs().format("YYYY-MM-DD");
//             filter.value.fromDate = dayjs()
//                 .subtract(1, "month")
//                 .format("YYYY-MM-DD");
//         } else if (currentTab.value == 2) {
//             filter.value.groupByType = "year";
//             filter.value.toDate = dayjs().format("YYYY-MM-DD");
//             filter.value.fromDate = dayjs()
//                 .subtract(1, "year")
//                 .format("YYYY-MM-DD");
//         } else {
//             filter.value.groupByType = "week";
//             filter.value.toDate = dayjs().format("YYYY-MM-DD");
//             filter.value.fromDate = dayjs()
//                 .subtract(1, "week")
//                 .format("YYYY-MM-DD");
//         }
//         await getDataByChartRenevue();
//         console.log("filter.value", filter.value);
//     }
// );

const { $t } = useI18n();

// type TrendServiceProps = {
//     id: number;
//     name: string;
//     quantity: number;
// };

// type DataProps = {
//     data: TrendServiceProps[];
//     total?: number;
// };

// const filter = ref<{
//     groupByType: string;
//     fromDate: string;
//     toDate: string;
// }>({
//     groupByType: "month",
//     fromDate: dayjs().subtract(1, "month").format("YYYY-MM-DD"),
//     toDate: dayjs().format("YYYY-MM-DD"),
// });

// const getDataByChartRenevue = async () => {
//     const query = {
//         groupByType: filter.value.groupByType,
//         fromDate: filter.value.fromDate,
//         toDate: filter.value.toDate,
//     };

//     const res = await getRenevueRepair(query);
//     if (res.code == 1) {
//         data.value.labels = res.data.labels;
//         data.value.datasets = res.data.datasets;
//     }
//     console.log("data data", data.value);
// };

// onMounted(async () => {
//     await getDataByChartRenevue();
// });
</script>
