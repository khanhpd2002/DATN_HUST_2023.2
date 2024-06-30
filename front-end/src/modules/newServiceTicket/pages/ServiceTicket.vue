<template>
	<div class="flex justify-between gap-3 mb-4">
		<div class="flex items-center gap-3">
			<ACCDAIcon
				name="ArrowLeft"
				@click="handleConfirmSave"
				class="cursor-pointer"
			/>
			<p class="font-bold text-[16px]">
				{{ $t('module.newServiceTicket.form.detail') }}
			</p>
			<p class="text-[16px] text-primary">
				{{ serviceTicketForm.code }}
			</p>
		</div>

		<ACCDDropdown
			class="ml-4 hidden lg:block cursor-pointer mt-2 pr-4"
			v-if="currentStep !== EStepperTicket.RECEIVE"
		>
			<template #activator>
				<ACCDButton class="flex items-center gap-3" type="secondary">
					<ACCDAIcon name="Printer" siz="20" />
					<span class="font-medium">In</span>
				</ACCDButton>
			</template>
			<template #default>
				<ul class="bg-layout-primary border border-line py-1 w-full rounded-md">
					<li
						class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center font-medium"
						@click="() => handleChangePrint(1)"
					>
						{{ $t('module.newServiceTicket.form.print.printQuation') }}
					</li>
					<li
						class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center font-medium"
						@click="() => handleChangePrint(2)"
					>
						{{ $t('module.newServiceTicket.form.print.printRepairOrder') }}
					</li>
					<li
						class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center font-medium"
						@click="() => handleChangePrint(3)"
					>
						{{ $t('module.newServiceTicket.form.print.printService') }}
					</li>
				</ul>
			</template>
		</ACCDDropdown>
	</div>

	<div class="flex flex-col gap-4 bg-white p-4 rounded-lg">
		<div class="stepper" v-if="currentStep != EStepperTicket.CUSTOMER_CANCEL">
			<ACCDStep
				class="flex flex-col md:flex-row md:items-center w-auto font-medium"
				:current="currentStep"
				:items="stepItems"
			/>
		</div>
		<div v-if="currentStep == EStepperTicket.CUSTOMER_CANCEL">
			<span class="italic">
				{{ $t(`module.newServiceTicket.error.cancel`) }}
				{{ dateCancel }}</span
			>
		</div>

		<div class="cd-modal__body py-4">
			<ACCDForm :show-footer="false">
				<div
					class="flex flex-col lg:flex-row md:flex-row gap-4 flex-wrap md:flex-nowrap"
				>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.phone')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.phone')"
							size="md"
							:disabled="
								serviceTicketForm.customerPhone.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.customerPhone.value"
							:options="serviceTicketForm.customerPhone.options"
							:forCreate="true"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.customerType')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.customerType')"
							:disabled="
								serviceTicketForm.customerTypeId.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.customerTypeId.value"
							:options="serviceTicketForm.customerTypeId.options"
							size="md"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.customerName')"
						class="w-full font-semibold"
					>
						<ACCDInputText
							:disabled="
								serviceTicketForm.customerName.disabled ||
								formConfig.action == EFormState.VIEW
							"
							:placeholder="$t('module.newServiceTicket.form.customerName')"
							v-model="serviceTicketForm.customerName.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
				</div>
				<div
					class="flex flex-col lg:flex-row md:flex-row gap-4 flex-wrap md:flex-nowrap"
				>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.licensePlate')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.licensePlate')"
							:disabled="
								serviceTicketForm.licensePlate.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.licensePlate.value"
							:options="serviceTicketForm.licensePlate.options"
							size="md"
							:forCreate="true"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.vinNumber')"
						class="w-full font-semibold"
					>
						<ACCDInputText
							:placeholder="$t('module.newServiceTicket.form.vinNumber')"
							:disabled="
								serviceTicketForm.vinNumber.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.vinNumber.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.journey')"
						class="w-full font-semibold"
					>
						<ACCDInputPrice
							:placeholder="$t('module.newServiceTicket.form.journey')"
							:disabled="
								serviceTicketForm.odo.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.odo.value"
							size="md"
							class="font-medium"
						></ACCDInputPrice>
					</ACCDFormItem>
				</div>

				<div
					class="flex flex-col lg:flex-row md:flex-row gap-4 flex-wrap md:flex-nowrap"
					v-if="serviceTicketForm.carBrandId.value"
				>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carBrand')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:disabled="
								serviceTicketForm.carBrandId.disabled ||
								formConfig.action == EFormState.VIEW
							"
							:placeholder="$t('module.newServiceTicket.form.carBrand')"
							v-model="serviceTicketForm.carBrandId.value"
							:options="serviceTicketForm.carBrandId.options"
							size="md"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carModel')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.carModel')"
							:disabled="
								serviceTicketForm.carModelId.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.carModelId.value"
							:options="serviceTicketForm.carModelId.options"
							size="md"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carYear')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.carYear')"
							:disabled="
								serviceTicketForm.carYearId.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.carYearId.value"
							:options="serviceTicketForm.carYearId.options"
							size="md"
							class="font-medium"
						></ACCDSelect>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carVersion')"
						class="w-full font-semibold"
					>
						<ACCDSelect
							:placeholder="$t('module.newServiceTicket.form.carVersion')"
							:disabled="
								serviceTicketForm.carVersionId.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.carVersionId.value"
							:options="serviceTicketForm.carVersionId.options"
							size="md"
							class="font-medium"
						>
						</ACCDSelect>
					</ACCDFormItem>
				</div>

				<div v-else>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carName')"
						class="w-full font-semibold"
					>
						<ACCDInputText
							:disabled="
								formConfig.action == EFormState.VIEW ||
								serviceTicketForm.carName.disable
							"
							:placeholder="$t('module.newServiceTicket.form.carName')"
							v-model="serviceTicketForm.carName.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
				</div>

				<div class="flex gap-3 mb-3">
					<div class="w-1/2 font-semibold">
						<span>
							{{ $t('module.newServiceTicket.form.appointmentDate') }}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="serviceTicketForm.appointmentDate.value"
							formatter="DD/MM/YY"
							:disabled="
								formConfig.action == EFormState.VIEW ||
								serviceTicketForm.appointmentDate.disable
							"
							class="font-medium"
						/>
					</div>
					<div class="w-1/2 font-semibold">
						<span> {{ $t('module.newServiceTicket.form.handOverDate') }}</span>
						<ACCDDatePicker
							size="md"
							v-model="serviceTicketForm.expectedHandoverDate.value"
							formatter="DD/MM/YY"
							:disabled="
								formConfig.action == EFormState.VIEW ||
								serviceTicketForm.expectedHandoverDate.disable
							"
							class="font-medium"
						/>
					</div>
				</div>
				<div>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.requirement')"
						class="font-semibold"
					>
						<ACCDTextArea
							:disabled="
								serviceTicketForm.description.disabled ||
								formConfig.action == EFormState.VIEW
							"
							v-model="serviceTicketForm.description.value"
							:placeholder="$t('module.newServiceTicket.form.requirement')"
							class="font-medium"
							:trim="true"
						>
						</ACCDTextArea>
					</ACCDFormItem>
				</div>

				<div>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.statusCar')"
						class="font-semibold"
					>
						<ACCDTextArea
							:disabled="
								serviceTicketForm.statusCar.disabled ||
								currentStep == EStepperTicket.CUSTOMER_CANCEL ||
								currentStep == EStepperTicket.PROCEED ||
								currentStep == EStepperTicket.SELL_PRICE ||
								currentStep == EStepperTicket.PAYMENT ||
								currentStep == EStepperTicket.HAND_OVER
							"
							v-model="serviceTicketForm.statusCar.value"
							:placeholder="$t('module.newServiceTicket.form.statusCar')"
							class="font-medium"
							:trim="true"
						>
						</ACCDTextArea>
					</ACCDFormItem>
				</div>
				<span class="italic text-red-500" v-if="isError">
					{{ $t('module.newServiceTicket.error.missingRequiredField') }}
				</span>
			</ACCDForm>

			<div
				v-if="
					(currentStep !== EStepperTicket.RECEIVE &&
						currentStep !== EStepperTicket.CUSTOMER_CANCEL) ||
					(currentStep === EStepperTicket.CUSTOMER_CANCEL &&
						productRowData.length > 0)
				"
			>
				<span class="font-semibold text-base block mb-2">{{
					$t('module.newServiceTicket.form.service.title')
				}}</span>
				<TableContainer>
					<ACCDTable
						:columns="serviceColumns"
						:rowData="serviceRowData"
						class="w-full"
					>
						<template #cell-stt="{ row, rowIndex, col, field }">
							{{ rowIndex + 1 }}
						</template>

						<template #cell-garageServiceTypeId="{ row, col, field }">
							<div
								v-if="currentStep !== EStepperTicket.SELL_PRICE || row.disable"
							>
								{{
									row.garageServiceTypeId.options.filter(
										(garageServiceType) =>
											garageServiceType.value === row.garageServiceTypeId.value
									)[0]?.label
								}}
							</div>

							<ACCDSelect
								@change="() => onSelectServiceTypeId(row)"
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								v-model="row.garageServiceTypeId.value"
								:options="row.garageServiceTypeId.options"
								class="w-full"
								:readonly="row.disable"
							>
							</ACCDSelect>
						</template>
						<template #cell-garageServiceId="{ row, col, field }">
							<div
								v-if="currentStep !== EStepperTicket.SELL_PRICE || row.disable"
							>
								{{
									row.garageServiceId.options.filter(
										(garageService) =>
											garageService.value === row.garageServiceId.value
									)[0]?.label
								}}
							</div>
							<ACCDSelect
								@change="(val: string) => onSelectServiceId( row, val)"
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								:readonly="row.disable || !row.garageServiceTypeId.value"
								v-model="row.garageServiceId.value"
								:options="row.garageServiceId.options"
								class="w-full"
								:forCreate="true"
								:autoCreate="true"
								placeholder=""
							>
							</ACCDSelect>
						</template>

						<template #cell-employeeId="{ row, col, field }">
							<div
								v-if="currentStep !== EStepperTicket.SELL_PRICE || row.disable"
							>
								{{
									row.employeeId.options.filter(
										(employee) => employee.value === row.employeeId.value
									)[0]?.label
								}}
							</div>
							<ACCDSelect
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								:readonly="row.disable"
								v-model="row.employeeId.value"
								:options="row.employeeId.options"
							>
							</ACCDSelect>
						</template>
						<template #cell-unitPrice="{ row, col, field }">
							<div
								v-if="currentStep !== EStepperTicket.SELL_PRICE || row.disable"
							>
								{{ formatPriceVN(row.unitPrice) }},000
							</div>
							<ACCDInputPrice
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								:readonly="row.disable"
								v-model="row.unitPrice"
								@update:modelValue="() => onChangeRowService(row, field)"
								class="w-full"
							>
								<template #inner-append>,000</template>
							</ACCDInputPrice>
						</template>
						<template #cell-money="{ row, col, field }">
							{{ formatPriceVN(row.money) }},000
						</template>
						<template #cell-discount="{ row, col, field }">
							<div
								v-if="
									(currentStep !== EStepperTicket.SELL_PRICE || row.disable) &&
									discountType == 1
								"
							>
								{{
									row.discount
										? `${formatPriceVN(row.discount)} ,000 VNĐ`
										: `0,000 VNĐ`
								}}
							</div>
							<div
								v-if="
									(currentStep !== EStepperTicket.SELL_PRICE || row.disable) &&
									discountType == 2
								"
							>
								{{ row.discount }}%
							</div>
							<div>
								<ACCDInputPrice
									v-if="
										currentStep == EStepperTicket.SELL_PRICE && !row.disable
									"
									:readonly="row.disable"
									v-model="row.discount"
									@update:modelValue="() => onChangeRowService(row, field)"
									@keypress="preFilterInputNumberOnly"
									class="w-full"
								>
									<template #inner-append>
										<span v-if="discountType == 1">,000</span>
										<span class="px-2" v-if="discountType == 1">VNĐ</span>
										<span class="px-2" v-if="discountType != 1">%</span>
									</template>
								</ACCDInputPrice>
							</div>
						</template>
						<template #cell-quantity="{ row, col, field }">
							<div v-if="currentStep !== EStepperTicket.SELL_PRICE">
								{{ row.quantity }}
							</div>

							<div v-else>
								<div v-if="row.disable">{{ row.quantity }}</div>
								<ACCDInputNumber
									v-else
									:readonly="row.disable"
									v-model="row.quantity"
									@update:modelValue="() => onChangeRowService(row, field)"
									@keypress="preFilterInputNumberOnly"
									class="w-full"
								>
								</ACCDInputNumber>
							</div>
						</template>
						<template #cell-tax="{ row, col, field }">
							<div
								v-if="currentStep !== EStepperTicket.SELL_PRICE || row.disable"
							>
								{{ row.tax }}%
							</div>
							<ACCDInputText
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								:readonly="row.disable"
								v-model="row.tax"
								@update:modelValue="() => onChangeRowService(row, field)"
								@keypress="preFilterInputNumberOnly"
								class="w-full"
							>
								<template #inner-append><span class="px-2">%</span></template>
							</ACCDInputText>
						</template>
						<template #cell-total="{ row, col, field }">
							{{ formatPriceVN(row.total) }},000
						</template>
						<template #cell-action="{ row, col, field }">
							<ACCDAIcon
								@click="(val: string | number) => {
								deleteServiceRow(row);
							}
								"
								class="text-gray-700 cursor-pointer"
								name="Trash"
								v-if="currentStep == EStepperTicket.SELL_PRICE"
							>
							</ACCDAIcon>
						</template>
					</ACCDTable>
				</TableContainer>

				<div v-if="error.errorServiceMsg">
					<span class="italic text-red-500">
						{{ error.errorServiceMsg }}
					</span>
				</div>
				<div v-if="error.errorResponse">
					<span class="italic text-red-500">
						{{ error.errorResponse }}
					</span>
				</div>
				<div v-if="errorServiceMoney">
					<span class="italic text-red-500">
						{{ errorServiceMoney }}
					</span>
				</div>

				<div
					v-if="
						currentStep == EStepperTicket.RECEIVE ||
						currentStep == EStepperTicket.SELL_PRICE
					"
					class="flex items-center text-active mt-1"
				>
					<span @click="addServiceRowData" class="cursor-pointer mr-1">
						<CDAIcon name="AddCircle"
					/></span>
					<span @click="addServiceRowData" class="cursor-pointer font-medium">
						{{
							$t('module.newServiceTicket.form.service.addServiceRowData')
						}}</span
					>
				</div>
			</div>

			<div
				v-if="
					(currentStep !== EStepperTicket.RECEIVE &&
						currentStep !== EStepperTicket.CUSTOMER_CANCEL) ||
					(currentStep === EStepperTicket.CUSTOMER_CANCEL &&
						productRowData.length > 0)
				"
				class="mt-6"
			>
				<span class="font-semibold text-base block mb-2">{{
					$t('module.serviceTicket.form.product.title')
				}}</span>
				<TableContainer>
					<ACCDTable
						:columns="productColumns"
						:rowData="productRowData"
						class="w-full"
					>
						<template #cell-action="{ row, col, field }">
							<WrapFlexContainer>
								<ACCDAIcon
									@click="
										() => {
											deleteProductRow(row)
										}
									"
									class="text-xl text-gray-700 cursor-pointer"
									name="Trash"
									v-if="currentStep == EStepperTicket.SELL_PRICE"
								></ACCDAIcon>
							</WrapFlexContainer>
						</template>

						<template #cell-stt="{ row, rowIndex, col, field }">
							<WrapFlexContainer>
								<div class="text-center font-medium text-typo overflow-hidden">
									{{ rowIndex + 1 }}
								</div>
							</WrapFlexContainer>
						</template>
						<template #cell-productId="{ row, col, field }">
							<WrapFlexContainer>
								<div
									v-if="currentStep != EStepperTicket.SELL_PRICE || row.disable"
								>
									{{
										row.productId.options.filter(
											(product) => product.value === row.productId.value
										)[0]?.label
									}}
								</div>
								<ACCDAutoComplete
									v-if="
										currentStep == EStepperTicket.SELL_PRICE && !row.disable
									"
									:readonly="row.disable"
									v-model="row.productId.value"
									@update:modelValue="onSelectProduct(row)"
									:options="row.productId.options"
									class="w-full"
									:forCreate="true"
									:autoCreate="true"
									placeholder=""
								>
								</ACCDAutoComplete>
							</WrapFlexContainer>
						</template>
						<template #cell-unit="{ row, col, field }">
							<WrapFlexContainer v-if="typeof row.productId.value == 'number'">
								{{ row.unit }}</WrapFlexContainer
							>

							<ACCDInputText
								v-else
								v-model="row.unit"
								@update:modelValue="() => onChangeRowProduct(row, field)"
								class="w-full"
							>
							</ACCDInputText>
						</template>
						<template #cell-discount="{ row, col, field }">
							<div
								v-if="
									(currentStep != EStepperTicket.SELL_PRICE || row.disable) &&
									discountType == 1
								"
							>
								{{
									row.discount
										? `${formatPriceVN(row.discount)} ,000 VNĐ`
										: `0,000 VNĐ`
								}}
							</div>
							<div
								v-if="
									(currentStep != EStepperTicket.SELL_PRICE || row.disable) &&
									discountType == 2
								"
							>
								{{ row.discount }}%
							</div>
							<ACCDInputPrice
								v-if="currentStep == EStepperTicket.SELL_PRICE && !row.disable"
								v-model="row.discount"
								:readonly="row.disable"
								@update:modelValue="() => onChangeRowProduct(row, field)"
								@keypress="preFilterInputNumberOnly"
								class="w-full"
							>
								<template #inner-append>
									<span v-if="discountType == 1">,000</span>
									<span class="px-2" v-if="discountType == 1">VNĐ</span>
									<span class="px-2" v-if="discountType != 1">%</span>
								</template>
							</ACCDInputPrice>
						</template>
						<template #cell-tax="{ row, col, field }">
							<WrapFlexContainer>
								<div
									v-if="currentStep != EStepperTicket.SELL_PRICE || row.disable"
								>
									{{ row.tax }}%
								</div>
								<ACCDInputText
									v-if="
										currentStep == EStepperTicket.SELL_PRICE && !row.disable
									"
									v-model="row.tax"
									:readonly="row.disable"
									@update:modelValue="() => onChangeRowProduct(row, field)"
									@keypress="preFilterInputNumberOnly"
									class="w-full"
								>
									<template #inner-append><span class="px-2">%</span></template>
								</ACCDInputText>
							</WrapFlexContainer>
						</template>
						<template #cell-total="{ row, col, field }">
							<WrapFlexContainer>
								{{ formatPriceVN(row.total) }},000
							</WrapFlexContainer>
						</template>
						<template #cell-quantity="{ row, col, field }">
							<WrapFlexContainer>
								<div v-if="currentStep != EStepperTicket.SELL_PRICE">
									{{ row.quantity ? row.quantity : 'N/A' }}
								</div>

								<div v-else>
									<div v-if="row.disable">
										{{ row.quantity }}
									</div>
									<ACCDInputNumber
										v-else
										v-model="row.quantity"
										:readonly="row.disable"
										@update:modelValue="() => onChangeRowProduct(row, field)"
										@keypress="preFilterInputNumberOnly"
										class="w-full"
									>
									</ACCDInputNumber>
								</div>
							</WrapFlexContainer>
						</template>
						<template #cell-unitPrice="{ row, col, field }">
							<WrapFlexContainer>
								<div
									v-if="currentStep != EStepperTicket.SELL_PRICE || row.disable"
								>
									{{ formatPriceVN(row.unitPrice) }},000
								</div>
								<ACCDInputPrice
									v-if="
										currentStep == EStepperTicket.SELL_PRICE && !row.disable
									"
									v-model="row.unitPrice"
									:readonly="row.disable"
									@change="() => onChangeRowProduct(row, field)"
									class="w-full"
								>
									<template #inner-append>,000</template>
								</ACCDInputPrice>
							</WrapFlexContainer>
						</template>
						<template #cell-status="{ row, col, field }">
							<WrapFlexContainer>
								<p
									v-if="currentStep == EStepperTicket.PROCEED"
									class="py-[8px] px-[12px] rounded-[16px] flex items-center text-center whitespace-nowrap w-fit"
									:class="`status-new-service-${row.status == 1 ? '6' : '0'}`"
									@click="() => handleOpenOutbound(row.outboundTicketId)"
								>
									<span class="mr-1 underline cursor-pointer">
										{{
											row.status == 1
												? EStatusExport.EXPORTED_1
												: EStatusExport.UN_EXPORTED_1
										}}
									</span>
									<span> <CDAIcon name="ExportSquare" size="16" /></span>
								</p>

								<p
									v-else
									class="py-[8px] px-[12px] rounded-[16px] flex items-center text-center whitespace-nowrap w-fit"
									:class="`status-new-service-${row.status == 1 ? '6' : '0'}`"
								>
									<span class="mr-1">
										{{
											row.status == 1
												? EStatusExport.EXPORTED_1
												: EStatusExport.UN_EXPORTED_1
										}}
									</span>
								</p>
							</WrapFlexContainer>
						</template>

						<template #cell-money="{ row, col, field }">
							<WrapFlexContainer>
								{{ formatPriceVN(row.money) }},000
							</WrapFlexContainer>
						</template>
					</ACCDTable>
				</TableContainer>

				<div
					v-if="
						currentStep == EStepperTicket.RECEIVE ||
						currentStep == EStepperTicket.SELL_PRICE
					"
					class="flex items-center text-active mt-1"
				>
					<span class="cursor-pointer mr-1" @click="addProductRowData">
						<CDAIcon name="AddCircle" />
					</span>
					<span class="cursor-pointer font-medium" @click="addProductRowData">
						{{
							$t('module.newServiceTicket.form.product.addProductRowData')
						}}</span
					>
				</div>

				<div v-if="error.errorProductMsg">
					<span class="italic text-red-500">
						{{ error.errorProductMsg }}
					</span>
				</div>

				<div v-if="error.errorMsg">
					<span class="italic text-red-500">
						{{ error.errorMsg }}
					</span>
				</div>
				<div v-if="errorProductMoney">
					<span class="italic text-red-500">
						{{ errorProductMoney }}
					</span>
				</div>
			</div>

			<div
				v-if="
					currentStep == EStepperTicket.PAYMENT ||
					currentStep == EStepperTicket.HAND_OVER
				"
				class="w-1/5 ml-auto py-3"
			>
				<div class="flex justify-between">
					<p>
						{{ $t('module.serviceTicket.form.cash.intoMoney') }}
					</p>
					<p>{{ formatPriceVN(Number(intoMoney)) }}</p>
				</div>
				<div class="flex justify-between">
					<p>
						{{ $t('module.serviceTicket.form.cash.totalDiscount') }}
					</p>
					<p>{{ formatPriceVN(Number(totalDiscount)) }}</p>
				</div>
				<div class="flex justify-between">
					<p>
						{{ $t('module.serviceTicket.form.cash.totalTax') }}
					</p>
					<p>{{ formatPriceVN(Number(totalTax)) }}</p>
				</div>
				<div class="flex justify-between border-t-2 pt-2 mt-2">
					<p class="font-semibold">
						{{ $t('module.serviceTicket.form.cash.totalMoney') }}
					</p>
					<p class="font-semibold">
						{{ formatPriceVN(Number(totalMoney)) }}
					</p>
				</div>
			</div>
		</div>

		<ACCDFormItem
			v-if="currentStep !== EStepperTicket.RECEIVE"
			:label="$t('module.newServiceTicket.form.note')"
			class="font-semibold"
		>
			<ACCDTextArea
				:disabled="currentStep !== EStepperTicket.SELL_PRICE"
				v-model="serviceTicketForm.note.value"
				:placeholder="$t('module.newServiceTicket.form.note')"
				class="font-medium"
				:trim="true"
			>
			</ACCDTextArea>
		</ACCDFormItem>
	</div>

	<div>
		<div class="flex justify-end gap-2.5 px-4 py-4 cd-modal__footer">
			<ACCDButton
				type="secondary"
				size="md"
				@click="onCancel"
				v-if="
					currentStep == EStepperTicket.RECEIVE ||
					currentStep == EStepperTicket.SELL_PRICE ||
					currentStep == EStepperTicket.PROCEED ||
					currentStep == EStepperTicket.PAYMENT
				"
			>
				{{ $t('module.newServiceTicket.action.cancel') }}
			</ACCDButton>
			<ACCDButton
				type="secondary"
				size="md"
				@click="onBack"
				v-if="
					currentStep == EStepperTicket.RECEIVE ||
					currentStep == EStepperTicket.SELL_PRICE
				"
			>
				{{ $t('module.newServiceTicket.action.save') }}
			</ACCDButton>

			<ACCDButton
				type="primary"
				size="md"
				@click="onSellPrice"
				v-if="currentStep == EStepperTicket.RECEIVE"
			>
				{{ $t('module.newServiceTicket.action.sellPrice') }}
			</ACCDButton>
			<ACCDButton
				type="primary"
				size="md"
				@click="onProceed"
				v-if="currentStep == EStepperTicket.SELL_PRICE"
				:disabled="isDisableProceed"
			>
				{{ $t('module.newServiceTicket.action.proceed') }}
			</ACCDButton>

			<ACCDButton
				type="secondary"
				size="md"
				@click="onPayment"
				v-if="currentStep == EStepperTicket.PROCEED"
			>
				{{ $t('module.newServiceTicket.action.changePrice') }}
			</ACCDButton>

			<ACCDButton
				type="primary"
				size="md"
				v-if="currentStep == EStepperTicket.PROCEED"
				@click="onCompleteCar"
			>
				{{ $t('module.newServiceTicket.action.done') }}
			</ACCDButton>

			<ACCDButton
				type="primary"
				size="md"
				v-if="currentStep == EStepperTicket.PAYMENT"
				@click="onHandOverCar"
			>
				{{ $t('module.newServiceTicket.action.handOver') }}
			</ACCDButton>
		</div>
	</div>

	<ServiceDialogBack
		v-model="confirmBack.visible"
		:id="confirmBack.id"
		:stepper="currentStep"
		:description="confirmBack.description"
		@close="handleCloseBack"
		@change-stepper="handleChangeStepper"
	>
	</ServiceDialogBack>

	<ServiceDialogCancel
		v-model="confirmCancel.visible"
		:id="confirmCancel.id"
		@close="handleCloseCancel"
	>
	</ServiceDialogCancel>
	<ServiceDialogConfirmSave
		v-model="confirmSave.visible"
		:id="confirmSave.id"
		:stepper="currentStep"
		:description="confirmSave.description"
		@close="handleCloseBack"
		@change-stepper="handleChangeStepper"
	></ServiceDialogConfirmSave>

	<ServiceDialogHandOverCar
		v-model="confirmHandOver.visible"
		:id="confirmHandOver.id"
		:stepper="currentStep"
		@close="confirmHandOver.visible = false"
	></ServiceDialogHandOverCar>

	<OutboundTicketForm
		v-if="outboundTicket.show"
		v-model="outboundTicket.show"
		:state="outboundTicket.action"
		:outboundTicketId="outboundTicket.id"
		@refresh="onRefreshDataService"
	></OutboundTicketForm>

	<ServiceForm
		v-if="openModalAddNewService"
		ref="serviceForm"
		v-model="openModalAddNewService"
		:state="'add-new'"
		@refresh="handleRefreshCreateNew"
		:serviceType="serviceType"
		:serviceName="serviceName"
		:instanceKeyRowService="instanceKeyRowService"
	></ServiceForm>
	<AccessaryForm></AccessaryForm>
