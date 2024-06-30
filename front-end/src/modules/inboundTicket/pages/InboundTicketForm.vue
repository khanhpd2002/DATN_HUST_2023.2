<template>
	<ACCDModal
		v-model="lazyValue"
		:title="computedTitle"
		class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
		@close="closeDialogConfirm"
	>
		<div>
			<ACCDForm :show-footer="false" class="w-full">
				<div class="flex gap-4">
					<ACCDFormItem
						:label="$t('module.inboundTicket.form.type')"
						class="w-full lg:w-1/2 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.inboundTicket.form.type')"
							v-model="inboundTicket.type"
							:options="typeOptions"
							:disabled="
								inboundTicket.status === 1 || id || state == EFormState.ADD
							"
							class="font-medium"
						/>
					</ACCDFormItem>
					<ACCDFormItem
						v-if="inboundTicket.type === 1 || state == EFormState.ADD"
						:label="$t('module.inboundTicket.form.distributor')"
						class="w-full lg:w-1/2 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.inboundTicket.form.distributor')"
							v-model="inboundTicket.distributorId"
							:options="distributorOptions"
							:disabled="inboundTicket.status === 1 || id"
							class="font-medium"
						/>
					</ACCDFormItem>
				</div>

				<div
					class="flex flex-col lg:flex-row md:flex-row gap-4"
					v-if="inboundTicket.type"
				>
					<ACCDFormItem
						:label="$t('module.inboundTicket.form.createdAt')"
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDDatePicker
							size="md"
							v-model="inboundTicket.createdAt"
							:disabled="true"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</ACCDFormItem>

					<ACCDFormItem
						:label="$t('module.inboundTicket.form.code')"
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.inboundTicket.form.code')"
							v-model="inboundTicket.ticketId"
							:options="listTicket"
							:disabled="true"
							class="font-medium"
						/>
					</ACCDFormItem>

					<div class="w-full lg:w-1/3">
						<p class="mb-1 font-semibold">
							{{ $t('module.inboundTicket.form.status') }}
						</p>

						<ACCDSelect
							size="md"
							v-model="inboundTicket.status"
							:options="statusOptions"
							labelName="label"
							valueName="value"
							:placeholder="$t('module.inboundTicket.form.status')"
							:disabled="true"
							class="font-medium"
						/>
					</div>
				</div>
			</ACCDForm>
		</div>

		<div>
			<div class="flex flex-col lg:flex-row justify-between mb-4">
				<h1 class="text-gray-900 font-semibold text-lg pb-1">
					{{ $t('module.inboundTicket.form.table.title') }}
				</h1>
			</div>
			<ACCDTable
				ref="table"
				:columns="state == EFormState.ADD ? columnDataAdd : columnData"
				:rowData="inboundTicket.inboundProducts"
			>
				<template #cell-stt="{ row, rowIndex, col, field }">
					{{ rowIndex + 1 }}
				</template>

				<template #cell-code="{ row, rowIndex, col, field }">
					<span>
						{{
							productOptions.find((item) => item.value == row.productId)
								?.rawValue
						}}
					</span>
				</template>
				<template #cell-productId="{ row, col, field }">
					<span v-if="state == EFormState.EDIT">
						{{
							productOptions.find((item) => item.value == row.productId)?.label
						}}</span
					>
					<ACCDSelect
						v-else
						@change="onSelectProduct(row)"
						v-model="row.productId"
						:options="productOptions"
						:forCreate="true"
						:autoCreate="true"
						placeholder=""
					/>
				</template>
				<template #cell-unit="{ row }">
					<WrapFlexContainer
						v-if="typeof row.productId == 'number' || row.productId == ''"
					>
						{{ row.unit }}</WrapFlexContainer
					>

					<ACCDInputText
						v-else
						v-model="row.unit"
						@update:modelValue="() => onSelectProduct(row)"
						class="w-full"
					>
					</ACCDInputText>
				</template>
				<template #cell-requestQuantity="{ row, col, field }">
					<span v-if="state == EFormState.EDIT">
						{{
							row.requestQuantity && formatPriceVN(row.requestQuantity)
						}}</span
					>
					<ACCDInputNumber
						v-else
						class="shadow-none"
						size="md"
						v-model="row.requestQuantity"
						@keypress="validateNumber"
					/>
				</template>
				<template #cell-exportQuantity="{ row, col, field }">
					<div v-if="inboundTicket.status === 1 || inboundTicket.status === 0">
						{{ row.exportQuantity }}
					</div>
					<ACCDInputNumber
						v-else
						class="shadow-none"
						size="md"
						v-model="row.exportQuantity"
						@keypress="validateNumber"
						:readonly="inboundTicket.status === 1 || state == EFormState.ADD"
					/>
				</template>
				<template #cell-note="{ row, col, field }">
					<ACCDInputText
						class="shadow-none"
						size="md"
						v-model="row.note"
						:readonly="inboundTicket.status === 1"
						:trim="true"
					/>
				</template>
			</ACCDTable>
			<span
				v-if="state != EFormState.EDIT"
				class="cursor-pointer"
				style="color: #25b3e8"
				@click="addSparePart"
				>{{ $t('module.inboundTicket.form.table.addProductRowData') }}</span
			>
			<br />
			<span class="italic text-red-500" v-if="invalid">{{
				$t('module.inboundTicket.error.missingRequiredField')
			}}</span>
			<span class="italic text-red-500" v-if="responseErrorMessages">{{
				responseErrorMessages
			}}</span>

			<div v-if="isEmptyProductQuanity">
				<span class="italic text-red-500">
					{{ $t('module.sellSparePart.error.empty') }}
				</span>
			</div>
			<div v-if="errorMessages">
				<span class="italic text-red-500">
					{{ errorMessages }}
				</span>
			</div>
		</div>

		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton
					variant="fill"
					type="secondary"
					class="bg-info-secondary"
					@click="closeDialog"
				>
					<span class="text-info-base font-medium">{{
						$t('module.customerType.action.back')
					}}</span>
				</ACCDButton>
				<div
					class="flex flex-row-reverse"
					v-if="inboundTicket.status === 1 || id"
				>
					<ACCDButton
						type="secondary"
						class="w-full lg:w-1/5"
						size="md"
						@click="handleChangePrint"
					>
						{{ $t('module.inboundTicket.action.printTicket') }}
					</ACCDButton>
				</div>
				<ACCDButton
					v-if="props.state !== EFormState.VIEW && inboundTicket.status === 2"
					type="primary"
					variant="fill"
					class="bg-info-secondary"
					@click="confirmAction"
				>
					<span class="text-white font-medium">{{
						$t('module.inboundTicket.action.confirm')
					}}</span>
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>

	<!-- <InboundSheet v-if="isShowInbound" /> -->
	<AccessaryForm></AccessaryForm>
