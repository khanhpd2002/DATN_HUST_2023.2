<template>
	<div class="" v-if="!isLoading">
		<div class="flex gap-4">
			<div class="w-1/4 bg-white rounded">
				<div class="flex justify-center pt-[24px]">
					<label for="letter-file" class="relative">
						<img
							:src="
								formValue.logo
									? baseImg + formValue.logo
									: '/src/assets/images/logo_cardoctor.svg'
							"
							class="avatar"
						></img>
						<img
							src="@/assets/images/svg/camera-navy.svg"
							size="30px"
							class="upload"
						/>

						<input
							id="letter-file"
							type="file"
							class="hidden"
							@change="handleChangeLogo"
						/>
					</label>
				</div>
				<p class="italic text-red-500 text-center">{{ isErrorAvatar }}</p>

				<div class="text-center p-[24px]">
					<p class="font-bold text-[24px]">{{ formValue.name }}</p>
					<p class="font-medium text-[16px]">
						{{ formValue.role }}
					</p>
				</div>

				<div class="p-[16px] border-top">
					<p
						class="font-medium cursor-pointer"
						@click="() => handleChangeTab(1)"
						:class="currentTab == 1 && 'link-active'"
					>
						Thông tin tài khoản
					</p>
					<p
						class="font-medium cursor-pointer"
						@click="() => handleChangeTab(2)"
						:class="currentTab == 2 && 'link-active'"
					>
						Đổi mật khẩu
					</p>
				</div>
			</div>

			<div class="w-3/4">
				<Header />
				<div class="profile" v-if="currentTab == 1">
					<div>
						<div class="flex flex-col mt-4 w-full bg-white rounded p-[16px]">
							<div class="flex justify-between items-center border-bottom">
								<p class="font-bold text-[16px]">
									{{
										isEdit
											? $t('module.profile.form.editTitle')
											: $t('module.profile.form.titleInfo')
									}}
								</p>
								<CDButton v-if="!isEdit" @click="isEdit = true">
									{{ $t('module.profile.action.edit') }} </CDButton
								>
							</div>

							<div v-if="!isEdit">
								<div class="flex flex-col gap-3">
									<p class="font-bold gap-3 mt-4">
										{{ $t('module.profile.infor') }}
									</p>
									<div class="flex flex-col gap-3">
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.phoneNumber') }}
											</p>
											<p class="font-semibold" >{{ formValue.phone }}</p>					
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.name') }}
											</p>
											<p class="font-semibold" >{{ formValue.contactPointName }}</p>					
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.garageName') }}
											</p>
											<p class="font-semibold" >{{ formValue.name }}</p>					
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.address') }}
											</p>
											<p class="font-semibold">
												{{ formValue.address }}
											</p>
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.email') }}
											</p>
											<p class="font-semibold" >{{ formValue.email }}</p>					
										</div>
									
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.taxCode') }}
											</p>
											<p class="font-semibold">{{ formValue.taxCode }}</p>
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.role') }}
											</p>
											<p class="font-semibold">{{ formValue.role }}</p>
										</div>
										<div class="flex items-center">
											<p class="w-1/4 font-medium">
												{{ $t('module.profile.form.status') }}
											</p>
											<p class="font-semibold">{{ formValue.status }}</p>
										</div>

									</div>
								</div>

								<div class="flex flex-col">
									<p class="font-bold my-3">
										{{ $t('module.profile.contact') }}
									</p>
									<div class="flex flex-col gap-3">
										<div class="bg-grey w-1/2 p-3 rounded-lg" v-for="contact in formValue.contacts">

									
										<div class="flex items-center ">
											<p class="w-1/2 font-medium">
												{{ $t('module.profile.form.name') }}
											</p>
											<p class="font-semibold">{{ contact.name }}</p>
										</div>
										<div class="flex items-center">
											<p class="w-1/2 font-medium">
												{{ $t('module.profile.form.phone') }}
											</p>
											<p class="font-semibold">{{ contact.phone }}</p>
										</div>
									</div>
										
									</div>
								</div>

								<div class="flex flex-col">
									<p class="font-bold my-3">
										{{ $t('module.profile.accountInfo') }}
									</p>
									<div class="flex flex-col gap-3">
										<div class="bg-grey w-1/2 p-3 rounded-lg" v-for="bank in formValue.banks">

									
										<div class="flex items-center " v-if="bank">
											<p class="w-1/2 font-medium">
												{{ $t('module.profile.form.ownerAccount') }}
											</p>
											<p class="font-semibold">{{ bank.owner }}</p>
										</div>
										<div class="flex items-center" v-if="bank">
											<p class="w-1/2 font-medium">
												{{ $t('module.profile.form.accountNumber') }}
											</p>
											<p class="font-semibold">{{ bank.bankNumber
 }}</p>
										</div>
										<div class="flex items-center" v-if="bank">
											<p class="w-1/2 font-medium">
												{{ $t('module.profile.form.bank') }}
											</p>
											<p class="font-semibold">{{ bank.bankName
 }}</p>
										</div>
									</div>
										
									</div>
								</div>
							</div>

							<div v-if="isEdit">
								<CDForm :show-footer="false" class="w-full">
									<p class="font-bold my-4">{{ $t('module.profile.infor') }}</p>
									<CDFormItem
										:label="$t('module.profile.form.phone')"
										class="w-full mr-4 font-semibold"
									
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.phone')"
											v-model="formValue.phone"
											disabled
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.name')"
										class="w-full mr-4 font-semibold"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.name')"
											v-model="formValue.contactPointName"
											disabled
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.garageName')"
										class="w-full mr-4 font-semibold"
									
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.garageName')"
											v-model="formValue.name"
											disabled
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.address')"
										class="w-full mr-4 font-semibold"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.address')"
											v-model="formValue.address"
											disabled
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.email')"
										class="w-full mr-4 font-semibold"
									
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.email')"
											v-model="formValue.email"
											disabled
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.taxCode')"
										class="w-full mr-4 font-semibold"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.taxCode')"
											v-model="formValue.taxCode"
											disabled
										/>
									</CDFormItem>

