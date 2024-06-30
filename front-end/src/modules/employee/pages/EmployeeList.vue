<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer align="end">
			<div class="w-full w-1/5">
				<p class="mb-1 mb-1 font-semibold">
					{{ $t('module.employee.filter.fullName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filter.fullName"
					:placeholder="$t('module.employee.filter.fullName')"
					class="font-medium"
					:trim="true"
				/>
			</div>

			<div class="w-full w-1/5">
				<p class="mb-1 font-semibold">
					{{ $t('module.employee.form.status') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.status"
					:options="statusOptions"
					:placeholder="$t('module.employee.form.status')"
					class="font-medium"
				/>
			</div>
			<div class="w-full w-1/5">
				<p class="mb-1 font-semibold">
					{{ $t('module.employee.form.contractType') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filter.type"
					:options="contractTypeOption"
					:placeholder="$t('module.employee.form.contractType')"
					class="font-medium"
				/>
			</div>

			<div class="flex flex-col w-full w-1/5">
				<span class="font-semibold">
					{{ $t('module.employee.form.startDate') }}</span
				>
				<ACCDDatePicker
					size="md"
					v-model="filter.startDate"
					formatter="DD/MM/YY"
					class="font-medium"
				/>
			</div>

			<div class="flex flex-col w-full w-1/5">
				<span class="font-semibold">
					{{ $t('module.employee.form.endDate') }}</span
				>
				<ACCDDatePicker
					size="md"
					v-model="filter.endDate"
					formatter="DD/MM/YY"
					class="font-medium"
				/>
			</div>
		</WrapFlexContainer>

		<div class="flex justify-end">
			<ACCDButton
				size="md"
				:disabled="false"
				type="primary"
				@click="() => onOpenFormModal()"
			>
				<span class="font-medium">{{ $t('module.employee.action.add') }}</span>
			</ACCDButton>
		</div>

		<TableContainer>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
			>
				<template #cell-statusVn="{ row, col, field }">
					<p
						class="w-full px-3 py-2 rounded-[16px] text-center whitespace-nowrap"
						:class="`status-${row.status}`"
					>
						{{ row.statusVn }}
					</p>
				</template>
				<template #cell-startDate="{ row, col, field }">
					{{ row.startDate ? row.startDate : '-' }}
				</template>
				<template #cell-endDate="{ row, col, field }">
					{{ row.endDate ? row.endDate : '-' }}
				</template>
				<template #cell-action="{ row, col, field }">
					<div
						class="cursor-pointer flex justify-center"
						@click="() => onOpenFormModal(row.id, row, EFormState.EDIT)"
					>
						<!-- <ACCDIcon
                            name="fa-solid fa-pen-to-square"
                            class="cursor-pointer p-2"
                        ></ACCDIcon> -->
						<img src="@/assets/images/svg/edit.svg" class="cursor-pointer" />
					</div>
				</template>
			</ACCDTable>
		</TableContainer>

		<EmployeeForm
			v-if="openModal"
			v-model="openModal"
			:state="stateModal"
			:employeeId="employeeId"
			@refresh="onRefreshData"
		></EmployeeForm>
	</div>
</template>

<script setup lang="ts">
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'

import { computed, onMounted, ref } from 'vue'
import { getGarageEmployees } from '../api'
import { EFormState } from '@/enums'
import { IEmployee } from '@/types'
import EmployeeForm from '@/modules/employee/pages/EmployeeForm.vue'
import { useI18n } from '@/composables/useI18n'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayjs from 'dayjs'
import { watch } from 'vue'
import { debounce } from 'lodash'
dayjs.extend(customParseFormat)
const { $t } = useI18n()

const PAGE_SIZE = 10

let listItem = ref([])
const openModal = ref(false)
const stateModal = ref<EFormState>()
const employeeId = ref<number | undefined>(undefined)

const filter = ref({
	fullName: '',
	startDate: '',
	endDate: '',
	status: '',
	type: '',
})

const columnData = [
	{
		key: 'stt',
		headerName: $t('module.employee.table.stt'),
		minWidth: '80px',
	},
	{
		key: 'fullName',
		headerName: $t('module.employee.table.fullName'),
		minWidth: '200px',
	},

	// {
	//     key: "genderVn",
	//     headerName: $t("module.employee.table.gender"),
	// },
	// {
	// 	key: 'birthday',
	// 	headerName: $t('module.employee.table.birthday'),
	// 	minWidth: '100px',
	// },
	{
		key: 'phoneNumber',
		headerName: $t('module.employee.table.phoneNumber'),
		minWidth: '200px',
	},
	{
		key: 'type',
		headerName: $t('module.employee.table.type'),
		minWidth: '150px',
	},
	{
		key: 'contractTypeVn',
		headerName: $t('module.employee.table.contractType'),
		minWidth: '150px',
	},

	{
		key: 'startDate',
		headerName: $t('module.employee.table.startDate'),
		minWidth: '150px',
	},
	{
		key: 'endDate',
		headerName: $t('module.employee.table.endDate'),
		minWidth: '150px',
	},
	{
		key: 'statusVn',
		headerName: $t('module.employee.table.status'),
	},
	{
		key: 'action',
		headerName: $t('module.employee.table.action'),
		width: '100px',
		align: 'center',
	},
]

const statusOptions = ref([
	{
		label: 'Tất cả',
		value: '',
	},
])

const contractTypeOption = ref([
	{
		label: 'Tất cả',
		value: 0,
	},
])

const pagination = ref({
	perPage: PAGE_SIZE,
	total: 0,
	currentPage: 1,
})

const rowData = computed(() => {
	return (
		listItem.value.map((item: IEmployee, index: number) => {
			return {
				...item,
				genderVn:
					item.gender != null || item.gender != undefined
						? $t(`module.employee.gender.${item.gender}`)
						: '',
				statusVn:
					item.status != null || item.status != undefined
						? $t(`module.employee.status.${item.status}`)
						: '',
				contractTypeVn:
					item.contractType != null || item.contractType != undefined
						? $t(`module.employee.contractType.${item.contractType}`)
						: '',
				birthday: item.birthday
					? dayjs(item.birthday).format('DD/MM/YYYY')
					: null,
				startDate: item.startDate
					? dayjs(item.startDate).format('DD/MM/YYYY')
					: null,
				endDate: item.endDate ? dayjs(item.endDate).format('DD/MM/YYYY') : null,
				stt: index + 1 + PAGE_SIZE * (pagination.value.currentPage - 1),
				status: item.status,
			}
		}) || []
	)
})

const EmployeeFormRef = ref<InstanceType<typeof EmployeeForm>>()
const onOpenFormModal = (
	id?: number,
	params?: IEmployee,
	state?: EFormState
) => {
	employeeId.value = id || undefined
	stateModal.value = state ? state : EFormState.ADD
	openModal.value = true
}
const getTableRowData = async () => {
	const _params = {
		fullName: filter.value.fullName,
		startDate: filter.value.startDate
			? dayjs(filter.value.startDate, 'DD/MM/YY').format('YYYY-MM-DD')
			: '',
		endDate: filter.value.endDate
			? dayjs(filter.value.endDate, 'DD/MM/YY').format('YYYY-MM-DD')
			: '',
		status: filter.value.status,
		type: filter.value.type,
		pageSize: pagination.value.perPage,
		pageNumber: pagination.value.currentPage,
	}
	const result = await getGarageEmployees(_params)
	listItem.value = result.data || []
	pagination.value.total = result.totalElement
}

onMounted(async () => {})
for (let i = 0; i < 3; ++i) {
	statusOptions.value.push({
		label: $t(`module.employee.status.${i}`),
		value: i,
	})
}

for (let i = 1; i < 3; ++i) {
	contractTypeOption.value.push({
		label: $t(`module.employee.contractType.${i}`),
		value: i,
	})
}
await getTableRowData()

const onRefreshData = () => {
	listItem.value = []
	getTableRowData()
}

const changePage = (val: any) => {
	pagination.value.currentPage = val.currentPage
	getTableRowData()
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
</script>
