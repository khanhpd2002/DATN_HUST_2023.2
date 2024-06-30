<template>
	<ACCDModal
		@close="closeDialog"
		v-model="isOpen"
		:title="computedTitle"
		class-width="md:w-[1024px]"
	>
		<div v-if="modalState !== EFormState.DELETE" class="cd-modal__body py-4">
			<CDForm :show-footer="false" class="w-full">
				<div class="flex flex-col lg:flex-row">
					<CDFormItem
						:label="$t('module.accessary.distributorCode')"
						class="w-full lg:w-1/2 mr-8"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.accessary.placeholder.distributorCode')"
							v-model="item.distributorId"
							:options="distributorOptions"
							:disabled="modalState === EFormState.VIEW || isDisable"
							@update:modelValue="(val: any) => {
        item.distributorId = val;
        item.distributorName = getValueById(val);
    }"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.accessary.distributorName')"
						class="w-full lg:w-1/2"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.accessary.placeholder.distributorName')"
							v-model="item.distributorName"
							:disabled="true"
						/>
					</CDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row">
					<CDFormItem
						:label="$t('module.accessary.code')"
						required
						class="w-full lg:w-1/2 mr-8"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.accessary.placeholder.code')"
							v-model="item.code"
							:disabled="
								modalState === EFormState.VIEW || modalState === EFormState.EDIT
							"
							@change="() => (duplicateCode = false)"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.accessary.name')"
						required
						class="w-full lg:w-1/2"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.accessary.placeholder.name')"
							v-model="item.name"
							:disabled="modalState === EFormState.VIEW || isValue"
						/>
					</CDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row">
					<CDFormItem
						:label="$t('module.accessary.sparePartType')"
						class="w-full lg:w-1/2 mr-8"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.accessary.placeholder.sparePartType')"
							v-model="item.sparePartType"
							:options="listOptions"
							:disabled="modalState === EFormState.VIEW || isValue"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.accessary.purchasePrice')"
						class="w-full lg:w-1/2"
					>
						<ACCDInputPrice
							@keypress="preFilterInputNumberOnly"
							size="md"
							:placeholder="$t('module.accessary.placeholder.purchasePrice')"
							v-model="item.purchasePrice"
							:disabled="modalState === EFormState.VIEW"
						/>
					</CDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row">
					<CDFormItem
						:label="$t('module.accessary.unit')"
						required
						class="w-full lg:w-1/2 pr-4"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.accessary.placeholder.unit')"
							v-model="item.unit"
							:disabled="modalState === EFormState.VIEW || isValue"
						/>
					</CDFormItem>
				</div>
			</CDForm>
			<div>
				<ACCDTable :columns="carCompatibleColumn" :rowData="carCompatibleRow">
					<template #cell-action="{ row, col, field }">
						<td class="">
							<div
								class="w-8 cursor-pointer flex justify-center pl-8"
								@click="
									() => {
										deleteCarCompatibleRow(row)
									}
								"
							>
								<ACCDIcon
									class="text-xl text-gray-700 cursor-pointer"
									name="fa-solid fa-trash-can"
								>
								</ACCDIcon>
							</div>
						</td>
					</template>
					<template #cell-carBrandId="{ row, col, field }">
						<td class="border-r w-56">
							<ACCDSelect
								@change="() => handleChangeCarBrand(row)"
								v-model="row.carBrandId.value"
								:options="row.carBrandId.options"
								class="w-full"
							>
							</ACCDSelect>
						</td>
					</template>
					<template #cell-carModelId="{ row, col, field }">
						<td class="border-r w-56">
							<ACCDSelect
								@change="() => handleChangeCarModeId(row)"
								v-model="row.carModelId.value"
								:options="row.carModelId.options"
								class="w-full"
							>
							</ACCDSelect>
						</td>
					</template>
					<template #cell-carYearId="{ row, col, field }">
						<td class="border-r w-56">
							<ACCDSelect
								@change="() => handleChangeCarYear(row)"
								v-model="row.carYearId.value"
								:options="row.carYearId.options"
								class="w-full"
							>
							</ACCDSelect>
						</td>
					</template>
					<template #cell-carVersionId="{ row, col, field }">
						<td class="border-r w-56">
							<ACCDSelect
								v-model="row.carVersionId.value"
								:options="row.carVersionId.options"
								class="w-full"
							>
							</ACCDSelect>
						</td>
					</template>
				</ACCDTable>
				<div class="flex items-center text-active mt-1">
					<span class="cursor-pointer mr-1" @click="addCarCompatibleRow">
						<ACCDAIcon name="AddCircle" size="20" class="cursor-pointer" />
					</span>
					<span
						v-if="modalState !== EFormState.VIEW"
						class="cursor-pointer font-medium"
						@click="addCarCompatibleRow"
						>{{ $t('module.accessary.form.addCarCompatible') }}</span
					>
				</div>
				<span class="italic text-red-500" v-if="invalid">
					{{ $t('module.accessary.error.missingRequiredField') }}
				</span>
				<span class="italic text-red-500" v-else-if="duplicateCode">
					{{ $t('module.accessary.error.duplicateCode') }}
				</span>
				<span class="italic text-red-500" v-else-if="errorCarMode">
					{{ errorCarMode }}
				</span>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton
					size="md"
					variant="fill"
					type="secondary"
					@click="onCloseDialog"
				>
					<span class="text-info-base font-medium">{{
						$t('module.accessary.action.back')
					}}</span>
				</ACCDButton>
				<ACCDButton
					size="md"
					type="primary"
					variant="fill"
					@click="onSubmit"
					:disabled="isValue"
				>
					<span class="text-white font-medium">{{
						modalState == EFormState.EDIT
							? $t('module.accessary.action.edit')
							: $t('module.accessary.action.save')
					}}</span>
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
</template>

