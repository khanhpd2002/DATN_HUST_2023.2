<template>
	<div>
		<BlankLayout v-if="$route.meta.layout">
			<router-view />
		</BlankLayout>
		<PortalLayout style="height: 100vh" v-else>
			<router-view />
		</PortalLayout>
	</div>
	<DialogWarningBack
		v-model="openDialogWarningBack"
		@closeDialog="openDialogWarningBack = false"
		@confirm="confirm"
	>
	</DialogWarningBack>
	<CDNotify ref="notifyContainer" />
</template>

<script lang="ts" setup>
import BlankLayout from '@/modules/sharedModules/pages/Layout/BlankLayout.vue'
import DialogWarningBack from '@/modules/sharedModules/component/DialogWarningBack.vue'
import PortalLayout from '@/modules/sharedModules/pages/Layout/index.vue'
import { ref, onMounted } from 'vue'
import { CDNotify, useNotify } from '@cd/design-system'
const openDialogWarningBack = ref(false)
import { emitter } from '@/utils/mitt'
const currentLayoutInstanceKey = ref(0)
const confirm = () => {
	emitter.emit('pages-layout-on-confirmClose', currentLayoutInstanceKey.value)
	openDialogWarningBack.value = false
}
onMounted(() => {
	emitter.on('layout-pages-open-confirmClose', (instanceKey) => {
		currentLayoutInstanceKey.value = instanceKey as number
		openDialogWarningBack.value = true
	})
	emitter.on('layout-page-close-confirmClose', () => {
		openDialogWarningBack.value = false
	})
})
</script>

<style lang="scss">
.table-fixed-layout {
	table {
		table-layout: fixed !important;
	}
}
</style>
