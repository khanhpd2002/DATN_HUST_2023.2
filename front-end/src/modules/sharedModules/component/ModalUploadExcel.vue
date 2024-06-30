<template>
	<CDModal
		title="Tải lên từ Excel"
		v-model="visible"
		class-width="md:w-[800px]"
	>
		<input
			ref="fileInputRef"
			type="file"
			hidden
			@change="onUpload"
			accept=".xls,.xlsx"
		/>
		<div class="block" @click="onTrigger">
			<div
				class="w-full text-base flex justify-center cursor-pointer items-center border border-line border-dashed rounded-md text-typo-secondary py-10"
			>
				<div class="flex flex-col items-center justify-center">
					<span class="text-grey-common"
						><ACCDAIcon name="DocumentUpload" size="80" type="bold" />
					</span>

					<p class="text-[24px] text-grey-common font-medium mt-[24px]">
						Bấm hoặc kéo thả vào đây để tải lên
					</p>
					<p
						class="text-active mt-2 font-medium"
						@click.stop="downloadTemplate"
						:loading="loadingDownloadTemplate"
					>
						Tải file mẫu
					</p>
				</div>
			</div>
		</div>
		<div
			class="mt-2 p-2 rounded-md flex justify-between items-center hover:bg-layout-muted"
			v-if="fileName && fileData"
		>
			<span class="line-clamp-1 text-primary">
				<CDIcon name="fa-solid fa-paperclip" class="mr-2" />
				{{ fileName }}
			</span>
			<ACCDAIcon name="Trash" class="cursor-pointer" @click="clearFile" />
		</div>
		<template #footer>
			<div>
				<CDButton
					type="primary"
					@click="onSubmitUpload"
					:loading="loadingSubmit"
				>
					Tải lên
				</CDButton>
			</div>
		</template>
		<CDNotify />
	</CDModal>
</template>

<script setup lang="ts">
import {
	CDButton,
	CDIcon,
	CDModal,
	CDNotify,
	CDUploaderSingle,
} from '@cd/design-system'
import { ref } from 'vue'
import { getExcelTemplate, uploadExcelFile } from '@/modules/sharedModules/api'
import { exportFileExcel } from '@/utils/file'
import { useToast } from '@/composables/useToast'
import { useI18n } from '@/composables/useI18n'

const visible = ref(false)
const tmpTemplateUrl = ref('')
const tmpTemplateName = ref('')
const tmpSubmitUrl = ref('')
const fileData = ref()
const fileName = ref('')
const loadingDownloadTemplate = ref(false)
const loadingSubmit = ref(false)
const open = ({
	templateUrl = '',
	templateName = '',
	submitUrl = '',
}: {
	templateUrl: string
	templateName: string
	submitUrl: string
}) => {
	visible.value = true
	fileName.value = ''
	fileData.value = null
	tmpTemplateUrl.value = templateUrl || ''
	tmpTemplateName.value = templateName || ''
	tmpSubmitUrl.value = submitUrl || ''
}
defineExpose({
	open,
})
const { $toast } = useToast()
const { $t } = useI18n()
const downloadTemplate = async () => {
	if (!tmpTemplateUrl.value) return
	loadingDownloadTemplate.value = true
	const result = await getExcelTemplate(tmpTemplateUrl.value)
	if (!result?.error) {
		exportFileExcel(result.data, tmpTemplateName.value || 'template.xlsx')
	} else {
		$toast('Có lỗi xảy ra', false)
	}
	loadingDownloadTemplate.value = false
}
const onUpload = async (event: Event) => {
	if (!event) return
	const target = event.target as HTMLInputElement
	if (target && target.files) {
		await uploadFile(target.files[0])
	}
}
const uploadFile = async (file: File) => {
	if (!file) return
	if (!validateFileType(file, 'application')) {
		return $toast('Định dạng file không đúng', false)
	}
	fileName.value = file.name
	fileData.value = file
}
const validateFileType = (file: File, type: string): boolean => {
	if (!file) return false
	const fileType = file.type.indexOf(type)
	return fileType !== -1
}
const fileInputRef = ref()
const onTrigger = () => {
	fileInputRef?.value?.click()
}
const onSubmitUpload = async () => {
	if (!fileData.value) return
	const splitFileName = fileData.value.name.split('.')
	if (
		splitFileName[splitFileName.length - 1] != 'xlsx' &&
		splitFileName[splitFileName.length - 1] != 'xls'
	) {
		return $toast('Định dạng file không đúng', false)
	}

	loadingSubmit.value = true

	const formData = new FormData()

	formData.append('file', fileData.value)
	const resultUpload = await uploadExcelFile(tmpSubmitUrl.value, formData)
	// TODO: handle upload success
	console.log('rés úp loads', resultUpload)
	if (!resultUpload || (resultUpload.code && resultUpload.code != 1)) {
		if (resultUpload.message) {
			$toast(resultUpload.message, false)
		} else {
			$toast($t('Tải lên File Excel không thành công'), false)
		}
	} else {
		$toast('Tải lên File Excel thành công', true)
		emit('success')
		close()
	}
	loadingSubmit.value = false
}
const clearFile = () => {
	fileData.value = null
	fileName.value = ''
	fileInputRef.value.value = null
}
const emit = defineEmits<{
	(e: 'success'): void
}>()
const close = () => {
	visible.value = false
	clearFile()
}
</script>

<style scoped lang="scss">
.uploader-content {
	:deep(.inline-block) {
		display: block !important;
	}
}
</style>
