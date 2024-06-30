<template>
	<ACCDModal
		:title="$t('module.sellSparePart.form.addTitle')"
		v-model="lazyValue"
		:hidefooter="true"
		class-width="lg:w-[1024px] md:w-[730px] sm:w-[500px]"
	>
		<div class="cd-modal__body py-4">
			<CDForm :show-footer="false" class="w-full">
				<div class="flex flex-col lg:flex-row gap-4">
					<CDFormItem
						:label="$t('module.sellSparePart.form.customerPhone')"
						required
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.sellSparePart.form.customerPhone')"
							v-model="customer.id"
							:options="customerOptions"
							:disabled="item.deliveryStatus != 1"
							@update:modelValue="(val: number) => {
        fillValue(val);
    }"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.sellSparePart.form.customerType')"
						required
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.sellSparePart.form.customerType')"
							v-model="customer.customerTypeId"
							:options="customerTypeOptions"
							:disabled="customerOptions.find((a) => a.value == customer.id)"
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
							:disabled="customerOptions.find((a) => a.value == customer.id)"
							class="font-medium"
						/>
					</CDFormItem>
				</div>
			</CDForm>
		</div>

		<div>
			<h1 class="text-gray-900 font-semibold text-[16px] pb-1 mb-4">
				{{ $t('module.sellSparePart.form.products.title') }}
			</h1>
			<TableContainer>
				<ACCDTable ref="table" :columns="columnData" :rowData="products">
					<template #cell-index="{ row, rowIndex }">
						{{ rowIndex + 1 }}
					</template>
					<template #cell-action="{ row, col, field }">
						<td
							class="whitespace-normal bg-table-primary px-4 py-2.5 font-medium text-typo"
							v-if="props.state !== EFormState.VIEW"
						>
							<ACCDAIcon
								@click="
									() => {
										deleteProduct(row, row.index)
									}
								"
								class="text-xl text-gray-700 cursor-pointer"
								name="Trash"
							></ACCDAIcon>
						</td>
					</template>
					<template #cell-sparePart="{ row, col, field }">
						<!-- <td class="w-52"> -->

						<span v-if="item.deliveryStatus != 1 && item.deliveryStatus != 2">
							{{
								productOptions.find((item) => item.value == row.productId)
									?.label
							}}</span
						>

						<span v-else-if="row.disable">
							{{
								productOptions.find((item) => item.value == row.productId)
									?.label
							}}
						</span>
						<ACCDSelect
							v-else
							size="md"
							:placeholder="$t('module.sellSparePart.form.products.sparePart')"
							v-model="row.productId"
							:options="productOptions"
							@change="() => onChangeProduct(row, col, field)"
							@createNew="(value) => handleCreateNewProduct(value, row)"
						/>
						<!-- </td> -->
					</template>
					<!-- <template #cell-unit="{ row, col, field }">
				<ACCDInputText size="md" class="custom-in-table" :placeholder="$t('module.sellSparePart.form.products.unit')
					" v-model="row.unit" 
					:readonly="$cloneProps.value.state == EFormState.VIEW || item.deliveryStatus != 1" />
			</template> -->
					<template #cell-quantity="{ row, col, field }">
						<!-- <td class="w-16"> -->
						<div
							v-if="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
						>
							{{ row.quantity }}
						</div>
						<ACCDInputNumber
							v-else
							size="md"
							:placeholder="$t('module.sellSparePart.form.products.quantity')"
							v-model="row.quantity"
							@update:modelValue="() => onChangeRowValue(row, field)"
							:readonly="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
							@keypress="preFilterInputNumberOnly"
						/>

						<!-- </td> -->
					</template>
					<template #cell-unitPrice="{ row, col, field }">
						<div
							v-if="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
						>
							{{ formatPriceVN(row.unitPrice) }}
						</div>
						<ACCDInputPrice
							v-else
							size="md"
							class="custom-in-table"
							:placeholder="$t('module.sellSparePart.form.products.unitPrice')"
							v-model="row.unitPrice"
							@change="() => onChangeRowValue(row, field)"
							:readonly="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
							@keypress="validatePrice"
						/>
					</template>
					<template #cell-originalPrice="{ row, col, field }">
						{{ formatPriceVN(row.originalPrice) }}
					</template>

					<template #cell-discount="{ row, col, field }">
						<!-- <td class="w-36"> -->
						<div
							v-if="
								(row.disable ||
									(item.deliveryStatus != 1 && item.deliveryStatus != 2)) &&
								discountType == 1
							"
							class="whitespace-nowrap"
						>
							{{ formatPriceVN(row.discount) }} VNĐ
						</div>
						<div
							v-if="
								(row.disable ||
									(item.deliveryStatus != 1 && item.deliveryStatus != 2)) &&
								discountType == 2
							"
							class="whitespace-nowrap"
						>
							{{ formatPriceVN(row.discount) }}%
						</div>
						<ACCDInputPrice
							v-else
							size="md"
							class="custom-in-table"
							:placeholder="$t('module.sellSparePart.form.products.discount')"
							v-model="row.discount"
							@change="() => onChangeRowValue(row, field)"
							:readonly="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
							@keypress="validatePrice"
						>
							<template #inner-append>
								<span v-if="discountType == 1" class="px-2"> VNĐ</span>
								<span v-if="discountType == 2" class="px-2"> %</span>
							</template>
						</ACCDInputPrice>
						<!-- </td> -->
					</template>
					<template #cell-tax="{ row, col, field }">
						<!-- <td class="w-16"> -->
						<div
							class="whitespace-nowrap"
							v-if="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
						>
							{{ formatPriceVN(row.tax) }} %
						</div>
						<ACCDInputText
							v-else
							size="md"
							class="custom-in-table"
							:placeholder="$t('module.sellSparePart.form.products.tax')"
							v-model="row.tax"
							@change="() => onChangeRowValue(row, field)"
							:readonly="
								row.disable ||
								(item.deliveryStatus != 1 && item.deliveryStatus != 2)
							"
							@keypress="preFilterInputNumberOnly"
						>
							<template #inner-append><span class="px-2">%</span></template>
						</ACCDInputText>
						<!-- </td> -->
					</template>
					<template #cell-price="{ row, col, field }">
						<!-- <td class="w-36"> -->
						{{ formatPriceVN(row.price) }}
						<!-- </td> -->
					</template>
				</ACCDTable>
			</TableContainer>
			<div
				v-if="item.deliveryStatus == 1 || item.deliveryStatus == 2"
				class="flex items-center text-active mt-2"
			>
				<span class="cursor-pointer mr-1" @click="addSparePart">
					<ACCDAIcon name="AddCircle" />
				</span>
				<span class="cursor-pointer font-medium" @click="addSparePart">
					{{ $t('module.sellSparePart.form.products.addSparePart') }}</span
				>
			</div>

			<span class="italic text-red-500" v-if="invalid">{{
				$t('module.sellSparePart.error.missingRequiredField')
			}}</span>
			<span class="italic text-red-500" v-if="responseErrorMessages">{{
				responseErrorMessages
			}}</span>
			<span class="italic text-red-500" v-if="errorProductMessage">{{
				errorProductMessage
			}}</span>

			<div v-if="isEmptyProductQuanity">
				<span class="italic text-red-500">
					{{ $t('module.sellSparePart.error.empty') }}
				</span>
			</div>
			<div v-if="isEmptyProduct">
				<span class="italic text-red-500">
					{{ $t('module.sellSparePart.error.emptyProduct') }}
				</span>
			</div>
		</div>
		<div class="w-1/3 ml-auto py-3">
			<div class="flex justify-between">
				<p>
					{{ $t('module.serviceTicket.form.cash.intoMoney') }}
				</p>
				<p>{{ formatPriceVN(Number(originalPrice)) }}</p>
			</div>
			<div class="flex justify-between">
				<p>
					{{ $t('module.serviceTicket.form.cash.totalDiscount') }}
				</p>
				<p>{{ formatPriceVN(Number(discount)) }}</p>
			</div>
			<div class="flex justify-between">
				<p>
					{{ $t('module.serviceTicket.form.cash.totalTax') }}
				</p>
				<p>{{ formatPriceVN(Number(tax)) }}</p>
			</div>
			<div class="flex justify-between border-t-2 pt-2 mt-2">
				<p class="font-semibold">
					{{ $t('module.serviceTicket.form.cash.totalMoney') }}
				</p>
				<p class="font-semibold">
					{{ formatPriceVN(Number(totalPrice)) }}
				</p>
			</div>
		</div>

		<div class="mt-4">
			<div
				v-if="props.state == EFormState.ADD"
				class="flex justify-end gap-2.5 w-full"
			>
				<ACCDButton type="secondary" variant="fill" @click="closeDialog">
					<span class="text-info-base font-medium"
						>{{ $t('module.sellSparePart.action.back') }}
					</span>
				</ACCDButton>
				<ACCDButton
					type="primary"
					variant="fill"
					@click="onSubmit"
					:disabled="isDisabled"
				>
					<span class="text-white font-medium">{{
						$t('module.sellSparePart.action.create')
					}}</span>
				</ACCDButton>
			</div>
		</div>
	</ACCDModal>

	<AccessaryForm></AccessaryForm>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue'
