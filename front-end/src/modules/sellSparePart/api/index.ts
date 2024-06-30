import { garageService } from '@/utils/request'
import type { IQuery, IResponse } from '@/types'
const inventoryId = localStorage.getItem('inventoryId')
const garageId = localStorage.getItem('garageId')

export function findAllSellSparePartByGarageId(
	query: IQuery
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/${garageId}`,
		method: 'get',
		params: { ...query },
	})
}

export function findById(sellSparePartId: number): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/info/${sellSparePartId}`,
		method: 'get',
	})
}

export function create(
	payload: SellSparePart,
	discountType: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/${garageId}/v2?discountType=${discountType}`,
		method: 'post',
		data: { ...payload, inventoryId: inventoryId },
	})
}

export function update(
	payload: SellSparePart,
	sellSparePartId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update/${sellSparePartId}`,
		method: 'patch',
		data: { ...payload },
	})
}
export function updateTicketStatusHandle(
	payload: SellSparePart,
	sellSparePartId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update-ticket/${sellSparePartId}/handle`,
		method: 'patch',
		data: { ...payload },
	})
}

export function updateServiceTicketHandling(
	payload: SellSparePart,
	sellSparePartId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update-ticket/${sellSparePartId}/handling/update`,
		method: 'patch',
		data: { ...payload },
	})
}

export function updateHandlingTicketSellSparePart(
	payload: SellSparePart,
	sellSparePartId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update-ticket/${sellSparePartId}/handle`,
		method: 'patch',
		data: { ...payload },
	})
}

export function updateTicketStatusDelivery(
	ticketId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update-ticket/${ticketId}/delivery`,
		method: 'patch',
	})
}

export function updateTicketStatusConfirmRecive(
	ticketId: number
): Promise<IResponse> {
	return garageService({
		url: `sell-spare-parts/update-ticket/${ticketId}/confirm-receive`,
		method: 'patch',
	})
}

export function updateTicketStatusRefund(ticketId: number) {
	return garageService({
		url: `sell-spare-parts/update-ticket/${ticketId}/refund`,
		method: 'patch',
	})
}

export function updateTicketStatusCancel(ticketId: number) {
	return garageService({
		url: `sell-spare-parts/update-ticket/${ticketId}/cancel`,
		method: 'patch',
	})
}

// export function getPriceByCustomerTypeAndProduct(customerTypeId: number, productId: number): Promise<IResponse> {
//   return garageService({
//     url: `product-prices/${customerTypeId}/prices`,
//     method: "get",
//     params:
//   });
// }
