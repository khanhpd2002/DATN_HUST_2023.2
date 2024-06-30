<template>
	<div
		class="bg-layout-primary rounded-md shadow-lg fixed top-0 left-0 transform z-30 w-full h-full overflow-y-scroll"
	>
		<div class="invoice_wrap h-full">
			<body class="invoice-gray">
				<div class="invoice_wrap">
					<div class="invoice-container">
						<div class="invoice-content-wrap light-invoice-content-wrap">
							<div class="WordSection1" id="download_section_inbound">
								<p class="MsoNormal">
									<span lang="NL" style="line-height: 130%">&nbsp;</span>
								</p>

								<p class="MsoNormal">
									<span lang="NL" style="line-height: 130%">&nbsp;</span>
								</p>

								<table
									class="MsoNormalTable"
									border="0"
									cellspacing="0"
									cellpadding="0"
									style="border-collapse: collapse"
								>
									<tbody>
										<tr>
											<td
												width="198"
												valign="top"
												style="width: 148.4pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													style="margin-bottom: 0in; line-height: 130%"
												>
													<span lang="NL" style="line-height: 130%"
														>&nbsp;</span
													>
												</p>
											</td>
											<td
												width="269"
												valign="top"
												style="width: 201.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span
															lang="NL"
															style="font-size: 13pt; line-height: 130%"
															>{{
																typePrint === 1
																	? 'PHIẾU NHẬP KHO'
																	: 'PHIẾU XUẤT KHO'
															}}</span
														></b
													><span
														lang="NL"
														style="font-size: 13pt; line-height: 130%"
													>
													</span>
												</p>
											</td>
										</tr>
										<tr>
											<td
												width="198"
												valign="top"
												style="width: 148.4pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													style="margin-bottom: 0in; line-height: 130%"
												>
													<span lang="NL" style="line-height: 130%"
														>&nbsp;</span
													>
												</p>
											</td>
											<td
												width="269"
												valign="top"
												style="width: 201.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>Ngày
															{{
																dayjs(dataPrint?.createdAt, 'DD/MM/YY').date()
															}}
															tháng
															{{
																dayjs(
																	dataPrint?.createdAt,
																	'DD/MM/YY'
																).month() + 1
															}}
															năm
															{{
																dayjs(dataPrint?.createdAt, 'DD/MM/YY').year()
															}}
														</span></i
													>
												</p>
											</td>
										</tr>
										<tr>
											<td
												width="198"
												valign="top"
												style="width: 148.4pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													style="margin-bottom: 0in; line-height: 130%"
												>
													<span lang="NL" style="line-height: 130%"
														>&nbsp;</span
													>
												</p>
											</td>
											<td
												width="269"
												valign="top"
												style="width: 201.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													style="margin-bottom: 0in; line-height: 130%"
												>
													<span lang="NL" style="line-height: 130%"
														>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														Số: ...................................</span
													>
												</p>
											</td>
										</tr>
									</tbody>
								</table>

								<p
									class="MsoNormal"
									style="
										margin-top: 0in;
										margin-right: 0in;
										margin-bottom: 0in;
										margin-left: 0.5in;
										line-height: 130%;
									"
								>
									<span lang="NL" style="line-height: 130%">&nbsp;</span>
								</p>

								<p
									class="mb-3"
									style="
										margin-bottom: 0in;
										text-indent: 0.5in;
										line-height: 130%;
									"
								>
									<span lang="NL" style="line-height: 130%"
										>- Loại hình
										{{ typePrint == 1 ? 'nhập' : 'xuất' }}
										kho:

										<span v-if="typePrint == 1">
											{{
												typePrint == 1
													? $t('module.inboundTicket.type.1')
													: $t('module.inboundTicket.type.2')
											}}
										</span>
										<span v-else>
											{{
												typePrint == 1
													? $t('module.outboundTicket.type.1')
													: $t('module.outboundTicket.type.2')
											}}
										</span>
									</span>
								</p>

								<p
									class="mb-5"
									style="
										margin-bottom: 0in;
										text-indent: 0.5in;
										line-height: 130%;
									"
								>
									<span lang="NL" style="line-height: 130%"
										>- Số chứng từ: {{ dataPrint?.code }}
									</span>
								</p>

								<div align="center">
									<table
										class="MsoNormalTable"
										border="1"
										cellspacing="0"
										cellpadding="0"
										width="92%"
										style="
											width: 92.66%;
											border-collapse: collapse;
											border: none;
										"
									>
										<tbody>
											<tr style="page-break-inside: avoid">
												<td
													width="7%"
													rowspan="3"
													style="
														width: 7.56%;
														border: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>STT</span
															></b
														>
													</p>
												</td>
												<td
													width="17%"
													rowspan="3"
													style="
														width: 17.08%;
														border: solid windowtext 1pt;
														border-left: none;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span
																lang="NL"
																style="font-size: 11pt; line-height: 130%"
																>Mã phụ tùng</span
															></b
														>
													</p>
												</td>
												<td
													width="23%"
													style="
														width: 23.34%;
														border-top: solid windowtext 1pt;
														border-left: none;
														border-bottom: none;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>&nbsp;</span
															></b
														>
													</p>
												</td>
												<td
													width="8%"
													rowspan="3"
													style="
														width: 8.16%;
														border: solid windowtext 1pt;
														border-left: none;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="margin-bottom: 0in; text-align: center"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>ĐVT</span
															></b
														>
													</p>
												</td>
												<td
													width="30%"
													colspan="2"
													style="
														width: 30.46%;
														border: solid windowtext 1pt;
														border-left: none;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>Số lượng</span
															></b
														>
													</p>
												</td>
												<td
													width="13%"
													rowspan="3"
													style="
														width: 13.4%;
														border: solid windowtext 1pt;
														border-left: none;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>Ghi chú</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr style="page-break-inside: avoid">
												<td
													width="23%"
													style="
														width: 23.34%;
														border: none;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>Tên phụ tùng</span
															></b
														>
													</p>
												</td>
												<td
													width="15%"
													rowspan="2"
													style="
														width: 15.22%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>Yêu cầu</span
															></b
														>
													</p>
												</td>
												<td
													width="15%"
													rowspan="2"
													style="
														width: 15.24%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>Thực
																{{ typePrint === 1 ? 'nhập' : 'xuất' }}</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr style="page-break-inside: avoid">
												<td
													width="23%"
													style="
														width: 23.34%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: 130%;
														"
													>
														<b
															><span lang="NL" style="line-height: 130%"
																>&nbsp;</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr
												style="page-break-inside: avoid"
												v-for="(item, i) in dataPrint?.inboundProducts ||
												dataPrint?.outboundProducts"
												:key="i"
											>
												<td
													width="7%"
													valign="top"
													style="
														width: 7.56%;
														border: solid windowtext 1pt;
														border-top: none;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														<span lang="NL" style="line-height: 130%"
															>&nbsp;</span
														>

														{{ i + 1 }}
													</p>
												</td>
												<td
													width="17%"
													valign="top"
													style="
														width: 17.08%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														{{
															productServiceId?.find(
																(el) => el.value == item.productId
															)?.rawValue
														}}
													</p>
												</td>
												<td
													width="23%"
													valign="top"
													style="
														width: 23.34%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														<span lang="NL" style="line-height: 130%"
															>&nbsp;</span
														>
														{{
															productServiceId?.find(
																(el) => el.value == item.productId
															)?.label
														}}
													</p>
												</td>
												<td
													width="8%"
													valign="top"
													style="
														width: 8.16%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														{{ item?.unit }}
													</p>
												</td>
												<td
													width="15%"
													valign="top"
													style="
														width: 15.22%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														<span lang="NL" style="line-height: 130%"
															>&nbsp;</span
														>
														{{ item.requestQuantity }}
													</p>
												</td>
												<td
													width="15%"
													valign="top"
													style="
														width: 15.24%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														<span lang="NL" style="line-height: 130%"
															>&nbsp;</span
														>
														{{ item.exportQuantity }}
													</p>
												</td>
												<td
													width="13%"
													valign="top"
													style="
														width: 13.4%;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 5px;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 130%"
													>
														<span lang="NL" style="line-height: 130%"
															>&nbsp;</span
														>

														{{ item.note }}
													</p>
												</td>
											</tr>
										</tbody>
									</table>
								</div>

								<p
									class="MsoNormal"
									style="
										margin-bottom: 0in;
										text-indent: 0.5in;
										line-height: 130%;
									"
								>
									<span lang="NL" style="line-height: 130%">&nbsp;</span>
								</p>

								<table
									class="MsoNormalTable"
									border="0"
									cellspacing="0"
									cellpadding="0"
									width="100%"
									style="margin-left: 31.4pt; border-collapse: collapse"
								>
									<tbody>
										<tr>
											<td
												width="95"
												valign="top"
												style="width: 71.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>Người lập
														</span></b
													>
												</p>
											</td>
											<td
												width="108"
												valign="top"
												style="width: 80.7pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>Người nhận
														</span></b
													>
												</p>
											</td>
											<td
												width="95"
												valign="top"
												style="width: 71.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>Thủ kho</span
														></b
													>
												</p>
											</td>
											<td
												width="130"
												valign="top"
												style="width: 97.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>Kế toán trưởng
														</span></b
													>
												</p>
											</td>
											<td
												width="173"
												valign="top"
												style="width: 130pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>Giám đốc
														</span></b
													>
												</p>
											</td>
										</tr>
										<tr>
											<td
												width="95"
												valign="top"
												style="width: 71.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>phiếu
														</span></b
													>
												</p>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>(Ký, họ tên)</span
														></i
													>
												</p>
											</td>
											<td
												width="108"
												valign="top"
												style="width: 80.7pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<b
														><span lang="NL" style="line-height: 130%"
															>hàng
														</span></b
													>
												</p>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>(Ký, họ tên)</span
														></i
													>
												</p>
											</td>
											<td
												width="95"
												valign="top"
												style="width: 71.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>(Ký, họ tên)</span
														></i
													>
												</p>
											</td>
											<td
												width="130"
												valign="top"
												style="width: 97.5pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<span lang="NL" style="line-height: 130%"
														>(Hoặc bộ phận có nhu cầu nhập)</span
													>
												</p>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>(Ký, họ tên)</span
														></i
													>
												</p>
											</td>
											<td
												width="173"
												valign="top"
												style="width: 130pt; padding: 0in 5.4pt 0in 5.4pt"
											>
												<p
													class="MsoNormal"
													align="center"
													style="
														margin-bottom: 0in;
														text-align: center;
														line-height: 130%;
													"
												>
													<i
														><span lang="NL" style="line-height: 130%"
															>(Ký, họ tên)</span
														></i
													>
												</p>
											</td>
										</tr>
									</tbody>
								</table>

								<p class="MsoNormal" style="line-height: 130%">&nbsp;</p>
							</div>
						</div>
						<section
							class="agency-bottom-content d-print-none"
							id="agency_bottom"
						>
							<div class="container">
								<div class="invo-buttons-wrap">
									<div class="invo-print-btn invo-btns">
										<ACCDButton
											type="primary"
											@click="() => printHtml('download_section_inbound')"
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
<script setup lang="ts">
import { onMounted, watch, ref, onUnmounted } from 'vue'
import html2pdf from 'html2pdf.js'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
dayjs.extend(customParseFormat)
type InboundProps = {
	id: number
	code: string
	type: null
	distributorId: number
	ticketId: number
	products: string
	note: string
	status: number
	createdAt: string
	inboundProducts?: []
	outboundProducts?: []
}