import { cloneDeep } from 'lodash'
import { EFormState } from '@/enums'
import type { ISelectOption } from '@/types'
import { getCustomerTypes } from '@/modules/customerType/api'
import { CDForm, CDFormItem, CDAIcon } from '@cd/design-system'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'
import { preFilterInputNumberOnly } from '@/modules/sharedModules/component/constants'
import { getCustomers } from '@/modules/customer/api'

import {
	create,
	update,
	updateServiceTicketHandling,
} from '@/modules/sellSparePart/api'
import { detailPriceByCustomerTypeAndProductAndService } from '@/modules/productPrice/api'
import { getParentProduct } from '@/modules/accessary/api'
import { emitter } from '@/utils/mitt'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'
import AccessaryForm from '@/modules/accessary/pages/AccessaryForm.vue'

import {
	countOriginPrice,
	countTotalPrice,
	formatPriceVN,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { watch } from 'vue'

const { $t } = useI18n()
const { $toast } = useToast()
const MAX_INT = import.meta.env.VITE_MAX_INTEGER

const item = ref<SellSparePart>({
	id: 0,
	sellCode: '',
	customerId: undefined,
	customerName: '',
	customerPhone: '',
	customerTypeId: undefined,
	createdAt: new Date(),
	deliveryStatus: 1,
	paymentStatus: undefined,
	totalPrice: 0,
	originalPrice: 0,
	tax: 0,
	discount: 0,
	discountType: 2,
	products: [],
	addProducts: [],
	removeProducts: [],
})

const products = ref<SellSparePartProduct[]>([])
const count = ref(0)
const totalPrice = ref(0)
const originalPrice = ref(0)
const tax = ref(0)
const discount = ref<number>(0)
const responseErrorMessages = ref('')
const isEmptyProductQuanity = ref<boolean>(false)
const isEmptyProduct = ref<boolean>(false)
const isChangeProduct = ref<boolean>(false)
const isDisabled = ref<boolean>(false)

const id = ref<number | null>(null)
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
const discountType = ref(2)
const errorProductMessage = ref<string>()

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	show: boolean
}
const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val: boolean) {
		emit('update:modelValue', val)
	},
})
const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
	(e: 'close'): void
}>()

