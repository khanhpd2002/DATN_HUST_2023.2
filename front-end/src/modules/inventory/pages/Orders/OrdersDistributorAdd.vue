<template>
	<div class="cd-modal__body py-4">
		<div class="flex items-center gap-3 mb-6">
			<ACCDAIcon name="ArrowLeft" class="cursor-pointer" @click="handleBack" />
			<p class="font-bold text-[16px]">
				{{
					cloneProps.state == EFormState.ADD
						? $t('module.inventory.order.form.create')
						: $t('module.inventory.order.form.view')
				}}
			</p>
			<p class="text-[16px] text-primary">{{ data.orderCode }}</p>
		</div>

		<div
			v-if="cloneProps.state == EFormState.EDIT"
			:class="deliveryStatus.className"
		>
			{{ deliveryStatus.title }}
		</div>

		<div class="bg-white rounded p-[16px]">
			<ACCDForm :show-footer="false" class="w-full">
				<div class="flex flex-col gap-4 py-4">
					<WrapFlexContainer>
						<ACCDFormItem
							:label="$t('module.inventory.order.form.distributorCode')"
							required
							class="w-full lg:w-1/4 font-semibold"
						>
							<ACCDSelect
								:placeholder="$t('module.inventory.order.form.distributorCode')"
								:disabled="cloneProps.state == EFormState.VIEW || id"
								:options="listDistributor"
								v-model="data.distributorId"
								class="font-medium"
								@change="(val: any) => onSelectDistributor(val)"
								@update:modelValue="(val: any) => handleUpdateDistributor(val)"
							>
							</ACCDSelect>
						</ACCDFormItem>
						<ACCDFormItem
							:label="$t('module.inventory.order.form.distributorName')"
							required
							class="w-full lg:w-1/4 font-semibold"
						>
							<!-- <ACCDSelect
								:disabled="data.distributorId"
								:options="listDistributorName"
								:placeholder="$t('module.inventory.order.form.distributorName')"
								v-model="data.distributorId"
								class="font-medium"
							>
							</ACCDSelect> -->

							<ACCDInputText
								:disabled="cloneProps.state == EFormState.VIEW || id"
								:readonly="true"
								:placeholder="$t('module.inventory.order.form.distributorName')"
								v-model="distributorInfo.distributorName"
								class="font-medium"
							>
							</ACCDInputText>
						</ACCDFormItem>
						<ACCDFormItem
							:label="$t('module.inventory.order.form.contactPhone')"
							required
							class="w-full lg:w-1/4 font-semibold"
						>
							<ACCDInputText
								:disabled="cloneProps.state == EFormState.VIEW || id"
								:readonly="true"
								:placeholder="$t('module.inventory.order.form.contactPhone')"
								v-model="distributorInfo.contactPhone"
								class="font-medium"
							>
							</ACCDInputText>
						</ACCDFormItem>
						<ACCDFormItem
							:label="$t('module.inventory.order.form.deliveryType')"
							class="w-full lg:w-1/4 font-semibold"
						>
							<ACCDInputText
								:disabled="cloneProps.state == EFormState.VIEW || id"
								:placeholder="$t('module.inventory.order.form.deliveryType')"
								v-model="data.deliveryType"
								class="font-medium"
								:trim="true"
							>
							</ACCDInputText>
						</ACCDFormItem>
					</WrapFlexContainer>
				</div>
				<!-- <p class="font-bold">
                    {{ $t("module.inventory.order.form.deliveryStatus") }}
                </p>
                <div class="p-[45px]">
                    <CDStep
                        :items="computedDeliveryStatusStepper"
                        :current="
                            computedDeliveryStatusStepper.findIndex(
                                (a) => a.key == data.deliveryStatus
                            )
                        "
                        :disabled="
                            cloneProps.state === EFormState.VIEW ||
                            cloneProps.state === EFormState.ADD
                        "
                        class="font-medium"
                    >
                    </CDStep>
                </div>
                <div>
                    <ACCDButton
                        type="secondary"
                        @click="() => onClickStep(4)"
                        :disabled="
                            cloneProps.state === EFormState.VIEW ||
                            cloneProps.state === EFormState.ADD ||
                            data.deliveryStatus != 4
                        "
                    >
                        Hoàn trả</ACCDButton
                    >
                </div> -->
			</ACCDForm>

			<div>
				<h1 class="font-semibold text-[16px] pb-1">
					{{ $t('module.sellSparePart.form.products.title') }}
				</h1>
				<ACCDTable :columns="columnData" :rowData="products">
					<template
						#cell-action="{ row, rowIndex }"
						v-if="
							data.deliveryStatus != 4 &&
							data.deliveryStatus != 5 &&
							data.deliveryStatus != 6
						"
					>
						<td>
							<ACCDAIcon
								@click="
									() => {
										deleteProductRow(row, rowIndex)
									}
								"
								class="text-xl text-gray-700 cursor-pointer"
								name="Trash"
								size="20"
							></ACCDAIcon>
						</td>
					</template>
					<template #cell-productId="{ row, rowIndex }">
						<span
							v-if="
								cloneProps.state == EFormState.VIEW ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
							"
						>
							{{
								listProduct.find((item: any) => item.value == row.productId)
									?.label
							}}</span
						>

						<ACCDSelect
							v-else
							v-model="row.productId"
							:options="listProduct"
							@change="(val: any) => onSelectProduct(row, val)"
							:forCreate="true"
							@createNew="(value) => handleCreateNewProduct(value, row)"
						>
						</ACCDSelect>
					</template>
					<template #cell-quantity="{ row, rowIndex, field }">
						<div
							v-if="
								data.deliveryStatus != 1 &&
								data.deliveryStatus != 2 &&
								data.deliveryStatus != 3
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
							@keypress="preFilterInputNumberOnly"
							:readonly="
								cloneProps.state == EFormState.VIEW ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
							"
						/>
					</template>
					<template #cell-discount="{ row, rowIndex, field }">
						<div
							v-if="
								(row.disable ||
									(data.deliveryStatus != 1 &&
										data.deliveryStatus != 2 &&
										data.deliveryStatus != 3)) &&
								discountType == 1
							"
							class="whitespace-nowrap"
						>
							{{ formatPriceVN(row.discount) }} VNĐ
						</div>
						<div
							v-if="
								(row.disable ||
									(data.deliveryStatus != 1 &&
										data.deliveryStatus != 2 &&
										data.deliveryStatus != 3)) &&
								discountType == 2
							"
							class="whitespace-nowrap"
						>
							{{ formatPriceVN(row.discount) }} %
						</div>
						<ACCDInputPrice
							v-if="
								data.deliveryStatus == 1 ||
								data.deliveryStatus == 2 ||
								data.deliveryStatus == 3
							"
							size="md"
							class="custom-in-table"
							:placeholder="$t('module.sellSparePart.form.products.discount')"
							v-model="row.discount"
							@change="() => onChangeRowValue(row, field)"
							@keypress="validatePrice"
							:readonly="
								cloneProps.state == EFormState.VIEW ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
							"
						>
							<template #inner-append>
								<span class="px-2" v-if="discountType == 1">VNĐ</span>
								<span class="px-2" v-if="discountType == 2">%</span>
							</template>
						</ACCDInputPrice>
					</template>
					<template #cell-tax="{ row, rowIndex, field }">
						<div
							class="whitespace-nowrap"
							v-if="
								row.disable ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
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
							@keypress="preFilterInputNumberOnly"
							:readonly="
								cloneProps.state == EFormState.VIEW ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
							"
						>
							<template #inner-append><span class="px-2"> %</span></template>
						</ACCDInputText>
					</template>
					<template #cell-price="{ row, rowIndex }">
						<td class="w-36">
							{{ formatPriceVN(row.price) }}
						</td>
					</template>
					<template #cell-unitPrice="{ row, field }">
						<div
							v-if="
								row.disable ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
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
								cloneProps.state == EFormState.VIEW ||
								(data.deliveryStatus != 1 &&
									data.deliveryStatus != 2 &&
									data.deliveryStatus != 3)
							"
							@keypress="validatePrice"
						/>
					</template>

					<template #cell-originalPrice="{ row, col, field }">
						{{ formatPriceVN(row.originalPrice) }}
					</template>
					<template #cell-status="{ row }">
						<p
							class="py-[8px] px-[12px] rounded-[16px] flex items-center text-center whitespace-nowrap w-fit"
							:class="`status-new-service-${row.status == 1 ? '6' : '0'}`"
						>
							<span class="mr-1">
								{{ row.status == 1 ? 'Đã nhập kho' : 'Chưa nhập kho' }}
							</span>
						</p>
					</template>
				</ACCDTable>

				<div
					v-if="
						cloneProps.state !== EFormState.VIEW &&
						(data.deliveryStatus == 1 ||
							data.deliveryStatus == 2 ||
							data.deliveryStatus == 3)
					"
					class="flex items-center text-active mt-2"
				>
					<span class="cursor-pointer mr-1" @click="addProductRowData">
						<ACCDAIcon name="AddCircle" />
					</span>
					<span class="cursor-pointer font-medium" @click="addProductRowData">
						{{ $t('module.sellSparePart.form.products.addSparePart') }}</span
					>
				</div>

				<div v-if="invalid">
					<span class="italic text-red-500">{{
						$t('module.sellSparePart.error.missingRequiredField')
					}}</span>
				</div>
				<div v-if="errorMessage">
					<span class="italic text-red-500">
						{{ errorMessage }}
					</span>
				</div>
				<div v-if="errorMsgRequiredProduct">
					<span class="italic text-red-500">
						{{ errorMsgRequiredProduct }}
					</span>
				</div>

				<div v-if="errorMsgRequiredProductQuality">
					<span class="italic text-red-500">
						{{ errorMsgRequiredProductQuality }}
					</span>
				</div>

				<div v-if="errorProductMessage">
					<span class="italic text-red-500">
						{{ errorProductMessage }}
					</span>
				</div>

				<div class="w-1/5 ml-auto py-3">
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

				<!-- <div class="flex justify-end gap-20">
                    <div class="flex flex-col datas-end">
                        <p class="font-semibold">
                            {{ $t("module.sellSparePart.form.totalPrice") }}
                        </p>
                        <p>
                            {{ $t("module.sellSparePart.form.originalPrice") }}
                        </p>
                        <p>{{ $t("module.sellSparePart.form.tax") }}</p>
                        <p>{{ $t("module.sellSparePart.form.discount") }}</p>
                    </div>
                    <div class="flex flex-col justify-end">
                        <p class="font-semibold">
                            {{ formatPriceVN(totalPrice) }}
                        </p>
                        <p>{{ formatPriceVN(originalPrice) }}</p>
                        <p>{{ formatPriceVN(tax) }}</p>
                        <p>{{ formatPriceVN(discount) }}</p>
                    </div>
                </div> -->
			</div>
		</div>
	</div>

	<div>
		<div
			v-if="cloneProps.state == EFormState.ADD"
			class="flex justify-end gap-2.5 w-full"
		>
			<ACCDButton type="secondary" variant="fill" @click="closeDialog">
				<span class="text-info-base font-medium">Hủy đơn</span>
			</ACCDButton>
			<ACCDButton
				type="primary"
				variant="fill"
				@click="onSubmit"
				:disabled="isDisabled"
			>
				<span class="text-white font-medium">{{
					$t('module.customerType.action.save')
				}}</span>
			</ACCDButton>
		</div>

		<div v-else class="flex justify-end gap-2.5 w-full">
			<ACCDButton
				type="secondary"
				@click="() => onClickStep(6)"
				v-if="
					data.deliveryStatus !== 4 &&
					data.deliveryStatus !== 5 &&
					data.deliveryStatus !== 6
				"
			>
				<span class="text-info-base font-medium">Hủy đơn</span>
			</ACCDButton>

			<!-- <ACCDButton
						variant="fill"
						type="secondary"
						@click="onSubmit"
						v-if="
							data.deliveryStatus == 1 ||
							data.deliveryStatus == 2 ||
							data.deliveryStatus == 3
						"
					>
						<span class="text-info-base font-medium">Lưu</span>
					</ACCDButton>
					<ACCDButton
						variant="fill"
						type="primary"
						@click="
							() => {
								onClickStep(1)
							}
						"
						v-if="data.deliveryStatus == 1"
					>
						<span class="text-info-base font-medium">Xử lý đơn</span>
					</ACCDButton>
					<ACCDButton
						variant="fill"
						type="primary"
						@click="
							() => {
								onClickStep(2)
							}
						"
						v-else-if="data.deliveryStatus == 2"
					>
						<span class="text-info-base font-medium">Giao hàng</span>
					</ACCDButton> -->

			<ACCDButton
				variant="fill"
				type="primary"
				@click="
					() => {
						onClickStep(3)
					}
				"
				v-if="
					data.deliveryStatus !== 4 &&
					data.deliveryStatus !== 5 &&
					data.deliveryStatus !== 6
				"
			>
				<span class="text-info-base font-medium">Đã nhận hàng</span>
			</ACCDButton>
		</div>

		<div class="flex justify-end">
			<!-- <ACCDButton
						variant="fill"
						type="primary"
						@click="() => router.push('/app/inventory/order-spare-parts')"
						v-if="data.deliveryStatus == 4"
					>
						<span class="text-info-base font-medium">Đóng</span>
					</ACCDButton> -->

			<ACCDButton
				type="secondary"
				@click="() => onClickStep(4)"
				v-if="data.deliveryStatus == 4"
				class="bg-error text-white border-none"
			>
				<span class="font-medium">Hoàn hàng</span>
			</ACCDButton>
		</div>
	</div>
	<ACCDModal
		v-model="warningDialog.showWarningDialog"
		class-width="md:w-[600px]"
		:hidefooter="true"
	>
		<div class="custom-modal flex flex-col gap-4">
			<div class="flex flex-col gap-2 pt-4 bg-layout-primary">
				<p
					class="font-semibold text-[16px]"
					:class="warningDialog.titleClass"
					:style="warningDialog.warningStyleTitle"
				>
					{{ warningDialog.title }}
				</p>
				<p class="font-medium">{{ warningDialog.content }}</p>
			</div>
			<div class="flex gap-2 w-full justify-center">
				<ACCDButton
					size="sm"
					@click="warningDialog.showWarningDialog = false"
					type="secondary"
				>
					Không
				</ACCDButton>
				<ACCDButton
					size="sm"
					@click="warningDialog.confirm"
					:class="warningDialog.actionType == 'danger' && 'bg-error'"
				>
					{{ warningDialog.actionType == 'danger' ? 'Hủy' : 'Hoàn Trả' }}
				</ACCDButton>
			</div>
		</div>
	</ACCDModal>
	<AccessaryForm></AccessaryForm>
