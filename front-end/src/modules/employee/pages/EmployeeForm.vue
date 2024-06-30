<template>
	<ACCDModal
		v-model="lazyValue"
		:title="computedTitle"
		class-width="md:w-[568px]"
		@close="closeDialogConfirm"
	>
		<div class="cd-modal__body py-4">
			<CDForm :show-footer="false" class="w-full">
				<div>
					<CDFormItem
						:label="$t('module.employee.form.fullName')"
						required
						class="font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.employee.form.fullName')"
							v-model="item.fullName"
							class="font-medium"
							:trim="true"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.employee.form.gender')"
						required
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.employee.form.gender')"
							v-model="item.gender"
							:options="genderOptions"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.employee.form.birthday')"
						required
						class="font-semibold"
					>
						<ACCDDatePicker
							:placeholder="$t('module.employee.form.birthday')"
							v-model="item.birthday"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.employee.form.phoneNumber')"
						required
						class="font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.employee.form.phoneNumber')"
							v-model="item.phoneNumber"
							class="font-medium"
							:trim="true"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.employee.form.type')"
						required
						class="font-semibold"
					>
						<ACCDInputText
							size="md"
							:placeholder="$t('module.employee.form.type')"
							v-model="item.type"
							class="font-medium"
							:trim="true"
						/>
					</CDFormItem>

					<CDFormItem
						:label="$t('module.employee.form.contractType')"
						required
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.employee.form.contractType')"
							v-model="item.contractType"
							:options="contractTypeOptions"
							class="font-medium"
						/>
					</CDFormItem>
					<CDFormItem
						:label="$t('module.employee.form.startDate')"
						required
						class="font-semibold"
					>
						<ACCDDatePicker
							size="md"
							:placeholder="$t('module.employee.form.startDate')"
							v-model="item.startDate"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</CDFormItem>

					<CDFormItem
						:label="$t('module.employee.form.status')"
						class="font-semibold"
					>
						<ACCDSelect
							size="md"
							:placeholder="$t('module.employee.form.status')"
							v-model="item.status"
							:options="statusOptions"
							v-if="props.state == EFormState.EDIT"
							class="font-medium"
						/>
						<ACCDSelect
							v-else
							size="md"
							:placeholder="$t('module.employee.form.status')"
							v-model="item.status"
							:options="statusOptionsCreate"
							class="font-medium"
						/>
					</CDFormItem>

					<CDFormItem
						:label="$t('module.employee.form.endDate')"
						required
						v-if="item.status == 1"
						class="font-semibold"
					>
						<ACCDDatePicker
							size="md"
							:placeholder="$t('module.employee.form.endDate')"
							v-model="item.endDate"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</CDFormItem>

					<p
						class="italic text-red-500"
						v-if="
							errors.find((error) => error.key == 'phoneNumber')
								?.validatorType == 'checkPhone'
						"
					>
						* {{ errors.find((error) => error.key == 'phoneNumber')?.message }}
					</p>
					<p class="italic text-red-500" v-else-if="invalid">
						{{ $t('module.employee.error.missingRequiredField') }}
					</p>

					<p class="italic text-red-500" v-else-if="responseErrorMessages">
						{{ responseErrorMessages }}
					</p>

					<p class="italic text-red-500" v-else-if="birthdayMsgError">
						{{ birthdayMsgError }}
					</p>
					<p class="italic text-red-500" v-else-if="startDateMsgError">
						{{ startDateMsgError }}
					</p>
					<p class="italic text-red-500" v-else-if="endDateMsgError">
						{{ endDateMsgError }}
					</p>
				</div>
			</CDForm>
		</div>
		<template #footer>
			<div class="flex justify-end gap-2.5">
				<div class="w-full">
					<ACCDButton
						size="md"
						variant="fill"
						type="secondary"
						class="w-full"
						@click="closeDialog"
					>
						<span class="text-info-base font-medium">{{
							$t('module.employee.action.back')
						}}</span>
					</ACCDButton>
				</div>
				<div class="w-full">
					<ACCDButton
						v-if="
							props.state != EFormState.VIEW && props.state != EFormState.DELETE
						"
						size="md"
						type="primary"
						variant="fill"
						class="w-full"
						@click="onSubmit"
					>
						<span class="text-white font-medium">{{
							props.state == EFormState.ADD
								? $t('module.employee.action.save')
								: $t('module.employee.action.edit')
						}}</span>
					</ACCDButton>
				</div>
			</div>
		</template>
	</ACCDModal>
