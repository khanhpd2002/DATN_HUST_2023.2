import auth from '@/router/middleware/auth'
import guest from '@/router/middleware/guest'

const routes = [
	{
		path: '/',
		name: 'Login',
		component: () =>
			import('@/modules/sharedModules/pages/Auth/login/index.vue'),
		meta: {
			layout: 'BlankLayout',
		},
	},

	{
		path: '/app',
		name: 'Layout',
		redirect: 'app/sell/customers',
		// component: () =>
		//     import("@/modules/sharedModules/pages/Layout/index.vue"),
		meta: {
			middleware: [auth],
		},
		children: [
			{
				name: 'Home Page',
				path: 'home',
				component: () => import('@/modules/home/pages/Home.vue'),
			},
			{
				name: 'Website - Quản lí nội dung',
				path: 'website/contents',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},
			{
				name: 'Website - Báo cáo chi tiết',
				path: 'website/report',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},
			{
				name: 'Debt - Danh sách đơn hàng đặt',
				path: 'debt/order-spare-parts',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},
			{
				name: 'Debt - Danh sách đơn bán',
				path: 'debt/orders-distributor-controller',
				component: () =>
					import(
						'@/modules/debt/pages/OrderDistributorController/OrderDistributorControllerList.vue'
					),
			},
			{
				name: 'Debt - Chỉnh sửa đơn bán',
				path: 'debt/orders-distributor-controller/view/:id/:type',
				component: () =>
					import(
						'@/modules/debt/pages/OrderDistributorController/OrderDistributorControllerDetail.vue'
					),
			},
			{
				name: 'Debt - Chi tiết đơn bán',
				path: 'debt/orders-distributor-controller/edit/:id/:type',
				component: () =>
					import(
						'@/modules/debt/pages/OrderDistributorController/OrderDistributorControllerDetail.vue'
					),
			},
			{
				name: 'Debt - Đơn bán hàng hoá dịch vụ',
				path: 'debt/service-ticket',
				component: () =>
					import('@/modules/debt/pages/ServiceTicket/ServiceTicket.vue'),
			},
			{
				name: 'Debt - Chi tiết dơn bán hàng hoá dịch vụ',
				path: 'debt/service-ticket/view/:id',
				component: () =>
					import('@/modules/debt/pages/ServiceTicket/ServiceTicketDetail.vue'),
			},
			{
				name: 'Debt - Chỉnh sửa dơn bán hàng hoá dịch vụ',
				path: 'debt/service-ticket/edit/:id',
				component: () =>
					import('@/modules/debt/pages/ServiceTicket/ServiceTicketDetail.vue'),
			},
			{
				name: 'Inventory - Danh sách NPP',
				path: 'inventory/distributors',
				component: () =>
					import('@/modules/distributor/pages/DistributorList.vue'),
			},
			{
				name: 'Inventory - Danh sách phụ tùng',
				path: 'inventory/spare-parts',
				component: () => import('@/modules/accessary/pages/AccessaryList.vue'),
			},
			{
				name: 'Inventory - Tồn kho phụ tùng',
				path: 'inventory/remain-spare-parts',
				component: () =>
					import(
						'@/modules/inventory/pages/sparePartsInventory/ListSparePartsInventory.vue'
					),
			},
			{
				name: 'Inventory - Lịch sử kiểm kê phụ tùng',
				path: 'inventory/histories',
				component: () =>
					import(
						'@/modules/inventory/pages/inventoryHistory/ListInventoryHistory.vue'
					),
			},
			{
				name: 'Inventory - Chi tiết lịch sử kiểm kê phụ tùng',
				path: 'inventory/histories/detail/:id',
				component: () =>
					import(
						'@/modules/inventory/pages/inventoryHistory/InventoryHistoryFormDetail.vue'
					),
			},
			{
				name: 'Inventory - Phiếu nhập kho',
				path: 'inventory/inbounds',
				component: () =>
					import('@/modules/inboundTicket/pages/InboundTicketList.vue'),
			},
			{
				name: 'Inventory - Phiếu xuất kho',
				path: 'inventory/outbounds',
				component: () =>
					import('@/modules/outboundTicket/pages/OutboundTicketList.vue'),
			},
			{
				name: 'debt - Danh sách đơn bán',
				path: 'debt/sales-order',
				component: () =>
					import('@/modules/debt/pages/SellsOrder/SellsOrderList.vue'),
			},
			{
				name: 'debt - Chi tiết danh sách đơn bán',
				path: 'debt/sales-order/view/:id',
				component: () =>
					import('@/modules/debt/pages/SellsOrder/SellsOrderDetail.vue'),
			},
			{
				name: 'debt - Chỉnh sửa danh sách đơn bán',
				path: 'debt/sales-order/edit/:id',
				component: () =>
					import('@/modules/debt/pages/SellsOrder/SellsOrderDetail.vue'),
			},
			{
				name: 'Inventory - Danh sách đơn hàng đặt',
				path: 'inventory/order-spare-parts',
				component: () =>
					import('@/modules/inventory/pages/Orders/OrdersDistributor.vue'),
			},
			{
				name: 'Inventory - Thêm mới đơn hàng đặt',
				path: 'inventory/order-spare-parts/add',
				component: () =>
					import('@/modules/inventory/pages/Orders/OrdersDistributorAdd.vue'),
			},
			{
				name: 'Inventory - Chi tiết đơn hàng đặt',
				path: 'inventory/order-spare-parts/:id/edit',
				component: () =>
					import('@/modules/inventory/pages/Orders/OrdersDistributorAdd.vue'),
			},
			// {
			//     name: "Inventory - Thêm mới đơn đặt hàng",
			//     path: "inventory/order-spare-parts/create",
			//     component: () =>
			//         import(
			//             "@/modules/inventory/pages/Orders/OrdersDistributorForm.vue"
			//         ),
			// },
			{
				name: 'Inventory - Chi tiết đơn đặt hàng',
				path: 'inventory/order-spare-parts/:id/:state',
				component: () =>
					import('@/modules/inventory/pages/Orders/OrdersDistributorForm.vue'),
			},
			{
				name: 'Sell - Danh sách nhóm khách hàng',
				path: 'sell/customer-types',
				component: () =>
					import('@/modules/customerType/pages/CustomerTypeList.vue'),
			},
			{
				name: 'Sell - Danh sách khách hàng',
				path: 'sell/customers',
				component: () => import('@/modules/customer/pages/CustomerList.vue'),
			},
			{
				name: 'Sell - Chỉnh sửa khách hàng',
				path: 'sell/customers/:id/edit',
				component: () => import('@/modules/customer/pages/CustomerDetail.vue'),
			},
			{
				name: 'Sell - Chi tiết khách hàng',
				path: 'sell/customers/:id/info',
				component: () => import('@/modules/customer/pages/CustomerDetail.vue'),
			},
			{
				name: 'Sell - Danh sách xe',
				path: 'sell/cars',
				component: () => import('@/modules/car/pages/CarList.vue'),
			},
			{
				name: 'Sell - Chỉnh sửa thông tin xe',
				path: 'sell/cars/:id/edit',
				component: () => import('@/modules/car/pages/CarDetail.vue'),
			},
			{
				name: 'Sell - Chi tiết thông tin xe',
				path: 'sell/cars/:id/info',
				component: () => import('@/modules/car/pages/CarDetail.vue'),
			},
			{
				name: 'Sell - Danh mục dịch vụ',
				path: 'sell/services',
				component: () =>
					import('@/modules/garageServiceController/pages/ServiceList.vue'),
			},
			{
				name: 'Sell - Quản lý giá bán',
				path: 'sell/product-prices',
				component: () =>
					import('@/modules/productPrice/pages/ProductPriceList.vue'),
			},
			{
				name: 'Sell - Bán hàng',
				path: 'sell/sell-spare-parts',
				component: () =>
					import('@/modules/sellSparePart/pages/SellSparePartList.vue'),
			},
			{
				name: 'Sell - Tạo đơn hàng mới',
				path: 'sell/sell-spare-parts/add',
				component: () =>
					import('@/modules/sellSparePart/pages/SellSparePartFormSite.vue'),
			},
			{
				name: 'Sell - Xử lý đơn hàng',
				path: 'sell/sell-spare-parts/:id/edit',
				component: () =>
					import('@/modules/sellSparePart/pages/SellSparePartFormSite.vue'),
			},
			// {
			//     name: "Sell - Phiếu dịch vụ",
			//     path: "sell/service-tickets",
			//     component: () =>
			//         import("@/modules/serviceTicket/pages/ServiceTicket.vue"),
			// },
			{
				name: 'Sell - Phiếu dịch vụ mới',
				path: 'sell/new-service-tickets',
				component: () =>
					import('@/modules/newServiceTicket/pages/ServiceTicketList.vue'),
			},
			{
				name: 'Sell - Phiếu dịch vụ mới chi tiet',
				path: 'sell/new-service-tickets-detail/:id',
				component: () =>
					import('@/modules/newServiceTicket/pages/ServiceTicket.vue'),
			},
			{
				name: 'Sell - Phiếu dịch vụ Print',
				path: 'sell/service-tickets/print/:id',
				component: () =>
					import('@/modules/serviceTicket/pages/ServiceTicketPrint.vue'),

				meta: {
					layout: 'BlankLayout',
				},
			},
			{
				name: 'Employee - Danh sách nhân sự',
				path: 'employees',
				component: () => import('@/modules/employee/pages/EmployeeList.vue'),
			},
			{
				name: 'Employee Report - Báo cáo chi tiết',
				path: 'employees-report',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},

			{
				name: 'Employee Report - Vai trò tài khoản',
				path: 'employees-role',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},
			{
				name: 'Service-Voucher',
				path: 'service-voucher',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/serviceVoucher.vue'),
			},
			{
				name: 'Quote-Slip',
				path: 'quote-slip',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/quoteSlip.vue'),
			},

			{
				name: 'Repair-Order',
				path: 'repair-order',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/repairOrder.vue'),
			},
			{
				name: 'Quotation-Sheet',
				path: 'quotation-sheet',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/QuotationSheet.vue'),
				meta: {
					layout: 'BlankLayout',
				},
			},
			{
				name: 'RepairOrderSheet',
				path: 'repair-sheet',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/RepairOrderSheet.vue'),
				meta: {
					layout: 'BlankLayout',
				},
			},
			{
				name: 'InboundOrOutboundSheet',
				path: 'inbound-outbound-sheet',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/InboundSheet.vue'),
				meta: {
					layout: 'BlankLayout',
				},
			},

			{
				name: 'inventoryMinutes',
				path: 'inventory-minutes-sheet',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/HistoryTemplate.vue'),
				meta: {
					layout: 'BlankLayout',
				},
			},
			{
				name: 'NewServiceTicketSheet',
				path: 'newservice-sheet',
				component: () =>
					import(
						'@/modules/sharedModules/pages/formHTML/NewServiceTicketSheet.vue'
					),
				meta: {
					layout: 'BlankLayout',
				},
			},
			{
				path: 'not-found',
				name: 'Not Found',
				component: () => import('@/modules/sharedModules/pages/NotFound.vue'),
			},
			{
				path: 'coming-soon',
				name: 'Coming Soon',
				component: () => import('@/modules/sharedModules/pages/ComingSoon.vue'),
			},
			{
				name: 'Inventory-Minutes',
				path: 'inventory-minutes',
				component: () =>
					import('@/modules/sharedModules/pages/formHTML/inventoryMinutes.vue'),
			},
			{
				name: 'Profile',
				path: 'profile',
				component: () => import('@/modules/profile/pages/Profile.vue'),
			},
		],
	},
]

export default routes
