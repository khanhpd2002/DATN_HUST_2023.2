<template>
	<div class="flex items-center gap-3 mb-6">
		<ACCDAIcon name="ArrowLeft" class="cursor-pointer" @click="handleBack" />
		<p class="font-bold text-[16px]">
			{{ $t('module.debt.serviceTicket.form.detail') }}
		</p>
	</div>
	<div class="p-[16px] bg-white rounded">
		<div class="cd-modal__body py-4">
			<ACCDForm :show-footer="false">
				<div class="flex flex-col lg:flex-row md:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.phone')"
						required
						class="w-full lg:w-1/3"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.phone')"
							size="md"
							:disabled="true"
							v-model="serviceTicketForm.customerPhone.value"
							:options="serviceTicketForm.customerPhone.options"
							:forCreate="true"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.customerType')"
						required
						class="w-full lg:w-1/3"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.customerType')"
							:disabled="true"
							v-model="serviceTicketForm.customerTypeId.value"
							:options="serviceTicketForm.customerTypeId.options"
							size="md"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.customerName')"
						required
						class="w-full lg:w-1/3"
					>
						<ACCDInputText
							:disabled="true"
							:placeholder="$t('module.debt.serviceTicket.form.customerName')"
							v-model="serviceTicketForm.customerName.value"
							size="md"
						></ACCDInputText>
					</ACCDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.licensePlate')"
						required
						class="w-full lg:w-1/3"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.licensePlate')"
							:disabled="true"
							v-model="serviceTicketForm.licensePlate.value"
							:options="serviceTicketForm.licensePlate.options"
							size="md"
							:forCreate="true"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.vinNumber')"
						class="w-full lg:w-1/3"
					>
						<ACCDInputText
							:placeholder="$t('module.debt.serviceTicket.form.vinNumber')"
							:disabled="true"
							v-model="serviceTicketForm.vinNumber.value"
							size="md"
						></ACCDInputText>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.journey')"
						class="w-full lg:w-1/3"
					>
						<ACCDInputText
							:placeholder="$t('module.debt.serviceTicket.form.journey')"
							:disabled="true"
							v-model="serviceTicketForm.odo.value"
							size="md"
						></ACCDInputText>
					</ACCDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.carBrand')"
						required
						class="w-full lg:w-1/4"
					>
						<ACCDSelect
							:disabled="true"
							:placeholder="$t('module.debt.serviceTicket.form.carBrand')"
							v-model="serviceTicketForm.carBrandId.value"
							:options="serviceTicketForm.carBrandId.options"
							size="md"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.carModel')"
						required
						class="w-full lg:w-1/4"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.carModel')"
							:disabled="true"
							v-model="serviceTicketForm.carModelId.value"
							:options="serviceTicketForm.carModelId.options"
							size="md"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.carYear')"
						required
						class="w-full lg:w-1/4"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.carYear')"
							:disabled="true"
							v-model="serviceTicketForm.carYearId.value"
							:options="serviceTicketForm.carYearId.options"
							size="md"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.carVersion')"
						required
						class="w-full lg:w-1/4"
					>
						<ACCDSelect
							:placeholder="$t('module.debt.serviceTicket.form.carVersion')"
							:disabled="true"
							v-model="serviceTicketForm.carVersionId.value"
							:options="serviceTicketForm.carVersionId.options"
							size="md"
						></ACCDSelect>
					</ACCDFormItem>
				</div>
				<div>
					<ACCDFormItem
						:label="$t('module.debt.serviceTicket.form.requirement')"
					>
						<ACCDTextArea
							:disabled="true"
							v-model="serviceTicketForm.description.value"
							:placeholder="$t('module.debt.serviceTicket.form.requirement')"
						>
						</ACCDTextArea>
					</ACCDFormItem>
				</div>
			</ACCDForm>
		</div>

		<div class="flex gap-4 flex-col">
			<div class="flex justify-end">
				<div class="w-full lg:w-1/4">
					<span>
						{{ $t('module.debt.serviceTicket.form.paymentStatus') }}
					</span>
					<ACCDSelect
						:placeholder="$t('module.debt.serviceTicket.form.paymentStatus')"
						:disabled="route.path.includes('view')"
						v-model="serviceTicketForm.paymentStatus.value"
						:options="serviceTicketForm.paymentStatus.options"
					></ACCDSelect>
				</div>
			</div>
			<div class="flex justify-between items-end">
				<span class="font-semibold text-xl">{{
					$t('module.debt.serviceTicket.form.service.title')
				}}</span>
				<ACCDButton
					type="primary"
					size="md"
					@click="onShowBill = true"
					class="w-1/6"
					v-if="route.path.includes('view')"
				>
					{{ $t('module.debt.serviceTicket.print') }}
				</ACCDButton>
			</div>

			<TableContainer>
				<ACCDTable
					:columns="serviceColumns"
					:rowData="serviceRowData"
					class="w-full"
				>
					<template #cell-stt="{ row, rowIndex, col, field }">
						{{ rowIndex + 1 }}
					</template>

					<template #cell-garageServiceTypeId="{ row, col, field }">
						{{
							row.garageServiceTypeId.options.filter(
								(garageServiceType) =>
									garageServiceType.value === row.garageServiceTypeId.value
							)[0]?.label
						}}
					</template>
					<template #cell-garageServiceId="{ row, col, field }">
						{{
							row.garageServiceId.options.filter(
								(garageService) =>
									garageService.value === row.garageServiceId.value
							)[0]?.label
						}}
					</template>

					<template #cell-employeeId="{ row, col, field }">
						{{
							row.employeeId.options.filter(
								(employee) => employee.value === row.employeeId.value
							)[0]?.label
						}}
					</template>
					<template #cell-unitPrice="{ row, col, field }">
						{{ formatPriceVN(row.unitPrice) }},000
					</template>
					<template #cell-money="{ row, col, field }">
						{{ formatPriceVN(row.money) }},000
					</template>
					<template #cell-discount="{ row, col, field }">
						<div v-if="discountType == 1">
							{{
								row.discount
									? `${formatPriceVN(row.discount)} ,000 VNĐ`
									: `0,000 VNĐ`
							}}
						</div>
						<div v-if="discountType == 2">{{ row.discount }}%</div>
					</template>
					<template #cell-quantity="{ row, col, field }">
						{{ row.quantity }}
					</template>
					<template #cell-tax="{ row, col, field }"> {{ row.tax }}%</template>
					<template #cell-total="{ row, col, field }">
						{{ formatPriceVN(row.total) }},000
					</template>
				</ACCDTable>
			</TableContainer>
		</div>
		<br />
		<div>
			<span class="font-semibold text-xl">{{
				$t('module.debt.serviceTicket.form.product.title')
			}}</span>
			<br />
			<br />
			<TableContainer>
				<ACCDTable
					:columns="productColumns"
					:rowData="productRowData"
					class="w-full"
				>
					<template #cell-stt="{ row, rowIndex, col, field }">
						<WrapFlexContainer>
							<div class="text-center font-medium text-typo overflow-hidden">
								{{ rowIndex + 1 }}
							</div>
						</WrapFlexContainer>
					</template>
					<template #cell-productId="{ row, col, field }">
						<WrapFlexContainer>
							{{
								row.productId.options.filter(
									(product) => product.value === row.productId.value
								)[0]?.label
							}}
						</WrapFlexContainer>
					</template>
					<template #cell-unit="{ row, col, field }">
						<WrapFlexContainer>
							{{ row.unit }}
						</WrapFlexContainer>
					</template>
					<template #cell-discount="{ row, col, field }">
						<div v-if="discountType == 1">
							{{
								row.discount
									? `${formatPriceVN(row.discount)} ,000 VNĐ`
									: `0,000 VNĐ`
							}}
						</div>
						<div v-if="discountType == 2">{{ row.discount }}%</div>
					</template>
					<template #cell-tax="{ row, col, field }">
						<WrapFlexContainer> {{ row.tax }}% </WrapFlexContainer>
					</template>
					<template #cell-total="{ row, col, field }">
						<WrapFlexContainer>
							{{ formatPriceVN(row.total) }},000
						</WrapFlexContainer>
					</template>
					<template #cell-quantity="{ row, col, field }">
						<WrapFlexContainer>
							{{ row.quantity ? row.quantity : 'N/A' }}
						</WrapFlexContainer>
					</template>
					<template #cell-unitPrice="{ row, col, field }">
						<WrapFlexContainer>
							{{ formatPriceVN(row.unitPrice) }},000
						</WrapFlexContainer>
					</template>

					<template #cell-money="{ row, col, field }">
						<WrapFlexContainer>
							{{ formatPriceVN(row.money) }},000
						</WrapFlexContainer>
					</template>
				</ACCDTable>
			</TableContainer>
		</div>
		<span class="italic text-red-500" v-if="invalid">
			{{ $t('module.employee.error.missingRequiredField') }}
		</span>
		<span v-if="errorMessage" class="italic text-red-500">
			{{ errorMessage }}
		</span>
		<br />
		<div class="flex justify-end gap-20">
			<div class="flex flex-col items-end">
				<p class="font-semibold">
					{{ $t('module.debt.serviceTicket.form.cash.totalMoney') }}
				</p>
				<p>
					{{ $t('module.debt.serviceTicket.form.cash.intoMoney') }}
				</p>
				<p>
					{{ $t('module.debt.serviceTicket.form.cash.totalTax') }}
				</p>
				<p>
					{{ $t('module.debt.serviceTicket.form.cash.totalDiscount') }}
				</p>
			</div>
			<div class="flex flex-col justify-end">
				<p class="font-semibold">
					{{ formatPriceVN(Number(totalMoney)) }}
				</p>
				<p>{{ formatPriceVN(Number(intoMoney)) }}</p>
				<p>{{ formatPriceVN(Number(totalTax)) }}</p>
				<p>{{ formatPriceVN(Number(totalDiscount)) }}</p>
			</div>
		</div>
		<br />
		<div class="flex justify-end gap-2.5 w-full">
			<ACCDButton
				@click="router.push('/app/debt/service-ticket')"
				type="secondary"
				class="w-1/6"
			>
				{{ $t('module.debt.serviceTicket.action.back') }}
			</ACCDButton>
			<ACCDButton
				v-if="route.params.id && route.path.includes('edit')"
				@click="onSubmit"
				type="primary"
				class="w-1/6"
			>
				{{ $t('module.debt.serviceTicket.action.confirm') }}
			</ACCDButton>
		</div>
	</div>
	<RepairOrder
		v-if="onShowBill"
		:title="$t('module.debt.serviceTicket.moduleName')"
		:customerInfo="customerInfo"
		:carInfo="carInfo"
		:serviceInfo="computedServiceInfo"
		:productInfo="computedProductInfo"
		:description="serviceTicketForm.description.value"
		:discountType="discountType"
	></RepairOrder>