</template>
<script setup lang="ts">
import { ISelectOption } from '@/types'

import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
	saveLogTracking,
} from '@/modules/sharedModules/api'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {
	EFormState,
	EFormConfirm,
	EStepperTicket,
	EStatusExport,
} from '@/enums'
import { useI18n } from '@/composables/useI18n'
import {
	getDetailOrder,
	updateQuotations,
	completeRepair,
	handOverCar,
	createDiagnoses,
	createQuotations,
	changeQuotationStatus,
} from '@/modules/newServiceTicket/api'
import { watch } from 'vue'
import { getCars } from '@/modules/car/api'
import { getCustomerTypes } from '@/modules/customerType/api'
import { getCustomersByGarageId } from '@/modules/sellingManagement/api'
import ServiceDialogBack from '@/modules/newServiceTicket/pages/ServiceDialogBack.vue'
import ServiceDialogCancel from '@/modules/newServiceTicket/pages/ServiceDialogCancel.vue'
import ServiceDialogConfirmSave from '@/modules/newServiceTicket/pages/ServiceDialogConfirmSave.vue'
import ServiceDialogHandOverCar from '@/modules/newServiceTicket/pages/ServiceDialogHandOverCar.vue'
import OutboundTicketForm from '@/modules/outboundTicket/pages/OutboundTicketForm.vue'
import ServiceForm from '@/modules/garageServiceController/pages/ServiceForm.vue'
import AccessaryForm from '@/modules/accessary/pages/AccessaryForm.vue'

