<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.debt.serviceTicket.filter.customerGroup') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.customerTypeId.value"
					:options="listCustomerType"
					:placeholder="$t('module.debt.serviceTicket.filter.customerGroup')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.debt.serviceTicket.filter.customerName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.customerName.value"
					:placeholder="$t('module.debt.serviceTicket.filter.customerName')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.debt.serviceTicket.filter.time') }}
				</p>
				<div class="flex gap-4">
					<div class="flex gap-4 items-center">
						<span class="font-semibold">
							{{ $t('module.debt.serviceTicket.filter.from') }}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="filterConfig.fromDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
					<div class="flex gap-4 items-center">
						<span class="font-semibold">
							{{ $t('module.debt.serviceTicket.filter.to') }}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="filterConfig.toDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
				</div>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.debt.serviceTicket.filter.paymentStatus') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.paymentStatus.value"
					:options="filterConfig.paymentStatus.options"
					:placeholder="$t('module.debt.serviceTicket.filter.paymentStatus')"
					class="font-medium"
				/>
			</div>
			<div class="hidden lg:block lg:w-1/3"></div>
			<div
				class="w-full lg:w-1/3 flex flex-col lg:flex-row md:flex-row justify-end gap-4"
			>
				<!-- <div class="flex items-end">
					<ACCDButton type="secondary" class="w-full">
						<div class="flex items-center">
							<ACCDAIcon class="mr-2" name="ExportCurve" size="20"></ACCDAIcon>
							<span>{{ $t('module.debt.serviceTicket.extractExcel') }}</span>
						</div>
					</ACCDButton>
				</div> -->
				<div class="flex items-end">
					<ACCDButton type="primary" class="w-full" @click="filter">
						<span class="text-base">
							{{ $t('module.debt.serviceTicket.find') }}</span
						>
					</ACCDButton>
				</div>
			</div>
		</WrapFlexContainer>
		<div>
			<ACCDTable
				ref="table"
				:columns="columns"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-totalPrice="{ row }">
					{{
						row.totalPrice?.toString()?.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
					}}
				</template>
				<template #cell-index="{ rowIndex }">
					{{ (pagination.currentPage - 1) * 10 + rowIndex + 1 }}
				</template>
				<template #cell-code="{ row }"> #{{ row.code }} </template>
				<template #cell-appointmentDate="{ row, col, field }">
					{{ dayjs(row.appointmentDate).format('DD/MM/YY') }}
				</template>
				<template #cell-paymentStatusTranslated="{ row, col, field }">
					<span
						class="w-40 h-7 rounded flex justify-center items-center"
						:style="paymentStatusColor[row.paymentStatus as keyof typeof paymentStatusColor]"
					>
						{{ row.paymentStatusTranslated }}
					</span>
				</template>
				<template #cell-action="{ row, col, field }">
					<ACCDDropdown>
						<template v-slot:activator="{ props }">
							<div
								class="w-8 cursor-pointer flex justify-center"
								v-bind="props"
							>
								<ACCDIcon
									class="text-base text-black"
									name="fa-solid fa-ellipsis-vertical"
								></ACCDIcon>
							</div>
						</template>
						<div class="rounded-md border w-56 bg-white py-2">
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => handleChangePage(row, EFormState.EDIT)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-pen-to-square"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.distributor.edit') }}
								</span>
							</div>
							<hr />
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => handleChangePage(row, EFormState.VIEW)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-file-contract"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.distributor.view') }}
								</span>
							</div>
						</div>
					</ACCDDropdown>
				</template>
			</ACCDTable>
		</div>
	</div>
	<!-- <serviceTicketForm
        v-if="formConfig.show"
        v-model="formConfig.show"
        :action="formConfig.action"
        :id="formConfig.id"
        @refresh="
            () => {
                refresh({
                    pageSize: 10,
                    pageNumber: 1,
                });
            }
        "
    ></serviceTicketForm> -->
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { getListOrder } from '@/modules/serviceTicket/api'
import dayjs from 'dayjs'
import { EFormState } from '@/enums'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import serviceTicketForm from '@/modules/debt/pages/ServiceTicket/ServiceTicketForm.vue'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { getCustomerTypes } from '@/modules/customerType/api'
import { useI18n } from '@/composables/useI18n'
import { watch } from 'vue'

import useCDRouter from '@/composables/useRouter'
import { emitter } from '@/utils/mitt'
const { router, query } = useCDRouter()
dayjs.extend(customParseFormat)
const paymentStatusColor = {
	'1': {
		backgroundColor: '#FEE2E2',
		color: '#EF4444',
	},
	'2': {
		backgroundColor: '#FEF3C7',
		color: '#F59E0B',
	},
	'3': {
		backgroundColor: '#D1FAE5',
		color: '#10B981',
	},
	'4': {
		backgroundColor: '#DBEAFE',
		color: '#3B82F6',
	},
	'5': {
		backgroundColor: '#E5E7EB',
		color: '#9CA3AF',
	},
}

