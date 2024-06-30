<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.sellSparePart.filter.title') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filter.filter"
					:placeholder="$t('module.sellSparePart.filter.placeholderTitle')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.sellSparePart.filter.customerTypeId') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.customerTypeId"
					:options="customerTypeOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.sellSparePart.filter.customerTypeId')"
					class="font-medium"
				/>
			</div>

			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.sellSparePart.filter.status') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.deliveryStatus"
					:options="statusOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.sellSparePart.filter.status')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<div class="flex flex-col lg:flex-row-reverse md:flex-row-reverse gap-4">
			<!-- <div>
                <ACCDButton
                    @click="onFilter"
                    size="md"
                    type="secondary"
                    class="w-full"
                >
                    <span class="font-medium">{{
                        $t("module.sellSparePart.action.filter")
                    }}</span>
                </ACCDButton>
            </div> -->
			<div>
				<ACCDButton
					size="md"
					type="primary"
					variant="fill"
					class="bg-info-secondary border-none w-full"
					@click="onOpenFormModal"
				>
					<span class="text-info-base font-medium">{{
						$t('module.sellSparePart.action.add')
					}}</span>
				</ACCDButton>
			</div>
			<!-- <div>
				<ACCDButton
					size="md"
					type="secondary"
					variant="fill"
					class="bg-info-secondary w-full"
				>
					<ACCDIcon
						class="text-gray-700 pr-3"
						name="fa-solid fa-upload"
					></ACCDIcon>
					<span class="text-info-base font-medium">{{
						$t('module.customer.action.uploadExcel')
					}}</span>
				</ACCDButton>
			</div> -->
		</div>
		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-totalPrice="{ row }">
					{{
						row.totalPrice?.toString()?.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
					}}
				</template>
				<template #cell-deliveryStatus="{ row, col, field }">
					<!--                    <td class="bg-background-deliveryStatus px-4">-->
					<!--                        {{ row.deliveryStatusVn }}-->
					<!--                    </td>-->
					<!--                    <CDTag>{{ row.deliveryStatusVn }}</CDTag>-->

					<p
						class="py-[8px] px-[12px] rounded-[16px] text-center whitespace-nowrap w-fit"
						:class="getClassStatus(row.deliveryStatus)"
					>
						{{ getTitleStatus(row.deliveryStatus) }}
					</p>
				</template>
				<template #cell-action="{ row, col, field }">
					<!-- <ACCDDropdown>
                        <template v-slot:activator="{ props }">
                            <div
                                class="w-8 cursor-pointer flex justify-center"
                                v-bind="props"
                            >
                                <ACCDIcon
                                    class="text-base text-black"
                                    name="fa-solid fa-ellipsis-vertical"
                                ></ACCDIcon>
                            </div>
                        </template>
                        <div class="rounded-md border w-56 bg-white py-2">
                           
                        </div>
                    </ACCDDropdown> -->

					<div
						class="flex gap-3 cursor-pointer justify-center"
						@click="
							() => $router.push(`/app/sell/sell-spare-parts/${row.id}/edit`)
						"
					>
						<span class="text-gray-700 text-primary"> Xử lý </span>
					</div>
				</template>
			</ACCDTable>
		</div>
		<SellSparePartForm
			v-if="formConfig.show"
			v-model="formConfig.show"
			:state="formConfig.action"
			:show="formConfig.show"
			@refresh="onRefreshData"
			@close="formConfig.show = false"
		></SellSparePartForm>
	</div>
</template>

<script setup lang="ts">
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { getCustomerTypes } from '@/modules/customerType/api'
import { ISelectOption } from '@/types'
import SellSparePartForm from '@/modules/sellSparePart/pages/SellSparePartForm.vue'
import { findAllSellSparePartByGarageId } from '@/modules/sellSparePart/api'
import dayjs from 'dayjs'
import { EFormState } from '@/enums'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import { useI18n } from '@/composables/useI18n'
import { debounce } from 'lodash'
import { emitter } from '@/utils/mitt'
import useCDRouter from '@/composables/useRouter'
dayjs.extend(customParseFormat)

const { $t } = useI18n()
const PAGE_SIZE = 10
const MAX_INT = import.meta.env.VITE_MAX_INTEGER
const { router, query } = useCDRouter()

let listItem = ref([])
const customerTypeOptions = ref([] as unknown as ISelectOption[])
const formConfig = ref({
	show: false,
	action: EFormState.ADD as number,
})

