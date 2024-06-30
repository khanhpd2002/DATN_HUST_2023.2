<template>
	<ACCDDialog v-model="modelValue" class-width="md:w-[374px]" :showFooter="false">
		<div class="flex gap-3 pt-4 bg-layout-primary">
			<div
				class="rounded-full min-w-[28px] w-[28px] h-[28px] flex justify-center items-center bg-red-100"
			>
				<ACCDIcon
					name="fa fa-triangle-exclamation"
					class="text-red-600"
				></ACCDIcon>
			</div>
			<span>{{ $t('module.distributor.confirmCancel') }}</span>
		</div>
		<div class="flex justify-end gap-2 w-full mt-8">
			<ACCDButton size="sm" @click="closeDialog" type="secondary">
				{{ $t('module.distributor.back') }}
			</ACCDButton>
			<ACCDButton type="primary" class="bg-error" size="sm" @click="confirm">
				{{ $t('module.distributor.confirmCancelBtn') }}
			</ACCDButton>
		</div>
	</ACCDDialog>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { EFormState } from '@/enums'
import useVuelidate from '@vuelidate/core'

export default defineComponent({
	setup() {
		return {
			EFormState,
			v$: useVuelidate(),
		}
	},
	props: {
		modelValue: {
			default: false as boolean,
		},
	},
	data() {
		return {
			invalid: false,
			isOpen: false,
		}
	},
	methods: {
		closeDialog() {
			this.$emit('closeDialog')
		},
		confirm() {
			this.$emit('confirm')
		},
	},
})
</script>

<style scoped></style>