<script lang="ts">
import {
	createAccessary,
	updateAccessaryrById,
	getAccessaryById,
	getProductByCode,
} from '@/modules/accessary/api'
import { defineComponent } from 'vue'
import { EFormState } from '@/enums'
import type { IAccessary, IAccessaryInfo, IDistributor } from '@/types'
import { ISelectOption } from '@/types'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
	saveLogTracking,
} from '@/modules/sharedModules/api'
import { CDForm, CDFormItem } from '@cd/design-system'
import { getListDistributors } from '@/modules/distributor/api'
import { cloneDeep, debounce, isObject } from 'lodash'
import useVuelidate from '@vuelidate/core'
import { helpers, required } from '@vuelidate/validators'
import { preFilterInputNumberOnly } from '@/modules/sharedModules/component/constants'
import { emitter } from '@/utils/mitt'

const baseCarCompatible = {
	carBrandId: {
		value: '',
		display: '',
		options: [] as ISelectOption[],
	},
	carModelId: {
		value: '',
		display: '',
		options: [] as ISelectOption[],
	},
	carYearId: {
		value: '',
		display: '',
		options: [] as ISelectOption[],
	},
	carVersionId: {
		value: '',
		display: '',
		options: [] as ISelectOption[],
	},
	instanceKey: 0,
}

