<template>
	<div class="w-full flex flex-wrap gap-2 items-center">
		<ACCDUploaderMultiple
			size="110px"
			uploadType="ECOMMERCE"
			:minioUrl="baseUpload"
			v-if="!props.viewOnly"
			@change="upLoadFile"
			format="image"
		>
			<div class="border-upload text-active font-medium cursor-pointer">
				Thêm ảnh
			</div>
		</ACCDUploaderMultiple>
		<div v-for="(im, index) in props.images" class="w-fit relative group">
			<!-- <ACCDIcon
				name="fa-regular fa-circle-xmark"
				class="absolute top-3 right-3 cursor-pointer hidden group-hover:block text-xl"
				v-if="!props.viewOnly && enableRemove"
				@click="() => removeImg(index)"
			></ACCDIcon> -->

			<div
				v-if="!props.viewOnly && enableRemove"
				@click="() => removeImg(index)"
				class="absolute cursor-pointer right-1 top-1"
			>
				<img src="@/assets/images/svg/close_upload.svg" />
			</div>

			<div class="absolute bottom-4 right-4 cursor-pointer">
				<ACCDCheckbox
					@update:model-value="$emit('triggerSelect')"
					v-model="im.isSelect"
					v-if="!props.viewOnly && enableSelect"
				></ACCDCheckbox>
			</div>
			<ACCDImg
				:key="index"
				@click="
					() => {
						indexViewing = index
						viewImg = true
					}
				"
				:src="im.url && im.url.indexOf('http') == 0 ? im.url : baseImg + im.url"
				class="rounded-lg cursor-pointer"
				size="110px"
			></ACCDImg>
		</div>

		<Teleport to="body">
			<CImageViewer
				v-if="viewImg"
				:viewIndex="indexViewing"
				@close-modal="viewImg = false"
				:images="props.images"
			>
			</CImageViewer>
		</Teleport>
	</div>
</template>

<script setup lang="ts">
import CImageViewer from './CImageViewer.vue'
import { computed, ref } from 'vue'

const baseImg = import.meta.env.VITE_UPLOAD_SERVICE
const baseUpload =
	import.meta.env.VITE_THIRD_PARTY_SERVICE + 'minio/storage/presign-ecommerce'
type MultiUploadImageProps = {
	viewOnly?: boolean
	images: {
		url: string
		isSelect?: boolean
	}[]
	enableRemove?: boolean
	enableSelect?: boolean
	setNew?: boolean
}
const removeImg = (i: number) => {
	lazyImages.value.splice(i, 1)
}
const indexViewing = ref(0)
const emit = defineEmits<{
	(
		e: 'update:images',
		val: {
			url: string
		}[]
	): void
	(e: 'triggerSelect'): void
}>()

const viewImg = ref(false)

const lazyImages = computed({
	get() {
		return props.images
	},
	set(
		val: {
			url: string
		}[]
	) {
		emit('update:images', val)
		console.log('update')
	},
})

const props = withDefaults(defineProps<MultiUploadImageProps>(), {
	viewOnly: false,
	images: () => [],
	enableRemove: true,
	enableSelect: false,
	setNew: false,
})
const upLoadFile = async (e: { url: string; filename: string }[]) => {
	for (let i = 0; i < e.length; i++)
		if (props.setNew) {
			lazyImages.value = lazyImages.value.concat(
				e.map((a) => {
					return { url: a.filename }
				})
			)
		} else {
			lazyImages.value.push({
				url: e[i].filename,
			})
		}
}
</script>
