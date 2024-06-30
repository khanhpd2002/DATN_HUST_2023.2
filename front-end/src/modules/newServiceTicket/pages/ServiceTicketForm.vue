<template>
	<ACCDDialog
		:model-value="lazyValue"
		:title="computedTitle"
		class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
	>
		<template #header>
			<div class="flex justify-between">
				<h1 class="font-bold pb-1 text-typo text-[20px]">
					{{ $t('module.newServiceTicket.form.create') }}
				</h1>
				<ACCDIcon
					name="fa-solid fa-xmark"
					class="cursor-pointer"
					@click="lazyValue = false"
				/>
			</div>
		</template>
		<div class="cd-modal__body py-4">
			<ACCDForm :show-footer="false">
				<div class="flex flex-col lg:flex-row md:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.phone')"
						required
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.phone')"
							size="md"
							:disabled="
								serviceTicketForm.customerPhone.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							v-model="serviceTicketForm.customerPhone.value"
							:options="serviceTicketForm.customerPhone.options"
							:forCreate="true"
							:autoCreate="true"
							@keypress="validateNumber"
							class="font-medium"
							@update:modelValue="(val: any)=>handleChangeCustomerPhone(val)"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.customerType')"
						required
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.customerType')"
							:disabled="
								serviceTicketForm.customerTypeId.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							v-model="serviceTicketForm.customerTypeId.value"
							:options="serviceTicketForm.customerTypeId.options"
							size="md"
							class="font-medium"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.customerName')"
						required
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDInputText
							:disabled="
								serviceTicketForm.customerName.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							:placeholder="$t('module.newServiceTicket.form.customerName')"
							v-model="serviceTicketForm.customerName.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row md:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.licensePlate')"
						required
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.licensePlate')"
							:disabled="
								serviceTicketForm.licensePlate.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							v-model="serviceTicketForm.licensePlate.value"
							:options="serviceTicketForm.licensePlate.options"
							size="md"
							:forCreate="true"
							:autoCreate="true"
							@update:modelValue="(val: any)=>handleChangeLicensePlate(val)"
							class="font-medium"
							@focus="handleFocusLicensePlate"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.vinNumber')"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDInputText
							:placeholder="$t('module.newServiceTicket.form.vinNumber')"
							:disabled="
								serviceTicketForm.vinNumber.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							v-model="serviceTicketForm.vinNumber.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.journey')"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDInputPrice
							:placeholder="$t('module.newServiceTicket.form.odo')"
							:disabled="
								serviceTicketForm.odo.disabled || action == EFormState.VIEW
							"
							v-model="serviceTicketForm.odo.value"
							size="md"
							class="font-medium"
						></ACCDInputPrice>
					</ACCDFormItem>
				</div>
				<div class="flex flex-col lg:flex-row md:flex-row gap-4">
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carBrand')"
						:required="!isCarTypeOther"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:disabled="
								serviceTicketForm.carBrandId.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT ||
								isCarTypeOther
							"
							:placeholder="$t('module.newServiceTicket.form.carBrand')"
							v-model="serviceTicketForm.carBrandId.value"
							:options="serviceTicketForm.carBrandId.options"
							size="md"
							class="font-medium"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carModel')"
						:required="!isCarTypeOther"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.carModel')"
							:disabled="
								serviceTicketForm.carModelId.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT ||
								isCarTypeOther
							"
							v-model="serviceTicketForm.carModelId.value"
							:options="serviceTicketForm.carModelId.options"
							size="md"
							class="font-medium"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carYear')"
						:required="!isCarTypeOther"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.carYear')"
							:disabled="
								serviceTicketForm.carYearId.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT ||
								isCarTypeOther
							"
							v-model="serviceTicketForm.carYearId.value"
							:options="serviceTicketForm.carYearId.options"
							size="md"
							class="font-medium"
						></ACCDAutoComplete>
					</ACCDFormItem>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carVersion')"
						:required="!isCarTypeOther"
						class="w-full lg:w-1/3 lg:w-1/2 font-semibold"
					>
						<ACCDAutoComplete
							:placeholder="$t('module.newServiceTicket.form.carVersion')"
							:disabled="
								serviceTicketForm.carVersionId.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT ||
								isCarTypeOther
							"
							v-model="serviceTicketForm.carVersionId.value"
							:options="serviceTicketForm.carVersionId.options"
							size="md"
							class="font-medium"
						>
						</ACCDAutoComplete>
					</ACCDFormItem>
				</div>

				<div>
					<div class="flex gap-3 items-center">
						<ACCDCheckbox
							v-model="isCarTypeOther"
							:disabled="
								serviceTicketForm.carBrandId.value || isDisableCarTypeOther
							"
						>
						</ACCDCheckbox>
						<span class="font-semibold">
							{{ $t('module.newServiceTicket.form.carType') }}</span
						>
					</div>

					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.carName')"
						required
						v-if="isCarTypeOther"
						class="font-semibold"
					>
						<ACCDInputText
							:placeholder="$t('module.newServiceTicket.form.carName')"
							:disabled="
								serviceTicketForm.carName.disabled ||
								action == EFormState.VIEW ||
								action == EFormState.EDIT
							"
							v-model="serviceTicketForm.carName.value"
							size="md"
							class="font-medium"
						></ACCDInputText>
					</ACCDFormItem>
				</div>

				<div class="flex gap-3 mb-3">
					<div class="w-1/2">
						<span class="font-semibold">
							{{ $t('module.newServiceTicket.form.appointmentDate') }}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="serviceTicketForm.appointmentDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
					<div class="w-1/2">
						<span class="font-semibold">
							{{ $t('module.newServiceTicket.form.handOverDate') }}</span
						>
						<ACCDDatePicker
							size="md"
							v-model="serviceTicketForm.expectedHandoverDate.value"
							formatter="DD/MM/YY"
							class="font-medium"
						/>
					</div>
				</div>
				<div>
					<ACCDFormItem
						:label="$t('module.newServiceTicket.form.requirement')"
						required
						class="font-semibold"
					>
						<ACCDTextArea
							:disabled="
								serviceTicketForm.description.disabled ||
								action == EFormState.VIEW
							"
							v-model="serviceTicketForm.description.value"
							:placeholder="$t('module.newServiceTicket.form.requirement')"
							class="font-medium"
						>
						</ACCDTextArea>
					</ACCDFormItem>
				</div>
			</ACCDForm>
		</div>

		<p class="italic text-red-500" v-if="invalid">
			{{ errors.find((error) => error)?.message }}
		</p>
		<p v-if="errorMessage" class="italic text-red-500">
			{{ errorMessage }}
		</p>
		<p v-if="errorDate" class="italic text-red-500">
			{{ errorDate }}
		</p>
		<template #footer>
			<div class="flex justify-end gap-2.5 w-full">
				<ACCDButton @click="onClickBack" type="secondary" size="md">
					{{ $t('module.newServiceTicket.action.back') }}
				</ACCDButton>
				<ACCDButton
					v-if="action != EFormState.VIEW"
					@click="onSubmit"
					type="primary"
					size="md"
					:disabled="isDisabled"
				>
					{{ $t('module.newServiceTicket.action.create') }}
				</ACCDButton>
			</div>
		</template>
	</ACCDDialog>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { emitter } from '@/utils/mitt'