import {
	formatPriceVN,
	preFilterInputNumberOnly,
} from '@/modules/sharedModules/component/constants'
import { cloneDeep } from 'lodash'
import {
	createListService,
	getListGarageServiceId,
	getListGarageServiceTypeId,
} from '@/modules/garageServiceController/api'
import { getGarageEmployees } from '@/modules/employee/api'
import { detailPriceByCustomerTypeAndProductAndService } from '@/modules/productPrice/api'
import { computed } from 'vue'
import { getParentProduct } from '@/modules/accessary/api'
import { $toast } from '@/main'
import { WEEK_DAYS, dayjs } from 'element-plus'
import router from '@/router'
import WrapFlexContainer from '@/modules/sharedModules/component/WrapFlexContainer.vue'
import TableContainer from '@/modules/sharedModules/component/TableContainer.vue'
import { CDAIcon } from '@cd/design-system'
import { emitter } from '@/utils/mitt'
import { IAccountInfo } from '@/types/Profile'

const { $t } = useI18n()

const formConfig = ref({
	show: false,
	action: EFormState.ADD as number,
	id: '',
})

const outboundTicket = ref({
	show: false,
	action: EFormState.ADD as number,
	id: '',
})

const confirmSave = ref({
	action: EFormConfirm.SAVE as number,
	visible: false,
	stepper: EStepperTicket.RECEIVE,
	id: '',
	description: '',
})

const confirmHandOver = ref({
	visible: false,
	stepper: EStepperTicket.RECEIVE,
	id: '',
})

const confirmBack = ref({
	action: EFormConfirm.SAVE as number,
	visible: false,
	stepper: EStepperTicket.RECEIVE,
	id: '',
	description: '',
})

const confirmCancel = ref({
	action: EFormConfirm.SAVE as number,
	visible: false,
	stepper: EStepperTicket.RECEIVE,
	id: '',
})
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

const productRowData = ref([] as (typeof baseProductRowData)[])
const oldProductRowData = ref([] as (typeof baseProductRowData)[])

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
	status: 2,
	outboundProductId: 0,
	disable: false,
	outboundTicketId: 0,
}

const stepItems = [
	{
		key: 1,
		title: 'Đã tiếp nhận xe',
	},
	{
		key: 2,
		title: 'Báo giá',
	},
	{
		key: 3,
		title: 'Đang tiến hành',
	},
	{
		key: 4,
		title: 'Thanh toán',
	},
	{
		key: 5,
		title: 'Bàn giao xe',
	},
]

const openModalAddNewService = ref<boolean>(false)
const serviceType = ref<number>(0)
const serviceName = ref<string>('')
const instanceKeyRowService = ref<number>(0)

const currentStep = ref<number>(0)

const route = useRoute()
const ticketID = ref<string>('')
const quotationID = ref<string>('')
const ticketStatus = ref<number>(0)
const isError = ref<boolean>(false)
const isSave = ref<boolean>(false)
const isDisableProceed = ref<boolean>(false)