export default defineComponent({
	components: { CDFormItem, CDForm },
	setup() {
		return {
			EFormState,
			// v$: useVuelidate(),
		}
	},
	data() {
		return {
			instanceKey: new Date().getTime(),
			preFilterInputNumberOnly: preFilterInputNumberOnly,
			invalid: false,
			duplicateCode: false,
			carCompatibleColumn: [
				{
					key: 'carBrandId',
					headerName: this.$t(
						'module.accessary.form.carCompatibleColumn.carBrandId'
					),
				},
				{
					key: 'carModelId',
					headerName: this.$t(
						'module.accessary.form.carCompatibleColumn.carModelId'
					),
				},
				{
					key: 'carYearId',
					headerName: this.$t(
						'module.accessary.form.carCompatibleColumn.carYearId'
					),
				},
				{
					key: 'carVersionId',
					headerName: this.$t(
						'module.accessary.form.carCompatibleColumn.carVersionId'
					),
				},
				{
					key: 'action',
					headerName: '',
				},
			],
			carCompatibleRow: [] as any[],
			maxInt: import.meta.env.VITE_MAX_INTEGER,
			isOpen: false,
			modalState: EFormState.VIEW as EFormState,
			item: {
				type: 1,
				code: '',
				name: '',
				color: '',
				distributorId: 0,
				distributorCode: null,
				distributorName: null,
				purchasePrice: null,
				sparePartType: 0,
				quantity: 0,
				unit: '',
				description: '',
				productCompatibilities: [],
			} as unknown as IAccessary,
			responseErrorMessages: '' as string,
			id: 0,
			carBrandOptions: [] as ISelectOption[],
			carModelOptions: [] as ISelectOption[],
			carYearOptions: [] as ISelectOption[],
			carVersionOptions: [] as ISelectOption[],
			distributorOptions: [] as ISelectOption[],
			sparePartTypeOptions: [] as ISelectOption[],
			originData: {} as any,
			isValue: false,
			intanceKeyRowProduct: 0,
			productName: '',
			isDisable: false,
			errorCarMode: '',
		}
	},

	computed: {
		v$() {
			return useVuelidate(this.validations, { item: this.item })
		},
		computedTitle(): string {
			switch (this.modalState) {
				case EFormState.ADD:
					return this.$t('module.accessary.titleCreate')
				case EFormState.ADD_NEW:
					return this.$t('module.accessary.titleCreate')
				case EFormState.EDIT:
					return this.$t('module.accessary.titleEdit')
				case EFormState.VIEW:
					return this.$t('module.accessary.titleView')
			}
			return ''
		},
		listOptions(): ISelectOption[] {
			for (let i = 1; i <= 6; i++) {
				this.sparePartTypeOptions.push({
					label: this.$t(`module.accessary.sparePartTypeOptions.${i}`),
					value: i,
					rawValue: undefined,
				})
			}

			return this.sparePartTypeOptions
		},
		validations() {
			return {
				item: {
					// distributorId: {
					// 	required: helpers.withMessage('Thiếu thông tin rồi', required),
					// 	$lazy: true,
					// },
					// distributorName: {
					// 	required: helpers.withMessage('Thiếu thông tin rồi', required),
					// 	$lazy: true,
					// },
					code: {
						required: helpers.withMessage('Thiếu thông tin rồi', required),
						$lazy: true,
					},
					name: {
						required: helpers.withMessage('Thiếu thông tin rồi', required),
						$lazy: true,
					},
					// purchasePrice: {
					// 	required: helpers.withMessage('Thiếu thông tin rồi', required),
					// 	$lazy: true,
					// },
					unit: {
						required: helpers.withMessage('Thiếu thông tin rồi', required),
						$lazy: true,
					},
				},
			}
		},
	},
	watch: {
		'item.carBrandId': {
			handler(newVal: number, oldVal: number) {
				console.log('newVal', newVal)

				if (newVal != oldVal) {
					this.carModelOptions = []
					getCarModelList(newVal).then((res) => {
						this.carModelOptions = res.data.data.map((item: IAccessaryInfo) => {
							return {
								value: item.id,
								label: item.title,
							}
						})
					})
				}
			},
		},
		'item.carModelId': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.carYearOptions = []

					getCarYearList(newVal).then((res) => {
						this.carYearOptions = res.data.data.map((item: IAccessaryInfo) => {
							return {
								value: item.id,
								label: item.title,
							}
						})
					})
				}

				console.log('carModelId', this.carCompatibleRow)
			},
		},
		'item.carYearId': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.carVersionOptions = []
					getCarVersionList(newVal).then((res) => {
						this.carVersionOptions = res.data.data.map(
							(item: IAccessaryInfo) => {
								return {
									value: item.id,
									label: item.title,
								}
							}
						)
					})
				}
			},
		},
	},
	async created() {
		emitter.on('pages-layout-on-confirmClose', (ik) => {
			if (ik == this.instanceKey) {
				this.closeDialog()
			}
		})

		emitter.on('on-add-new-product', (data: any) => {
			this.isOpen = true
			this.modalState = EFormState.ADD_NEW
			this.item.name = data.productName
			this.intanceKeyRowProduct = data.intanceKey
			this.productName = data.productName
			this.item.distributorId = data.distributorId
			this.item.distributorName = this.getValueById(data.distributorId)
			this.carCompatibleRow = []
			if (this.item.distributorId) {
				this.isDisable = true
			} else {
				this.isDisable = false
			}
		})

		await getCarBrandList().then((res) => {
			this.carBrandOptions = res.data.data.map((item: IAccessaryInfo) => {
				return {
					value: item.id,
					label: item.title,
				}
			})
		})
		this.getListDistributors()
		// for (let i = 1; i <= 6; i++) {
		// 	this.sparePartTypeOptions.push({
		// 		label: this.$t(`module.accessary.sparePartTypeOptions.${i}`),
		// 		value: i,
		// 		rawValue: undefined,
		// 	})
		// }
	},
	methods: {
		// handleDebounceChangeCode: debounce(async function (code: any) {
		// 	const res = await getProductByCode(code)
		// 	if (res.code === 1) {
		// 		this.item.sparePartType = res.data.sparePartType
		// 		this.item.quantity = res.data.quantity
		// 		this.item.name = res.data.name
		// 		this.item.unit = res.data.unit
		// 		this.isValue = true
		// 	} else if (res.code === 404) {
		// 		this.isValue = false
		// 	} else {
		// 		this.item.sparePartType = ''
		// 		this.item.quantity = ''
		// 		this.item.name = ''
		// 		this.item.unit = ''
		// 		this.isValue = false
		// 	}
		// }, 1000),

		onCloseDialog() {
			emitter.emit('layout-pages-open-confirmClose', this.instanceKey)
			this.invalid = false
			const productEx = {
				productId: this.productName,
				intanceKey: this.intanceKeyRowProduct,
			}
			emitter.emit('on-remove-product', productEx)
		},
		async onSelect(row: any, field: string, val: any) {
			let sRow = this.carCompatibleRow.find((c) => {
				return row.instanceKey == c.instanceKey
			})
			sRow[field].value = val.value
			sRow[field].display = val.label

			if (field == 'carBrandId') {
				let res = await getCarModelList(val.value)
				let options = res.data.data.map((d: any) => {
					return {
						value: d.id,
						label: d.title,
					}
				})
				sRow.carModelId.value = ''
				sRow.carModelId.display = ''
				sRow.carYearId.value = ''
				sRow.carYearId.display = ''
				sRow.carVersionId.label = ''
				sRow.carVersionId.display = ''
				sRow.carModelId.options = options
			} else if (field == 'carModelId') {
				let res = await getCarYearList(val.value)
				let options = res.data.data.map((d: any) => {
					return {
						value: d.id,
						label: d.title,
					}
				})
				sRow.carYearId.value = ''
				sRow.carYearId.display = ''
				sRow.carVersionId.label = ''
				sRow.carVersionId.display = ''
				sRow.carYearId.options = options
			} else if (field == 'carYearId') {
				let res = await getCarVersionList(val.value)
				let options = res.data.data.map((d: any) => {
					return {
						value: d.id,
						label: d.title,
					}
				})
				sRow.carVersionId.label = ''
				sRow.carVersionId.display = ''
				sRow.carVersionId.options = options
			}
		},
		deleteCarCompatibleRow(row: any) {
			this.carCompatibleRow = this.carCompatibleRow.filter((sRow: any) => {
				return sRow.instanceKey != row.instanceKey
			})
		},
		addCarCompatibleRow() {
			let row = {
				...cloneDeep(baseCarCompatible),
				instanceKey:
					this.carCompatibleRow.length > 0
						? this.carCompatibleRow[this.carCompatibleRow.length - 1]
								.instanceKey + 1
						: 0,
			}

			row.carBrandId.options = this.carBrandOptions
			this.carCompatibleRow.push(row)
		},
		async handleChangeCarBrand(row: any) {
			let sRow = this.carCompatibleRow.find((c) => {
				return row.instanceKey == c.instanceKey
			})

			if (sRow) {
				if (row.carBrandId.value) {
					await getCarModelList(row.carBrandId.value).then((res) => {
						row.carModelId.options = res.data.data.map((s: any) => {
							return {
								value: s.id,
								label: s.title,
								origin: s,
							} as ISelectOption & { origin: any }
						})
					})
				}
			}
			row.carModelId.value = ''
			row.carYearId.value = ''
			row.carVersionId.value = ''
		},

		async handleChangeCarModeId(row: any) {
			this.errorCarMode = ''
			let sRow = this.carCompatibleRow.find((c) => {
				return row.instanceKey == c.instanceKey
			})

			if (sRow) {
				if (row.carModelId.value) {
					await getCarYearList(row.carModelId.value).then((res) => {
						row.carYearId.options = res.data.data.map((s: any) => {
							return {
								value: s.id,
								label: s.title,
								origin: s,
							} as ISelectOption & { origin: any }
						})
					})
				}
			}
			row.carYearId.value = ''
			row.carVersionId.value = ''
		},

		async handleChangeCarYear(row: any) {
			let sRow = this.carCompatibleRow.find((c) => {
				return row.instanceKey == c.instanceKey
			})
			if (sRow) {
				if (row.carModelId.value) {
					await getCarVersionList(row.carYearId.value).then((res) => {
						row.carVersionId.options = res.data.data.map((s: any) => {
							return {
								value: s.id,
								label: s.title,
								origin: s,
							} as ISelectOption & { origin: any }
						})
					})
				}
			}
			row.carVersionId.value = ''
		},

		async openDialog(id?: number, data?: IAccessary, state?: EFormState) {
			this.carCompatibleRow = []
			this.isOpen = true
			this.id = id || 0
			this.modalState = state || EFormState.ADD
			// this.item = data ? { ...data } : ({} as IAccessary);
			this.invalid = false
			if (state == EFormState.EDIT) {
				if (id) {
					let res = await getAccessaryById(id)

					if (res.code == 1) {
						this.originData = res.data
						this.item = res.data
						this.item.distributorName = this.getValueById(
							this.item.distributorId
						)
						let carCompatibleRow = [] as any[]
						res.data.productCompatibilities.map(async (r: any, i: number) => {
							let row = cloneDeep(baseCarCompatible)

							row.carBrandId.value = r.carBrandId
							row.carModelId.value = r.carModelId
							row.carYearId.value = r.carYearId
							row.carVersionId.value = r.carVersionId
							row.instanceKey = i
							row.carBrandId.options = this.carBrandOptions

							let resM = await getCarModelList(row.carBrandId.value)
							if (resM.data.code == 1) {
								let optionsM = resM.data.data.map((d: any) => {
									return {
										value: d.id,
										label: d.title,
									}
								})
								row.carModelId.options = optionsM
							}
							let resY = await getCarYearList(row.carModelId.value)
							if (resY.data.code == 1) {
								let optionsY = resY.data.data.map((d: any) => {
									return {
										value: d.id,
										label: d.title,
									}
								})
								row.carYearId.options = optionsY
							}

							let resE = await getCarVersionList(row.carYearId.value)
							if (resE.data.code == 1) {
								let optionsE = resE.data.data.map((d: any) => {
									return {
										value: d.id,
										label: d.title,
									}
								})
								row.carVersionId.options = optionsE
							}

							// debugger;
							this.carCompatibleRow.push(row)
						})

						//  = carCompatibleRow;
					}
				}
			} else {
				this.item = {
					type: 1,
					code: '',
					name: '',
					color: '',
					distributorId: 0,
					distributorCode: null,
					distributorName: null,
					purchasePrice: null,
					sparePartType: 0,
					quantity: 0,
					unit: '',
					description: '',
					productCompatibilities: [],
				} as unknown as IAccessary
			}

			console.log('carCompatibleRow', this.carCompatibleRow)
		},
		closeDialog() {
			this.isOpen = false
			this.resetForm()
			emitter.emit('layout-page-close-confirmClose')
			const productEx = {
				productId: this.productName,
				intanceKey: this.intanceKeyRowProduct,
				distributorId: this.item.distributorId,
			}
			emitter.emit('on-remove-product', productEx)
			this.isDisable = false
		},
		resetForm() {
			this.responseErrorMessages = ''
			this.invalid = false
			this.duplicateCode = false
			this.isValue = false
			this.item = {} as typeof this.item
		},
		async onSubmit() {
			saveLogTracking({
				logEvent: 'Click_CREATE_SPARE_PART',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})
			this.responseErrorMessages = ''
			const result = await this.v$.value.$validate()
			if (!result) {
				this.invalid = true
				return
			} else {
				this.invalid = false
			}

			this.carCompatibleRow.forEach((el) => {
				if (el.carBrandId.value && !el.carModelId.value) {
					this.errorCarMode = this.$t('module.accessary.error.carModeError')
				}
			})
			if (this.errorCarMode) {
				return
			}
			if (
				this.modalState === EFormState.ADD ||
				this.modalState === EFormState.ADD_FOR_CUSTOMER ||
				this.modalState === EFormState.ADD_NEW
			) {
				let data = { ...this.item }
				data.productCompatibilities = this.carCompatibleRow.map((row) => {
					let val = {} as any
					for (let key in row) {
						if (isObject(row[key])) {
							val[key] = row[key].value ? row[key].value : 0
						}
					}
					return val
				}) as any

				data.type = 1
				createAccessary(data).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.accessary.success.create'), true)
						this.$emit('refresh')
						this.isOpen = false

						// this.closeDialog();
						const product = {
							productId: res.data.parentProductId,
							intanceKey: this.intanceKeyRowProduct,
							distributorId: data.distributorId,
						}
						emitter.emit('on-change-data-product', product)
						this.resetForm()
					} else {
						this.duplicateCode = true
					}
				})
			}
			if (this.modalState === EFormState.EDIT) {
				const updateRow = this.carCompatibleRow.map((row) => {
					let val = {} as any
					for (let key in row) {
						if (isObject(row[key])) {
							val[key] = row[key].value ? row[key].value : 0
						}
					}
					return val
				}) as any

				this.item.productCompatibilities = updateRow

				updateAccessaryrById(this.item, this.item.id).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.accessary.success.edit'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.responseErrorMessages = res.message
						this.$toast(this.responseErrorMessages, false)
					}
				})
			}
		},
		async getListDistributors() {
			const res = await getListDistributors({
				pageSize: 100000,
				pageNumber: 1,
			})

			this.distributorOptions = res.data.map((a: IDistributor) => {
				return {
					label: a.code + ' - ' + a.name,
					value: a.id,
				}
			})
		},
		getValueById(idToFind: number): string {
			const selectedOption = this.distributorOptions.find(
				(option: ISelectOption) => option.value === idToFind
			)
			if (selectedOption) {
				return selectedOption.label
			} else {
				// Return a default value or handle the case where the id is not found.
				return ''
			}
		},
	},
})
</script>

<style scoped></style>
