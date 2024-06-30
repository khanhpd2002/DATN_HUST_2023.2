<template>
	<ACCDModal
		v-model="lazyValue"
		:title="modalTitle"
		class-width="md:w-[422px]"
		@close="closeDialog"
	>
		<div>
			<div class="flex flex-col gap-4 mx-1 py-4">
				<div>
					<p class="mb-1 text-gray-700 font-semibold">
						{{ $t('module.garageServiceController.form.garageServiceType') }}
						<span class="text-red-500">*</span>
					</p>
					<ACCDSelect
						v-if="props.state === 'create' || props.state === 'add-new'"
						size="md"
						:placeholder="
							$t('module.garageServiceController.form.garageServiceType')
						"
						v-model="formConfig.garageServiceTypeId.value"
						:options="garageServiceTypeOptions"
						:forCreate="true"
						class="font-medium"
					/>
					<ACCDSelect
						v-if="props.state === 'edit'"
						size="md"
						:placeholder="
							$t('module.garageServiceController.form.garageServiceType')
						"
						v-model="formConfig.garageServiceTypeId.value"
						:options="garageServiceTypeOptions"
						class="font-medium"
					/>
				</div>
				<div>
					<p class="mb-1 text-gray-700 font-semibold">
						{{ $t('module.garageServiceController.form.name') }}
						<span class="text-red-500">*</span>
					</p>
					<ACCDInputText
						size="md"
						:placeholder="
							$t('module.garageServiceController.form.placeholderName')
						"
						v-model="formConfig.name.value"
						class="font-medium"
						:trim="true"
					/>
					<div class="text-red-600 mt-1" v-if="formConfig.name.errorMessage">
						<span>
							{{ formConfig.name.errorMessage }}
						</span>
					</div>
				</div>
				<div>
					<p class="mb-1 text-gray-700 font-semibold">
						{{ $t('module.garageServiceController.form.code') }}
					</p>
					<ACCDInputText
						size="md"
						:placeholder="
							$t('module.garageServiceController.form.placeholderCode')
						"
						v-model="formConfig.code.value"
						class="font-medium"
						:trim="true"
					/>
					<div class="text-red-600 mt-1" v-if="formConfig.code.errorMessage">
						<span>
							{{ formConfig.code.errorMessage }}
						</span>
					</div>
				</div>
				<div>
					<p class="mb-1 text-gray-700 font-semibold">
						{{ $t('module.garageServiceController.form.description') }}
					</p>
					<ACCDInputText
						size="md"
						:placeholder="$t('module.garageServiceController.form.description')"
						v-model="formConfig.description.value"
						class="font-medium"
						:trim="true"
					/>
					<div
						class="text-red-600 mt-1"
						v-if="formConfig.description.errorMessage"
					>
						<span>
							{{ formConfig.description.errorMessage }}
						</span>
					</div>
				</div>
			</div>
			<div class="italic text-red-600 mt-4" v-if="responseErrorMessages">
				<span>
					{{ responseErrorMessages }}
				</span>
			</div>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton @click="onClickBack" type="secondary" size="md">
					{{ $t('module.distributor.back') }}
				</ACCDButton>

				<ACCDButton @click="onSubmit" type="primary" size="md">
					{{
						props.state === 'edit'
							? $t('module.garageServiceController.action.edit')
							: $t('module.garageServiceController.action.create')
					}}
				</ACCDButton>
			</div>
		</template>
	</ACCDModal>
</template>

<script setup lang="ts">
import { $toast } from '@/main.js'

const { $t } = useI18n()
import { ref, computed, onMounted } from 'vue'
import {
	createService,
	updateService,
	BaseIAccessary,
	getDetailService,
	getListGarageServiceTypeId,
} from '../api'
import { useI18n } from '@/composables/useI18n'
import { emitter } from '@/utils/mitt'
import { ISelectOption } from '@/types'
import { watch } from 'vue'

const garageServiceTypeOptions = ref([] as ISelectOption[])
const StateOptions: string[] = ['create', 'edit', 'add-new']

type ModalProps = {
	modelValue: boolean
	state: (typeof StateOptions)[number]
	serviceId?: string
	serviceType?: number
	serviceName?: string
	instanceKeyRowService?: number
}
const originValue = ref(BaseIAccessary)
const responseErrorMessages = ref('')
const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: false,
	state: 'create',
	serviceType: 0,
	serviceName: '',
	instanceKeyRowService: 0,
})
const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val) {
		emit('update:modelValue', val)
	},
})

const closeDialog = () => {
	emitter.emit('layout-page-close-confirmClose')
	emitter.emit('on-remove-service', props)
}

