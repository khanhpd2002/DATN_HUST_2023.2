import { IResponse } from '@/types'
import { OrderDistributor } from '@/types/OrdersDistributor'
import { garageService } from '@/utils/request'
const garageId = localStorage.getItem('garageId')

const inventoryId = localStorage.getItem('inventoryId')
export function getListSparePartsInventory(query: any): Promise<any> {
	return garageService({
		url: `products/${inventoryId}/parent`,
		method: 'get',
		params: { ...query, garageId: garageId },
	})
}

export function getDetailParentSparePart(id: string): Promise<any> {
	return garageService({
		url: 'products/' + inventoryId + '/parent-info/' + id,
		method: 'get',
	})
}

export function getProductForInventoryHistory(): Promise<any> {
	return garageService({
		url: 'products/' + inventoryId + '/product-histories',
		method: 'get',
	})
}

export function getProductForAdjustmentInventoryHistory(
	historyID: any
): Promise<any> {
	return garageService({
		url:
			'inventory-histories/' +
			inventoryId +
			'/info-to-update-history/' +
			historyID,
		method: 'get',
	})
}

export function getDetailSparePartsInventory(id: string): Promise<any> {
	return garageService({
		url: 'products/' + inventoryId + '/info/' + id,
		method: 'get',
	})
}

export function updateSparePartsInventory(id: string, data: any): Promise<any> {
	return garageService({
		url: 'products/' + inventoryId + '/update-parent/' + id,
		method: 'patch',
		data: data,
	})
}

export function createInventoryHistory(data: any): Promise<any> {
	return garageService({
		url: 'inventory-histories/' + inventoryId,
		method: 'post',
		data: data,
	})
}

export function adjustmentInventoryHistory(
	data: any,
	historyID: any
): Promise<any> {
	return garageService({
		url: `inventory-histories/${inventoryId}/adjustment/${historyID}`,
		method: 'post',
		data: data,
	})
}

export function getListInventoryHistory(query: any): Promise<any> {
	return garageService({
		url: 'inventory-histories/' + inventoryId,
		method: 'get',
		params: { ...query, garageId: garageId },
	})
}

export function getDetailInventoryHistory(id: string): Promise<any> {
	return garageService({
		url: 'inventory-histories/' + inventoryId + '/info/' + id,
		method: 'get',
	})
}

export function updateInventoryHistory(data: any, id: string): Promise<any> {
	return garageService({
		url: 'inventory-histories/' + inventoryId + '/update/' + id,
		method: 'patch',
		data: data,
	})
}

export function getOrderDistributorControllerList(query: any): Promise<any> {
	return garageService({
		url: 'orders-distributor',
		method: 'get',
		params: { ...query, garageId: garageId },
	})
}

// export function getDetailOrderDistributorController(
//     id: string | number
// ): Promise<any> {
//     return garageService({
//         url: "orders-distributor/" + garageId + "/info/" + id,
//         method: "get",
//     });
// }
export function getDetailOrderDistributorController(
	id: string | number
): Promise<any> {
	return garageService({
		url: 'orders-distributor/' + garageId + '/info/' + id,
		method: 'get',
	})
}

export function findOrderDistributor(id: string | number): Promise<any> {
	return garageService({
		url: `orders-distributor/info/${id}`,
		method: 'get',
	})
}

export function createOrderDistributor(data: OrderDistributor): Promise<any> {
	return garageService({
		url: 'orders-distributor/' + garageId,
		method: 'post',
		data: data,
	})
}

export function updateOrderDistributor(
	data: any,
	id: string | number
): Promise<any> {
	return garageService({
		url: 'orders-distributor/' + garageId + '/update/' + id,
		method: 'patch',
		data: data,
	})
}

export function createNewOrderDistributor(
	payload: OrderDistributor,
	discountType: number
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/${garageId}?discountType=${discountType}`,
		method: 'post',
		data: payload,
	})
}
export function updateOrderReceived(
	orderId: string,
	payload: OrderDistributor
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update/${orderId}`,
		method: 'patch',
		data: payload,
	})
}

export function cancelUpdateOrder(
	orderId: number | string
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update-ticket/${orderId}/cancel`,
		method: 'patch',
	})
}

export function handlingUpdateOrder(
	orderId: number | string,
	data: any
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update-ticket/${orderId}/handle`,
		method: 'patch',
		data,
	})
}

export function handleSaveUpdateOrder(
	orderId: number | string,
	data: any
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update/${orderId}`,
		method: 'patch',
		data,
	})
}

export function handleDeliveryUpdateOrder(
	orderId: number | string,
	data?: any
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update-ticket/${orderId}/delivery`,
		method: 'patch',
		data,
	})
}

export function handleReceiveProductOrder(
	orderId: number | string,
	data?: any
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update-ticket/${orderId}/confirm-receive`,
		method: 'patch',
		data,
	})
}

export function handleRefundOrder(
	orderId: number | string
): Promise<IResponse> {
	return garageService({
		url: `orders-distributor/update-ticket/${orderId}/refund`,
		method: 'patch',
	})
}