const props = withDefaults(defineProps<ModalProps>(), {
	state: EFormState.ADD,
})

const columnData = computed(() => {
	return [
		{
			key: 'index',
			headerName: $t('module.sellSparePart.table.stt'),
		},
		{
			key: 'sparePart',
			headerName: $t('module.sellSparePart.form.products.sparePart'),
			minWidth: '250px',
		},

		{
			key: 'unit',
			headerName: $t('module.sellSparePart.form.products.unit'),
			minWidth: '100px',
		},
		{
			key: 'quantity',
			headerName: $t('module.sellSparePart.form.products.quantity'),

			minWidth: '120px',
		},
		{
			key: 'unitPrice',
			headerName: $t('module.sellSparePart.form.products.unitPrice'),
			minWidth: '120px',
		},
		{
			key: 'originalPrice',
			headerName: $t('module.sellSparePart.form.products.originalPrice'),
			minWidth: '120px',
		},
		{
			key: 'discount',
			headerName: $t('module.sellSparePart.form.products.discount'),
			minWidth: '120px',
		},
		{
			key: 'tax',
			headerName: $t('module.sellSparePart.form.products.tax'),
			minWidth: '120px',
		},
		{
			key: 'price',
			headerName: $t('module.sellSparePart.form.products.price'),
			minWidth: '120px',
		},
		{
			key: 'action',
			headerName: '',
		},
	]
})

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

