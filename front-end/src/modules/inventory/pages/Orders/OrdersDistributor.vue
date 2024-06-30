<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.order.filter.orderCode') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.orderCode.value"
					:placeholder="$t('module.inventory.order.filter.orderCode')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.order.filter.distributorCode') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.distributorCode.value"
					:placeholder="$t('module.inventory.order.filter.distributorCode')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.order.filter.distributorName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.distributorName.value"
					:placeholder="$t('module.inventory.order.filter.distributorName')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.order.filter.deliveryStatus') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.deliveryStatus.value"
					:options="filterConfig.deliveryStatus.options"
					:placeholder="$t('module.inventory.order.filter.deliveryStatus')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer justify="between">
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.order.filter.time') }}
				</p>
				<div class="w-full flex gap-4">
					<div class="w-1/2 flex gap-4 items-center">
						<span class="font-semibold">
							{{ $t('module.inventory.order.filter.fromDate') }}</span
						>
						<ACCDDatePicker
							class="w-full font-medium"
							size="md"
							v-model="filterConfig.fromDate.value"
							formatter="MM/DD/YY"
						/>
					</div>
					<div class="w-1/2 flex gap-4 items-center">
						<span class="font-semibold">
							{{ $t('module.inventory.order.filter.toDate') }}</span
						>
						<ACCDDatePicker
							class="w-full font-medium"
							size="md"
							v-model="filterConfig.toDate.value"
							formatter="MM/DD/YY"
							:disabledDates="disabledDates"
						/>
					</div>
				</div>
			</div>
			<WrapFlexContainer align="end">
				<!-- <div>
					<ACCDButton
						type="secondary"
						variant="fill"
						class="bg-info-secondary w-full"
					>
						<ACCDIcon
							class="text-gray-700 pr-3"
							name="fa-solid fa-upload"
						></ACCDIcon>
						<span class="text-info-base font-medium">{{
							$t('module.inventory.order.uploadFromExcel')
						}}</span>
					</ACCDButton>
				</div> -->
				<div>
					<ACCDButton type="primary" class="w-full" @click="createOrder">
						{{ $t('module.inventory.order.create') }}
					</ACCDButton>
				</div>
				<!-- <div >
					<ACCDButton type="secondary" variant="fill" @click="filter" class="w-full">
							{{ $t("module.inventory.order.find") }}</ACCDButton>
			</div> -->
			</WrapFlexContainer>
		</WrapFlexContainer>
		<ACCDTable
			:columns="columns"
			:rowData="rowData"
			:pagination="pagination"
			@changePage="changePage"
			class="table-fixed-layout"
		>
			<template #cell-index="{ rowIndex }">
				{{ rowIndex + 1 }}
			</template>
			<template #cell-orderCode="{ row }"> #{{ row.orderCode }} </template>
			<template #cell-createdAt="{ row, col, field }">
				{{ dayjs(row.createdAt).format('DD/MM/YY') }}
			</template>
			<template #cell-totalPrice="{ row }">
				{{ row.totalPrice?.toString()?.replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}đ
			</template>
			<template #cell-deliveryStatus="{ row }">
				{{
					row.deliveryStatus &&
					$t('module.inventory.order.deliveryStatus.' + row.deliveryStatus)
				}}
			</template>
			<template #cell-action="{ row, col, field }">
				<div
					class="flex gap-3 cursor-pointer justify-center"
					@click="() => updateOrder(row)"
				>
					<span class="text-gray-700 text-primary">
						{{ $t('module.inventory.order.table.handle') }}
					</span>
				</div>
			</template>
		</ACCDTable>
	</div>
	<OrderDistributorForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:state="formConfig.state"
		:orderId="formConfig.orderId"
		@refresh="getTableRowData"
	>
	</OrderDistributorForm>
</template>

<script setup lang="ts">
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'
import { getOrderDistributorControllerList } from '@/modules/inventory/api'
import { ref, onMounted, watch } from 'vue'

import dayjs from 'dayjs'

import customParseFormat from 'dayjs/plugin/customParseFormat'
import OrderDistributorForm from '@/modules/inventory/pages/Orders/OrdersDistributorForm.vue'
import { EFormState } from '@/enums'
import { debounce } from 'lodash'
import { saveLogTracking } from '@/modules/sharedModules/api'
import { emitter } from '@/utils/mitt'
import useCDRouter from '@/composables/useRouter'

const PAGE_SIZE = 10

const { router, query } = useCDRouter()

dayjs.extend(customParseFormat)

const { $t } = useI18n()
const { $toast } = useToast()
const filterConfig = ref({
	orderCode: {
		value: '',
	},
	distributorCode: {
		value: '',
	},
	distributorName: {
		value: '',
	},
	deliveryStatus: {
		value: '',
		options: [
			{
				value: 1,
				label: $t('module.inventory.order.deliveryStatus.1'),
			},
			{
				value: 4,
				label: $t('module.inventory.order.deliveryStatus.4'),
			},
			{
				value: 6,
				label: $t('module.inventory.order.deliveryStatus.6'),
			},
		],
	},
	fromDate: {
		value: '',
	},
	toDate: {
		value: '',
	},
})
const formConfig = ref({
	show: false,
	state: EFormState.ADD,
	orderId: 0,
})

const columns = ref([
	{
		key: 'index',
		headerName: $t('module.inventory.order.table.index'),
		width: '80px',
	},
	{
		key: 'orderCode',
		headerName: $t('module.inventory.order.table.orderCode'),
	},
	{
		key: 'distributorName',
		headerName: $t('module.inventory.order.table.distributorName'),
	},
	{
		key: 'createdAt',
		headerName: $t('module.inventory.order.table.createdAt'),
	},
	{
		key: 'totalPrice',
		headerName: $t('module.inventory.order.table.totalPrice'),
	},
	{
		key: 'deliveryStatus',
		headerName: $t('module.inventory.order.table.status'),
	},
	{
		key: 'action',
		headerName: $t('module.inventory.order.table.action'),
		align: 'center',
	},
])
const rowData = ref([])
const pagination = ref({
	perPage: 10,
	total: 0,
	currentPage: 1,
})

const isDateError = ref(false)

watch(
	() => filterConfig.value,
	debounce(() => {
		filter()
	}, 500),
	{ deep: true }
)

const changePage = async (val: any) => {
	// pagination.value.currentPage = val.currentPage
	await query({
		currentPage: val.currentPage,
		pageSize: val.pageSize,
	})
}

const filter = () => {
	pagination.value.currentPage = 1

	if (
		dayjs(filterConfig.value.fromDate.value).isAfter(
			filterConfig.value.toDate.value
		)
	) {
		isDateError.value = true
		return
	} else {
		isDateError.value = false
	}
	let data = {} as any
	Object.keys(filterConfig.value).map((field) => {
		if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
			data[field] =
				filterConfig.value[field as keyof typeof filterConfig.value].value
			if (field == 'fromDate' || field == 'toDate') {
				data[field] = dayjs(
					filterConfig.value[field as keyof typeof filterConfig.value].value,
					'DD/MM/YY'
				).format('YYYY-MM-DD')
			}
		}
	})
	getTableRowData({
		...data,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})
}

