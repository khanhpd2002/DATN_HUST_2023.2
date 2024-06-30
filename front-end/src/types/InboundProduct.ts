type InboundProduct = {
	id: number
	productId: number | null
	inboundTicketId: number
	unit?: string
	distributorId?: number
	requestQuantity?: number
	exportQuantity?: number
	note?: string
	status: number
	instanceKey: number
	productName?: string
}
