export type IBank = {
	owner: string
	bankNumber: string
	bankName: string
}

export type IContact = {
	name: string
	phone: string
}

export type IProfile = {
	id: number
	contractPointPhone: string
	contactPointName: string
	name: string
	address: string
	contactPointEmail: string
	taxCode: string
	additionalInformation: {
		banks: IBank[]
		contacts: IContact[]
	}
	logo: string
}

export type IAccountInfo = {
	contacts: IContact[]
	banks: IBank[]
	taxCode: string
	email: string
	address: string
}
