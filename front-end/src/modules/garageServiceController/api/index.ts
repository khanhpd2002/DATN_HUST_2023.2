import { garageService, service } from '@/utils/request'
import type { IQuery, IDistributor, IResponse, IAccessary, ICar } from '@/types'
export const BaseIAccessary = {
  type: 0,
  code: '',
  name: '',
  color: '',
  distributorId: 0,
  distributorCode: undefined,
  distributorName: undefined,
  purchasePrice: 0,
  sparePartType: 0,
  quantity: 0,
  unit: '',
  description: undefined,
  productCompatibilities: [] as ICar[],
} as unknown as IAccessary
const GARAGE_ID = localStorage.getItem('garageId')
const inventoryId = localStorage.getItem('inventoryId')
export function getListGarageServiceId(query: IQuery): Promise<IResponse> {
  return garageService({
    url: `garage-services/${GARAGE_ID}`,
    method: 'get',
    params: { ...query },
  })
}

export function createService(payload: any): Promise<any> {
  return garageService({
    url: `garage-services/${GARAGE_ID}`,
    method: 'post',
    data: { ...payload },
  })
}

export function createListService(payload: any[]): Promise<any> {
  return garageService({
    url: `garage-services/${GARAGE_ID}/bulk`,
    method: 'post',
    data: payload,
  })
}

export function updateService(
  payload: any,
  garageServiceId: string
): Promise<any> {
  return garageService({
    url: `garage-services/${GARAGE_ID}/update/${garageServiceId}`,
    method: 'patch',
    data: { ...payload },
  })
}

export function getDetailService(id: string): Promise<any> {
  return garageService({
    url: `garage-services/${GARAGE_ID}/info/${id}`,
    method: 'get',
  })
}

export function getListGarageServiceTypeId(): Promise<IResponse> {
  return garageService({
    url: `garage-service-types/${GARAGE_ID}`,
    method: 'get',
  })
}
