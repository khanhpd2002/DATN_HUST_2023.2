<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.distributorName') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.distributorId.value"
					:options="filterConfig.distributorId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.accessary.placeholder.distributorCode')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.name') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.name.value"
					:placeholder="$t('module.accessary.placeholder.name')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.sparePartType') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.sparePartType.value"
					:options="filterConfig.sparePartType.options"
					:placeholder="$t('module.accessary.placeholder.sparePartType')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.carBrand') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carBrandId.value"
					:options="filterConfig.carBrandId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.accessary.placeholder.carBrand')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.carModel') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carModelId.value"
					:options="filterConfig.carModelId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.accessary.placeholder.carModel')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">{{ $t('module.car.carYear') }}</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carYearId.value"
					:options="filterConfig.carYearId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.accessary.placeholder.carYear')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.accessary.carVersion') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carVersionId.value"
					:options="filterConfig.carVersionId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.accessary.placeholder.carVersion')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<div class="mb-4 flex gap-4">
			<div class="hidden lg:block w-1/4"></div>
			<div class="hidden lg:block w-1/4"></div>
			<div class="hidden lg:block w-1/4"></div>
		</div>
		<WrapFlexContainer justify="end">
			<div>
				<ACCDButton
					size="md"
					type="secondary"
					variant="fill"
					@click="openModalUploadExcel"
				>
					<ACCDAIcon class="mr-2" name="ExportCurve" size="20"></ACCDAIcon>
					<span class="text-info-base font-medium">{{
						$t('module.car.action.uploadExcel')
					}}</span>
				</ACCDButton>
			</div>
			<div>
				<ACCDButton
					size="md"
					type="secondary"
					variant="fill"
					@click="
						() => {
							onOpenFormModal()
						}
					"
				>
					<span class="text-info-base font-medium">{{
						$t('module.accessary.titleCreate')
					}}</span>
				</ACCDButton>
			</div>
			<div>
				<ACCDButton
					@click="filter"
					size="md"
					type="primary"
					variant="fill"
					class="w-full"
				>
					<span class="text-white font-medium">{{
						$t('module.car.action.search')
					}}</span>
				</ACCDButton>
			</div>
		</WrapFlexContainer>
		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:tableName="tableName"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-index="{ rowIndex }">
					{{ (pagination.currentPage - 1) * 10 + rowIndex + 1 }}
				</template>
				<template #cell-action="{ row, col, field }">
					<ACCDAIcon
						name="Edit"
						class="cursor-pointer"
						size="20"
						@click="
							() => {
								onOpenFormModal(row.id, row, EFormState.EDIT)
							}
						"
					></ACCDAIcon>
				</template>
				<template #cell-purchasePrice="{ row }">
					{{
						row.purchasePrice
							? row.purchasePrice
									.toString()
									.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							: ''
					}}
				</template>
			</ACCDTable>
		</div>
		<AccessaryForm ref="AccessaryFormRef" @refresh="onRefreshData" />
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

<script lang="ts">
import AccessaryForm from '@/modules/accessary/pages/AccessaryForm.vue'
import { getAccessaries } from '@/modules/accessary/api'
// import { IAccessary} from "@/types";
import { IAccessary, IAccessaryInfo, ICarInfo } from '@/types'
import { EFormState } from '@/enums'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'
import { getListDistributors } from '@/modules/distributor/api'
import { defineComponent } from 'vue'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
} from '@/modules/sharedModules/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'

const PAGE_SIZE = 10