</template>

<script setup lang="ts">
// import { CDStep } from '@cd/design-system'

import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import AccessaryForm from '@/modules/accessary/pages/AccessaryForm.vue'
import { getListDistributors } from '@/modules/distributor/api'
import { EFormState } from '@/enums'
import { computed, ref, watch } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'

import { getParentProduct } from '@/modules/accessary/api'

import { cloneDeep } from 'lodash'
import { useRoute } from 'vue-router'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { emitter } from '@/utils/mitt'
import {
	countOriginPrice,
	countTotalPrice,
	formatPriceVN,
	preFilterInputNumberOnly,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import {
	cancelUpdateOrder,
	createNewOrderDistributor,
	findOrderDistributor,
	handleReceiveProductOrder,
	handleRefundOrder,
	handlingUpdateOrder,
	updateOrderReceived,
} from '@/modules/inventory/api'
import router from '@/router'
import {
	OrderDistributor,
	OrderDistributorProduct,
} from '@/types/OrdersDistributor'
import { saveLogTracking } from '@/modules/sharedModules/api'

const { $t } = useI18n()
const { $toast } = useToast()

const route = useRoute()

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	orderId?: number | string
}

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	state: EFormState.ADD,
})

const distributorInfo = ref({
	contactPhone: '',
	distributorName: '',
})
const invalid = ref(false)
const errorProductMessage = ref<string>()

