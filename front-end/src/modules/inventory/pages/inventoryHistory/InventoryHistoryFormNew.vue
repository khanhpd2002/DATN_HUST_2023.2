<template>
	<ACCDModal
		v-model="lazyValue"
		:title="computedTitle"
		class-width="lg:w-[1200px] md:w-[730px] sm:w-[500px]"
		@close="closeDialog"
	>
		<div class="flex flex-col gap-4 py-4" v-if="!isLoading">
			<div class="flex gap-4">
				<div
					class="w-full flex flex-col gap-1"
					:class="props.action === EFormState.ADD ? '' : 'lg:w-1/2'"
				>
					<p class="font-semibold">
						{{ $t('module.inventory.inventoryHistory.form.date') }}
					</p>
					<ACCDDatePicker
						size="md"
						v-model="countDate"
						:disabled="props.action === EFormState.VIEW"
						class="font-medium"
					/>
				</div>
				<div
					class="w-full flex flex-col gap-1"
					:class="props.action === EFormState.EDIT ? 'lg:w-1/2' : ''"
					v-if="props.action === EFormState.EDIT"
				>
					<div
						class="flex flex-col gap-1"
						v-if="props.action === EFormState.EDIT"
					>
						<p class="mb-1 w-full font-semibold">
							{{ $t('module.inventory.inventoryHistory.form.approverName') }}
						</p>
						<ACCDSelect
							v-model="approverName"
							:options="approverInventory"
							:placeholder="
								$t('module.inventory.inventoryHistory.form.approverName')
							"
							class="font-medium"
						>
						</ACCDSelect>
					</div>
				</div>
			</div>

			<div>
				<div class="flex flex-col gap-4" v-if="props.action == EFormState.ADD">
					<div
						v-for="(e, i) in employeeOnInventory"
						class="flex flex-col gap-1"
					>
						<p class="mb-1 w-full font-semibold">
							{{
								$t('module.inventory.inventoryHistory.form.employee') +
								' ' +
								(i !== 0 ? i + 1 : '')
							}}
						</p>
						<ACCDSelect
							v-model="e.value"
							:options="listGarageEmployees"
							:placeholder="
								$t('module.inventory.inventoryHistory.form.employee')
							"
							class="font-medium"
						>
						</ACCDSelect>
					</div>
				</div>
				<div
					v-if="props.action == EFormState.EDIT"
					class="flex flex-col items-baseline"
				>
					<p class="mb-1 w-full font-semibold">
						{{ $t('module.inventory.inventoryHistory.form.employee') }}
					</p>
					<div class="flex rounded bg-common px-[12px] py-[8px]">
						<span
							v-for="(emp, index) in counterName"
							:key="emp.id"
							class="font-medium"
						>
							<span>{{ emp.counterName }} </span>
							<span class="mr-2"
								>{{ index == counterName.length - 1 ? '' : ',' }}
							</span>
						</span>
					</div>
				</div>
				<div
					class="flex items-center text-active mt-3"
					v-if="$props.action == EFormState.ADD"
				>
					<span @click="addEmployee" class="cursor-pointer">
						<ACCDAIcon name="AddCircle" />
					</span>
					<span @click="addEmployee" class="cursor-pointer">
						{{ $t('module.inventory.inventoryHistory.form.addEmployee') }}</span
					>
				</div>
			</div>
			<div class="flex justify-between items-center">
				<span class="text-[16px] font-bold">{{
					$t('module.inventory.inventoryHistory.form.productList')
				}}</span>
				<ACCDButton
					v-if="props.action !== EFormState.VIEW"
					type="secondary"
					@click="openModalUploadExcel"
				>
					<ACCDAIcon class="mr-2" name="DocumentUpload" size="20"></ACCDAIcon>
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
					:rowData="computedInventoryProductRow"
					:show-summary="true"
				>
					<template #cell-productId="{ row }">
						<span>
							{{
								listGarageProducts.find((item) => item.value == row.productId)
									?.label
							}}
						</span>
					</template>
					<template #cell-unitPrice="{ row }">
						{{ formatPriceVN(row.systemUnitPrice) }}
					</template>
					<template #cell-priceBySystemInventory="{ row }">
						{{ formatPriceVN(row.systemUnitPrice * row.systemInventory) }}
					</template>
					<template #cell-realityInventory="{ row, rowIndex }">
						<div v-if="props.action === EFormState.VIEW">
							{{ row.realityInventory }}
						</div>
						<ACCDInputNumber
							v-else
							:readonly="props.action === EFormState.VIEW"
							v-model="row.realityInventory"
							@update:modelValue="(val:number)=> {
                                isEmptyProductQuanity = false
                                return inventoryProductRow[rowIndex].realityInventory=val} "
							@keypress="validatePrice"
						>
						</ACCDInputNumber>
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
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-systemInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-priceBySystemInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-realityInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-priceByRealityInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-differenceInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
					<template #sum-priceByDifferenceInventory="{ value }">
						<td class="cd-table__td--sum font-bold px-4">
							{{ formatPriceVN(value) }}
						</td>
					</template>
				</ACCDTable>
				<!-- <span
			v-if="props.action !== EFormState.VIEW"
			class="cursor-pointer"
			style="color: #25b3e8"
			@click="addProductRowData"
			>{{
				$t("module.inventory.inventoryHistory.form.addProduct")
			}}</span
		> -->
				<div v-if="isEmptyProductQuanity">
					<span class="italic text-red-500">
						{{ $t('module.sellSparePart.error.empty') }}
					</span>
				</div>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton @click="onClickBack" type="secondary" size="md">
					{{ $t('module.inventory.inventoryHistory.form.back') }}
				</ACCDButton>
				<ACCDButton
					v-if="action != EFormState.VIEW"
					@click="onSubmit"
					type="primary"
					size="md"
				>
					{{
						action == EFormState.ADD
							? $t('module.inventory.inventoryHistory.form.confirm')
							: $t('module.inventory.inventoryHistory.form.edit')
					}}
				</ACCDButton>
			</div>
		</template>

		<Loading :isLoading="isLoading" />
	</ACCDModal>
	<InventoryMinutes
		v-if="onShowBill"
		@close="onShowBill = false"
		:inventoryDate="countDate"
		:listEmployee="counterName"
		:productInfo="computedProducts"
	>
	</InventoryMinutes>

	<ModalUploadExcel ref="ModalUploadExcelRef" />
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
import {
	adjustmentInventoryHistory,
	createInventoryHistory,
	getDetailInventoryHistory,
	getProductForAdjustmentInventoryHistory,
	getProductForInventoryHistory,
	updateInventoryHistory,
} from '@/modules/inventory/api'
import { emitter } from '@/utils/mitt'
import {
	formatPriceVN,
	validatePrice,
} from '@/modules/sharedModules/component/constants'
import { useI18n } from '@/composables/useI18n'
import Loading from '@/modules/sharedModules/component/Loading.vue'
import ModalUploadExcel from '@/modules/sharedModules/component/ModalUploadExcel.vue'
dayjs.extend(customParseFormat)
const { $t } = useI18n()