import { EFormState } from '@/enums'
import { $toast } from '@/main'
import { ISelectOption } from '@/types'
import { getCars } from '@/modules/car/api'
import {
	getCarBrandList,
	getCarModelList,
	getCarVersionList,
	getCarYearList,
	saveLogTracking,
} from '@/modules/sharedModules/api'
import { getCustomerTypes } from '@/modules/customerType/api'
import {
	createOrder,
	updateOrder,
	getDetailOrder,
} from '@/modules/newServiceTicket/api'
import { getCustomersByGarageId } from '@/modules/sellingManagement/api'
import useVuelidate from '@vuelidate/core'
import { helpers, required } from '@vuelidate/validators'
import { useI18n } from '@/composables/useI18n'
import { validateNumber } from '@/modules/sharedModules/component/constants'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayjs from 'dayjs'

dayjs.extend(customParseFormat)

const { $t } = useI18n()
type ModalProps = {
	modelValue: boolean
	action: number
	id?: string
}

const props = withDefaults(defineProps<ModalProps>(), {
	modelValue: true,
	action: EFormState.ADD,
	id: '',
})

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
		value: null,
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

	carName: {
		value: '',
		disabled: false,
	},
	appointmentDate: {
		value: dayjs().format('DD/MM/YY'),
	},
	expectedHandoverDate: {
		value: '',
	},
})
const errorMessage = ref('')
const invalid = ref(false)
const allCarOfGarageOptions = ref([])
const isCarTypeOther = ref<boolean>(false)
const isDisableCarTypeOther = ref<boolean>(false)
const errorDate = ref('')
const customerOptions = ref<ISelectOption[]>([])
const customerCarOptions = ref<ISelectOption[]>([])
const isDisabled = ref<boolean>(false)