<!--									<CDFormItem-->
<!--										:label="$t('module.profile.form.role')"-->
<!--										class="w-full mr-4 font-semibold"-->
<!--									-->
<!--									>-->
<!--										<CDInputText-->
<!--											size="md"-->
<!--											:placeholder="$t('module.profile.form.role')"-->
<!--											v-model="formValue.role"-->
<!--											disabled-->
<!--										/>-->
<!--									</CDFormItem>-->
<!--									<CDFormItem-->
<!--										:label="$t('module.profile.form.status')"-->
<!--										class="w-full mr-4 font-semibold"-->
<!--									>-->
<!--										<CDInputText-->
<!--											size="md"-->
<!--											:placeholder="$t('module.profile.form.status')"-->
<!--											v-model="formValue.status"-->
<!--											disabled-->
<!--										/>-->
<!--									</CDFormItem>-->

								

									<div class="bg-grey rounded-lg p-3">
										<p class="font-bold mb-4">
										{{ $t('module.profile.contact') }}
									</p>
									<div class="flex gap-3 mt-2" v-for="(item, index) in formValue.contacts">
										<CDFormItem
										:label="$t('module.profile.form.name')"
										class="w-1/2 font-semibold"
							
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.name')"
											v-model="item.name"
											
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.phoneNumber')"
										class="w-1/2 font-semibold "
										:message="item.error"
										
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.phoneNumber')"
											v-model="item.phone"
											:class="item.error ? 'border-error':''"
											@change="(val) =>handleChangePhoneNumber(item,index )"

										/>
									</CDFormItem>
									</div>