const PAGE_SIZE = 10
const { $t } = useI18n()
const filterConfig = ref({
	customerTypeId: {
		value: '',
	},
	customerName: {
		value: '',
	},
	fromDate: {
		value: '',
	},
	toDate: {
		value: '',
	},
	paymentStatus: {
		value: '',
		options: [
			{
				value: 1,
				label: $t('module.debt.serviceTicket.paymentStatus.1'),
			},
			// {
			// 	value: 2,
			// 	label: $t('module.debt.serviceTicket.paymentStatus.2'),
			// },
			{
				value: 3,
				label: $t('module.debt.serviceTicket.paymentStatus.3'),
			},
			// {
			// 	value: 4,
			// 	label: $t('module.debt.serviceTicket.paymentStatus.4'),
			// },
			// {
			// 	value: 5,
			// 	label: $t('module.debt.serviceTicket.paymentStatus.5'),
			// },
		],
	},
	paymentDate: {
		value: '',
	},
})
const columns = ref([
	{
		key: 'index',
		headerName: $t('module.debt.serviceTicket.table.index'),
		width: '80px',
	},
	{
		key: 'code',
		headerName: $t('module.debt.serviceTicket.table.orderCode'),
	},
	{
		key: 'customerName',
		headerName: $t('module.debt.serviceTicket.table.customerName'),
	},
	{
		key: 'appointmentDate',
		headerName: $t('module.debt.serviceTicket.table.createdAt'),
	},
	{
		key: 'totalPrice',
		headerName: $t('module.debt.serviceTicket.table.totalPrice'),
	},
	{
		key: 'paymentStatusTranslated',
		headerName: $t('module.debt.serviceTicket.table.paymentStatus'),
	},
	{
		key: 'action',
		headerName: '',
		width: '80px',
	},
])
const formConfig = ref({
	show: false,
	action: EFormState.ADD as number,
	id: '',
})
const rowData = ref([])
const pagination = ref({
	perPage: 10,
	total: 0,
	currentPage: 1,
})
const listCustomerType = ref([])
const onOpenFormModal = (row: any, action: number) => {
	formConfig.value.show = true
	formConfig.value.id = row.id
	formConfig.value.action = action
}

// watch(
// 	() => pagination.value.currentPage,
// 	() => {
// 		let data = {} as any
// 		Object.keys(filterConfig.value).map((field) => {
// 			if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
// 				data[field] =
// 					filterConfig.value[field as keyof typeof filterConfig.value].value
// 				if (field == 'fromDate' || field == 'toDate') {
// 					data[field] = dayjs(
// 						filterConfig.value[field as keyof typeof filterConfig.value].value,
// 						'DD/MM/YY'
// 					).format('YYYY-MM-DD')
// 				}
// 			}
// 		})

// 		const params = {
// 			...data,
// 			pageNumber: pagination.value.currentPage,
// 			pageSize: pagination.value.perPage,
// 		}

// 		getTableRowData(params)
// 	},
// 	{ deep: true }
// )
const filter = () => {
	pagination.value.currentPage = 1
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
		pageNumber: pagination.value.currentPage,
		pageSize: pagination.value.perPage,
	})
}

const refresh = (config: any) => {
	pagination.value.currentPage = config.pageNumber
	pagination.value.perPage = config.pageSize
	getTableRowData(config)
}

const changePage = async (val: any) => {
	// pagination.value.currentPage = val.currentPage
	await query({
		currentPage: val.currentPage,
		pageSize: val.pageSize,
	})
}

const getTableRowData = async (params: any) => {
	let res = await getListOrder({
		...params,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})

	rowData.value = res.data.map((a: any) => {
		return {
			...a,
			paymentStatusTranslated: $t(
				'module.debt.serviceTicket.paymentStatus.' + a.paymentStatus
			),
		}
	})
	pagination.value.total = res.totalElement
}

const handleChangePage = (row: any, type: number) => {
	if (type == EFormState.VIEW) {
		router.push(`/app/debt/service-ticket/view/${row.id}`)
	} else {
		router.push(`/app/debt/service-ticket/edit/${row.id}`)
	}
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
		getTableRowData({
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

onBeforeUnmount(() => {
	emitter.off('router-on-set-query')
})
await getTableRowData({})
await getCustomerTypes({
	pageSize: 10000,
	pageNumber: 1,
}).then((res) => {
	listCustomerType.value = res.data.map((a: any) => {
		return {
			value: a.id,
			label: a.customerTypeName,
		}
	})
})
</script>