</template>

<script setup lang="ts">
import { EFormState } from '@/enums'
import { computed, onMounted, ref, watch } from 'vue'
import { emitter } from '@/utils/mitt'
import { useI18n } from '@/composables/useI18n'
// import InboundSheet from "@/modules/sharedModules/pages/formHTML/InboundSheet.vue";
import {
	confirmActionInbound,
	createInboundTicket,
	findAllRepairOrder,
	getDetailInboundTicket,
} from '@/modules/inboundTicket/api'
import { ISelectOption } from '@/types'
import { getParentProduct } from '@/modules/accessary/api'
import {
	formatPriceVN,
	validateNumber,
} from '@/modules/sharedModules/component/constants'
import { useToast } from '@/composables/useToast'
import { getDetailParentSparePart } from '@/modules/inventory/api'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { getListDistributors } from '@/modules/distributor/api'
import { ElEmpty, dayjs } from 'element-plus'
import router from '@/router'
import { findAllSellSparePartByGarageId } from '@/modules/sellSparePart/api'
import { getOrderDistributorControllerList } from '@/modules/inventory/api'
import AccessaryForm from '@/modules/accessary/pages/AccessaryForm.vue'

const { $t } = useI18n()

const MAX_INT = import.meta.env.VITE_MAX_INTEGER

const instanceKey = new Date().getTime()
const { $toast } = useToast()
const isShowInbound = ref(false)
const inboundTicket = ref({
	id: 0,
	code: 'string',
	type: 1,
	distributorId: 0,
	ticketId: 0,
	products: '',
	note: '',
	status: 2,
	createdAt: dayjs().format('DD/MM/YYYY'),
	inboundProducts: [] as InboundProduct[],
})

const id = ref<number | null | string>(null)
const productOptions = ref<ISelectOption[]>([])
const distributorOptions = ref<ISelectOption[]>([])
const invalid = ref(false)
const responseErrorMessages = ref('')
const errorMessages = ref('')

const listTicket = ref([])

const validations = computed(() => {
	return {
		inboundTicket: {
			type: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
		},
	}
})
const v$ = useVuelidate(validations, { inboundTicket })

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	inboundTicketId?: number | string
}

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	state: EFormState.ADD,
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

const typeOptions = ref([
	{
		value: 1,
		label: $t('module.inboundTicket.type.1'),
	},
	{
		value: 2,
		label: $t('module.inboundTicket.type.2'),
	},
	{
		value: 3,
		label: $t('module.inboundTicket.type.3'),
	},
])

