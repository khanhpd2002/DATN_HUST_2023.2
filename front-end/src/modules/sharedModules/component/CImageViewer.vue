<template>
	<ACCDModal
		classWidth="w-10/12 md:4/5 lg:w-4/5"
		v-model="isOpenModal"
		:hidefooter="true"
	>
		<div class="flex justify-between border-b">
			<h6 class="font-bold text-[20px]">Ảnh</h6>
			<ACCDIcon
				name="fa-solid fa-xmark"
				@click="emit('closeModal')"
				class="cursor-pointer"
			></ACCDIcon>
		</div>
		<div class="flex gap-4 pt-4">
			<div class="h-[calc(70vh)] overflow-y-auto w-[135px]">
				<div class="flex flex-col gap-8 overflow-y-auto">
					<ACCDImg
						v-for="(img, i) in props.images"
						:key="i"
						class="rounded-lg cursor-pointer"
						:class="{
							'border-2 border-sky-500': viewIndexClone == i,
						}"
						:src="
							img.url && img.url.indexOf('http') == 0
								? img.url
								: baseImg + img.url
						"
						size="100px"
						@click="() => (viewIndexClone = i)"
					>
					</ACCDImg>
				</div>
			</div>
			<div class="my-auto py-8 flex flex-col gap-8">
				<ACCDAIcon
					name="ArrowUp2"
					size="20"
					style="font-size: 20px"
					class="cursor-pointer"
					@click="viewIndexClone = viewIndexClone > 0 ? viewIndexClone - 1 : 0"
				></ACCDAIcon>
				<ACCDAIcon
					name="ArrowDown2"
					size="20"
					style="font-size: 20px"
					class="cursor-pointer"
					@click="
						viewIndexClone =
							viewIndexClone < props.images.length - 1
								? viewIndexClone + 1
								: props.images.length - 1
					"
				></ACCDAIcon>
			</div>
			<div class="w-[calc(80vw)] h-[calc(79vh)] fill">
				<img
					v-if="props.images"
					class="rounded-lg cursor-pointer"
					:id="'image-viewer-' + instanceKey"
					ref="mainImageViewing"
					style="
						transform: scale(1);
						margin: 0 auto;
						display: block;
						transform: translate3d(0%, 0%, 0);
					"
					:src="
						props.images[viewIndexClone].url &&
						props.images[viewIndexClone].url.indexOf('http') == 0
							? props.images[viewIndexClone].url
							: baseImg + props.images[viewIndexClone].url
					"
				/>
				<div
					id="bar-wrap"
					ref="barWrap"
					class="flex items-center justify-center"
				>
					<ACCDAIcon
						name="SearchZoomOut1"
						size="24"
						class="text-white mr-2 text-[24px] cursor-pointer"
						@click="zoomOutImage"
					></ACCDAIcon>
					<div id="zoom-bar-cont" ref="zoomBarCont">
						<div id="slider" ref="slider">
							<div class="sl-top"></div>
							<div class="sl-bot"></div>
						</div>
					</div>
					<ACCDAIcon
						name="SearchZoomIn"
						size="24"
						class="text-white ml-2 text-[24px] cursor-pointer"
						@click="zoomInImage"
					>
					</ACCDAIcon>
				</div>
			</div>
		</div>
	</ACCDModal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { onMounted } from 'vue'
const baseImg = import.meta.env.VITE_UPLOAD_SERVICE