const serviceTicketForm = ref({
	status: {
		value: 1,
		options: [
			{
				value: 0,
				label: $t('module.newServiceTicket.status.0'),
			},
			{
				value: 1,
				label: $t('module.newServiceTicket.status.1'),
			},
			{
				value: 2,
				label: $t('module.newServiceTicket.status.2'),
			},
			{
				value: 3,
				label: $t('module.newServiceTicket.status.3'),
			},
			{
				value: 4,
				label: $t('module.newServiceTicket.status.4'),
			},
			{
				value: 5,
				label: $t('module.newServiceTicket.status.5'),
			},
			{
				value: 6,
				label: $t('module.newServiceTicket.status.6'),
			},
			{
				value: 7,
				label: $t('module.newServiceTicket.status.7'),
			},
			{
				value: 8,
				label: $t('module.newServiceTicket.status.8'),
			},
		],
	},
	customerId: {
		value: null,
		options: [] as ISelectOption[],
		disabled: false,
	},
	customerPhone: {
		value: '',
		options: [] as (ISelectOption & { origin: any })[],
		disabled: false,
	},
	customerTypeId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	customerName: {
		value: '',
		disabled: false,
	},
	customerAddress: {
		value: '',
	},
	carId: {
		value: null,
		options: [] as ISelectOption[],
		disabled: false,
	},
	licensePlate: {
		value: '',
		options: [] as (ISelectOption & { origin: any })[],
		disabled: false,
	},
	vinNumber: {
		value: '',
		disabled: false,
	},
	odo: {
		value: '',
		disabled: false,
	},
	carBrandId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carModelId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carYearId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	carVersionId: {
		value: '',
		options: [] as ISelectOption[],
		disabled: false,
	},
	description: {
		value: '',
		disabled: false,
	},
	statusCar: {
		value: '',
		disabled: false,
	},
	code: '',
	carName: {
		value: '',
		disable: false,
	},
	appointmentDate: {
		value: '',
		disable: false,
	},
	expectedHandoverDate: {
		value: '',
		disable: false,
	},

	note: {
		value: '',
		disabled: false,
	},
})

// const serviceTicketForm = ref(cloneDeep(defaultServiceTicketForm));

const allCarOfGarageOptions = ref([])
const dataQuotation = ref({
	quotationLabours: [] as (typeof baseServiceRowData)[],
	quotationSpareParts: [] as (typeof baseProductRowData)[],
})

type CreateNewServiceType = {
	garageServiceId: string
	instanceKey: number
	garageServiceTypeId: number | string
}

const listCreateNewService = ref([] as CreateNewServiceType[])
const listResponseNewService = ref([] as CreateNewServiceType[])

const dateCancel = ref<string>('')
const discountType = ref(2)

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const handleChangePrint = async (type: number) => {
	const params = listCreateNewService.value.map((item) => {
		return {
			name: item.garageServiceId,
			garageServiceTypeId: item.garageServiceTypeId,
			instanceKey: item.instanceKey,
		}
	})

	const result = await createListService(params)

	listResponseNewService.value = result.data

	if (result.code == 1) {
		localStorage.setItem('dataPrint', JSON.stringify(dataPrint.value))
		localStorage.setItem('totalMoney', JSON.stringify(totalMoney.value))
		localStorage.setItem('intoMoney', JSON.stringify(intoMoney.value))
		localStorage.setItem('totalTax', JSON.stringify(totalTax.value))
		localStorage.setItem('totalDiscount', JSON.stringify(totalDiscount.value))
		localStorage.setItem(
			'ticketCode',
			JSON.stringify(serviceTicketForm.value.code)
		)

		if (type == 1) {
			saveLogTracking({
				logEvent: 'Click_PRINT_QUOTATION',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})
			let route = router.resolve({ path: '/app/quotation-sheet' })
			window.open(route.href, '_blank')
		} else if (type == 2) {
			saveLogTracking({
				logEvent: 'Click_PRINT_REPAIR_TICKET',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})
			let route = router.resolve({ path: '/app/repair-sheet' })
			window.open(route.href, '_blank')
		} else {
			saveLogTracking({
				logEvent: 'Click_PRINT_SERVICE_TICKET',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})
			let route = router.resolve({ path: '/app/newservice-sheet' })
			window.open(route.href, '_blank')
		}

		// listCreateNewService.value = []
	}
}

const getListGarageServiceIdByServiceTypeId = async (id: number | string) => {
	let listServiceId = []
	const res = await getListGarageServiceId({
		pageSize: 10000,
		pageNumber: 1,
	})

	if (res.code == 1) {
		listServiceId = res.data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
	}
	return listServiceId
}

watch(
	() => listResponseNewService.value,
	async () => {
		if (listResponseNewService.value.length > 0) {
			serviceRowData.value.forEach((item) => {
				const newValueLabel = item.garageServiceId.value
				if (Number(item.garageServiceId.value) !== item.garageServiceId.value) {
					const indexNewValueLabelonCreateNewServiceList =
						listCreateNewService.value.findIndex(
							(cNS) =>
								cNS.garageServiceId == (newValueLabel as unknown as string)
						)
					const idByNewValueLabel =
						listResponseNewService.value[
							indexNewValueLabelonCreateNewServiceList
						].garageServiceId

					item.garageServiceId.value = idByNewValueLabel as unknown as number
					item.garageServiceId.options = item.garageServiceId.options.filter(
						(a) => a.value == newValueLabel
					)
					onSelectServiceTypeId(item)
				}
			})
		}

		if (listResponseNewService.value.length > 0) {
			listResponseNewService.value = []
		}
		listCreateNewService.value = []
	},
	{ deep: true }
)

watch(
	() => currentStep.value,
	(val) => {
		currentStep.value = val
	}
)

