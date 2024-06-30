import { garageService } from '@/utils/request'

const garageId = localStorage.getItem('garageId')
export function getListOrder(query?: any): Promise<any> {
	return garageService({
		url: 'repair-orders/' + garageId,
		method: 'get',
		params: { ...query },
	})
}

export function createOrder(data: any): Promise<any> {
	return garageService({
		url: `repair-orders/${garageId}`,
		method: 'post',
		data: { ...data },
	})
}

export function getDetailOrder(id: string): Promise<any> {
	return garageService({
		url: 'repair-orders/' + garageId + '/info/' + id,
		method: 'get',
	})
}

export function updateOrder(id: string, data: any): Promise<any> {
	return garageService({
		url: 'repair-orders/' + garageId + '/update/' + id,
		method: 'patch',
		data: { ...data, garageId: garageId },
	})
}
export function cancelDiagnoses(data: any, id: string): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/diagnoses`,
		method: 'patch',
		data: { ...data },
	})
}

export function createDiagnoses(
	data: any,
	id: string,
	isCreateDiagnose: boolean
): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/diagnoses?isCreateDiagnose=${isCreateDiagnose}`,
		method: 'post',
		data: { ...data },
	})
}

export function createQuotations(
	data: any,
	id: string,
	isStartRepair: boolean,
	discountType: number
): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/quotations?isStartRepair=${isStartRepair}&discountType=${discountType}`,
		method: 'post',
		data: { ...data },
	})
}

export function updateQuotations(
	data: any,
	id: string,
	quotationID: string,
	isStartRepair: boolean
): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/quotations/${quotationID}?isStartRepair=${isStartRepair}`,
		method: 'patch',
		data: { ...data },
	})
}

export function handOverCar(id: string): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/handover-car`,
		method: 'patch',
	})
}

export function completeRepair(id: string): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/complete-repair`,
		method: 'patch',
	})
}

export function cancelRepair(id: string): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/cancel`,
		method: 'patch',
	})
}

export function changeQuotationStatus(id: string): Promise<any> {
	return garageService({
		url: `repair-orders/${id}/change-quotation-status`,
		method: 'patch',
	})
}