</template>
<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { EFormState } from '@/enums'
import { $toast } from '@/main'
import { ISelectOption } from '@/types'
import { getCars } from '@/modules/car/api'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
} from '@/modules/sharedModules/api'

import { getCustomerTypes } from '@/modules/customerType/api'
import {
	getListGarageServiceId,
	getListGarageServiceTypeId,
} from '@/modules/garageServiceController/api'

import { getDetailOrder } from '@/modules/serviceTicket/api'
import { getGarageEmployees } from '@/modules/employee/api'
import { getCustomersByGarageId } from '@/modules/sellingManagement/api'

import RepairOrder from '@/modules/sharedModules/pages/formHTML/repairOrder.vue'
import { formatPriceVN } from '@/modules/sharedModules/component/constants'
import { updatePaymentStatusServiceTicket } from '@/modules/debt/api'
import { useI18n } from '@/composables/useI18n'
import { getParentProduct } from '@/modules/accessary/api'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'
import { useRoute, useRouter } from 'vue-router'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'

const { $t } = useI18n()

const route = useRoute()
const router = useRouter()
type ModalProps = {
	modelValue: boolean
	action: number
	id?: string
}

const baseServiceRowData = {
	garageServiceTypeId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	garageServiceId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	quantity: 0 as number,
	unitPrice: 0 as number,
	money: 0 as number,
	discount: 0,
	tax: 0,
	total: 0 as number | string,
	employeeId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	instanceKey: 0,
}