const columnData = computed(() => {
	if (id.value && cloneProps.value.state == EFormState.EDIT) {
		return [
			{
				key: 'productId',
				headerName: $t('module.inventory.order.form.table.productId'),
				minWidth: '250px',
			},
			{
				key: 'unit',
				headerName: $t('module.inventory.order.form.table.unit'),
				minWidth: '80px',
			},
			{
				key: 'quantity',
				headerName: $t('module.inventory.order.form.table.quantity'),
				minWidth: '100px',
			},
			{
				key: 'unitPrice',
				headerName: $t('module.inventory.order.form.table.unitPrice'),
			},
			{
				key: 'originalPrice',
				headerName: $t('module.inventory.order.form.table.totalPrice'),
			},
			{
				key: 'discount',
				headerName: $t('module.inventory.order.form.table.discount'),
			},
			{
				key: 'tax',
				headerName: $t('module.inventory.order.form.table.tax'),
			},
			{
				key: 'price',
				headerName: $t('module.inventory.order.form.table.price'),
			},

			{
				key: 'status',
				headerName: $t('module.inventory.order.form.table.status'),
			},
			{
				key: 'action',
				headerName: '',
			},
		]
	} else {
		return [
			{
				key: 'productId',
				headerName: $t('module.inventory.order.form.table.productId'),
				minWidth: '250px',
			},
			{
				key: 'unit',
				headerName: $t('module.inventory.order.form.table.unit'),
				minWidth: '80px',
			},
			{
				key: 'quantity',
				headerName: $t('module.inventory.order.form.table.quantity'),
				minWidth: '100px',
			},
			{
				key: 'unitPrice',
				headerName: $t('module.inventory.order.form.table.unitPrice'),
			},
			{
				key: 'originalPrice',
				headerName: $t('module.inventory.order.form.table.totalPrice'),
			},
			{
				key: 'discount',
				headerName: $t('module.inventory.order.form.table.discount'),
			},
			{
				key: 'tax',
				headerName: $t('module.inventory.order.form.table.tax'),
			},
			{
				key: 'price',
				headerName: $t('module.inventory.order.form.table.price'),
			},

			{
				key: 'action',
				headerName: '',
			},
		]
	}
})

