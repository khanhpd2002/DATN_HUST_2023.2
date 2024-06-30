<template>
	<div class="flex items-center gap-3 mb-4">
		<ACCDAIcon
			name="ArrowLeft"
			class="cursor-pointer"
			@click="() => router.back()"
		/>
		<p class="font-bold text-[16px]">
			{{ $t('module.inventory.inventoryHistory.form.detail') }}
		</p>
	</div>
	<div class="bg-white rounded p-4">
		<div class="flex flex-col gap-4 py-4" v-if="!isLoading">
			<div class="w-full lg:w-1/2 flex flex-col gap-1">
				<p class="font-semibold">
					{{ $t('module.inventory.inventoryHistory.form.date') }}
				</p>
				<ACCDDatePicker
					size="md"
					v-model="countDate"
					:disabled="true"
					class="font-medium"
				/>
			</div>
			<div>
				<div class="flex flex-col items-baseline">
					<p class="mb-1 w-full font-semibold">
						{{ $t('module.inventory.inventoryHistory.form.employee') }}
					</p>
					<div class="flex rounded bg-common px-[12px] py-[8px]">
						<span
							v-if="counterName.length > 0"
							v-for="(emp, index) in counterName"
							:key="emp.id"
							class="font-medium"
						>
							<span>{{ emp.counterName }} </span>
							<span class="mr-2"
								>{{ index == counterName.length - 1 ? '' : ',' }}
							</span>
						</span>
						<span v-else>
							{{ $t('module.inventory.inventoryHistory.form.employee') }}</span
						>
					</div>
				</div>
			</div>
			<div class="flex justify-between items-center">
				<span class="text-[16px] font-semibold">{{
					$t('module.inventory.inventoryHistory.form.productList')
				}}</span>

				<div class="flex gap-4">
					<ACCDButton type="secondary" @click="handleChangePrint" class="px-16">
						{{ $t('module.inventory.inventoryHistory.form.printBill') }}
					</ACCDButton>
					<ACCDButton
						type="primary"
						class="px-16"
						v-if="isChangeInventory"
						@click="hanldeOpenChange"
					>
						{{ $t('module.inventory.inventoryHistory.form.change') }}
					</ACCDButton>
				</div>
			</div>

			<div>
				<ACCDTable
					:columns="inventoryProductColumn"
					:rowData="computedInventoryProductRow"
					:show-summary="true"
				>
					<template #cell-product="{ row }">
						<span> {{ row.productCode }} - {{ row.productName }} </span>
					</template>
					<template #cell-unitPrice="{ row }">
						{{ formatPriceVN(row.systemUnitPrice) }}
					</template>
					<template #cell-priceBySystemInventory="{ row }">
						{{ formatPriceVN(row.systemUnitPrice * row.systemInventory) }}
					</template>
					<template #cell-realityInventory="{ row, rowIndex }">
						{{ row.realityInventory }}
					</template>
					<template #cell-priceByRealityInventory="{ row }">
						{{ formatPriceVN(row.priceByRealityInventory) }}
					</template>
					<template #cell-differrenceInventory="{ row }">
						<td class="w-20 cd-table__td">
							{{ formatPriceVN(row.differrenceInventory) }}
						</td>
					</template>
					<template #cell-priceByDifferenceInventory="{ row }">
						<td class="cd-table__td">
							{{ formatPriceVN(row.priceByDifferenceInventory) }}
						</td>
					</template>

					<template #sum-unitPrice="{ value }">
						<td class="cd-table__td--sum px-4">{{ formatPriceVN(value) }}</td>
					</template>

					<template #sum-priceBySystemInventory="{ value }">
						<td class="cd-table__td--sum px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>

					<template #sum-priceByRealityInventory="{ value }">
						<td class="cd-table__td--sum px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>

					<template #sum-priceByDifferenceInventory="{ value }">
						<td class="cd-table__td--sum px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
				</ACCDTable>

				<div v-if="isEmptyProductQuanity">
					<span class="italic text-red-500">
						{{ $t('module.sellSparePart.error.empty') }}
					</span>
				</div>
			</div>
		</div>
	</div>
	<InventoryHistoryFormNew
		v-if="formConfigNew.show"
		v-model="formConfigNew.show"
		:action="formConfigNew.actions"
		:id="formConfigNew.id"
		@refresh="() => router.push('/app/inventory/histories')"
	>
	</InventoryHistoryFormNew>

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
import { computed, onMounted, ref, watch } from 'vue'
import { getGarageEmployees } from '@/modules/employee/api'
import { ISelectOption } from '@/types'
import { $toast } from '@/main.js'
import { EFormState } from '@/enums'
import dayjs from 'dayjs'
import InventoryMinutes from '@/modules/sharedModules/pages/formHTML/inventoryMinutes.vue'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import InventoryHistoryFormNew from '@/modules/inventory/pages/inventoryHistory/InventoryHistoryFormNew.vue'
import {
	createInventoryHistory,
	getDetailInventoryHistory,
	getProductForInventoryHistory,
	updateInventoryHistory,
} from '@/modules/inventory/api'
import { emitter } from '@/utils/mitt'
import {
	formatPriceVN,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { useI18n } from '@/composables/useI18n'
import router from '@/router'
import { useRoute } from 'vue-router'
const route = useRoute()
dayjs.extend(customParseFormat)
const { $t } = useI18n()

const onShowBill = ref(false)

type InventoryProductRow = {
	productId: number
	productCode: string
	productName: string
	systemUnitPrice: number
	realityUnitPrice: number
	systemInventory: number
	realityInventory: number
	unit: string
	priceBySystemInventory: number
	priceByRealityInventory: number
	differenceInventory: number
	priceByDifferenceInventory: number
}

type InventoryHistoryDetails = {
	differencePrice: number
	differenceQuantity: number
	id: number
	inventoryHistoryId: number
	priceRealityInventory: number
	priceSystemInventory: number
	productCode: string
	productId: number
	productName: string
	realityInventory: number
	realityUnitPrice: number
	systemInventory: number
	systemUnitPrice: number
	unit: string
}

const baseInventoryProductRow = {
	productId: 0,
	systemUnitPrice: 0,
	realityUnitPrice: 0,
	systemInventory: 0,
	realityInventory: 0,
	unit: '',
	priceBySystemInventory: 0,
	priceByRealityInventory: 0,
	differenceInventory: 0,
	priceByDifferenceInventory: 0,
} as InventoryProductRow

const listGarageProducts = ref([] as (ISelectOption & { origin: any })[])
const listGarageEmployees = ref([] as ISelectOption[])
const countDate = ref(dayjs().format('DD/MM/YYYY'))
const inventoryProductRow = ref([] as InventoryProductRow[])
const tempProductRow = ref([] as InventoryProductRow[])
const isChangeInventory = ref<boolean>(false)
const dataPrint = ref({
	countDate: '',
	counterName: [] as any[],
	computedInventoryProductRow: [] as any[],
})

const formConfigNew = ref({
	show: false,
	actions: EFormState.ADD,
	id: '',
})

const inventoryProductColumn = ref([
	{
		key: 'product',
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
			},
			{
				key: 'priceBySystemInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.priceBySystemInventory'
				),
			},
		],
	},
	{
		align: 'center',
		key: 'realityInventory&priceByRealityInventory',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.byReality'
		),
		minWidth: '200px',
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
			},
		],
	},
	{
		align: 'center',
		key: 'differenceInventory&priceByDifferenceInventory',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.difference'
		),
		children: [
			{
				key: 'differenceInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.differenceInventory'
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
const isEmptyProductQuanity = ref<boolean>(false)
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

const isLoading = ref<boolean>(false)
const historyId = route.params.id.toString()

const instanceKey = new Date().getTime()
const computedInventoryProductRow = computed(() => {
	return inventoryProductRow.value.map((row) => {
		return {
			...row,
			unitPrice: row.systemUnitPrice,
			priceBySystemInventory: row.systemUnitPrice * row.systemInventory,
			priceByRealityInventory: row.realityUnitPrice * row.realityInventory,
		}
	})
})

// watch(
// 	() => tempProductRow.value,
// 	() => {
// 		console.log('tempProductRow', tempProductRow.value)
// 		inventoryProductRow.value = listGarageProducts.value.map((item) => {
// 			let res = tempProductRow.value.find((el) => el.productId == item.value)

// 			return {
// 				productId: item.value,
// 				realityUnitPrice: item.origin.realityUnitPrice,
// 				systemUnitPrice: item.origin.systemUnitPrice,
// 				systemInventory: item.origin.quantity,
// 				realityInventory: res?.realityInventory ? res?.realityInventory : 0,
// 				unit: item.origin.unit,
// 				priceBySystemInventory: 0,
// 				priceByRealityInventory: 0,
// 				differenceInventory: 0,
// 				priceByDifferenceInventory: 0,
// 			}
// 		}) as InventoryProductRow[]
// 	},
// 	{ deep: true }
// )

// watch(
// 	() => listGarageProducts.value,
// 	() => {
// 		console.log('listGarageProducts', listGarageProducts.value)
// 		inventoryProductRow.value = listGarageProducts.value.map((item) => {
// 			return {
// 				productId: item.value,
// 				realityUnitPrice: item.origin.realityUnitPrice
// 					? item.origin.realityUnitPrice
// 					: 0,
// 				systemUnitPrice: item.origin.systemUnitPrice
// 					? item.origin.systemUnitPrice
// 					: 0,
// 				systemInventory: item.origin.quantity,
// 				realityInventory: 0,
// 				unit: item.origin.unit,

// 				priceBySystemInventory: 0,
// 				priceByRealityInventory: 0,
// 				differenceInventory: 0,
// 				priceByDifferenceInventory: 0,
// 			}
// 		})
// 	},
// 	{ deep: true }
// )
onMounted(async () => {
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

	handleGetDetail()

	// await getProductList()

	// if (id) {
	// 	getDetailInventoryHistory(id).then((res) => {
	// 		let data = res.data
	// 		employeeOnInventory.value = data.countersName.map((a: any) => {
	// 			return { value: a.id }
	// 		})
	// 		countDate.value = dayjs(data.countDate, 'YYYY-MM-DD').format('DD/MM/YYYY')
	// 		isChangeInventory.value = data.canAdjustment

	// 		console.log(data.inventoryHistoryDetails)

	// 		tempProductRow.value = data.inventoryHistoryDetails.map(
	// 			(a: InventoryHistoryDetails) => {
	// 				return {
	// 					productId: a.productId,
	// 					realityUnitPrice: a.realityUnitPrice,
	// 					systemUnitPrice: a.systemUnitPrice,
	// 					systemInventory: a.systemInventory,
	// 					realityInventory: a.realityInventory,
	// 					differenceInventory: a.differenceQuantity,
	// 					priceBySystemInventory: a.priceSystemInventory,
	// 					priceByRealityInventory: a.priceRealityInventory,
	// 					priceByDifferenceInventory: a.differencePrice,
	// 				}
	// 			}
	// 		)
	// 	})
	// }
})

const handleGetDetail = async () => {
	const result = await getDetailInventoryHistory(historyId)

	if (result.code == 1) {
		employeeOnInventory.value = result.data.countersName.map((a: any) => {
			return { value: a.id }
		})
		countDate.value = dayjs(result.data.countDate, 'YYYY-MM-DD').format(
			'DD/MM/YYYY'
		)
		isChangeInventory.value = result.data.canAdjustment

		inventoryProductRow.value = result.data.inventoryHistoryDetails.map(
			(item: InventoryHistoryDetails) => {
				return {
					...item,
					realityUnitPrice: item.realityUnitPrice ? item.realityUnitPrice : 0,
					systemUnitPrice: item.systemUnitPrice ? item.systemUnitPrice : 0,
					systemInventory: item.systemInventory,
					realityInventory: item.realityInventory ? item.realityInventory : 0,
					unit: item.unit,
					priceBySystemInventory: item.priceSystemInventory
						? item.priceSystemInventory
						: 0,
					priceByRealityInventory: item.priceRealityInventory
						? item.priceRealityInventory
						: 0,
					differenceInventory: item.differenceQuantity
						? item.differenceQuantity
						: 0,
					priceByDifferenceInventory: item.differencePrice
						? item.differencePrice
						: 0,
				}
			}
		)
	}
}

// const getProductList = async () => {
// 	isLoading.value = true
// 	const res = await getProductForInventoryHistory()

// 	if (res.code == 1) {
// 		listGarageProducts.value = res.data.map((e: any) => {
// 			return {
// 				value: e.id,
// 				label: e.code + ' - ' + e.name,
// 				origin: e,
// 			}
// 		})
// 	}

// 	isLoading.value = false
// }

const computedProducts = computed(() => {
	let listProduct = inventoryProductRow.value.map((p) => {
		let productName = listGarageProducts.value.find((a) => {
			return a.value == p.productId
		})?.label

		// return {
		// 	productId: p.productId,
		// 	realityUnitPrice: p.realityUnitPrice,
		// 	systemUnitPrice: p.systemUnitPrice,
		// 	systemInventory: p.systemInventory,
		// 	realityInventory: p.realityInventory,
		// 	unit: p.unit,
		// 	productName: productName,
		// }
		return {
			differenceInventory: p.differenceInventory,
			priceByDifferenceInventory: p.priceByDifferenceInventory,
			priceByRealityInventory: p.priceByRealityInventory,
			priceBySystemInventory: p.priceBySystemInventory,
			productId: p.productId,
			realityInventory: p.realityInventory,
			realityUnitPrice: p.realityUnitPrice,
			systemInventory: p.systemInventory,
			systemUnitPrice: p.systemUnitPrice,
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

const hanldeOpenChange = () => {
	formConfigNew.value.id = route.params.id.toString()
	formConfigNew.value.actions = EFormState.EDIT
	formConfigNew.value.show = true
}

const handleChangePrint = () => {
	let route = router.resolve({ path: '/app/inventory-minutes-sheet' })
	window.open(route.href, '_blank')
	dataPrint.value.countDate = countDate.value
	dataPrint.value.counterName = counterName.value
	dataPrint.value.computedInventoryProductRow =
		computedInventoryProductRow.value

	localStorage.setItem('historyDataPrint', JSON.stringify(dataPrint.value))
	localStorage.setItem(
		'inventoryProductRow',
		JSON.stringify(inventoryProductRow.value)
	)
}
</script>

<style lang="scss">
.cd-table__td--sum {
	font-weight: 700 !important;
}
.cd-table__th {
	font-weight: 700 !important;
}
</style>