watch(
	() => serviceTicketForm.value.carBrandId.value,
	(newVal: string) => {
		serviceTicketForm.value.carModelId.options = []
		if (newVal != null)
			getCarModelList(newVal).then((res) => {
				serviceTicketForm.value.carModelId.options = res.data?.data?.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
	}
)
watch(
	() => serviceTicketForm.value.carModelId.value,
	(newVal: string) => {
		serviceTicketForm.value.carYearId.options = []
		if (newVal != null)
			getCarYearList(newVal).then((res) => {
				serviceTicketForm.value.carYearId.options = res.data?.data?.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
	}
)
watch(
	() => serviceTicketForm.value.carYearId.value,
	(newVal: string) => {
		serviceTicketForm.value.carVersionId.options = []
		if (newVal != null)
			getCarVersionList(newVal).then((res) => {
				serviceTicketForm.value.carVersionId.options = res.data?.data?.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
	}
)

watch(
	() => serviceTicketForm.value.customerPhone.value,
	async (val) => {
		let originOptions = serviceTicketForm.value.customerPhone.options.find(
			(a) => {
				if (a.value == val) {
					return true
				}
			}
		)

		if (originOptions) {
			let origin = originOptions.origin
			serviceTicketForm.value.customerTypeId.value = origin.customerTypeId
			serviceTicketForm.value.customerName.value = origin.fullName
			serviceTicketForm.value.customerId.value = origin.id
			serviceTicketForm.value.customerTypeId.disabled = true
			serviceTicketForm.value.customerName.disabled = true

			serviceTicketForm.value.licensePlate.options =
				allCarOfGarageOptions.value.filter((car: any) => {
					return (
						car.origin.customerId == serviceTicketForm.value.customerId.value
					)
				})
		}
	}
)
watch(
	() => serviceTicketForm.value.licensePlate.value,
	async (val) => {
		let originOptions = serviceTicketForm.value.licensePlate.options.find(
			(a) => {
				if (a.value == val) {
					return true
				}
			}
		)

		if (originOptions) {
			serviceTicketForm.value.vinNumber.value = originOptions.origin.vinNumber
			serviceTicketForm.value.carBrandId.value = originOptions.origin.carBrandId
			serviceTicketForm.value.carModelId.value = originOptions.origin.carModelId
			serviceTicketForm.value.carYearId.value = originOptions.origin.carYearId
			serviceTicketForm.value.carVersionId.value =
				originOptions.origin.carVersionId
			serviceTicketForm.value.carId.value = originOptions.origin.id
		}
	}
)

watch(
	() => serviceTicketForm.value.statusCar.value,
	async (val) => {
		confirmBack.value.description = val
	}
)

watch(
	() => serviceTicketForm.value.statusCar.disabled,
	async (val) => {
		serviceTicketForm.value.statusCar.disabled = val
	}
)

watch(
	() => ticketStatus.value,
	(val) => {
		ticketStatus.value = val

		if (val == EStepperTicket.SELL_PRICE) {
			currentStep.value = EStepperTicket.RECEIVE
		} else if (val == EStepperTicket.PAYMENT) {
			currentStep.value = EStepperTicket.SELL_PRICE
		} else if (val == EStepperTicket.HAND_OVER) {
			currentStep.value = EStepperTicket.PROCEED
		} else if (val == EStepperTicket.COMPLETE) {
			currentStep.value = EStepperTicket.PAYMENT
		} else if (val == EStepperTicket.DONE) {
			currentStep.value = EStepperTicket.HAND_OVER
		} else if (val == EStepperTicket.CUSTOMER_CANCEL) {
			currentStep.value = EStepperTicket.CUSTOMER_CANCEL
		}
	}
)

watch(
	() => oldProductRowData.value,
	(val) => {
		oldProductRowData.value = val
	},
	{ deep: true }
)

watch(
	() => productRowData.value,
	(val) => {
		productRowData.value = val
		error.value.errorProductMsg = ''
		error.value.errorMsg = ''
	},
	{ deep: true }
)

const getCarByUser = (params: any) => {
	return getCars({ ...params, pageSize: 10000, pageNumber: 1 })
}
const getOptions = async () => {
	let carBrandListRes = await getCarBrandList()
	serviceTicketForm.value.carBrandId.options = carBrandListRes.data.data.map(
		(item: any) => {
			return {
				value: item.id,
				label: item.title,
			}
		}
	)
	let carByUserRes = await getCarByUser({})

	if (carByUserRes.code == 1) {
		allCarOfGarageOptions.value = carByUserRes.data.map((c: any) => {
			return {
				value: c.licensePlate,
				label: c.licensePlate,
				origin: c,
			}
		})
	}

	let customerRes = await getCustomerTypes({
		pageSize: 10000,
		pageNumber: 1,
	})
	serviceTicketForm.value.customerTypeId.options = customerRes.data.map(
		(a: any) => {
			return {
				value: a.id,
				label: a.customerTypeName,
			}
		}
	)
	let customerPhoneRes = await getCustomersByGarageId({
		pageSize: 10000,
		pageNumber: 1,
	})
	serviceTicketForm.value.customerPhone.options = customerPhoneRes.data.map(
		(a: any) => {
			return {
				value: a.phoneNumber,
				label: a.phoneNumber,
				origin: a,
			}
		}
	)
}

const listServiceIdOptions = ref([] as (ISelectOption & { origin: any })[])
const listServiceTypeIdOptions = ref([] as (ISelectOption & { origin: any })[])
const listGarageEmployees = ref([] as (ISelectOption & { origin: any })[])
const listGarageProducts = ref([] as (ISelectOption & { origin: any })[])

const serviceColumns = ref([
	{
		key: 'stt',
		headerName: $t('module.newServiceTicket.table.index'),
	},
	{
		key: 'garageServiceTypeId',
		headerName: $t('module.newServiceTicket.table.type'),
		minWidth: '280px',
	},
	{
		key: 'garageServiceId',
		headerName: $t('module.newServiceTicket.form.service.name'),
		minWidth: '300px',
	},

	{
		key: 'quantity',
		headerName: $t('module.newServiceTicket.form.service.quantity'),
		minWidth: '120px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.newServiceTicket.form.service.price'),
		minWidth: '150px',
	},
	{
		key: 'money',
		headerName: $t('module.newServiceTicket.form.service.money'),
		minWidth: '150px',
	},
	{
		key: 'discount',
		headerName: $t('module.newServiceTicket.form.service.discount'),
		minWidth: '150px',
	},
	{
		key: 'tax',
		headerName: $t('module.newServiceTicket.form.service.tax'),
		minWidth: '120px',
	},
	{
		key: 'total',
		headerName: $t('module.newServiceTicket.form.service.total'),
		minWidth: '150px',
	},
	{
		key: 'employeeId',
		headerName: $t('module.newServiceTicket.form.service.assignee'),
		minWidth: '200px',
	},
	{
		key: 'action',
		headerName: '',
	},
])

const serviceRowData = ref([] as (typeof baseServiceRowData)[])
const error = ref<{
	errorMsg: string
	errorServiceMsg: string
	errorProductMsg: string
	errorResponse: string
}>({
	errorMsg: '',
	errorServiceMsg: '',
	errorProductMsg: '',
	errorResponse: '',
})

watch(
	() => dataQuotation.value,
	(val) => {
		dataQuotation.value = val
	},
	{ deep: true }
)

watch(
	() => serviceRowData.value,
	(currentValue) => {
		dataQuotation.value.quotationLabours = currentValue
		dataPrint.value.serviceRowData = currentValue
		error.value.errorServiceMsg = ''
		error.value.errorResponse = ''
		error.value.errorMsg = ''
	},
	{ deep: true }
)

watch(
	() => productRowData.value,
	(currentValue) => {
		dataQuotation.value.quotationSpareParts = currentValue
		dataPrint.value.productRowData = currentValue
	},
	{ deep: true }
)

const removeQuotationSpareParts = ref([] as (typeof baseProductRowData)[])
const addQuotationSpareParts = ref([] as (typeof baseProductRowData)[])

const isChangePrice = ref<boolean>(false)
const tempServiceFormValue = ref(cloneDeep(serviceTicketForm.value))
const tempServiceRowData = ref([] as (typeof baseServiceRowData)[])
const tempProductRowData = ref([] as (typeof baseProductRowData)[])

const errorServiceMoney = ref<string>()
const errorProductMoney = ref<string>()

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
	otherCar: string
	appointmentDate: string
	expectedHandoverDate: string
	garageName: string
	garageAddress: string
	garagePhone: string
	note: string
	garageAvatar?: string
	discountType: number
	accountInfo?: IAccountInfo
}

const dataPrint = ref<BillProps>({
	customerName: '',
	phone: '',
	address: '',
	licensePlate: '',
	customerTypeId: '',
	description: '',
	statusCar: '',
	vinNumber: '',
	carModelId: '',
	carBrandId: '',
	carYearId: '',
	carVersionId: '',
	odo: '',
	serviceRowData: [],
	productRowData: [],
	otherCar: '',
	appointmentDate: '',
	expectedHandoverDate: '',
	garageName: '',
	garageAddress: '',
	garagePhone: '',
	note: '',
	garageAvatar: '',
	discountType: 2,
	accountInfo: {
		contacts: [],
		banks: [],
		taxCode: '',
		email: '',
		address: '',
	},
})

const isUpdateOrCreateQuotation = ref<number>(0)

watch(
	() => serviceTicketForm.value,
	() => {
		dataPrint.value.customerName = serviceTicketForm.value.customerName.value
		dataPrint.value.phone = serviceTicketForm.value.customerPhone.value
		dataPrint.value.licensePlate = serviceTicketForm.value.licensePlate.value
		dataPrint.value.address = serviceTicketForm.value.customerAddress.value

		const customerNameType =
			serviceTicketForm.value.customerTypeId.options.find(
				(item) => item.value == serviceTicketForm.value.customerTypeId.value
			)?.label

		dataPrint.value.customerTypeId = customerNameType
		dataPrint.value.statusCar = serviceTicketForm.value.statusCar.value
		dataPrint.value.description = serviceTicketForm.value.description.value
		dataPrint.value.vinNumber = serviceTicketForm.value.vinNumber.value
		dataPrint.value.carModelId =
			serviceTicketForm.value.carModelId.options.find(
				(item) => item.value == serviceTicketForm.value.carModelId.value
			)?.label

		dataPrint.value.carModelId =
			serviceTicketForm.value.carModelId.options.find(
				(item) => item.value == serviceTicketForm.value.carModelId.value
			)?.label

		dataPrint.value.carBrandId =
			serviceTicketForm.value.carBrandId.options.find(
				(item) => item.value == serviceTicketForm.value.carBrandId.value
			)?.label

		dataPrint.value.carYearId = serviceTicketForm.value.carYearId.options.find(
			(item) => item.value == serviceTicketForm.value.carYearId.value
		)?.label
		dataPrint.value.carVersionId =
			serviceTicketForm.value.carVersionId.options.find(
				(item) => item.value == serviceTicketForm.value.carVersionId.value
			)?.label
		dataPrint.value.odo = serviceTicketForm.value.odo.value
		dataPrint.value.otherCar = serviceTicketForm.value.carName.value
		dataPrint.value.appointmentDate =
			serviceTicketForm.value.appointmentDate.value

		dataPrint.value.expectedHandoverDate =
			serviceTicketForm.value.expectedHandoverDate.value
		dataPrint.value.note = serviceTicketForm.value.note.value
		dataPrint.value.discountType = discountType.value

		const garageAvatar = localStorage.getItem('avatar')
			? localStorage.getItem('avatar')
			: ''

		const garageName = JSON.parse(localStorage.getItem('garage') as string)
			? JSON.parse(localStorage.getItem('garage') as string).name
			: ''

		const garageAddress = JSON.parse(localStorage.getItem('garage') as string)
			? JSON.parse(localStorage.getItem('garage') as string).address
			: ''
		const garagePhone = JSON.parse(
			localStorage.getItem('garageOwner') as string
		)
			? JSON.parse(localStorage.getItem('garageOwner') as string).phone
			: ''

		dataPrint.value.garageAvatar = garageAvatar
		dataPrint.value.garageName = garageName
		dataPrint.value.garagePhone = garagePhone
		dataPrint.value.garageAddress = garageAddress
	},
	{ deep: true }
)

watch(
	() => isChangePrice.value,
	(val) => {
		isChangePrice.value = val
	}
)

const productColumns = ref([
	{
		key: 'stt',
		headerName: $t('module.newServiceTicket.table.index'),
		minWidth: '30px',
	},
	{
		key: 'productId',
		headerName: $t('module.serviceTicket.form.product.name'),
		minWidth: '300px',
	},
	{
		key: 'unit',
		headerName: $t('module.serviceTicket.form.product.unit'),
		minWidth: '80px',
	},
	{
		key: 'quantity',
		headerName: $t('module.serviceTicket.form.product.quantity'),
		minWidth: '120px',
	},
	{
		key: 'unitPrice',
		headerName: $t('module.serviceTicket.form.product.price'),
		minWidth: '150px',
	},
	{
		key: 'money',
		headerName: $t('module.serviceTicket.form.product.money'),
		minWidth: '80px',
	},
	{
		key: 'discount',
		headerName: $t('module.serviceTicket.form.product.discount'),
		minWidth: '150px',
	},
	{
		key: 'tax',
		headerName: $t('module.serviceTicket.form.product.tax'),
		minWidth: '120px',
	},
	{
		key: 'total',
		headerName: $t('module.serviceTicket.form.product.total'),
		minWidth: '100px',
	},
	{
		key: 'status',
		headerName: $t('module.newServiceTicket.form.product.status'),
		minWidth: '150px',
	},
	{
		key: 'action',
		headerName: '',
	},
])

const totalMoney = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result += Number(r.total)
	})
	productRowData.value.forEach((r) => {
		result += Number(r.total)
	})

	return Number(result * 1000).toFixed(0)
})

const intoMoney = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result += Number(r.money)
	})
	productRowData.value.forEach((r) => {
		result += Number(r.money)
	})

	return Number(result * 1000).toFixed(0)
})

const totalTax = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		console.log(r)
		result +=
			discountType.value == 1
				? (r.tax / 100) * (r.money - r.discount)
				: (r.tax / 100) * r.money * (1 - r.discount / 100)
	})
	productRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? (r.tax / 100) * (r.money - r.discount)
				: (r.tax / 100) * r.money * (1 - r.discount / 100)
	})

	return Number(result * 1000).toFixed(0)
})

const totalDiscount = computed(() => {
	let result = 0
	serviceRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? Number(r.discount)
				: Number((r.money * r.discount) / 100)
	})
	productRowData.value.forEach((r) => {
		result +=
			discountType.value == 1
				? Number(r.discount)
				: Number((r.money * r.discount) / 100)
	})

	return Number(result * 1000).toFixed(0)
})

await getOptions()
await getListGarageServiceId({ pageSize: 10000, pageNumber: 1 }).then((res) => {
	listServiceIdOptions.value = res.data.map((s: any) => {
		return {
			value: s.id,
			label: s.name,
			origin: s,
		} as ISelectOption & { origin: any }
	})
})
await getListGarageServiceTypeId().then((res) => {
	listServiceTypeIdOptions.value = res.data.map((s: any) => {
		return {
			value: s.id,
			label: s.name,
			origin: s,
		} as ISelectOption & { origin: any }
	})
})
await getGarageEmployees({
	pageSize: 10000,
	pageNumber: 1,
}).then((res) => {
	listGarageEmployees.value = res.data.map((e: any) => {
		return {
			value: e.id,
			label: e.fullName,
		}
	})
})
await getParentProduct({
	pageSize: 10000,
	pageNumber: 1,
}).then((res) => {
	listGarageProducts.value = res.data.map((e: any) => {
		return {
			value: e.id,
			label: e.code + ' - ' + e.name,
			origin: e,
		}
	})
})

