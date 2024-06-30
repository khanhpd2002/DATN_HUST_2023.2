<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.car.filter') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.filter.value"
					:placeholder="$t('module.car.placeholder.filter')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/2">
				<p class="mb-1 font-semibold">
					{{ $t('module.car.customerTypeName') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.customerTypeId.value"
					:options="filterConfig.customerTypeId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.car.placeholder.customerTypeName')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">{{ $t('module.car.carBrand') }}</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carBrandId.value"
					:options="filterConfig.carBrandId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.car.placeholder.carBrand')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">{{ $t('module.car.carModel') }}</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carModelId.value"
					:options="filterConfig.carModelId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.car.placeholder.carModel')"
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
					:placeholder="$t('module.car.placeholder.carYear')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">{{ $t('module.car.carVersion') }}</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.carVersionId.value"
					:options="filterConfig.carVersionId.options"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.car.placeholder.carVersion')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
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
					type="primary"
					variant="fill"
					class="w-full"
					@click="
						() => {
							onOpenFormModal()
						}
					"
				>
					<span class="text-info-base font-medium">{{
						$t('module.car.action.add')
					}}</span>
				</ACCDButton>
			</div>
		</WrapFlexContainer>

		<TableContainer>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:tableName="tableName"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-stt="{ row, rowIndex, col, field }">
					{{ rowIndex + 1 }}
				</template>
				<template #cell-action="{ row, col, field }">
					<ACCDDropdown class="flex justify-center">
						<template v-slot:activator="{ props }">
							<div class="cursor-pointer flex justify-center" v-bind="props">
								<img
									src="@/assets/images/svg/edit.svg"
									class="cursor-pointer"
								/>
							</div>
						</template>
						<div class="rounded-md border w-56 bg-white py-2">
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => onOpenFormModal(row.id, row, EFormState.EDIT)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-pen-to-square"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.car.action.edit') }}
								</span>
							</div>
							<hr />
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => redirectCarDetail(row.id, EFormState.VIEW)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-file-contract"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.car.action.view') }}
								</span>
							</div>
						</div>
					</ACCDDropdown>
				</template>
			</ACCDTable>
		</TableContainer>

		<CarForm ref="CarFormRef" @refresh="onRefreshData" v-if="showModal" />
		<ModalUploadExcel ref="ModalUploadExcelRef" @success="onRefreshData" />
	</div>
</template>

<script lang="ts">
import { getCars } from '@/modules/car/api'
import CarForm from '@/modules/car/pages/CarForm.vue'
import { ICar, ICarInfo } from '@/types'
import { EFormState } from '@/enums'
import { defineComponent, watch } from 'vue'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
} from '@/modules/sharedModules/api'
import { getCustomerTypes } from '@/modules/customerType/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'

const PAGE_SIZE = 10

export default defineComponent({
	components: {
		ModalUploadExcel,
		CarForm,
		WrapFlexContainer,
	},
	async setup() {
		const initListCarRes = await getCars({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
		return { initListCarRes }
	},
	computed: {
		rowData(): ICar[] {
			return (
				this.listItem?.map((item: ICar, index: number) => {
					return {
						...item,
						car: [item.carBrandName, item.carModelName, item.carYearName]
							.filter((item) => item != undefined)
							.join(' ')
							.trim(),
						stt: index + 1 + PAGE_SIZE * (this.pagination.currentPage - 1),
					}
				}) || []
			)
		},
	},
	data() {
		return {
			showModal: false as boolean,
			listItem: [] as any,
			EFormState: EFormState,
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			tableName: this.$t('module.customerType.tableName'),
			columnData: [
				{
					key: 'stt',
					headerName: this.$t('module.car.stt'),
					width: '60px',
				},
				{
					key: 'customerName',
					headerName: this.$t('module.car.customerName'),
					width: '160px',
				},
				{
					key: 'customerPhone',
					headerName: this.$t('module.car.customerPhone'),
				},
				{
					key: 'licensePlate',
					headerName: this.$t('module.car.licensePlate'),
				},
				{
					key: 'carBrandName',
					headerName: this.$t('module.car.carBrand'),
				},
				{
					key: 'carModelName',
					headerName: this.$t('module.car.carModel'),
				},
				{
					key: 'carYearName',
					headerName: this.$t('module.car.carYear'),
				},
				{
					key: 'carVersionName',
					headerName: this.$t('module.car.carVersion'),
				},
				{
					key: 'carName',
					headerName: this.$t('module.car.carName'),
				},
				{
					key: 'action',
					headerName: this.$t('module.car.action.action'),
					align: 'center',
				},
			],
			filterConfig: {
				filter: {
					value: '',
				},
				customerTypeId: {
					value: 0,
					options: [],
				},
				customerName: {
					value: '',
				},
				customerPhone: {
					value: '',
				},
				licensePlate: {
					value: '',
				},
				vinNumber: {
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
				{
					icon: 'EllipsisVerticalIcon',
					name: this.$t('module.car.action.view'),
					action: (params: any) =>
						this.redirectCarDetail(params.id, EFormState.VIEW),
				},
				{
					icon: 'EllipsisVerticalIcon',
					name: this.$t('module.car.edit'),
					action: (params: any) =>
						this.redirectCarDetail(params.id, EFormState.EDIT),
				},
			],
		}
	},
	created() {
		this.getCustomerTypes()
		this.calculateCarFeatureOption()
		this.listItem = this.initListCarRes.data || []
		this.pagination.total = this.initListCarRes.totalElement

		// this.getTableRowData({
		//     pageSize: PAGE_SIZE,
		//     pageNumber: 1,
		// });
	},
	watch: {
		'filterConfig.carBrandId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filter()
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
					this.filter()
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
					this.filter()
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
		'filterConfig.carVersionId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filter()
				}
			},
		},
		'filterConfig.filter.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filter()
				}
			},
		},
		'filterConfig.customerTypeId.value': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.filter()
				}
			},
		},
	},
	methods: {
		openModalUploadExcel() {
			const garageId = localStorage.getItem('garageId')
			this.$refs.ModalUploadExcelRef.open({
				templateUrl: 'download-template/car-customer',
				templateName: 'car-customer_template.xlsx',
				submitUrl: `import-excels/${garageId}/car-customer`,
			})
		},
		onRefreshData() {
			this.listItem = []
			this.showModal = false
			this.getTableRowData({
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		},
		redirectCarDetail(id: number, state: EFormState) {
			state == EFormState.EDIT
				? this.$router.push({ path: `/app/sell/cars/${id}/edit` })
				: this.$router.push({ path: `/app/sell/cars/${id}/info` })
		},
		onOpenFormModal(id?: number, params?: ICar, state?: EFormState) {
			this.showModal = true
			setTimeout(() => {
				;(this.$refs.CarFormRef as InstanceType<typeof CarForm>).openDialog(
					id,
					params,
					state
				)
			}, 100)
		},
		filter() {
			let data = {
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			} as any
			Object.keys(this.filterConfig).map((field) => {
				if (this.filterConfig[field].value) {
					data[field] = this.filterConfig[field].value
				}
			})
			this.getTableRowData(data)
		},
		changePage(val: any) {
			this.pagination.currentPage = val.currentPage
			this.getTableRowData({
				pageNumber: val.currentPage,
				pageSize: PAGE_SIZE,
			})
		},
		async getTableRowData(params: any) {
			const result = await getCars(params)
			this.listItem = result.data || []
			this.pagination.total = result.totalElement
		},
		async getCarFeatureOption(data: any) {
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
			let res: any = await this.getCarFeatureOption({
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
		async getCustomerTypes() {
			const res = await getCustomerTypes({
				pageSize: 100000,
				pageNumber: 1,
			})
			this.filterConfig.customerTypeId.options = res.data.map((a: any) => {
				return {
					label: a.customerTypeName,
					value: a.id,
				}
			})
		},
	},
})
</script>
