<template>
	<div class="rounded-md bg-white p-4 h-full">
		<p class="font-bold h-[38px] leading-[38px] whitespace-nowrap">
			{{ $t('module.home.service.popularService') }}
		</p>

		<div class="flex flex-col lg:flex-row gap-4">
			<div class="w-1/2">
				<span> {{ $t('module.home.service.filter.title') }}</span>
				<ACCDSelect
					v-model="filter"
					:options="filterOptions"
					:label="$t('module.home.service.filter.title')"
					:placeholder="$t('module.home.service.filter.title')"
					width="60px"
				/>
			</div>
			<div class="w-1/2">
				<p>{{ $t('module.home.service.total') }}</p>
				<p class="text-base font-bold">{{ total }}</p>
			</div>
		</div>

		<v-chart
			v-if="option.series.length > 0"
			class="chart h-[250px] mt-6"
			:option="option"
			autoresize
		/>
		<div v-else class="relative h-full">
			<p class="no-data">
				{{ $t('module.home.service.noData') }}
			</p>
		</div>
	</div>
</template>

<script setup lang="ts">
import { computed, watch, onMounted, ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { getTypeTrendService } from '../api/index'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import {
	TitleComponent,
	TooltipComponent,
	LegendComponent,
} from 'echarts/components'
import VChart from 'vue-echarts'
use([
	CanvasRenderer,
	PieChart,
	TitleComponent,
	TooltipComponent,
	LegendComponent,
])

const data = ref<number[]>([])
const labels = ref<string[]>([])

const list = computed(() => {
	return {
		labels: labels.value,
		datasets: [
			{
				backgroundColor: [
					'#15AA2C',
					'#4355F5',
					'#FF6B00',
					'#ED1F42',
					'#A95EDB',
					'#07BAC6',
				],
				data: data.value,
			},
		],
	}
})

const total = computed(() => {
	return data.value.reduce((acc, current) => acc + current, 0)
})

const { $t } = useI18n()

type TrendServiceProps = {
	id: number
	name: string
	quantity: number
}

type DataProps = {
	data: TrendServiceProps[]
	total?: number
}

const dataTrendService = ref<DataProps>()

const filter = ref<string>('1m')
const filterOptions = ref<{ value: string; label: string }[]>([
	{
		value: '1m',
		label: '1 Tháng',
	},
	{
		value: '3m',
		label: '3 Tháng',
	},
	{
		value: '6m',
		label: '6 Tháng',
	},
	{
		value: '1Y',
		label: '1 Năm',
	},
])

watch(
	() => {
		filter.value
	},
	async () => {
		await getListTypeTrendService()
	},
	{ deep: true }
)

const data1 = ref([])

const option = computed(() => {
	return {
		title: {
			left: 'center',
		},
		tooltip: {
			trigger: 'item',
			formatter: '{a} <br/>{b} : {c} ({d}%)',
		},
		legend: {
			orient: 'horizontal',
			top: '5%',
			left: 'center',
			data: data1.value.map((item: any) => item.name),
		},

		series: [
			{
				name: 'Dịch vụ phổ biến',
				type: 'pie',
				radius: ['40%', '70%'],
				avoidLabelOverlap: false,

				label: {
					show: false,
					position: 'center',
				},

				data: data1.value,
			},
		],
	}
})

const getListTypeTrendService = async () => {
	const query = {
		time: filter.value,
	}

	const res = await getTypeTrendService(query)

	if (res.code == 1) {
		dataTrendService.value = res.data.listTypePopularServices
		data1.value = res.data.listTypePopularServices.map((item: any) => {
			return {
				name: item.name,
				value: item.quantity,
			}
		})
	}
}

onMounted(async () => {
	await getListTypeTrendService()
})
</script>
