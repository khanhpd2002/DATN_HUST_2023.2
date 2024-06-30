<template>
	<ACCDModal
		@close="closeDialog"
		class-width="md:w-[422px]"
		v-model="isOpen"
		:title="$t('module.productPrice.form.title')"
	>
		<div class="flex flex-col gap-4 mx-1 py-4">
			<div>
				<p class="font-semibold mb-1">
					{{ $t('module.productPrice.form.productType') }}
				</p>
				<ACCDSelect
					:placeholder="$t('module.productPrice.form.productType')"
					size="md"
					v-model="item.type"
					:options="listProductType"
					:disabled="true"
					class="font-medium"
				>
				</ACCDSelect>
			</div>
			<div v-if="item.type === 1">
				<p class="font-semibold mb-1">
					{{ $t('module.productPrice.form.productCode') }}
				</p>
				<ACCDInputText
					:placeholder="$t('module.productPrice.form.productCode')"
					size="md"
					v-model="item.productCode"
					:disabled="true"
					class="font-medium"
				>
				</ACCDInputText>
			</div>
			<div v-if="item.type === 2">
				<p class="font-semibold mb-1">
					{{ $t('module.productPrice.form.productCode') }}
				</p>
				<ACCDInputText
					:placeholder="$t('module.productPrice.form.productCode')"
					size="md"
					v-model="item.garageServiceCode"
					:disabled="true"
					class="font-medium"
				>
				</ACCDInputText>
			</div>
			<div v-if="item.type === 1">
				<p class="font-semibold mb-1">
					{{ $t('module.productPrice.form.productName') }}
				</p>
				<ACCDInputText
					:placeholder="$t('module.productPrice.form.productName')"
					size="md"
					v-model="item.productName"
					:disabled="true"
					class="font-medium"
				>
				</ACCDInputText>
			</div>
			<div v-if="item.type === 2">
				<p class="font-semibold mb-1">
					{{ $t('module.productPrice.form.productName') }}
				</p>
				<ACCDInputText
					:placeholder="$t('module.productPrice.form.productName')"
					size="md"
					v-model="item.garageServiceName"
					:disabled="true"
					class="font-medium"
				>
				</ACCDInputText>
			</div>
			<div v-for="productPrice in item.configPrices">
				<div>
					<p class="font-semibold mb-1">
						{{
							$t('module.productPrice.form.price') +
							' ' +
							productPrice.customerTypeName
						}}
					</p>
					<ACCDInputPrice
						:placeholder="$t('module.productPrice.form.productName')"
						size="md"
						v-model="productPrice.price"
						@keypress="validateNumber"
						class="font-medium"
					>
					</ACCDInputPrice>
				</div>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton type="secondary" variant="fill" @click="onCloseDialog">
					<span class="text-info-base font-medium">{{
						$t('module.productPrice.action.back')
					}}</span></ACCDButton
				>
				<ACCDButton type="primary" variant="fill" @click="onSubmit">
					<span class="text-white font-medium">{{
						$t('module.productPrice.action.save')
					}}</span></ACCDButton
				>
			</div>
		</template>
	</ACCDModal>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useProducts } from '@/modules/product/store'
import { createPrice, updatePrice } from '@/modules/productPrice/api'
import { getListProducts } from '@/modules/product/api'
import { IConfigPrice, ISelectOption } from '@/types'
import { cloneDeep } from 'lodash'
import { validateNumber } from '@/modules/sharedModules/component/constants'
import { EFormState } from '@/enums'
import { emitter } from '@/utils/mitt'

export default defineComponent({
	data() {
		return {
			instanceKey: new Date().getTime(),
			isOpen: false,
			item: {} as IConfigPrice,
			responseErrorMessages: '' as string,
			productId: 0,
			garageServiceId: 0,
			type: 0,
			listProductType: [
				{
					value: 1,
					label: this.$t('module.productPrice.productTypeOptions.1'),
				},
				{
					value: 2,
					label: this.$t('module.productPrice.productTypeOptions.2'),
				},
			],
			listProduct: [] as ISelectOption[],
			customerTypeList: [] as string[],
		}
	},
	computed: {
		listServiceTypes() {
			return (
				useProducts().listServiceTypes?.map((item) => ({
					id: item.id,
					value: this.$t(`module.product.${item.code}`),
				})) || []
			)
		},
	},
	created() {
		this.onGetListProducts()
		emitter.on('pages-layout-on-confirmClose', (ik) => {
			if (ik == this.instanceKey) {
				this.closeDialog()
			}
		})
	},
	methods: {
		validateNumber,
		openDialog(
			productId?: number,
			garageServiceId?: number,
			type?: number,
			data?: IConfigPrice
		) {
			this.isOpen = true
			this.productId = productId || 0
			this.garageServiceId = garageServiceId || 0
			this.type = type || 0
			this.item = data ? { ...cloneDeep(data) } : ({} as IConfigPrice)
			for (let i = 0; i < this.item.configPrices.length; i++) {
				this.customerTypeList.push(this.item.configPrices[i].customerTypeName)
			}
		},
		onCloseDialog() {
			emitter.emit('layout-pages-open-confirmClose', this.instanceKey)
		},
		closeDialog() {
			this.isOpen = false
			this.resetForm()
			emitter.emit('layout-page-close-confirmClose', this.instanceKey)
		},
		resetForm() {
			this.responseErrorMessages = ''
		},
		onChangeType() {
			this.item.productId = 0
			this.onGetListProducts()
		},
		async onGetListProducts() {
			const result: any = await getListProducts({
				pageNumber: 1,
				pageSize: 1000,
				type: this.item.type,
			})
			if (result.code === 1) {
				this.listProduct = result.data.map((item: any) => ({
					label: item.code,
					value: item.id,
				}))
			}
		},
		async onSubmit() {
			const garageId = localStorage.getItem('garageId')
			if (!garageId) return
			const payload = {
				...this.item,
				modelId: this.type === 1 ? this.productId : this.garageServiceId,
			}
			for (let i = 0; i < payload.configPrices.length; i++) {
				payload.configPrices[i].modelId = payload.modelId
			}
			const result =
				this.productId || this.garageServiceId
					? await updatePrice(
							payload,
							this.type === 1 ? this.productId : this.garageServiceId,
							this.type
					  )
					: await createPrice(payload)
			if (result?.code === 1) {
				this.$toast(
					this.$t(
						`module.product.${this.productId ? 'editSuccess' : 'createSuccess'}`
					),
					true
				)
				this.$emit('refresh')
				this.closeDialog()
			} else {
				this.$toast(
					result?.message ||
						this.$t(
							`module.product.${this.productId ? 'editFailed' : 'createFailed'}`
						),
					false
				)
			}
		},
	},
})
</script>

<style scoped></style>
