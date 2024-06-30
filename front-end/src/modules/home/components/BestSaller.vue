<template>
	<div class="rounded-md bg-white p-4 h-full">
		<div class="flex gap-4 items-center justify-between">
			<p class="font-bold">{{ $t('module.home.service.bestSaller') }}</p>
			<ACCDButton type="primary">{{
				$t('module.home.service.quotation')
			}}</ACCDButton>
		</div>

		<ACCDTable class="mt-4" :columns="columnData" :rowData="rowData">
			<template #cell-image="{ row, col, field }">
				<img :src="row.image" alt="image" width="50px" />
			</template>
		</ACCDTable>
		<p class="mt-4 more">{{ $t('module.home.service.viewMore') }}</p>
	</div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { onMounted } from 'vue'

const { $t } = useI18n()

type ServicePopularProps = {
	name: string
	money: number
}

type DataProps = {
	data: ServicePopularProps[]
}

const props = withDefaults(defineProps<DataProps>(), {})
const columnData = ref([
	{
		key: 'productId',
		headerName: $t('module.home.service.table.productId'),
	},
	{
		key: 'image',
		minWidth: '80px',
	},
	{
		key: 'productName',
		headerName: $t('module.home.service.table.productName'),
	},
	{
		key: 'sale',
		headerName: $t('module.home.service.table.sale'),
	},
])

const rowData = [
	{
		productId: 'CTR302',
		productName: 'Cần gạt mưa Toyota Vios',
		sale: '1,029',
		image:
			'https://s3.ap-southeast-1.amazonaws.com/cardoctor/uploads/product-1691988971-2.jpg',
	},
	{
		productId: 'CTR302',
		productName: 'Cần gạt mưa Toyota Vios',
		sale: '1,029',
		image:
			'https://s3.ap-southeast-1.amazonaws.com/cardoctor/uploads/product-1691988971-2.jpg',
	},
	{
		productId: 'CTR302',
		productName: 'Cần gạt mưa Toyota Vios',
		sale: '1,029',
		image:
			'https://s3.ap-southeast-1.amazonaws.com/cardoctor/uploads/product-1691988971-2.jpg',
	},
]
</script>

<style lang="scss" scoped>
.more {
	color: #4355f5;
	font-weight: 600;
	cursor: pointer;
}
</style>
