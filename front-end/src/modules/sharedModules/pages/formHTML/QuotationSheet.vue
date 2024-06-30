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
							id="printableAreaQuotation"
						>
							<div class="WordSection1">
								<div class="flex gap-6">
									<div>
										<img
											v-if="dataPrint?.garageAvatar"
											:src="baseImg + dataPrint?.garageAvatar"
											alt="logo"
											size="100px"
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
											<div class="font-bold whitespace-nowrap w-[120px]">
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
											<div class="font-bold whitespace-nowrap w-[120px]">
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
												$t('module.sharedModules.print.quotation.title')
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
												$t('module.sharedModules.print.quotation.ticketCode')
											}}: {{ ticketCode.replace('DV', 'BG') }}</span
										>
									</b>
								</p>

								<table class="table-custome">
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
													$t('module.sharedModules.print.quotation.request')
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
												$t('module.sharedModules.print.quotation.statusCar')
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

								<div class="page-break">
									<p class="mb-3 mt-12">
										<b
											><span
												style="
													line-height: 107%;
													font-family: 'Times New Roman', serif;
												"
												>{{
													$t(
														'module.sharedModules.print.quotation.serviceList'
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
																		'module.sharedModules.print.quotation.table.STT'
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
																		'module.sharedModules.print.quotation.table.serviceName'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="5%"
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
																		'module.sharedModules.print.quotation.table.quantity'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="12%"
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
																		'module.sharedModules.print.quotation.table.unitPrice'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.money'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.discount'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="8%"
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
																		'module.sharedModules.print.quotation.table.tax'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.intoMoney'
																	)
																}}
															</span></b
														>
													</p>
												</td>
											</tr>

											<template v-for="(ser, indexType) in serviceList">
												<tr>
													<td
														colspan="8"
														valign="top"
														style="
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
																	{{ item.quantity ? item.quantity : '0' }}
																</span>
															</p>
														</td>
														<td
															width="58"
															valign="top"
															style="
																width: 43.35pt;
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
																	{{ formatPriceVN(item.unitPrice) }},000
																</span>
															</p>
														</td>
														<td
															width="93"
															valign="top"
															style="
																width: 69.5pt;
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
																	{{ formatPriceVN(item.money) }},000</span
																>
															</p>
														</td>
														<td
															width="79"
															valign="top"
															style="
																width: 59.25pt;
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
																<span
																	style="font-family: 'Times New Roman', serif"
																>
																	{{
																		dataPrint.discountType == 1
																			? item.discount
																				? `${formatPriceVN(
																						item.discount
																				  )} ,000 VNĐ`
																				: `0%`
																			: `${item.discount}%`
																	}}
																</span>
															</p>
														</td>
														<td
															width="78"
															valign="top"
															style="
																width: 58.7pt;
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
																	>{{ item.tax }}%
																</span>
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
																	{{ formatPriceVN(item.total) }},000</span
																>
															</p>
														</td>
													</tr>
												</template>
											</template>
										</tbody>
									</table>
								</div>

								<div class="overflow-x-auto page-break">
									<p class="mb-4 mt-10">
										<b
											><span
												style="
													line-height: 107%;
													font-family: 'Times New Roman', serif;
												"
												>{{
													$t(
														'module.sharedModules.print.quotation.productList'
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
																		'module.sharedModules.print.quotation.table.STT'
																	)
																}}</span
															></b
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
																		'module.sharedModules.print.quotation.table.productName'
																	)
																}}
															</span></b
														>
													</p>
												</td>
												<td
													width="5%"
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
																		'module.sharedModules.print.quotation.table.unit'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="5%"
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
																		'module.sharedModules.print.quotation.table.quantity'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="12%"
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
																		'module.sharedModules.print.quotation.table.unitPrice'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.money'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.discount'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="8%"
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
																		'module.sharedModules.print.quotation.table.tax'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="14%"
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
																		'module.sharedModules.print.quotation.table.intoMoney'
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
															{{ product.productName }}
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
															{{ product.unit }}
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
														<span style="font-family: 'Times New Roman', serif">
															{{ formatPriceVN(product.unitPrice) }},000</span
														>
													</p>
												</td>
												<td
													width="94"
													valign="top"
													style="
														width: 70.85pt;
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
														<span style="font-family: 'Times New Roman', serif"
															>{{ formatPriceVN(product.money) }},000</span
														>
													</p>
												</td>
												<td
													width="78"
													valign="top"
													style="
														width: 58.35pt;
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
															{{
																dataPrint.discountType == 1
																	? product.discount
																		? `${formatPriceVN(
																				product.discount
																		  )} ,000 VNĐ`
																		: `0%`
																	: `${product.discount}%`
															}}
														</span>
													</p>
												</td>
												<td
													width="62"
													valign="top"
													style="
														width: 46.55pt;
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
														<span style="font-family: 'Times New Roman', serif"
															>{{ product.tax }}%</span
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
															{{ formatPriceVN(product.total) }},000</span
														>
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

								<div class="page-break">
									<table
										class="MsoTableGrid"
										border="1"
										cellspacing="0"
										cellpadding="0"
										style="border-collapse: collapse; border: none"
									>
										<tbody>
											<tr>
												<td
													width="50%"
													valign="top"
													style="
														border: solid windowtext 1pt;
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
																>{{
																	$t(
																		'module.sharedModules.print.quotation.table.note'
																	)
																}}:</span
															></b
														>
														{{ dataPrint?.note }}
													</p>
												</td>
												<td
													width="28%"
													valign="top"
													style="
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														align="right"
														style="
															margin-bottom: 0in;
															text-align: right;
															line-height: 120%;
															margin-right: 10px;
														"
													>
														<b
															><i
																><span
																	style="
																		line-height: 120%;
																		font-family: 'Times New Roman', serif;
																	"
																	>{{
																		$t(
																			'module.sharedModules.print.quotation.table.money'
																		)
																	}}:</span
																></i
															></b
														>
													</p>
													<p
														class="MsoNormal"
														align="right"
														style="
															margin-bottom: 0in;
															text-align: right;
															line-height: 120%;
															margin-right: 10px;
														"
													>
														<b
															><i
																><span
																	style="
																		line-height: 120%;
																		font-family: 'Times New Roman', serif;
																	"
																	>{{
																		$t(
																			'module.sharedModules.print.quotation.table.totalDiscount'
																		)
																	}}:</span
																></i
															></b
														>
													</p>
													<p
														class="MsoNormal"
														align="right"
														style="
															margin-bottom: 0in;
															text-align: right;
															line-height: 120%;
															margin-right: 10px;
														"
													>
														<b
															><i
																><span
																	style="
																		line-height: 120%;
																		font-family: 'Times New Roman', serif;
																	"
																>
																	{{
																		$t(
																			'module.sharedModules.print.quotation.table.totalTax'
																		)
																	}}:</span
																></i
															></b
														>
													</p>
													<p
														class="MsoNormal"
														align="right"
														style="
															margin-bottom: 0in;
															text-align: right;
															line-height: 120%;
															margin-right: 10px;
														"
													>
														<b
															><i
																><span
																	style="
																		line-height: 120%;
																		font-family: 'Times New Roman', serif;
																	"
																	>{{
																		$t(
																			'module.sharedModules.print.quotation.table.intoMoney'
																		)
																	}}:</span
																></i
															></b
														>
													</p>
												</td>
												<td
													width="125"
													valign="top"
													style="
														width: 93.5pt;
														border: solid windowtext 1pt;
														border-left: none;
														padding: 0in 5.4pt 0in 5.4pt;
													"
												>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 120%"
													>
														<b
															><span
																style="
																	margin-left: 10px;
																	font-family: 'Times New Roman', serif;
																"
															>
																{{ formatPriceVN(intoMoney) }}</span
															></b
														>
													</p>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 120%"
													>
														<b
															><span
																style="
																	margin-left: 10px;
																	font-family: 'Times New Roman', serif;
																"
															>
																{{ formatPriceVN(totalDiscount) }}</span
															></b
														>
													</p>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 120%"
													>
														<b
															><span
																style="
																	margin-left: 10px;
																	font-family: 'Times New Roman', serif;
																"
															>
																{{ formatPriceVN(totalTax) }}</span
															></b
														>
													</p>
													<p
														class="MsoNormal"
														style="margin-bottom: 0in; line-height: 120%"
													>
														<b
															><span
																style="
																	margin-left: 10px;
																	font-family: 'Times New Roman', serif;
																"
															>
																{{ formatPriceVN(totalMoney) }}</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr>
												<td
													width="623"
													colspan="3"
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
																{{
																	$t(
																		'module.sharedModules.print.quotation.table.totalString'
																	)
																}}
																:
																{{ totalVietNamese }}</span
															></b
														>
													</p>
												</td>
											</tr>
											<tr style="height: 94pt">
												<td
													width="312"
													valign="top"
													style="
														width: 233.75pt;
														border: solid windowtext 1pt;
														border-top: none;
														padding: 0in 5.4pt 0in 5.4pt;
														height: 94pt;
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
																		'module.sharedModules.print.quotation.mentor'
																	)
																}}</span
															></b
														>
													</p>
												</td>
												<td
													width="312"
													colspan="2"
													valign="top"
													style="
														width: 233.75pt;
														border-top: none;
														border-left: none;
														border-bottom: solid windowtext 1pt;
														border-right: solid windowtext 1pt;
														padding: 0in 5.4pt 0in 5.4pt;
														height: 94pt;
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
																		'module.sharedModules.print.quotation.customer'
																	)
																}}</span
															></b
														>
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

								<p class="MsoNormal">&nbsp;</p>
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
											@click="() => printHtml('printableAreaQuotation')"
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

