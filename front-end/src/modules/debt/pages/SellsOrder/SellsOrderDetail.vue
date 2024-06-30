<template xmlns="http://www.w3.org/1999/html">
	<div class="flex items-center gap-3 mb-6">
		<ACCDAIcon name="ArrowLeft" class="cursor-pointer" @click="handleBack" />
		<p class="font-bold text-[16px]">
			{{ $t('module.debt.order.detail') }}
		</p>
	</div>
	<div class="p-[16px] bg-white rounded">
		<div class="cd-modal__body py-4">
			<CDForm :show-footer="false" class="w-full">
				<div class="flex flex-col lg:flex-row">
					<CDFormItem
						:label="$t('module.sellSparePart.form.customerPhone')"
						required
						class="w-full lg:w-1/3 mr-4 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.sellSparePart.form.customerPhone')"
							v-model="customer.id"
							:options="customerOptions"
							:disabled="true"
							@update:modelValue="(val: number)=>{
                                  fillValue(val);
                                }"
							:forCreate="true"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.sellSparePart.form.customerType')"
						required
						class="w-full lg:w-1/3 mr-4 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.sellSparePart.form.customerType')"
							v-model="customer.customerTypeId"
							:options="customerTypeOptions"
							:disabled="true"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.sellSparePart.form.customerName')"
						required
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.sellSparePart.form.customerName')"
							v-model="customer.fullName"
							:disabled="true"
							class="font-medium"
						/>
					</CDFormItem>
				</div>
				<div class="flex justify-end">
					<CDFormItem
						:label="$t('module.debt.order.modal.paymentStatus')"
						required
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.debt.order.modal.paymentStatus')"
							v-model="item.paymentStatus"
							:options="paymentStatusOptions"
							:disabled="route.path.includes('view')"
							class="font-medium"
						/>
					</CDFormItem>
				</div>
			</CDForm>
		</div>

		<div>
			<h1 class="text-gray-900 font-semibold text-lg pb-1 mb-4">
				{{ $t('module.sellSparePart.form.products.title') }}
			</h1>
			<ACCDTable ref="table" :columns="columnData" :rowData="products">
				<template #cell-sparePart="{ row, col, field }">
					{{
						productOptions.find((item) => item.value == row.productId)?.label
					}}
				</template>
				<template #cell-unit="{ row, col, field }">
					{{ row.unit }}
				</template>
				<template #cell-quantity="{ row, col, field }">
					{{ row.quantity }}
				</template>
				<template #cell-unitPrice="{ row, col, field }">
					{{ formatPriceVN(row.unitPrice) }}
				</template>
				<template #cell-originalPrice="{ row, col, field }">
					{{ formatPriceVN(row.originalPrice) }}
				</template>
				<template #cell-discount="{ row, col, field }">
					{{ formatPriceVN(row.discount) }} VNĐ
				</template>
				<template #cell-tax="{ row, col, field }">
					{{ formatPriceVN(row.tax) }} %
				</template>
				<template #cell-price="{ row, col, field }">
					{{ formatPriceVN(row.price) }}
				</template>
			</ACCDTable>

			<br />
			<span class="italic text-red-500" v-if="invalid">{{
				$t('module.sellSparePart.error.missingRequiredField')
			}}</span>
			<span class="italic text-red-500" v-if="responseErrorMessages">{{
				responseErrorMessages
			}}</span>
		</div>
		<div class="flex justify-end gap-20">
			<div class="flex flex-col items-end">
				<p class="font-semibold">
					{{ $t('module.sellSparePart.form.totalPrice') }}
				</p>
				<p>{{ $t('module.sellSparePart.form.originalPrice') }}</p>
				<p>{{ $t('module.sellSparePart.form.tax') }}</p>
				<p>{{ $t('module.sellSparePart.form.discount') }}</p>
			</div>
			<div class="flex flex-col justify-end">
				<p class="font-semibold">{{ formatPriceVN(totalPrice) }}</p>
				<p>{{ formatPriceVN(originalPrice) }}</p>
				<p>{{ formatPriceVN(tax) }}</p>
				<p>{{ formatPriceVN(discount) }}</p>
			</div>
		</div>
	</div>
	<div class="flex justify-end gap-2.5 w-full mt-4">
		<ACCDButton variant="fill" type="secondary" @click="closeDialog">
			<span class="text-info-base font-medium">{{
				$t('module.customerType.action.back')
			}}</span></ACCDButton
		>
		<ACCDButton
			v-if="route.path.includes('edit')"
			type="primary"
			variant="fill"
			@click="onSubmit"
		>
			<span class="text-white font-medium">{{
				$t('module.customerType.action.save')
			}}</span></ACCDButton
		>
	</div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue'
