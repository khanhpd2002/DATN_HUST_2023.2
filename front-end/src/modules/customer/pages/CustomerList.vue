<template>
	<div class="flex flex-col gap-4 p-[16px] customer bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.customer.customerName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.customerName.value"
					:placeholder="$t('module.car.placeholder.customerName')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.customer.customerTypeName') }}
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
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.customer.phoneNumber') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.phoneNumber.value"
					:placeholder="$t('module.customer.placeholder.phoneNumber')"
					class="font-medium"
					:trim="true"
				/>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer justify="end">
			<div>
				<ACCDButton
					size="md"
					type="secondary"
					variant="fill"
					class="w-full"
					@click="openModalUploadExcel"
				>
					<ACCDAIcon class="mr-2" name="ExportCurve" size="20"></ACCDAIcon>
					<span class="text-info-base font-medium">{{
						$t('module.customer.action.uploadExcel')
					}}</span>
				</ACCDButton>
			</div>
			<div>
				<ACCDButton
					size="md"
					type="secondary"
					variant="fill"
					class="w-full"
					@click="
						() => {
							onOpenFormModal()
						}
					"
				>
					<span class="text-info-base font-medium">{{
						$t('module.customer.action.add')
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
						$t('module.customer.action.search')
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
				<template #cell-action="{ row, col, field }">
					<ACCDDropdown class="flex justify-center">
						<template v-slot:activator="{ props }">
							<div class="cursor-pointer text-center p-4" v-bind="props">
								<ACCDIcon
									class="text-base text-black"
									name="fa-solid fa-ellipsis-vertical"
								></ACCDIcon>
							</div>
						</template>
						<div class="rounded-md border w-56 bg-white py-2">
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => redirectCustomerDetail(row.id, EFormState.EDIT)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-pen-to-square"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.customer.action.edit') }}
								</span>
							</div>
							<hr />
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => redirectCustomerDetail(row.id, EFormState.VIEW)"
							>
								<ACCDIcon
									class="text-gray-700"
									name="fa-solid fa-file-contract"
								></ACCDIcon>
								<span class="text-gray-700">
									{{ $t('module.customer.action.view') }}
								</span>
							</div>
						</div>
					</ACCDDropdown>
				</template>
			</ACCDTable>
		</div>
		<CustomerForm ref="CustomerFormRef" @refresh="onRefreshData" />
		<ModalUploadExcel ref="ModalUploadExcelRef" @success="onRefreshData" />
	</div>
</template>

<script lang="ts">
import { getCustomers } from '@/modules/customer/api'
import { ICustomer } from '@/types'
import { EFormState } from '@/enums'
import { defineComponent } from 'vue'
import CustomerForm from '@/modules/customer/pages/CustomerForm.vue'
import { getCustomerTypes } from '@/modules/customerType/api'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'
import useCDRouter from '@/composables/useRouter'
import { emitter } from '@/utils/mitt'
const PAGE_SIZE = 10
const { router, query } = useCDRouter()
export default defineComponent({
	components: {
		CustomerForm,
		WrapFlexContainer,
		ModalUploadExcel,
	},
	async setup() {
		const initListCustomerRes = await getCustomers({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
		return { initListCustomerRes }
	},
	computed: {
		rowData(): ICustomer[] {
			return (
				this.listItem?.map((item: ICustomer, index: number) => {
					return {
						...item,
						stt: index + 1 + PAGE_SIZE * (this.pagination.currentPage - 1),
					}
				}) || []
			)
		},
	},
	data() {
		return {
			listItem: [] as any,
			EFormState: EFormState,
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			tableName: this.$t('module.customer.title'),
			columnData: [
				{
					key: 'stt',
					headerName: this.$t('module.customer.stt'),
					width: '80px',
				},
				{
					key: 'customerTypeName',
					headerName: this.$t('module.customer.customerTypeName'),
					maxLength: 20,
				},
				{
					key: 'fullName',
					headerName: this.$t('module.customer.customerName'),
				},
				{
					key: 'address',
					headerName: this.$t('module.customer.address'),
				},
				{
					key: 'phoneNumber',
					headerName: this.$t('module.customer.phoneNumber'),
				},
				{
					key: 'action',
					headerName: this.$t('module.car.action.action'),
					align: 'center',
					width: '120px',
				},
			],
			filterConfig: {
				customerTypeId: {
					value: 0,
					options: [],
				},
				customerName: {
					value: '',
				},
				phoneNumber: {
					value: '',
				},
			} as any,

			contextActions: [
				{
					icon: 'EllipsisVerticalIcon',
					name: this.$t('module.customer.action.view'),
					action: (params: any) =>
						this.onOpenFormModal(params.id, params, EFormState.VIEW),
				},
				{
					icon: 'EllipsisVerticalIcon',
					name: this.$t('module.customer.edit'),
					action: (params: any) =>
						this.onOpenFormModal(params.id, params, EFormState.EDIT),
				},
			],
		}
	},
	created() {
		this.getCustomerTypes()
		const result = this.initListCustomerRes
		this.listItem = result.data || []
		this.pagination.total = result.totalElement
		this.getTableRowData({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
	},

	mounted() {
		emitter.on('router-on-set-query', async (params) => {
			this.pagination.currentPage = (
				params as { pageSize: number; currentPage: number }
			).currentPage
			this.pagination.perPage = (
				params as { pageSize: number; currentPage: number }
			).pageSize

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
		})

		if (this.$router.currentRoute.value.query) {
			if (Number(this.$router.currentRoute.value.query.currentPage)) {
				this.pagination.currentPage = Number(
					this.$router.currentRoute.value.query.currentPage
				)
			}
			if (Number(this.$router.currentRoute.value.query.pageSize)) {
				this.pagination.perPage = Number(
					this.$router.currentRoute.value.query.pageSize
				)
			}
		}
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
			this.getTableRowData({
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		},
		onOpenFormModal(id?: number, params?: ICustomer, state?: EFormState) {
			;(
				this.$refs.CustomerFormRef as InstanceType<typeof CustomerForm>
			).openDialog(id, params, state)
		},
		redirectCustomerDetail(id: number, state: EFormState) {
			state == EFormState.EDIT
				? this.$router.push({ path: `/app/sell/customers/${id}/edit` })
				: this.$router.push({ path: `/app/sell/customers/${id}/info` })
		},
		objectToQueryString(params: any) {
			const queryString = Object.keys(params)
				.map(
					(key) =>
						`${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`
				)
				.join('&')
			return queryString
		},
		query(params: any) {
			history.pushState(
				{
					current: this.$route.fullPath,
				},
				'',
				this.$route.path + '?' + this.objectToQueryString(params)
			)
			this.$router.options.history.push(
				this.$route.path + '?' + this.objectToQueryString(params)
			)
			// route.query = params
			emitter.emit('router-on-set-query', params)
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
		async changePage(val: any) {
			await this.query({
				currentPage: val.currentPage,
				pageSize: val.pageSize,
			})
		},
		async getTableRowData(params: any) {
			const result = await getCustomers(params)
			this.listItem = result.data || []
			this.pagination.total = result.totalElement
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
