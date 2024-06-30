<template>
	<ACCDModal
		v-model="lazyValue"
		:title="computedTitle"
		class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
	>
		<div class="flex flex-col gap-4 py-4">
			<div class="w-full lg:w-1/6 flex flex-col gap-1">
				<p>{{ $t('module.inventory.inventoryHistory.form.date') }}</p>
				<ACCDDatePicker
					size="md"
					v-model="countDate"
					:disabled="props.action === EFormState.VIEW"
					formatter="DD/MM/YY"
				/>
			</div>
			<div>
				<div class="flex flex-col lg:w-1/4 gap-4">
					<div
						v-for="(e, i) in employeeOnInventory"
						class="flex flex-col gap-1"
					>
						<p class="mb-1 w-full">
							{{
								$t('module.inventory.inventoryHistory.form.employee') +
								' ' +
								(i !== 0 ? i + 1 : '')
							}}
						</p>
						<ACCDSelect
							v-model="e.value"
							:disabled="props.action === EFormState.VIEW"
							:options="listGarageEmployees"
							:placeholder="
								$t('module.inventory.inventoryHistory.form.employee')
							"
						>
						</ACCDSelect>
					</div>
				</div>
				<span
					@click="addEmployee"
					class="cursor-pointer"
					style="color: #25b3e8"
					v-if="$props.action !== EFormState.VIEW"
				>
					{{ $t('module.inventory.inventoryHistory.form.addEmployee') }}
				</span>
			</div>
			<div class="flex justify-between items-center">
				<span>{{
					$t('module.inventory.inventoryHistory.form.productList')
				}}</span>
				<ACCDButton v-if="props.action !== EFormState.VIEW">
					<ACCDIcon
						class="text-gray-700 pr-3"
						name="fa-solid fa-upload"
					></ACCDIcon>
					<span class="text-info-base font-medium">
						{{ $t('module.inventory.inventoryHistory.form.uploadFromExcel') }}
					</span>
				</ACCDButton>
				<ACCDButton
					v-else
					type="secondary"
					@click="onShowBill = true"
					class="px-16"
				>
					{{ $t('module.inventory.inventoryHistory.form.printBill') }}
				</ACCDButton>
			</div>
			<div>
				<ACCDTable
					:columns="inventoryProductColumn"
					:rowData="inventoryProductRow"
					:show-summary="true"
				>
					<template #cell-productId="{ row }">
						<td class="cd-table__td">
							<ACCDSelect
								:readonly="props.action === EFormState.VIEW"
								v-model="row.productId"
								@change="() => onSelectProduct(row)"
								:options="listGarageProducts"
							>
							</ACCDSelect>
						</td>
					</template>
					<template #cell-unitPrice="{ row }">
						<td class="cd-table__td">
							{{
								row.unitPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							}}
						</td>
					</template>
					<template #cell-priceBySystemInventory="{ row }">
						<td class="cd-table__td">
							{{
								(row.unitPrice * row.systemInventory)
									.toString()
									.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							}}
						</td>
					</template>
					<template #cell-realityInventory="{ row }">
						<td class="w-20 cd-table__td">
							<ACCDInputText
								:readonly="props.action === EFormState.VIEW"
								v-model="row.realityInventory"
								@keypress="validatePrice"
							>
							</ACCDInputText>
						</td>
					</template>
					<template #cell-priceByRealityInventory="{ row }">
						<td class="cd-table__td">
							{{
								(row.unitPrice * row.realityInventory)
									.toString()
									.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							}}
						</td>
					</template>
					<template #cell-differrenceInventory="{ row }">
						<td class="w-20 cd-table__td">
							{{ row.realityInventory - row.systemInventory }}
						</td>
					</template>
					<template #cell-priceByDifferenceInventory="{ row }">
						<td class="cd-table__td">
							{{
								(
									Math.abs(row.realityInventory - row.systemInventory) *
									row.unitPrice
								)
									.toString()
									.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
							}}
						</td>
					</template>
					<template #sum-unitPrice="{ value }">
						<td class="cd-table__td--sum font-bold">
							{{ value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
						</td>
					</template>
					<template #sum-priceBySystemInventory="{ value }">
						<td class="cd-table__td--sum font-bold">
							{{ value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
						</td>
					</template>
					<template #sum-priceByRealityInventory="{ value }">
						<td class="cd-table__td--sum font-bold">
							{{ value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
						</td>
					</template>
					<template #sum-priceByDifferenceInventory="{ value }">
						<td class="cd-table__td--sum font-bold">
							{{ value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
						</td>
					</template>
				</ACCDTable>
				<span
					v-if="props.action !== EFormState.VIEW"
					class="cursor-pointer"
					style="color: #25b3e8"
					@click="addProductRowData"
					>{{ $t('module.inventory.inventoryHistory.form.addProduct') }}</span
				>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton
					@click="onClickBack"
					type="secondary"
					size="md"
					class="w-1/6"
				>
					{{ $t('module.inventory.inventoryHistory.form.back') }}
				</ACCDButton>
				<ACCDButton
					v-if="action != EFormState.VIEW"
					@click="onSubmit"
					type="primary"
					size="md"
					class="w-1/6"
				>
					{{ $t('module.inventory.inventoryHistory.form.save') }}
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
	<InventoryMinutes
		v-if="onShowBill"
		@close="onShowBill = false"
		:inventoryDate="countDate"
		:listEmployee="counterName"
		:productInfo="computedProducts"
	>
	</InventoryMinutes>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getGarageEmployees } from '@/modules/employee/api'
import { getListProducts } from '@/modules/product/api'
import { ISelectOption } from '@/types'
import { $toast, instanceI18n } from '@/main.js'
import { EFormState } from '@/enums'
import dayjs from 'dayjs'
import InventoryMinutes from '@/modules/sharedModules/pages/formHTML/inventoryMinutes.vue'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import {
	createInventoryHistory,
	getDetailInventoryHistory,
	updateInventoryHistory,
} from '@/modules/inventory/api'
import { emitter } from '@/utils/mitt'
import { validatePrice } from '@/modules/sharedModules/component/constants'
import { useI18n } from '@/composables/useI18n'

dayjs.extend(customParseFormat)
const { $t } = useI18n()

const onShowBill = ref(false)

type InventoryProductRow = {
	productId: number
	unitPrice: number
	systemInventory: number
	realityInventory: number
	unit: string
	priceBySystemInventory: number
	priceByRealityInventory: number
	differrenceInventory: number
	priceByDifferenceInventory: number
}
const baseInventoryProductRow = {
	productId: 0,
	unitPrice: 0,
	systemInventory: 0,
	realityInventory: 0,
	unit: '',
	priceBySystemInventory: 0,
	priceByRealityInventory: 0,
	differrenceInventory: 0,
	priceByDifferenceInventory: 0,
} as InventoryProductRow

const listGarageProducts = ref([] as (ISelectOption & { origin: any })[])
const listGarageEmployees = ref([] as ISelectOption[])
const countDate = ref(dayjs().format('DD/MM/YYYY'))
const inventoryProductRow = ref([] as InventoryProductRow[])

const inventoryProductColumn = ref([
	{
		key: 'productId',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.productId'
		),
		minWidth: '300px',
	},
	{
		key: 'unit',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.unit'
		),
	},
	{
		key: 'unitPrice',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.unitPrice'
		),
		minWidth: '100px',
	},
	{
		align: 'center',
		key: 'systemInventory&systemInventory',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.bySystem'
		),
		children: [
			{
				key: 'systemInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.systemInventory'
				),
				minWidth: '100px',
			},
			{
				key: 'priceBySystemInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.priceBySystemInventory'
				),
				minWidth: '100px',
			},
		],
	},
	{
		align: 'center',
		key: 'realityInventory&priceByRealityInventory',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.byReality'
		),
		children: [
			{
				key: 'realityInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.realityInventory'
				),
				minWidth: '100px',
			},
			{
				key: 'priceByRealityInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.priceByRealityInventory'
				),
				minWidth: '100px',
			},
		],
	},
	{
		align: 'center',
		key: 'differrenceInventory&priceByDifferenceInventory',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.differrence'
		),
		children: [
			{
				key: 'differrenceInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.differrenceInventory'
				),
				minWidth: '100px',
			},
			{
				key: 'priceByDifferenceInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.priceByDifferenceInventory'
				),
				minWidth: '100px',
			},
		],
	},
])