const baseProductRowData = {
	productId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	unit: '',
	quantity: 0,
	unitPrice: 0,
	price: 0,
	money: 0,
	discount: 0,
	tax: 0,
	total: 0,
	instanceKey: 0,
}

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	actions: EFormState.ADD,
	id: '',
})

const serviceColumns = ref([
	{
		key: 'stt',
		headerName: $t('module.newServiceTicket.table.index'),
	},
	{
		key: 'garageServiceTypeId',
		headerName: $t('module.newServiceTicket.table.type'),
		minWidth: '280px',
	},
	{
		key: 'garageServiceId',
		headerName: $t('module.newServiceTicket.form.service.name'),
		minWidth: '300px',
	},

	{
		key: 'quantity',
		headerName: $t('module.newServiceTicket.form.service.quantity'),
		minWidth: '120px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.newServiceTicket.form.service.price'),
		minWidth: '150px',
	},
	{
		key: 'money',
		headerName: $t('module.newServiceTicket.form.service.money'),
		minWidth: '150px',
	},
	{
		key: 'discount',
		headerName: $t('module.newServiceTicket.form.service.discount'),
		minWidth: '150px',
	},
	{
		key: 'tax',
		headerName: $t('module.newServiceTicket.form.service.tax'),
		minWidth: '120px',
	},
	{
		key: 'total',
		headerName: $t('module.newServiceTicket.form.service.total'),
		minWidth: '150px',
	},
	{
		key: 'employeeId',
		headerName: $t('module.newServiceTicket.form.service.assignee'),
		minWidth: '200px',
	},
])
const serviceRowData = ref([] as (typeof baseServiceRowData)[])
const productColumns = ref([
	{
		key: 'stt',
		headerName: $t('module.newServiceTicket.table.index'),
		minWidth: '30px',
	},
	{
		key: 'productId',
		headerName: $t('module.serviceTicket.form.product.name'),
		minWidth: '300px',
	},
	{
		key: 'unit',
		headerName: $t('module.serviceTicket.form.product.unit'),
		minWidth: '80px',
	},
	{
		key: 'quantity',
		headerName: $t('module.serviceTicket.form.product.quantity'),
		minWidth: '120px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.serviceTicket.form.product.price'),
		minWidth: '150px',
	},
	{
		key: 'money',
		headerName: $t('module.serviceTicket.form.product.money'),
		minWidth: '80px',
	},
	{
		key: 'discount',
		headerName: $t('module.serviceTicket.form.product.discount'),
		minWidth: '150px',
	},
	{
		key: 'tax',
		headerName: $t('module.serviceTicket.form.product.tax'),
		minWidth: '120px',
	},
	{
		key: 'total',
		headerName: $t('module.serviceTicket.form.product.total'),
		minWidth: '100px',
	},
])
const productRowData = ref([] as (typeof baseProductRowData)[])