const statusOptions = ref([
	{
		value: 0,
		label: $t('module.inboundTicket.status.0'),
	},
	{
		value: 1,
		label: $t('module.inboundTicket.status.1'),
	},
	{
		value: 2,
		label: $t('module.inboundTicket.status.2'),
	},
])

const columnData = [
	{
		key: 'stt',
		headerName: $t('module.inboundTicket.form.table.stt'),
	},

	{
		key: 'code',
		headerName: $t('module.inboundTicket.form.table.code'),
	},
	{
		key: 'productId',
		headerName: $t('module.inboundTicket.form.table.productId'),
	},
	{
		key: 'unit',
		headerName: $t('module.inboundTicket.form.table.unit'),
	},
	{
		key: 'requestQuantity',
		headerName: $t('module.inboundTicket.form.table.requestQuantity'),
	},
	{
		key: 'exportQuantity',
		headerName: $t('module.inboundTicket.form.table.exportQuantity'),
	},
	{
		key: 'note',
		headerName: $t('module.inboundTicket.form.table.note'),
	},
]

const columnDataAdd = [
	{
		key: 'stt',
		headerName: $t('module.inboundTicket.form.table.stt'),
	},

	{
		key: 'code',
		headerName: $t('module.inboundTicket.form.table.code'),
	},
	{
		key: 'productId',
		headerName: $t('module.inboundTicket.form.table.productId'),
	},
	{
		key: 'unit',
		headerName: $t('module.inboundTicket.form.table.unit'),
	},
	{
		key: 'requestQuantity',
		headerName: $t('module.inboundTicket.form.table.requestQuantity'),
	},

	{
		key: 'note',
		headerName: $t('module.inboundTicket.form.table.note'),
	},
]

const computedTitle = computed(() => {
	switch (props.state) {
		case EFormState.ADD:
			return $t('module.inboundTicket.form.addTitle')
		case EFormState.EDIT:
			return $t('module.inboundTicket.form.editTitle')
		default:
			return ''
	}
})

const isEmptyProductQuanity = ref<boolean>(false)

watch(
	() => inboundTicket.value.inboundProducts,
	() => {
		isEmptyProductQuanity.value = false
		errorMessages.value = ''
		localStorage.setItem(
			'inboudTicketPrint',
			JSON.stringify(inboundTicket.value)
		)
		localStorage.setItem('typePrint', JSON.stringify(1))
	},
	{ deep: true }
)

const closeDialog = () => {
	emitter.emit('layout-pages-open-confirmClose', instanceKey)
	invalid.value = false
}

const closeDialogConfirm = () => {
	emitter.emit('layout-page-close-confirmClose', instanceKey)
}

const resetForm = () => {
	responseErrorMessages.value = ''
}

const confirmAction = async () => {
	const result = await v$.value.$validate()
	if (!result) {
		invalid.value = true
		return
	} else {
		invalid.value = false
	}

	if (inboundTicket.value.inboundProducts.length > 0) {
		if (
			inboundTicket.value.inboundProducts.every(
				(item) => !item.exportQuantity
			) &&
			props.state == EFormState.EDIT
		) {
			isEmptyProductQuanity.value = true
		}
		if (
			inboundTicket.value.inboundProducts.some((el) => !el.productId) &&
			props.state == EFormState.ADD
		) {
			errorMessages.value = $t('module.inboundTicket.error.requiredProduct')
		}
	} else {
		errorMessages.value = $t('module.inboundTicket.error.notEmpty')
	}

	if (isEmptyProductQuanity.value || errorMessages.value) {
		return 0
	}

	inboundTicket.value.inboundProducts.forEach((el) => {
		if (typeof el.productId == 'string') {
			el.productName = el.productId
			el.productId = null
		}
	})
	const params = {
		...inboundTicket.value,
	} as unknown as InboundTicket

	console.log('params', params)
	const response = id.value
		? await confirmActionInbound(id.value as number, params)
		: await createInboundTicket(params)
	const successMessage = $t('module.inboundTicket.toast.editSuccess')
	if (response.code === 1) {
		$toast(successMessage, true)
		lazyValue.value = false
		emit('refresh')
		resetForm()
	} else {
		responseErrorMessages.value = '* ' + response.message
	}
}

const addSparePart = () => {
	let rowData = {
		id: 0,
		productId: 0,
		inboundTicketId: 0,
		status: 0,
		unit: '',
		requestQuantity: 0,
		exportQuantity: 0,
		instanceKey: 0,
	}

	rowData.instanceKey =
		inboundTicket.value.inboundProducts.length > 0
			? inboundTicket.value.inboundProducts[
					inboundTicket.value.inboundProducts.length - 1
			  ].instanceKey + 1
			: 0

	inboundTicket.value.inboundProducts.push(rowData)
}

