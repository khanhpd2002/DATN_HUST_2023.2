<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.distributor.code') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.code.value"
					:placeholder="$t('module.distributor.code')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.distributor.name') }}
				</p>
				<ACCDInputText
					v-model="filterConfig.name.value"
					:placeholder="$t('module.distributor.name')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.distributor.province') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.provinceId.value"
					:options="filterConfig.provinceId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.distributor.province')"
					class="font-medium"
					@update:modelValue="() => handleChangeProvine()"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.distributor.district') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.districtId.value"
					:options="filterConfig.districtId.options"
					:placeholder="$t('module.distributor.district')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>

		<div class="flex justify-between items-end">
			<div>
				<p class="mb-1 font-semibold">
					{{ $t('module.distributor.ward') }}
				</p>
				<ACCDSelect
					size="md"
					@update:modelValue="calculateAdressOption"
					v-model="filterConfig.wardId.value"
					:options="filterConfig.wardId.options"
					:placeholder="$t('module.distributor.ward')"
					class="font-medium"
				/>
			</div>

			<div class="flex gap-3">
				<div>
					<ACCDButton
						size="md"
						variant="fill"
						type="secondary"
						@click="openModalUploadExcel"
					>
						<ACCDAIcon name="ExportCurve" size="20" class="mr-3" />
						<span class="font-medium">{{
							$t('module.distributor.uploadExcel')
						}}</span>
					</ACCDButton>
				</div>
				<div>
					<ACCDButton
						size="md"
						:disabled="false"
						type="secondary"
						class="w-full"
						@click="
							() => {
								onOpenFormModal(undefined, EFormState.ADD)
							}
						"
					>
						<span class="font-medium">{{
							$t('module.distributor.createNewListDistributor')
						}}</span>
					</ACCDButton>
				</div>
				<div>
					<ACCDButton
						:disabled="false"
						@click="filter"
						size="md"
						type="primary"
					>
						<span class="text-white font-medium">{{
							$t('module.distributor.find')
						}}</span>
					</ACCDButton>
				</div>
			</div>
		</div>

		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:tableName="$t('module.distributor.title')"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-action="{ row, col, field }">
					<ACCDDropdown>
						<template v-slot:activator="{ props }">
							<div
								class="w-8 cursor-pointer flex justify-center p-4"
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
									name="fa-solid fa-pen-to-square"
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
		<DistributorForm ref="DistributorFormRef" @refresh="onRefreshData" />
		<ModalUploadExcel
			ref="ModalUploadExcelRef"
			@success="
				getTableRowData({
					pageSize: PAGE_SIZE,
					pageNumber: 1,
				})
			"
		/>
	</div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, watch } from 'vue'
import {
	getListDistributors,
	getProvinceList,
	getDistrictList,
	getWardList,
} from '@/modules/distributor/api'
import { EFormState } from '@/enums'
import { IDistributor } from '@/types'
import DistributorForm from '@/modules/distributor/pages/DistributorForm.vue'
import { useI18n } from '@/composables/useI18n'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'

const { $t } = useI18n()
const PAGE_SIZE = 10

const listItem = ref([])