</template>

<script setup lang="ts">
import { EFormState } from '@/enums'
import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/composables/useToast'
import { computed, onMounted, ref } from 'vue'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import {
	createEmployee,
	getEmployeeById,
	updateEmployee,
} from '@/modules/employee/api'
import { IEmployee, ISelectOption } from '@/types'
import { emitter } from '@/utils/mitt'
import { CDForm, CDFormItem } from '@cd/design-system'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import { watch } from 'vue'
dayjs.extend(customParseFormat)

const { $t } = useI18n()
const { $toast } = useToast()

const responseErrorMessages = ref('')
const invalid = ref(false)
const id = ref<number | null>(null)

const item = ref({
	id: 0,
	fullName: '',
	birthday: null as string | null,
	type: '',
	gender: null,
	status: 0,
	contractType: null,
	startDate: dayjs().format('DD/MM/YY'),
	endDate: null as string | null,
	phoneNumber: '',
})
const genderOptions = ref<ISelectOption[]>([])
const statusOptions = ref<ISelectOption[]>([])
const statusOptionsCreate = ref<ISelectOption[]>([
	{
		value: 0,
		label: $t('module.employee.status.0'),
	},
])

const contractTypeOptions = ref<ISelectOption[]>([])

type ModalProps = {
	modelValue: boolean
	state: EFormState
	employeeId?: number
}

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: false,
	state: EFormState.ADD,
})

const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val) {
		emit('update:modelValue', val)
	},
})

const computedTitle = computed(() => {
	switch (props.state) {
		case EFormState.ADD:
			return $t('module.employee.form.addTitle')
		case EFormState.EDIT:
			return $t('module.employee.form.editTitle')
		case EFormState.VIEW:
			return $t('module.employee.form.viewTitle')
		default:
			return ''
	}
})

const validations = computed(() => {
	const isRequired =
		item.value.status != 1
			? {}
			: {
					required: helpers.withMessage('Thiếu thông tin rồi', required),
					$lazy: true,
			  }
	return {
		item: {
			fullName: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			gender: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			birthday: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			phoneNumber: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				checkPhone: helpers.withMessage(
					'Vui lòng nhập Số điện thoại đúng định dạng',
					helpers.regex(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g)
				),
				$lazy: true,
			},
			type: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			contractType: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			startDate: {
				required: helpers.withMessage('Thiếu thông tin rồi', required),
				$lazy: true,
			},
			endDate: isRequired,
		},
	}
})
const v$ = useVuelidate(validations, { item })
const errors = computed(() => {
	return v$.value.$errors.map((item) => {
		return {
			key: item.$property,
			message: item.$message,
			validatorType: item.$validator,
		}
	})
})
const instanceKey = new Date().getTime()

const birthdayMsgError = ref<string>('')
const startDateMsgError = ref<string>('')
const endDateMsgError = ref<string>('')

// watch(
// 	() => item.value.birthday,
// 	() => {
// 		birthdayMsgError.value = ''
// 	},
// 	{ deep: true }
// )

// watch(
// 	() => item.value.startDate,
// 	() => {
// 		startDateMsgError.value = ''
// 	}
// )

// watch(
// 	() => item.value.endDate,
// 	() => {
// 		endDateMsgError.value = ''
// 	}
// )

watch(
	() => item.value,
	() => {
		invalid.value = false
		endDateMsgError.value = ''
		startDateMsgError.value = ''
		birthdayMsgError.value = ''
	},
	{ deep: true }
)

