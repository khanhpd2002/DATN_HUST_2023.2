export type ISuggestPriceProduct = {
	productDescription: string
}

export type ISuggestPriceFormRequestList = {
	carInfo: string
	images: ISuggestPriceProductImage[]
	products: ISuggestPriceProduct[]
}

export type ISuggestPriceProductImage = {
	url: string
}

export type IGarageInfos = {
	garageId: number
	garageName?: string
	garagePhone?: string
	garageCode?: string
	garageGroupId?: number
}
export type ISuggestPriceRequestBody = {
	garageOwnerId: number
	garageOwnerName: string
	suggestPriceFormRequestList: ISuggestPriceFormRequestList[]
	isValueAddedInvoice: boolean
	garageInfos: IGarageInfos[]
}

export type ISuggestPriceListResponse = {
	id: number
	garageOwnerId: number
	garageOwnerName: string
	garageOwnerPhone: string
	code: string
	statusAdmin: string
	statusGarage: string
	createdAt: string
	garageInfos: IGarageInfos[]
	carInfo: string
}

export type ISuggestPriceQuery = {
	statusGarage?: string
	code?: string
	createdAt?: string
	page: number
	size: number
}

export type ISuggestPriceInfo = {
	carInfo: string
	statusGarage: string
	isValueAddedInvoice: boolean
	products: []
}

export type IProductVariants = {
	isOutOfStockProduct: boolean
	note?: string
	priceSystem: number
	productName: string
	productVariantId: number
	segmentName: string
	suggestItemId: number
	unit: string
	unitItem: string
	quantity?: number
	quantityOrder?: number
	isChecked?: boolean
	exportPrice?: number
	disabled?: boolean
	intoMoney?: number
}

export type IListProductVariant = {
	isOutOfStock: boolean
	productDescriptionUpdated: string
	productVariants: IProductVariants[]
	itemId: number
	unit: string
}

export type IProductGarage = {
	categoryId: number
	createdAt: string
	id: number
	isOutOfStock: boolean
	productDescription: string
	productDescriptionUpdated: string
	quoteProductType: string
	suggestPriceFormId: number
	suggestPriceId: number
	unit: string
	updatedAt: string
}
export type IOrderBody = {
	productVariantId: number
	quantityOrder: number
	total: number
}