const getDetail = async () => {
	await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
	}).then((res) => {
		listGarageProducts.value = res.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	})

	await getListGarageServiceId({ pageSize: 10000, pageNumber: 1 }).then(
		(res) => {
			listServiceIdOptions.value = res.data.map((s: any) => {
				return {
					value: s.id,
					label: s.name,
					origin: s,
				} as ISelectOption & { origin: any }
			})
		}
	)

	ticketID.value = route.params.id.toString()
	confirmBack.value.id = ticketID.value
	confirmCancel.value.id = ticketID.value
	confirmSave.value.id = ticketID.value

	if (ticketID.value) {
		formConfig.value.show = true
		formConfig.value.action = EFormState.VIEW
		const res = await getDetailOrder(ticketID.value)
		isUpdateOrCreateQuotation.value = res.data.quotation.status
		discountType.value = res.data.quotation.discountType
		quotationID.value = res.data.quotation.id
		if (res.data.quotation.quotationLabours) {
			serviceRowData.value = res.data.quotation.quotationLabours.map(
				(a: any, index: number) => {
					let employeeId = 0
					listGarageEmployees.value.some((e) => {
						if (a.employeeName == e.label) {
							employeeId = e.value
							return true
						}
					})

					return {
						garageServiceId: {
							value: a.garageServiceId,
							options: listServiceIdOptions.value,
						},
						garageServiceTypeId: {
							value: a.garageServiceTypeId,
							options: listServiceTypeIdOptions.value,
						},
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						employeeId: {
							value: employeeId,
							options: listGarageEmployees.value,
						},
						instanceKey: index,
						price: a.price / 1000,
						disable: a.outboundProductId == 0 ? true : false,
					}
				}
			)
		}

		if (res.data.quotation.quotationSpareParts) {
			productRowData.value = res.data.quotation.quotationSpareParts.map(
				(a: any, index: number) => {
					const temp = [
						...listGarageProducts.value,
						{ value: a.productName, label: a.productName },
					]

					return {
						productId: {
							value: a.productId ? a.productId : a.productName,
							options: a.productName ? temp : listGarageProducts.value,
						},
						unit: a.unit,
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						price: a.price / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						instanceKey: index,
						// status:
						//     a.status == 1
						//         ? EStatusExport.EXPORTED_1
						//         : EStatusExport.UN_EXPORTED_1,
						status: a.status,
						outboundProductId: a.outboundProductId,
						disable: a.outboundProductId == 0 ? false : true,
						outboundTicketId: a.outboundTicketId,
					}
				}
			)
		}

		if (res.data.quotation.oldQuotationSpareParts) {
			oldProductRowData.value = res.data.quotation.oldQuotationSpareParts.map(
				(a: any, index: number) => {
					return {
						productId: {
							value: a.productId,
							options: listGarageProducts.value,
						},
						unit: a.unit,
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						price: a.price / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						total:
							((Number(a.quantity) * Number(a.unitPrice) - Number(a.discount)) *
								(1 + a.tax)) /
							1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount / 100,
						instanceKey: index,
						// status:
						//     a.status == 1
						//         ? EStatusExport.EXPORTED_1
						//         : EStatusExport.UN_EXPORTED_1,
						status: a.status,
						outboundProductId: a.outboundProductId,
						outboundTicketId: a.outboundTicketId,
					}
				}
			)
			isChangePrice.value = true
		}

		ticketStatus.value = res.data.status
	}
}

onMounted(async () => {
	error.value.errorProductMsg = ''
	error.value.errorResponse = ''
	error.value.errorServiceMsg = ''

	ticketID.value = route.params.id.toString()
	confirmBack.value.id = ticketID.value
	confirmCancel.value.id = ticketID.value
	confirmSave.value.id = ticketID.value
	if (ticketID.value) {
		formConfig.value.show = true
		formConfig.value.action = EFormState.VIEW
		const res = await getDetailOrder(ticketID.value)

		serviceTicketForm.value.customerPhone.value =
			res.data.customerResponse.phoneNumber
		serviceTicketForm.value.customerTypeId.value =
			res.data.customerResponse.customerTypeId
		serviceTicketForm.value.customerName.value =
			res.data.customerResponse.fullName

		serviceTicketForm.value.customerAddress.value =
			res.data.customerResponse.address
		serviceTicketForm.value.licensePlate.value =
			res.data.carResponse.licensePlate
		serviceTicketForm.value.odo.value = res.data.odo
		serviceTicketForm.value.description.value = res.data.description
		serviceTicketForm.value.carYearId.value =
			res.data.carResponse.carYearIdparams
		serviceTicketForm.value.statusCar.value = res.data.diagnose.description
		serviceTicketForm.value.code = res.data.code
		serviceTicketForm.value.carName.value = res.data.carResponse.carName
		serviceTicketForm.value.note.value = res.data.note
		serviceTicketForm.value.appointmentDate.value = dayjs(
			res.data.appointmentDate
		).format('DD-MM-YYYY')
		serviceTicketForm.value.expectedHandoverDate.value = res.data
			?.expectedHandoverDate
			? dayjs(res.data.expectedHandoverDate).format('DD-MM-YYYY')
			: ''
		serviceTicketForm.value.appointmentDate.disable = true
		serviceTicketForm.value.expectedHandoverDate.disable = true

		quotationID.value = res.data.quotation.id
		dateCancel.value = dayjs(res.data.appointmentDate).format('DD-MM-YYYY')
		isUpdateOrCreateQuotation.value = res.data.quotation.status
		discountType.value = res.data.quotation.discountType
			? res.data.quotation.discountType
			: 2
		if (res.data.quotation.quotationLabours) {
			serviceRowData.value = res.data.quotation.quotationLabours.map(
				(a: any, index: number) => {
					let employeeId = 0
					listGarageEmployees.value.some((e) => {
						if (a.employeeName == e.label) {
							employeeId = e.value
							return true
						}
					})

					return {
						garageServiceId: {
							value: a.garageServiceId,
							options: listServiceIdOptions.value,
						},
						garageServiceTypeId: {
							value: a.garageServiceTypeId,
							options: listServiceTypeIdOptions.value,
						},
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						tax: a.tax * 100,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						employeeId: {
							value: employeeId,
							options: listGarageEmployees.value,
						},
						instanceKey: index,
						price: a.price / 1000,
						disable: a.outboundProductId == 0 ? true : false,
					}
				}
			)
		}

		if (res.data.quotation.quotationSpareParts) {
			productRowData.value = res.data.quotation.quotationSpareParts.map(
				(a: any, index: number) => {
					const temp = [
						...listGarageProducts.value,
						{ value: a.productName, label: a.productName },
					]

					return {
						productId: {
							value: a.productId ? a.productId : a.productName,
							options: a.productName ? temp : listGarageProducts.value,
						},
						unit: a.unit,
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						price: a.price / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						total:
							discountType.value == 1
								? ((Number(a.quantity) * Number(a.unitPrice) -
										Number(a.discount)) *
										(1 + a.tax)) /
								  1000
								: (Number(a.quantity) *
										Number(a.unitPrice) *
										(1 - a.discount) *
										(1 + a.tax)) /
								  1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						instanceKey: index,
						// status:
						//     a.status == 1
						//         ? EStatusExport.EXPORTED_1
						//         : EStatusExport.UN_EXPORTED_1,
						status: a.status,
						outboundProductId: a.outboundProductId,
						disable: a.outboundProductId == 0 ? false : true,
						outboundTicketId: a.outboundTicketId,
					}
				}
			)
		}

		if (res.data.quotation.oldQuotationSpareParts) {
			oldProductRowData.value = res.data.quotation.oldQuotationSpareParts.map(
				(a: any, index: number) => {
					return {
						productId: {
							value: a.productId,
							options: listGarageProducts.value,
						},
						unit: a.unit,
						quantity: a.quantity,
						unitPrice: a.unitPrice / 1000,
						price: a.price / 1000,
						money: (Number(a.quantity) * Number(a.unitPrice)) / 1000,
						total:
							((Number(a.quantity) * Number(a.unitPrice) - Number(a.discount)) *
								(1 + a.tax)) /
							1000,
						tax: a.tax * 100,
						discount:
							discountType.value == 1 ? a.discount * 1000 : a.discount * 100,
						instanceKey: index,
						// status:
						//     a.status == 1
						//         ? EStatusExport.EXPORTED_1
						//         : EStatusExport.UN_EXPORTED_1,
						status: a.status,
						outboundProductId: a.outboundProductId,
						outboundTicketId: a.outboundTicketId,
					}
				}
			)
			isChangePrice.value = true
		}

		ticketStatus.value = res.data.status
		tempServiceFormValue.value = cloneDeep(serviceTicketForm.value)
		tempServiceRowData.value = cloneDeep(serviceRowData.value)
		tempProductRowData.value = cloneDeep(productRowData.value)
		handleGetAccountInfo()
	}
})

emitter.on('on-change-data', async (data: any) => {
	let listServiceTypeId: any[] = []
	let listServiceId: any[] = []

	const result = await getListGarageServiceTypeId()
	if (result.code == 1) {
		listServiceTypeId = result.data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
	}

	const res = await getListGarageServiceId({
		pageSize: 10000,
		pageNumber: 1,
		garageServiceTypeId: data.garageServiceTypeId,
	})

	if (res.code == 1) {
		listServiceId = res.data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
	}

	serviceRowData.value = serviceRowData.value.map((item) => {
		return {
			...item,
			garageServiceTypeId: {
				options: listServiceTypeId,
				value:
					item.instanceKey == data.intanceKey
						? data.garageServiceTypeId
						: item.garageServiceTypeId.value,
			},

			garageServiceId: {
				value:
					item.instanceKey == data.intanceKey
						? data.garageServiceId
						: item.garageServiceId.value,
				options:
					item.instanceKey == data.intanceKey
						? listServiceId
						: item.garageServiceId.options,
			},
		}
	})
})

emitter.on('on-remove-service', async (data: any) => {
	let listServiceTypeId: any[] = []
	let listServiceId: any[] = []

	const result = await getListGarageServiceTypeId()
	if (result.code == 1) {
		listServiceTypeId = result.data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
	}

	const res = await getListGarageServiceId({
		pageSize: 10000,
		pageNumber: 1,
		garageServiceTypeId: data.serviceName ? data.serviceType : null,
	})

	if (res.code == 1) {
		listServiceId = res.data.map((s: any) => {
			return {
				value: s.id,
				label: s.name,
				origin: s,
			} as ISelectOption & { origin: any }
		})
	}
	serviceRowData.value = serviceRowData.value.map((item) => {
		if (item.instanceKey == data.instanceKeyRowService) {
			return {
				...item,
				garageServiceTypeId: {
					options: listServiceTypeId,
					value: data.serviceName ? item.garageServiceTypeId.value : 0,
				},

				garageServiceId: {
					options: listServiceId,
					value: 0,
				},
				quantity: 0 as number,
				unitPrice: 0 as number,
				money: 0 as number,
				discount: 0,
				tax: 0,
				total: 0,
				employeeId: {
					value: 0,
					options: item.employeeId.options,
				},
				price: 0,
			}
		}

		return {
			...item,
		}
	})
})

emitter.on('on-change-data-product', async (data: any) => {
	let listProduct: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
	})

	if (result.code == 1) {
		listProduct = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}

	productRowData.value = productRowData.value.map((item) => {
		return {
			...item,
			productId: {
				value:
					item.instanceKey == data.intanceKey
						? data.productId
						: item.productId.value,
				options: listProduct,
			},
		}
	})

	const updateRow = productRowData.value.find(
		(item) => item.instanceKey == data.intanceKey
	)

	if (updateRow) {
		await onSelectProduct(updateRow)
	}
})

