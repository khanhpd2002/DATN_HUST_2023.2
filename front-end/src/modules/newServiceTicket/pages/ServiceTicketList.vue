<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="flex flex-col w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.newServiceTicket.filter.searchCode') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.filter.value"
					:placeholder="$t('module.newServiceTicket.filter.placeHolder')"
					class="font-medium"
					:trim="true"
				/>
			</div>

			<div class="flex flex-col w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.newServiceTicket.filter.status') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.status.value"
					:placeholder="$t('module.newServiceTicket.filter.status')"
					:options="filterConfig.status.options"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>

		<div class="flex justify-end gap-4">
			<ACCDButton size="md" @click="addServiceTicket" class="px-8 py-3">
				<span class="text-base">
					{{ $t('module.newServiceTicket.create') }}
				</span>
			</ACCDButton>
			<!-- <ACCDButton
				size="md"
				type="primary"
				@click="filter"
				class="w-1/2 lg:w-1/6"
		>
				<span class="text-base">
						{{ $t("module.newServiceTicket.find") }}
				</span>
		</ACCDButton> -->
		</div>
		<div>
			<ACCDTable
				ref="table"
				:columns="columns"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-index="{ rowIndex }">
					{{ (pagination.currentPage - 1) * 10 + rowIndex + 1 }}
				</template>
				<template #cell-code="{ row, col, field }">
					<div
						v-if="row.status == 6"
						class="cursor-pointer"
						@click="() => onViewTicket(row, EFormState.VIEW)"
					>
						<span class="text-gray-700">
							{{ row.code }}
						</span>
					</div>
					<span v-else>{{ row.code }} </span>
				</template>
				<template #cell-totalPrice="{ row }">
					{{
						row.totalPrice
							? row.totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							: '-'
					}}
				</template>
				<template #cell-id="{ row, col, field }"> #{{ row.id }} </template>
				<template #cell-appointmentDate="{ row, col, field }">
					{{ dayjs(row.appointmentDate).format('DD/MM/YY') }}
				</template>
				<template #cell-status="{ row, col, field }">
					<p
						class="py-[8px] px-[12px] rounded-[16px] text-center whitespace-nowrap w-fit"
						:class="`status-new-service-${row.status}`"
					>
						{{
							$t(
								'module.newServiceTicket.status.' +
									(row.status ? row.status : 0)
							)
						}}
					</p>
				</template>
				<template #cell-action="{ row, col, field }">
					<div
						class="flex gap-3 cursor-pointer justify-center"
						@click="() => onViewTicket(row, EFormState.VIEW)"
					>
						<span class="text-gray-700 text-primary">
							{{ $t('module.newServiceTicket.action.handle') }}
						</span>
					</div>
				</template>
			</ACCDTable>
		</div>
	</div>

	<ServiceTicketForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:action="formConfig.action"
		:id="formConfig.id"
		@refresh="onRefreshData"
	></ServiceTicketForm>
</template>
<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { saveLogTracking } from '@/modules/sharedModules/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import ServiceTicketForm from '@/modules/newServiceTicket/pages/ServiceTicketForm.vue'
import { EFormState } from '@/enums'
import { getListOrder } from '@/modules/newServiceTicket/api'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import { useI18n } from '@/composables/useI18n'

import { debounce } from 'lodash'
import { emitter } from '@/utils/mitt'
import useCDRouter from '@/composables/useRouter'

const { $t } = useI18n()

const { router, query } = useCDRouter()

dayjs.extend(customParseFormat)
const PAGE_SIZE = 10
const filterConfig = ref({
	status: {
		value: 0,
		options: [
			{ label: $t('module.newServiceTicket.status.0'), value: 0 },
			{ label: $t('module.newServiceTicket.status.1'), value: 1 },
			{ label: $t('module.newServiceTicket.status.3'), value: 3 },
			{ label: $t('module.newServiceTicket.status.4'), value: 4 },
			{ label: $t('module.newServiceTicket.status.5'), value: 5 },
			{ label: $t('module.newServiceTicket.status.6'), value: 6 },
			{ label: $t('module.newServiceTicket.status.8'), value: 8 },
		],
	},

	filter: {
		value: '',
	},
})
const columns = ref([
	{
		key: 'index',
		headerName: $t('module.newServiceTicket.table.index'),
		width: '80px',
	},
	{
		key: 'code',
		headerName: $t('module.newServiceTicket.table.code'),
		minWidth: '150px',
	},
	{
		key: 'customerName',
		headerName: $t('module.newServiceTicket.table.customerName'),
		minWidth: '120px',
	},
	{
		key: 'appointmentDate',
		headerName: $t('module.newServiceTicket.table.date'),
		minWidth: '100px',
	},
	{
		key: 'totalPrice',
		headerName: $t('module.newServiceTicket.table.totalCash'),
		minWidth: '100px',
	},
	{
		key: 'status',
		headerName: $t('module.newServiceTicket.table.status'),
		minWidth: '100px',
	},
	{
		key: 'action',
		headerName: $t('module.newServiceTicket.table.action'),
		minWidth: '100px',
		align: 'center',
	},
])
const rowData = ref([])
const pagination = ref({
	pageSize: PAGE_SIZE,
	total: 0,
	currentPage: 1,
})
const formConfig = ref({
	show: false,
	action: EFormState.ADD as number,
	id: '',
})

