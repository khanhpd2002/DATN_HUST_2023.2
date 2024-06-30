import { garageService } from '@/utils/request'
import type { IResponse } from '@/types'
import { IProfile } from '@/types/Profile'

// export function getDetailProfile(): Promise<IResponse<any>> {
// 	return garageService({
// 		url: 'users/me',
// 		method: 'get',
// 	})
// }

export function changePassword(
	payload: ChangePassword
): Promise<IResponse<any>> {
	return garageService({
		url: 'users/change-password',
		method: 'post',
		data: { ...payload },
	})
}

export function updateLogo(data: { logo: string }): Promise<IResponse<any>> {
	const garageId = localStorage.getItem('garageId')
	return garageService({
		url: `garages/${garageId}/update-logo`,
		method: 'post',
		data,
	})
}

export function updateAccountInfo(data: {
	contacts: { name: string; phone: string }[]
	banks: { owner: string; bankNumber: string; bankName: string }[]
}): Promise<IResponse<any>> {
	const garageId = localStorage.getItem('garageId')
	return garageService({
		url: `garages/${garageId}/garage-info`,
		method: 'patch',
		data,
	})
}

export function getAccountInfo(id?: string): Promise<IResponse<IProfile>> {
	const garageId = id ? id : localStorage.getItem('garageId')
	return garageService({
		url: `garages/${garageId}/garage-info`,
		method: 'get',
	})
}