const filter = ref({
	filter: '',
	customerTypeId: '',
	deliveryStatus: '',
})

const columnData = [
	{
		key: 'stt',
		headerName: $t('module.sellSparePart.table.stt'),
		width: '80px',
	},
	{
		key: 'sellCode',
		headerName: $t('module.sellSparePart.table.sellCode'),
	},
	{
		key: 'customerName',
		headerName: $t('module.sellSparePart.table.customerName'),
	},
	{
		key: 'createdAt',
		headerName: $t('module.sellSparePart.table.createdAt'),
	},
	{
		key: 'totalPrice',
		headerName: $t('module.sellSparePart.table.totalPrice'),
	},
	{
		key: 'deliveryStatus',
		headerName: $t('module.sellSparePart.table.deliveryStatus'),
	},
	{
		key: 'action',
		headerName: $t('module.sellSparePart.table.action'),
		align: 'center',
	},
]

const statusOptions = ref([
	{
		value: 2,
		label: $t('module.sellSparePart.deliveryStatus.2'),
	},
	{
		value: 6,
		label: $t('module.sellSparePart.deliveryStatus.6'),
	},
	{
		value: 4,
		label: $t('module.sellSparePart.deliveryStatus.7'),
	},
])
const onOpenFormModal = () => {
	formConfig.value.show = true
	formConfig.value.action = EFormState.ADD
}

const pagination = ref({
	perPage: PAGE_SIZE,
	total: 0,
	currentPage: 1,
})

const rowData = computed(() => {
	return (
		listItem?.value.map((item: SellSparePart, index: number) => {
			return {
				...item,
				createdAt: dayjs(item.createdAt).format('DD/MM/YY'),
				stt: index + 1 + PAGE_SIZE * (pagination.value.currentPage - 1),
			}
		}) || []
	)
})

onMounted(async () => {
	let result = await getCustomerTypes({
		pageSize: MAX_INT,
		pageNumber: 1,
	})
	customerTypeOptions.value = result.data.map((a: any) => {
		return {
			label: a.customerTypeName,
			value: a.id,
		}
	})

	emitter.on('router-on-set-query', async (params) => {
		pagination.value.currentPage = (
			params as { pageSize: number; currentPage: number }
		).currentPage
		pagination.value.perPage = (
			params as { pageSize: number; currentPage: number }
		).pageSize
		getTableRowData()
	})
	if (router.currentRoute.value.query) {
		if (Number(router.currentRoute.value.query.currentPage)) {
			pagination.value.currentPage = Number(
				router.currentRoute.value.query.currentPage
			)
		}
		if (Number(router.currentRoute.value.query.pageSize)) {
			pagination.value.perPage = Number(
				router.currentRoute.value.query.pageSize
			)
		}
		getTableRowData()
	}
})
onBeforeUnmount(() => {
	emitter.off('router-on-set-query')
})
const getTableRowData = async () => {
	const _params = {
		customerTypeId: filter.value.customerTypeId,
		filter: filter.value.filter,
		deliveryStatus: filter.value.deliveryStatus,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	}

	const result = await findAllSellSparePartByGarageId(_params)
	listItem.value = result.data || []
	pagination.value.total = result.totalElement
}
await getTableRowData()
const changePage = async (val: any) => {
	// pagination.value.currentPage = val.currentPage
	// getTableRowData()
	await query({
		currentPage: val.currentPage,
		pageSize: val.pageSize,
	})
}

const onRefreshData = async () => {
	listItem.value = []
	await getTableRowData()
}

const onFilter = () => {
	pagination.value.currentPage = 1
	getTableRowData()
}

watch(
	() => filter.value,
	debounce(() => {
		onFilter()
	}, 500),
	{ deep: true }
)

const getClassStatus = (status: number) => {
	switch (status) {
		case 1:
		case 2:
		case 3:
			return 'status-new-service-4 '
		case 4:
			return 'status-new-service-6 '
		case 5:
			return 'status-new-service-6 '
		case 6:
			return 'status-new-service-8'
		default:
			return 'status-new-service-1'
	}
}

const getTitleStatus = (status: number) => {
	switch (status) {
		case 4:
			return $t(`module.sellSparePart.deliveryStatus.7`)
		case 6:
			return $t(`module.sellSparePart.deliveryStatus.6`)
		default:
			return $t(`module.sellSparePart.deliveryStatus.2`)
	}
}
</script>