<div class="flex gap-2 text-active font-medium"> <span class="cursor-pointer "  @click="handleAddContact">	<ACCDAIcon name="AddCircle" /> </span>
									<span class="cursor-pointer mr-1"  @click="handleAddContact">
										{{  $t('module.profile.form.addContact')}}
				</span></div>
									
			

								
									</div>


									<div class="bg-grey rounded-lg p-3 mt-3">
										<p class="font-bold mb-4">
										{{ $t('module.profile.accountInfo') }}
									</p>
									<div class="flex gap-3"  v-for="(bank, index) in formValue.banks" :key="index">
										<CDFormItem
										:label="$t('module.profile.form.ownerAccount')"
										class="w-1/3 font-semibold"
										v-if="bank"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.ownerAccount')"
											v-model="bank.owner"
										
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.accountNumber')"
										class="w-1/3 font-semibold"
										v-if="bank"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.accountNumber')"
											v-model="bank.bankNumber"
								
										/>
									</CDFormItem>
									<CDFormItem
										:label="$t('module.profile.form.bank')"
										class="w-1/3 font-semibold"
										v-if="bank"
									>
										<CDInputText
											size="md"
											:placeholder="$t('module.profile.form.bank')"
											v-model="bank.bankName"
									
										/>
									</CDFormItem>
									</div>
<div class="flex gap-2 text-active font-medium"> <span class="cursor-pointer "  @click="handleAddBank">	<ACCDAIcon name="AddCircle" /> </span>
									<span class="cursor-pointer mr-1"  @click="handleAddBank">
										{{  $t('module.profile.form.addAccount')}}
				</span></div>
									
			

								
									</div>
								
									<!-- <CDFormItem :label="t('module.profile.form.role')" class="w-full mr-4">
								  <CDInputText
									size="md"
									:placeholder="t('module.profile.form.role')"
									v-model="formValue.role"
								  />
								</CDFormItem> -->

									<div class="flex gap-4 justify-end border-top-1">
										<CDButton type="secondary" @click="isEdit = false">{{
											$t('module.profile.action.cancel')
										}}</CDButton>
										<CDButton type="primary" @click="handleUpdateProfile">{{
											$t('module.profile.saveChange')
										}}</CDButton>
									</div>
								</CDForm>
							</div>
						</div>
					</div>
				</div>
				<div class="mt-3" v-if="currentTab == 2">
					<ChangePassword />
				</div>
			</div>
		</div>
	</div>
	<Loading :is-loading="isLoading" />
</template>

<script setup lang="ts">
import {
	CDButton,
	CDForm,
	CDFormItem,
	CDImg,
	CDInputText,
	useNotify,
} from '@cd/design-system'

import Loading from '@/modules/sharedModules/component/Loading.vue'
import { helpers, required } from '@vuelidate/validators'
import useVuelidate from '@vuelidate/core'
import Header from './Header.vue'
import { computed, onMounted, ref, watch } from 'vue'
import axios from 'axios'
import { emitter } from '@/utils/mitt'
import { useI18n } from '@/composables/useI18n'
import { getDetailProfile, updateLogo, updateAccountInfo, getAccountInfo } from '@/modules/profile/api'
import ChangePassword from '@/modules/profile/pages/ChangePassword.vue'
import { useToast } from '@/composables/useToast'
import _, { cloneDeep } from 'lodash'

const { $t } = useI18n()
const baseImg = import.meta.env.VITE_UPLOAD_SERVICE
const baseUpload =
	import.meta.env.VITE_THIRD_PARTY_SERVICE + 'minio/storage/presign-ecommerce'

const { notify } = useNotify()

const { $toast } = useToast()

const currentTab = ref<number>(1)


const formValue = ref({
	logo: '',
	contactPointName: '',
	name: '',
	role: 'Admin',
	status: 'Kích hoạt',
	phone: '',
	email: '',
	address:'',
	taxCode:'',
	contacts:[] as {name: string, phone: string, error?: string}[],
	banks:[] as {owner: string; bankNumber: string; bankName: string }[]
})

