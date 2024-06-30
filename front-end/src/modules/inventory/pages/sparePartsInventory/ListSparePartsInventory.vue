<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{
						$t('module.inventory.sparePartsInventory.filterConfig.productType')
					}}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.sparePartType.value"
					:placeholder="
						$t('module.inventory.sparePartsInventory.filterConfig.productType')
					"
					:options="filterConfig.sparePartType.options"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.inventory.sparePartsInventory.filterConfig.code') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.code.value"
					:placeholder="
						$t('module.inventory.sparePartsInventory.filterConfig.code')
					"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{
						$t('module.inventory.sparePartsInventory.filterConfig.productName')
					}}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.name.value"
					:placeholder="
						$t('module.inventory.sparePartsInventory.filterConfig.productName')
					"
					class="font-medium"
					:trim="true"
				/>
			</div>
		</WrapFlexContainer>
		<div>
			<div class="flex gap-4 justify-end">
				<div>
					<ACCDButton
						type="secondary"
						variant="fill"
						class="bg-info-secondary w-full items-center"
						@click="exportExcel"
					>
						<ACCDAIcon class="mr-2" name="ExportCurve" size="20"></ACCDAIcon>
						<span class="text-info-base font-medium">{{
							$t('module.inventory.sparePartsInventory.exportExcel')
						}}</span></ACCDButton
					>
				</div>
				<div>
					<ACCDButton
						type="primary"
						variant="fill"
						@click="filter"
						class="bg-info-secondary border-none w-full"
					>
						<span class="text-info-base font-medium">{{
							$t('module.inventory.sparePartsInventory.find')
						}}</span></ACCDButton
					>
				</div>
			</div>
		</div>
		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-index="{ rowIndex }">
					{{ rowIndex + 1 }}
				</template>
				<template #cell-sparePartType="{ row, col, field }">
					{{
						row.sparePartType
							? $t(
									'module.inventory.sparePartsInventory.sparePartTypeOptions.' +
										row.sparePartType
							  )
							: ''
					}}
				</template>
			</ACCDTable>
		</div>
	</div>
	<UpdateForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:id="formConfig.id"
		@refresh="() => getTableRowData({})"
	>
	</UpdateForm>
	<ModalUploadExcel
		ref="ModalUploadExcelRef"
		@success="
			getTableRowData({
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		"
	/>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ISelectOption } from '@/types'
import UpdateForm from './SparePartsInventoryForm.vue'
import { getListSparePartsInventory } from '@/modules/inventory/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { useI18n } from '@/composables/useI18n'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'
import { exportExcelFile } from '@/modules/sharedModules/api'
import { exportFileExcel } from '@/utils/file'
import { useToast } from '@/composables/useToast'
const { $t } = useI18n()

const PAGE_SIZE = 10
const { $toast } = useToast()
const filterConfig = ref({
	sparePartType: {
		value: '',
		options: [
			{
				value: 1,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.1'
				),
			},
			{
				value: 2,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.2'
				),
			},
			{
				value: 3,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.3'
				),
			},
			{
				value: 4,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.4'
				),
			},
			{
				value: 5,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.5'
				),
			},
			{
				value: 6,
				label: $t(
					'module.inventory.sparePartsInventory.sparePartTypeOptions.6'
				),
			},
		] as ISelectOption[],
	},
	code: {
		value: '',
	},
	name: {
		value: '',
	},
})
const formConfig = ref({
	show: false,
	id: '',
})

const columnData = ref([
	{
		key: 'index',
		headerName: $t('module.inventory.sparePartsInventory.table.index'),
		width: '100px',
	},
	{
		key: 'code',
		headerName: $t('module.inventory.sparePartsInventory.table.code'),
		minWidth: '100px',
	},
	{
		key: 'name',
		headerName: $t('module.inventory.sparePartsInventory.table.productName'),
		minWidth: '200px',
	},
	{
		key: 'unit',
		headerName: $t('module.inventory.sparePartsInventory.table.unit'),
		minWidth: '100px',
	},
	{
		key: 'sparePartType',
		headerName: $t('module.inventory.sparePartsInventory.table.sparePartType'),
		width: '300px',
	},
	{
		key: 'quantity',
		headerName: $t('module.inventory.sparePartsInventory.table.quantity'),
		minWidth: '100px',
	},
])
const rowData = ref([])
const pagination = ref({
	perPage: 10,
	total: 0,
	currentPage: 1,
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
			pageSize: pagination.value.perPage,
		}

		getTableRowData(params)
	},
	{ deep: true }
)

const changePage = (val: any) => {
	pagination.value.currentPage = val.currentPage
}
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
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})
}
const updateQuantity = (row: any) => {
	formConfig.value.id = row.id
	formConfig.value.show = true
}

const getTableRowData = async (params: any) => {
	const result = await getListSparePartsInventory({
		...params,
		type: 1,
		pageSize: PAGE_SIZE,
	})
	rowData.value = result.data
	pagination.value.total = result.totalElement
}

const loadingDownloadTemplate = ref(false)
const exportExcel = async () => {
	loadingDownloadTemplate.value = true
	const inventoryId = localStorage.getItem('inventoryId')
	const exportUrl = `export-excels/${inventoryId}/products`
	const result = await exportExcelFile(exportUrl)
	if (!result?.error) {
		exportFileExcel(result.data, 'tonkho.xlsx')
	} else {
		$toast('Có lỗi xảy ra', false)
	}
	loadingDownloadTemplate.value = false
}

await getTableRowData({ pageSize: PAGE_SIZE, pageNumber: 1 })
</script>