const serviceTicketForm = ref({
	paymentStatus: {
		value: 1,
		options: [
			{
				value: 1,
				label: $t('module.debt.serviceTicket.paymentStatus.1'),
			},
			// {
			//     value: 2,
			//     label: $t("module.debt.serviceTicket.paymentStatus.2"),
			// },
			{
				value: 3,
				label: $t('module.debt.serviceTicket.paymentStatus.3'),
			},
			// {
			//     value: 4,
			//     label: $t("module.debt.serviceTicket.paymentStatus.4"),
			// },
			// {
			//     value: 5,
			//     label: $t("module.debt.serviceTicket.paymentStatus.5"),
			// },
		],
	},
	customerPhone: {
		value: '',
		options: [] as (ISelectOption & { origin: any })[],
		disabled: false,
	},
	customerTypeId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	customerName: {
		value: '',
		disabled: false,
	},

	licensePlate: {
		value: '',
		options: [] as (ISelectOption & { origin: any })[],
		disabled: false,
	},
	vinNumber: {
		value: '',
		disabled: false,
	},
	odo: {
		value: '',
		disabled: false,
	},
	carBrandId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carModelId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carYearId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carVersionId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	description: {
		value: '',
		disabled: false,
	},
	//hidden field
	discount: {
		value: null,
	},
	totalPrice: {
		value: null,
	},
	carId: {
		value: '' as string | null,
	},
	customerId: {
		value: 0 as number | null,
	},
})
const listServiceOptions = ref([] as (ISelectOption & { origin: any })[])
const listServiceTypeIdOptions = ref([] as (ISelectOption & { origin: any })[])
const listGarageEmployees = ref([] as (ISelectOption & { origin: any })[])
const listGarageProducts = ref([] as (ISelectOption & { origin: any })[])
const errorMessage = ref('')
const invalid = ref(false)
const onShowBill = ref(false)
const allCarOfGarageOptions = ref([])
watch(
	() => serviceTicketForm.value.carBrandId.value,
	(newVal: string) => {
		serviceTicketForm.value.carModelId.options = []
		getCarModelList(newVal).then((res) => {
			serviceTicketForm.value.carModelId.options = res.data.data.map(
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
watch(
	() => serviceTicketForm.value.carModelId.value,
	(newVal: string) => {
		serviceTicketForm.value.carYearId.options = []
		getCarYearList(newVal).then((res) => {
			serviceTicketForm.value.carYearId.options = res.data.data.map(
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
watch(
	() => serviceTicketForm.value.carYearId.value,
	(newVal: string) => {
		serviceTicketForm.value.carVersionId.options = []
		getCarVersionList(newVal).then((res) => {
			serviceTicketForm.value.carVersionId.options = res.data.data.map(
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
// thay đổi phoneNumber, thay đổi customerType, customerId, name, listCars
watch(
	() => serviceTicketForm.value.customerPhone.value,
	async (val) => {
		let originOptions = serviceTicketForm.value.customerPhone.options.find(
			(a) => {
				if (a.value == val) {
					return true
				}
			}
		)
		serviceTicketForm.value.licensePlate.value = ''
		serviceTicketForm.value.customerId.value = null
		serviceTicketForm.value.customerTypeId.value = ''
		serviceTicketForm.value.customerName.value = ''
		if (originOptions) {
			let origin = originOptions.origin
			serviceTicketForm.value.customerTypeId.value = origin.customerTypeId
			serviceTicketForm.value.customerName.value = origin.fullName
			serviceTicketForm.value.customerId.value = origin.id
			serviceTicketForm.value.customerTypeId.disabled = true
			serviceTicketForm.value.customerName.disabled = true
			serviceTicketForm.value.licensePlate.options =
				allCarOfGarageOptions.value.filter((car: any) => {
					return (
						car.origin.customerId == serviceTicketForm.value.customerId.value
					)
				})
		} else {
			serviceTicketForm.value.customerTypeId.disabled = false
			serviceTicketForm.value.customerName.disabled = false
			serviceTicketForm.value.licensePlate.options = []
			serviceTicketForm.value.carId.value = null
		}
	}
)
watch(
	() => serviceTicketForm.value.licensePlate.value,
	(val) => {
		let originOptions = serviceTicketForm.value.licensePlate.options.find(
			(a) => {
				return a.value == val
			}
		)
		if (originOptions) {
			serviceTicketForm.value.vinNumber.value = originOptions.origin.vinNumber
			serviceTicketForm.value.carBrandId.value = originOptions.origin.carBrandId
			serviceTicketForm.value.carModelId.value = originOptions.origin.carModelId
			serviceTicketForm.value.carYearId.value = originOptions.origin.carYearId
			serviceTicketForm.value.carVersionId.value =
				originOptions.origin.carVersionId
			serviceTicketForm.value.carId.value = originOptions.origin.id
			//disable các field được fill
			serviceTicketForm.value.vinNumber.disabled = true
			serviceTicketForm.value.carBrandId.disabled = true
			serviceTicketForm.value.carModelId.disabled = true
			serviceTicketForm.value.carYearId.disabled = true
			serviceTicketForm.value.carVersionId.disabled = true
		} else {
			serviceTicketForm.value.vinNumber.disabled = false
			serviceTicketForm.value.odo.disabled = false
			serviceTicketForm.value.carBrandId.disabled = false
			serviceTicketForm.value.carModelId.disabled = false
			serviceTicketForm.value.carYearId.disabled = false
			serviceTicketForm.value.carVersionId.disabled = false
		}
	}
)
const instanceKey = new Date().getTime()
const discountType = ref(2)

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const customerInfo = computed(() => {
	return {
		customerName: serviceTicketForm.value.customerName.value,
		customerPhone: serviceTicketForm.value.customerPhone.value,
	}
})

const carInfo = computed(() => {
	let carBrand = serviceTicketForm.value.carBrandId.options.find((c) => {
		return c.value == serviceTicketForm.value.carBrandId.value
	})
	let carModel = serviceTicketForm.value.carModelId.options.find((c) => {
		return c.value == serviceTicketForm.value.carModelId.value
	})
	let carYear = serviceTicketForm.value.carYearId.options.find((c) => {
		return c.value == serviceTicketForm.value.carYearId.value
	})
	let carVersion = serviceTicketForm.value.carVersionId.options.find((c) => {
		return c.value == serviceTicketForm.value.carVersionId.value
	})
	return {
		licensePlate: serviceTicketForm.value.licensePlate.value,
		vinNumber: serviceTicketForm.value.vinNumber.value,
		odo: serviceTicketForm.value.odo.value,
		carBrand: carBrand?.label,
		carModel: carModel?.label,
		carYear: carYear?.label,
		carVersion: carVersion?.label,
	}
})

const computedServiceInfo = computed(() => {
	let services = serviceRowData.value.map((s) => {
		let service = s.garageServiceId.options.find((p) => {
			return p.value == s.garageServiceId.value
		})
		let employee = s.employeeId.options.find((p) => {
			return p.value == s.employeeId.value
		})
		let result = {} as any
		Object.keys(s).map((k) => {
			if (typeof s == 'object' && 'value' in s) {
				result[k] = (s as any)[k as keyof typeof s].value
			} else {
				result[k] = (s as any)[k as keyof typeof s]
			}
		})
		return {
			...result,
			serviceName: service?.label,
			employeeName: employee?.label,
		}
	})
	return services
})

const computedProductInfo = computed(() => {
	let products = productRowData.value.map((s) => {
		let product = s.productId.options.find((p) => {
			return p.value == s.productId.value
		})

		let result = {} as any
		Object.keys(s).map((k) => {
			if (typeof s == 'object' && 'value' in s) {
				result[k] = (s as any)[k as keyof typeof s].value
			} else {
				result[k] = (s as any)[k as keyof typeof s]
			}
		})
		return {
			...result,
			money: result.money.toFixed(0),
			productName: product?.label,
		}
	})
	return products
})

const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val: boolean) {
		emit('update:modelValue', val)
	},
})
const getCarByUser = (params: any) => {
	return getCars({ ...params, pageSize: 10000, pageNumber: 1 })
}

const onGetDetailOrder = (id: string) => {
	getDetailOrder(id).then((res: any) => {
		let data = res.data
		serviceTicketForm.value.customerPhone.value =
			data.customerResponse.phoneNumber
		setTimeout(() => {
			serviceTicketForm.value.licensePlate.value = data.carResponse.licensePlate
		}, 300)
		serviceTicketForm.value.odo.value = data.odo
		serviceTicketForm.value.paymentStatus.value = data.paymentStatus
		serviceTicketForm.value.description.value = data.description
		discountType.value = res.data.quotation.discountType
			? res.data.quotation.discountType
			: 2
		console.log(
			'data.quotation.quotationLabours',
			data.quotation.quotationLabours
		)
		if (res.data.quotation.quotationLabours) {
			serviceRowData.value = data.quotation.quotationLabours.map(
				(a: any, index: number) => {
					let employeeId = 0
					listGarageEmployees.value.some((e) => {
						if (a.employeeName == e.label) {
							employeeId = e.value
							return true
						}
					})

					return {
						garageServiceId: {
							value: a.garageServiceId,
							options: listServiceOptions.value,
						},
						garageServiceTypeId: {
							value: a.garageServiceTypeId,
							options: listServiceTypeIdOptions.value,
						},
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						tax: a.tax * 100,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						employeeId: {
							value: employeeId,
							options: listGarageEmployees.value,
						},
						instanceKey: index,
						price: a.price / 1000,
						disable: a.outboundProductId == 0 ? true : false,
					}
				}
			)
		}
		console.log('serviceRowData', serviceRowData.value)
		if (res.data.quotation.quotationSpareParts) {
			productRowData.value = data.quotation.quotationSpareParts.map(
				(a: any, index: number) => {
					const temp = [
						...listGarageProducts.value,
						{ value: a.productName, label: a.productName },
					]

					return {
						productId: {
							value: a.productId ? a.productId : a.productName,
							options: a.productName ? temp : listGarageProducts.value,
						},
						unit: a.unit,
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						price: a.price / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						instanceKey: index,
						// status:
						//     a.status == 1
						//         ? EStatusExport.EXPORTED_1
						//         : EStatusExport.UN_EXPORTED_1,
						status: a.status,
						outboundProductId: a.outboundProductId,
						disable: a.outboundProductId == 0 ? false : true,
						outboundTicketId: a.outboundTicketId,
					}
				}
			)
		}
	})
}

const totalMoney = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result += Number(r.total)
	})
	productRowData.value.forEach((r) => {
		result += Number(r.total)
	})

	return Number(result * 1000).toFixed(0)
})

const intoMoney = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result += Number(r.money)
	})
	productRowData.value.forEach((r) => {
		result += Number(r.money)
	})

	return Number(result * 1000).toFixed(0)
})

const totalTax = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		console.log(r)
		result +=
			discountType.value == 1
				? (r.tax / 100) * (r.money - r.discount)
				: (r.tax / 100) * r.money * (1 - r.discount / 100)
	})
	productRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? (r.tax / 100) * (r.money - r.discount)
				: (r.tax / 100) * r.money * (1 - r.discount / 100)
	})

	return Number(result * 1000).toFixed(0)
})

