import { defineStore } from 'pinia'

import {
	getAllGarage,
	getGarageInforById,
	getAddressInfo,
	getAddressDetail,
	getAllRescues,
	getListSubSystem,
	updateGarage,
	createGarage,
	getRescues,
	createGarageOwner,
	acceptGarages,
	rejectGarage,
} from '@/modules/garage/api/index'

export const useGarageStore = defineStore('Result', {
	state: () => {
		return {
			demoList: {},
			overviewData: {
				title: '',
				content: '',
			},
		}
	},
	getters: {
		// demoList: state => state.demoList
	},
	actions: {
		async getAllGarage(query) {
			const res = await getAllGarage(query)

			return this.filterResponse(res, null, ({ data }) => {
				this.overviewData = data
			})
		},
		async getAddressInfo(query: any): Promise<any> {
			const res = await getAddressInfo(query)

			return this.filterResponse(
				res,
				(data: any) => {
					this.overviewData = data
				},
				() => {}
			)
		},
		async getAddressDetail(query: any) {
			const res = await getAddressDetail(query)

			return this.filterResponse(
				res,
				(data: any) => {
					this.overviewData = data
				},
				() => {}
			)
		},
		async getAllRescues(query) {
			const res = await getAllRescues(query)

			return this.filterResponse(
				res,
				({ data }) => {
					this.overviewData = data
				},
				() => {}
			)
		},
		async getListSubSystem(query) {
			const res = await getAllRescues(query)

			return this.filterResponse(
				res,
				({ data }) => {
					this.overviewData = data
				},
				() => {}
			)
		},
		async createGarage(query) {
			const res = await createGarage(query)

			return this.filterResponse(
				res,
				({ data }) => {
					this.overviewData = data
				},
				() => {}
			)
		},
		async getInforGarage(id: string) {
			const res = await getGarageInforById(id)
			localStorage.setItem('garage', JSON.stringify(res.data.garage))
		},
	},
})
