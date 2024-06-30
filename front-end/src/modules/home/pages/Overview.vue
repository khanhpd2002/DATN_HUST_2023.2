<template>
	<div class="flex flex-col rounded-md bg-slate-50 p-4 mt-4 bg-white rounded">
		<div class="flex flex-col lg:flex-row gap-4 w-full">
			<Result v-for="item in overview" :key="item.color" :data="item" />
		</div>

		<div class="flex flex-col gap-4 lg:flex-row mt-4 w-full min-h-[300px]">
			<div class="lg:w-1/2 mb-[30px]">
				<Revenue />
			</div>
			<div class="lg:w-1/2">
				<PopularService :data="popularServices" />
			</div>
		</div>

		<div class="flex flex-col lg:grid grid-cols-2 gap-4 mt-[50px]">
			<div class="w-full">
				<BestSaller :data="popularServices" />
			</div>
			<div class="flex flex-col lg:flex-row gap-4">
				<div class="w-full lg:w-1/2">
					<ExcellentStaff :data="excellentStaff" />
				</div>
				<div class="w-full lg:w-1/2">
					<TrendService />
				</div>
			</div>
		</div>
	</div>
<!--	<div class="overflow-x-auto w-full xl:max-w-[calc(100vw-320px)]">-->
<!--		<SwipperContainer />-->
<!--	</div>-->
</template>
<script setup lang="ts">
import { ref, watch } from 'vue'
import Result from '../components/Result.vue'
import PopularService from '../components/PopularService.vue'
import TrendService from '../components/TrendService.vue'
import Revenue from '../components/Revenue.vue'
import BestSaller from '../components/BestSaller.vue'
import ExcellentStaff from '../components/ExcellentStaff.vue'

import { getDetailDashboard } from '../api'
import { useI18n } from '@/composables/useI18n'
import { onMounted } from 'vue'

const { $t } = useI18n()
type QueryProps = {
	fromDate: string
	toDate: string
}

const props = withDefaults(defineProps<QueryProps>(), {
	fromDate: '',
	toDate: '',
})

export type RevenueData = {
	incomeOfEmployee: number
	numberEmployee: number
	numberService: number
	numberServiceCancel: number
	refundRate: number
	revenue: number
}

const overview = ref<any[]>([
	{
		name: 'revenue',
		color: '#15AA2C',
		background: '#C6F8CE',
		title: 'Doanh thu',
		width: 'w-full sm:w-1/2',
		percent: '',
		result: '',
	},
	{
		name: 'number_service',
		color: '#315BF1',
		background: '#D6DEFC',
		title: 'Số lượt dịch vụ',
		width: 'w-full sm:w-1/2',
		percent: '',
		result: '',
	},
	{
		name: 'number_employee',
		color: '#ED1F42',
		background: '#FBD2D9',
		title: 'Số thợ',
		width: 'w-full sm:w-1/5',
		percent: '',
		result: '',
	},
	{
		name: 'income_of_employee',
		color: '#07BAC6',
		background: '#C6FFFD',
		title: 'Doanh thu/Thợ',
		width: 'w-full sm:w-1/2',
		percent: '',
		result: '',
	},
	{
		name: 'number_service_cancel',
		color: '#FF6B00',
		background: '#FFE1CC',
		title: 'Lượt hủy',
		width: 'w-full sm:w-1/5',
		percent: '',
		result: '',
	},
	{
		name: 'refund_rate',
		color: '#A95EDB',
		background: '#F1DCFF',
		title: 'Tỷ lệ hoàn trả',
		width: 'w-full sm:w-1/2',
		percent: '',
		result: '',
	},
])

const popularServices = ref<{ name: string; money: number }[]>([])

const excellentStaff = ref<
	{
		money: number
		name: string
		serviceName: string
		image?: string
	}[]
>([])

watch(
	() => props,
	() => {
		if (props.fromDate && props.toDate) getInforDashboard(props)
	},
	{ deep: true }
)

const getInforDashboard = async (query?: any) => {
	const res = await getDetailDashboard(query)
	if (res.code == 1) {
		popularServices.value = res.data.popularServices
		excellentStaff.value = res.data.excellentStaffs

		let temp: any[] = []
		overview.value.forEach((item) => {
			res.data.overview.forEach((el: any) => {
				if (el.name == item.name) {
					temp.push({
						...item,
						result: el.current,
						percent: el.percent,
					})
				}
			})
		})
		overview.value = temp
	}
}

onMounted(() => {
	getInforDashboard(props)
})
</script>
