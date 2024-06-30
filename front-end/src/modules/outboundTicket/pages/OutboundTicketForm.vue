<template>
	<ACCDModal
		v-model="lazyValue"
		:title="computedTitle"
		class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
		@close="closeDialogConfirm"
	>
		<div>
			<ACCDForm :show-footer="false" class="w-full">
				<div class="flex flex-col lg:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.outboundTicket.form.type')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.outboundTicket.form.type')"
							v-model="outboundTicket.type"
							:options="typeOptions"
							:disabled="true"
						/>
					</ACCDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.outboundTicket.form.createdAt')"
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDDatePicker
							size="md"
							v-model="outboundTicket.createdAt"
							:disabled="true"
							:formatter="'YYYY-MM-DD'"
						/>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.outboundTicket.form.code')"
						class="w-full lg:w-1/3 font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.outboundTicket.form.code')"
							v-model="outboundTicket.ticketId"
							:options="listTicket"
							:disabled="true"
						/>
					</ACCDFormItem>

					<div class="w-full lg:w-1/3">
						<p class="mb-1 font-semibold">
							{{ $t('module.outboundTicket.form.status') }}
						</p>
						<ACCDSelect
							size="md"
							v-model="outboundTicket.status"
							:options="statusOptions"
							labelName="label"
							valueName="value"
							:placeholder="$t('module.outboundTicket.form.status')"
							:disabled="true"
							class="font-semibold"
						/>
					</div>
				</div>
			</ACCDForm>
		</div>

		<div>
			<div class="flex flex-col lg:flex-row justify-between">
				<h1 class="text-gray-900 font-semibold text-lg pb-1 mb-2">
					{{ $t('module.outboundTicket.form.table.title') }}
				</h1>
			</div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="outboundTicket.outboundProducts"
			>
				<template #cell-stt="{ row, rowIndex, col, field }">
					{{ rowIndex + 1 }}
				</template>
				<template #cell-code="{ row, col, field }">
					{{ productOptions.find((a) => a.value == row.productId)?.rawValue }}
				</template>
				<template #cell-productId="{ row, col, field }">
					{{ productOptions.find((a) => a.value == row.productId)?.label }}
				</template>
				<template #cell-exportQuantity="{ row, col, field }">
					<div
						v-if="outboundTicket.status === 1 || outboundTicket.status === 0"
					>
						{{ row.exportQuantity }}
					</div>
					<ACCDInputNumber
						v-else
						class="shadow-none"
						size="md"
						v-model="row.exportQuantity"
						@keypress="validateNumber"
						:readonly="outboundTicket.status === 1"
					/>
				</template>
				<template #cell-note="{ row, col, field }">
					<ACCDInputText
						class="shadow-none"
						size="md"
						v-model="row.note"
						:readonly="outboundTicket.status === 1"
						:trim="true"
					/>
				</template>
			</ACCDTable>
			<br />
			<span class="italic text-red-500" v-if="invalid">{{
				$t('module.outboundTicket.error.missingRequiredField')
			}}</span>
			<span class="italic text-red-500" v-if="responseErrorMessages">{{
				responseErrorMessages
			}}</span>
			<div v-if="isEmptyProductQuanity">
				<span class="italic text-red-500">
					{{ $t('module.sellSparePart.error.empty') }}
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

				<ACCDButton
					type="secondary"
					class="w-full lg:w-1/5"
					@click="handleChangePrint"
				>
					{{ $t('module.outboundTicket.action.printTicket') }}
				</ACCDButton>

				<ACCDButton
					v-if="props.state !== EFormState.VIEW && outboundTicket.status === 2"
					type="primary"
					variant="fill"
					class="bg-info-secondary"
					@click="confirmAction"
				>
					<span class="text-white font-medium">{{
						$t('module.outboundTicket.action.confirm')
					}}</span>
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
	<!-- <InboundSheet v-if="isShowOutbound" /> -->
</template>

<script setup lang="ts">
import { EFormState } from '@/enums'
import { computed, onMounted, ref, watch } from 'vue'
import { emitter } from '@/utils/mitt'
import { useI18n } from '@/composables/useI18n'