const isDisabled = ref<boolean>(false)
const discountType = ref(2)

const data = ref<OrderDistributor>({
	id: 0,
	orderCode: '',
	deliveryStatus: 1,
	paymentStatus: undefined,
	distributorId: 0,
	distributorCode: '',
	distributorName: '',
	distributorPhone: '',
	deliveryType: '',
	discount: 0,
	discountType: 2,
	tax: 0,
	totalPrice: 0,
	inventoryId: 0,
	products: [],
	addProducts: [],
	removeProducts: [],
})
const baseProduct = {
	productId: 0,
	unit: '',
	quantity: 0,
	unitPrice: 0,
	discount: 0,
	tax: 0,
	originalPrice: 0,
	price: 0,
	status: 0,
	instanceKey: 0,
}

const products = ref<OrderDistributorProduct[]>([])
const totalPrice = ref(0)
const originalPrice = ref(0)
const tax = ref(0)
const discount = ref<number>(0)
const errorMessage = ref('')
const errorMsgRequiredProduct = ref('')
const errorMsgRequiredProductQuality = ref('')

const stateView = computed(() => {
	return props.state
})
const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val: boolean) {
		emit('update:modelValue', val)
	},
})

const computedTitle = computed(() => {
	if (stateView.value == EFormState.ADD) {
		return $t('module.inventory.order.form.create')
	}
	if (stateView.value == EFormState.EDIT) {
		return $t('module.inventory.order.form.edit')
	}
	if (stateView.value == EFormState.VIEW) {
		return $t('module.inventory.order.form.view')
	}
})

