<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inboundTicket.filter.type') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.type"
					:options="typeOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.inboundTicket.filter.type')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inboundTicket.filter.code') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.code"
					:placeholder="$t('module.inboundTicket.filter.code')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.inboundTicket.filter.status') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.status"
					:options="statusOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.inboundTicket.filter.status')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<div class="flex flex-col justify-between lg:flex-row gap-4">
			<div class="w-full lg:w-1/2">
				<div class="flex gap-4">
					<div class="flex gap-4 items-center">
						<span class="font-semibold">
							{{
								$t('module.inventory.inventoryHistory.filterConfig.from')
							}}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="filterConfig.fromDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
					<div class="flex gap-4 items-center">
						<span class="font-semibold">
							{{
								$t('module.inventory.inventoryHistory.filterConfig.to')
							}}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="filterConfig.toDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
				</div>
			</div>
			<div class="w-full lg:w-1/2 flex justify-end gap-4">
				<div>
					<ACCDButton
						size="md"
						type="primary"
						variant="fill"
						class="bg-info-secondary border-none w-full"
						@click="
							() => {
								createInbound()
							}
						"
					>
						<span class="text-info-base font-medium">{{
							$t('module.inboundTicket.action.add')
						}}</span>
					</ACCDButton>
				</div>
				<div>
					<ACCDButton
						@click="onFilter"
						type="primary"
						variant="fill"
						class="w-full"
					>
						<span class="text-white font-medium">{{
							$t('module.inboundTicket.action.filter')
						}}</span>
					</ACCDButton>
				</div>
			</div>
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
				<template #cell-statusStr="{ row, col, field }">
					<span
						class="h-7 rounded flex justify-center items-center w-40"
						:style="statusColor[row.status as keyof typeof statusColor]"
					>
						{{ row.statusStr }}
					</span>
				</template>
				<template #cell-action="{ row, col, field }">
					<span class="text-lg"
						><ACCDAIcon
							@click="
								() => {
									confirmInbound(row, EFormState.EDIT)
								}
							"
							name="Edit"
							class="cursor-pointer"
							size="20"
						></ACCDAIcon
					></span>
				</template>
			</ACCDTable>
		</div>
	</div>
	<InboundTicketForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:state="formConfig.action"
		:inboundTicketId="formConfig.id"
		@refresh="
			() => {
				refresh({
					pageSize: 10,
					pageNumber: 1,
				})
			}
		"
	></InboundTicketForm>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { formatIsoDateToDatetime } from '@/modules/sharedModules/component/constants'
import { findAllInboundTicket } from '@/modules/inboundTicket/api'
import { EFormState } from '@/enums'
import InboundTicketForm from '@/modules/inboundTicket/pages/InboundTicketForm.vue'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayjs from 'dayjs'
dayjs.extend(customParseFormat)
import { watch } from 'vue'
const { $t } = useI18n()
const PAGE_SIZE = 10
const MAX_INT = import.meta.env.VITE_MAX_INTEGER
let listItem = ref([])
// const lazyFilterDate = computed({
//     get() {
//         return dayjs(filter.value.date ? filter.value.date : new Date()).format(
//             "DD/MM/YY"
//         );
//     },
//     set(val: string) {
//         filter.value.date = dayjs(val, "DD/MM/YY").format("YYYY-MM-DD");
//     },
// });

const filterConfig = ref({
	type: '',
	code: '',
	status: '',
	fromDate: {
		value: '',
	},
	toDate: {
		value: '',
	},
})

const formConfig = ref({
	show: false,
	action: EFormState.ADD,
	id: '',
})

type ModalProps = {
	modelValue: boolean
	state: EFormState | undefined
	inboundTicketId?: number | string
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
}>()

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	state: EFormState.ADD,
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
		headerName: $t('module.inboundTicket.table.stt'),
		width: '80px',
	},
	{
		key: 'createdAt',
		headerName: $t('module.inboundTicket.table.createdAt'),
	},
	{
		key: 'code',
		headerName: $t('module.inboundTicket.table.code'),
	},
	{
		key: 'typeStr',
		headerName: $t('module.inboundTicket.table.type'),
	},
	{
		key: 'statusStr',
		headerName: $t('module.inboundTicket.table.status'),
		minWidth: '200px',
	},
	{
		key: 'action',
		headerName: '',
		width: '80px',
	},
]

const rowData = computed(() => {
	return (
		listItem?.value.map((item: InboundTicket, index: number) => {
			return {
				...item,
				typeStr: $t(`module.inboundTicket.type.${item.type}`),
				statusStr: $t(`module.inboundTicket.status.${item.status}`),
				createdAt: dayjs(item.createdAt).format('DD/MM/YY'),
				stt: index + 1 + PAGE_SIZE * (pagination.value.currentPage - 1),
			}
		}) || []
	)
})

const statusColor = {
	'0': {
		backgroundColor: '#E5E7EB',
		color: '#9CA3AF',
	},
	'1': {
		backgroundColor: '#D1FAE5',
		color: '#10B981',
	},
	'2': {
		backgroundColor: '#FEF3C7',
		color: '#F59E0B',
	},
}

const createInbound = () => {
	formConfig.value.show = true
	formConfig.value.id = ''
	formConfig.value.action = EFormState.ADD
}

const confirmInbound = (row: any, action: EFormState) => {
	formConfig.value.show = true
	formConfig.value.id = row.id
	formConfig.value.action = action
}

onMounted(async () => {})

const pagination = ref({
	perPage: PAGE_SIZE,
	total: 0,
	currentPage: 1,
})

const changePage = (val: any) => {
	pagination.value.currentPage = val.currentPage
}

const getTableRowData = async (params?: any) => {
	const result = await findAllInboundTicket({
		...params,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	})
	listItem.value = result.data || []
	pagination.value.total = result.totalElement
}

const onRefreshData = async () => {
	listItem.value = []
	await getTableRowData()
}

watch(
	() => pagination.value.currentPage,
	() => {
		const params = {
			fromDate: filterConfig.value.fromDate.value
				? dayjs(filterConfig.value.fromDate.value, 'DD/MM/YY').format(
						'YYYY-MM-DD'
				  )
				: null,
			toDate: filterConfig.value.toDate.value
				? dayjs(filterConfig.value.toDate.value, 'DD/MM/YY').format(
						'YYYY-MM-DD'
				  )
				: null,

			type: filterConfig.value.type,
			code: filterConfig.value.code,
			status: filterConfig.value.status,
		}

		getTableRowData(params)
	},
	{ deep: true }
)

const onFilter = () => {
	pagination.value.currentPage = 1
	const params = {
		fromDate: filterConfig.value.fromDate.value
			? dayjs(filterConfig.value.fromDate.value, 'DD/MM/YY').format(
					'YYYY-MM-DD'
			  )
			: null,
		toDate: filterConfig.value.toDate.value
			? dayjs(filterConfig.value.toDate.value, 'DD/MM/YY').format('YYYY-MM-DD')
			: null,

		type: filterConfig.value.type,
		code: filterConfig.value.code,
		status: filterConfig.value.status,
	}

	getTableRowData({
		...params,
		pageNumber: pagination.value.currentPage,
	})
}

const refresh = (config: any) => {
	pagination.value.currentPage = config.pageNumber
	pagination.value.perPage = config.pageSize
	getTableRowData()
}

await getTableRowData()
</script>