onMounted(async () => {
	for (let i = 1; i < 6; ++i) {
		deliveryStatusOptions.value.push({
			label: $t(`module.sellSparePart.deliveryStatus.${i}`),
			value: i,
			rawValue: null,
		})
	}
})

watch(
	() => products.value,
	() => {
		isEmptyProductQuanity.value = false
		isEmptyProduct.value = false
		responseErrorMessages.value = ''
		if (products.value.length <= 0) {
			isChangeProduct.value = true
		}
		isDisabled.value = false
	},
	{ deep: true }
)

// watch(
// 	() => customer.value.id,
// 	() => {
// 		if (props.state === EFormState.ADD) products.value = []
// 	}
// )

const closeDialog = () => {
	emit('close')
}
const resetForm = () => {
	responseErrorMessages.value = ''
}

const getDifference = (array1: any, array2: any) => {
	return array1.filter((object1: any) => {
		return !array2.some((object2: any) => {
			return object1.outboundProductId == object2.outboundProductId
		})
	})
}

const handleDetailLoading = async () => {
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

	for (let i = 1; i < 6; ++i) {
		deliveryStatusOptions.value.push({
			label: $t(`module.sellSparePart.deliveryStatus.${i}`),
			value: i,
			rawValue: null,
		})
	}
}
await handleDetailLoading()

const onSubmit = async () => {
	isDisabled.value = true
	const result = await v$.value.$validate()
	if (!result) {
		invalid.value = true
		return
	} else {
		invalid.value = false
	}

	if (products.value.length <= 0) {
		isEmptyProduct.value = true
	} else {
		products.value.forEach((item) => {
			if (!item.quantity) {
				isEmptyProductQuanity.value = true
			}
			if (!item.productId) {
				isEmptyProduct.value = true
			}
		})
	}

	item.value.products = products.value.map((p) => {
		let result = { ...p } as any
		Object.keys(p).forEach((k) => {
			if (!p[k as keyof typeof p]) {
				result[k as keyof typeof result] =
					baseSparePart[k as keyof typeof baseSparePart]
			}
		})
		return result
	})

	if (count.value === 0) {
		item.value.customerName = customer.value.fullName
		item.value.customerPhone = customer.value.id
		item.value.customerTypeId = customer.value.customerTypeId
	}
	if (count.value === 1) {
		item.value.customerId = Number(customer.value.id)
	}
	item.value.products.map((product) => (product.tax = product.tax / 100))
	item.value.products.map(
		(products) =>
			(products.discount =
				discountType.value == 1 ? products.discount : products.discount / 100)
	)
	item.value.discount = discount.value
	item.value.tax = tax.value
	item.value.originalPrice = originalPrice.value
	item.value.totalPrice = totalPrice.value
	const data = cloneDeep(item.value) as any

	const intersectionData: any[] = []
	products.value.map((el1) => {
		originProduct.value.map((el2) => {
			if (el1.outboundProductId == el2.outboundProductId) {
				intersectionData.push(el1)
			}
		})
	})

	const add = [
		...getDifference(intersectionData, products.value),
		...getDifference(products.value, intersectionData),
	]
	const remove = [
		...getDifference(intersectionData, originProduct.value),
		...getDifference(originProduct.value, intersectionData),
	]

	if (item.value.deliveryStatus == 2) {
		data.addProducts = add.map((item) => {
			return {
				...item,
				status: 2,
			}
		})
		data.removeProducts = remove.map((item) => {
			return {
				...item,
			}
		})

		if (isEmptyProductQuanity.value || isEmptyProduct.value) {
			return 0
		}

		const res = await updateServiceTicketHandling(data, id.value as number)
		if (res.code === 1) {
			$toast($t('module.sellSparePart.toast.editSuccess'), true)
			setTimeout(() => {
				window.location.reload()
			}, 2000)
		} else {
			$toast(res.message, false)
		}
	} else {
		const params = {
			...data,
			products: data.products.filter((item: any) => item.productId),
		}
		if (isEmptyProductQuanity.value || isEmptyProduct.value) {
			return 0
		}

		const response = id.value
			? await update(params as SellSparePart, id.value)
			: await create(params as SellSparePart, discountType.value)
		const successMessage = id.value
			? $t('module.sellSparePart.toast.editSuccess')
			: $t('module.sellSparePart.toast.createSuccess')

		if (response.code === 1) {
			$toast(successMessage, true)
			resetForm()
			emit('close')
			emit('refresh')
		} else {
			responseErrorMessages.value = '* ' + response.message
		}
	}
}
const baseSparePart = {
	productId: 0,
	quantity: 0,
	unit: '',
	unitPrice: 0,
	originalPrice: 0,
	discount: 0,
	tax: 0,
	status: 0,
	price: 0,
	outboundProductId: null,
	instanceKey: 0,
}
const addSparePart = () => {
	let rowData = cloneDeep(baseSparePart)
	;(rowData.instanceKey =
		products.value.length > 0
			? products.value[products.value.length - 1].instanceKey + 1
			: 0),
		products.value.push(rowData)
}

