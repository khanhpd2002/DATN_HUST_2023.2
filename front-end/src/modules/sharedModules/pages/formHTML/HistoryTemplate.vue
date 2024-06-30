<template>
	<div
		class="bg-layout-primary rounded-md shadow-lg fixed top-0 left-0 transform z-30 w-full h-full overflow-y-scroll"
	>
		<div class="invoice_wrap h-full">
			<body class="invoice-gray">
				<div class="invoice_wrap">
					<div class="history-container">
						<div class="invoice-content-wrap light-invoice-content-wrap">
							<div class="WordSection1" id="download_section">
								<p class="font-bold text-xxl text-center">BIÊN BẢN KIỂM KÊ</p>

								<p class="MsoBodyText">
									<b><span lang="vi">&nbsp;</span></b>
								</p>

								<p class="MsoBodyText" style="margin-top: 7pt">
									<b><span lang="vi">&nbsp;</span></b>
								</p>

								<p
									class="MsoBodyText"
									style="
										margin-top: 0.05pt;
										margin-right: 0in;
										margin-bottom: 0in;
										margin-left: 279.75pt;
										margin-bottom: 0.0001pt;
									"
								>
									<span lang="vi"
										>Ngày<span style="letter-spacing: -0.2pt"> </span>
										{{ dataPrint?.countDate.split('/')[0] }} tháng
										{{ dataPrint?.countDate.split('/')[1] }}
										<span style="letter-spacing: -0.1pt"
											>năm

											{{ dataPrint?.countDate.split('/')[2] }}
										</span></span
									>
								</p>

								<p
									class="MsoBodyText"
									style="
										margin-top: 0.15in;
										margin-right: 0in;
										margin-bottom: 0in;
										margin-left: 5pt;
										margin-bottom: 0.0001pt;
									"
								>
									Người thực hiện kiểm kê:
								</p>

								<p class="mx-5" v-for="emloyee in dataPrint?.counterName">
									- Ông/Bà: {{ emloyee?.counterName }}
								</p>

								<p class="MsoBodyText"><span lang="vi">&nbsp;</span></p>

								<p class="MsoBodyText" style="margin-left: 5pt">
									Đã kiểm kê vật tư phụ tùng, kết quả như sau:
								</p>

								<p class="MsoBodyText" style="margin-top: 0.5pt">
									<span lang="vi" style="font-size: 9pt">&nbsp;</span>
								</p>
								<!-- <table
									class="MsoNormalTable"
									border="1"
									cellspacing="0"
									cellpadding="0"
									style="
										margin-left: 5.5pt;
										border-collapse: collapse;
										border: none;
										width: 100%;
									"
								>
									<tbody>
										<tr style="height: 16.5pt">
											<td
												width="105"
												rowspan="2"
												valign="top"
												style="
													width: 78.95pt;
													border: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													align="center"
													style="
														margin-left: 0.45pt;
														text-align: center;
														line-height: 13.75pt;
													"
												>
													Vật tư phụ tùng
												</p>
											</td>
											<td
												width="47"
												rowspan="2"
												valign="top"
												style="
													width: 35.5pt;
													border: solid black 1pt;
													border-left: none;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 5.35pt; line-height: 13.75pt"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>ĐVT</span
													>
												</p>
											</td>
											<td
												width="63"
												rowspan="2"
												valign="top"
												style="
													width: 47.3pt;
													border: solid black 1pt;
													border-left: none;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													align="center"
													style="
														margin-left: 0.65pt;
														text-align: center;
														line-height: 13.75pt;
													"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>Đơn</span
													>
												</p>
												<p
													class="TableParagraph"
													align="center"
													style="
														margin-top: 2.75pt;
														margin-right: 0.05pt;
														margin-bottom: 0in;
														margin-left: 0.65pt;
														margin-bottom: 0.0001pt;
														text-align: center;
													"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>giá</span
													>
												</p>
											</td>
											<td
												width="137"
												colspan="2"
												valign="top"
												style="
													width: 102.6pt;
													border: solid black 1pt;
													border-left: none;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 16.55pt; line-height: 13.75pt"
												>
													<span lang="vi" style="font-size: 12pt"
														>Theo<span style="letter-spacing: -0.1pt"> </span
														>hệ<span style="letter-spacing: -0.1pt">
															thống</span
														></span
													>
												</p>
											</td>
											<td
												width="135"
												colspan="2"
												valign="top"
												style="
													width: 101.5pt;
													border: solid black 1pt;
													border-left: none;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 0.45in; line-height: 13.75pt"
												>
													Thực tế
												</p>
											</td>
											<td
												width="136"
												colspan="2"
												valign="top"
												style="
													width: 101.65pt;
													border: solid black 1pt;
													border-left: none;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 23.7pt; line-height: 13.75pt"
												>
													Chênh lệch
												</p>
											</td>
										</tr>
										<tr style="height: 16.5pt">
											<td
												width="48"
												valign="top"
												style="
													width: 0.5in;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 10.9pt; line-height: 13.75pt"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>SL</span
													>
												</p>
											</td>
											<td
												width="89"
												valign="top"
												style="
													width: 66.6pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 7.45pt; line-height: 13.75pt"
												>
													<span lang="vi" style="font-size: 12pt"
														>Thành<span style="letter-spacing: -0.2pt">
															tiền</span
														></span
													>
												</p>
											</td>
											<td
												width="43"
												valign="top"
												style="
													width: 0.45in;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 9.15pt; line-height: 13.75pt"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>SL</span
													>
												</p>
											</td>
											<td
												width="92"
												valign="top"
												style="
													width: 69.1pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 8.8pt; line-height: 13.75pt"
												>
													<span lang="vi" style="font-size: 12pt"
														>Thành<span style="letter-spacing: -0.2pt">
															tiền</span
														></span
													>
												</p>
											</td>
											<td
												width="52"
												valign="top"
												style="
													width: 38.9pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 12.45pt; line-height: 13.75pt"
												>
													<span
														lang="vi"
														style="font-size: 12pt; letter-spacing: -0.25pt"
														>SL</span
													>
												</p>
											</td>
											<td
												width="84"
												valign="top"
												style="
													width: 62.75pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.5pt;
												"
											>
												<p
													class="TableParagraph"
													style="margin-left: 5.55pt; line-height: 13.75pt"
												>
													<span lang="vi" style="font-size: 12pt"
														>Thành<span style="letter-spacing: -0.2pt">
															tiền</span
														></span
													>
												</p>
											</td>
										</tr>
										<tr
											style="height: 16.65pt"
											v-for="(
												product, index
											) in dataPrint?.computedInventoryProductRow"
											:key="index"
										>
											<td
												width="105"
												valign="top"
												style="
													width: 78.95pt;
													border: solid black 1pt;
													border-top: none;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.productName }}
												</p>
											</td>
											<td
												width="47"
												valign="top"
												style="
													width: 35.5pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.unit }}
												</p>
											</td>
											<td
												width="63"
												valign="top"
												style="
													width: 47.3pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.systemInventory }}
												</p>
											</td>
											<td
												width="48"
												valign="top"
												style="
													width: 0.5in;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.priceBySystemInventory }}
												</p>
											</td>
											<td
												width="89"
												valign="top"
												style="
													width: 66.6pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.realityInventory }}
												</p>
											</td>
											<td
												width="43"
												valign="top"
												style="
													width: 0.45in;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.priceByRealityInventory }}
												</p>
											</td>
											<td
												width="92"
												valign="top"
												style="
													width: 69.1pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.unit }}
												</p>
											</td>
											<td
												width="52"
												valign="top"
												style="
													width: 38.9pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.differenceInventory }}
												</p>
											</td>
											<td
												width="84"
												valign="top"
												style="
													width: 62.75pt;
													border-top: none;
													border-left: none;
													border-bottom: solid black 1pt;
													border-right: solid black 1pt;
													padding: 0in 0in 0in 0in;
													height: 16.65pt;
												"
											>
												<p class="p-3">
													{{ product.priceByDifferenceInventory }}
												</p>
											</td>
										</tr>
									</tbody>
								</table> -->

								<ACCDTable
									:columns="inventoryProductColumn"
									:rowData="computedInventoryProductRow"
									:show-summary="true"
								>
									<template #cell-productId="{ row }">
										<span>
											{{
												listGarageProducts.find(
													(item) => item.value == row.productId
												)?.label
											}}
										</span>
									</template>
									<template #cell-unitPrice="{ row }">
										{{ formatPriceVN(row.systemUnitPrice) }}
									</template>
									<template #cell-priceBySystemInventory="{ row }">
										{{
											formatPriceVN(row.systemUnitPrice * row.systemInventory)
										}}
									</template>
									<template #cell-realityInventory="{ row, rowIndex }">
										<div>
											{{ row.realityInventory }}
										</div>
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

								<p class="MsoBodyText" style="margin-top: 10.9pt">
									<span lang="vi" style="font-size: 10pt">&nbsp;</span>
								</p>

								<table
									class="MsoNormalTable"
									border="0"
									cellspacing="0"
									cellpadding="0"
									style="
										margin-left: 87.85pt;
										border-collapse: collapse;
										width: 100%;
									"
								>
									<tbody>
										<tr style="height: 13.25pt">
											<td
												width="166"
												valign="top"
												style="
													width: 124.65pt;
													padding: 0in 0in 0in 0in;
													height: 13.25pt;
												"
											>
												<p
													class="font-bold"
													style="margin-left: 2.5pt; line-height: 12.3pt"
												>
													GIÁM ĐỐC
												</p>
											</td>
											<td
												width="308"
												valign="top"
												style="
													width: 231.25pt;
													padding: 0in 0in 0in 0in;
													height: 13.25pt;
												"
											>
												<p
													class="font-bold"
													style="margin-left: 58.5pt; line-height: 12.3pt"
												>
													NGƯỜI THỰC HIỆN KIỂM KÊ
												</p>
											</td>
										</tr>
									</tbody>
								</table>

								<p class="MsoNormal" style="padding-bottom: 100px">
									<span lang="vi">&nbsp;</span>
								</p>
							</div>
						</div>

						<section
							class="agency-bottom-content d-print-none"
							id="agency_bottom"
						>
							<div class="container">
								<div class="invo-buttons-wrap">
									<div class="invo-print-btn invo-btns">
										<ACCDButton type="primary" @click="printHtml"
											>Print</ACCDButton
										>
									</div>
								</div>
							</div>
						</section>
					</div>
				</div>
			</body>
		</div>
	</div>