const totalDiscount = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? Number(r.discount)
				: Number((r.money * r.discount) / 100)
	})
	productRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? Number(r.discount)
				: Number((r.money * r.discount) / 100)
	})

	return Number(result * 1000).toFixed(0)
})

const onSubmit = async () => {
	let data = {} as any
	Object.keys(serviceTicketForm.value).forEach((key: string) => {
		data[key] =
			serviceTicketForm.value[key as keyof typeof serviceTicketForm.value].value
	})
	data.quotationLabours = serviceRowData.value
		.filter((r) => {
			return r.garageServiceId.value != 0
		})
		.map((r) => {
			return {
				...r,
				tax: r.tax / 100,
				employeeId: r.employeeId.value,
				productId: r.garageServiceId.value,
			}
		})
	data.quotationSpareParts = productRowData.value
		.filter((r) => {
			return r.productId.value
		})
		.map((r) => {
			return { ...r, tax: r.tax / 100, productId: r.productId.value }
		})

	const id = route.params.id.toString()
	let res = await updatePaymentStatusServiceTicket(
		id,
		serviceTicketForm.value.paymentStatus.value
	)
	if (res.code == 1) {
		$toast($t('module.debt.serviceTicket.form.toast.updateSuccess'), true)
		setTimeout(() => {
			router.push('/app/debt/service-ticket')
		}, 3000)
	} else {
		errorMessage.value = '* ' + res.message
		$toast($t('module.debt.serviceTicket.form.toast.updateFailse'), false)
	}
}