const filter = () => {
	pagination.value.currentPage = 1
	let data = {} as any
	Object.keys(filterConfig.value).map((field) => {
		if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
			data[field] =
				filterConfig.value[field as keyof typeof filterConfig.value].value
		}
	})
	getTableRowData({
		...data,
		pageSize: pagination.value.pageSize,
		pageNumber: pagination.value.currentPage,
	})
}
const onOpenFormModal = (row: any, action: number) => {
	formConfig.value.show = true
	formConfig.value.id = row.id
	formConfig.value.action = action
}

const onViewTicket = (row: any, action: number) => {
	router.push(`/app/sell/new-service-tickets-detail/${row.id}`)
}
const getTableRowData = async (params: any) => {
	let res = await getListOrder({
		...params,
		pageSize: PAGE_SIZE,
		pageNumber: pagination.value.currentPage,
	})
	rowData.value = res.data
	pagination.value.total = res.totalElement
}

// watch(
// 	() => pagination.value.currentPage,
// 	() => {
// 		let data = {} as any

// 		Object.keys(filterConfig.value).map((field) => {
// 			if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
// 				data[field] =
// 					filterConfig.value[field as keyof typeof filterConfig.value].value
// 			}
// 		})

// 		const params = {
// 			...data,
// 			pageNumber: pagination.value.currentPage,
// 			pageSize: pagination.value.pageSize,
// 		}

// 		getTableRowData(params)
// 	},
// 	{ deep: true }
// )

const changePage = async (val: any) => {
	// pagination.value.currentPage = val.currentPage
	await query({
		currentPage: val.currentPage,
		pageSize: val.pageSize,
	})
}

const addServiceTicket = () => {
	formConfig.value.show = true
	formConfig.value.action = EFormState.ADD
	saveLogTracking({
		logEvent: 'Click_FOR_CREATE_REPAIR_ORDER',
		garageId: localStorage.getItem('garageId'),
		actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
	})
}

await getTableRowData({
	pageSize: pagination.value.pageSize,
	pageNumber: pagination.value.currentPage,
})

emitter.on('on-add-newServiceTicket', () => {
	formConfig.value.show = true
})

onMounted(async () => {
	emitter.on('router-on-set-query', async (params) => {
		pagination.value.currentPage = (
			params as { pageSize: number; currentPage: number }
		).currentPage
		pagination.value.pageSize = (
			params as { pageSize: number; currentPage: number }
		).pageSize

		let data = {} as any

		Object.keys(filterConfig.value).map((field) => {
			if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
				data[field] =
					filterConfig.value[field as keyof typeof filterConfig.value].value
			}
		})

		await getTableRowData({
			...data,
			pageSize: pagination.value.pageSize,
			pageNumber: pagination.value.currentPage,
		})
	})
	if (router.currentRoute.value.query) {
		if (Number(router.currentRoute.value.query.currentPage)) {
			pagination.value.currentPage = Number(
				router.currentRoute.value.query.currentPage
			)
		}
		if (Number(router.currentRoute.value.query.pageSize)) {
			pagination.value.pageSize = Number(
				router.currentRoute.value.query.pageSize
			)
		}
		await getTableRowData({
			pageSize: pagination.value.pageSize,
			pageNumber: pagination.value.currentPage,
		})
	}
})
onBeforeUnmount(() => {
	emitter.off('router-on-set-query')
})

watch(
	() => filterConfig.value,

	debounce(async () => {
		await filter()
	}, 1000),
	{ deep: true }
)

const onRefreshData = async () => {
	pagination.value.currentPage = 1
	await getTableRowData({})
}
</script>
<!-- 
<style lang="scss">
.custome-table {
 
      
 
}
</style> -->
