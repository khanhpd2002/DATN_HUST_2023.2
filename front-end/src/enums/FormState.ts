export enum EFormState {
	ADD = 1,
	VIEW = 2,
	EDIT = 3,
	DELETE = 4,
	ADD_FOR_CUSTOMER = 5,
	ADD_NEW = 6,
}

export enum EFormConfirm {
	SAVE = 1,
	CANCEL = 2,
}

export enum EStepperTicket {
	RECEIVE = 0,
	SELL_PRICE = 1,
	PROCEED = 2,
	PAYMENT = 3,
	HAND_OVER = 4,
	COMPLETE = 5,
	DONE = 6,
	GARAGE_CANCEL = 7,
	CUSTOMER_CANCEL = 8,
}

export enum EStatusExport {
	EXPORTED = 'ĐÃ XUẤT KHO',
	UN_EXPORTED = 'CHƯA XUẤT KHO',
	EXPORTED_1 = 'Đã xuất kho',
	UN_EXPORTED_1 = 'Chưa xuất kho',
}

export enum EStatusSuggestPrice {
	CANCELED = 'CANCELED',
	NOT_QUOTED = 'NOT_QUOTED',
	QUOTED = 'QUOTED',
	ORDERED = 'ORDERED',
	ON_THE_WAY = 'ON_THE_WAY',
	DELIVERED = 'DELIVERED',
}