const id = ref<number | null>(null)

const validations = computed(() => {
	return {
		data: {
			distributorId: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			deliveryStatus: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
		},
		distributorInfo: {
			contactPhone: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
		},
	}
})
const v$ = useVuelidate(validations, { data, distributorInfo })
const instanceKey = new Date().getTime()
const deliveryStatusStepper = ref([
	{
		key: 1,
		title: $t(`module.inventory.order.deliveryStatus.1`),
	},
	{
		key: 2,
		title: 'Đang xử lý',
	},
	{
		key: 3,
		title: $t(`module.inventory.order.deliveryStatus.3`),
	},
	{
		key: 4,
		title: $t(`module.inventory.order.deliveryStatus.4`),
	},
])

const deliveryStatus = computed(() => {
	const status = {
		title: '',
		className: '',
	}

	switch (data.value.deliveryStatus) {
		case 1:
			status.title = $t(`module.inventory.order.deliveryStatus.1`)
			status.className = 'status-new-service-0 badge-status'
			break
		case 2:
			status.title = $t(`module.inventory.order.deliveryStatus.2`)
			status.className = 'status-new-service-1 badge-status'
			break
		case 3:
			status.title = $t(`module.inventory.order.deliveryStatus.3`)
			status.className = 'status-new-service-1 badge-status'
			break
		case 4:
			status.title = $t(`module.inventory.order.deliveryStatus.4`)
			status.className = 'status-new-service-6 badge-status'
			break
		case 5:
			status.title = $t(`module.inventory.order.deliveryStatus.5`)
			status.className = 'status-new-service-8 badge-status'
			break
		case 6:
			status.title = $t(`module.inventory.order.deliveryStatus.6`)
			status.className = 'status-new-service-8 badge-status'
			break

		default:
			break
	}

	return status
})

const computedDeliveryStatusStepper = computed(() => {
	const cloneDeliveryStatusStepper = cloneDeep(deliveryStatusStepper.value)
	if (
		cloneProps.value.state === EFormState.ADD ||
		cloneProps.value.state === EFormState.VIEW
	) {
		return cloneDeliveryStatusStepper.map(
			(a: { key: number; title: string }) => {
				return { ...a, disabled: true }
			}
		)
	} else {
		const result: {
			key: number
			title: string
			disabled?: boolean
			activeClass?: string
			activeIcon?: string
		}[] = cloneDeliveryStatusStepper.map(
			(a: { key: number; title: string }) => {
				return {
					...a,
					disabled: data.value.deliveryStatus
						? a.key !== data.value.deliveryStatus + 1
						: true,
				}
			}
		)
		if (data.value.deliveryStatus == 5) {
			result.push({
				key: 5,
				title: 'Đã hoàn trả',
				activeClass: 'bg-red-400 text-white',
				activeIcon: 'fa-solid fa-rotate-left',
			})
		}
		if (data.value.deliveryStatus == 6) {
			result.push({
				key: 6,
				title: 'Đã hủy',
				activeClass: 'bg-red-400 text-white',
				activeIcon: 'fa-solid fa-xmark',
			})
		}

		return result
	}
})

const cloneProps = ref({
	state: EFormState.ADD,
	orderDistributorId: 0,
})

const listDistributor = ref([])
const listDistributorName = ref([])
const originProduct = ref([] as OrderDistributorProduct[])

const listProduct = ref<any[]>([])
const isChangeProduct = ref<boolean>(false)

watch(
	() => products.value,
	() => {
		errorMessage.value = ''
		errorMsgRequiredProduct.value = ''
		errorMsgRequiredProductQuality.value = ''
		isDisabled.value = false
	},
	{ deep: true }
)

const deleteProductRow = (row: OrderDistributorProduct, index: number) => {
	const indexRemove = products.value.indexOf(row)
	products.value.splice(indexRemove, 1)
	totalPrice.value -= row.price
	originalPrice.value -= row.originalPrice
	calcPrice()
}

const addProductRowData = () => {
	// data.value.products.push(cloneDeep(baseProduct));
	let rowData = cloneDeep(baseProduct)
	;(rowData.instanceKey =
		products.value.length > 0
			? products.value[products.value.length - 1].instanceKey + 1
			: 0),
		products.value.push(rowData)
	// products.value.push(cloneDeep(baseProduct));
}

const onSelectProduct = (row: any, productId: number) => {
	let originProduct = listProduct.value.find((p: any) => {
		return p.value == productId
	}) as any

	if (originProduct) {
		row.unit = originProduct.origin.unit
		row.unitPrice = originProduct.origin.purchasePrice
	}
}