emitter.on('on-remove-product', async (product: any) => {
	let listProduct: any[] = []
	const result = await getParentProduct({
		pageSize: 10000,
		pageNumber: 1,
	})

	if (result.code == 1) {
		listProduct = result.data.map((e: any) => {
			return {
				value: e.id,
				label: e.code + ' - ' + e.name,
				origin: e,
			}
		})
	}

	productRowData.value = productRowData.value.map((item) => {
		if (item.instanceKey == product.intanceKey) {
			return {
				...item,
				productId: {
					options: listProduct,
					value: 0,
				},
				quantity: 0 as number,
				unitPrice: 0 as number,
				money: 0 as number,
				discount: 0,
				tax: 0,
				total: 0,
				price: 0,
			}
		}
		return { ...item }
	})
})

watch(
	() => serviceRowData.value,
	() => {},
	{ deep: true }
)

watch(
	() => productRowData.value,
	() => {},
	{ deep: true }
)

const onChangeRowProduct = (
	row: typeof baseProductRowData,
	field: keyof typeof baseProductRowData
) => {
	let sRow = productRowData.value.find((r) => {
		return r.instanceKey == row.instanceKey
	})
	if (sRow) {
		console.log(sRow)
		let val = Number(sRow[field])
		sRow.money = sRow.unitPrice * sRow.quantity
		sRow.total =
			discountType.value == 1
				? (sRow.unitPrice * sRow.quantity - row.discount) * (1 + sRow.tax / 100)
				: sRow.unitPrice *
				  sRow.quantity *
				  (1 - sRow.discount / 100) *
				  (1 + sRow.tax / 100)
		if (sRow.total < 0) {
			errorProductMoney.value = $t(
				'module.newServiceTicket.error.errorPriceMessage'
			)
			isDisableProceed.value = true
		} else {
			errorProductMoney.value = ''
			isDisableProceed.value = false
		}
	}
}
const onSelectProduct = (row: typeof baseProductRowData) => {
	let sRow = productRowData.value.find((r) => {
		return r.instanceKey == row.instanceKey
	})

	console.log('sRow', sRow)

	if (sRow) {
		let product = sRow.productId.options.find((o) => {
			return o.value == row.productId.value
		})?.origin
		row.quantity = 1

		if (product && product.id) {
			detailPriceByCustomerTypeAndProductAndService(
				serviceTicketForm.value.customerTypeId.value,
				product.id,
				1
			).then((res) => {
				row.unitPrice = res.data.price / 1000
				row.total = res.data.price / 1000
				row.money = res.data.price / 1000
				row.total = res.data.price / 1000
			})
			row.unit = product.unit

			row.discount = 0
		}
	}
}

const addProductRowData = () => {
	if (currentStep.value == EStepperTicket.PROCEED) return
	let rowData = cloneDeep(baseProductRowData)
	rowData.productId.options = listGarageProducts.value
	;(rowData.instanceKey =
		productRowData.value.length > 0
			? productRowData.value[productRowData.value.length - 1].instanceKey + 1
			: 0),
		productRowData.value.push(rowData)
}
const deleteProductRow = (row: typeof baseProductRowData) => {
	if (currentStep.value == EStepperTicket.PROCEED) return

	productRowData.value = productRowData.value.filter((sRow) => {
		return sRow.instanceKey != row.instanceKey
	})
}

const addServiceRowData = () => {
	if (currentStep.value == EStepperTicket.PROCEED) return
	let rowData = cloneDeep(baseServiceRowData)
	rowData.garageServiceId.options = listServiceIdOptions.value
	rowData.garageServiceTypeId.options = listServiceTypeIdOptions.value
	rowData.employeeId.options = listGarageEmployees.value
	;(rowData.instanceKey =
		serviceRowData.value.length > 0
			? serviceRowData.value[serviceRowData.value.length - 1].instanceKey + 1
			: 0),
		serviceRowData.value.push(rowData)
}

const deleteServiceRow = (row: typeof baseServiceRowData) => {
	if (currentStep.value == EStepperTicket.PROCEED) return
	serviceRowData.value = serviceRowData.value.filter((sRow) => {
		return sRow.instanceKey != row.instanceKey
	})
	listCreateNewService.value = listCreateNewService.value.filter((sRow) => {
		return sRow.instanceKey != row.instanceKey
	})
}

const onSelectServiceTypeId = async (row: typeof baseServiceRowData) => {
	let sRow = serviceRowData.value.find((r) => {
		return r.instanceKey == row.instanceKey
	})

	if (sRow) {
		let service = sRow.garageServiceTypeId.options.find((o) => {
			return o.value == row.garageServiceTypeId.value
		})?.origin
		if (listCreateNewService.value.length == 0) {
			row.garageServiceId.value = 0
		}
		if (service && service.id) {
			await getListGarageServiceId({
				pageSize: 10000,
				pageNumber: 1,
				garageServiceTypeId: service.id,
			}).then((res) => {
				row.garageServiceId.options = res.data.map((s: any) => {
					return {
						value: s.id,
						label: s.name,
						origin: s,
					} as ISelectOption & { origin: any }
				})
			})
		}
	}
}

const onSelectServiceId = async (
	row: typeof baseServiceRowData,
	newService?: any
) => {
	let sRow = serviceRowData.value.find((r) => {
		return r.instanceKey == row.instanceKey
	})

	if (sRow) {
		let service = sRow.garageServiceId.options.find((o) => {
			return o.value == row.garageServiceId.value
		})?.origin
		row.quantity = 1

		if (service && service.id) {
			await detailPriceByCustomerTypeAndProductAndService(
				serviceTicketForm.value.customerTypeId.value,
				service.id,
				2
			).then((res) => {
				row.unitPrice = res.data.price / 1000
				row.money = res.data.price / 1000
				row.total = res.data.price / 1000
			})
		}
		row.discount = 0

		if (newService) {
			row.garageServiceId.value = newService

			if (typeof newService == 'string') {
				listCreateNewService.value.push({
					garageServiceId: newService,
					instanceKey: row.instanceKey,
					garageServiceTypeId: row.garageServiceTypeId.value,
				})
			}
		}
	}
}

const onChangeRowService = (
	row: typeof baseServiceRowData,
	field: keyof typeof baseServiceRowData
) => {
	let sRow = serviceRowData.value.find((r) => {
		return r.instanceKey == row.instanceKey
	})

	if (sRow) {
		let val = Number(sRow[field])
		sRow.money = sRow.unitPrice * sRow.quantity

		let total =
			discountType.value == 1
				? Number(
						(sRow.unitPrice * sRow.quantity - sRow.discount) *
							(1 + sRow.tax / 100)
				  ).toFixed(0)
				: Number(
						sRow.unitPrice *
							sRow.quantity *
							(1 - sRow.discount / 100) *
							(1 + sRow.tax / 100)
				  ).toFixed(0)

		sRow.total = +total

		if (sRow.total < 0) {
			errorServiceMoney.value = $t(
				'module.newServiceTicket.error.errorPriceMessage'
			)
			isDisableProceed.value = true
		} else {
			errorServiceMoney.value = ''
			isDisableProceed.value = false
		}
	}
}

const onCancel = async () => {
	confirmCancel.value.visible = true
}

const onBack = async () => {
	isSave.value = true

	if (currentStep.value == EStepperTicket.RECEIVE) {
		const params = {
			description: serviceTicketForm.value.statusCar.value,
		}
		const res = await createDiagnoses(params, ticketID.value, false)
		if (res.code == 1) {
			$toast($t('module.newServiceTicket.form.toast.saveChange'), true)
		}
	} else if (currentStep.value == EStepperTicket.SELL_PRICE) {
		await handleChangeStepper(currentStep.value)
	} else {
		// setTimeout(() => {
		// 	window.location.reload()
		// }, 1000)
	}
}

const onSellPrice = async () => {
	// if (!serviceTicketForm.value.statusCar.value) {
	// 	isError.value = true
	// 	return
	// } else {
	// 	isError.value = false
	// }

	const params = { description: serviceTicketForm.value.statusCar.value }

	const res = await createDiagnoses(params, ticketID.value, true)

	if (res.code == 1) {
		$toast($t('module.newServiceTicket.form.toast.quotationSuccess'), true)
		await getDetail()
		currentStep.value = EStepperTicket.SELL_PRICE
		serviceTicketForm.value.statusCar.disabled = true
		confirmBack.value.stepper = EStepperTicket.SELL_PRICE
		confirmCancel.value.stepper = EStepperTicket.SELL_PRICE
	} else {
		$toast(res.message, false)
	}
}

const parseQuotationSpareParts = (array: (typeof baseProductRowData)[]) => {
	console.log('parseQuotationSpareParts', array)
	const res = array
		.filter((item) => item.productId.value)
		.map((item) => {
			if (item.productId.value) {
				if (typeof item.productId.value == 'string') {
					return {
						productName: item.productId.value,
						productId: null,
						unit: item.unit,
						quantity: item.quantity != null ? item.quantity : 0,
						unitPrice: item.unitPrice * 1000,
						discount:
							discountType.value == 1
								? item.discount * 1000
								: item.discount / 100,
						tax: item.tax / 100,
						status: item.status,
						outboundProductId: item.outboundProductId,
						outboundTicketId: item.outboundTicketId,
					}
				} else {
					return {
						productId: item.productId.value,
						unit: item.unit,
						quantity: item.quantity != null ? item.quantity : 0,
						unitPrice: item.unitPrice * 1000,
						discount:
							discountType.value == 1
								? item.discount * 1000
								: item.discount / 100,
						tax: item.tax / 100,
						status: item.status,
						outboundProductId: item.outboundProductId,
						outboundTicketId: item.outboundTicketId,
					}
				}
			}
		})
	return res
}

function getDifference(array1: any, array2: any) {
	return array1.filter((object1: any) => {
		return !array2.some((object2: any) => {
			return object1.outboundProductId == object2.outboundProductId
		})
	})
}

