import { contentService, garageService, trackingService } from '@/utils/request'
import {
	LoginPayloadType,
	LoginResponseType,
	TokenResponseType,
} from '@/modules/sharedModules/types/User'
import { IResponse } from '@/types'
import {Tracking} from "@/modules/sharedModules/types/Tracking";

export function login(data: LoginPayloadType): Promise<TokenResponseType> {
	return garageService({
		url: 'users/login',
		method: 'post',
		data,
	})
}

export function verify(): Promise<IResponse> {
	return garageService({
		url: 'users/me',
		method: 'get',
	})
}

export function getInventoryByGarage(garageId: number): Promise<IResponse> {
	return garageService({
		url: `inventories/${garageId}`,
		method: 'get',
		params: {
			pageSize: 100,
			pageNumber: 1,
		},
	})
}

export function getCarBrandList(): Promise<IResponse> {
	return contentService({
		url: 'car-brands',
		method: 'get',
	})
}

export function getCarModelList(
	carBrandId: number | string
): Promise<IResponse> {
	return contentService({
		url: `car-brands/${carBrandId}/car-models`,
		method: 'get',
	})
}

export function getCarYearList(
	carModelId: number | string
): Promise<IResponse> {
	return contentService({
		url: `car-models/${carModelId}/car-years`,
		method: 'get',
	})
}

export function getCarVersionList(
	carYearId: number | string
): Promise<IResponse> {
	return contentService({
		url: `car-years/${carYearId}/car-versions`,
		method: 'get',
	})
}

export function getExcelTemplate(endpoint: string): Promise<any> {
	return garageService({
		url: endpoint,
		method: 'get',
		responseType: 'blob',
	})
}

export function uploadExcelFile(
	endpoint: string,
	formData: FormData
): Promise<any> {
	return garageService({
		url: endpoint,
		method: 'post',
		data: formData,
	})
}

export function exportExcelFile(endpoint: string): Promise<any> {
	return garageService({
		url: endpoint,
		method: 'post',
		responseType: 'blob',
	})
}

export function saveLogTracking(data: Tracking): Promise<IResponse> {
	return trackingService({
		url: `gms-log-tracking`,
		method: 'post',
		data,
	})
}