</template>
<script lang="ts" setup>
import { formatPriceVN } from '@/modules/debtControlManagement/components/constants'
import { dayjs } from 'element-plus'
import html2pdf from 'html2pdf.js'
import { computed, onMounted, ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { ISelectOption } from '@/types'
import { getProductForInventoryHistory } from '@/modules/inventory/api'

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
const { $t } = useI18n()

const dataPrint = ref()
const inventoryProductRow = ref([] as InventoryProductRow[])
// const options = {
// 	margin: [0, 0, 0, 0],
// 	filename: 'My_File.pdf',
// 	image: { type: 'jpeg', quality: 0.98 },
// 	html2canvas: { scale: 2, scrollX: 0, scrollY: 0 },
// 	jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' },
// 	pagebreak: {
// 		mode: ['avoid-all', 'css', 'legacy'],
// 	},
// }

const options = {
	margin: [0, 0, 0, 0],
	filename: 'My_file.pdf',
	image: { type: 'jpeg', quality: 1 },
	html2canvas: { dpi: 192, letterRendering: true, scale: 1 },
	jsPDF: { unit: 'mm', format: 'letter', orientation: 'landscape' },
}

const inventoryProductColumn = ref([
	{
		key: 'productId',
		headerName: $t(
			'module.inventory.inventoryHistory.form.inventoryProductColumn.productId'
		),
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

		children: [
			{
				key: 'realityInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.realityInventory'
				),
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
			},
			{
				key: 'priceByDifferenceInventory',
				headerName: $t(
					'module.inventory.inventoryHistory.form.inventoryProductColumn.priceByDifferenceInventory'
				),
			},
		],
	},
])

const computedInventoryProductRow = computed(() => {
	return inventoryProductRow.value.map((row) => {
		return {
			...row,
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
const listGarageProducts = ref([] as (ISelectOption & { origin: any })[])

const printHtml = () => {
	const download_section = document.getElementById('download_section')
	html2pdf().from(download_section).set(options).save()
}

onMounted(() => {
	getProductList()
	dataPrint.value = JSON.parse(localStorage.getItem('historyDataPrint') || '{}')
	inventoryProductRow.value = JSON.parse(
		localStorage.getItem('inventoryProductRow') || '{}'
	)

	console.log('dataPrint.value', dataPrint.value)
	console.log('inventoryProductRow.value', inventoryProductRow.value)
})

const getProductList = async () => {
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
}
</script>

<style scoped>
@import './css/custom.css';
@import './css/media-query.css';
.WordSection1 {
	padding: 50px;
	color: black;
}
.history-container {
	max-width: 1000px;
	margin: 0 auto;
}

body {
	line-height: 1.5;
}
.cd-table {
	font-size: 12px;
}
</style>
