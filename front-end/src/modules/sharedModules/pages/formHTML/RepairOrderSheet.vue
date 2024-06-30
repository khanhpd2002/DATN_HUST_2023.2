<template>
	<div
		class="bg-layout-primary rounded-md shadow-lg fixed top-0 left-0 transform z-30 w-full h-full overflow-y-scroll"
	>
		<div class="invoice_wrap">
			<body class="invoice-gray">
				<div class="invoice_wrap">
					<div class="invoice-container">
						<div
							class="invoice-content-wrap light-invoice-content-wrap"
							id="printableAreaRepairOrder"
						>
							<div class="WordSection1">
								<div class="flex gap-6">
									<div>
										<img
											v-if="dataPrint?.garageAvatar"
											:src="baseImg + dataPrint?.garageAvatar"
											alt="logo"
											class="w-[150px] h-[150px] object-cover"
										/>
									</div>

									<div class="flex-1">
										<div class="flex gap-2">
											<span class="font-bold">{{ dataPrint?.garageName }}</span>
										</div>

										<div class="flex gap-2">
											<span class="font-bold w-[120px]">
												{{
													$t('module.sharedModules.print.quotation.address')
												}}:</span
											>
											<span>{{ dataPrint?.garageAddress }}</span>
										</div>
										<div class="flex gap-2">
											<div class="font-bold w-[120px]">
												{{
													$t('module.sharedModules.print.quotation.bankNumber')
												}}:
											</div>
											<div v-if="dataPrint?.accountInfo?.banks">
												<div
													v-for="(bank, index) in dataPrint?.accountInfo?.banks"
													:key="index"
												>
													<p>
														{{ bank.owner }} - {{ bank.bankNumber }} -
														{{ bank.bankName }}
													</p>
												</div>
											</div>
										</div>
										<div class="flex gap-2">
											<div class="font-bold w-[120px]">
												{{
													$t('module.sharedModules.print.quotation.contact')
												}}:
											</div>
											<div v-if="dataPrint?.accountInfo?.contacts">
												<div
													v-for="(contact, index) in dataPrint?.accountInfo
														?.contacts"
													:key="index"
												>
													<p>{{ contact.name }} - {{ contact.phone }}</p>
												</div>
											</div>
										</div>

										<div class="flex gap-2">
											<div class="flex gap-2 w-1/2">
												<p class="font-bold w-[120px]">
													{{
														$t('module.sharedModules.print.quotation.taxCode')
													}}:
												</p>
												<p>
													{{ dataPrint?.accountInfo?.taxCode }}
												</p>
											</div>
											<div class="flex gap-2 w-1/2">
												<p class="font-bold">
													{{
														$t('module.sharedModules.print.quotation.email')
													}}:
												</p>
												<p>{{ dataPrint?.accountInfo?.email }}</p>
											</div>
										</div>
									</div>
								</div>

								<p class="mt-[30px]" style="text-align: center">
									<b
										><span
											style="
												font-size: 14pt;
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>{{
												$t('module.sharedModules.print.repaireOrder.title')
											}}</span
										></b
									>
								</p>

								<p class="text-center italic">
									Ngày {{ dayjs().date() }} tháng {{ dayjs().month() + 1 }} năm
									{{ dayjs().year() }}
								</p>

								<p class="MsoNormal" align="center" style="text-align: center">
									<b
										><span
											style="
												font-size: 14pt;
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>&nbsp;</span
										></b
									>
								</p>

								<p class="flex justify-end mb-3">
									<b>
										<span
											style="
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>{{
												$t(
													'module.sharedModules.print.repaireOrder.ticketCode'
												)
											}}: {{ ticketCode.replace('DV', 'SC') }}</span
										>
									</b>
								</p>

								<table class="table-custome page-break">
									<thead>
										<tr>
											<th class="w-[40%]">
												{{
													$t(
														'module.sharedModules.print.quotation.customerInfo'
													)
												}}
											</th>
											<th colspan="4" class="w-[40%]">
												{{ $t('module.sharedModules.print.quotation.carInfo') }}
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td rowspan="6" class="vertical-align-top">
												<p class="ml-2">
													{{
														$t(
															'module.sharedModules.print.quotation.customerName'
														)
													}}: {{ dataPrint?.customerName }}
												</p>
												<p class="ml-2">
													{{
														$t('module.sharedModules.print.quotation.address')
													}}:
													{{ dataPrint?.address }}
												</p>
												<p class="ml-2">
													{{
														$t(
															'module.sharedModules.print.quotation.phoneNumber'
														)
													}}
													:{{ dataPrint?.phone }}
												</p>
											</td>
											<td class="text-center font-semibold">
												{{
													$t('module.sharedModules.print.quotation.carBrand')
												}}
											</td>
											<td class="text-center font-semibold">
												{{ $t('module.sharedModules.print.quotation.carMode') }}
											</td>
											<td class="text-center font-semibold">
												{{ $t('module.sharedModules.print.quotation.carYear') }}
											</td>
											<td class="text-center font-semibold">
												{{
													$t('module.sharedModules.print.quotation.carVersion')
												}}
											</td>
										</tr>

										<tr>
											<td class="text-center">
												{{ dataPrint?.carBrandId }}
											</td>
											<td class="text-center" v-if="dataPrint?.carModelId">
												{{ dataPrint?.carModelId }}
											</td>
											<td class="text-center invisible" v-else>a</td>
											<td class="text-center">{{ dataPrint?.carYearId }}</td>
											<td class="text-center">{{ dataPrint?.carVersionId }}</td>
										</tr>

										<tr>
											<td colspan="2" class="text-center font-semibold">
												{{
													$t('module.sharedModules.print.quotation.vinNumber')
												}}
											</td>
											<td colspan="2" class="text-center font-semibold">
												{{
													$t('module.sharedModules.print.quotation.otherCar')
												}}
											</td>
										</tr>

										<tr>
											<td
												colspan="2"
												class="text-center"
												v-if="dataPrint?.vinNumber"
											>
												{{ dataPrint?.vinNumber }}
											</td>
											<td colspan="2" class="text-center invisible" v-else>
												a
											</td>
											<td
												colspan="2"
												class="text-center"
												v-if="dataPrint?.otherCar"
											>
												{{ dataPrint?.otherCar }}
											</td>
											<td colspan="2" class="text-center invisible" v-else>
												a
											</td>
										</tr>

										<tr>
											<td class="text-center font-semibold">
												{{
													$t(
														'module.sharedModules.print.quotation.licensePlate'
													)
												}}
											</td>
											<td class="text-center font-semibold">
												{{ $t('module.sharedModules.print.quotation.odo') }}
											</td>
											<td class="text-center font-semibold">
												{{
													$t(
														'module.sharedModules.print.quotation.receiveCarDate'
													)
												}}
											</td>
											<td class="text-center font-semibold">
												{{
													$t(
														'module.sharedModules.print.quotation.expectedHandoverDate'
													)
												}}
											</td>
										</tr>

										<tr>
											<td class="text-center">{{ dataPrint?.licensePlate }}</td>
											<td class="text-center">{{ dataPrint?.odo }}</td>
											<td class="text-center whitespace-nowrap">
												{{ dataPrint?.appointmentDate }}
											</td>
											<td class="text-center whitespace-nowrap">
												{{ dataPrint?.expectedHandoverDate }}
											</td>
										</tr>
									</tbody>
								</table>

								<div class="page-break">
									<p class="mb-4 mt-10">
										<b
											><span
												style="
													line-height: 107%;
													font-family: 'Times New Roman', serif;
												"
												>{{
													$t('module.sharedModules.print.repaireOrder.request')
												}}:</span
											></b
										>
									</p>

									<table class="table-custome">
										<tbody>
											<tr>
												<td>
													<p v-html="contentDescription"></p>
												</td>
											</tr>
										</tbody>
									</table>
								</div>

								<!-- <p class="mb-3 mt-12">
									<b
										><span
											style="
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>{{
												$t('module.sharedModules.print.repaireOrder.statusCar')
											}}:</span
										></b
									>
								</p>

								<table
									class="MsoTableGrid"
									cellspacing="0"
									cellpadding="0"
									style="border-collapse: collapse; width: 100%"
								>
									<tbody>
										<tr style="height: 61.15pt">
											<td
												width="623"
												valign="top"
												style="vertical-align: baseline !important"
												class="border border-black"
											>
												<p class="px-2" v-html="contentStatusCar"></p>
											</td>
										</tr>
									</tbody>
								</table> -->
								<div class="break-page">
									<p class="mb-3 mt-12">
										<b
											><span
												style="
													line-height: 107%;
													font-family: 'Times New Roman', serif;
												"
												>{{
													$t(
														'module.sharedModules.print.repaireOrder.serviceList'
													)
												}}:</span
											></b
										>
									</p>

									<table
										class="mb-8 service-table"
										border="1"
										cellspacing="0"
										cellpadding="0"
										style="border-collapse: collapse; border: none"
									>
										<tbody>
											<tr>
												<td
													width="5%"
													style="
														border: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.STT'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width=""
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.serviceName'
																	)
																}}</span
															></b
														>
													</p>
												</td>

												<td
													width="10%"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.quantity'
																	)
																}}</span
															></b
														>
													</p>
												</td>

												<td
													width="20%"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.employee'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="15%"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.status'
																	)
																}}</span
															></b
														>
													</p>
												</td>
											</tr>

											<template v-for="(ser, indexType) in serviceList">
												<tr>
													<td
														width="623"
														colspan="8"
														valign="top"
														style="
															width: 467.5pt;
															border: solid windowtext 1pt;
															border-top: none;
															padding: 0in 5.4pt 0in 5.4pt;
														"
													>
														<p
															class="MsoNormal"
															style="margin-bottom: 0in; line-height: normal"
														>
															<b
																><span
																	style="font-family: 'Times New Roman', serif"
																>
																	{{ convertToRoman(indexType + 1) }}.
																	{{ ser.typeName }}</span
																></b
															>
														</p>
													</td>
												</tr>

												<template v-for="(item, index) in ser.item">
													<tr>
														<td
															width="45"
															valign="top"
															style="
																width: 33.5pt;
																border: solid windowtext 1pt;
																border-top: none;
																padding: 0in 5.4pt 0in 5.4pt;
															"
														>
															<p
																class="MsoNormal"
																align="center"
																style="
																	margin-bottom: 0in;
																	text-align: center;
																	line-height: normal;
																"
															>
																<span
																	style="font-family: 'Times New Roman', serif"
																	>{{
																		getFirstIndexTemplatePrint(
																			indexType,
																			serviceList
																		) +
																		index +
																		1
																	}}</span
																>
															</p>
														</td>
														<td
															width="129"
															valign="top"
															style="
																width: 96.95pt;
																border-top: none;
																border-left: none;
																border-bottom: solid windowtext 1pt;
																border-right: solid windowtext 1pt;
																padding: 0in 5.4pt 0in 5.4pt;
															"
														>
															<p
																class="MsoNormal"
																align="center"
																style="
																	margin-bottom: 0in;
																	text-align: center;
																	line-height: normal;
																"
															>
																<span
																	style="font-family: 'Times New Roman', serif"
																>
																	{{ item.garaServiceName }}
																</span>
															</p>
														</td>
														<td
															width="63"
															valign="top"
															style="
																width: 47.25pt;
																border-top: none;
																border-left: none;
																border-bottom: solid windowtext 1pt;
																border-right: solid windowtext 1pt;
																padding: 0in 5.4pt 0in 5.4pt;
															"
														>
															<p
																class="MsoNormal"
																align="center"
																style="
																	margin-bottom: 0in;
																	text-align: center;
																	line-height: normal;
																"
															>
																<span
																	style="font-family: 'Times New Roman', serif"
																>
																	{{ item.quantity }}</span
																>
															</p>
														</td>

														<td
															width="79"
															valign="top"
															style="
																width: 59pt;
																border-top: none;
																border-left: none;
																border-bottom: solid windowtext 1pt;
																border-right: solid windowtext 1pt;
																padding: 0in 5.4pt 0in 5.4pt;
															"
														>
															<p
																class="MsoNormal"
																align="center"
																style="
																	margin-bottom: 0in;
																	text-align: center;
																	line-height: normal;
																"
															>
																<span
																	style="font-family: 'Times New Roman', serif"
																>
																	{{ item.employeeName }}</span
																>
															</p>
														</td>
														<td
															width="79"
															valign="top"
															style="
																width: 59pt;
																border-top: none;
																border-left: none;
																border-bottom: solid windowtext 1pt;
																border-right: solid windowtext 1pt;
																padding: 0in 5.4pt 0in 5.4pt;
															"
														>
															<p
																class="MsoNormal"
																align="center"
																style="
																	margin-bottom: 0in;
																	text-align: center;
																	line-height: normal;
																"
															>
																<span
																	style="font-family: 'Times New Roman', serif"
																>
																</span>
															</p>
														</td>
													</tr>
												</template>
											</template>
										</tbody>
									</table>
								</div>

								<div class="overflow-x-auto break-page">
									<p class="mb-4 mt-10">
										<b
											><span
												style="
													line-height: 107%;
													font-family: 'Times New Roman', serif;
												"
												>{{
													$t(
														'module.sharedModules.print.repaireOrder.productList'
													)
												}}:</span
											></b
										>
									</p>
									<table
										class="service-table"
										border="1"
										cellspacing="0"
										cellpadding="0"
										style="border-collapse: collapse; border: none"
									>
										<tbody>
											<tr>
												<td
													width="5%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.STT'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="20%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.productCode'
																	)
																}}
															</span></b
														>
													</p>
												</td>
												<td
													width=""
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.productName'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="10%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.quantity'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="15%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.unit'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="20%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<b
															><span
																style="font-family: 'Times New Roman', serif"
																>{{
																	$t(
																		'module.sharedModules.print.repaireOrder.table.note'
																	)
																}}</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr v-for="(product, index) in productList">
												<td
													width="45"
													valign="top"
													style="
														width: 33.5pt;
														border: solid windowtext 1pt;
														border-top: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span style="font-family: 'Times New Roman', serif">
															{{ index + 1 }}</span
														>
													</p>
												</td>
												<td
													width="105"
													valign="top"
													style="
														width: 78.75pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span style="font-family: 'Times New Roman', serif">
															{{ product.productCode }}
														</span>
													</p>
												</td>
												<td
													width="73"
													valign="top"
													style="
														width: 55.1pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span style="font-family: 'Times New Roman', serif">
															{{ product.productName }}
														</span>
													</p>
												</td>
												<td
													width="48"
													valign="top"
													style="
														width: 36.15pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span style="font-family: 'Times New Roman', serif">
															{{
																product.quantity ? product.quantity : '0'
															}}</span
														>
													</p>
												</td>
												<td
													width="47"
													valign="top"
													style="
														width: 35.35pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span
															style="font-family: 'Times New Roman', serif"
															>{{ product.unit }}</span
														>
													</p>
												</td>

												<td
													width="71"
													valign="top"
													style="
														width: 52.9pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="center"
														style="
															margin-bottom: 0in;
															text-align: center;
															line-height: normal;
														"
													>
														<span style="font-family: 'Times New Roman', serif">
														</span>
													</p>
												</td>
											</tr>
										</tbody>
									</table>
								</div>

								<p class="MsoNormal">
									<b
										><span
											style="
												font-size: 14pt;
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>&nbsp;</span
										></b
									>
								</p>

								<p class="mb-10 mt-10 flex justify-end mr-10">
									<b
										><span
											style="
												line-height: 107%;
												font-family: 'Times New Roman', serif;
											"
											>{{
												$t('module.sharedModules.print.repaireOrder.mentor')
											}}</span
										></b
									>
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
										<ACCDButton
											type="primary"
											@click="() => printHtml('printableAreaRepairOrder')"
											>{{
												$t('module.sharedModules.print.quotation.action.print')
											}}</ACCDButton
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
import { EStatusExport } from '@/enums'
import { ISelectOption } from '@/types'
import { onMounted, onUnmounted, ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import {
	formatPriceVN,
	to_vietnamese,
	convertToRoman,
	getFirstIndexTemplatePrint,
	handleGroupByTypeName,
} from '@/modules/sharedModules/component/constants'

import html2pdf from 'html2pdf.js'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
dayjs.extend(customParseFormat)

import { computed } from 'vue'
import _, { flatten, groupBy, result } from 'lodash'
import { IAccountInfo } from '@/types/Profile'

const emit = defineEmits<{
	(e: 'close'): void
}>()
const { $t } = useI18n()

const baseImg = import.meta.env.VITE_UPLOAD_SERVICE

const baseProductRowData = {
	stt: 1,
	productId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	unit: '',
	quantity: 0,
	unitPrice: 0,
	price: 0,
	money: 0,
	discount: 0,
	tax: 0,
	total: 0,
	instanceKey: 0,
	status: EStatusExport.UN_EXPORTED,
	outboundProductId: 0,
	disable: false,
}
const baseServiceRowData = {
	stt: 1,
	garageServiceTypeId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	garageServiceId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	quantity: 0 as number,
	unitPrice: 0 as number,
	money: 0 as number,
	discount: 0,
	tax: 0,
	total: 0,
	employeeId: {
		value: 0,
		options: [] as (ISelectOption & { origin: any })[],
	},
	instanceKey: 0,
	price: 0,
	disable: false,
}

type BillProps = {
	customerName: string
	phone: string
	address: string
	licensePlate: string
	customerTypeId: string
	description: string
	statusCar: string
	vinNumber: string
	carModelId: string
	carBrandId: string
	carYearId: string
	carVersionId: string
	odo: string
	serviceRowData: (typeof baseServiceRowData)[]
	productRowData: (typeof baseProductRowData)[]
	garageName: string
	garageAddress: string
	garagePhone: string
	garageAvatar: string
	accountInfo?: IAccountInfo
	appointmentDate?: string
	expectedHandoverDate?: string
}

const totalVietNamese = ref<string>('')
const printHtml = (divName: string) => {
	var printContents = document.getElementById(divName).innerHTML
	var originalContents = document.body.innerHTML

	document.body.innerHTML = printContents

	window.print()

	document.body.innerHTML = originalContents
	window.location.reload()
}

// const printHtml = () => {
// 	const download_section = document.getElementById('download_section')
// 	html2pdf()
// 		.from(download_section)
// 		.set({
// 			margin: [10, 0, 0, 0],
// 			filename: 'Lệnh Sửa Chữa.pdf',
// 			image: { type: 'jpg', quality: 0.98 },
// 			html2canvas: {
// 				scale: 2,
// 				logging: true,
// 				dpi: 192,
// 				letterRendering: true,
// 				useCORS: true,
// 			},
// 			jsPDF: { unit: 'mm', format: 'a4', orientation: 'p' },
// 			pagebreak: {
// 				mode: ['avoid-all', 'css'],
// 			},
// 		})
// 		.toPdf()
// 		.get('pdf')
// 		.then(function (pdf) {
// 			var totalPages = pdf.internal.getNumberOfPages()

// 			for (let i = 1; i <= totalPages; i++) {
// 				pdf.setPage(i)
// 				pdf.setFontSize(8)
// 				pdf.setTextColor(100)
// 				pdf.text(
// 					i + '/' + totalPages + '',
// 					pdf.internal.pageSize.getWidth() - 8,
// 					pdf.internal.pageSize.getHeight() - 8
// 				)
// 			}
// 		})
// 		.save()
// }

const dataPrint = ref<BillProps | null>(null)
const totalMoney = ref<number>(0)
const intoMoney = ref<number>(0)
const totalTax = ref<number>(0)
const totalDiscount = ref<number>(0)
const ticketCode = ref<string>('')

const contentStatusCar = computed(() => {
	return dataPrint.value?.statusCar
		? dataPrint.value?.statusCar.replace(/\n/g, '<br />')
		: ''
})

const contentDescription = computed(() => {
	return dataPrint.value?.description
		? dataPrint.value?.description.replace(/\n/g, '<br />')
		: ''
})

const serviceList = computed(() => {
	// const groupByTypeNameMapper: any = {}
	// dataPrint.value?.serviceRowData.forEach((s) => {
	// 	const garaTypeName = s.garageServiceTypeId.options.find(
	// 		(t) => t.value === s.garageServiceTypeId.value
	// 	)?.label

	// 	if (!garaTypeName) return
	// 	const garaServiceName = s.garageServiceId.options.find(
	// 		(_s) => _s.value === s.garageServiceId.value
	// 	)?.label

	// 	groupByTypeNameMapper[garaTypeName] =
	// 		groupByTypeNameMapper[garaTypeName] || []

	// 	groupByTypeNameMapper[garaTypeName].push({
	// 		...s,
	// 		garaServiceName,
	// 		garaTypeName,
	// 	})
	// })

	// const result = []
	// for (const key in groupByTypeNameMapper) {
	// 	result.push({
	// 		typeName: key,
	// 		item: groupByTypeNameMapper[key],
	// 	})
	// }

	// return result || []

	return handleGroupByTypeName(dataPrint.value?.serviceRowData)
})

const productList = computed(() => {
	return dataPrint.value?.productRowData.map((el) => {
		return {
			...el,
			productName:
				el.productId.options.find((pro) => pro.value == el.productId.value)
					?.origin?.name ||
				el.productId.options.find((pro) => pro.value == el.productId.value)
					?.label,
			productCode:
				el.productId.options.find((pro) => pro.value == el.productId.value)
					?.origin?.code || '',
		}
	})
})

onMounted(() => {
	dataPrint.value = JSON.parse(localStorage.getItem('dataPrint') || '{}')
	totalMoney.value = JSON.parse(localStorage.getItem('totalMoney') || '{}')
	intoMoney.value = JSON.parse(localStorage.getItem('intoMoney') || '{}')
	totalTax.value = JSON.parse(localStorage.getItem('totalTax') || '{}')
	totalDiscount.value = JSON.parse(
		localStorage.getItem('totalDiscount') || '{}'
	)
	ticketCode.value = JSON.parse(localStorage.getItem('ticketCode') || '{}')
	totalVietNamese.value = to_vietnamese(intoMoney.value)
})
</script>
<style lang="scss" scoped>
@import './css/custom.css';
@import './css/media-query.css';
.WordSection1 {
	padding: 40px;
	color: black;
}

body {
	font-size: 13px;
	font-family: 'Times New Roman', serif !important;
}
table {
	width: 100%;
	td {
		padding: 0px 2px !important;
		p {
			padding: 2px 0px !important;
		}
	}
}

.table-custome {
	border-collapse: collapse;
	th,
	td {
		border: 1px solid;
	}
}

.service-table {
	td {
		vertical-align: middle !important;
	}
}

@media print {
	#printableAreaRepairOrder {
		display: none !important;
	}
	.page-break {
		page-break-inside: avoid;
	}
	tr,
	th,
	td {
		page-break-inside: avoid !important;
		page-break-after: auto;
	}
}
</style>
