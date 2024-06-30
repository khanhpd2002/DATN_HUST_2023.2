<template>
    <div>
        <div
            class="bg-layout-primary rounded-md shadow-lg fixed top-0 left-0 transform z-30 w-full h-full overflow-y-scroll"
        >
            <div class="invoice_wrap">
                <body class="invoice-gray">
                    <div class="invoice_wrap">
                        <ACCDIcon
                            name="fa-solid icon-xmark"
                            class="cursor-pointer absolute p-4 top-0 right-0"
                            @click="emit('close')"
                        ></ACCDIcon>
                        <div class="invoice-container">
                            <div
                                class="invoice-content-wrap light-invoice-content-wrap"
                                id="download_section"
                            >
                                <header class="" id="invo_header">
                                    <div class="invoice-logo-content">
                                        <div class="headerLogo">
                                            <!-- <div>{{ title }}</div> -->
                                            <p class="logo inter-700">
                                                {{ title }}
                                            </p>
                                        </div>
                                    </div>
                                </header>
                                <section
                                    class="agency-service-content white-content-section"
                                    id=""
                                >
                                    <div class="container">
                                        <div
                                            class="bus-detail-wrap money-detail-wrap"
                                        >
                                            <div id="app">
                                                <div class="customerPhone">
                                                    <div
                                                        class="bus-detail-col border-bottom"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            Khách hàng:
                                                            <span>{{
                                                                customerInfo.customerName
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        >
                                                            <span id=""></span>
                                                        </div>
                                                    </div>
                                                    <div
                                                        class="bus-detail-col border-bottom"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            Số điện thoại:
                                                            <span>{{
                                                                customerInfo.customerPhone
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        >
                                                            <span id=""></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="car" id="myCar">
                                                    <div
                                                        class="bus-detail-col seat-col"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            Hãng xe:
                                                            <span>{{
                                                                carInfo.carBrand
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        >
                                                            <span id=""></span>
                                                        </div>
                                                    </div>
                                                    <div class="bus-detail-col">
                                                        <div
                                                            class="bus-type text-black border border-black pl-2"
                                                        >
                                                            Dòng xe:
                                                            <span>{{
                                                                carInfo.carModel
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        >
                                                            <span id=""></span>
                                                        </div>
                                                    </div>
                                                    <div class="bus-detail-col">
                                                        <div
                                                            class="bus-type text-black border border-black pl-2"
                                                        >
                                                            Đời xe:{{
                                                                carInfo.carYear
                                                            }}
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        >
                                                            <span id=""></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="version">
                                                    <div
                                                        class="bus-detail-col seat-col"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            Phiên bản xe:
                                                            <span>{{
                                                                carInfo.carVersion
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        ></div>
                                                    </div>
                                                    <div class="bus-detail-col">
                                                        <div
                                                            class="bus-type text-black border border-black pl-2"
                                                        >
                                                            BKS:
                                                            <span>{{
                                                                carInfo.licensePlate
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        ></div>
                                                    </div>
                                                    <div class="bus-detail-col">
                                                        <div
                                                            class="bus-type text-black border border-black pl-2"
                                                        >
                                                            Số VIN:
                                                            <span>{{
                                                                carInfo.vinNumber
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        ></div>
                                                    </div>
                                                </div>
                                                <div class="flex">
                                                    <div
                                                        class="bus-detail-col seat-col"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            Số km hiện tại:
                                                            <span>{{
                                                                carInfo.odo
                                                            }}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        ></div>
                                                    </div>
                                                    <div
                                                        class="bus-detail-col seat-col"
                                                    >
                                                        <div
                                                            class="bus-type medium-font text-black border border-black pl-2"
                                                        >
                                                            <span>{{}}</span>
                                                        </div>
                                                        <div
                                                            class="bus-tname second-color inter-400 medium-font"
                                                        ></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="text-black inter-700">
                                            <p>Yêu cầu của khách hàng:</p>
                                        </div>
                                        <div
                                            class="bus-detail-wrap newDetail-wrap text-typo"
                                        >
                                            {{ description }}
                                        </div>
                                        <div class="table-wrapper dark-table">
                                            <table class="invoice-table">
                                                <thead>
                                                    <div
                                                        class="invoice-owner-conte-wrap newBorder text-black inter-700 max-w-max-content mb-5"
                                                    >
                                                        <p>
                                                            Công việc cần thực
                                                            hiện:
                                                        </p>
                                                    </div>
                                                    <tr class="invo-tb-header">
                                                        <th
                                                            class="invo-table-title wrap-wid border border-black text-center medium-font text-black weight-nomarl"
                                                        >
                                                            Tên Dịch vụ
                                                        </th>
                                                        <th
                                                            class="invo-table-title pric-wid border border-black text-center medium-font text-black weight-nomarl"
                                                        >
                                                            Đơn giá
                                                        </th>
                                                        <th
                                                            class="invo-table-title tota-wid border border-black text-center rate-title medium-font text-black weight-nomarl"
                                                        >
                                                            SL
                                                        </th>
                                                        <th
                                                            class="invo-table-title pric-wid border border-black text-center rate-title medium-font text-black weight-nomarl"
                                                        >
                                                            Chiết khấu
                                                        </th>
                                                        <th
                                                            class="invo-table-title wrap-wid border border-black text-center rate-title medium-font text-black weight-nomarl"
                                                        >
                                                            Thuế
                                                        </th>
                                                        <th
                                                            class="invo-table-title tota-wid border border-black text-center medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Tổng
                                                        </th>
                                                        <th
                                                            class="invo-table-title tota-wid border border-black text-center medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Người thực hiện
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody
                                                    class="invo-tb-body"
                                                    id="app"
                                                >
                                                    <tr
                                                        class="invo-tb-row"
                                                        v-for="part in serviceInfo"
                                                    >
                                                        <td
                                                            class="invo-tb-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
                                                                part.serviceName
                                                            }}</span>
                                                        </td>
                                                        <td
                                                            class="invo-tb-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
		                                                            (part.unitPrice * 1000)
                                                                    .toString()
                                                                    .replace(
                                                                        /\B(?=(\d{3})+(?!\d))/g,
                                                                        "."
                                                                    )
                                                            }}</span>
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
                                                                part.quantity
                                                            }}</span>
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
                                                                part.discount
                                                                    .toString()
                                                                    .replace(
                                                                        /\B(?=(\d{3})+(?!\d))/g,
                                                                        "."
                                                                    )
                                                            }} {{ discountType == 1 ? 'VNĐ' : '%'}}</span>
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span
                                                                >{{
                                                                    part.tax
                                                                }}%</span
                                                            >
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
		                                                            (part.total * 1000)
                                                                    .toFixed(0)
                                                                    .toString()
                                                                    .replace(
                                                                        /\B(?=(\d{3})+(?!\d))/g,
                                                                        "."
                                                                    )
                                                            }}</span>
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            <span>{{
                                                                part.employeeName
                                                            }}</span>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="table-wrapper dark-table">
                                            <table class="invoice-table">
                                                <thead>
                                                    <div
                                                        class="invoice-owner-conte-wrap text-black inter-700 max-w-max-content mb-5"
                                                    >
                                                        <p>Phụ tùng/Vật tư:</p>
                                                    </div>
                                                    <tr class="invo-tb-header">
                                                        <th
                                                            class="max-w-200 invo-table-title re-desc-wid border border-black text-center inter-700 medium-font text-black weight-nomarl"
                                                        >
                                                            Tên Phụ Tùng
                                                        </th>
                                                        <th
                                                            class="invo-table-title re-price-wid border border-black text-center rate-title inter-700 medium-font text-black weight-nomarl"
                                                        >
                                                            ĐVT
                                                        </th>
                                                        <th
                                                            class="invo-table-title re-price-wid border border-black text-center rate-title inter-700 medium-font text-black weight-nomarl"
                                                        >
                                                            SL
                                                        </th>
                                                        <th
                                                            class="invo-table-title pric-wid border border-black text-center inter-700 medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Đơn giá
                                                        </th>
                                                        <th
                                                            class="invo-table-title pric-wid border border-black text-center inter-700 medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Chiết khấu
                                                        </th>
                                                        <th
                                                            class="invo-table-title pric-wid border border-black text-center inter-700 medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Thuế
                                                        </th>
                                                        <th
                                                            class="invo-table-title tota-wid border border-black text-center inter-700 medium-font total-head text-black weight-nomarl"
                                                        >
                                                            Tổng
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody class="invo-tb-body">
                                                    <tr
                                                        class="invo-tb-row"
                                                        v-for="part in productInfo"
                                                    >
                                                        <td
                                                            class="invo-tb-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{
                                                                part.productName
                                                            }}
                                                        </td>
                                                        <td
                                                            class="invo-tb-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{ part.unit }}
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{ part.quantity }}
                                                        </td>
                                                        <td
                                                            class="invo-tb-data rate-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{
		                                                        (part.unitPrice * 1000)
                                                                    .toString()
                                                                    .replace(
                                                                        /\B(?=(\d{3})+(?!\d))/g,
                                                                        "."
                                                                    )
                                                            }}
                                                        </td>
                                                        <td
                                                            class="invo-tb-data total-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{ part.discount }} {{ discountType == 1 ? 'VNĐ' : '%'}}
                                                        </td>
                                                        <td
                                                            class="invo-tb-data total-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{ part.tax }}%
                                                        </td>
                                                        <td
                                                            class="invo-tb-data total-data text-black weight-nomarl border border-black text-center"
                                                        >
                                                            {{
		                                                        (part.money * 1000)
                                                                    .toString()
                                                                    .replace(
                                                                        /\B(?=(\d{3})+(?!\d))/g,
                                                                        "."
                                                                    )
                                                            }}
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div>
                                            <div
                                                class="bus-type inter-700 medium-font text-black service-advisor max-w-max-content mt-5"
                                            >
                                                Cố vấn dịch vụ
                                            </div>
                                            <div>
                                                <br />
                                                <br />
                                                <br />
                                            </div>
                                        </div>
                                        <div class="invo-addition-wrap">
                                            <div
                                                class="invo-add-info-content"
                                            ></div>
                                            <div
                                                class="invo-bill-total dark-invo-bill"
                                            >
                                                <table
                                                    class="invo-total-table"
                                                    id="app"
                                                ></table>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                        <section
                            class="agency-bottom-content d-print-none"
                            id="agency_bottom"
                        >
                            <div class="container">
                                <div class="invo-buttons-wrap">
                                    <div class="invo-print-btn invo-btns">
                                        <a
                                            href="javascript:window.print()"
                                            class="print-btn"
                                        >
                                            <svg
                                                width="24"
                                                height="24"
                                                viewBox="0 0 24 24"
                                                fill="none"
                                                xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <g
                                                    clip-path="url(#clip0_5_313)"
                                                >
                                                    <path
                                                        d="M14 3V7C14 7.26522 14.1054 7.51957 14.2929 7.70711C14.4804 7.89464 14.7348 8 15 8H19"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M17 21H7C6.46957 21 5.96086 20.7893 5.58579 20.4142C5.21071 20.0391 5 19.5304 5 19V5C5 4.46957 5.21071 3.96086 5.58579 3.58579C5.96086 3.21071 6.46957 3 7 3H14L19 8V19C19 19.5304 18.7893 20.0391 18.4142 20.4142C18.0391 20.7893 17.5304 21 17 21Z"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M9 7H10"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M9 13H15"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M13 17H15"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                </g>
                                                <defs>
                                                    <clipPath id="clip0_5_313">
                                                        <rect
                                                            width="24"
                                                            height="24"
                                                            fill="white"
                                                        />
                                                    </clipPath>
                                                </defs>
                                            </svg>
                                            <span class="inter-700 medium-font"
                                                >Print</span
                                            >
                                        </a>
                                    </div>
                                    <div class="invo-down-btn invo-btns">
                                        <a
                                            class="download-btn"
                                            id="generatePDF"
                                        >
                                            <svg
                                                width="24"
                                                height="24"
                                                viewBox="0 0 24 24"
                                                fill="none"
                                                xmlns="http://www.w3.org/2000/svg"
                                            >
                                                <g
                                                    clip-path="url(#clip0_5_246)"
                                                >
                                                    <path
                                                        d="M4 17V19C4 19.5304 4.21071 20.0391 4.58579 20.4142C4.96086 20.7893 5.46957 21 6 21H18C18.5304 21 19.0391 20.7893 19.4142 20.4142C19.7893 20.0391 20 19.5304 20 19V17"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M7 11L12 16L17 11"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                    <path
                                                        d="M12 4V16"
                                                        stroke="white"
                                                        stroke-width="2"
                                                        stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                    />
                                                </g>
                                                <defs>
                                                    <clipPath id="clip0_5_246">
                                                        <rect
                                                            width="24"
                                                            height="24"
                                                            fill="white"
                                                        />
                                                    </clipPath>
                                                </defs>
                                            </svg>
                                            <span class="inter-700 medium-font"
                                                >Download</span
                                            >
                                        </a>
                                    </div>
                                </div>
                                <div class="invo-note-wrap">
                                    <div class="note-title">
                                        <svg
                                            width="24"
                                            height="24"
                                            viewBox="0 0 24 24"
                                            fill="none"
                                            xmlns="http://www.w3.org/2000/svg"
                                        >
                                            <g clip-path="url(#clip0_8_240)">
                                                <path
                                                    d="M14 3V7C14 7.26522 14.1054 7.51957 14.2929 7.70711C14.4804 7.89464 14.7348 8 15 8H19"
                                                    stroke="#00BAFF"
                                                    stroke-width="2"
                                                    stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                />
                                                <path
                                                    d="M17 21H7C6.46957 21 5.96086 20.7893 5.58579 20.4142C5.21071 20.0391 5 19.5304 5 19V5C5 4.46957 5.21071 3.96086 5.58579 3.58579C5.96086 3.21071 6.46957 3 7 3H14L19 8V19C19 19.5304 18.7893 20.0391 18.4142 20.4142C18.0391 20.7893 17.5304 21 17 21Z"
                                                    stroke="#00BAFF"
                                                    stroke-width="2"
                                                    stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                />
                                                <path
                                                    d="M9 7H10"
                                                    stroke="#00BAFF"
                                                    stroke-width="2"
                                                    stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                />
                                                <path
                                                    d="M9 13H15"
                                                    stroke="#00BAFF"
                                                    stroke-width="2"
                                                    stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                />
                                                <path
                                                    d="M13 17H15"
                                                    stroke="#00BAFF"
                                                    stroke-width="2"
                                                    stroke-linecap="round"
                                                    stroke-linejoin="round"
                                                />
                                            </g>
                                            <defs>
                                                <clipPath id="clip0_8_240">
                                                    <rect
                                                        width="24"
                                                        height="24"
                                                        fill="white"
                                                    />
                                                </clipPath>
                                            </defs>
                                        </svg>
                                        <span class="inter-700 medium-font"
                                            >Note:</span
                                        >
                                    </div>
                                    <h3
                                        class="inter-400 medium-font second-color note-desc mtb-0 text-black"
                                    >
                                        This is computer generated receipt and
                                        does not require physical signature.
                                    </h3>
                                </div>
                            </div>
                        </section>
                    </div>
                </body>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { onMounted } from "vue";