const onClickStep = async (val: number) => {
	let res: any
	const result = await v$.value.$validate()
	if (!result) {
		invalid.value = true
		return
	} else {
		invalid.value = false
	}

	products.value.forEach((item) => {
		if (!item.quantity) {
			errorMsgRequiredProductQuality.value = $t(
				'module.inventory.order.error.emptyProductQuality'
			)
		}
		if (!item.productId) {
			errorMsgRequiredProduct.value = $t(
				'module.inventory.order.error.emptyProduct'
			)
		}
	})
	data.value.products = products.value
		.filter((item) => item.productId && item.quantity)
		.map((p) => {
			let result = { ...p } as any
			Object.keys(p).forEach((k) => {
				if (!p[k as keyof typeof p]) {
					result[k as keyof typeof result] =
						baseProduct[k as keyof typeof baseProduct]
				}
				if (k == 'discount') {
					result['discount'] =
						discountType.value == 1 ? p.discount : p.discount / 100
				}
			})
			return result
		})

	data.value.products.map((product) => (product.tax = product.tax / 100))
	data.value.discount = discount.value
	data.value.tax = tax.value
	data.value.originalPrice = originalPrice.value
	data.value.totalPrice = totalPrice.value
	const temp = cloneDeep(data.value) as any
	temp.addProducts = []
	temp.removeProducts = []
	for (let i of temp.products) {
		//product nayf khoong ton tai trong origin => da duoc them

		if (
			originProduct.value.findIndex((op) => op.productId == i.productId) == -1
		) {
			temp.addProducts.push(i)
		}
	}
	for (let i of originProduct.value) {
		if (
			temp.products.findIndex(
				(op: any) => op.inboundProductId == i.inboundProductId
			) == -1
		) {
			temp.removeProducts.push(i)
		}
	}
	if (errorMsgRequiredProduct.value || errorMsgRequiredProductQuality.value) {
		return 0
	}
	switch (val) {
		// case 1:
		// 	res = await handlingUpdateOrder(data.value.id, temp)

		// 	if (res && res.code === 1) {
		// 		$toast($t('module.inventory.order.toast.statusChange'), true)
		// 		data.value.deliveryStatus = 2
		// 		window.location.reload()
		// 	} else {
		// 		$toast(res.message, false)
		// 	}
		// 	break
		// case 2:
		// 	res = await handleDeliveryUpdateOrder(data.value.id, temp)
		// 	if (res && res.code === 1) {
		// 		$toast($t('module.inventory.order.toast.statusChange'), true)
		// 		data.value.deliveryStatus = 3
		// 		window.location.reload()
		// 	} else {
		// 		$toast(res.message, false)
		// 	}
		// 	break
		case 3:
			res = await handleReceiveProductOrder(data.value.id, temp)
			if (res && res.code === 1) {
				$toast($t('module.inventory.order.toast.statusChange'), true)
				data.value.deliveryStatus = 4
				// window.location.reload()
			} else {
				$toast(res.message, false)
			}
			break
		case 4:
			warningDialog.value.title = $t(
				'module.inventory.order.form.confirmReturn'
			)
			warningDialog.value.content = $t(
				'module.inventory.order.form.returnOrder'
			)
			warningDialog.value.warningStyleTitle = 'color:#25B3E8; font-size:16px'
			warningDialog.value.actionType = 'info'
			warningDialog.value.titleClass = 'font-semibold'
			warningDialog.value.confirm = async () => {
				res = await handleRefundOrder(data.value.id)
				if (res && res.code === 1) {
					$toast('Đã hoàn trả', true)
					data.value.deliveryStatus = 5
					window.location.reload()
				} else {
					$toast(res.message, false)
				}
				warningDialog.value.showWarningDialog = false
			}
			warningDialog.value.showWarningDialog = true

			break
		case 6:
			warningDialog.value.title = $t(
				'module.inventory.order.form.confirmCancel'
			)
			warningDialog.value.content = $t(
				'module.inventory.order.form.cancelOrder'
			)
			warningDialog.value.warningStyleTitle = 'color:#ED1F42; font-size:16px'
			warningDialog.value.titleClass = 'font-semibold'
			warningDialog.value.actionType = 'danger'
			warningDialog.value.confirm = async () => {
				res = await cancelUpdateOrder(data.value.id)
				if (res && res.code === 1) {
					$toast('Hủy đơn thành công', true)
					data.value.deliveryStatus = 6
					window.location.reload()
				} else {
					$toast(res.message, false)
				}
				warningDialog.value.showWarningDialog = false
			}
			warningDialog.value.showWarningDialog = true

			break
		default:
			break
	}
}

const warningDialog = ref({
	showWarningDialog: false,
	confirm: () => {},
	title: '',
	warningStyleTitle: '',
	actionType: '',
	titleClass: '',
	content: '',
})

const closeDialog = () => {
	warningDialog.value.title = 'Xác nhận hủy đơn hàng'
	warningDialog.value.content = 'Bạn có chắc muốn hủy đơn hàng này?'
	warningDialog.value.warningStyleTitle = 'color:#ED1F42; font-size:16px'
	warningDialog.value.titleClass = 'font-semibold'
	warningDialog.value.actionType = 'danger'
	warningDialog.value.confirm = async () => {
		router.push('/app/inventory/order-spare-parts')
	}
	warningDialog.value.showWarningDialog = true
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
	invalid.value = false
	if (row.price < 0) {
		errorProductMessage.value = $t(
			'module.inventory.order.error.errorProductMessage'
		)
		isDisabled.value = true
	} else {
		errorProductMessage.value = ''
		isDisabled.value = false
	}
}