const onSubmit = async () => {
	let onValidte = true
	Object.keys(formConfig.value).map((k) => {
		if (
			!formConfig.value['garageServiceTypeId'].value &&
			!formConfig.value['garageServiceTypeName'].value
		) {
			{
				responseErrorMessages.value = $t(
					'module.garageServiceController.form.requiredFieldError'
				)
				onValidte = false
			}
		}
		if (
			formConfig.value[k as keyof typeof formConfig.value]?.isRequired &&
			formConfig.value[k as keyof typeof formConfig.value]?.value == ''
		) {
			responseErrorMessages.value = $t(
				'module.garageServiceController.form.requiredFieldError'
			)
			onValidte = false
		}
	})
	if (!onValidte) {
		return
	}
	if (props.state == 'create' || props.state == 'add-new') {
		let data = {} as any
		Object.keys(formConfig.value).map((k) => {
			data[k] = formConfig.value[k as keyof typeof formConfig.value].value
		})

		if (isNaN(Number(data.garageServiceTypeId))) {
			data.garageServiceTypeName = data.garageServiceTypeId
				? data.garageServiceTypeId.toString()
				: ''
			data.garageServiceTypeId = undefined
		}
		let res = await createService(data)
		if (res.code == 1) {
			$toast($t('module.garageServiceController.toast.createSuccess'), true)
			lazyValue.value = false
			const serviceData = {
				garageServiceTypeId: res.data.garageServiceTypeId,
				garageServiceId: res.data.garageServiceId,
				intanceKey: props.instanceKeyRowService,
			}
			emitter.emit('refresh')
			emitter.emit('on-change-data', serviceData)
			resetForm()
		} else {
			if (res.code != 99) {
				responseErrorMessages.value =
					'* Mã dịch vụ đã tồn tại vui lòng nhập lại'
				return
			}
			responseErrorMessages.value = '*' + res.message
		}
	} else if (props.state == 'edit') {
		let data = originValue.value
		let editedData = {} as any
		Object.keys(formConfig.value).map((k) => {
			editedData[k] = formConfig.value[k as keyof typeof formConfig.value].value
		})
		data = {
			...data,
			...editedData,
		}
		let res = await updateService(data, props.serviceId as string)
		if (res.code == 1) {
			$toast($t('module.garageServiceController.toast.editSuccess'), true)
			lazyValue.value = false
			emitter.emit('refresh')
			resetForm()
		} else {
			responseErrorMessages.value = res.message
		}
	}
}
const instanceKey = new Date().getTime()
onMounted(async () => {
	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
			emitter.emit('on-remove-service', props)
		}
	})

	const result = await getListGarageServiceTypeId()
	if (result.code == 1) {
		garageServiceTypeOptions.value = result.data.map(
			(item: IGarageServiceType) => {
				return {
					value: item.id,
					label: item.name,
				}
			}
		)
	}

	if (garageServiceTypeOptions.value.length > 0 && props.state == 'add-new') {
		const serviceEx = garageServiceTypeOptions.value.find(
			(item) => item.value == props.serviceType
		)
		if (serviceEx) {
			formConfig.value.garageServiceTypeId.value = serviceEx.value
		} else {
			const addNew = {
				value: props.serviceType,
				label: props.serviceType,
			}
			garageServiceTypeOptions.value =
				garageServiceTypeOptions.value.concat(addNew)
			formConfig.value.garageServiceTypeId.value = props.serviceType
		}
		formConfig.value.name.value = props.serviceName
	}

	if (garageServiceTypeOptions.value.length == 0 && props.state == 'add-new') {
		formConfig.value.garageServiceTypeId.value = props.serviceType
		const addNew = {
			value: props.serviceType,
			label: props.serviceType,
		}
		garageServiceTypeOptions.value =
			garageServiceTypeOptions.value.concat(addNew)
	}

	if (props.state == 'edit') {
		let res = await getDetailService(props.serviceId as string)
		if (res.code == 1) {
			originValue.value = res.data
			for (let k in originValue.value) {
				if (formConfig.value.hasOwnProperty(k)) {
					formConfig.value[k as keyof typeof formConfig.value].value =
						originValue.value[k as keyof typeof formConfig.value] as string
				}
			}
		}
	}
})

const resetForm = () => {
	Object.keys(formConfig.value).map((k) => {
		formConfig.value[k as keyof typeof formConfig.value].value = ''
	})
}
const emit = defineEmits<{
	(e: 'update:modelValue', val: boolean): void
}>()
const modalTitle = computed(() => {
	if (props.state == 'create' || props.state == 'add-new') {
		return $t('module.garageServiceController.form.createServiceTitle')
	} else {
		return $t('module.garageServiceController.form.editServiceTitle')
	}
})

const formConfig = ref({
	garageServiceTypeName: {
		value: '',
		isRequired: false,
		errorMessage: '',
	},
	garageServiceTypeId: {
		value: 0,
		isRequired: false,
		errorMessage: '',
	},
	name: {
		value: '',
		isRequired: true,
		errorMessage: '',
	},
	code: {
		value: '',
		isRequired: false,
		errorMessage: '',
	},
	description: {
		value: '',
		errorMessage: '',
		isRequired: false,
	},
})

watch(
	() => formConfig.value,
	() => {
		responseErrorMessages.value = ''
	},
	{ deep: true }
)

const onClickBack = () => {
	emitter.emit('layout-pages-open-confirmClose', instanceKey)
}
</script>
