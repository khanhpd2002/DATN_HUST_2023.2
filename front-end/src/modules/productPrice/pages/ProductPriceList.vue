<template>
	<div class="flex flex-col gap-4 p-[16px] bg-white rounded">
		<WrapFlexContainer>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.productPrice.productType') }}
				</p>
				<ACCDSelect
					size="md"
					v-model="filterConfig.type.value"
					:options="filterConfig.type.options"
					:placeholder="$t('module.productPrice.productType')"
					class="font-medium"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.productPrice.productCode') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.code.value"
					:placeholder="$t('module.productPrice.productCode')"
					class="font-medium"
					:trim="true"
				/>
			</div>
			<div class="w-full lg:w-1/3">
				<p class="mb-1 font-semibold">
					{{ $t('module.productPrice.productName') }}
				</p>
				<ACCDInputText
					size="md"
					v-model="filterConfig.name.value"
					:placeholder="$t('module.productPrice.productName')"
					class="font-medium"
					:trim="true"
				/>
			</div>
		</WrapFlexContainer>
		<div class="mb-4 flex flex-row-reverse">
			<div>
				<ACCDButton
					@click="filter"
					size="md"
					variant="fill"
					type="primary"
					class="bg-info-secondary border-none w-full"
					>{{ $t('module.productPrice.filter') }}
				</ACCDButton>
			</div>
		</div>
		<div>
			<TableContainer>
				<ACCDTable
					ref="table"
					:columns="columnData"
					:rowData="rowData"
					:tableName="tableName"
					:pagination="pagination"
					@changePage="changePage"
				>
					<template #cell-action="{ row, col, field }">
						<ACCDAIcon
							@click="
								() => {
									editPrice(row.productId, row.garageServiceId, row.type, row)
								}
							"
							class="text-gray-700 cursor-pointer"
							name="Edit"
							size="20"
						></ACCDAIcon>
					</template>
				</ACCDTable>
			</TableContainer>
		</div>
		<ProductPriceForm ref="ProductPriceFormRef" @refresh="onRefreshData" />
	</div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { getListPrices } from '@/modules/productPrice/api'
import ProductPriceForm from '@/modules/productPrice/pages/ProductPriceForm.vue'
import { formatPriceVN } from '@/modules/sharedModules/component/constants'
import { IConfigPrice } from '@/types'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'

const PAGE_SIZE = 10

export default defineComponent({
	components: { ProductPriceForm, WrapFlexContainer, TableContainer },
	async setup() {
		const initListPriceRes = await getListPrices({
			pageSize: PAGE_SIZE,
			pageNumber: 1,
		})
		return { initListPriceRes }
	},
	computed: {
		rowData(): any {
			return (
				this.listPrices?.map((item: any, index: number) => {
					let res: any
					res = {
						...item,
						stt: index + 1 + PAGE_SIZE * (this.pagination.currentPage - 1),
						typeStr:
							item.type == 1
								? this.$t('module.product.product')
								: this.$t('module.product.service'),
						code: item.type == 1 ? item.productCode : item.garageServiceCode,
						name: item.type == 1 ? item.productName : item.garageServiceName,
					}
					for (let i = 0; i < this.customerTypeList.length; i++) {
						res[this.customerTypeList[i]] = formatPriceVN(
							item.configPrices[i]?.price
						)
					}
					return res
				}) || []
			)
		},
	},
	data() {
		return {
			customerTypeList: [] as string[],
			filterConfig: {
				type: {
					value: '',
					options: [
						{
							value: 1,
							label: this.$t('module.productPrice.productTypeOptions.1'),
						},
						{
							value: 2,
							label: this.$t('module.productPrice.productTypeOptions.2'),
						},
					],
				},
				code: {
					value: '',
				},
				name: {
					value: '',
				},
			},
			pagination: {
				perPage: PAGE_SIZE,
				total: 0,
				currentPage: 1,
			},
			tableName: this.$t('module.productPrice.list'),
			listPrices: [] as any,
			columnData: [
				{
					key: 'stt',
					headerName: this.$t('module.productPrice.table.stt'),
					minWidth: '80px',
				},
				{
					key: 'typeStr',
					headerName: this.$t('module.productPrice.table.productType'),
					minWidth: '200px',
				},
				{
					key: 'code',
					headerName: this.$t('module.productPrice.table.productCode'),
					minWidth: '200px',
				},
				{
					key: 'name',
					headerName: this.$t('module.productPrice.table.productName'),
					minWidth: '250px',
				},
			],
		}
	},
	created() {
		// this.getTableRowData({
		//     pageSize: PAGE_SIZE,
		//     pageNumber: 1,
		// });
		this.handleTableRowData(this.initListPriceRes)
	},
	methods: {
		editPrice(
			productId: number,
			garageServiceId: number,
			type: number,
			params: IConfigPrice
		) {
			;(
				this.$refs.ProductPriceFormRef as InstanceType<typeof ProductPriceForm>
			).openDialog(productId, garageServiceId, type, params)
		},
		onRefreshData() {
			this.getTableRowData({
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			})
		},
		changePage(val: any) {
			this.pagination.currentPage = val.currentPage
			this.getTableRowData({
				pageNumber: val.currentPage,
				pageSize: PAGE_SIZE,
			})
		},
		filter() {
			let data = {
				pageSize: PAGE_SIZE,
				pageNumber: 1,
			} as any
			Object.keys(this.filterConfig).map((field) => {
				if (this.filterConfig[field as keyof typeof this.filterConfig].value) {
					data[field] =
						this.filterConfig[field as keyof typeof this.filterConfig].value
				}
			})
			this.getTableRowData(data)
		},
		handleTableRowData(result: any) {
			this.listPrices = result.data || []
			this.pagination.total = result.totalElement
			let detailPrices = this.listPrices[0].configPrices
			for (let i = 0; i < detailPrices.length; i++) {
				if (!this.customerTypeList.includes(detailPrices[i].customerTypeName)) {
					this.columnData.push({
						key: detailPrices[i].customerTypeName,
						headerName:
							this.$t('module.productPrice.table.price') +
							' ' +
							detailPrices[i].customerTypeName,
					})
					this.customerTypeList.push(detailPrices[i].customerTypeName)
				}
			}
			let count = 0
			for (const element of this.columnData) {
				if (element.key == 'action') {
					count++
				}
			}
			if (!count) {
				this.columnData.push({
					key: 'action',
					headerName: '',
				})
			}
		},
		async getTableRowData(params: any) {
			const result = await getListPrices(params)
			this.handleTableRowData(result)
		},
	},
})
</script>
