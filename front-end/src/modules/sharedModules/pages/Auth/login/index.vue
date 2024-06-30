<template>
	<div
		class="flex min-h-full flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8 min-h-screen"
	>
		<div class="sm:mx-auto sm:w-full sm:max-w-md">
			<img class="mx-auto h-28 w-auto" :src="logo" alt="Your Company" />
			<h2
				class="mt-6 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900"
			>
				Đăng nhập vào hệ thống quản lí Garage
			</h2>
		</div>
		<!--      <template #content >-->
		<div class="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
			<div class="bg-white px-6 py-12 shadow sm:rounded-lg sm:px-12">
				<form class="space-y-6" action="#" @submit.prevent="onSubmit">
					<div>
						<label
							for="username"
							class="block text-sm font-medium leading-6 text-gray-900"
							>Tên người dùng</label
						>
						<div class="mt-2">
							<input
								id="username"
								name="username"
								type="text"
								v-model="formValues.username"
								autocomplete="username"
								required=""
								class="pl-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-gray-600 sm:text-sm sm:leading-6"
							/>
						</div>
					</div>

					<div>
						<label
							for="password"
							class="block text-sm font-medium leading-6 text-gray-900"
							>Mật khẩu</label
						>
						<div class="mt-2 password-group">
							<input
								id="password"
								name="password"
								:type="isShow ? 'text' : 'password'"
								v-model="formValues.password"
								autocomplete="current-password"
								required=""
								class="pl-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
							/>
							<div
								class="cursor-pointer absolute top-[6px] right-[8px]"
								@click="handleChangeType"
							>
								<CDIcon name="fa fa-eye" v-if="!isShow"></CDIcon>
								<CDIcon name="fa fa-eye-slash" v-else></CDIcon>
							</div>
						</div>
						<span><i class="fa-solid fa-eye-slash"></i></span>
					</div>

					<div class="flex items-center justify-between">
						<div class="flex items-center">
							<input
								id="remember-me"
								name="remember-me"
								type="checkbox"
								v-model="formValues.email"
								class="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-600"
							/>
							<label
								for="remember-me"
								class="ml-3 block text-sm leading-6 text-gray-900"
								>Nhớ mật khẩu</label
							>
						</div>

						<!-- <div class="text-sm leading-6">
							<a
									href="#"
									class="font-semibold text-indigo-600 hover:text-indigo-500"
									>Forgot password?</a
							>
					</div> -->
					</div>

					<div>
						<button
							type="submit"
							class="flex w-full justify-center rounded-md px-3 py-1.5 login-button"
						>
							Đăng nhập
						</button>
					</div>
				</form>
			</div>
		</div>
		<!--      </template>-->

		<CDModal
			:title="$t('module.sharedModules.login.selectGarage')"
			v-model="visibleModal"
			@close="onCloseModal"
		>
			<ul>
				<li
					v-for="(garage, index) in listGarages"
					:key="index"
					@click="onSelectGarage(garage)"
					class="bg-slate-100 hover:bg-slate-200 px-4 py-2 rounded-md mb-2 cursor-pointer"
				>
					{{ garage?.name }}
				</li>
			</ul>
		</CDModal>
	</div>
</template>

<script>
import logoWhite from '@/assets/images/logo/logo-white.svg'
import logo from '@/assets/images/logo_cardoctor.svg'
import sideImg from '@/assets/images/logo_cardoctor.svg'
import { defineComponent } from 'vue'
import {
	getInventoryByGarage,
	login,
	saveLogTracking,
	verify,
} from '@/modules/sharedModules/api'
import { CDForm, CDFormItem, CDModal, CDIcon } from '@cd/design-system'
import { getAccountInfo } from '@/modules/profile/api'

export default defineComponent({
	components: { CDFormItem, CDForm, CDModal, CDIcon },
	created() {},
	data() {
		return {
			logoWhite,
			logo,
			sideImg,
			formValues: {},
			visibleModal: false,
			listGarages: [],
			garageOwner: {},
			isShow: false,
		}
	},
	methods: {
		async onGetListGarages() {
			return await verify()
		},
		async onSubmit() {
			let loginPayloadType = this.formValues
			const result = await login(loginPayloadType)

			if (result.code === 1) {
				localStorage.setItem('garage_token_type', result.data.tokenType)
				localStorage.setItem('garage_token', result.data.accessToken)
				const listGarageResult = await this.onGetListGarages()
				if (listGarageResult?.code === 1) {
					this.garageOwner = listGarageResult?.data?.garageOwner
					this.listGarages = listGarageResult?.data?.garages || []
					this.visibleModal = true
					localStorage.setItem('garageOwner', JSON.stringify(this.garageOwner))
					saveLogTracking({
						logEvent: 'Login_GMS_SUCCESS',
						garageId: localStorage.getItem('garageId'),
						actionBy: JSON.parse(localStorage.getItem('garageOwner')).userName,
					})
				} else {
					this.$toast(this.$t('module.sharedModules.login.noGarage'), false)
				}
			} else {
				saveLogTracking({
					logEvent: 'Login_GMS_FAIL',
					actionBy: loginPayloadType.username,
				})
				this.$toast(this.$t('module.sharedModules.login.loginFail'), false)
			}
		},
		async onSelectGarage(garage) {
			if (!garage) return
			localStorage.setItem('garageId', garage.id)
			localStorage.setItem('garage', JSON.stringify(garage))

			localStorage.setItem('avatar', garage.logo)

			this.visibleModal = false
			const result = await getInventoryByGarage(garage.id)

			if (result.code === 1) {
				if (result.data.length === 0) {
					this.$toast(
						this.$t('module.sharedModules.login.missingInventory'),
						false
					)
					return
				}
				localStorage.setItem('inventoryId', result.data[0].id)
			}

			const res = await getAccountInfo(garage.id)
			if (res.code == 1) {
				const accountInfo = {
					contacts: res.data.additionalInformation.contacts,
					banks: res.data.additionalInformation.banks,
					taxCode: res.data.taxCode,
					email: res.data.contactPointEmail,
					address: res.data.address,
				}
				localStorage.setItem('accountInfo', JSON.stringify(accountInfo))
			}

			// await this.$router.push('/app/sell/customers')
			window.location.assign('/app/sell/customers')
		},
		onCloseModal() {
			localStorage.removeItem('garage_token_type')
			localStorage.removeItem('garage_token')
			localStorage.removeItem('users')
		},

		handleChangeType() {
			this.isShow = !this.isShow
		},
	},
})
</script>
<style lang="scss" scoped>
.login-button {
	background: #25b3e8;
	line-height: 38px;
	color: white;
	font-weight: 600;
}

.password-group {
	position: relative;
}
</style>