watch(
	() => isCarTypeOther.value,
	() => {
		if (!isCarTypeOther.value) {
			serviceTicketForm.value.carBrandId.value = ''
			serviceTicketForm.value.carVersionId.value = ''
			serviceTicketForm.value.carModelId.value = ''
			serviceTicketForm.value.carYearId.value = ''
			// serviceTicketForm.value.carName.value = "";
		} else {
			serviceTicketForm.value.carBrandId.value = ''
			serviceTicketForm.value.carVersionId.value = ''
			serviceTicketForm.value.carModelId.value = ''
			serviceTicketForm.value.carYearId.value = ''
			// serviceTicketForm.value.carName.value = "";
		}
	}
)

watch(
	() => serviceTicketForm.value.carBrandId.value,
	(newVal: string) => {
		serviceTicketForm.value.carModelId.options = []
		if (serviceTicketForm.value.carBrandId.value && !isCarTypeOther.value)
			getCarModelList(newVal).then((res) => {
				serviceTicketForm.value.carModelId.options = res.data.data.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
		if (!serviceTicketForm.value.carBrandId.disabled) {
			serviceTicketForm.value.carVersionId.value = ''
			serviceTicketForm.value.carModelId.value = ''
			serviceTicketForm.value.carYearId.value = ''
		}
	}
)
watch(
	() => serviceTicketForm.value.carModelId.value,
	(newVal: string) => {
		serviceTicketForm.value.carYearId.options = []
		if (serviceTicketForm.value.carModelId.value && !isCarTypeOther.value)
			getCarYearList(newVal).then((res) => {
				serviceTicketForm.value.carYearId.options = res.data.data.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
		if (!serviceTicketForm.value.carModelId.disabled) {
			serviceTicketForm.value.carYearId.value = ''
			serviceTicketForm.value.carVersionId.value = ''
		}
	}
)
watch(
	() => serviceTicketForm.value.carYearId.value,
	(newVal: string) => {
		serviceTicketForm.value.carVersionId.options = []
		if (serviceTicketForm.value.carYearId.value && !isCarTypeOther.value)
			getCarVersionList(newVal).then((res) => {
				serviceTicketForm.value.carVersionId.options = res.data.data.map(
					(item: any) => {
						return {
							value: item.id,
							label: item.title,
						}
					}
				)
			})
		if (!serviceTicketForm.value.carYearId.disabled) {
			serviceTicketForm.value.carVersionId.value = ''
		}
	}
)
// thay đổi phoneNumber, thay đổi customerType, customerId, name, listCars

const handleChangeCustomerPhone = (val: any) => {
	serviceTicketForm.value.customerId.value = null
	serviceTicketForm.value.customerTypeId.value = ''
	serviceTicketForm.value.customerName.value = ''
	serviceTicketForm.value.licensePlate.value = ''
}

watch(
	() => serviceTicketForm.value.customerPhone.value,
	async (val) => {
		let originOptions = customerOptions.value.find((a: ISelectOption) => {
			if (a.value == val) {
				return true
			}
		})

		if (originOptions) {
			let origin = originOptions.rawValue
			serviceTicketForm.value.customerTypeId.value = origin.customerTypeId
			serviceTicketForm.value.customerName.value = origin.fullName
			serviceTicketForm.value.customerId.value = origin.id
			serviceTicketForm.value.customerTypeId.disabled = true
			serviceTicketForm.value.customerName.disabled = true

			serviceTicketForm.value.licensePlate.options =
				allCarOfGarageOptions.value.filter((car: ISelectOption) => {
					return (
						car.rawValue.customerId == serviceTicketForm.value.customerId.value
					)
				})
			customerCarOptions.value = [
				...serviceTicketForm.value.licensePlate.options,
			]
		} else {
			serviceTicketForm.value.customerTypeId.disabled = false
			serviceTicketForm.value.customerName.disabled = false
			serviceTicketForm.value.licensePlate.options = []
			serviceTicketForm.value.carId.value = null
			serviceTicketForm.value.licensePlate.value = serviceTicketForm.value
				.licensePlate.value
				? serviceTicketForm.value.licensePlate.value
				: ''
		}
	}
)

watch(
	() => serviceTicketForm.value.licensePlate.value,
	async (val) => {
		let originOptions = customerCarOptions.value.find((a: ISelectOption) => {
			if (a.value == val) {
				return true
			}
		})

		if (originOptions) {
			serviceTicketForm.value.vinNumber.value = originOptions.rawValue.vinNumber
			serviceTicketForm.value.carBrandId.value =
				originOptions.rawValue.carBrandId
			serviceTicketForm.value.carModelId.value =
				originOptions.rawValue.carModelId
			serviceTicketForm.value.carYearId.value = originOptions.rawValue.carYearId
			serviceTicketForm.value.carVersionId.value =
				originOptions.rawValue.carVersionId
			serviceTicketForm.value.carId.value = originOptions.rawValue.id
			serviceTicketForm.value.carName.value = originOptions.rawValue.carName
			isCarTypeOther.value = !serviceTicketForm.value.carBrandId.value
			if (
				!serviceTicketForm.value.carBrandId.value ||
				serviceTicketForm.value.carBrandId.value !== ''
			) {
				isDisableCarTypeOther.value = true
				serviceTicketForm.value.carName.value = originOptions.rawValue.carName
			}
			//disable các field được fill
			serviceTicketForm.value.vinNumber.disabled = true
			serviceTicketForm.value.carBrandId.disabled = true
			serviceTicketForm.value.carModelId.disabled = true
			serviceTicketForm.value.carYearId.disabled = true
			serviceTicketForm.value.carVersionId.disabled = true
			serviceTicketForm.value.carName.disabled = true
		} else {
			serviceTicketForm.value.vinNumber.disabled = false
			serviceTicketForm.value.odo.disabled = false
			serviceTicketForm.value.carBrandId.disabled = false
			serviceTicketForm.value.carModelId.disabled = false
			serviceTicketForm.value.carYearId.disabled = false
			serviceTicketForm.value.carVersionId.disabled = false
			serviceTicketForm.value.carName.disabled = false
			serviceTicketForm.value.vinNumber.value = ''
			serviceTicketForm.value.carBrandId.value = ''
			serviceTicketForm.value.carModelId.value = ''
			serviceTicketForm.value.carYearId.value = ''
			serviceTicketForm.value.carVersionId.value = ''
			serviceTicketForm.value.carName.value = ''
			isCarTypeOther.value = false
			isDisableCarTypeOther.value = false
		}
	}
)

const instanceKey = new Date().getTime()
const closeDialog = () => {
	emitter.emit('layout-page-close-confirmClose', instanceKey)
}

onMounted(async () => {
	emitter.on('pages-layout-on-confirmClose', (ik) => {
		if (ik == instanceKey) {
			lazyValue.value = false
		}
	})

	await getOptions()

	if (
		props.action === EFormState.EDIT ||
		(props.action === EFormState.VIEW && props.id)
	) {
		const res = await getDetailOrder(props.id)

		serviceTicketForm.value.customerPhone.value =
			res.data.customerResponse.phoneNumber
		serviceTicketForm.value.customerTypeId.value =
			res.data.customerResponse.customerTypeId
		serviceTicketForm.value.customerName.value =
			res.data.customerResponse.fullName
		serviceTicketForm.value.licensePlate.value =
			res.data.carResponse.licensePlate
		serviceTicketForm.value.odo.value = res.data.odo
		serviceTicketForm.value.description.value = res.data.description
	}
})
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
				rawValue: c,
			}
		})
	}

	serviceTicketForm.value.licensePlate.options = allCarOfGarageOptions.value

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
				rawValue: a,
			}
		}
	)
	customerOptions.value = customerPhoneRes.data.map((a: any) => {
		return {
			value: a.phoneNumber,
			label: a.phoneNumber,
			rawValue: a,
		}
	})
}

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void
	(e: 'refresh'): void
}>()