const calcPrice = () => {
	discount.value = 0
	tax.value = 0
	for (let i = 0; i < products.value.length; i++) {
		discount.value =
			Number(discount.value) +
			(discountType.value == 1
				? Number(products.value[i].discount)
				: Number(
						(products.value[i].originalPrice * products.value[i].discount) / 100
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

watch(
	() => data.value.distributorId,
	async (val, oldVal) => {
		invalid.value = false

		await getListDistributors({
			pageNumber: 1,
			pageSize: 10000,
			distributorId: data.value.distributorId,
			type: 1,
		}).then((res) => {
			if (res.code == 1) {
				listDistributor.value = res.data.map((a: any) => {
					return {
						value: a.id,
						label: a.code + ' - ' + a.name,
						origin: a,
					}
				})
				listDistributorName.value = res.data.map((a: any) => {
					return {
						value: a.id,
						label: a.name,
					}
				})
			}
		})
		let distributor = listDistributor.value.find((d: any) => {
			return val == d.value
		}) as any

		if (distributor) {
			distributorInfo.value.contactPhone = distributor.origin.contactPhone
			distributorInfo.value.distributorName = distributor.origin.name
		}
	}
)

const onSelectDistributor = (value: any) => {
	getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
		type: 1,
		distributorId: data.value.distributorId,
	}).then((res) => {
		listProduct.value = res.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	})
	if (cloneProps.value.state == EFormState.ADD) {
		if (data.value.distributorId !== 0) {
			products.value = []
			totalPrice.value = 0
			tax.value = 0
			originalPrice.value = 0
			discount.value = 0
		}
	}
}

const handleUpdateDistributor = (value: any) => {
	if (cloneProps.value.state == EFormState.ADD) {
		distributorInfo.value.contactPhone = ''
		distributorInfo.value.distributorName = ''
	}
}

watch(
	() => data.value.distributorId,
	() => {
		getParentProduct({
			pageSize: 10000,
			pageNumber: 1,
			type: 1,
			distributorId: data.value.distributorId,
		}).then((res) => {
			listProduct.value = res.data.map((e: any) => {
				return {
					value: e.id,
					label: e.code + ' - ' + e.name,
					origin: e,
				}
			})
		})
	},
	{ deep: true }
)

const onSubmit = async () => {
	isDisabled.value = true
	const result = await v$.value.$validate()
	if (!result) {
		invalid.value = true
		return
	} else {
		invalid.value = false
	}

	data.value.products = products.value
		.filter((item) => item.productId && item.quantity)
		.map((p) => {
			let result = { ...p } as any
			Object.keys(p).forEach((k) => {
				if (!p[k as keyof typeof p]) {
					result[k as keyof typeof result] =
						baseProduct[k as keyof typeof baseProduct]
				}
			})
			return result
		})
	data.value.products.map((product) => (product.tax = product.tax / 100))
	data.value.products.map(
		(product) =>
			(product.discount =
				discountType.value == 1 ? product.discount : product.discount / 100)
	)
	data.value.discount = discount.value
	data.value.tax = tax.value
	data.value.originalPrice = originalPrice.value
	data.value.totalPrice = totalPrice.value
	const temp = cloneDeep(data.value) as any

	if (
		cloneProps.value.state == EFormState.EDIT &&
		data.value.deliveryStatus == 2
	) {
		temp.addProducts = []
		temp.removeProducts = []

		for (let i of temp.products) {
			if (
				originProduct.value.findIndex((op) => op.productId == i.productId) == -1
			) {
				temp.addProducts.push(i)
			}
		}

		for (let i of originProduct.value) {
			if (
				temp.products.findIndex(
					(op: any) => op.inboundProductId == i.inboundProductId
				) == -1
			) {
				temp.removeProducts.push(i)
			}
		}

		const result = await handlingUpdateOrder(id.value as number, temp)

		if (result.code === 1) {
			$toast($t('module.inventory.order.toast.updateOrderSuccess'), true)
			// window.location.reload()
		} else {
			$toast(result.message, false)
		}
	} else {
		if (products.value.length > 0) {
			products.value.forEach((item) => {
				if (!item.quantity) {
					errorMsgRequiredProductQuality.value = $t(
						'module.inventory.order.error.emptyProductQuality'
					)
				}
				if (!item.productId) {
					errorMsgRequiredProduct.value = $t(
						'module.inventory.order.error.emptyProduct'
					)
				}
			})
		} else {
			errorMsgRequiredProduct.value = $t(
				'module.inventory.order.error.emptyProduct'
			)
		}

		if (errorMsgRequiredProduct.value || errorMsgRequiredProductQuality.value) {
			return 0
		}

		const params = {
			...data.value,
			products: data.value.products.filter(
				(item) => item.productId && item.quantity
			),
		}

		saveLogTracking({
			logEvent: 'Click_CREATE_ORDER_DISTRIBUTOR',
			garageId: localStorage.getItem('garageId'),
			actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
		})

		const res = id.value
			? await updateOrderReceived(id.value?.toString(), params)
			: await createNewOrderDistributor(
					params,
					discountType.value ? discountType.value : 2
			  )
		const successMessage = id.value
			? $t('module.inventory.order.toast.updateOrderSuccess')
			: $t('module.inventory.order.toast.createOrderSuccess')

		if (res.code === 1) {
			$toast(successMessage, true)
			setTimeout(() => {
				router.push('/app/inventory/order-spare-parts')
				resetForm()
			}, 1000)

			// emit("refresh");
		} else {
			errorMessage.value = '* ' + res.message
		}
	}
}
const resetForm = () => {
	errorMessage.value = ''
}

const handleGetDetailLoading = async () => {
	if (route.fullPath == '/app/inventory/order-spare-parts/add') {
		cloneProps.value.state = EFormState.ADD
		setTimeout(() => {
			emitter.emit('layout-pages-change-navName', 'Thêm mới đơn hàng đặt')
		}, 100)
	} else {
		cloneProps.value.state = EFormState.EDIT
		cloneProps.value.orderDistributorId = Number(route.params.id)
		setTimeout(() => {
			emitter.emit('layout-pages-change-navName', 'Chi tiết đơn hàng đặt')
		}, 100)
	}
	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
		}
	})

	if (cloneProps.value.orderDistributorId) {
		id.value = cloneProps.value.orderDistributorId
		const result = await findOrderDistributor(
			cloneProps.value.orderDistributorId.toString()
		)
		data.value = result ? result.data : null
		totalPrice.value = Number(data.value.totalPrice)
		discountType.value = data.value.discountType ? data.value.discountType : 2
		originProduct.value = data.value.products
		// products.value = cloneDeep(data.value.products)
		products.value = cloneDeep(
			data.value.products?.map((el, index) => {
				return {
					...el,
					instanceKey: index,
				}
			})
		)
		products.value.map((product) => {
			product.tax = product.tax * 100
			product.discount = product.discount * 100
		})
		products.value.forEach((product) => {
			originalPrice.value += product.originalPrice
			discount.value +=
				discountType.value == 1
					? product.discount
					: (product.originalPrice * product.discount) / 100
			tax.value +=
				discountType.value == 1
					? (product.originalPrice - product.discount) * (product.tax / 100)
					: product.originalPrice *
					  (1 - product.discount / 100) *
					  (product.tax / 100)
		})
		isChangeProduct.value = true
	}

	getListDistributors({
		pageNumber: 1,
		pageSize: 10000,
	}).then((res) => {
		if (res.code == 1) {
			listDistributor.value = res.data.map((a: any) => {
				return {
					value: a.id,
					label: a.code + ' - ' + a.name,
					origin: a,
				}
			})
			listDistributorName.value = res.data.map((a: any) => {
				return {
					value: a.id,
					label: a.name,
				}
			})
		}
	})

	if (data.value.distributorId) {
		getParentProduct({
			pageSize: 10000,
			pageNumber: 1,
			type: 1,
			distributorId: data.value.distributorId,
		}).then((res) => {
			listProduct.value = res.data.map((e: any) => {
				return {
					value: e.id,
					label: e.code + ' - ' + e.name,
					origin: e,
				}
			})
		})
	} else {
		getParentProduct({
			pageSize: 10000,
			pageNumber: 1,
			type: 1,
		}).then((res) => {
			listProduct.value = res.data.map((e: any) => {
				return {
					value: e.id,
					label: e.code + ' - ' + e.name,
					origin: e,
				}
			})
		})
	}
}