const getTableRowData = async (params: any) => {
	let res = await getOrderDistributorControllerList({
		...params,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})
	rowData.value = res.data.map((a: any) => {
		return {
			...a,
			paymentStatusTranslated: $t(
				'module.inventory.order.deliveryStatus.' + a.paymentStatus
			),
		}
	})
	pagination.value.total = res.totalElement
}

const updateOrder = (row: any) => {
	router.push('/app/inventory/order-spare-parts/' + row.id + '/edit')
	// formConfig.value.orderId = row.id;
	// formConfig.value.state = EFormState.EDIT;
	// formConfig.value.show = true;
}

const detailOrder = (row: any) => {
	// router.push("/app/inventory/order-spare-parts/" + row.id + "/view");
	formConfig.value.orderId = row.id
	formConfig.value.state = EFormState.VIEW
	formConfig.value.show = true
}

const createOrder = () => {
	saveLogTracking({
		logEvent: 'Click_FOR_CREATE_ORDER_DISTRIBUTOR',
		garageId: localStorage.getItem('garageId'),
		actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
	})
	router.push('/app/inventory/order-spare-parts/add')
	// formConfig.value.orderId = 0;
	// formConfig.value.state = EFormState.ADD;
	// formConfig.value.show = true;
}

const disabledDates = (date: Date): boolean => {
	return dayjs(date) <= dayjs(filterConfig.value.fromDate.value)
}

onMounted(async () => {
	emitter.on('router-on-set-query', async (params) => {
		pagination.value.currentPage = (
			params as { pageSize: number; currentPage: number }
		).currentPage
		pagination.value.perPage = (
			params as { pageSize: number; currentPage: number }
		).pageSize
		let data = {} as any

		Object.keys(filterConfig.value).map((field) => {
			if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
				data[field] =
					filterConfig.value[field as keyof typeof filterConfig.value].value
				if (field == 'fromDate' || field == 'toDate') {
					data[field] = dayjs(
						filterConfig.value[field as keyof typeof filterConfig.value].value,
						'DD/MM/YY'
					).format('YYYY-MM-DD')
				}
			}
		})

		await getTableRowData({
			...data,
			pageNumber: pagination.value.currentPage,
			pageSize: pagination.value.perPage,
		})
	})
	if (router.currentRoute.value.query) {
		if (Number(router.currentRoute.value.query.currentPage)) {
			pagination.value.currentPage = Number(
				router.currentRoute.value.query.currentPage
			)
		}
		if (Number(router.currentRoute.value.query.pageSize)) {
			pagination.value.perPage = Number(
				router.currentRoute.value.query.pageSize
			)
		}
	}
})
await getTableRowData({})
</script>