type CarInfo = {
    carVersion: string;
    carBrand: string;
    carYear: String;
    carModel: string;
    licensePlate: string;
    vinNumber: string;
    odo: string;
};
onMounted(() => {});
type CustomerInfo = {
    customerName: string;
    customerPhone: string | Number;
};

type ServiceInfo = {
    serviceName: string;
    quantity: number;
    unitPrice: number;
    money: number;
    discount: number;
    tax: number;
    employeeName: string;
};

type ProductInfo = {
    productName: string;
    unit: string;
    quantity: string;
    unitPrice: number;
    discount: number;
    tax: number;
    money: number;
};

type BillProps = {
    title: string;
    customerInfo: any;
    carInfo: any;
    description: string;
    serviceInfo: any[];
    productInfo: any[];
	discountType: any;
};

const printPdf = () => {
    var downloadSection = document.getElementById(
        "download_section"
    ) as HTMLElement;
};

const props = withDefaults(defineProps<BillProps>(), {
    title: "Lệnh sửa chữa",
    customerInfo: {
        customerName: "Tên khách hàng",
        customerPhone: "095198198195",
    } as CustomerInfo,
    carInfo: {},
    description: "Sửa hộ cái xe với bro ơi",
	discountType: 2
});
const emit = defineEmits<{
    (e: "close"): void;
}>();
</script>

<style scoped>
@import "./css/custom.css";
@import "./css/media-query.css";
.modal-fade-enter,
.modal-fade-leave-to {
    opacity: 0;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
    transition: opacity 0.2s ease;
}
</style>
