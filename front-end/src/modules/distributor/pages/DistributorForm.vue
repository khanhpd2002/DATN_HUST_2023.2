<template>
	<ACCDModal
		@close="closeDialog"
		v-model="isOpen"
		:title="computedTitle"
		class-width="md:w-[422px]"
	>
		<template v-if="modalState !== EFormState.DELETE">
			<div class="flex flex-col gap-4 mx-1 cd-modal__body py-4">
				<CDForm :show-footer="false">
					<CDFormItem
						:label="$t('module.distributor.code')"
						required
						class="font-semibold"
					>
						<ACCDInputText
							:disabled="modalState !== EFormState.ADD"
							size="md"
							:placeholder="$t('module.distributor.placeholder.code')"
							v-model="item.code"
							:readonly="modalState === EFormState.VIEW"
							class="font-medium"
							@change="() => (invalid = false)"
						/>
						<span class="italic text-red-500" v-if="duplicateDistributorCode">
							{{ $t('module.distributor.error.duplicateDistributorCode') }}
						</span>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.name')"
						required
						class="font-semibold"
					>
						<ACCDInputText
							:disabled="modalState === EFormState.VIEW"
							size="md"
							:placeholder="$t('module.distributor.placeholder.name')"
							v-model="item.name"
							:readonly="modalState === EFormState.VIEW"
							class="font-medium"
							@change="() => (invalid = false)"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.province')"
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.distributor.placeholder.province')"
							:modelValue="item.provinceId"
							:disabled="modalState === EFormState.VIEW"
							:options="provinceOptions"
							@update:modelValue="(val: number)=>{item.provinceId = val; item.districtId = undefined; item.wardId = undefined}"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.district')"
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.distributor.placeholder.district')"
							:modelValue="item.districtId"
							:disabled="modalState === EFormState.VIEW"
							:options="districtOptions"
							@update:modelValue="(val: number)=>{
                    item.districtId = val;
                    item.wardId = undefined
                }"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.ward')"
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.distributor.placeholder.ward')"
							:modelValue="item.wardId"
							:options="wardOptions"
							:disabled="modalState === EFormState.VIEW"
							@update:modelValue="(val: number)=>item.wardId = val"
							:isRequired="true"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.address')"
						class="font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.distributor.placeholder.address')"
							:disabled="modalState === EFormState.VIEW"
							v-model="item.address"
							:readonly="modalState === EFormState.VIEW"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.contactName')"
						class="font-semibold"
					>
						<ACCDInputText
							:disabled="modalState === EFormState.VIEW"
							size="md"
							:label="$t('module.distributor.contactName')"
							:placeholder="$t('module.distributor.placeholder.contactName')"
							v-model="item.contactName"
							:readonly="modalState === EFormState.VIEW"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.distributor.contactPhone')"
						class="font-semibold"
						required
					>
						<ACCDInputText
							:disabled="modalState === EFormState.VIEW"
							size="md"
							:label="$t('module.distributor.contactPhone')"
							:placeholder="$t('module.distributor.placeholder.contactPhone')"
							v-model="item.contactPhone"
							:readonly="modalState === EFormState.VIEW"
							class="font-medium"
							@update:modelValue="handleChangePhone"
							:maxLength="10"
						/>
					</CDFormItem>
					<span class="italic text-red-500" v-if="isErrorPhoneNumber">
						{{ $t('module.distributor.error.phoneNumber') }}
					</span>
					<span class="italic text-red-500" v-else-if="invalid">
						{{ $t('module.distributor.error.missingRequiredField') }}
					</span>
				</CDForm>
			</div>
		</template>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<div>
					<ACCDButton @click="onClickBack" type="secondary" size="md">
						{{ $t('module.distributor.back') }}
					</ACCDButton>
				</div>
				<div
					v-if="
						modalState !== EFormState.VIEW && modalState !== EFormState.DELETE
					"
				>
					<ACCDButton @click="onSubmit" type="primary" size="md">
						{{
							modalState == EFormState.EDIT
								? $t('module.distributor.edit')
								: $t('module.distributor.confirm')
						}}
					</ACCDButton>
				</div>
				<ACCDButton
					v-if="modalState === EFormState.DELETE"
					@click="onSubmit"
					type="submit"
					class="block rounded-md bg-red-600 px-3 py-2 text-center font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600"
				>
					{{ $t('module.distributor.deleteAction') }}
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
	<!-- <ACCDModal v-model="isCancel" width="374px">
  <div class="flex gap-3">
    <div class="rounded-full bg-red-100">
      <ACCDIcon
          name="fa-solid fa-triangle-exclamation"
          class="text-red-600 w-8 h-8 flex justify-center items-center"
      ></ACCDIcon>
    </div>
    <span class="text-xs">{{
        $t("module.distributor.confirmCancel")
      }}</span>
  </div>
  <template #footer>
    <div class="flex justify-end gap-2 mr-4 mb-4">
      <ACCDButton size="sm" @click="isCancel = false">
        {{ $t("module.distributor.back") }}
      </ACCDButton>
      <ACCDButton type="danger" size="sm" @click="closeDialog">
        {{ $t("module.distributor.confirm") }}
      </ACCDButton>
    </div>
  </template>
