export interface IResponse<DataType> {
	code: number
	message: string
	msg: string
	data: DataType
	currentPage: number
	totalElement: number
	pageSize: number
}