await handleGetDetailLoading()

const handleBack = () => {
	router.back()
}

const handleCreateNewProduct = (value: any, row: any) => {
	const product = {
		productName: value,
		intanceKey: row.instanceKey,
		distributorId: data.value.distributorId,
	}
	emitter.emit('on-add-new-product', product)
}

emitter.on('on-change-data-product', async (product: any) => {
	// cloneProps.value.state = EFormState.ADD_NEW
	data.value.distributorId = product.distributorId

	let list: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
		type: 1,
		distributorId: product.distributorId ? product.distributorId : null,
	})

	if (result.code == 1) {
		list = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}

	listProduct.value = list

	products.value = products.value.map((item) => {
		if (item.instanceKey == product.intanceKey) {
			return {
				...item,
				productId: product.productId,
			}
		}
		return {
			...item,
		}
	})

	const updateRow = products.value.find(
		(item) => item.instanceKey == product.intanceKey
	)

	if (updateRow) {
		await onSelectProduct(updateRow, updateRow.productId)
	}
})

emitter.on('on-remove-product', async (product: any) => {
	let list: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
		type: 1,
		distributorId: data.value.distributorId,
	})

	if (result.code == 1) {
		list = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}

	listProduct.value = list

	products.value = products.value.map((item) => {
		if (item.instanceKey == product.intanceKey) {
			return {
				...item,
				productId: 0,
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
</script>