const lazyValue = computed({
	get() {
		return props.modelValue
	},
	set(val: boolean) {
		emit('update:modelValue', val)
	},
})

const getCarByUser = (params: any) => {
	return getCars({ ...params, pageSize: 10000, pageNumber: 1 })
}

const validations = computed(() => {
	const isRequired = isCarTypeOther.value
		? {}
		: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
		  }
	const isRequiredCarType = isCarTypeOther.value
		? {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
		  }
		: {}

	return {
		item: {
			customerPhone: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				checkPhone: helpers.withMessage(
					'* Vui lòng nhập Số điện thoại đúng định dạng',
					helpers.regex(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g)
				),
				$lazy: true,
			},
			customerTypeId: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
			},
			customerName: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
			},
			licensePlate: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
			},
			carBrandId: isRequired,
			carModelId: isRequired,
			carYearId: isRequired,
			carVersionId: isRequired,
			carName: isRequiredCarType,
			description: {
				required: helpers.withMessage(
					$t('module.employee.error.missingRequiredField'),
					required
				),
				$lazy: true,
			},
		},
	}
})
const item = computed(() => {
	let item = {} as any
	Object.keys(serviceTicketForm.value).forEach((k) => {
		item[k] =
			serviceTicketForm.value[k as keyof typeof serviceTicketForm.value].value
	})
	return item
})
const v$ = useVuelidate(validations, { item })
const errors = computed(() => {
	return v$.value.$errors.map((item) => {
		return {
			key: item.$property,
			message: item.$message,
		}
	})
})

