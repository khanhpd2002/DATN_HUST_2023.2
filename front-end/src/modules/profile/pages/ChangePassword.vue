<template>
	<div class="bg-white rounded p-4">
		<CDForm :show-footer="false" class="w-full">
			<p class="text-xl font-bold text-black border-bottom">
				{{ $t('module.profile.form.title') }}
			</p>
			<CDFormItem
				:label="$t('module.profile.form.oldPassword')"
				required
				class="w-full mr-4 mb-8"
				:message="oldPasswordMessage"
			>
				<!-- <CDInputText
					size="md"
					:placeholder="$t('module.profile.form.newPassword')"
					v-model="formValue.oldPass"
					:type="isShowOldPass ? 'text' : 'password'"
					style="text-security: disc; -webkit-text-security: disc"
					autocomplete="off"
					:inValid="Boolean(oldPasswordMessage)"
				>
					<template #inner-append>
						<div @click="() => handleChangeType()" class="mr-4">
							<CDAIcon name="Eye" v-if="!isShowOldPass" size="20"></CDAIcon>
							<CDAIcon name="EyeSlash" v-else size="20"></CDAIcon></div
					></template>
				</CDInputText> -->

				<div class="mt-2 password-group">
					<input
						id="password"
						name="password"
						:type="isShowOldPass ? 'text' : 'password'"
						v-model="formValue.oldPass"
						autocomplete="off"
						:required="true"
						class="input-custom"
						:class="Boolean(oldPasswordMessage) && 'error'"
						:placeholder="$t('module.profile.form.oldPassword')"
					/>
					<div
						class="cursor-pointer absolute top-[8px] right-[8px]"
						@click="() => handleChangeType()"
					>
						<CDIcon
							name="fa fa-eye"
							v-if="!isShowOldPass"
							color="black"
						></CDIcon>
						<CDIcon name="fa fa-eye-slash" v-else color="black"></CDIcon>
					</div>
				</div>
			</CDFormItem>
			<CDFormItem
				:label="$t('module.profile.form.newPassword')"
				required
				class="w-full mr-4 mb-8"
				:message="newPasswordMessage"
			>
				<!-- <CDInputText
					size="md"
					:placeholder="$t('module.profile.form.newPassword')"
					v-model="formValue.newPass"
					:type="isShowNewPass ? 'text' : 'password'"
					style="text-security: disc; -webkit-text-security: disc"
					autocomplete="off"
					:inValid="Boolean(newPasswordMessage)"
				>
					<template #inner-append>
						<div @click="() => handleChangeType(1)" class="mr-4">
							<CDAIcon name="Eye" v-if="!isShowNewPass" size="20"></CDAIcon>
							<CDAIcon name="EyeSlash" size="20" v-else></CDAIcon></div

                            {{ isShowNewPass }}
					></template>
				</CDInputText> -->

				<div class="mt-2 password-group">
					<input
						id="password"
						name="password"
						:type="isShowNewPass ? 'text' : 'password'"
						v-model="formValue.newPass"
						autocomplete="off"
						:required="true"
						class="input-custom"
						:class="Boolean(newPasswordMessage) && 'error'"
						:placeholder="$t('module.profile.form.newPassword')"
					/>
					<div
						class="cursor-pointer absolute top-[8px] right-[8px]"
						@click="() => handleChangeType(1)"
					>
						<CDIcon
							name="fa fa-eye"
							v-if="!isShowNewPass"
							color="black"
						></CDIcon>
						<CDIcon name="fa fa-eye-slash" v-else color="black"></CDIcon>
					</div>
				</div>
			</CDFormItem>
			<CDFormItem
				:label="$t('module.profile.form.confirmPassword')"
				required
				class="w-full mr-4 mb-8"
				:message="confirmNewPasswordMessage"
			>
				<!-- <CDInputText
					size="md"
					:placeholder="$t('module.profile.form.newPassword')"
					v-model="formValue.confirmPass"
					:type="isShowConfirmPass ? 'text' : 'password'"
					style="text-security: disc; -webkit-text-security: disc"
					autocomplete="off"
					:inValid="Boolean(confirmNewPasswordMessage)"
				>
					<template #inner-append>
						<div @click="() => handleChangeType(2)" class="mr-4">
							<CDAIcon name="Eye" v-if="!isShowConfirmPass" size="20"></CDAIcon>
							<CDAIcon name="EyeSlash" size="20" v-else></CDAIcon>
						</div>
					</template>
				</CDInputText> -->
				<div class="mt-2 password-group">
					<input
						id="password"
						name="password"
						:type="isShowConfirmPass ? 'text' : 'password'"
						v-model="formValue.confirmPass"
						autocomplete="off"
						:required="true"
						class="input-custom"
						:class="Boolean(confirmNewPasswordMessage) && 'error'"
						:placeholder="$t('module.profile.form.confirmPassword')"
					/>

					<div
						class="cursor-pointer absolute top-[8px] right-[8px]"
						@click="() => handleChangeType(2)"
					>
						<CDIcon
							name="fa fa-eye"
							v-if="!isShowConfirmPass"
							color="black"
						></CDIcon>
						<CDIcon name="fa fa-eye-slash" color="black" v-else></CDIcon>
					</div>
				</div>
			</CDFormItem>
			<p class="italic text-red-500" v-if="errorMsg">
				{{ errorMsg }}
			</p>
		</CDForm>

		<div class="flex justify-end gap-2 w-full border-top">
			<CDButton type="secondary" @click="handleCancel">{{
				$t('module.profile.cancel')
			}}</CDButton>
			<CDButton type="primary" @click="onSubmit" :disabled="isDisabled">{{
				$t('module.profile.saveChange')
			}}</CDButton>
		</div>
	</div>