const validations = computed(() => {
	return {
		formValue: {
			displayName: {
				required: helpers.withMessage(
					'Tên hiển thị không được để trống',
					required
				),
				$lazy: true,
			},
			phone: {
				required: helpers.withMessage(
					'Số điện thoại không được để trống',
					required
				),
				checkPhone: helpers.withMessage(
					'Vui lòng nhập Số điện thoại đúng định dạng',
					helpers.regex(/^[0-9]{9,12}$/)
				),
				$lazy: true,
			},
			email: {
				required: helpers.withMessage('Email không được để trống', required),
				checkEmail: helpers.withMessage(
					'Vui lòng nhập đúng định dạng Email',
					helpers.regex(
						/^([_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*(\.[a-zA-Z]{1,6}))?$/
					)
				),
				$lazy: true,
			},
		},
	}
})

const v$ = useVuelidate(validations, { formValue })
const errors = computed(() => {
	return v$.value.$errors.map((item) => {
		return {
			key: item.$property,
			message: item.$message,
		}
	})
})

const isLoading = ref<boolean>(false)

const isEdit = ref(false)
const isErrorAvatar = ref<string>('')



type UploaderItem = {
	url: string
	filename: string
}

const handleChangePhoneNumber = (val: any, index: number) => {
	
	formValue.value.contacts.forEach((el, ind) => {
if(ind == index){
	el.error = ''
}
	})
}

const handleAddContact = () => {
	formValue.value.contacts ? formValue.value.contacts.push({name: '', phone: ''}) : formValue.value.contacts = [{name: '', phone: ''}]
}

const handleAddBank = () => {
	formValue.value.banks ? formValue.value.banks.push({owner: '', bankNumber: '', bankName: ''}) : formValue.value.banks = [{owner: '', bankNumber: '', bankName: ''}]
}

const handleChangeTab = (type: number) => {
	currentTab.value = type
}

const handleChangeLogo = async (e: any) => {
	const splitFileName = e.target.files[0].name.split('.')


	if (
		splitFileName[splitFileName.length - 1] != 'png' &&
		splitFileName[splitFileName.length - 1] != 'jpg' &&
		splitFileName[splitFileName.length - 1] != 'svg' &&
		splitFileName[splitFileName.length - 1] != 'jpeg'
	) {
		isErrorAvatar.value = 'Định dạng file không đúng'
		return
	} else {
		isErrorAvatar.value = ''
	}

	const fileName = e.target.files[0].name
	const presignUrls = await onGetPresign([fileName])
	const contents = (await readFileAsUint8Array(e.target.files[0])) as any

	await axios(`${presignUrls[0].url}`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'image/*',
		},
		data: contents,
	})

	formValue.value.logo = presignUrls[0].filename
	const params = {
		logo: presignUrls[0].filename,
	}

	await updateLogo(params)

	emitter.emit('on-update-avatar',presignUrls[0].filename )

	
}

const onGetPresign = async (
	fileNames: string[],
	uploadType = 'OTHER'
): Promise<UploaderItem[]> => {
	try {
		const res = await axios(baseUpload, {
			method: 'POST',
			data: {
				fileNames,
				uploadType,
			},
			headers: {
				'Content-Type': 'application/json',
			},
		})

		if (res?.status === 200 && res?.data?.data) {
			return res?.data?.data || []
		}

		return []
	} catch (e) {
		return []
	}
}

const readFileAsUint8Array = async (file: File) => {
	return new Promise((resolve, reject) => {
		const reader = new FileReader()
		reader.onload = (event) => {
			const arrayBuffer = event?.target?.result as ArrayBuffer
			const uint8Array = new Uint8Array(arrayBuffer)
			resolve(uint8Array)
		}
		reader.onerror = function (event) {
			reject(event?.target?.error)
		}
		reader.readAsArrayBuffer(file)
	})
}


