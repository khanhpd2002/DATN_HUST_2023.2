<template>
	<div
		class="bg-layout-primary rounded-md shadow-lg fixed top-0 left-0 transform z-30 w-full h-full overflow-y-scroll"
	>
		<div class="bg-suggest h-full">
			<body class="invoice-gray">
				<div class="suggest">
					<div class="history-container">
						<div class="invoice-content-wrap light-invoice-content-wrap">
							<div class="WordSection1" id="download_section">
								<div>
									<p class="font-bold">{{ garageName }}</p>
									<p class="font-bold">
										{{ $t('module.sharedModules.print.suggestPrice.address') }}:
										{{ garageAddress }}
									</p>
									<p class="font-bold">
										{{ $t('module.sharedModules.print.suggestPrice.phone') }}:
										{{ garagePhone }}
									</p>
								</div>
								<div>
									<p class="font-bold text-[24px] text-center my-8">
										{{
											$t('module.sharedModules.print.suggestPrice.tableProduct')
										}}
									</p>
									<table>
										<tr>
											<th>
												{{
													$t(
														'module.sharedModules.print.suggestPrice.productName'
													)
												}}
											</th>
											<th v-if="!isShow">
												{{
													$t(
														'module.sharedModules.print.suggestPrice.productType'
													)
												}}
											</th>
											<th>
												{{ $t('module.sharedModules.print.suggestPrice.unit') }}
											</th>
											<th>
												{{
													$t('module.sharedModules.print.suggestPrice.price')
												}}
											</th>
										</tr>
										<tr v-for="item in list.filter((item) => item.isChecked)">
											<td>{{ item.productName }}</td>

											<td v-if="!isShow">{{ item.segmentName }}</td>
											<td>{{ item.unit }}</td>
											<td>{{ formatPriceVN(item.exportPrice || 0) }}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
						<section class="d-print-none" id="agency_bottom">
							<div class="container">
								<div class="invo-buttons-wrap">
									<div class="invo-print-btn invo-btns">
										<ACCDButton type="primary" @click="printHtml"
											>{{ $t('module.sharedModules.print.suggestPrice.print') }}
										</ACCDButton>
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
import { onMounted, ref } from 'vue'
import { IListProductVariant, IProductVariants } from '@/types/SuggestPrice'
import { formatPriceVN } from '../../component/constants'
import { flatten } from 'lodash'
import html2pdf from 'html2pdf.js'
const suggestPrintData = ref([] as IListProductVariant[])
const list = ref([] as IProductVariants[])
const options = {
	margin: [0, 0, 50, 0], //top, left, buttom, right,
	filename: 'Yêu cầu báo giá.pdf',
	image: { type: 'jpeg', quality: 0.98 },
	html2canvas: { dpi: 192, scale: 1, letterRendering: true },
	pagebreak: { mode: 'avoid-all' },
	jsPDF: { unit: 'pt', format: 'a4', orientation: 'portrait' },
}
const garageName = JSON.parse(localStorage.getItem('garage') as string)
	? JSON.parse(localStorage.getItem('garage') as string).name
	: ''

const garageAddress = JSON.parse(localStorage.getItem('garage') as string)
	? JSON.parse(localStorage.getItem('garage') as string).address
	: ''
const garagePhone = JSON.parse(localStorage.getItem('garageOwner') as string)
	? JSON.parse(localStorage.getItem('garageOwner') as string).phone
	: ''

const isShow = ref(false)
onMounted(() => {
	suggestPrintData.value = JSON.parse(
		localStorage.getItem('suggestPrintData') || '{}'
	)

	const result = suggestPrintData.value.map(
		(item: IListProductVariant) => item.productVariants
	)

	list.value = flatten(result)
	isShow.value = JSON.parse(localStorage.getItem('isShowSegement') || '{}')
})
const printHtml = () => {
	const download_section = document.getElementById('download_section')
	html2pdf().from(download_section).set(options).save()
}
</script>
<style lang="scss" scoped>
@import './css/custom.css';
@import './css/media-query.css';
.history-container {
	max-width: 1000px;
	margin: 0 auto;
}
.WordSection1 {
	padding: 50px;
	color: black;
	height: 100%;
}
.suggest {
	height: 100%;
}
.bg-suggest {
	background: gray;
}
table {
	width: 100%;
	th {
		border: 1px solid;
		padding: 8px;
	}
	td {
		border: 1px solid;
		padding: 8px;
	}
}
</style>