</ACCDModal> -->
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { EFormState } from '@/enums'
import type { IDistributor, ISelectOption, IAddress } from '@/types'
import {
	getProvinceList,
	getDistrictList,
	getWardList,
	createDistributor,
	updateDistributor,
	deleteDistributor,
} from '@/modules/distributor/api'
import CDInput from './CDInput.vue'
import { CDForm, CDFormItem } from '@cd/design-system'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import { emitter } from '@/utils/mitt'

export default defineComponent({
	setup() {
		return {
			EFormState,
			v$: useVuelidate(),
		}
	},
	components: {
		CDFormItem,
		CDForm,
		CDInput,
	},
	data() {
		return {
			instanceKey: new Date().getTime(),
			duplicateDistributorCode: false,
			invalid: false,
			isCancel: false,
			isOpen: false,
			modalState: EFormState.VIEW as EFormState,
			item: {
				code: '',
				name: '',
				provinceId: '',
				districtId: '',
				wardId: '',
				address: '',
				contactName: '',
				contactPhone: '',
			} as unknown as IDistributor,
			provinceOptions: [] as ISelectOption[],
			districtOptions: [] as ISelectOption[],
			wardOptions: [] as ISelectOption[],
			responseErrorMessages: '' as string,
			existDistributorError: '' as string,
			isErrorPhoneNumber: false,
		}
	},
	validations() {
		return {
			item: {
				code: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				name: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
				},
				contactPhone: {
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
					return this.$t('module.distributor.titleCreate')
				case EFormState.EDIT:
					return this.$t('module.distributor.titleEdit')
				case EFormState.VIEW:
					return this.$t('module.distributor.titleView')
				case EFormState.DELETE:
					return (
						this.$t('module.distributor.delete') + ' ' + this.item.name + '?'
					)
				default:
					return ''
			}
		},
	},
	watch: {
		'item.provinceId': {
			handler(newVal: number, oldVal: number) {
				if (newVal && newVal != oldVal) {
					this.districtOptions = []
					getDistrictList(newVal).then((res) => {
						this.districtOptions = res.data.data.map((item: IAddress) => {
							return {
								value: item.id,
								label: item.name,
							}
						})
					})
				}
			},
		},
		'item.districtId': {
			handler(newVal: number, oldVal: number) {
				if (newVal && newVal != oldVal) {
					this.wardOptions = []

					getWardList(newVal).then((res) => {
						this.wardOptions = res.data.data.map((item: IAddress) => {
							return {
								value: item.id,
								label: item.name,
							}
						})
					})
				}
			},
		},
	},
	created() {
		emitter.on('pages-layout-on-confirmClose', (ik) => {
			if (ik == this.instanceKey) {
				this.closeDialog()
			}
		})

		getProvinceList().then((res) => {
			this.provinceOptions = res.data.data.map((item: IDistributor) => {
				return {
					value: item.id,
					label: item.name,
				}
			})
		})
	},
	methods: {
		onClickBack() {
			emitter.emit('layout-pages-open-confirmClose', this.instanceKey)
		},
		openDialog(data: IDistributor, state: EFormState) {
			this.isOpen = true
			this.modalState = state
			this.invalid = false
			this.isErrorPhoneNumber = false
			if (data) {
				this.item = { ...data, address: data.address?.split(', ')[0] }
			} else {
				this.item = {} as IDistributor
			}
		},
		closeDialog() {
			this.duplicateDistributorCode = false
			this.invalid = false
			this.isOpen = false
			this.isCancel = false
			this.resetForm()
			emitter.emit('layout-page-close-confirmClose')
		},
		resetForm() {
			this.responseErrorMessages = ''
			this.item = {} as IDistributor
		},
		handleChangePhone() {
			this.isErrorPhoneNumber = false
			this.invalid = false
		},
		async onSubmit() {
			this.responseErrorMessages = ''
			const result = await this.v$.$validate()
			if (!result) {
				this.invalid = true
				return
			} else {
				this.invalid = false
				this.duplicateDistributorCode = false
			}
			if (
				this.item.contactPhone &&
				!this.item.contactPhone.match(
					/([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/
				)
			) {
				this.isErrorPhoneNumber = true
				return
			}
			if (this.modalState === EFormState.ADD) {
				createDistributor(this.item).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.distributor.createSuccess'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.duplicateDistributorCode = true
						this.existDistributorError = res.message || res.msg
					}
				})
			}
			if (this.modalState === EFormState.EDIT) {
				updateDistributor(this.item, this.item.id).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.distributor.editSuccess'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						this.duplicateDistributorCode = true
						this.responseErrorMessages = res.message
					}
				})
			}

			if (this.modalState === EFormState.DELETE) {
				deleteDistributor(this.item.id).then((res) => {
					if (res.code == 1) {
						this.$toast(this.$t('module.distributor.deleteSuccess'), true)
						this.$emit('refresh')
						this.closeDialog()
					} else {
						// if (res.message == 'Thông tin nhà phân phối đã tồn tại') {
						this.duplicateDistributorCode = true
						// }
						this.responseErrorMessages = res.message
					}
				})
			}
		},
	},
})
</script>

<style scoped></style>