import {
	confirmActionOutbound,
	findAllRepairOrder,
	getDetailOutboundTicket,
} from '@/modules/outboundTicket/api'
import { ISelectOption } from '@/types'
import { getParentProduct } from '@/modules/accessary/api'
import {
	validateNumber,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { useToast } from '@/composables/useToast'
import { dayjs } from 'element-plus'
import router from '@/router'
import { findAllSellSparePartByGarageId } from '@/modules/sellSparePart/api'
import { getOrderDistributorControllerList } from '@/modules/inventory/api'

const { $t } = useI18n()
const PAGE_SIZE = 10
const MAX_INT = import.meta.env.VITE_MAX_INTEGER

const instanceKey = new Date().getTime()
const { $toast } = useToast()
const outboundTicket = ref({
	id: 0,
	code: 'string',
	type: 0,
	distributorId: 0,
	ticketId: 0,
	products: '',
	note: '',
	status: 2,
	createdAt: '',
	outboundProducts: [] as OutboundProduct[],
})
// const isShowOutbound = ref(false);

const id = ref<number | null | string>(null)
const productOptions = ref<ISelectOption[]>([])
const invalid = ref(false)
const responseErrorMessages = ref('')
const listTicket = ref([])

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	outboundTicketId?: number | string
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
		label: $t('module.outboundTicket.type.1'),
	},
	{
		value: 2,
		label: $t('module.outboundTicket.type.2'),
	},
	{
		value: 3,
		label: $t('module.outboundTicket.type.3'),
	},
])

const statusOptions = ref([
	{
		value: 1,
		label: $t('module.outboundTicket.status.1'),
	},
	{
		value: 2,
		label: $t('module.outboundTicket.status.2'),
	},
	{
		value: 0,
		label: $t('module.outboundTicket.status.0'),
	},
])

const columnData = [
	{
		key: 'stt',
		headerName: $t('module.outboundTicket.form.table.stt'),
	},
	{
		key: 'code',
		headerName: $t('module.outboundTicket.form.table.code'),
	},
	{
		key: 'productId',
		headerName: $t('module.outboundTicket.form.table.productId'),
	},
	{
		key: 'unit',
		headerName: $t('module.outboundTicket.form.table.unit'),
	},
	{
		key: 'requestQuantity',
		headerName: $t('module.outboundTicket.form.table.requestQuantity'),
	},
	{
		key: 'exportQuantity',
		headerName: $t('module.outboundTicket.form.table.exportQuantity'),
		width: '80px',
	},
	{
		key: 'note',
		headerName: $t('module.outboundTicket.form.table.note'),
	},
]

const computedTitle = computed(() => {
	switch (props.state) {
		case EFormState.ADD:
			return $t('module.outboundTicket.form.addTitle')
		case EFormState.EDIT:
			return $t('module.outboundTicket.form.editTitle')
		default:
			return ''
	}
})
const isEmptyProductQuanity = ref<boolean>(false)
watch(
	() => outboundTicket.value.outboundProducts,
	() => {
		isEmptyProductQuanity.value = false
		localStorage.setItem(
			'outboudTicketPrint',
			JSON.stringify(outboundTicket.value)
		)
		localStorage.setItem('typePrint', JSON.stringify(2))
	},
	{ deep: true }
)

const closeDialogConfirm = () => {
	emitter.emit('layout-page-close-confirmClose', instanceKey)
}

const closeDialog = () => {
	emitter.emit('layout-pages-open-confirmClose', instanceKey)
	invalid.value = false
}

const resetForm = () => {
	responseErrorMessages.value = ''
}

const confirmAction = async () => {
	const params = {
		...outboundTicket.value,
	} as unknown as OutboundTicket

	if (
		outboundTicket.value.outboundProducts.every((item) => !item.exportQuantity)
	) {
		isEmptyProductQuanity.value = true
	}
	if (isEmptyProductQuanity.value) {
		return 0
	}

	const response = await confirmActionOutbound(id.value as number, params)

	const successMessage = $t('module.outboundTicket.toast.editSuccess')
	if (response.code === 1) {
		$toast(successMessage, true)
		lazyValue.value = false
		emit('refresh')
		resetForm()
	} else {
		responseErrorMessages.value = '* ' + response.message
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
			outboundTicket.value.outboundProducts.forEach((el1) => {
				if (el.value == el1.productId) {
					temp.push(el)
				}
			})
		})

		if (temp) {
			localStorage.setItem('productServiceId', JSON.stringify(temp))
		}
	},
	{ deep: true }
)

onMounted(async () => {
	if (props.outboundTicketId) {
		id.value = props.outboundTicketId
		const result = await getDetailOutboundTicket(Number(props.outboundTicketId))
		outboundTicket.value = {
			...result.data,
			outboundProducts: result.data.outboundProducts.map((el) => {
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

	if (outboundTicket.value.type == 2) {
		await findAllRepairOrder({
			pageSize: 100000,
			pageNumber: 1,
		}).then((res) => {
			listTicket.value = res.data
				.filter((item) => item.id == outboundTicket.value.ticketId)
				.map((a: any) => {
					return {
						label: a.code,
						value: a.id,
					}
				})
		})
	} else if (outboundTicket.value.type == 3) {
		await findAllSellSparePartByGarageId({
			pageSize: 100000,
			pageNumber: 1,
		}).then((res) => {
			listTicket.value = res.data
				.filter((item) => item.id == outboundTicket.value.ticketId)
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
				.filter((item) => item.id == outboundTicket.value.ticketId)
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
</script>