type ProductProps = {
	value: number
	label: string
}
const options = {
	margin: [0, -0.1, 0, 0], //top, left, buttom, right,
	filename: 'my_file.pdf',
	image: { type: 'jpeg', quality: 0.98 },
	// html2canvas: { dpi: 192, scale: 2, letterRendering: true },
	html2canvas: { scale: 4, useCORS: true, dpi: 192, letterRendering: true },
	pagebreak: { mode: 'avoid-all' },
	// jsPDF: { unit: "pt", format: "a4", orientation: "portrait" },
	jsPDF: {
		unit: 'in',
		format: 'a4',
		orientation: 'portrait',
		putTotalPages: true,
	},
}

const printHtml = (divName: string) => {
	let printContents = document.getElementById(divName).innerHTML
	let originalContents = document.body.innerHTML
	document.body.innerHTML = printContents
	window.print()
	document.body.innerHTML = originalContents
	window.location.reload()
}

const dataPrint = ref<InboundProps | null>(null)
const productServiceId = ref<ProductProps[] | null>(null)
const typePrint = ref(1)

onMounted(() => {
	typePrint.value = JSON.parse(localStorage.getItem('typePrint') || '{}')

	productServiceId.value = JSON.parse(
		localStorage.getItem('productServiceId') || '{}'
	)

	if (typePrint.value == 1) {
		dataPrint.value = JSON.parse(
			localStorage.getItem('inboudTicketPrint') || '{}'
		)
	} else {
		dataPrint.value = JSON.parse(
			localStorage.getItem('outboudTicketPrint') || '{}'
		)
	}
})
</script>
<style scoped>
@import './css/custom.css';
@import './css/media-query.css';
.WordSection1 {
	padding: 50px;
	color: black;
}

@media print {
	#download_section_inbound {
		width: 100%;
		height: 100%;
	}
	@page {
		size: B5;
		margin: 0;
	}
}
</style>