const rowData = computed(() => {
	return (
		listItem?.value?.map((item: IDistributor, index: number) => {
			return {
				...item,
				address: [
					item.address,
					item.wardName,
					item.districtName,
					item.provinceName,
				]
					.filter((item) => item != undefined && item != '')
					.join(', ')
					.trim(),
				order: index + 1 + PAGE_SIZE * (pagination.value.currentPage - 1),
			}
		}) || ([] as IDistributor[])
	)
})
const pagination = ref({
	perPage: PAGE_SIZE,
	total: 0,
	currentPage: 1,
})
const columnData = computed(() => {
	return [
		{
			key: 'order',
			headerName: $t('module.distributor.order'),
			width: '80px',
		},
		{
			key: 'code',
			headerName: $t('module.distributor.code'),
		},
		{
			key: 'name',
			headerName: $t('module.distributor.name'),
		},
		{
			key: 'address',
			headerName: $t('module.distributor.address'),
		},
		{
			key: 'contactName',
			headerName: $t('module.distributor.contactName'),
		},
		{
			key: 'contactPhone',
			headerName: $t('module.distributor.contactPhone'),
		},
		{
			key: 'action',
			headerName: '',
			width: '80px',
		},
	]
})
const filterConfig = ref<any>({
	code: {
		value: '',
	},
	name: {
		value: '',
	},
	provinceId: {
		value: 0,
		options: [],
	},
	districtId: {
		value: 0,
		options: [],
	},
	wardId: {
		value: 0,
		options: [],
	},
})
const contextActions = computed(() => {
	return [
		{
			icon: 'EllipsisVerticalIcon',
			name: $t('module.sellingManagement.customer.action.view'),
			action: (params: any) => onOpenFormModal(params, EFormState.VIEW),
		},
		{
			icon: 'EllipsisVerticalIcon',
			name: $t('module.product.editAction'),
			action: (params: any) => onOpenFormModal(params, EFormState.EDIT),
		},
		{
			icon: 'EllipsisVerticalIcon',
			name: $t('module.distributor.deleteAction'),
			action: (params: any) => onOpenFormModal(params, EFormState.DELETE),
		},
	]
})
const onRefreshData = () => {
	listItem.value = []
	getTableRowData({
		pageSize: PAGE_SIZE,
		pageNumber: 1,
	})
}
const DistributorFormRef = ref<InstanceType<typeof DistributorForm>>()
const onOpenFormModal = (params?: IDistributor, state?: EFormState) => {
	DistributorFormRef.value?.openDialog(params, state)
}

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

const filter = () => {
	pagination.value.currentPage = 1
	let data = {
		pageSize: PAGE_SIZE,
	} as any
	Object.keys(filterConfig.value).map((field) => {
		if (filterConfig.value[field]?.value) {
			data[field] = filterConfig.value[field]?.value
		}
	})
	getTableRowData({
		...data,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})
}
const getAddressInfo = async (data: any) => {
	const dataRes = {
		province: [],
		district: [],
		ward: [],
	}
	const provinceId = data.provinceId
	const districtId = data.districtId
	let res: any
	if (provinceId == 0) {
		res = await getProvinceList()
		dataRes.province = res.data.data
	} else {
		res = await getDistrictList(provinceId)
		dataRes.district = res.data.data
		if (districtId != 0) {
			res = await getWardList(districtId)
			dataRes.ward = res.data.data
		}
	}
	return dataRes
}
const changePage = (val: any) => {
	pagination.value.currentPage = val.currentPage
}
const getTableRowData = async (params: any) => {
	const result = await getListDistributors(params)
	listItem.value = result.data || []
	pagination.value.total = result.totalElement
}
const calculateAdressOption = async () => {
	console.log('filterConfig', filterConfig.value)
	let res: any = await getAddressInfo({
		provinceId:
			filterConfig.value.provinceId.value == 0
				? 0
				: filterConfig.value.provinceId.value,
		districtId:
			filterConfig.value.districtId.value == 0
				? 0
				: filterConfig.value.districtId.value,
	})
	if (filterConfig.value.provinceId.value == 0) {
		filterConfig.value.provinceId.options = res.province.map((a: any) => {
			return {
				label: a.name,
				value: a.id,
			}
		})
	} else {
		filterConfig.value.districtId.options = res.district.map((a: any) => {
			return {
				label: a.name,
				value: a.id,
			}
		})
		filterConfig.value.wardId.options = res.ward.map((a: any) => {
			return {
				label: a.name,
				value: a.id,
			}
		})
	}
}
watch(
	() => filterConfig.value.provinceId.value,
	() => {
		calculateAdressOption()
	}
)
watch(
	() => filterConfig.value.districtId.value,
	() => {
		calculateAdressOption()
	}
)
await Promise.all([
	calculateAdressOption(),
	getTableRowData({
		pageSize: PAGE_SIZE,
		pageNumber: 1,
	}),
])
const ModalUploadExcelRef = ref()
const openModalUploadExcel = () => {
	const garageId = localStorage.getItem('garageId')
	ModalUploadExcelRef?.value?.open({
		templateUrl: 'download-template/distributor',
		templateName: 'distributor_template.xlsx',
		submitUrl: `import-excels/${garageId}/distributors`,
	})
}

const handleChangeProvine = () => {
	filterConfig.value.districtId.value = ''
	filterConfig.value.districtId.options = []
	filterConfig.value.wardId.value = ''
	filterConfig.value.wardId.options = []
}
</script>