const validatePhoneNumber = () => {
	if(formValue.value.contacts) {
		let phoneRegex = /([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/

let contacts = 	formValue.value?.contacts?.map((el) => {
		if(el.phone && !el.phone.match(phoneRegex)) {
return { ...el , error: $t('module.profile.error.phoneNumber')}
		} 
		return {...el}
	})

	formValue.value = {...formValue.value, contacts}
	}
	

}

const handleGetAccountInfo = async () => {

	try {
		const res = await getAccountInfo()

	
	if(res.code == 1){
		formValue.value.phone = res.data.contractPointPhone
		formValue.value.contactPointName = res.data.contactPointName
		formValue.value.name = res.data.name
		formValue.value.address = res.data.address
		formValue.value.email = res.data.contactPointEmail
		formValue.value.taxCode = res.data.taxCode
		formValue.value.contacts = res.data.additionalInformation.contacts
		formValue.value.banks =  res.data.additionalInformation.banks
		formValue.value.logo =   res.data.logo
	}
	} catch (error) {
		console.log(error);
	}
	
}


const handleUpdateProfile = async () => {

validatePhoneNumber()

if(formValue.value?.contacts?.some((el) =>el.error)) return
	const params = {
		contacts: formValue.value.contacts ? cloneDeep(formValue.value.contacts.filter((el) => !_.values(el).every((el) => !el)).map((el) => {
			delete el['error']
			return {...el}
		})) : [],
		banks:formValue.value.banks ? cloneDeep(formValue.value.banks.filter((el) => !_.values(el).every((el) => !el))) :[]
	}
	try {
		const res = await updateAccountInfo(params)
		
		
		if(res.code == 1){
			$toast($t('module.profile.toast.success'), true)
			isEdit.value = false
		}

		const result = await getAccountInfo()
			if (result.code == 1) {
				const accountInfo = {
					contacts: result.data.additionalInformation.contacts,
					banks: result.data.additionalInformation.banks,
					taxCode: result.data.taxCode,
					email: result.data.contactPointEmail,
					address: result.data.address,
				}
				localStorage.setItem('accountInfo', JSON.stringify(accountInfo))
			}
	} catch (error) {
		console.log(error);
	}

}

// const handleGetDetailProfile = async () => {
// 	const garageId = localStorage.getItem('garageId')
// 	const res = await getDetailProfile()
// 	if (res.code == 1) {
// 		formValue.value = {
// 			...formValue.value,
// 			garageOwnerName: res.data.garageOwner.name,
// 			phone: res.data.garageOwner.phone,
// 			email: res.data.garageOwner.email,
// 			logo: res.data.garages.find((el) => el.id == garageId)?.logo,
// 			garageName: JSON.parse(localStorage.getItem('garage')).name,
// 		}

// 		emitter.emit('on-set-avatar')
// 	}
// }

onMounted(async () => {
	// await handleGetDetailProfile()
	await handleGetAccountInfo()

	emitter.on('on-change-current-tab', () => {
		currentTab.value = 1
	})
	emitter.on('on-change-account-password', (data: any) => {
		currentTab.value = data
	})
})
</script>

<style lang="scss" scoped>
.border-error {
	border: 1px solid red;
}
.btn-upload {
	cursor: pointer;
	white-space: nowrap;
	border-radius: 8px;
	border: 1px solid;
	padding: 10px;
}
.avatar {
	width: 100px !important;
	height: 100px !important;
	border-radius: 100% !important;
}

img {
	object-fit: contain !important;
}

.upload {
	position: absolute;
	top: 60%;
	right: -10%;
	cursor: pointer;
}
.border-top {
	border-top: 1px solid #e5e7eb;
	p {
		padding: 16px;
	}
}

.border-bottom {
	border-bottom: 1px solid #e5e7eb;
	padding: 16px 0;
}

.border-top-1 {
	border-top: 1px solid #e5e7eb;
	margin-top: 16px;
	padding-top: 16px;
}
.error {
	border: 1px solid #ed1f42 !important;
}
</style>