import { EFormState } from '@/enums'
import type { ISelectOption } from '@/types'
import { getCustomerTypes } from '@/modules/customerType/api'
import { CDForm, CDFormItem } from '@cd/design-system'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'
import { getCustomerById, getCustomers } from '@/modules/customer/api'
import { create, findById, update } from '@/modules/sellSparePart/api'
import { detailPriceByCustomerTypeAndProductAndService } from '@/modules/productPrice/api'
import { getParentProduct } from '@/modules/accessary/api'
import { emitter } from '@/utils/mitt'
import {
	countOriginPrice,
	countTotalPrice,
	formatPriceVN,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { updateSellOrderPaymentStatus } from '@/modules/debt/api'
import { useRoute, useRouter } from 'vue-router'
const { $t } = useI18n()
const router = useRouter()
const route = useRoute()

const { $toast } = useToast()
const MAX_INT = import.meta.env.VITE_MAX_INTEGER
const isOpen = ref(false)
const item = ref<SellSparePart>({
	id: 0,
	sellCode: '',
	customerId: undefined,
	customerName: '',
	customerPhone: '',
	customerTypeId: undefined,
	createdAt: new Date(),
	deliveryStatus: undefined,
	paymentStatus: undefined,
	totalPrice: 0,
	originalPrice: 0,
	tax: 0,
	discount: 0,
	products: [],
})
const paymentStatusOptions = ref([
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
])

const products = ref<SellSparePartProduct[]>([])
const count = ref(0)
const totalPrice = ref(0)
const originalPrice = ref(0)
const tax = ref(0)
const discount = ref<number>(0)
const responseErrorMessages = ref('')
const id = ref<number | null | string>(null)
const customer = ref({
	id: 0 || '',
	customerTypeId: 0,
	customerTypeName: '',
	fullName: '',
	phoneNumber: '',
})
const customerTypeOptions = ref([])
const deliveryStatusOptions = ref<ISelectOption[]>([])
const productOptions = ref<ISelectOption[]>([])
const customerOptions = ref<ISelectOption[]>([])
const invalid = ref(false)

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	sellSparePartId?: number | string
}

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const columnData = [
	{
		key: 'sparePart',
		headerName: $t('module.sellSparePart.form.products.sparePart'),
		minWidth: '200px',
	},
	{
		key: 'unit',
		headerName: $t('module.sellSparePart.form.products.unit'),
		minWidth: '80px',
	},
	{
		key: 'quantity',
		headerName: $t('module.sellSparePart.form.products.quantity'),
		minWidth: '100px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.sellSparePart.form.products.unitPrice'),
		minWidth: '100px',
	},
	{
		key: 'originalPrice',
		headerName: $t('module.sellSparePart.form.products.originalPrice'),
	},
	{
		key: 'discount',
		headerName: $t('module.sellSparePart.form.products.discount'),
	},
	{
		key: 'tax',
		headerName: $t('module.sellSparePart.form.products.tax'),
		minWidth: '100px',
	},
	{
		key: 'price',
		headerName: $t('module.sellSparePart.form.products.price'),
	},
]

const validations = computed(() => {
	return {
		customer: {
			id: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			customerTypeId: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			fullName: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
		},
		item: {
			deliveryStatus: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
		},
	}
})
const v$ = useVuelidate(validations, { customer, item })
const instanceKey = new Date().getTime()

const handleGetDetailLoading = async () => {
	const id = route.params.id
	if (id) {
		const result = await findById(id)
		item.value = result ? result.data : null
		products.value = item.value.products
		products.value.map((product) => {
			product.tax = product.tax * 100
		})
		products.value.forEach((product) => {
			originalPrice.value += product.originalPrice
			discount.value += product.discount
			tax.value +=
				(product.originalPrice - product.discount) * (product.tax / 100)
		})
		const res = await getCustomerById(item.value.customerId as number)
		customer.value = res.data
		totalPrice.value = item.value.totalPrice as number
	}

	Promise.all([
		getCustomerTypes({
			pageSize: MAX_INT,
			pageNumber: 1,
		}),
		getParentProduct({
			pageSize: MAX_INT,
			pageNumber: 1,
		}),
		getCustomers({
			pageSize: MAX_INT,
			pageNumber: 1,
		}),
	]).then((res) => {
		let result = res[0]
		let productList = res[1]
		let customerList = res[2]
		customerTypeOptions.value = result.data.map((a: any) => {
			return {
				label: a.customerTypeName,
				value: a.id,
			}
		})
		productOptions.value = productList.data.map((a: any) => {
			return {
				label: `${a.code} - ${a.name}`,
				value: a.id,
			}
		})
		customerOptions.value = customerList.data.map((a: any) => {
			return {
				label: `${a.phoneNumber} - ${a.fullName}`,
				value: a.id,
				rawValue: a,
			}
		})
	})
}

await handleGetDetailLoading()
const closeDialog = () => {
	router.push('/app/debt/sales-order')
}
const resetForm = () => {
	responseErrorMessages.value = ''
}

const onSubmit = async () => {
	const id = route.params.id.toString()
	item.value.products.map((product) => {
		product.tax = product.tax / 100
	})
	const response = await updateSellOrderPaymentStatus(
		id,
		item.value.paymentStatus
	)
	const successMessage = $t('module.sellSparePart.toast.editSuccess')
	if (response.code === 1) {
		$toast(successMessage, true)
		setTimeout(() => {
			router.push('/app/debt/sales-order')
		}, 3000)
	} else {
		responseErrorMessages.value = '* ' + response.message
	}
}

const fillValue = (idToFind: number) => {
	const selectedOption = customerOptions.value.find(
		(option: ISelectOption) => option.value === idToFind
	)
	if (selectedOption) {
		count.value = 1
		customer.value.customerTypeId = selectedOption.rawValue.customerTypeId
		customer.value.fullName = selectedOption.rawValue.fullName
		customer.value.phoneNumber = selectedOption.rawValue.phoneNumber
	} else {
		count.value = 0
		customer.value.customerTypeId = 0
		customer.value.fullName = ''
		customer.value.phoneNumber = ''
	}
}

const calcPrice = () => {
	discount.value = 0
	tax.value = 0
	for (let i = 0; i < products.value.length; i++) {
		discount.value = Number(discount.value) + Number(products.value[i].discount)
		discount.value = Number(discount.value)
		tax.value =
			Number(tax.value) +
			Number(
				((products.value[i].originalPrice - products.value[i].discount) *
					products.value[i].tax) /
					100
			)
		tax.value = Number(tax.value)
	}
}

const handleBack = () => {
	router.back()
}
</script>

<style scoped></style>