const computedTitle = computed(() => {
	if (props.action == EFormState.ADD) {
		return $t('module.newServiceTicket.form.create')
	}
	if (props.action == EFormState.VIEW) {
		return $t('module.newServiceTicket.form.view')
	}
	return $t('module.newServiceTicket.form.edit')
})

const onClickBack = () => {
	if (props.action != EFormState.VIEW) {
		emitter.emit('layout-pages-open-confirmClose', instanceKey)
	} else {
		lazyValue.value = false
	}
}

const handleFocusLicensePlate = () => {
	if (
		!serviceTicketForm.value.customerPhone.value &&
		!customerOptions.value.some(
			(el) => el.value == serviceTicketForm.value.customerPhone.value
		)
	) {
		serviceTicketForm.value.licensePlate.options = allCarOfGarageOptions.value
	}
}

const handleChangeLicensePlate = async (val: any) => {
	const result = await getCarByUser({
		customerId: serviceTicketForm.value.customerId.value,
		licensePlate: val,
	})
	if (result.code == 1 && result.data.length > 0) {
		if (!serviceTicketForm.value.customerPhone.value) {
			const customerIdByLicensePlate = result.data.find(
				(el) => el.licensePlate == val
			).customerId

			const customerPhone = serviceTicketForm.value.customerPhone.options.find(
				(el) => el.rawValue.id == customerIdByLicensePlate
			)?.value

			if (customerPhone) {
				serviceTicketForm.value.customerPhone.value = customerPhone
			}
		}
		let originOptions = serviceTicketForm.value.licensePlate.options.find(
			(a: ISelectOption) => {
				if (a.value == val) {
					return true
				}
			}
		)

		if (originOptions) {
			serviceTicketForm.value.vinNumber.value = originOptions.rawValue.vinNumber
			serviceTicketForm.value.carBrandId.value =
				originOptions.rawValue.carBrandId
			serviceTicketForm.value.carModelId.value =
				originOptions.rawValue.carModelId
			serviceTicketForm.value.carYearId.value = originOptions.rawValue.carYearId
			serviceTicketForm.value.carVersionId.value =
				originOptions.rawValue.carVersionId
			serviceTicketForm.value.carId.value = originOptions.rawValue.id
			serviceTicketForm.value.carName.value = originOptions.rawValue.carName
			isCarTypeOther.value = !serviceTicketForm.value.carBrandId.value
			if (
				!serviceTicketForm.value.carBrandId.value ||
				serviceTicketForm.value.carBrandId.value !== ''
			) {
				isDisableCarTypeOther.value = true
				serviceTicketForm.value.carName.value = originOptions.rawValue.carName
			}
			//disable các field được fill
			serviceTicketForm.value.vinNumber.disabled = true
			serviceTicketForm.value.carBrandId.disabled = true
			serviceTicketForm.value.carModelId.disabled = true
			serviceTicketForm.value.carYearId.disabled = true
			serviceTicketForm.value.carVersionId.disabled = true
			serviceTicketForm.value.carName.disabled = true
		}
	} else {
		serviceTicketForm.value.carId.value = null
	}
}