const zoomBarCont = ref()
const slider = ref()
const barWrap = ref()
const mainImageViewing = ref()
const emit = defineEmits<{
	(e: 'closeModal'): void
}>()
const isOpenModal = ref(true as boolean)
type ImageProps = { url: string }
type ModalProps = {
	images: ImageProps[]
	viewIndex: number
}
const viewIndexClone = ref(0)
const instanceKey = new Date().getTime()
let bar = document.querySelector('#zoom-bar-cont')
function sliderDrag(e: MouseEvent) {
	//Variables aren't set as global to account for possible window resize
	let x0 = zoomBarCont.value?.getBoundingClientRect().left
	let width = zoomBarCont.value?.offsetWidth
	let dX = e.clientX - x0
	let dPercent = dX / width
	let sliderOff = slider.value.offsetLeft

	slider.value.style.left = valBetween((dX / width) * 100, 0, 100) + '%'
	slider.value.style.leftValue = dX

	zoomImage(sliderOff, width, dPercent)
}
function sliderInit(e: Event) {
	e.preventDefault()
	document.addEventListener('mousemove', sliderDrag)
	document.addEventListener('mouseup', (e) => {
		document.removeEventListener('mousemove', sliderDrag)
	})
}

onMounted(() => {
	viewIndexClone.value = props.viewIndex
	setTimeout(() => {
		let slider = document.querySelector('#slider')

		slider?.addEventListener('mousedown', sliderInit)
		bar?.addEventListener('mousedown', sliderInit)
	}, 100)
})

const valBetween = (v: number, min: number, max: number) => {
	return Math.min(max, Math.max(min, v))
}
const zoomOutImage = () => {
	const width = zoomBarCont.value.offsetWidth
	const currentLeft =
		slider.value.style.leftValue != undefined
			? slider.value.style.leftValue
			: 60
	const sliderOff = slider.value.offsetLeft
	let dx = 0
	if (currentLeft >= 10) {
		dx = currentLeft - 10
	}
	slider.value.style.left = valBetween((dx / width) * 100, 0, 100) + '%'
	slider.value.style.leftValue = dx
	zoomImage(sliderOff, width, dx / width)
}

const zoomInImage = () => {
	const width = zoomBarCont.value.offsetWidth
	const currentLeft =
		slider.value.style.leftValue != undefined
			? slider.value.style.leftValue
			: 60
	const sliderOff = slider.value.offsetLeft
	let dx = 0
	if (currentLeft <= 120) {
		dx = currentLeft + 10
	} else {
		dx = 120
	}
	if (dx >= 120) {
		dx = 120
	}
	slider.value.style.left = valBetween((dx / width) * 100, 0, 100) + '%'
	slider.value.style.leftValue = dx
	zoomImage(sliderOff, width, dx / width)
}

let setZoomVal = function (val: number, percent: number, botVal = true) {
	return botVal
		? valBetween((percent * (1 - val)) / 0.5, 0, 1 - val) + val
		: valBetween(percent * (val * 2 - 2), val - 1, val * 2 - 2) - (val - 2)
}

const zoomImage = (sliderOff: number, width: number, dPercent: number) => {
	console.log(sliderOff, width, dPercent)
	mainImageViewing.value.style.transform =
		'translate3d(0%,0%,0) scale(' +
		(sliderOff / width < 0.5
			? setZoomVal(0.4, dPercent)
			: setZoomVal(3, dPercent, false)) *
			0.9
	;+')'
}
const props = withDefaults(defineProps<ModalProps>(), {
	viewIndex: 0,
})
watch(
	() => isOpenModal.value,
	(val) => {
		if (!val) {
			emit('closeModal')
		}
	}
)
</script>
<style scoped>
.fill {
	display: flex;
	justify-content: center;
	align-items: center;
	overflow: hidden;
}

.fill img {
	flex-shrink: 0;
	min-width: 100%;
	min-height: 100%;
}

#bar-wrap {
	position: fixed;
	left: 0;
	right: 0;
	margin: 0 auto;
	bottom: 0;
	border-top: 1px solid #444;
	background: rgba(0, 0, 0, 0.6);
	height: 80px;
}

#zoom-bar-cont {
	height: 4px;
	border-radius: 2px;
	width: 120px;
	background: #d9d9d9;
	position: relative;
}

#slider {
	height: 16px;
	width: 16px;
	border-radius: 50%;
	border: 1px solid white;
	background: rgba(67, 85, 245, 1);
	position: absolute;
	bottom: 50%;
	left: 50%;
	transform: translateY(50%) translateX(-50%);
	display: flex;
	justify-content: center;
}
</style>