const employeeOnInventory = ref([{ value: 0 }])
const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

type ModalProps = {
	modelValue: boolean
	action: number
	id?: string
}
const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	actions: EFormState.ADD,
	id: '',
})
const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val: boolean) {
		emit('update:modelValue', val)
	},
})

const computedTitle = computed(() => {
	if (props.action == EFormState.ADD) {
		return $t('module.inventory.inventoryHistory.form.title.add')
	}
	if (props.action == EFormState.EDIT) {
		return $t('module.inventory.inventoryHistory.form.title.edit')
	}
	if (props.action == EFormState.VIEW) {
		return $t('module.inventory.inventoryHistory.form.title.view')
	}
})
const instanceKey = new Date().getTime()
onMounted(() => {
	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
		}
	})
	getGarageEmployees({
		pageSize: 10000,
		pageNumber: 1,
	}).then((res) => {
		listGarageEmployees.value = res.data.map((e: any) => {
			return {
				value: e.id,
				label: e.fullName,
				origin: e,
			}
		})
	})
	getListProducts({
		pageSize: 10000,
		pageNumber: 1,
		type: 1,
	}).then((res) => {
		listGarageProducts.value = res.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	})
	if (props.action != EFormState.ADD && props.id) {
		getDetailInventoryHistory(props.id).then((res) => {
			let data = res.data
			employeeOnInventory.value = data.countersName.map((a: any) => {
				return { value: a.id }
			})
			countDate.value = dayjs(data.countDate, 'YYYY-MM-DD').format('DD/MM/YYYY')
			inventoryProductRow.value = data.inventoryHistoryDetails.map((a: any) => {
				let product = listGarageProducts.value.find((p) => {
					return p.value == a.productId
				})?.origin
				return {
					unit: product?.unit,
					productId: a.productId,
					unitPrice: a.unitPrice,
					systemInventory: a.systemInventory,
					realityInventory: a.realityInventory,
					differrenceInventory: a.differenceQuantity,
					priceBySystemInventory: a.priceSystemInventory,
					priceByRealityInventory: a.priceRealityInventory,
					priceByDifferenceInventory: a.differencePrice,
				}
			})
		})
	}
})
const addEmployee = () => {
	if (props.action != EFormState.VIEW) {
		employeeOnInventory.value.push({
			value: 0,
		})
	}
}
const onClickBack = () => {
	if (props.action != EFormState.VIEW) {
		emitter.emit('layout-pages-open-confirmClose', instanceKey)
	} else {
		lazyValue.value = false
	}
}

