<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<!-- <div class="w-full lg:w-1/4">
                <p class="mb-1">
                    {{ $t("module.outboundTicket.filter.date") }}
                </p>
                <ACCDDatePicker
                    size="md"
                    v-model="lazyFilterDate"
                    formatter="DD/MM/YY"
                />
            </div> -->
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.outboundTicket.filter.type') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.type"
					:options="typeOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.outboundTicket.filter.type')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.outboundTicket.filter.code') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filter.code"
					:placeholder="$t('module.outboundTicket.filter.code')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/4">
				<p class="mb-1 font-semibold">
					{{ $t('module.outboundTicket.filter.status') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.status"
					:options="statusOptions"
					labelName="label"
					valueName="value"
					:placeholder="$t('module.outboundTicket.filter.status')"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>
		<div class="flex flex-col lg:flex-row justify-between gap-4">
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
			<div class="w-full lg:w-1/6 flex justify-end">
				<div>
					<ACCDButton
						@click="onFilter"
						type="primary"
						variant="fill"
						class="w-full"
					>
						<span class="font-medium">{{
							$t('module.outboundTicket.action.filter')
						}}</span>
					</ACCDButton>
				</div>
			</div>
			<!-- <div class="w-1/2 lg:w-1/6 mr-4">
                <ACCDButton
                    size="md"
                    type="primary"
                    variant="fill"
                    class="bg-info-secondary border-none w-full"
                    @click="
                        () => {
                            console.log('Not work at this moment');
                        }
                    "
                >
                    <span class="text-info-base font-medium">{{
                        $t("module.outboundTicket.action.add")
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
				<template #cell-statusStr="{ row, col, field }">
					<span
						class="h-7 rounded flex justify-center items-center w-40"
						:style="statusColor[row.status as keyof typeof statusColor]"
					>
						{{ row.statusStr }}
					</span>
				</template>
				<template #cell-action="{ row, col, field }">
					<ACCDAIcon
						@click="
							() => {
								confirmOutbound(row, EFormState.EDIT)
							}
						"
						name="Edit"
						class="cursor-pointer"
						size="20"
					></ACCDAIcon>
				</template>
			</ACCDTable>
		</div>
	</div>
	<OutboundTicketForm
		v-if="formConfig.show"
		v-model="formConfig.show"
		:state="formConfig.action"
		:outboundTicketId="formConfig.id"
		@refresh="
			() => {
				refresh({
					pageSize: 10,
					pageNumber: 1,
				})
			}
		"
	></OutboundTicketForm>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { formatIsoDateToDatetime } from '@/modules/sharedModules/component/constants'
import { findAllOutboundTicket } from '@/modules/outboundTicket/api'
import { EFormState } from '@/enums'
import OutboundTicketForm from '@/modules/outboundTicket/pages/OutboundTicketForm.vue'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayjs from 'dayjs'
dayjs.extend(customParseFormat)
const { $t } = useI18n()
const PAGE_SIZE = 10
const MAX_INT = import.meta.env.VITE_MAX_INTEGER
let listItem = ref([])

const filter = ref({
	date: '',
	type: '',
	code: '',
	status: '',
})
const lazyFilterDate = computed({
	get() {
		return dayjs(filter.value.date ? filter.value.date : new Date()).format(
			'DD/MM/YY'
		)
	},
	set(val: string) {
		filter.value.date = dayjs(val, 'DD/MM/YY').format('YYYY-MM-DD')
	},
})

const filterConfig = ref({
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
	outboundTicketId?: number | string
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
		headerName: $t('module.outboundTicket.table.stt'),
		width: '80px',
	},
	{
		key: 'createdAt',
		headerName: $t('module.outboundTicket.table.createdAt'),
	},
	{
		key: 'code',
		headerName: $t('module.outboundTicket.table.code'),
	},
	{
		key: 'typeStr',
		headerName: $t('module.outboundTicket.table.type'),
	},
	{
		key: 'statusStr',
		headerName: $t('module.outboundTicket.table.status'),
	},
	{
		key: 'action',
		headerName: '',
		width: '80px',
	},
]

const rowData = computed(() => {
	return (
		listItem?.value.map((item: OutboundTicket, index: number) => {
			return {
				...item,
				typeStr: $t(`module.outboundTicket.type.${item.type}`),
				statusStr: $t(`module.outboundTicket.status.${item.status}`),
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

const confirmOutbound = (row: any, action: EFormState) => {
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
	getTableRowData()
}

const getTableRowData = async () => {
	const _params = {
		fromDate: filterConfig.value.fromDate.value
			? dayjs(filterConfig.value.fromDate.value, 'DD/MM/YY').format(
					'YYYY-MM-DD'
			  )
			: null,
		toDate: filterConfig.value.toDate.value
			? dayjs(filterConfig.value.toDate.value, 'DD/MM/YY').format('YYYY-MM-DD')
			: null,
		type: filter.value.type,
		code: filter.value.code,
		status: filter.value.status,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	}
	const result = await findAllOutboundTicket(_params)
	listItem.value = result.data || []
	pagination.value.total = result.totalElement
}

const onRefreshData = async () => {
	listItem.value = []
	await getTableRowData()
}

const onFilter = () => {
	pagination.value.currentPage = 1
	getTableRowData()
}

const refresh = (config: any) => {
	pagination.value.currentPage = config.pageNumber
	pagination.value.perPage = config.pageSize
	getTableRowData()
}

await getTableRowData()
</script>
