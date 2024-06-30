<template>
	<div class="flex items-center gap-3 mb-6">
		<ACCDAIcon name="ArrowLeft" class="cursor-pointer" @click="handleBack" />
		<p class="font-bold text-[16px]">
			{{ $t('module.inventory.order.form.view') }}
		</p>
	</div>
	<div
		:class="deliveryStatus.className"
		v-if="+route.params.type == EFormState.VIEW"
	>
		{{ deliveryStatus.title }}
	</div>

	<div class="p-[16px] bg-white rounded">
		<div>
			<ACCDForm :show-footer="false" class="w-full">
				<div class="flex flex-col lg:flex-row">
					<ACCDFormItem
						:label="
							$t('module.debt.orderDistributorController.form.distributorId')
						"
						required
						class="w-full lg:w-1/4 mr-4 font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="
								$t('module.debt.orderDistributorController.form.distributorId')
							"
							v-model="distributorInfo.distributorCode"
							:disabled="true"
							class="font-medium"
						/>
					</ACCDFormItem>
					<ACCDFormItem
						:label="
							$t('module.debt.orderDistributorController.form.distributorName')
						"
						required
						class="w-full lg:w-1/4 mr-4 font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="
								$t(
									'module.debt.orderDistributorController.form.distributorName'
								)
							"
							v-model="distributorInfo.distributorName"
							:disabled="true"
							class="font-medium"
						/>
					</ACCDFormItem>
					<ACCDFormItem
						:label="
							$t('module.debt.orderDistributorController.form.phoneNumber')
						"
						required
						class="w-full lg:w-1/4 mr-4 font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="
								$t('module.debt.orderDistributorController.form.phoneNumber')
							"
							v-model="distributorInfo.distributorContact"
							:disabled="true"
							class="font-medium"
						/>
					</ACCDFormItem>
					<ACCDFormItem
						:label="
							$t('module.debt.orderDistributorController.form.deliveryType')
						"
						required
						class="w-full lg:w-1/4 font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="
								$t('module.debt.orderDistributorController.form.deliveryType')
							"
							v-model="originData.deliveryType"
							:disabled="true"
							class="font-medium"
						/>
					</ACCDFormItem>
				</div>
				<div
					class="flex justify-end"
					v-if="+route.params.type == EFormState.EDIT"
				>
					<ACCDFormItem
						:label="$t('module.debt.order.modal.paymentStatus')"
						required
						class="w-full lg:w-1/4 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.debt.order.modal.paymentStatus')"
							v-model="originData.paymentStatus"
							:options="paymentStatusOptions"
							:disabled="route.path.includes('view')"
							class="font-medium"
						/>
					</ACCDFormItem>
				</div>
			</ACCDForm>
		</div>

		<div>
			<div class="flex justify-between">
				<h1 class="text-gray-900 font-semibold text-lg pb-1 mb-4">
					{{ $t('module.sellSparePart.form.products.title') }}
				</h1>
				<!-- <ACCDButton
					type="primary"
					size="md"
					v-if="$route.path.includes('view')"
				>
					{{ $t('module.inventory.order.form.printBill') }}
				</ACCDButton> -->
			</div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="originData.products"
			>
				<template #cell-id="{ row, col, field }">
					{{ productOptions.find((a) => a.value == row.productId)?.label }}
				</template>
				<template #cell-unit="{ row, col, field }">
					{{ row.unit }}
				</template>
				<template #cell-quantity="{ row, col, field }">
					{{ formatPriceVN(row.quantity) }}
				</template>
				<template #cell-unitPrice="{ row, col, field }">
					{{ formatPriceVN(row.unitPrice) }}
				</template>
				<template #cell-originalPrice="{ row, col, field }">
					{{
						Math.ceil(row.unitPrice * row.quantity)
							.toString()
							.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
					}}
				</template>
				<template #cell-discount="{ row, col, field }">
					{{ row.discount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
				</template>
				<template #cell-tax="{ row, col, field }">
					{{ Number(row.tax) }}%
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
		<ACCDButton variant="fill" type="secondary" @click="handleBack">
			<span class="text-info-base font-medium">{{
				$t('module.customerType.action.back')
			}}</span>
		</ACCDButton>
		<ACCDButton
			v-if="$route.path.includes('edit')"
			type="primary"
			variant="fill"
			@click="onSubmit"
		>
			<span class="text-white font-medium">{{
				$t('module.customerType.action.save')
			}}</span>
		</ACCDButton>
	</div>
	<OrderDistributorForm
		v-if="onShowBill"
		@close="onShowBill = false"
		:productInfo="originData.products"
		:distributorName="distributorInfo.distributorName"
		:distributorContactPhone="distributorInfo.contactPhone"
	>
	</OrderDistributorForm>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue'
import OrderDistributorForm from '@/modules/sharedModules/pages/formHTML/OrderDistributorForm.vue'
import type { IDistributor, ISelectOption } from '@/types'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'
import {
	getDetailOrderDistributorController,
	updatePaymentStatusDistributorController,
} from '@/modules/debt/api'
import { getListDistributors } from '@/modules/distributor/api'
import { getParentProduct } from '@/modules/accessary/api'
import { emitter } from '@/utils/mitt'
import {
	formatPriceVN,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { useRoute, useRouter } from 'vue-router'
import router from '@/router'
import { EFormState } from '@/enums'
const { $t } = useI18n()
const route = useRoute()
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

const totalPrice = ref(0)
const originalPrice = ref(0)
const tax = ref(0)
const discount = ref<number>(0)
const responseErrorMessages = ref('')
const onShowBill = ref(false)

const deliveryStatusOptions = ref<ISelectOption[]>([])
const productOptions = ref<ISelectOption[]>([])
const invalid = ref(false)

const originData = ref({
	id: 0,
	orderCode: 'string',
	distributorId: 0,
	deliveryType: 'string',
	phoneNumber: '',
	distributorName: '',
	deliveryStatus: 0,
	paymentStatus: 0,
	discount: 0,
	tax: 0,
	totalPrice: 0,
	garageId: 0,
	inventoryId: 0,
	products: [] as SellSparePartProduct[],
})

const distributorInfo = computed(() => {
	let distributorId = originData.value.distributorId
	let distributor = listDistributors.value.find((a: any) => {
		return a.id == distributorId
	})

	return distributor ? distributor : ({} as any)
})

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const columnData = [
	{
		key: 'id',
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
		minWidth: '80px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.sellSparePart.form.products.unitPrice'),
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
	},
	{
		key: 'price',
		headerName: $t('module.sellSparePart.form.products.price'),
	},
]

const instanceKey = new Date().getTime()
const listDistributors = ref([])

const deliveryStatus = computed(() => {
	const status = {
		title: '',
		className: '',
	}

	switch (originData.value.paymentStatus) {
		case 1:
			status.title = $t(`module.debt.serviceTicket.paymentStatus.1`)
			status.className = 'status-new-service-1 badge-status'
			break

		case 3:
			status.title = $t(`module.debt.serviceTicket.paymentStatus.3`)
			status.className = 'status-new-service-6 badge-status'
			break

		default:
			break
	}

	return status
})

const handleGetDetailLoading = async () => {
	const id = route.params.id.toString()
	const type = route.params.type.toString()
	if (id) {
		const result = await getDetailOrderDistributorController(Number(id))
		originData.value = result.data
		totalPrice.value = originData.value.totalPrice as number
		let computedOriginalPrice = 0
		let computedTax = 0
		let computedDiscount = 0
		originData.value.products.forEach((p) => {
			p.tax = p.tax * 100
			computedOriginalPrice += p.quantity * p.unitPrice
			computedDiscount += p.discount
			computedTax += (p.quantity * p.unitPrice - p.discount) * p.tax
		})
		discount.value = computedDiscount
		tax.value = computedTax / 100
		originalPrice.value = computedOriginalPrice
	}

	let productList = await getParentProduct({
		pageSize: MAX_INT,
		pageNumber: 1,
	})
	productOptions.value = productList.data.map((a: any) => {
		return {
			label: `${a.code} - ${a.name}`,
			value: a.id,
		}
	})
	getListDistributors({
		pageSize: 100000,
		pageNumber: 1,
	}).then((res) => {
		listDistributors.value = res.data.map((a: IDistributor) => {
			return {
				id: a.id,
				distributorName: a.name,
				distributorContact: a.contactPhone,
				distributorCode: a.code,
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

await handleGetDetailLoading()
const handleBack = () => {
	router.back()
}

const onSubmit = async () => {
	const id = route.params.id.toString()
	item.value.products.map((product) => {
		product.tax = product.tax / 100
	})
	let data = originData.value
	const response = await updatePaymentStatusDistributorController(
		id,
		originData.value.paymentStatus
	)
	const successMessage = $t(
		'module.debt.serviceTicket.form.toast.updateSellOrder'
	)
	if (response.code === 1) {
		$toast(successMessage, true)
		setTimeout(() => {
			router.push('/app/debt/orders-distributor-controller')
		}, 3000)
	} else {
		responseErrorMessages.value = '* ' + response.message
	}
}
</script>

<style scoped></style>