const onSelectProduct = async (row: InboundProduct) => {
	if (typeof row.productId == 'number') {
		await getDetailParentSparePart(String(row.productId)).then((res) => {
			row.unit = res.data.unit
		})
	}
}

const handleChangePrint = () => {
	let route = router.resolve({ path: '/app/inbound-outbound-sheet' })
	window.open(route.href, '_blank')
}

watch(
	() => {
		productOptions.value
	},
	() => {
		let temp: { value: ''; label: '' }[] = []

		productOptions.value.forEach((el) => {
			inboundTicket.value.inboundProducts.forEach((el1) => {
				if (el.value == el1.productId) {
					temp.push(el)
				}
			})
		})
		if (temp.length > 0) {
			localStorage.setItem('productServiceId', JSON.stringify(temp))
		}
	},
	{ deep: true }
)

watch(
	() => inboundTicket.value.distributorId,
	async () => {
		if (props.state == EFormState.ADD) {
			inboundTicket.value.inboundProducts = []
		}
		let productList = await getParentProduct({
			pageSize: MAX_INT,
			pageNumber: 1,
			type: inboundTicket.value.type,
			distributorId: inboundTicket.value.distributorId,
		})

		productOptions.value = productList.data.map((a: any) => {
			return {
				label: a.name,
				value: a.id,
				rawValue: a.code,
			}
		})
	},
	{ deep: true }
)

onMounted(async () => {
	if (props.inboundTicketId) {
		id.value = props.inboundTicketId
		const result = await getDetailInboundTicket(Number(props.inboundTicketId))

		inboundTicket.value = {
			...result.data,
			inboundProducts: result.data.inboundProducts.map((el) => {
				return {
					...el,
					exportQuantity:
						result.data.status != 2 ? el.exportQuantity : el.requestQuantity,
				}
			}),
			createdAt: dayjs(result.data.createdAt).format('DD/MM/YY'),
		}
	}

	let productList = await getParentProduct({
		pageSize: MAX_INT,
		pageNumber: 1,
	})
	productOptions.value = productList.data.map((a: any) => {
		return {
			label: a.name,
			value: a.id,
			rawValue: a.code,
		}
	})
	let distributorList = await getListDistributors({
		pageSize: MAX_INT,
		pageNumber: 1,
	})
	distributorOptions.value = distributorList.data.map((a: any) => {
		return {
			label: a.name,
			value: a.id,
			rawValue: a.code,
		}
	})

	if (inboundTicket.value.type == 2) {
		await findAllRepairOrder({
			pageSize: 100000,
			pageNumber: 1,
		}).then((res) => {
			listTicket.value = res.data
				.filter((item) => item.id == inboundTicket.value.ticketId)
				.map((a: any) => {
					return {
						label: a.code,
						value: a.id,
					}
				})
		})
	} else if (inboundTicket.value.type == 3) {
		await findAllSellSparePartByGarageId({
			pageSize: 100000,
			pageNumber: 1,
		}).then((res) => {
			listTicket.value = res.data
				.filter((item) => item.id == inboundTicket.value.ticketId)
				.map((a: any) => {
					return {
						label: a.sellCode,
						value: a.id,
					}
				})
		})
	} else {
		await getOrderDistributorControllerList({
			pageSize: 100000,
			pageNumber: 1,
		}).then((res) => {
			listTicket.value = res.data
				.filter((item) => item.id == inboundTicket.value.ticketId)
				.map((a: any) => {
					return {
						label: a.orderCode,
						value: a.id,
					}
				})
		})
	}

	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
		}
	})
})

const handleCreateNewProduct = (value: any, row: InboundProduct) => {
	const data = { productName: value, intanceKey: row.instanceKey }
	emitter.emit('on-add-new-product', data)
}

emitter.on('on-change-data-product', async (data: any) => {
	let productList = await getParentProduct({
		pageSize: MAX_INT,
		pageNumber: 1,
		type: inboundTicket.value.type ? inboundTicket.value.type : null,
		distributorId: inboundTicket.value.distributorId
			? inboundTicket.value.distributorId
			: null,
	})

	productOptions.value = productList.data.map((a: any) => {
		return {
			label: a.name,
			value: a.id,
			rawValue: a.code,
		}
	})

	inboundTicket.value.inboundProducts = inboundTicket.value.inboundProducts.map(
		(item) => {
			return {
				...item,
				productId:
					item.instanceKey == data.intanceKey ? data.productId : item.productId,
			}
		}
	)

	const updateRow = inboundTicket.value.inboundProducts.find(
		(item) => item.instanceKey == data.intanceKey
	)

	if (updateRow) {
		await onSelectProduct(updateRow)
	}
})
</script>
