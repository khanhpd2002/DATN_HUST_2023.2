import { EStatusSuggestPrice } from '@/enums'

export function formatPriceVN(value: number) {
	let val = (value / 1).toFixed(0).replace('.', ',')
	return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

export function countTotalPrice(
	quantity: number,
	unitPrice: number,
	discount: number,
	tax: number,
	discountType: number
): number {
	let price = quantity * unitPrice
	if (discount && discountType == 1) {
		price = price - discount
	}
	if (discount && discountType == 2) {
		price = price * (1 - discount / 100)
	}
	if (tax) {
		price = price * (1 + tax / 100)
	}
	return Number(price.toFixed(0))
}

export function countOriginPrice(quantity: number, unitPrice: number): number {
	return quantity * unitPrice
}

export function validatePrice(event: any) {
	let keyCode = event.keyCode
	if ((keyCode < 48 || keyCode > 57) && keyCode !== 46) {
		event.preventDefault()
	}
}

export function preFilterInputNumberOnly(event: KeyboardEvent) {
	let keyCode = event.keyCode
	if (keyCode < 48 || keyCode > 57) {
		event.preventDefault()
	}
}

export function validateNumber(event: any) {
	let keyCode = event.keyCode
	if (keyCode < 48 || keyCode > 57) {
		event.preventDefault()
	}
}

export function getFirstIndexTemplatePrint(
	index: number,
	serviceList: any
): number {
	let result = 0
	for (let i = 0; i < index; i++) {
		result += serviceList[i].item.length
	}
	return result
}

export function formatDateYMDToDMY(inputDate: string): string {
	if (!inputDate) {
		return ''
	}
	const parts = inputDate.split('-')
	if (parts.length !== 3) {
		throw new Error('Invalid date format. Expected yyyy-MM-dd')
	}

	const year = parts[0]
	const month = parts[1]
	const day = parts[2]

	// Create a Date object with the given parts
	// @ts-ignore
	const date = new Date(Number(year), month - 1, day)

	// Extract day, month, and year components
	const formattedDay = String(date.getDate()).padStart(2, '0')
	const formattedMonth = String(date.getMonth() + 1).padStart(2, '0')
	const formattedYear = date.getFullYear()

	// Return the formatted date in "dd/MM/yyyy" format
	return `${formattedDay}/${formattedMonth}/${formattedYear}`
}

export function formatIsoDateToDatetime(inputDate: string): string {
	if (!inputDate) {
		return ''
	}
	// Convert ISO timestamp to a JavaScript Date object
	const date = new Date(inputDate)

	// Extract day, month, and year components
	const day = date.getUTCDate()
	const month = date.getUTCMonth() + 1 // Months are zero-based, so add 1.
	const year = date.getUTCFullYear()

	// Format the date in the "dd/mm/yyyy" format
	return `${day}/${month}/${year}`
}

const defaultNumbers = ' Hai Ba Bốn Năm Sáu Bảy Tám Chín'

const chuHangDonVi = ('1 Một' + defaultNumbers).split(' ')
const chuHangChuc = ('lẻ Mười' + defaultNumbers).split(' ')
const chuHangTram = ('Không Một' + defaultNumbers).split(' ')

function convert_block_three(number: string) {
	if (number == '000') return ''
	let _a = number + '' //Convert biến 'number' thành kiểu string

	//Kiểm tra độ dài của khối
	switch (_a.length) {
		case 0:
			return ''
		case 1:
			return chuHangDonVi[_a]
		case 2:
			return convert_block_two(_a)
		case 3:
			var chuc_dv = ''
			if (_a.slice(1, 3) != '00') {
				chuc_dv = convert_block_two(_a.slice(1, 3))
			}
			var tram = chuHangTram[_a[0]] + ' Trăm'
			return tram + ' ' + chuc_dv
	}
}

function convert_block_two(number: any) {
	var dv = chuHangDonVi[number[1]]
	var chuc = chuHangChuc[number[0]]
	var append = ''

	// Nếu chữ số hàng đơn vị là 5
	if (number[0] > 0 && number[1] == 5) {
		dv = 'Lăm'
	}

	// Nếu số hàng chục lớn hơn 1
	if (number[0] > 1) {
		append = ' Mươi'

		if (number[1] == 1) {
			dv = ' Mốt'
		}
	}

	return chuc + '' + append + ' ' + dv
}

const dvBlock = '1 Nghìn Triệu Tỷ'.split(' ')

export function to_vietnamese(number: any) {
	var str = parseInt(number) + ''
	var i = 0
	var arr = []
	var index = str.length
	var result = []
	var rsString = ''

	if (index == 0 || str == 'NaN') {
		return ''
	}

	// Chia chuỗi số thành một mảng từng khối có 3 chữ số
	while (index >= 0) {
		arr.push(str.substring(index, Math.max(index - 3, 0)))
		index -= 3
	}

	// Lặp từng khối trong mảng trên và convert từng khối đấy ra chữ Việt Nam
	for (i = arr.length - 1; i >= 0; i--) {
		if (arr[i] != '' && arr[i] != '000') {
			result.push(convert_block_three(arr[i]))

			// Thêm đuôi của mỗi khối
			if (dvBlock[i]) {
				result.push(dvBlock[i])
			}
		}
	}

	// Join mảng kết quả lại thành chuỗi string
	rsString = result.join(' ')

	// Trả về kết quả kèm xóa những ký tự thừa
	return rsString
		.replace(/[0-9]/g, '')
		.replace(/ /g, ' ')
		.replace(/ $/, '')
		.concat(' Đồng')
}

export const getClassSuggestPriceStatus = (
	status: string,
	isDetail?: boolean
) => {
	switch (status) {
		case EStatusSuggestPrice.CANCELED:
			return 'status-new-service-8'
		case EStatusSuggestPrice.QUOTED:
			return 'status-new-service-2'
		case EStatusSuggestPrice.NOT_QUOTED:
			return isDetail ? 'status-new-service-9' : 'status-new-service-1'
		case EStatusSuggestPrice.DELIVERED:
			return 'status-new-service-6'
		case EStatusSuggestPrice.ON_THE_WAY:
			return 'status-new-service-4'
		case EStatusSuggestPrice.ORDERED:
			return 'status-new-service-2'
		default:
			return 'status-new-service-0'
	}
}

export const convertToRoman = (num: number) => {
	let roman = ''
	let nums = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1]
	let romans = [
		'M',
		'CM',
		'D',
		'CD',
		'C',
		'XC',
		'L',
		'XL',
		'X',
		'IX',
		'V',
		'IV',
		'I',
	]
	for (let i = 0; i < nums.length; i++) {
		while (num >= nums[i]) {
			roman += romans[i]
			num -= nums[i]
		}
	}
	return roman
}

export const validatePhoneNumber = (val: string) => {
	if (val != '') {
		let phoneRegex = /([\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})\b/
		return val.match(phoneRegex)
	}
	return false
}

export const handleGroupByTypeName = (array: any) => {
	const groupByTypeNameMapper: any = {}
	array?.forEach((s: any) => {
		const garaTypeName = s.garageServiceTypeId.options.find(
			(t: any) => t.value === s.garageServiceTypeId.value
		)?.label

		if (!garaTypeName) return
		const garaServiceName = s.garageServiceId.options.find(
			(_s: any) => _s.value === s.garageServiceId.value
		)?.label

		groupByTypeNameMapper[garaTypeName] =
			groupByTypeNameMapper[garaTypeName] || []

		groupByTypeNameMapper[garaTypeName].push({
			...s,
			garaServiceName,
			garaTypeName,
		})
	})

	const result = []
	for (const key in groupByTypeNameMapper) {
		result.push({
			typeName: key,
			item: groupByTypeNameMapper[key],
		})
	}

	return result || []
}