const deleteProduct = (row: SellSparePartProduct, index: number) => {
	const indexRemove = products.value.indexOf(row)
	products.value.splice(indexRemove, 1)
	totalPrice.value -= row.price
	originalPrice.value -= row.originalPrice
	calcPrice()
}

const onChangeRowValue = (row: SellSparePartProduct, field: string) => {
	totalPrice.value -= row.price
	originalPrice.value -= row.originalPrice
	row.price = countTotalPrice(
		row.quantity,
		row.unitPrice,
		row.discount,
		row.tax,
		discountType.value
	)
	row.originalPrice = countOriginPrice(row.quantity, row.unitPrice)
	totalPrice.value += row.price
	originalPrice.value += row.originalPrice
	calcPrice()
	if (row.price < 0) {
		errorProductMessage.value = $t('module.sellSparePart.error.errorProductMessage')
		isDisabled.value = true
	} else {
		errorProductMessage.value = ''
		isDisabled.value = false
	}
}

const onChangeProduct = async (
	row: SellSparePartProduct,
	col: any,
	field: string
) => {
	if (typeof row.productId == 'number') {
		const result = await detailPriceByCustomerTypeAndProductAndService(
			customer.value.customerTypeId,
			row.productId,
			1
		)

		console.log('dđ', result)
		if (result.code == 1) {
			row.unit = result.data.unit
			row.unitPrice = result.data.price
			row.quantity = 0
			row.discount = 0
			row.tax = 0

			onChangeRowValue(row, field)
		}
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

const originProduct = ref([] as SellSparePartProduct[])

const calcPrice = () => {
	discount.value = 0
	tax.value = 0
	for (let i = 0; i < products.value.length; i++) {
		discount.value =
			Number(discount.value) +
			(discountType.value == 1
				? Number(products.value[i].discount)
				: Number(
						products.value[i].originalPrice * (products.value[i].discount / 100)
				  ))
		discount.value = Number(discount.value)
		tax.value =
			Number(tax.value) +
			(discountType.value == 1
				? Number(
						((products.value[i].originalPrice - products.value[i].discount) *
							products.value[i].tax) /
							100
				  )
				: Number(
						(products.value[i].originalPrice *
							(1 - products.value[i].discount / 100) *
							products.value[i].tax) /
							100
				  ))
		tax.value = Number(tax.value)
	}
}

const handleCreateNewProduct = (value: any, row: any) => {
	const data = { productName: value, intanceKey: row.instanceKey }
	emitter.emit('on-add-new-product', data)
}

emitter.on('on-change-data-product', async (data: any) => {
	let listProduct: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
	})

	if (result.code == 1) {
		listProduct = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}
	productOptions.value = listProduct
	products.value = products.value.map((item) => {
		return {
			...item,
			productId:
				item.instanceKey == data.intanceKey ? data.productId : item.productId,
		}
	})

	const updateRow = products.value.find(
		(item) => item.instanceKey == data.intanceKey
	)
	if (updateRow) {
		await onChangeProduct(updateRow, '', '')
	}
})

emitter.on('on-remove-product', async (product: any) => {
	let listProduct: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
	})

	if (result.code == 1) {
		listProduct = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}

	products.value = products.value.map((item) => {
		if (item.instanceKey == product.intanceKey) {
			return {
				...item,
				productId: {
					options: listProduct,
					value: 0,
				},
				quantity: 0 as number,
				unitPrice: 0 as number,
				money: 0 as number,
				discount: 0,
				tax: 0,
				total: 0,
				price: 0,
			}
		}
		return { ...item }
	})
})

const onRefreshDataService = async () => {
	await handleDetailLoading()
}
</script>

<style scoped></style>
