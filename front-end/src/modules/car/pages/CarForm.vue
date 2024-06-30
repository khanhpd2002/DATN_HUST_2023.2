<template>
	<ACCDModal
		@close="closeDialog"
		v-model="isOpen"
		:title="computedTitle"
		class-width="md:w-[422px]"
	>
		<div v-if="modalState !== EFormState.DELETE">
			<div class="flex flex-col gap-4 mx-1 py-4">
				<div>
					<CDForm :show-footer="false">
						<CDFormItem
							:label="$t('module.car.carBrand')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="$t('module.car.placeholder.carBrand')"
								v-model="item.carBrandId"
								:options="carBrandOptions"
								:disabled="modalState === EFormState.VIEW"
								@update:modelValue="(val: number)=>{
                                  item.carBrandId = val;
                                  item.carModelId = undefined;
                                  item.carYearId = undefined;
                                  item.carVersionId = undefined
                                }"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.carModel')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="$t('module.car.placeholder.carModel')"
								v-model="item.carModelId"
								:options="carModelOptions"
								:disabled="modalState === EFormState.VIEW"
								@update:modelValue="(val: number)=>{
                                    item.carModelId = val;
                                    item.carYearId = undefined;
                                    item.carVersionId = undefined;
                                }"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.carYear')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="$t('module.car.placeholder.carYear')"
								v-model="item.carYearId"
								:options="carYearOptions"
								:disabled="modalState === EFormState.VIEW"
								@update:modelValue="(val: number)=>{
                    item.carYearId = val;
                    item.carVersionId = undefined;
                }"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.carVersion')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="$t('module.car.placeholder.carVersion')"
								v-model="item.carVersionId"
								:options="carVersionOptions"
								:disabled="modalState === EFormState.VIEW"
								@update:modelValue="(val: number)=> {
                  item.carVersionId = val
                }"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.licensePlate')"
							required
							class="font-semibold"
						>
							<ACCDInputText
								size="md"
								:placeholder="$t('module.car.placeholder.licensePlate')"
								v-model="item.licensePlate"
								:disabled="modalState === EFormState.VIEW"
								class="font-medium"
							/>
							<span class="italic text-red-500" v-if="duplicateLicensePlate">
								{{ $t('module.car.error.duplicateLicensePlate') }}
							</span>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.vinNumber')"
							class="font-semibold"
						>
							<ACCDInputText
								size="md"
								:placeholder="$t('module.car.placeholder.vinNumber')"
								v-model="item.vinNumber"
								:disabled="modalState === EFormState.VIEW"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.car.customerName')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="$t('module.car.placeholder.customerName')"
								v-model="item.customerId"
								:options="customerOptions"
								:disabled="
									modalState === EFormState.EDIT ||
									modalState === EFormState.ADD_FOR_CUSTOMER
								"
								@update:modelValue="(val: number)=>item.customerId = val"
								class="font-medium"
							/>
						</CDFormItem>
						<span class="italic text-red-500" v-if="invalid">
							{{ $t('module.car.error.missingRequiredField') }}
						</span>
					</CDForm>
				</div>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton variant="fill" type="secondary" @click="onCloseDialog">
					<span class="text-info-base font-medium">{{
						$t('module.car.action.cancel')
					}}</span></ACCDButton
				>
				<ACCDButton
					v-if="
						modalState !== EFormState.VIEW && modalState !== EFormState.DELETE
					"
					type="primary"
					variant="fill"
					@click="onSubmit"
				>
					<span class="text-white font-medium">{{
						modalState == EFormState.EDIT
							? $t('module.car.action.edit')
							: $t('module.car.action.save')
					}}</span></ACCDButton
				>
			</div>
		</template>
	</ACCDModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { EFormState } from '@/enums'
import type { ICar, ICustomer } from '@/types'
import { ICarInfo, ISelectOption } from '@/types'
import { createCar, updateCarById } from '@/modules/car/api'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
} from '@/modules/sharedModules/api'
import { getCustomers } from '@/modules/customer/api'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { CDForm, CDFormItem } from '@cd/design-system'
import { emitter } from '@/utils/mitt'