const onSubmit = async () => {
	const result = await v$.value.$validate()
	if (!result) {
		invalid.value = true
		return
	}
	saveLogTracking({
		logEvent: 'Click_CREATE_REPAIR_ORDER',
		garageId: localStorage.getItem('garageId'),
		actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
	})

	const startDate = dayjs(
		serviceTicketForm.value.appointmentDate.value,
		'DD/MM/YY'
	).format('YYYY-MM-DD')
	const endDate = dayjs(
		serviceTicketForm.value.expectedHandoverDate.value,
		'DD/MM/YY'
	).format('YYYY-MM-DD')

	if (dayjs(endDate).isBefore(startDate, 'day')) {
		errorDate.value = $t('module.newServiceTicket.error.errorDate')
		return
	}

	let data = {} as any
	Object.keys(serviceTicketForm.value).forEach((key: string) => {
		data[key] =
			serviceTicketForm.value[key as keyof typeof serviceTicketForm.value].value

		if (key == 'appointmentDate') {
			data[key] = dayjs(
				serviceTicketForm.value[key as keyof typeof serviceTicketForm.value]
					.value,
				'DD/MM/YY'
			).format('YYYY-MM-DD')
		}
	})

	isDisabled.value = true

	try {
		if (props.action == EFormState.ADD) {
			let res = await createOrder({
				...data,
				expectedHandoverDate: serviceTicketForm.value.expectedHandoverDate.value
					? dayjs(
							serviceTicketForm.value.expectedHandoverDate.value,
							'DD/MM/YY'
					  ).format('YYYY-MM-DD') + 'T00:00'
					: '',
			})
			if (res.code == 1) {
				$toast($t('module.serviceTicket.form.toast.createSuccess'), true)
				lazyValue.value = false
				emit('refresh')
			} else {
				errorMessage.value = '* ' + res.message
			}
		} else {
			let res = await updateOrder(props.id, data)

			if (res.code == 1) {
				$toast($t('module.serviceTicket.form.toast.updateSuccess'), true)
				lazyValue.value = false
				emit('refresh')
			} else {
				errorMessage.value = '* ' + res.message
			}
		}

		isDisabled.value = false
	} catch (error) {
		isDisabled.value = false
	}
}
</script>
