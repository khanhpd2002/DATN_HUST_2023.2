<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer align="end">
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.garageServiceController.filterConfig.code') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.code.value"
					:placeholder="$t('module.garageServiceController.filterConfig.code')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.garageServiceController.filterConfig.name') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.name.value"
					:placeholder="$t('module.garageServiceController.filterConfig.name')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3 flex gap-4">
				<div class="w-1/2">
					<ACCDButton
						@click="createService"
						size="md"
						type="secondary"
						class="w-full"
					>
						<span class="">{{
							$t('module.garageServiceController.create')
						}}</span>
					</ACCDButton>
				</div>
				<div class="w-1/2">
					<ACCDButton @click="filter" size="md" type="primary" class="w-full">
						<span class="">{{
							$t('module.garageServiceController.find')
						}}</span>
					</ACCDButton>
				</div>
			</div>
		</WrapFlexContainer>
		<div>
			<ACCDTable
				ref="table"
				:columns="columnData"
				:rowData="rowData"
				:pagination="pagination"
				@changePage="changePage"
				class="table-fixed-layout"
			>
				<template #cell-action="{ row, col, field }">
					<div>
						<div class="w-8 cursor-pointer flex justify-center"></div>
						<ACCDAIcon
							v-if="row.garageId"
							@click="
								() => {
									editService(row.id)
								}
							"
							name="Edit"
							class="cursor-pointer"
							size="20"
						></ACCDAIcon>
					</div>
				</template>
			</ACCDTable>
		</div>
		<ServiceForm
			v-if="openModal"
			ref="serviceForm"
			v-model="openModal"
			:state="stateModal"
			:serviceId="editServiceId"
			@refresh="onRefreshData"
		></ServiceForm>
	</div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { getListGarageServiceId } from '../api'
import { EFormState } from '@/enums'
import { IAccessary } from '@/types'
import ServiceForm from './ServiceForm.vue'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import { emitter } from '@/utils/mitt'

const PAGE_SIZE = 10

export default defineComponent({
	async setup() {
		const initListGarageServiceidRes = await getListGarageServiceId({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
		return { initListGarageServiceidRes }
	},
	computed: {
		rowData(): IAccessary[] {
			return (
				this.listItem?.map((item: IAccessary, index: number) => {
					return {
						...item,
						stt: index + 1 + PAGE_SIZE * (this.pagination.currentPage - 1),
					}
				}) || []
			)
		},
	},
	components: {
		ServiceForm,
		WrapFlexContainer,
	},
	watch: {},
	data() {
		return {
			editServiceId: '' as string,
			openModal: false,
			stateModal: '',
			EFormState: EFormState,
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			listItem: [] as any,
			columnData: [
				{
					key: 'stt',
					headerName: this.$t('module.garageServiceController.table.stt'),
					width: '80px',
				},
				{
					key: 'garageServiceType',
					headerName: this.$t(
						'module.garageServiceController.table.garageServiceType'
					),
				},
				{
					key: 'code',
					headerName: this.$t('module.garageServiceController.table.code'),
				},
				{
					key: 'name',
					headerName: this.$t('module.garageServiceController.table.name'),
					maxWidth: '300px',
				},
				{
					key: 'description',
					headerName: this.$t(
						'module.garageServiceController.table.description'
					),
				},
				{
					key: 'action',
					headerName: '',
					width: '80px',
				},
			],
			filterConfig: {
				code: {
					value: '',
				},
				name: {
					value: '',
				},
			} as any,
		}
	},
	created() {
		emitter.on('refresh', () => {
			this.onRefreshData()
		})

		this.listItem = this.initListGarageServiceidRes.data || []
		this.pagination.total = this.initListGarageServiceidRes.totalElement

		// this.getTableRowData({
		//     pageSize: PAGE_SIZE,
		//     pageNumber: 1,
		// });
	},
	methods: {
		editService(id: string) {
			this.editServiceId = id
			this.stateModal = 'edit'
			this.openModal = true
		},
		createService() {
			this.stateModal = 'create'
			this.openModal = true
			// this.$refs.serviceForm.openModal();
		},
		onRefreshData() {
			this.listItem = []
			this.getTableRowData({
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		},

		filter() {
			let data = {
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			} as any
			Object.keys(this.filterConfig).map((field) => {
				if (this.filterConfig[field].value) {
					data[field] = this.filterConfig[field].value
				}
			})
			this.getTableRowData(data)
		},

		changePage(val: any) {
			this.pagination.currentPage = val.currentPage
			this.getTableRowData({
				pageNumber: val.currentPage,
				pageSize: PAGE_SIZE,
			})
		},
		async getTableRowData(params: any) {
			const result = await getListGarageServiceId(params)
			this.listItem = result.data || []
			this.pagination.total = result.totalElement
		},
	},
})
</script>
