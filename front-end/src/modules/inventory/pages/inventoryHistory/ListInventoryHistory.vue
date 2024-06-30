<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer justify="between">
			<div class="w-full">
				<div class="flex gap-4">
					<div class="flex flex-col gap-4 lg:w-1/2">
						<span class="font-semibold">
							{{
								$t('module.inventory.inventoryHistory.filterConfig.from')
							}}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="filterConfig.fromDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
					<div class="flex flex-col gap-4 lg:w-1/2">
						<span class="font-semibold">
							{{
								$t('module.inventory.inventoryHistory.filterConfig.to')
							}}</span
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

		<div class="flex justify-end gap-4 w-full">
			<div>
				<ACCDButton type="primary" class="w-full" @click="addInventoryNew">
					<div class="text-base">
						<span>{{ $t('module.inventory.inventoryHistory.create') }} </span>
					</div>
				</ACCDButton>
			</div>
			<!-- <div class="w-full">
                    <ACCDButton
                        type="secondary"
                        size="md"
                        class="w-full"
                        @click="addInventory"
                    >
                        <div class="text-base">
                            <span>{{
                                $t("module.inventory.inventoryHistory.create")
                            }}</span>
                        </div>
                    </ACCDButton>
                </div> -->
			<!-- <div>
				<ACCDButton type="primary" class="w-full" @click="filter">
					<div class="text-base">
						<span>{{ $t('module.inventory.inventoryHistory.filter') }}</span>
					</div>
				</ACCDButton>
			</div> -->
		</div>
		<div>
			<TableContainer>
				<ACCDTable
					ref="table"
					:columns="columns"
					:rowData="rowData"
					:pagination="pagination"
					@changePage="changePage"
					class="table-fixed-layout"
				>
					<template #cell-countDate="{ row }">
						{{ dayjs(row.countDate).format('DD/MM/YY') }}
					</template>
					<template #cell-index="{ rowIndex }">
						{{ rowIndex + 1 }}
					</template>
					<template #cell-action="{ row, col, field }">
						<div
							class="flex px-4 py-2 gap-3 cursor-pointer"
							@click="() => handleDetailInventoryHistory(row)"
						>
							<ACCDAIcon class="text-gray-700" name="Edit" siz="20"></ACCDAIcon>
						</div>
						<!-- <ACCDDropdown>
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
								@click="() => handleDetailInventoryHistory(row)"
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
					</ACCDDropdown> -->
					</template>
				</ACCDTable>
			</TableContainer>
		</div>
	</div>
	<InventoryHistoryForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:action="formConfig.actions"
		:id="formConfig.id"
		@refresh="() => getTableRowData({})"
	>
	</InventoryHistoryForm>
	<InventoryHistoryFormNew
		v-if="formConfigNew.show"
		v-model="formConfigNew.show"
		:action="formConfigNew.actions"
		:id="formConfigNew.id"
		@refresh="() => getTableRowData({})"
	>
	</InventoryHistoryFormNew>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import InventoryHistoryForm from './InventoryHistoryForm.vue'
import InventoryHistoryFormNew from './InventoryHistoryFormNew.vue'

import { EFormState } from '@/enums'
import { instanceI18n } from '@/main.js'
import { getListInventoryHistory } from '@/modules/inventory/api'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'
import { useI18n } from '@/composables/useI18n'
import { cloneDeep, debounce } from 'lodash'
import { watch } from 'vue'

import { emitter } from '@/utils/mitt'
import useCDRouter from '@/composables/useRouter'

const { router, query } = useCDRouter()

dayjs.extend(customParseFormat)
const PAGE_SIZE = 10

const { $t } = useI18n()
const filterConfig = ref({
	fromDate: {
		value: '',
	},
	toDate: {
		value: '',
	},
})
const formConfig = ref({
	show: false,
	actions: EFormState.ADD,
	id: '',
})

