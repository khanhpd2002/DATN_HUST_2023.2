<template>
	<div class="rounded-md bg-white p-4 h-full">
		<div class="flex flex-col justify-between items-center lg:flex-row gap-4">
			<p class="font-bold">
				{{ $t('module.home.service.titleRevenue') }}
			</p>
			<ACCDTabs :tabs="tabs" v-model="currentTab"> </ACCDTabs>
		</div>

		<v-chart :option="option" autoresize class="chart mt-5 min-h-[300px]" />
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { getRenevueRepair } from '../api/index'
import { onMounted } from 'vue'

import { watch } from 'vue'
import { computed } from 'vue'
import { dayjs } from 'element-plus'
import VChart from 'vue-echarts'
import * as echarts from 'echarts/core'
import {
	TitleComponent,
	ToolboxComponent,
	TooltipComponent,
	GridComponent,
	LegendComponent,
} from 'echarts/components'
import { LineChart } from 'echarts/charts'
import { UniversalTransition } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
	TitleComponent,
	ToolboxComponent,
	TooltipComponent,
	GridComponent,
	LegendComponent,
	LineChart,
	CanvasRenderer,
	UniversalTransition,
])

const tabs = ref([
	{
		title: '1 tuần',
	},
	{
		title: '1 tháng',
	},
	{
		title: '1 năm',
	},
])
const currentTab = ref(1)
const data = ref([])
const label = ref([])
const option = computed(() => {
	return {
		tooltip: {
			trigger: 'axis',
		},
		legend: {
			data: data.value.map((item: any) => item.name),
		},
		grid: {
			left: '3%',
			right: '4%',
			bottom: '3%',
			containLabel: true,
		},
		toolbox: {
			feature: {
				saveAsImage: {},
			},
		},
		xAxis: {
			type: 'category',
			boundaryGap: false,
			data: label.value,
		},
		yAxis: {
			type: 'value',
		},
		series: data.value,
	}
})

watch(
	() => currentTab.value,
	async () => {
		if (currentTab.value == 1) {
			filter.value.groupByType = 'month'
			filter.value.toDate = dayjs().format('YYYY-MM-DD')
			filter.value.fromDate = dayjs().subtract(1, 'month').format('YYYY-MM-DD')
		} else if (currentTab.value == 2) {
			filter.value.groupByType = 'year'
			filter.value.toDate = dayjs().format('YYYY-MM-DD')
			filter.value.fromDate = dayjs().subtract(1, 'year').format('YYYY-MM-DD')
		} else {
			filter.value.groupByType = 'week'
			filter.value.toDate = dayjs().format('YYYY-MM-DD')
			filter.value.fromDate = dayjs().subtract(1, 'week').format('YYYY-MM-DD')
		}
		await getDataByChartRenevue()
	}
)

const { $t } = useI18n()

const filter = ref<{
	groupByType: string
	fromDate: string
	toDate: string
}>({
	groupByType: 'month',
	fromDate: dayjs().subtract(1, 'month').format('YYYY-MM-DD'),
	toDate: dayjs().format('YYYY-MM-DD'),
})

const getDataByChartRenevue = async () => {
	const query = {
		groupByType: filter.value.groupByType,
		fromDate: filter.value.fromDate,
		toDate: filter.value.toDate,
	}

	const res = await getRenevueRepair(query)
	if (res.code == 1) {
		data.value = res.data.datasets.map((item: any) => {
			return {
				name: item.label,
				type: 'line',
				stack: 'Total',
				data: item.data,
			}
		})

		label.value = res.data.labels
	}
}

onMounted(async () => {
	await getDataByChartRenevue()

	echarts.use([GridComponent, LineChart, CanvasRenderer, UniversalTransition])
})
</script>
