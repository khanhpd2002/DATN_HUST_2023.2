<template>
	<div class="min-h-screen flex overflow-x-auto">
		<div :class="computedOnDisplaySiderClass">
			<ACCDSider :open="true">
				<div class="flex flex-col pr-0 bg-white border-r">
					<div class="shrink-0 py-2 h-[64px] flex justify-between">
						<div class="flex px-4 items-center">
							<img
								class="h-[50px] w-auto mr-4"
								src="@\assets\images\logo_cardoctor.svg"
								alt="Your Company"
							/>
							<p class="text-2xl text-neutral-600 font-medium">Cardoctor</p>
						</div>

						<ACCDIcon
							name="fa-solid fa-xmark"
							class="cursor-pointer relative p-4 block xl:hidden text-black"
							@click="onDisplaySider = false"
						></ACCDIcon>
					</div>

					<div class="h-[calc(100vh-64px)] overflow-y-auto px-2 pb-[68px] pt-4">
						<ul role="list" class="space-y-1">
							<li v-for="(item, i) in navigation" :key="item.name">
								<a
									v-if="!item.hasOwnProperty('child')"
									:href="item.href"
									:class="[
										item.current
											? 'bg-[#f4fbfe] text-grey-menu'
											: 'text-grey-menu',
										'group flex gap-3 rounded-md p-2 pr-3 font-semibold leading-6 cursor-pointer  hover:bg-[#f4fbfe]',
									]"
								>
									<CDAIcon
										size="20"
										:class="[
											item.current ? 'text-gray-600' : 'text-gray-900 ',
											'text-base shrink-0',
										]"
										:name="item.icon"
										style="font-size: 20px !important"
										type="bold"
									>
									</CDAIcon>
									{{ item.name }}
								</a>

								<div
									v-else
									@click="toggleSubmenu(i)"
									class="cursor-pointer"
									:class="[
										item.current
											? 'bg-[#f4fbfe] text-grey-menu'
											: 'text-grey-menu hover:text-gray-600 hover:bg-[#f4fbfe]',
										'group flex gap-x-3 rounded-md p-2 leading-6 font-semibold',
									]"
								>
									<CDAIcon
										type="bold"
										size="20"
										:class="[
											item.current ? 'text-gray-600' : 'text-gray-900 ',
											'text-base shrink-0',
										]"
										:name="item.icon"
										style="font-size: 20px !important"
									>
									</CDAIcon>

									<span
										class="flex min-w-[calc(100%-60px)] items-center justify-between"
									>
										{{ item.name }}
										<div
											:class="activeSubmenu.index === i ? 'rotate-90' : ''"
										></div>
									</span>
									<!-- <ACCDIcon
                                        :name="
                                            item.isActive
                                                ? 'fa fa-caret-down'
                                                : 'fa fa-caret-right'
                                        "
                                        style="font-size: 16px !important"
                                    /> -->
									<img
										v-if="item.isActive"
										src="@/assets/images/svg/left-menu/arrow-up.svg"
									/>
									<img
										v-else
										src="@/assets/images/svg/left-menu/arrow-down.svg"
									/>
								</div>
								<Transition
									enter-active-class="submenu_enter-active"
									leave-active-class="submenu_leave-active"
									@before-enter="beforeEnter"
									@enter="enter"
									@after-enter="afterEnter"
									@before-leave="beforeLeave"
									@leave="leave"
									@after-leave="afterLeave"
								>
									<!-- !! SubMenu !! -->
									<ul
										:class="[item.isActive ? 'show-submenu' : 'hidden-submenu']"
									>
										<li
											v-for="(ci, index) in item.child"
											:key="index"
											class="p-2 pl-[40px] cursor-pointer rounded"
											:class="`${'text-grey-menu '} ${'hover:text-grey-menu hover:bg-[#f4fbfe]'} ${
												ci.href === route.path || ci.activeSubmenu?.includes(route.name as string)
													? 'text-grey-menu bg-[#f4fbfe]'
													: ''
											}`"
											@click="() => redirect(ci)"
										>
											<span
												v-if="!ci.href"
												class="flex-1 flex"
												style="text-align: left"
											>
												{{ ci.name }}
											</span>
											<router-link :to="ci.href" v-else>
												<!-- <a @click="() => redirect(ci)"> -->
												<span class="flex-1 flex" style="text-align: left">
													{{ ci.name }}
												</span>
											</router-link>
											<!-- </a> -->
										</li>
									</ul>
								</Transition>
							</li>
						</ul>
					</div>
					<div class="text-[12px] ml-3 mt-[-16px]">
						Version : {{ versionPackage }}
					</div>

					<!-- <div
                        class="h-[68px] w-full absolute bottom-0 border-t mt-auto flex items-center py-2 px-4 bg-white border-r"
                    >
                        <img
                            class="h-9 w-9 rounded-full bg-gray-50"
                            src="@\assets\images\logo_cardoctor.svg"
                            alt=""
                        />
                        <span class="items-center ml-4">
                            <p
                                class="font-medium leading-6 text-gray-900"
                                aria-hidden="true"
                            >
                                {{ garageOwner }}
                            </p>
                            <span
                                class="text-xs font-medium leading-6 text-gray-500"
                                >{{ garage }}</span
                            >
                        </span>
                    </div> -->
				</div>
			</ACCDSider>
		</div>
		<div class="flex flex-col w-full overflow-x-auto">
			<header
				class="h-16 bg-white flex items-center justify-between px-4 font-bold border-b"
			>
				<div class="text-[16px]">
					{{
						route.path.includes('profile') ? 'Thông tin tài khoản' : labelMenu
					}}

					<span v-if="route.path.includes('price-detail')"
						>Chi tiết yêu cầu báo giá</span
					>
				</div>
				<div class="flex items-center gap-3">
					<div class="rounded-full p-1 bg-common text-[20px] cursor-pointer">
						<ACCDAIcon name="Message" type="bold" size="20" />
					</div>
					<div class="rounded-full p-1 bg-common text-[20px] cursor-pointer">
						<ACCDAIcon name="NotificationBing" type="bold" size="20" />
					</div>
					<div></div>

					<ACCDDropdown class="ml-4 hidden lg:block cursor-pointer mt-2 pr-4">
						<template #activator>
							<div class="flex items-center gap-3 cursor-pointer">
								<img
									:src="garageAvatar"
									rounded
									class="avatar object-contain"
								/>

								<div class="flex flex-col">
									<p class="font-bold">{{ garageOwner }}</p>
									<p class="font-medium">{{ garage }}</p>
								</div>
							</div>
						</template>
						<template #default>
							<ul
								class="bg-layout-primary border border-line py-1 w-full rounded-md"
							>
								<li
									class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center"
									@click="() => handleChangeAccount(1)"
								>
									<span>{{
										$t('module.sharedModules.header.accountInfo')
									}}</span>
								</li>
								<li
									class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center"
									@click="() => handleChangeAccount(2)"
								>
									<span>{{
										$t('module.sharedModules.header.changePassword')
									}}</span>
								</li>
								<li
									class="cursor-pointer px-4 py-2 hover:bg-layout-muted flex items-center border-t border-line"
									@click="handleLogout"
								>
									<span>{{ $t('module.sharedModules.header.logout') }}</span>
								</li>
							</ul>
						</template>
					</ACCDDropdown>
					<ACCDIcon
						name="fa-solid fa-bars"
						class="block xl:hidden cursor-pointer"
						@click="onDisplaySider = true"
					>
					</ACCDIcon>
				</div>
			</header>
			<main class="h-[calc(100vh-64px)] overflow-auto bg-common">
				<div class="mx-auto pb-4 p-4 rounded-lg">
					<!--                    <router-view @click="onDisplaySider = false" />-->
					<RouterView name="default" v-slot="{ Component, route }">
						<Suspense timeout="0">
							<template #default>
								<component :is="Component" />
							</template>
							<template #fallback>
								<div class="h-[calc(100vh-64px)]">
									<CDPageSpinner>
										<div id="loading-bg">
											<div class="loading-logo text-center">
												<img
													src="@/assets/images/logo_cardoctor.svg"
													alt="Logo"
													width="100px"
												/>
											</div>
											<div class="loading">
												<div class="effect-1 effects"></div>
												<div class="effect-2 effects"></div>
												<div class="effect-3 effects"></div>
											</div>
										</div>
									</CDPageSpinner>
								</div>
							</template>
						</Suspense>
					</RouterView>

					<!-- <router-view
						v-slot="{ Component, route }"
						:key="new Date().getTime()"
					>
						<Transition name="fade" mode="out-in" :key="new Date().getTime()">
							<suspense timeout="0">
								<div :key="new Date().getTime()">
									<component :is="Component" />
								</div>

								<template #fallback>
									<div class="h-[calc(100vh-64px)]">
										<CDPageSpinner>
											<div id="loading-bg">
												<div class="loading-logo text-center">
													<img
														src="@/assets/images/logo_cardoctor.svg"
														alt="Logo"
														width="100px"
													/>
												</div>
												<div class="loading">
													<div class="effect-1 effects"></div>
													<div class="effect-2 effects"></div>
													<div class="effect-3 effects"></div>
												</div>
											</div>
										</CDPageSpinner>
									</div>
								</template>
							</suspense>
						</Transition>
					</router-view> -->
				</div>
			</main>
		</div>
	</div>
</template>

<script lang="ts" setup>
// import logo from '@/assets/images/jpg_logo.jpg'

import {computed, onMounted, ref, watch} from 'vue'
import {cloneDeep} from 'lodash'
import {useRoute, useRouter} from 'vue-router'
import {emitter} from '@/utils/mitt'
import {CDAIcon, CDPageSpinner} from '@cd/design-system'
import {useI18n} from '@/composables/useI18n'

const versionPackage = import.meta.env.VITE_VERSION
const { $t } = useI18n()

const router = useRouter()

const baseImg = import.meta.env.VITE_UPLOAD_SERVICE
const garageOwner = JSON.parse(
	localStorage.getItem('garageOwner') as string
).name
const garage = ref(
	JSON.parse(localStorage.getItem('garage') as string).name || ''
)

const onDisplaySider = ref(false)
const computedOnDisplaySiderClass = computed(() => {
	if (onDisplaySider.value) {
		return 'block absolute xl:relative z-50'
	}
	return 'hidden xl:block'
})

const garageAvatar = ref(baseImg + localStorage.getItem('avatar') || '')

console.log('garageAvatar', garageAvatar)

const navigation = [
	{
		name: 'Trang chủ',
		href: '/app/home',
		icon: 'Home2',
	},
	{
		name: 'Quản lý bán hàng',
		href: '#',
		icon: 'ShoppingCart',
		isActive: false,
		child: [
			{
				name: 'Phiếu dịch vụ',
				href: '/app/sell/new-service-tickets',
				icon: '',
				activeSubmenu: 'Sell - Phiếu dịch vụ mới chi tiet',
				child: [
					{
						name: 'Xử lý phiếu dịch vụ',
						href: `/app/sell/new-service-tickets-detail/:id`,
					},
				],
			},
			{
				name: 'Bán hàng',
				href: '/app/sell/sell-spare-parts',
				icon: '',
				activeSubmenu: 'Sell - Xử lý đơn hàng',
			},
		],
	},
	{
		name: 'Quản trị kho',
		href: '#',
		icon: 'ArchiveBox',
		isActive: false,
		child: [
			{
				name: 'Danh sách đơn hàng đặt',
				href: '/app/inventory/order-spare-parts',
				icon: '',
				activeSubmenu: 'Inventory - Chi tiết đơn hàng đặt',
			},
			{
				name: 'Phiếu nhập kho',
				href: '/app/inventory/inbounds',
				icon: '',
			},
			{
				name: 'Phiếu xuất kho',
				href: '/app/inventory/outbounds',
				icon: '',
			},
			{
				name: 'Tồn kho phụ tùng',
				href: '/app/inventory/remain-spare-parts',
				icon: '',
			},
			{
				name: 'Lịch sử kiểm kê phụ tùng',
				href: '/app/inventory/histories',
				icon: '',
			},
		],
	},
	{
		name: 'Quản lý công nợ',
		href: '#',
		icon: 'DollarCircle',
		isActive: false,
		child: [
			{
				name: 'Danh sách đơn hàng đặt',
				href: '/app/debt/orders-distributor-controller',
				icon: '',
				activeSubmenu: ['Debt - Chỉnh sửa đơn bán', 'Debt - Chi tiết đơn bán'],
			},
			{
				name: 'Danh sách đơn hàng bán',
				href: '/app/debt/sales-order',
				icon: '',
				activeSubmenu: [
					'debt - Chi tiết danh sách đơn bán',
					'debt - Chỉnh sửa danh sách đơn bán',
				],
			},
			{
				name: 'Phiếu dịch vụ',
				href: '/app/debt/service-ticket',
				icon: '',
				activeSubmenu: [
					'Debt - Chi tiết dơn bán hàng hoá dịch vụ',
					'Debt - Chỉnh sửa dơn bán hàng hoá dịch vụ',
				],
				child: [
					{
						name: 'Chi tiết phiếu dịch vụ',
						href: `/app/debt/service-ticket/view/:id`,
					},
					{
						name: 'Chỉnh sửa phiếu dịch vụ',
						href: `/app/debt/service-ticket/edit/:id`,
					},
				],
			},
		],
	},
	{
		name: 'Quản lý nhân sự',
		href: '#',
		icon: 'UserOctagon',
		isActive: false,
		child: [
			{
				name: 'Danh sách nhân sự',
				href: '/app/employees',
				icon: '',
			},
			{
				name: 'Quản lý tài khoản',
				href: '/app/employees-report',
				icon: '',
			},
			{
				name: 'Vai trò tài khoản',
				href: '/app/employees-role',
				icon: '',
			},
		],
	},

	{
		name: 'Danh mục chung',
		icon: 'Category',
		isActive: false,
		child: [
			{
				name: 'Danh sách NPP',
				href: '/app/inventory/distributors',
				icon: '',
			},
			{
				name: 'Danh sách phụ tùng',
				href: '/app/inventory/spare-parts',
				icon: '',
			},
			{
				name: 'Danh mục dịch vụ',
				href: '/app/sell/services',
			},
			{
				name: 'Danh sách khách hàng',
				href: '/app/sell/customers',
				icon: 'fa-solid fa-sliders',
			},
			{
				name: 'Danh sách xe',
				href: '/app/sell/cars',
				icon: '',
			},
			{
				name: 'Quản lý giá bán',
				href: '/app/sell/product-prices',
				icon: ' ',
			},
		],
	},
] as any[]
const flattenAllRouterFlat = (menu: any) => {
	return menu.reduce(function (result: any, next: any) {
		result.push(next)
		if (next.child) {
			result = result.concat(flattenAllRouterFlat(next.child))
			next.child = []
		}
		return result
	}, [])
}
const route = useRoute()

const allFlattenRouter = flattenAllRouterFlat(cloneDeep(navigation))
const labelMenu = ref('')

watch(
	() => route.path,
	(path) => {
		labelMenu.value = allFlattenRouter?.find(
			(item: any) =>
				item?.href === path ||
				item?.href?.split('/')[3] == `${path?.split('/')[3]}`
		)?.name
	},
	{
		immediate: true,
		deep: true,
	}
)

const activeSubmenu = ref<{ index: number; active: boolean }>({
	index: 0,
	active: false,
})

const toggleSubmenu = (index: any) => {
	if (activeSubmenu.value.index === index) {
		activeSubmenu.value.index = NaN
	} else {
		activeSubmenu.value.index = index
	}

	navigation[index].isActive = !navigation[index].isActive
}

const isShowPopupChat = ref<boolean>(false)

const handleShowPopupChat = () => {
	isShowPopupChat.value = !isShowPopupChat.value
}

const beforeEnter = (element: any) => {
	requestAnimationFrame(() => {
		if (!element.style.height) {
			element.style.height = '0px'
		}

		element.style.display = null
	})
}
const enter = (element: any) => {
	requestAnimationFrame(() => {
		requestAnimationFrame(() => {
			element.style.height = `${element.scrollHeight}px`
		})
	})
}
const afterEnter = (element: any) => {
	element.style.height = null
}
const beforeLeave = (element: any) => {
	requestAnimationFrame(() => {
		if (!element.style.height) {
			element.style.height = `${element.offsetHeight}px`
		}
	})
}
const leave = (element: any) => {
	requestAnimationFrame(() => {
		requestAnimationFrame(() => {
			element.style.height = '0px'
		})
	})
}
const afterLeave = (element: any) => {
	element.style.height = null
}

emitter.on('on-update-avatar', (data: any) => {
	if (data) {
		garageAvatar.value = baseImg + data
		localStorage.setItem('avatar', data)
	}
})
onMounted(() => {
	garage.value = JSON.parse(localStorage.getItem('garage') as string).name
	garageAvatar.value = baseImg + localStorage.getItem('avatar')
})

const redirect = (ci: any) => {
	garageAvatar.value = baseImg + localStorage.getItem('avatar')
	garage.value = JSON.parse(localStorage.getItem('garage') as string).name
	if (ci.hasOwnProperty('href')) {
		router.push(ci.href)
	}
}

const handleLogout = () => {
	localStorage.clear()
	router.push('/')
}

const handleChangeAccount = (currentTab?: number) => {
	emitter.emit('on-change-account-password', currentTab)
	router.push('/app/profile')
}
</script>

<style scoped lang="scss">
.avatar {
	width: 35px !important;
	height: 35px !important;
	border-radius: 100% !important;
}
.fade-enter-active,
.fade-leave-active {
	transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
	opacity: 0;
}

.custome-float-button {
	width: 50px;
	height: 50px;
	background: #25b3e8;
	border-radius: 50%;
	position: absolute;
	right: 0px;
	bottom: 50px;
	z-index: 100;
	text-align: center;
	line-height: 50px;
	color: white;
	cursor: pointer;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
	transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

::-webkit-scrollbar {
	width: 4px;
	height: 0;
	background-color: var(--color-layout-primary);
}

::-webkit-scrollbar-thumb {
	background-color: #e5e5e5;
	border-radius: 2px;
}

.show-submenu {
	display: block !important;
}

.hidden-submenu {
	display: none;
}
</style>