const onProceed = async () => {
	serviceRowData.value.forEach((item) => {
		if (item.quantity == 0) {
			error.value.errorServiceMsg = ''
			error.value.errorServiceMsg = $t(
				`module.newServiceTicket.error.emptyQuantity`
			)
		}
		if (!item.garageServiceTypeId.value) {
			error.value.errorServiceMsg = ''
			error.value.errorServiceMsg = $t(
				`module.newServiceTicket.error.wrongFormat`
			)
		}
		if (!item.garageServiceId.value) {
			error.value.errorServiceMsg = ''
			error.value.errorServiceMsg = $t(
				`module.newServiceTicket.error.emptyTypeService`
			)
		}
	})

	if (productRowData.value.length <= 0 && serviceRowData.value.length <= 0) {
		error.value.errorMsg = ''
		error.value.errorMsg = $t(`module.newServiceTicket.error.errorMsg`)
	}

	productRowData.value.forEach((item) => {
		if (item.quantity == 0 || item.quantity == null) {
			error.value.errorProductMsg = ''
			error.value.errorProductMsg = $t(
				`module.newServiceTicket.error.emptyQuantity`
			)
		}

		if (!item.productId.value) {
			error.value.errorProductMsg = ''
			error.value.errorProductMsg = $t(
				'module.newServiceTicket.error.emptyProduct'
			)
		}
	})

	if (
		error.value.errorMsg ||
		error.value.errorServiceMsg ||
		error.value.errorProductMsg ||
		error.value.errorResponse
	) {
		return 0
	}
	const quotationLabours = serviceRowData.value.map((item) => {
		if (typeof item.garageServiceId.value == 'string') {
			return {
				garageServiceTypeId: item.garageServiceTypeId.value,
				garageServiceId: null,
				employeeId: item.employeeId.value,
				garageServiceName: item.garageServiceId.value,
				unitPrice: item.unitPrice * 1000,
				quantity: item.quantity != null ? item.quantity : 0,
				discount:
					discountType.value == 1 ? item.discount * 1000 : item.discount / 100,
				tax: item.tax / 100,
				price: item.price * 1000,
			}
		}

		return {
			garageServiceTypeId: item.garageServiceTypeId.value,
			garageServiceId: item.garageServiceId.value,
			employeeId: item.employeeId.value,
			garageServiceName: listServiceIdOptions.value.find(
				(e) => e.value == item.garageServiceId.value
			)?.label,
			unitPrice: item.unitPrice * 1000,
			quantity: item.quantity != null ? item.quantity : 0,
			discount:
				discountType.value == 1 ? item.discount * 1000 : item.discount / 100,
			tax: item.tax / 100,
			price: item.price * 1000,
		}
	})

	const quotationSpareParts = parseQuotationSpareParts(productRowData.value)

	// const intersectionData = getIntersection('outboundProductId', productRowData.value, oldProductRowData.value)
	const intersectionData: any[] = []
	productRowData.value.map((el1) => {
		oldProductRowData.value.map((el2) => {
			if (el1.outboundProductId == el2.outboundProductId) {
				intersectionData.push(el1)
			}
		})
	})
	const add = [
		...getDifference(intersectionData, productRowData.value),
		...getDifference(productRowData.value, intersectionData),
	]
	const remove = [
		...getDifference(intersectionData, oldProductRowData.value),
		...getDifference(oldProductRowData.value, intersectionData),
	]

	const addQuotationSpareParts = parseQuotationSpareParts(add)
	const removeQuotationSpareParts = parseQuotationSpareParts(remove)

	if (isUpdateOrCreateQuotation.value) {
		const params = {
			quotationLabours: [...quotationLabours],
			quotationSpareParts: [...quotationSpareParts],
			addQuotationSpareParts: [...addQuotationSpareParts],
			removeQuotationSpareParts: [...removeQuotationSpareParts],
			note: serviceTicketForm.value.note.value || '',
		}

		const res = await updateQuotations(
			params,
			ticketID.value,
			quotationID.value,
			true
		)
		if (res.code == 1) {
			currentStep.value = EStepperTicket.PROCEED
			serviceTicketForm.value.statusCar.disabled = true
			serviceTicketForm.value.note.disabled = true
			confirmBack.value.stepper = EStepperTicket.PROCEED

			$toast($t('module.newServiceTicket.form.toast.repaireSuccess'), true)
			await getDetail()
		} else if (res.error == 500) {
			return 0
		} else {
			$toast(res.message, false)
		}
	} else {
		const params = {
			quotationLabours: [...quotationLabours],
			quotationSpareParts: [...quotationSpareParts],
			note: serviceTicketForm.value.note.value || '',
		}

		const res = await createQuotations(
			params,
			ticketID.value,
			true,
			discountType.value ? discountType.value : 2
		)
		if (res.code == 1) {
			saveLogTracking({
				logEvent: 'Click_PROCEED_REPAIR_ORDER',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})
			currentStep.value = EStepperTicket.PROCEED
			serviceTicketForm.value.statusCar.disabled = true
			serviceTicketForm.value.note.disabled = true
			confirmBack.value.stepper = EStepperTicket.PROCEED
			quotationID.value = res.data
			$toast($t('module.newServiceTicket.form.toast.repaireSuccess'), true)
			await getDetail()
		} else {
			$toast(res.message || res.msg, false)
		}
	}
}

const onPayment = async () => {
	currentStep.value = EStepperTicket.SELL_PRICE
	const res = await changeQuotationStatus(ticketID.value)

	if (res.code == 1) {
		isChangePrice.value = true
		await getDetail()
		$toast($t('module.newServiceTicket.form.toast.changeQuotation'), true)
	} else {
		$toast(res.message, false)
	}
}
const onCompleteCar = async () => {
	const res = await completeRepair(ticketID.value)

	if (res.code == 1) {
		currentStep.value = EStepperTicket.PAYMENT
		serviceTicketForm.value.statusCar.disabled = true
		await getDetail()
		$toast($t('module.newServiceTicket.form.toast.completeSuccess'), true)
	} else {
		$toast(res.message, false)
	}
}

const onHandOverCar = async () => {
	const res = await handOverCar(ticketID.value)

	if (res.code == 1) {
		currentStep.value = EStepperTicket.HAND_OVER
		serviceTicketForm.value.statusCar.disabled = true

		setTimeout(() => {
			confirmHandOver.value.visible = true
			confirmHandOver.value.id = ticketID.value
		}, 1000)
	} else {
		$toast(res.message, false)
	}
}

const handleOpenOutbound = (id: string) => {
	outboundTicket.value.show = true
	outboundTicket.value.id = id
	outboundTicket.value.action = EFormState.EDIT
}

const handleCloseBack = () => {
	confirmSave.value.visible = false
}

const handleCloseCancel = () => {
	confirmCancel.value.visible = false
}

function compareArraysByStringify(arr1: any, arr2: any) {
	return JSON.stringify(arr1) === JSON.stringify(arr2)
}

const handleConfirmSave = () => {
	isSave.value = false
	if (currentStep.value == EStepperTicket.RECEIVE) {
		if (
			serviceTicketForm.value.statusCar.value != null &&
			tempServiceFormValue.value.statusCar.value !==
				serviceTicketForm.value.statusCar.value
		) {
			confirmSave.value.visible = true
			confirmSave.value.id = ticketID.value
			confirmSave.value.description = serviceTicketForm.value.statusCar.value
		} else {
			confirmSave.value.visible = false
			router.back()
		}
	} else if (currentStep.value == EStepperTicket.SELL_PRICE) {
		const isDiffServiceRowData = compareArraysByStringify(
			tempServiceRowData.value,
			serviceRowData.value
		)
		const isDiffProductRowData = compareArraysByStringify(
			tempProductRowData.value,
			productRowData.value
		)
		if (
			!isDiffServiceRowData ||
			!isDiffProductRowData ||
			(serviceTicketForm.value.note.value != null &&
				tempServiceFormValue.value.note.value !==
					serviceTicketForm.value.note.value)
		) {
			confirmSave.value.visible = true
			confirmSave.value.id = ticketID.value
		} else {
			confirmSave.value.visible = false
			router.back()
		}
	} else {
		confirmSave.value.visible = false
		router.back()
	}
}

const handleChangeStepper = async (stepper: number) => {
	currentStep.value = stepper
	const quotationLabours = serviceRowData.value
		.filter((item) => item.garageServiceId.value)
		.map((item) => {
			if (typeof item.garageServiceId.value == 'string') {
				return {
					garageServiceTypeId: item.garageServiceTypeId.value,
					garageServiceId: null,
					employeeId: item.employeeId.value,
					garageServiceName: item.garageServiceId.value,
					unitPrice: item.unitPrice * 1000,
					quantity: item.quantity != null ? item.quantity : 0,
					discount:
						discountType.value == 1
							? item.discount * 1000
							: item.discount / 100,
					tax: item.tax / 100,
					price: item.price * 1000,
				}
			}

			return {
				garageServiceTypeId: item.garageServiceTypeId.value,
				garageServiceId: item.garageServiceId.value,
				employeeId: item.employeeId.value,
				garageServiceName: listServiceIdOptions.value.find(
					(e) => e.value == item.garageServiceId.value
				)?.label,
				unitPrice: item.unitPrice * 1000,
				quantity: item.quantity != null ? item.quantity : 0,
				discount:
					discountType.value == 1 ? item.discount * 1000 : item.discount / 100,
				tax: item.tax / 100,
				price: item.price * 1000,
			}
		})

	const quotationSpareParts = parseQuotationSpareParts(productRowData.value)
	const removeQuotationSparePartsParams = parseQuotationSpareParts(
		removeQuotationSpareParts.value
	)
	const addQuotationSparePartsParams = parseQuotationSpareParts(
		addQuotationSpareParts.value
	)
	const params = {
		quotationLabours: [...quotationLabours],
		quotationSpareParts: [...quotationSpareParts],
		addQuotationSpareParts: [...addQuotationSparePartsParams],
		removeQuotationSpareParts: [...removeQuotationSparePartsParams],
		note: serviceTicketForm.value.note.value || '',
	}

	if (currentStep.value == EStepperTicket.SELL_PRICE) {
		if (isChangePrice.value) {
			const res = await updateQuotations(
				params,
				ticketID.value,
				quotationID.value,
				false
			)

			if (res.code == 1) {
				if (!isSave.value) {
					setTimeout(() => {
						router.push('/app/sell/new-service-tickets')
					}, 1000)
				}

				$toast($t('module.newServiceTicket.form.toast.saveChange'), true)
				await getDetail()
			} else if (res.code == 100 && res.message) {
				error.value.errorResponse = res.message

				return 0
			} else if (res.error == 500) {
				return 0
			} else {
				$toast(res.message, false)
			}
		} else {
			const res = await createQuotations(
				params,
				ticketID.value,
				false,
				discountType.value ? discountType.value : 2
			)
			if (res.code == 1) {
				$toast($t('module.newServiceTicket.form.toast.saveChange'), true)
				await getDetail()
			} else if (res.code == 100 && res.message) {
				error.value.errorResponse = res.message
			} else if (res.error == 500) {
				return 0
			} else {
				$toast(res.message, false)
			}
		}
	} else {
		router.push('/app/sell/new-service-tickets')
	}
}

const onRefreshDataService = async () => {
	await getDetail()
}

const handleGetAccountInfo = () => {
	const accountInfo = JSON.parse(localStorage.getItem('accountInfo') as string)
		? JSON.parse(localStorage.getItem('accountInfo') as string)
		: ''
	dataPrint.value.accountInfo = accountInfo
}
</script>
<style lang="scss" scoped>
.stepper {
	margin: 30px 40px 50px;
	@media (max-width: 600px) {
		margin: 0;
	}
}

.width-row {
	min-width: 100px;
	max-width: 200px;
	// @media (max-width: 600px) {
	//     width: 120px;
	// }
	// @media (max-width: 768px) {
	//     width: auto;
	// }
}
</style>