import VueHtml2pdf from 'vue-html2pdf'

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
	note: string
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
	discountType: number
	accountInfo?: IAccountInfo
	appointmentDate?: string
	expectedHandoverDate?: string
}

const totalVietNamese = ref<string>('')

const options = {
	margin: [10, 0, 0, 0], //top, left, buttom, right,
	filename: 'Phiếu Báo Giá.pdf',
	image: { type: 'jpeg', quality: 0.98 },
	// html2canvas: { dpi: 192, scale: 2, letterRendering: true },
	html2canvas: { scale: 4, useCORS: true, dpi: 192, letterRendering: true },
	pagebreak: {
		mode: ['avoid-all', 'css'],
	},
	// jsPDF: { unit: "pt", format: "a4", orientation: "portrait" },
	jsPDF: {
		unit: 'mm',
		format: 'a4',
		orientation: 'portrait',
		putTotalPages: true,
	},
}

// const printHtml = () => {
// 	const download_section = document.getElementById('download_section')
// 	html2pdf().from(download_section).set(options).save()
// }

const printHtml = (divName: string) => {
	var printContents = document.getElementById(divName).innerHTML
	var originalContents = document.body.innerHTML

	document.body.innerHTML = printContents

	window.print()

	document.body.innerHTML = originalContents
	window.location.reload()
}

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
	return handleGroupByTypeName(dataPrint.value?.serviceRowData)
})

const productList = computed(() => {
	return dataPrint.value?.productRowData.map((el) => {
		return {
			...el,
			productName:
				el.productId.options.find((pro) => pro.value == el.productId.value)
					?.label || '',
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
	totalVietNamese.value = to_vietnamese(totalMoney.value)
	console.log('dataPrint', dataPrint.value)
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
	#printableAreaQuotation {
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