export default defineComponent({
	components: {
		AccessaryForm,
		WrapFlexContainer,
		ModalUploadExcel,
	},
	async setup() {
		const initAccessariesDataRes = await getAccessaries({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
		return { initAccessariesDataRes }
	},
	computed: {
		rowData(): IAccessary[] {
			return (
				this.listItem?.map((item: IAccessary, index: number) => {
					return {
						...item,
						sparePartTypeVn: item.sparePartType
							? this.$t(
									`module.accessary.sparePartTypeOptions.${item.sparePartType}`
							  )
							: '',
					}
				}) || []
			)
		},
	},
	data() {
		return {
			PAGE_SIZE: PAGE_SIZE,
			listItem: [] as any,
			EFormState: EFormState,
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			listAccessarys: [] as any,
			tableName: this.$t('module.customerType.tableName'),
			columnData: [
				{
					key: 'index',
					headerName: this.$t('module.accessary.index'),
					width: '80px',
				},
				{
					key: 'distributorCode',
					headerName: this.$t('module.accessary.distributorCode'),
				},
				{
					key: 'code',
					headerName: this.$t('module.accessary.code'),
					maxLength: 20,
				},
				{
					key: 'name',
					headerName: this.$t('module.accessary.name'),
				},
				{
					key: 'unit',
					headerName: this.$t('module.accessary.unit'),
				},
				{
					key: 'purchasePrice',
					headerName: this.$t('module.accessary.purchasePrice'),
				},
				{
					key: 'sparePartTypeVn',
					headerName: this.$t('module.accessary.sparePartType'),
				},
				{
					key: 'action',
					headerName: this.$t(''),
					align: 'end',
					width: '100px',
				},
			],
			filterConfig: {
				distributorId: {
					value: 0,
					options: [],
				},
				sparePartType: {
					value: 0,
					options: [],
				},
				code: {
					value: '',
				},
				name: {
					value: '',
				},
				carBrandId: {
					value: 0,
					options: [],
				},
				carModelId: {
					value: 0,
					options: [],
				},
				carYearId: {
					value: 0,
					options: [],
				},
				carVersionId: {
					value: 0,
					options: [],
				},
			} as any,
			contextActions: [
				// {
				//   icon: "EllipsisVerticalIcon",
				//   name: this.$t(
				//       "module.accessary.action.view"
				//   ),
				//   action: (params: any) =>
				//       this.onOpenFormModal(params.id, params, EFormState.VIEW),
				// },
				{
					icon: 'EllipsisVerticalIcon',
					name: this.$t('module.accessary.action.edit'),
					action: (params: any) =>
						this.onOpenFormModal(params.id, params, EFormState.EDIT),
				},
			],
		}
	},
	created() {
		for (let i = 1; i <= 6; i++) {
			this.filterConfig.sparePartType.options.push({
				label: this.$t(`module.accessary.sparePartTypeOptions.${i}`),
				value: i,
			})
		}
		this.calculateCarFeatureOption()

		this.listItem = this.initAccessariesDataRes.data || []
		this.pagination.total = this.initAccessariesDataRes.totalElement
		// this.getTableRowData({
		//     pageSize: PAGE_SIZE,
		//     pageNumber: 1,
		// });
		this.getListDistributors()
	},
	watch: {
		'filterConfig.carBrandId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filterConfig.carModelId.options = []
					getCarModelList(newVal).then((res) => {
						this.filterConfig.carModelId.options = res.data.data.map(
							(item: ICarInfo) => {
								return {
									value: item.id,
									label: item.title,
								}
							}
						)
					})
				}
			},
		},
		'filterConfig.carModelId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filterConfig.carYearId.options = []
					getCarYearList(newVal).then((res) => {
						this.filterConfig.carYearId.options = res.data.data.map(
							(item: ICarInfo) => {
								return {
									value: item.id,
									label: item.title,
								}
							}
						)
					})
				}
			},
		},
		'filterConfig.carYearId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filterConfig.carVersionId.options = []
					getCarVersionList(newVal).then((res) => {
						this.filterConfig.carVersionId.options = res.data.data.map(
							(item: ICarInfo) => {
								return {
									value: item.id,
									label: item.title,
								}
							}
						)
					})
				}
			},
		},

		'pagination.currentPage': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					let data = {} as any
					Object.keys(this.filterConfig).map((field) => {
						if (this.filterConfig[field].value) {
							data[field] = this.filterConfig[field].value
						}
					})
					this.getTableRowData({
						...data,
						pageSize: this.pagination.perPage,
						pageNumber: this.pagination.currentPage,
					})
				}
			},
		},
	},
	methods: {
		openModalUploadExcel() {
			const inventoryId = localStorage.getItem('inventoryId')

			this.$refs.ModalUploadExcelRef.open({
				templateUrl: 'download-template/product',
				templateName: 'product_template.xlsx',
				submitUrl: `import-excels/${inventoryId}/products`,
			})
		},
		onRefreshData() {
			this.listItem = []
			this.getTableRowData({
				id: 1,
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		},
		onOpenFormModal(id?: number, params?: IAccessary, state?: EFormState) {
			;(
				this.$refs.AccessaryFormRef as InstanceType<typeof AccessaryForm>
			).openDialog(id, params, state)
		},
		filter() {
			this.pagination.currentPage = 1
			let data = {} as any
			Object.keys(this.filterConfig).map((field) => {
				if (this.filterConfig[field].value) {
					data[field] = this.filterConfig[field].value
				}
			})

			this.getTableRowData({
				...data,
				pageSize: this.pagination.perPage,
				pageNumber: this.pagination.currentPage,
			})
		},
		changePage(val: any) {
			this.pagination.currentPage = val.currentPage
		},
		async getTableRowData(params: any) {
			const result = await getAccessaries(params)
			this.listItem = result.data || []
			this.pagination.total = result.totalElement

			this.listAccessarys = result.data
		},
		async getAccessaryFeatureOption(data: any) {
			const dataRes = {
				carBrands: [],
				carModels: [],
				carYears: [],
				carVersions: [],
			}
			const carBrandId = data.carBrandId
			const carModelId = data.carModelId
			const carYearId = data.carYearId
			let res: any
			if (carBrandId == 0) {
				res = await getCarBrandList()
				dataRes.carBrands = res.data.data
			} else {
				res = await getCarModelList(carBrandId)
				dataRes.carModels = res.data.data
				if (carModelId == 0) {
					res = await getCarModelList(carModelId)
					dataRes.carYears = res.data.data
				} else {
					res = await getCarYearList(carModelId)
					dataRes.carYears = res.data.data
					if (carYearId != 0) {
						res = await getCarVersionList(carYearId)
						dataRes.carVersions = res.data.data
					}
				}
			}
			return dataRes
		},
		async calculateCarFeatureOption() {
			let res: any = await this.getAccessaryFeatureOption({
				carBrandId:
					this.filterConfig.carBrandId.value == 0
						? 0
						: this.filterConfig.carBrandId.value,
				carModelId:
					this.filterConfig.carModelId.value == 0
						? 0
						: this.filterConfig.carModelId.value,
				carYearId:
					this.filterConfig.carYearId.value == 0
						? 0
						: this.filterConfig.carYearId.value,
				carVersionId:
					this.filterConfig.carVersionId.value == 0
						? 0
						: this.filterConfig.carVersionId.value,
			})
			if (this.filterConfig.carBrandId.value == 0) {
				this.filterConfig.carBrandId.options = res.carBrands.map((a: any) => {
					return {
						label: a.title,
						value: a.id,
					}
				})
			} else {
				this.filterConfig.carModelId.options = res.carModels.map((a: any) => {
					return {
						label: a.title,
						value: a.id,
					}
				})
				this.filterConfig.carYearId.options = res.carYears.map((a: any) => {
					return {
						label: a.title,
						value: a.id,
					}
				})
				this.filterConfig.carVersionId.options = res.carVersions.map(
					(a: any) => {
						return {
							label: a.title,
							value: a.id,
						}
					}
				)
			}
		},
		async getListDistributors() {
			const res = await getListDistributors({
				pageSize: 100000,
				pageNumber: 1,
			})
			this.filterConfig.distributorId.options = res.data.map((a: any) => {
				return {
					label: `${a.code} - ${a.name}`,
					value: a.id,
				}
			})
		},
	},
})
</script>