const formConfigNew = ref({
	show: false,
	actions: EFormState.ADD,
	id: '',
})
const pagination = ref({
	perPage: 10,
	total: 0,
	currentPage: 1,
})
const columns = ref([
	{
		key: 'index',
		headerName: $t('module.inventory.inventoryHistory.table.index'),
		width: '80px',
	},
	{
		key: 'countDate',
		headerName: $t('module.inventory.inventoryHistory.table.countDate'),
		minWidth: '100px',
	},
	{
		key: 'countersName',
		headerName: $t('module.inventory.inventoryHistory.table.counterName'),
		minWidth: '200px',
	},
	{
		key: 'approverName',
		headerName: $t('module.inventory.inventoryHistory.table.approverName'),
		minWidth: '200px',
	},
	{
		key: 'errorRate',
		headerName: $t('module.inventory.inventoryHistory.table.deviationRate'),
		width: '150px',
	},
	{
		key: 'action',
		headerName: $t('module.inventory.inventoryHistory.table.action'),
		width: '100px',
	},
])
const rowData = ref([])
onMounted(() => {
	emitter.on('router-on-set-query', async (params) => {
		pagination.value.currentPage = (
			params as { pageSize: number; currentPage: number }
		).currentPage
		pagination.value.perPage = (
			params as { pageSize: number; currentPage: number }
		).pageSize

		getTableRowData(params)
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

watch(
	() => pagination.value.currentPage,
	() => {
		const params = {
			fromDate: filterConfig.value.fromDate.value
				? dayjs(filterConfig.value.fromDate.value, 'DD/MM/YY').format(
						'YYYY-MM-DD'
				  )
				: null,
			toDate: filterConfig.value.toDate.value
				? dayjs(filterConfig.value.toDate.value, 'DD/MM/YY').format(
						'YYYY-MM-DD'
				  )
				: null,
			pageNumber: pagination.value.currentPage,
			pageSize: pagination.value.perPage,
		}

		getTableRowData(params)
	},
	{ deep: true }
)

const filter = () => {
	pagination.value.currentPage = 1
	let data = {
		pageSize: PAGE_SIZE,
	} as any
	let filterConfigClone = cloneDeep(filterConfig.value)
	Object.keys(filterConfigClone).map((a) => {
		if (!filterConfigClone[a].value) {
			delete filterConfigClone[a]
		}
	})
	Object.keys(filterConfigClone).map((field) => {
		if (field == 'fromDate' || field == 'toDate') {
			data[field] = dayjs().format('YYYY-MM-DD')
		}
		if (filterConfigClone[field as keyof typeof filterConfigClone].value) {
			data[field] =
				filterConfigClone[field as keyof typeof filterConfigClone].value
			if (field == 'fromDate' || field == 'toDate') {
				data[field] = dayjs(
					filterConfigClone[field as keyof typeof filterConfigClone].value,
					'DD/MM/YY'
				).format('YYYY-MM-DD')
			}
		}
	})

	getTableRowData({ ...data })
}
const addInventory = () => {
	formConfig.value.actions = EFormState.ADD
	formConfig.value.show = true
}

const addInventoryNew = () => {
	formConfigNew.value.actions = EFormState.ADD
	formConfigNew.value.show = true
}
const changePage = async (val: any) => {
	// pagination.value.currentPage = val.currentPage
	await query({
		currentPage: val.currentPage,
		pageSize: val.pageSize,
	})
}

const getTableRowData = (params: any) => {
	getListInventoryHistory({
		...params,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	}).then((result) => {
		rowData.value = result.data.map((a: any, index: any) => {
			a.approverName = JSON.parse(a.approvers)
			a.countersName = JSON.parse(a.countersName)

			return {
				...a,
				errorRate: a.errorRate * 100 + '%',
				countersName: (() => {
					let result = ''
					if (a.countersName && Array.isArray(a.countersName)) {
						a.countersName.map((e: any, i: number) => {
							if (e.counterName) {
								result += (i ? ', ' : ' ') + e.counterName
							}
						})
					}
					return result
				})(),

				approverName: (() => {
					let result = ''
					if (a.approverName && Array.isArray(a.approverName)) {
						a.approverName.map((e: any, i: number) => {
							if (e.approverName) {
								result += (i ? ', ' : ' ') + e.approverName
							}
						})
					}
					return result
				})(),
			}
		})
		pagination.value.total = result.totalElement
	})
}
const onOpenFormModal = (row: any, action: number) => {
	// formConfig.value.id = row.id;
	// formConfig.value.actions = action;
	// formConfig.value.show = true;

	formConfigNew.value.id = row.id
	formConfigNew.value.actions = action
	formConfigNew.value.show = true
}

const handleDetailInventoryHistory = (row: any) => {
	router.push({
		path: `/app/inventory/histories/detail/${row.id}`,
	})
}

await getTableRowData({})

watch(
	() => filterConfig.value,
	debounce(() => {
		filter()
	}, 500),
	{ deep: true }
)
</script>