const computedProducts = computed(() => {
	let listProduct = inventoryProductRow.value.map((p) => {
		let productName = listGarageProducts.value.find((a) => {
			return a.value == p.productId
		})?.label
		return {
			productId: p.productId,
			unitPrice: p.unitPrice,
			systemInventory: p.systemInventory,
			realityInventory: p.realityInventory,
			unit: p.unit,
			productName: productName,
		}
	})
	return listProduct
})

const counterName = computed(() => {
	const counterName = employeeOnInventory.value.map((a) => {
		let employee = listGarageEmployees.value.find((e) => {
			return e.value == a.value
		})
		return { id: a.value, counterName: employee?.label }
	})
	return counterName
})

const onSubmit = () => {
	let data = {
		countersName: employeeOnInventory.value.map((a) => {
			let employee = listGarageEmployees.value.find((e) => {
				return e.value == a.value
			})
			return { id: a.value, counterName: employee?.label }
		}),
		countDate: dayjs(countDate.value, 'DD/MM/YYYY').format('YYYY-MM-DD'),
		inventoryHistoryDetails: inventoryProductRow.value.map((p) => {
			return {
				productId: p.productId,
				unitPrice: p.unitPrice,
				systemInventory: p.systemInventory,
				realityInventory: p.realityInventory,
			}
		}),
	}
	if (props.action == EFormState.ADD) {
		createInventoryHistory(data).then((res) => {
			if (res.code == 1) {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.createSuccess'),
					true
				)
				emit('refresh')
				lazyValue.value = false
			} else {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.createFailse'),
					false
				)
			}
		})
	} else {
		updateInventoryHistory(data, props.id).then((res) => {
			if (res.code == 1) {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.updateSuccess'),
					true
				)
				emit('refresh')
				lazyValue.value = false
			} else {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.updateFailse'),
					false
				)
			}
		})
	}
}
const addProductRowData = () => {
	inventoryProductRow.value.push({ ...baseInventoryProductRow })
}

const onSelectProduct = (row: any) => {
	let option = listGarageProducts.value.find((a) => {
		return a.value == row.productId
	})?.origin as any
	row.unit = option.unit
	row.unitPrice = option.purchasePrice
	row.systemInventory = option.quantity
}
</script>
