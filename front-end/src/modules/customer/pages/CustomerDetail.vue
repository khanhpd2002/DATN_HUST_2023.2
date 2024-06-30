<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded" v-if="!isLoading">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.customer.customerName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="item.fullName"
					:placeholder="$t('module.car.placeholder.customerName')"
					:disabled="isView"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.customer.customerTypeName') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="item.customerTypeId"
					:options="customerTypeOptions"
					labelName="label"
					valueName="value"
					:disabled="isView"
					:placeholder="$t('module.car.placeholder.customerTypeName')"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.customer.phoneNumber') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="item.phoneNumber"
					:disabled="isView"
					:placeholder="$t('module.customer.placeholder.phoneNumber')"
					@update:modelValue="() => (isErrorPhoneNumber = false)"
				/>
				<span v-if="isErrorPhoneNumber" class="italic text-red-500">{{
					$t('module.customer.error.phoneNumber')
				}}</span>
			</div>
		</WrapFlexContainer>
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1">
					{{ $t('module.customer.address') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="item.address"
					:placeholder="$t('module.customer.placeholder.address')"
					:disabled="isView"
				/>
			</div>
			<div class="hidden lg:block lg:w-1/3"></div>
			<div class="hidden lg:block lg:w-1/3"></div>
		</WrapFlexContainer>

		<div
			class="mb-4 flex flex-col-reverse lg:flex-row-reverse justify-between lg:items-end gap-4"
		>
			<div>
				<ACCDButton
					v-if="!isView"
					@click="addCarModel(undefined, newCar, EFormState.ADD_FOR_CUSTOMER)"
					size="md"
					type="primary"
					variant="fill"
					class="w-full"
				>
					<span class="text-white font-medium">{{
						$t('module.customer.action.addCar')
					}}</span>
				</ACCDButton>
			</div>
			<h1 class="font-semibold text-2xl">
				{{ $t('module.customer.listCar') }}
			</h1>
		</div>
		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:tableName="tableName"
			>
				<template
					#cell-action="{ row, col, field }"
					v-if="isView"
					v-slot:activator="{ props }"
				>
					<div
						class="w-8 cursor-pointer text-center"
						@click="() => openCarDetail(row.id)"
					>
						<ACCDIcon
							class="text-base text-black"
							name="fa-solid fa-eye"
						></ACCDIcon>
					</div>
				</template>
				<template #cell-action="{ row, col, field }" v-if="!isView">
					<ACCDDropdown>
						<template v-slot:activator="{ props }">
							<div class="text-center cursor-pointer w-8" v-bind="props">
								<ACCDIcon
									class="text-base text-black"
									name="fa-solid fa-ellipsis-vertical"
								></ACCDIcon>
							</div>
						</template>
						<div class="rounded-md border w-56 bg-white py-2">
							<div
								class="flex px-4 py-2 gap-3 cursor-pointer"
								@click="() => addCarModel(row.id, row, EFormState.EDIT)"
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
								@click="() => addCarModel(row.id, row, EFormState.VIEW)"
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
		<div>
			<div class="flex justify-end mt-4 space-x-4">
				<ACCDButton type="secondary" variant="fill" @click="backCustomerList">
					<span class="text-info-base font-medium">{{
						$t('module.customerType.action.back')
					}}</span>
				</ACCDButton>
				<ACCDButton
					type="primary"
					variant="fill"
					@click="onSubmit"
					v-if="!isView"
				>
					<span class="text-white font-medium">{{
						$t('module.customerType.action.save')
					}}</span>
				</ACCDButton>
			</div>
		</div>
		<CustomerForm ref="CustomerFormRef" @refresh="onRefreshData" />
		<CarForm ref="CarFormRef" @refresh="onRefreshData" />
	</div>

	<Loading :isLoading="isLoading" />
</template>

<script lang="ts">
import { getCustomerById, updateCustomerById } from '@/modules/customer/api'
import { ICar, ICustomer, ISelectOption } from '@/types'
import { EFormState } from '@/enums'
import { defineComponent } from 'vue'
import CustomerForm from '@/modules/customer/pages/CustomerForm.vue'
import { getCustomerTypes } from '@/modules/customerType/api'
import { useRoute } from 'vue-router'
import CarForm from '@/modules/car/pages/CarForm.vue'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import Loading from '@/modules/sharedModules/component/Loading.vue'
import { validatePhoneNumber } from '@/modules/sharedModules/component/constants'

const PAGE_SIZE = 10

export default defineComponent({
	components: {
		CustomerForm,
		CarForm,
		WrapFlexContainer,
		Loading,
	},
	setup() {
		const route = useRoute()
		const customerId = route.params.id
		const isView = route.path.includes('/info')

		return { customerId, isView }
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
			isLoading: false,
			newCar: {} as ICar,
			listItem: [] as any,
			item: {} as ICustomer,
			EFormState: EFormState,
			responseErrorMessages: '' as string,
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			isErrorPhoneNumber: false,
			tableName: this.$t('module.customer.title'),
			customerTypeOptions: [] as ISelectOption[],
			columnData: [
				{
					key: 'stt',
					headerName: this.$t('module.customer.detail.stt'),
				},
				{
					key: 'carBrandName',
					headerName: this.$t('module.customer.detail.carBrandName'),
				},
				{
					key: 'carModelName',
					headerName: this.$t('module.customer.detail.carModelName'),
				},
				{
					key: 'carYearName',
					headerName: this.$t('module.customer.detail.carYearName'),
				},
				{
					key: 'carVersionName',
					headerName: this.$t('module.customer.detail.carVersionName'),
				},
				{
					key: 'licensePlate',
					headerName: this.$t('module.customer.detail.licensePlate'),
				},
				{
					key: 'vinNumber',
					headerName: this.$t('module.customer.detail.vinNumber'),
				},
				{
					key: 'action',
					headerName: this.$t('module.car.action.action'),
					align: 'center',
				},
			],

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
		this.getTableRowData(Number(this.customerId))
	},

	mounted() {
		setTimeout(() => {
			this.$evtBus.emit(
				'layout-pages-change-navName',
				this.isView
					? this.$t('module.customer.titleInfo')
					: this.$t('module.customer.titleEdit')
			)
		}, 200)
	},
	methods: {
		onRefreshData() {
			this.listItem = []
			this.getTableRowData(Number(this.customerId))
		},
		onOpenFormModal(id?: number, params?: ICustomer, state?: EFormState) {
			;(
				this.$refs.CustomerFormRef as InstanceType<typeof CustomerForm>
			).openDialog(id, params, state)
		},
		addCarModel(id?: number, params?: ICar, state?: EFormState) {
			;(this.$refs.CarFormRef as InstanceType<typeof CarForm>).openDialog(
				id,
				params,
				state
			)
		},
		backCustomerList() {
			this.$router.back()
		},
		openCarDetail(carId: any) {
			this.$router.push({ path: `/app/sell/cars/${carId}/info` })
		},
		onSubmit() {
			if (
				!this.item.phoneNumber.match(
					/([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/
				)
			) {
				this.isErrorPhoneNumber = true
				return
			}

			updateCustomerById(this.item, this.item.id).then((res) => {
				if (res.code == 1) {
					this.$toast(this.$t('module.customerType.success.edit'), true)
					this.$emit('refresh')
					// this.closeDialog();
				} else {
					this.responseErrorMessages = res.message
					this.$toast(this.responseErrorMessages, false)
				}
			})
		},

		async getTableRowData(customerId: number) {
			this.isLoading = true
			const result = await getCustomerById(customerId)
			this.item = result.data
			this.newCar.customerId = this.item.id
			this.listItem = result.data.cars || []
			this.isLoading = false
		},
		async getCustomerTypes() {
			const res = await getCustomerTypes({
				pageSize: 100000,
				pageNumber: 1,
			})
			this.customerTypeOptions = res.data.map((a: any) => {
				return {
					label: a.customerTypeName,
					value: a.id,
				}
			})
		},
	},
})
</script>
