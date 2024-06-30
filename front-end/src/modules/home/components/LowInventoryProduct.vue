<template>
    <div class="rounded-md bg-white p-4 h-full">
        <div class="flex flex-col justify-between lg:flex-row gap-4">
            <p class="font-bold mb-5">
                {{ $t("module.home.service.lowInventory") }}
            </p>
        </div>

        <v-chart
            :option="option"
            autoresize
            class="chart"
            style="height: 300px"
        />
    </div>
</template>

<script lang="ts" setup>
import { getLowInventoryProducts } from "../api/index";
import VChart from "vue-echarts";
import * as echarts from "echarts/core";
import {
    ToolboxComponent,
    TooltipComponent,
    GridComponent,
    LegendComponent,
} from "echarts/components";
import { BarChart, LineChart } from "echarts/charts";
import { UniversalTransition } from "echarts/features";
import { CanvasRenderer } from "echarts/renderers";
import { onMounted, ref, computed } from "vue";

echarts.use([
    ToolboxComponent,
    TooltipComponent,
    GridComponent,
    LegendComponent,
    BarChart,
    LineChart,
    CanvasRenderer,
    UniversalTransition,
]);

const data = ref([]);
const legend = ref([]);
const xAxis = ref([]);
const option = computed(() => {
    return {
        tooltip: {
            trigger: "axis",
            axisPointer: {
                type: "cross",
                crossStyle: {
                    color: "#999",
                },
            },
        },

        legend: {
            data: legend.value,
        },
        xAxis: [
            {
                type: "category",
                data: xAxis.value,
                axisPointer: {
                    type: "shadow",
                },
            },
        ],
        yAxis: [
            {
                type: "value",
                name: legend.value[0],
            },
            {
                type: "value",
                name: legend.value[1],
            },
        ],
        series: data.value,
    };
});

const getInforChartLowInventoryProducts = async () => {
    const res = await getLowInventoryProducts();

    if (res.code == 1) {
        legend.value = res.data.datasets.map((item: any) => item.label);
        data.value = res.data.datasets.map((item: any) => {
            return { ...item, name: item.label };
        });
        xAxis.value = res.data.labels;
    }
};

onMounted(() => {
    getInforChartLowInventoryProducts();
});
</script>