export default defineComponent({
	components: { CDForm, CDFormItem },
	setup() {
		return {
			EFormState,
			v$: useVuelidate(),
		}
	},
	data() {
		return {
			instanceKey: new Date().getTime(),
			invalid: false,
			duplicateLicensePlate: false,
			maxInt: import.meta.env.VITE_MAX_INTEGER,
			isOpen: false,
			modalState: EFormState.VIEW as EFormState,
			item: {
				carName: '',
				carBrandId: '',
				carBrandName: '',
				carModelId: '',
				carModelName: '',
				carYearId: '',
				carYearName: '',
				carVersionId: '',
				carVersionName: '',
				licensePlate: '',
				vinNumber: '',
				customerId: '',
			} as unknown as ICar,
			responseErrorMessages: '' as string,
			id: 0,
			carBrandOptions: [] as ISelectOption[],
			carModelOptions: [] as ISelectOption[],
			carYearOptions: [] as ISelectOption[],
			carVersionOptions: [] as ISelectOption[],
			customerOptions: [] as ISelectOption[],
		}
	},
	validations() {
		return {
			item: {
				carBrandId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				carModelId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				carYearId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				carVersionId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				licensePlate: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				customerId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
			},
		}
	},
	computed: {
		computedTitle(): string {
			switch (this.modalState) {
				case EFormState.ADD:
					return this.$t('module.car.create')
				case EFormState.ADD_FOR_CUSTOMER:
					return this.$t('module.car.create')
				case EFormState.EDIT:
					return this.$t('module.car.edit')
				case EFormState.VIEW:
					return this.$t('module.car.view')
			}
			return ''
		},
	},
	watch: {
		'item.carBrandId': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal && newVal) {
					this.carModelOptions = []
					getCarModelList(newVal).then((res) => {
						this.carModelOptions = res.data.data.map((item: ICarInfo) => {
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
						this.carYearOptions = res.data.data.map((item: ICarInfo) => {
							return {
								value: item.id,
								label: item.title,
							}
						})
					})
				}
			},
		},
		'item.carYearId': {
			handler(newVal: number, oldVal: number) {
				if (newVal != oldVal) {
					this.carVersionOptions = []
					getCarVersionList(newVal).then((res) => {
						this.carVersionOptions = res.data.data.map((item: ICarInfo) => {
							return {
								value: item.id,
								label: item.title,
							}
						})
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
		await getCarBrandList().then((res) => {
			this.carBrandOptions = res.data.data.map((item: ICarInfo) => {
				return {
					value: item.id,
					label: item.title,
				}
			})
		})
		await getCustomers({ pageSize: this.maxInt, pageNumber: 1 }).then((res) => {
			this.customerOptions = res.data.map((item: ICustomer) => {
				return {
					value: item.id,
					label: item.fullName,
				}
			})
		})
	},
	methods: {
		openDialog(id?: number, data?: ICar, state?: EFormState) {
			this.isOpen = true
			this.id = id || 0
			this.modalState = state || EFormState.ADD
			this.invalid = false
			this.item = data
				? { ...data }
				: ({
						carName: '',
						carBrandId: '',
						carBrandName: '',
						carModelId: '',
						carModelName: '',
						carYearId: '',
						carYearName: '',
						carVersionId: '',
						carVersionName: '',
						licensePlate: '',
						vinNumber: '',
						customerId: '',
				  } as unknown as ICar)
		},
		onCloseDialog() {
			emitter.emit('layout-pages-open-confirmClose', this.instanceKey)
		},
		closeDialog() {
			this.isOpen = false
			this.invalid = false
			this.resetForm()
			emitter.emit('layout-page-close-confirmClose', this.instanceKey)
		},
		resetForm() {
			this.responseErrorMessages = ''
			this.invalid = false
			this.duplicateLicensePlate = false
			this.item = {} as ICar
		},
		async onSubmit() {
			const result = await this.v$.$validate()
			if (!result) {
				this.invalid = true
				return
			} else {
				this.invalid = false
			}

			if (
				this.modalState === EFormState.ADD ||
				this.modalState === EFormState.ADD_FOR_CUSTOMER
			) {
				createCar(this.item).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.car.success.create'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.duplicateLicensePlate = true
						this.responseErrorMessages = res.message || res.msg
						if (res.code == 99) {
							this.$toast(this.responseErrorMessages, false)
						}
					}
				})
			}
			if (this.modalState === EFormState.EDIT) {
				updateCarById(this.item, this.item.id).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.car.success.edit'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.duplicateLicensePlate = true
						this.responseErrorMessages = res.message
						if (res.code == 99) {
							this.$toast(this.responseErrorMessages, false)
						}
					}
				})
			}
		},
	},
})
</script>

<style scoped></style>