const handleGetDetailLoading = async () => {
	Promise.all([
		getCarBrandList(),
		getCarByUser({}),
		getCustomerTypes({
			pageSize: 10000,
			pageNumber: 1,
		}),
		getCustomersByGarageId({
			pageSize: 10000,
			pageNumber: 1,
		}),
		getListGarageServiceTypeId(),
		getListGarageServiceId({ pageSize: 10000, pageNumber: 1 }),
		getGarageEmployees({
			pageSize: 10000,
			pageNumber: 1,
		}),
		getParentProduct({
			pageSize: 10000,
			pageNumber: 1,
			type: 1,
		}),
	]).then((res) => {
		serviceTicketForm.value.carBrandId.options = res[0].data.data.map(
			(item: any) => {
				return {
					value: item.id,
					label: item.title,
				}
			}
		)
		allCarOfGarageOptions.value = res[1].data.map((c: any) => {
			return {
				value: c.licensePlate,
				label: c.licensePlate,
				origin: c,
			}
		})
		serviceTicketForm.value.customerTypeId.options = res[2].data.map(
			(a: any) => {
				return {
					value: a.id,
					label: a.customerTypeName,
				}
			}
		)
		serviceTicketForm.value.customerPhone.options = res[3].data.map(
			(a: any) => {
				return {
					value: a.phoneNumber,
					label: a.phoneNumber,
					origin: a,
				}
			}
		)
		listServiceTypeIdOptions.value = res[4].data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
		listServiceOptions.value = res[5].data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
		listGarageEmployees.value = res[6].data.map((e: any) => {
			return {
				value: e.id,
				label: e.fullName,
			}
		})
		listGarageProducts.value = res[7].data.map((e: any) => {
			return {
				value: e.id,
				label: `${e.code} - ${e.name}`,
				origin: e,
			}
		})
	})

	const id = route.params.id
	if (id) onGetDetailOrder(id.toString())
}
await handleGetDetailLoading()

const handleBack = () => {
	router.back()
}
</script>

<style lang="scss">
.width-row {
	min-width: 100px;
	max-width: 200px;
}
</style>