const onShowBill = ref(false)

type InventoryProductRow = {
	productId: number
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

const inventoryProductColumn = ref([
	{
		key: 'productId',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.productId'
		),
		minWidth: '300px',
		class: 'font-bold',
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
		minWidth: '250px',
		children: [
			{
				key: 'realityInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.realityInventory'
				),

				width: '120px',
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
const approverInventory = ref([{ value: 0 }])
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
const computedInventoryProductRow = computed(() => {
	return inventoryProductRow.value.map((row) => {
		return {
			...row,
			unitPrice: row.systemUnitPrice,
			priceBySystemInventory: row.systemUnitPrice * row.systemInventory,
			priceByRealityInventory: row.realityUnitPrice * row.realityInventory,
			differenceInventory: row.realityInventory - row.systemInventory,
			priceByDifferenceInventory: Math.abs(
				row.realityUnitPrice * row.realityInventory -
					row.systemUnitPrice * row.systemInventory
			),
		}
	})
})

watch(
	() => tempProductRow.value,
	() => {
		if (props.action == EFormState.ADD) {
			inventoryProductRow.value = listGarageProducts.value.map((item) => {
				let res = tempProductRow.value.find((el) => el.productId == item.value)

				return {
					productId: item.value,
					realityUnitPrice: item.origin.realityUnitPrice,
					systemUnitPrice: item.origin.systemUnitPrice,
					systemInventory: item.origin.quantity,
					realityInventory: res?.realityInventory ? res?.realityInventory : 0,
					unit: item.origin.unit,
					priceBySystemInventory: 0,
					priceByRealityInventory: 0,
					differenceInventory: 0,
					priceByDifferenceInventory: 0,
				}
			}) as InventoryProductRow[]
		}
	},
	{ deep: true }
)

watch(
	() => listGarageProducts.value,
	() => {
		if (props.action == EFormState.ADD) {
			inventoryProductRow.value = listGarageProducts.value.map((item) => {
				return {
					productId: item.value,
					realityUnitPrice: item.origin.realityUnitPrice
						? item.origin.realityUnitPrice
						: 0,
					systemUnitPrice: item.origin.systemUnitPrice
						? item.origin.systemUnitPrice
						: 0,
					systemInventory: item.origin.quantity,
					realityInventory: 0,
					unit: item.origin.unit,

					priceBySystemInventory: 0,
					priceByRealityInventory: 0,
					differenceInventory: 0,
					priceByDifferenceInventory: 0,
				}
			})
		} else {
			inventoryProductRow.value = listGarageProducts.value.map((item) => {
				return {
					productId: item.value,
					realityUnitPrice: item.origin.realityUnitPrice
						? item.origin.realityUnitPrice
						: 0,
					systemUnitPrice: item.origin.systemUnitPrice
						? item.origin.systemUnitPrice
						: 0,
					systemInventory: item.origin.systemInventory,
					realityInventory: item.origin.realityInventory,
					unit: item.origin.unit,

					priceBySystemInventory: item.origin.priceBySystemInventory,
					priceByRealityInventory: item.origin.priceByRealityInventory,
					differenceInventory: item.origin.differenceInventory,
					priceByDifferenceInventory: item.origin.priceByDifferenceInventory,
				}
			})
		}
	},
	{ deep: true }
)
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

	await getProductList()

	if (props.action != EFormState.ADD && props.id) {
		getDetailInventoryHistory(props.id).then((res) => {
			let data = res.data
			employeeOnInventory.value = data.countersName.map((a: any) => {
				return { value: a.id }
			})
			approverInventory.value = data.approvers.map((a: any) => {
				return { value: a.id }
			})
			countDate.value = dayjs(data.countDate, 'YYYY-MM-DD').format('DD/MM/YYYY')

			tempProductRow.value = data.inventoryHistoryDetails.map((a: any) => {
				let product = listGarageProducts.value.find((p) => {
					return p.value == a.productId
				})?.origin

				return {
					unit: product?.unit,
					productId: a.productId,
					realityUnitPrice: a.realityUnitPrice,
					systemUnitPrice: a.systemUnitPrice,
					systemInventory: a.systemInventory,
					realityInventory: a.realityInventory,
					differenceInventory: a.differenceQuantity,
					priceBySystemInventory: a.priceSystemInventory,
					priceByRealityInventory: a.priceRealityInventory,
					priceByDifferenceInventory: a.differencePrice,
				}
			})
		})
	}
})

const closeDialog = () => {
	emitter.emit('layout-page-close-confirmClose', instanceKey)
}

const getProductList = async () => {
	isLoading.value = true

	if (props.action == EFormState.ADD) {
		const res = await getProductForInventoryHistory()
		if (res.code == 1) {
			listGarageProducts.value = res.data.map((e: any) => {
				return {
					value: e.id,
					label: e.code + ' - ' + e.name,
					origin: e,
				}
			})
		}

		isLoading.value = false
	} else {
		const res = await getProductForAdjustmentInventoryHistory(props.id)
		if (res.code == 1) {
			listGarageProducts.value = res.data.inventoryHistoryDetails.map(
				(e: any) => {
					return {
						value: e.productId,
						label: e.productCode + ' - ' + e.productName,
						origin: e,
					}
				}
			)
			isLoading.value = false
		}
	}
}

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
			realityUnitPrice: p.realityUnitPrice,
			systemUnitPrice: p.systemUnitPrice,
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

const approverName = ref({ id: '', approverName: '' })

const approvers = computed(() => {
	const approverName = approverInventory.value.map((a) => {
		let employee = listGarageEmployees.value.find((e) => {
			return e.value == a.value
		})
		return { id: a.value, approverName: employee?.label }
	})
	return approverName
})

const onSubmit = () => {
	let data = {
		countersName: employeeOnInventory.value
			.filter((a) => a.value !== 0)
			.map((a) => {
				let employee = listGarageEmployees.value.find((e) => {
					return e.value == a.value
				})
				return { id: a.value, counterName: employee?.label }
			}),
		approvers: [],
		countDate: dayjs(countDate.value, 'DD/MM/YYYY').format('YYYY-MM-DD'),
		inventoryHistoryDetails: inventoryProductRow.value.map((p) => {
			return {
				productId: p.productId,
				realityUnitPrice: p.realityUnitPrice,
				systemUnitPrice: p.systemUnitPrice,
				systemInventory: p.systemInventory ? p.systemInventory : 0,
				realityInventory: p.realityInventory,
			}
		}),
	}

	inventoryProductRow.value.forEach((item) => {
		if (item.realityInventory == null) {
			isEmptyProductQuanity.value = true
		}
	})

	if (isEmptyProductQuanity.value) {
		return 0
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
					$t('module.inventory.inventoryHistory.form.toast.createFail'),
					false
				)
			}
		})
	} else {
		adjustmentInventoryHistory(data, props.id).then((res) => {
			if (res.code == 1) {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.updateSuccess'),
					true
				)
				emit('refresh')
				lazyValue.value = false
			} else {
				$toast(
					$t('module.inventory.inventoryHistory.form.toast.updateFail'),
					false
				)
			}
		})
	}
}

const ModalUploadExcelRef = ref()
const openModalUploadExcel = () => {
	const garageId = localStorage.getItem('garageId')
	ModalUploadExcelRef?.value?.open({
		templateUrl: 'download-template/distributor',
		templateName: 'distributor_template.xlsx',
		submitUrl: `import-excels/${garageId}/distributors`,
	})
}
</script>

<style lang="scss">
.cd-table__th {
	font-weight: 700 !important;
}
</style>