const closeDialogConfirm = () => {
	emitter.emit('layout-page-close-confirmClose', instanceKey)
}

onMounted(async () => {
	if (props.employeeId) {
		id.value = props.employeeId
		const result = await getEmployeeById(props.employeeId)
		item.value = result ? result.data : null
		item.value.birthday = item.value.birthday
			? dayjs(item.value.birthday).format('DD/MM/YY')
			: null
		item.value.startDate = item.value.startDate
			? dayjs(item.value.startDate).format('DD/MM/YY')
			: null
		item.value.endDate = item.value.endDate
			? dayjs(item.value.endDate).format('DD/MM/YY')
			: null
	}

	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
		}
	})
})

const handleGenerateOptions = () => {
	for (let i = 0; i < 3; ++i) {
		statusOptions.value.push({
			label: $t(`module.employee.status.${i}`),
			value: i,
		})
	}
	for (let i = 1; i < 3; ++i) {
		contractTypeOptions.value.push({
			label: $t(`module.employee.contractType.${i}`),
			value: i,
		})
	}
	for (let j = 0; j < 3; ++j) {
		genderOptions.value.push({
			label: $t(`module.employee.gender.${j}`),
			value: j,
		})
	}
}

handleGenerateOptions()
const closeDialog = () => {
	emitter.emit('layout-pages-open-confirmClose', instanceKey)
	invalid.value = false
}

const resetForm = () => {
	responseErrorMessages.value = ''
	invalid.value = false
}

const emit = defineEmits<{
	(e: 'refresh'): void
	(e: 'update:modelValue', val: boolean): void
}>()

const onSubmit = async () => {
	const result = await v$.value.$validate()

	if (!result) {
		invalid.value = true
		return
	} else {
		invalid.value = false
	}

	const currentDate = dayjs().format('YYYY-MM-DD')
	const birthday = dayjs(item.value.birthday, 'DD/MM/YY').format('YYYY-MM-DD')
	const startDate = dayjs(item.value.startDate, 'DD/MM/YY').format('YYYY-MM-DD')
	const endDate = dayjs(item.value.endDate, 'DD/MM/YY').format('YYYY-MM-DD')

	if (dayjs(currentDate).isBefore(birthday, 'day')) {
		birthdayMsgError.value = $t('module.employee.error.errorBirthDay')
	} else if (
		dayjs(startDate).isBefore(birthday, 'day') ||
		dayjs(startDate).isSame(birthday, 'day')
	) {
		startDateMsgError.value = $t('module.employee.error.errorStartDate')
	} else if (dayjs(endDate).isBefore(startDate, 'day')) {
		endDateMsgError.value = $t('module.employee.error.errorEndDate')
	}
	if (
		birthdayMsgError.value ||
		startDateMsgError.value ||
		endDateMsgError.value
	) {
		return
	}

	const data = {} as any
	Object.keys(item.value).map((k) => {
		data[k] = item.value[k as keyof typeof item.value]
	})
	data.birthday = data.birthday
		? dayjs(data.birthday, 'DD/MM/YY').format('YYYY-MM-DD')
		: null
	data.startDate = data.startDate
		? dayjs(data.startDate, 'DD/MM/YY').format('YYYY-MM-DD')
		: null
	data.endDate = data.endDate
		? dayjs(data.endDate, 'DD/MM/YY').format('YYYY-MM-DD')
		: null

	const response = id.value
		? await updateEmployee(data as IEmployee, id.value)
		: await createEmployee(data as IEmployee)
	const successMessage = id.value
		? $t('module.employee.toast.editSuccess')
		: $t('module.employee.toast.createSuccess')
	if (response.code === 1) {
		$toast(successMessage, true)
		lazyValue.value = false
		emit('refresh')
		resetForm()
	} else {
		responseErrorMessages.value = '*' + response.message
	}
}
</script>

<style scoped></style>