</template>
<script setup lang="ts">
import {
	CDButton,
	CDForm,
	CDFormItem,
	CDAIcon,
	CDIcon,
	CDInputText,
} from '@cd/design-system'

import { ref } from 'vue'
import { useToast } from '@/composables/useToast'
import { watch } from 'vue'
import { emitter } from '@/utils/mitt'
import { useI18n } from '@/composables/useI18n'
import { changePassword } from '@/modules/profile/api'
import router from '@/router'

const { $toast } = useToast()
const { $t } = useI18n()
const errorMsg = ref('')
const confirmNewPasswordMessage = ref('')
const newPasswordMessage = ref('')
const oldPasswordMessage = ref('')

const isShowOldPass = ref<boolean>(false)
const isShowNewPass = ref<boolean>(false)
const isShowConfirmPass = ref<boolean>(false)
const isDisabled = ref<boolean>(true)

const formValue = ref({
	oldPass: '',
	newPass: '',
	confirmPass: '',
})
const handleChangeType = (type?: number) => {
	console.log('type', type)
	if (type == 1) {
		isShowNewPass.value = !isShowNewPass.value
	} else if (type == 2) {
		isShowConfirmPass.value = !isShowConfirmPass.value
	} else {
		isShowOldPass.value = !isShowOldPass.value
	}
}

const onSubmit = async () => {
	confirmNewPasswordMessage.value = ''
	newPasswordMessage.value = ''
	if (formValue.value.oldPass == '') {
		oldPasswordMessage.value = 'Mật khẩu cũ không được để trống'
		return
	}

	if (formValue.value.newPass == '') {
		newPasswordMessage.value = 'Mật khẩu mới không được để trống'
		return
	}

	if (formValue.value.confirmPass == '') {
		confirmNewPasswordMessage.value =
			'Nhập lại mật khẩu mới không được để trống'
		return
	}

	if (formValue.value.oldPass == formValue.value.newPass) {
		oldPasswordMessage.value = 'Mật khẩu mới trùng với Mật khẩu cũ'
		newPasswordMessage.value = 'Mật khẩu mới trùng với Mật khẩu cũ'
		return
	}

	if (formValue.value.newPass != formValue.value.confirmPass) {
		confirmNewPasswordMessage.value =
			'Mật khẩu mới và nhập lại mật khẩu mới không trùng khớp'
		return
	}
	const data = {
		oldPassword: formValue.value.oldPass,
		newPassword: formValue.value.newPass,
		retypeNewPassword: formValue.value.confirmPass,
	}

	const res = await changePassword(data)
	if (res.code == 1) {
		$toast('Thay đổi mật khẩu thành công', true)
		setTimeout(async () => {
			await router.push('/')
		}, 1000)
	} else if (res.code == 400) {
		oldPasswordMessage.value = res.message
	} else {
		$toast(res.message, false)
	}
}

watch(
	() => formValue.value,
	() => {
		isDisabled.value = !(
			formValue.value.oldPass != '' ||
			formValue.value.newPass != '' ||
			formValue.value.confirmPass != ''
		)
		errorMsg.value = ''
		confirmNewPasswordMessage.value = ''
		newPasswordMessage.value = ''
		oldPasswordMessage.value = ''
	},
	{ deep: true }
)

const handleCancel = () => {
	emitter.emit('on-change-current-tab')
}
</script>

<style lang="scss" scoped>
.title {
	font-size: 16px;
}
.password-group {
	position: relative;
}
.error {
	border: 1px solid #f14c68;
}
.border-bottom {
	border-bottom: 1px solid #e5e7eb;
	padding: 16px 0;
	margin-bottom: 16px;
}

.border-top {
	border-top: 1px solid #e5e7eb;
	padding-top: 16px;
	margin-top: 16px;
}
</style>
