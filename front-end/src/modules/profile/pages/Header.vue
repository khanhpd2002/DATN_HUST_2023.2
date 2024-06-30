<template>
	<div class="flex flex-col lg:flex-row gap-4">
		<div class="w-full lg:w-1/2 py-[36px] px-[24px] bg-white rounded">
			<div class="flex w-1/2 gap-[23px]">
				<img src="@/assets/images/svg/dollar.svg" size="40px" />
				<div class="flex flex-col">
					<p class="font-medium">Doanh thu năm</p>
					<p class="font-bold text-[28px]">{{ formatPriceVN(revenue) }}đ</p>
				</div>
			</div>
		</div>
		<div class="w-full lg:w-1/2 py-[36px] px-[24px] bg-white rounded">
			<div class="flex gap-[23px]">
				<img src="@/assets/images/svg/order.svg" size="40px" />
				<div class="flex flex-col">
					<p class="font-medium">Số đơn hàng / tháng</p>
					<p class="font-bold text-[28px]">{{ formatPriceVN(numberService) }}</p>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getDetailDashboard} from "@/modules/home/api";
import dayjs from "dayjs";
import {formatPriceVN} from "@/modules/sharedModules/component/constants";

const revenue = ref<number>();
const numberService = ref<number>();

onMounted(async () => {
	const fromDate = dayjs().startOf('year').format('YYYY-MM-DD')
	const toDate = dayjs().format('YYYY-MM-DD')
	const res = await getDetailDashboard({
		fromDate: fromDate,
		toDate: toDate
	})
	if (res.code == 1) {
		const overview = res.data.overview
		overview.forEach((item: OverviewDashboard) => {
			if (item.name === 'revenue') {
				revenue.value = item.current
			}
			if (item.name === 'number_service') {
				numberService.value = item.current
			}
		})
	}
})
</script>
