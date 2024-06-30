<template>
	<div class="flex flex-col gap-4">
		<WrapFlexContainer>
			<div class="flex flex-col w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.customerType') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.customerType.value"
					:placeholder="$t('module.serviceTicket.filter.customerType')"
					:options="filterConfig.customerType.options"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.customerName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.customerName.value"
					:placeholder="$t('module.serviceTicket.filter.customerName')"
					:trim="true"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.phone') }}
				</p>
				<ACCDInputText
					size="md"
					v-model.trin="filterConfig.phone.value"
					:placeholder="$t('module.serviceTicket.filter.phone')"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="flex flex-col w-full lg:w-1/4">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.carBrand') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carBrandId.value"
					:placeholder="$t('module.serviceTicket.filter.carBrand')"
					:options="filterConfig.carBrandId.options"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/4">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.carModel') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carModelId.value"
					:placeholder="$t('module.serviceTicket.filter.carModel')"
					:options="filterConfig.carModelId.options"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/4">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.carYear') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carYearId.value"
					:placeholder="$t('module.serviceTicket.filter.carYear')"
					:options="filterConfig.carYearId.options"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/4">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.carVersion') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carVersionId.value"
					:placeholder="$t('module.serviceTicket.filter.carVersion')"
					:options="filterConfig.carVersionId.options"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="flex flex-col w-full lg:w-1/2">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.licensePlate') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.licensePlate.value"
					:placeholder="$t('module.serviceTicket.filter.licensePlate')"
				/>
			</div>
			<div class="flex flex-col w-full lg:w-1/2">
				<p class="mb-1">
					{{ $t('module.serviceTicket.filter.vinNumber') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.vinNumber.value"
					:placeholder="$t('module.serviceTicket.filter.vinNumber')"
				/>
			</div>
		</WrapFlexContainer>
		<div class="flex justify-end gap-4">
			<ACCDButton
				type="secondary"
				size="md"
				@click="addServiceTicket"
				class="w-1/2 lg:w-1/6"
			>
				<span class="text-base">
					{{ $t('module.serviceTicket.create') }}
				</span>
			</ACCDButton>
			<ACCDButton
				size="md"
				type="primary"
				@click="filter"
				class="w-1/2 lg:w-1/6"
			>
				<span class="text-base">
					{{ $t('module.serviceTicket.find') }}
				</span>
			</ACCDButton>
		</div>
		<div>
			<ACCDTable
				ref="table"
				:columns="columns"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
			>
				<template #cell-index="{ rowIndex }">
					{{ rowIndex + 1 }}
				</template>
				<template #cell-totalPrice="{ row }">
					{{
						row.totalPrice
							? row.totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							: 0
					}}
				</template>
				<template #cell-id="{ row, col, field }"> #{{ row.id }} </template>
				<template #cell-appointmentDate="{ row, col, field }">
					{{ dayjs(row.appointmentDate).format('DD/MM/YYYY') }}
				</template>
				<template #cell-status="{ row, col, field }">
					{{
						$t('module.serviceTicket.status.' + (row.status ? row.status : 0))
					}}
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
									name="fa fa-ellipsis-vertical"
								></ACCDIcon>
							</div>
						</template>
						<div class="rounded-md border w-56 bg-white py-2">
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => onOpenFormModal(row, EFormState.EDIT)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa fa-pen-to-square"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.distributor.edit') }}
								</span>
							</div>
							<hr />
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => onOpenFormModal(row, EFormState.VIEW)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa fa-file-contract"
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
	<ServiceTicketForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:action="formConfig.action"
		:id="formConfig.id"
		@refresh="onRefreshData"
	></ServiceTicketForm>
</template>
<script setup lang="ts">
const { $t } = useI18n()
import { ISelectOption } from '@/types'
import { ref, onMounted, watch } from 'vue'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
} from '@/modules/sharedModules/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { getCustomerTypes } from '@/modules/customerType/api'
import { getCustomersByGarageId } from '@/modules/sellingManagement/api'

import { EFormState } from '@/enums'
import { getListOrder } from '@/modules/serviceTicket/api'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import ServiceTicketForm from '@/modules/serviceTicket/pages/ServiceTicketForm.vue'
import { useI18n } from '@/composables/useI18n'

dayjs.extend(customParseFormat)
const PAGE_SIZE = 10
const filterConfig = ref({
	customerType: {
		value: '',
		options: [] as ISelectOption[],
	},
	carBrandId: {
		value: '',
		options: [] as ISelectOption[],
	},
	carModelId: {
		value: '',
		options: [] as ISelectOption[],
	},
	carYearId: {
		value: '',
		options: [] as ISelectOption[],
	},
	carVersionId: {
		value: '',
		options: [] as ISelectOption[],
	},
	customerName: {
		value: '',
	},
	phone: {
		value: '',
	},

	licensePlate: {
		value: '',
	},
	vinNumber: {
		value: '',
	},
})
const columns = ref([
	{
		key: 'index',
		headerName: $t('module.serviceTicket.table.index'),
	},
	{
		key: 'id',
		headerName: $t('module.serviceTicket.table.code'),
	},
	{
		key: 'customerName',
		headerName: $t('module.serviceTicket.table.customerName'),
	},
	{
		key: 'appointmentDate',
		headerName: $t('module.serviceTicket.table.date'),
	},
	{
		key: 'totalPrice',
		headerName: $t('module.serviceTicket.table.totalCash'),
	},
	{
		key: 'status',
		headerName: $t('module.serviceTicket.table.status'),
	},
	{
		key: 'action',
		headerName: '',
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

watch(
	() => pagination.value.currentPage,
	() => {
		let data = {} as any
		Object.keys(filterConfig.value).map((field) => {
			if (filterConfig.value[field as keyof typeof filterConfig.value].value) {
				data[field] =
					filterConfig.value[field as keyof typeof filterConfig.value].value
			}
		})

		const params = {
			...data,
			pageNumber: pagination.value.currentPage,
			pageSize: pagination.value.pageSize,
		}

		getTableRowData(params)
	},
	{ deep: true }
)
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
const getTableRowData = async (params: any) => {
	let res = await getListOrder({
		...params,
		pageSize: pagination.value.pageSize,
		pageNumber: pagination.value.currentPage,
	})
	rowData.value = res.data
	pagination.value.total = res.totalElement
}

const changePage = (val: any) => {
	pagination.value.currentPage = val.currentPage
}

const addServiceTicket = () => {
	formConfig.value.show = true
	formConfig.value.action = EFormState.ADD
}
onMounted(async () => {
	await getTableRowData({})
	await getCustomerTypes({
		pageSize: 10000,
		pageNumber: 1,
	}).then((res) => {
		filterConfig.value.customerType.options = res.data.map((a: any) => {
			return {
				value: a.id,
				label: a.customerTypeName,
				origin: a,
			}
		})
	})
	await getCarBrandList().then((res) => {
		filterConfig.value.carBrandId.options = res.data.data.map((item: any) => {
			return {
				value: item.id,
				label: item.title,
			}
		})
	})
})

watch(
	() => filterConfig.value.carBrandId.value,
	(newVal: string) => {
		filterConfig.value.carModelId.options = []
		getCarModelList(newVal).then((res) => {
			filterConfig.value.carModelId.options = res.data.data.map((item: any) => {
				return {
					value: item.id,
					label: item.title,
				}
			})
		})
	}
)
watch(
	() => filterConfig.value.carModelId.value,
	(newVal: string) => {
		filterConfig.value.carYearId.options = []
		getCarYearList(newVal).then((res) => {
			filterConfig.value.carYearId.options = res.data.data.map((item: any) => {
				return {
					value: item.id,
					label: item.title,
				}
			})
		})
	}
)
watch(
	() => filterConfig.value.carYearId.value,
	(newVal: string) => {
		filterConfig.value.carVersionId.options = []
		getCarVersionList(newVal).then((res) => {
			filterConfig.value.carVersionId.options = res.data.data.map(
				(item: any) => {
					return {
						value: item.id,
						label: item.title,
					}
				}
			)
		})
	}
)

const onRefreshData = async () => {
	await getTableRowData({})
}
</script>
