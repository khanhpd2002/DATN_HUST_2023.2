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
							:label="$t('module.customer.phoneNumber')"
							required
							class="font-semibold"
						>
							<ACCDInputText
								@keypress="preFilterInputNumberOnly"
								size="md"
								:placeholder="$t('module.customer.placeholder.phoneNumber')"
								v-model="item.phoneNumber"
								:disabled="modalState === EFormState.VIEW"
								class="font-medium"
								:maxLength="10"
								@update:modelValue="() => handleChangePhoneNumber()"
							/>
							<span class="italic text-red-500" v-if="duplicatePhoneNumber">
								{{ $t('module.customer.error.duplicatePhoneNumber') }}
							</span>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.customer.placeholder.customerTypeName')"
							required
							class="font-semibold"
						>
							<ACCDSelect
								size="md"
								:placeholder="
									$t('module.customer.placeholder.customerTypeName')
								"
								:modelValue="item.customerTypeId"
								:disabled="modalState === EFormState.VIEW"
								:options="customerTypeOptions"
								@update:modelValue="(val: number) => { item.customerTypeId = val }"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.customer.placeholder.customerName')"
							required
							class="font-semibold"
						>
							<ACCDInputText
								size="md"
								:placeholder="$t('module.customer.placeholder.customerName')"
								v-model="item.fullName"
								:disabled="modalState === EFormState.VIEW"
								class="font-medium"
							/>
						</CDFormItem>
						<CDFormItem
							:label="$t('module.customer.placeholder.address')"
							class="font-semibold"
						>
							<ACCDInputText
								size="md"
								:placeholder="$t('module.customer.placeholder.address')"
								v-model="item.address"
								:disabled="modalState === EFormState.VIEW"
								class="font-medium"
							/>
						</CDFormItem>
						<span class="italic text-red-500" v-if="isErrorPhoneNumber">
							{{ $t('module.customer.error.phoneNumber') }}
						</span>
						<span class="italic text-red-500" v-else-if="invalid">
							<span>
								{{ '*' }}
							</span>
							{{ $t('module.customer.error.missingRequiredField') }}
						</span>
					</CDForm>
				</div>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5">
				<ACCDButton type="secondary" @click="onCloseDialog">
					<span class="font-medium">{{
						$t('module.customer.action.cancel')
					}}</span>
				</ACCDButton>

				<ACCDButton
					v-if="
						modalState !== EFormState.VIEW && modalState !== EFormState.DELETE
					"
					type="primary"
					@click="onSubmit"
				>
					<span class="font-medium">{{
						modalState == EFormState.EDIT
							? $t('module.customer.action.edit')
							: $t('module.customer.action.save')
					}}</span>
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { EFormState } from '@/enums'
import { CDForm, CDFormItem } from '@cd/design-system'
import { ICustomer, ICustomerType, ISelectOption } from '@/types'
import { createCustomer, updateCustomerById } from '@/modules/customer/api'
import { getCustomerTypes } from '@/modules/customerType/api'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { emitter } from '@/utils/mitt'
import { saveLogTracking } from '@/modules/sharedModules/api'
import {
	preFilterInputNumberOnly,
	validatePhoneNumber,
} from '@/modules/sharedModules/component/constants'

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
			duplicatePhoneNumber: false,
			isCancel: false,
			invalid: false,
			isOpen: false,
			modalState: EFormState.VIEW as EFormState,
			item: {
				phoneNumber: '',
				customerTypeName: '',
				customerTypeId: '',
				address: '',
				fullName: '',
			} as ICustomer,
			responseErrorMessages: '' as string,
			id: 0,
			customerTypeOptions: [] as ISelectOption[],
			isErrorPhoneNumber: false,
		}
	},
	validations() {
		return {
			item: {
				phoneNumber: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				customerTypeId: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				fullName: {
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
					return this.$t('module.customer.create')
				case EFormState.EDIT:
					return this.$t('module.customer.edit')
				case EFormState.VIEW:
					return this.$t('module.customer.view')
				default:
					return ''
			}
		},
	},
	created() {
		emitter.on('pages-layout-on-confirmClose', (ik) => {
			if (ik == this.instanceKey) {
				this.closeDialog()
			}
		})
		const MAX_INT = import.meta.env.VITE_MAX_INTEGER
		getCustomerTypes({ pageSize: MAX_INT, pageNumber: 1 }).then((res) => {
			this.customerTypeOptions = res.data.map((item: ICustomerType) => {
				return {
					value: item.id,
					label: item.customerTypeName,
				}
			})
		})
	},
	methods: {
		preFilterInputNumberOnly,
		openDialog(id?: number, data?: ICustomer, state?: EFormState) {
			this.isOpen = true
			this.id = id || 0
			this.modalState = state || EFormState.ADD
			this.item = data ? { ...data } : ({} as ICustomer)
			this.invalid = false
		},
		onCloseDialog() {
			emitter.emit('layout-pages-open-confirmClose', this.instanceKey)
		},
		closeDialog() {
			this.duplicatePhoneNumber = false
			this.isOpen = false
			this.invalid = false
			this.isCancel = false
			this.resetForm()
			emitter.emit('layout-page-close-confirmClose')
		},
		resetForm() {
			this.responseErrorMessages = ''
			this.item = {} as ICustomer
		},
		handleChangePhoneNumber() {
			this.isErrorPhoneNumber = false
			this.invalid = false
		},
		async onSubmit() {
			saveLogTracking({
				logEvent: 'Click_CREATE_CUSTOMER',
				garageId: localStorage.getItem('garageId'),
				actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
			})

			const result = await this.v$.$validate()
			if (!result) {
				this.invalid = true
				return
			}

			if (
				!this.item.phoneNumber.match(
					/([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/
				)
			) {
				this.isErrorPhoneNumber = true
				return
			}

			if (this.modalState === EFormState.ADD) {
				this.item.cars = []
				if (isNaN(Number(this.item.customerTypeId))) {
					this.item.customerTypeName = this.item.customerTypeId
						? this.item.customerTypeId.toString()
						: ''
					this.item.customerTypeId = undefined
				}
				createCustomer(this.item).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.customer.success.create'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.duplicatePhoneNumber = true
						this.responseErrorMessages = res.message || res.msg
						// this.$toast(this.responseErrorMessages, false);
					}
				})
			}
			if (this.modalState === EFormState.EDIT) {
				updateCustomerById(this.item, this.item.id).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.customerType.success.edit'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.responseErrorMessages = res.message
						this.duplicatePhoneNumber = true
						// this.$toast(this.responseErrorMessages, false);
					}
				})
			}
		},
	},
})
</script>

<style scoped></style>
