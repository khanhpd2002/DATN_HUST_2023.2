<template>
    <div class="invoice_wrap">
        <head>
            <meta charset="utf-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta
                name="viewport"
                content="width=device-width, initial-scale=1, shrink-to-fit=no"
            />
            <title>Service Voucher</title>
            <link href="../formHTML/images/logo/fevicon.ico" rel="icon" />
            <link
                href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap"
                rel="stylesheet"
            />
            <link
                rel="stylesheet"
                type="text/css"
                href="../formHTML/css/custom.css"
            />
            <link
                rel="stylesheet"
                type="text/css"
                href="../formHTML/css/media-query.css"
            />
        </head>
        <body class="invoice-gray">
            <div class="invoice_wrap">
                <div class="invoice-container">
                    <div
                        class="invoice-content-wrap light-invoice-content-wrap"
                        id="download_section"
                    >
                        <header class="" id="invo_header">
                            <div class="invoice-logo-content">
                                <div class="headerLogo">
                                    <p class="logo">PHIẾU DỊCH VỤ</p>
                                </div>
                            </div>
                        </header>
                        <section
                            class="agency-service-content white-content-section"
                            id=""
                        >
                            <div class="container">
                                <div class="bus-detail-wrap money-detail-wrap">
                                    <div id="app">
                                        <div class="customerPhone">
                                            <div
                                                class="bus-detail-col border-bottom"
                                            >
                                                <div
                                                    class="bus-type inter-700 medium-font text-black"
                                                >
                                                    Khách hàng:
                                                    <span>{{
                                                        customerName
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
                                                    class="bus-type inter-700 medium-font text-black"
                                                >
                                                    Số điện thoại:<span>{{
                                                        customerPhone
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
                                                    class="bus-type inter-700 medium-font text-black"
                                                >
                                                    Hãng xe:<span>{{
                                                        carVersion
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
                                                    class="bus-type inter-700 text-black"
                                                >
                                                    Dòng xe:<span>{{
                                                        carLife
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
                                                    class="bus-type inter-700 text-black"
                                                >
                                                    Đời xe:{{ carYears }}
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
                                                    class="bus-type inter-700 medium-font text-black"
                                                >
                                                    Phiên bản xe:<span>{{
                                                        vehicleVersion
                                                    }}</span>
                                                </div>
                                                <div
                                                    class="bus-tname second-color inter-400 medium-font"
                                                ></div>
                                            </div>
                                            <div class="bus-detail-col">
                                                <div
                                                    class="bus-type inter-700 text-black"
                                                >
                                                    BKS:<span>{{
                                                        seaControl
                                                    }}</span>
                                                </div>
                                                <div
                                                    class="bus-tname second-color inter-400 medium-font"
                                                ></div>
                                            </div>
                                            <div class="bus-detail-col">
                                                <div
                                                    class="bus-type inter-700 text-black"
                                                >
                                                    Số VIN:<span>{{
                                                        vinNumber
                                                    }}</span>
                                                </div>
                                                <div
                                                    class="bus-tname second-color inter-400 medium-font"
                                                ></div>
                                            </div>
                                        </div>
                                        <div>
                                            <div
                                                class="bus-detail-col seat-col"
                                            >
                                                <div
                                                    class="bus-type inter-700 medium-font text-black"
                                                >
                                                    Số km hiện tại:
                                                    <span>{{ kilometer }}</span>
                                                </div>
                                                <div
                                                    class="bus-tname second-color inter-400 medium-font"
                                                ></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-black">
                                    <p>Yêu cầu của khách hàng:</p>
                                </div>
                                <div
                                    class="bus-detail-wrap money-detail-wrap"
                                ></div>
                                <div class="table-wrapper dark-table">
                                    <table class="invoice-table">
                                        <thead>
                                            <div
                                                class="invoice-owner-conte-wrap newBorder text-black"
                                            >
                                                <p>Công việc cần thực hiện:</p>
                                            </div>
                                            <tr class="invo-tb-header">
                                                <th
                                                    class="invo-table-title sno-wid inter-700 medium-font text-black"
                                                >
                                                    Tên CV
                                                </th>
                                                <th
                                                    class="invo-table-title re-desc-wid inter-700 medium-font text-black"
                                                >
                                                    Đơn giá
                                                </th>
                                                <th
                                                    class="invo-table-title re-price-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    SL
                                                </th>
                                                <th
                                                    class="invo-table-title re-qty-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    Thành tiền
                                                </th>
                                                <th
                                                    class="invo-table-title tota-wid inter-700 medium-font total-head text-black"
                                                >
                                                    Chiết khấu
                                                </th>
                                                <th
                                                    class="invo-table-title sno-wid inter-700 medium-font text-black"
                                                >
                                                    Thuế
                                                </th>
                                                <th
                                                    class="invo-table-title sno-wid inter-700 medium-font text-black"
                                                >
                                                    Tổng tiền
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody class="invo-tb-body" id="app">
                                            <tr class="invo-tb-row">
                                                <td
                                                    class="invo-tb-data text-black"
                                                >
                                                    <span>{{ jobName }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data text-black"
                                                >
                                                    <span>{{ unitPrice }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{ jobAmount }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{
                                                        calculateTotal()
                                                    }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{
                                                        jobDiscount
                                                    }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{ jobTax }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data total-data text-black"
                                                >
                                                    <span>{{
                                                        calculateGrandTotal()
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
                                                class="invoice-owner-conte-wrap newBorder text-black"
                                            >
                                                <p>Phụ tùng/Vật tư:</p>
                                            </div>
                                            <tr class="invo-tb-header">
                                                <th
                                                    class="invo-table-title sno-wid inter-700 medium-font text-black"
                                                >
                                                    Tên PT
                                                </th>
                                                <th
                                                    class="invo-table-title re-desc-wid inter-700 medium-font text-black"
                                                >
                                                    Đơn giá
                                                </th>
                                                <th
                                                    class="invo-table-title re-price-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    ĐVT
                                                </th>
                                                <th
                                                    class="invo-table-title re-qty-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    SL
                                                </th>
                                                <th
                                                    class="invo-table-title tota-wid inter-700 medium-font total-head text-black"
                                                >
                                                    Thành tiền
                                                </th>
                                                <th
                                                    class="invo-table-title re-qty-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    Chiết khấu
                                                </th>
                                                <th
                                                    class="invo-table-title re-qty-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    Thuế
                                                </th>
                                                <th
                                                    class="invo-table-title re-qty-wid rate-title inter-700 medium-font text-black"
                                                >
                                                    Tổng tiền
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody class="invo-tb-body">
                                            <tr
                                                class="invo-tb-row"
                                                v-for="part in parts"
                                            >
                                                <td
                                                    class="invo-tb-data text-black"
                                                >
                                                    {{ part.name }}
                                                </td>
                                                <td
                                                    class="invo-tb-data text-black"
                                                >
                                                    {{ part.unitPrice }}
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    {{ part.unit }}
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    {{ part.quantity }}
                                                </td>
                                                <td
                                                    class="invo-tb-data total-data text-black"
                                                >
                                                    {{ part.total }}
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{
                                                        jobDiscount
                                                    }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data rate-data text-black"
                                                >
                                                    <span>{{ jobTax }}</span>
                                                </td>
                                                <td
                                                    class="invo-tb-data total-data text-black"
                                                >
                                                    <span>{{
                                                        calculateGrandTotal()
                                                    }}</span>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="invo-addition-wrap">
                                    <div class="invo-add-info-content"></div>
                                    <div class="invo-bill-total dark-invo-bill">
                                        <table
                                            class="invo-total-table"
                                            id="app"
                                        >
                                            <tbody>
                                                <tr>
                                                    <td
                                                        class="inter-700 medium-font w-text hotel-sub text-black"
                                                    >
                                                        Thành tiền:<span>{{
                                                            totalIntoMoney
                                                        }}</span>
                                                    </td>
                                                    <td
                                                        class="invo-total-data inter-400 medium-font second-color"
                                                    ></td>
                                                </tr>
                                                <tr class="tax-row">
                                                    <td
                                                        class="inter-700 medium-font w-text hotel-sub text-black"
                                                    >
                                                        Tổng tiền chi tiết
                                                        khấu:<span
                                                            class="invo-total-data inter-700 medium-font second-color"
                                                            >{{
                                                                totalDeductionDetails
                                                            }}</span
                                                        >
                                                    </td>
                                                    <td
                                                        class="invo-total-data inter-400 medium-font second-color"
                                                    ></td>
                                                </tr>
                                                <tr
                                                    class="disc-row bottom-border"
                                                >
                                                    <td
                                                        class="inter-700 medium-font w-text hotel-sub text-black"
                                                    >
                                                        Tổng tiền thuế:
                                                        <span>{{
                                                            totalTaxAmount
                                                        }}</span>
                                                    </td>
                                                    <td
                                                        class="invo-total-data inter-400 medium-font discount-price"
                                                    ></td>
                                                </tr>
                                                <tr class="invo-grand-total">
                                                    <td
                                                        class="inter-700 sm-text primary-color hotel-sub text-black"
                                                    >
                                                        Tổng tiền :<span>{{
                                                            totalPrice
                                                        }}</span>
                                                    </td>
                                                    <td
                                                        class="sm-text w-text invo-total-price"
                                                    ></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div
                                    class="bus-detail-wrap money-detail-wrap text-black"
                                >
                                    <p>
                                        Tổng số tiền khách hàng thanh toán bằng
                                        chữ:
                                    </p>
                                </div>
                                <div class="bus-detail-wrap money-detail-wrap">
                                    <div class="customerPhone">
                                        <div
                                            class="bus-detail-col border-bottom"
                                        >
                                            <div
                                                class="bus-type inter-700 medium-font text-black"
                                            >
                                                Cố vấn dịch vụ:
                                            </div>
                                            <div
                                                class="bus-tname second-color inter-400 medium-font"
                                            ></div>
                                        </div>
                                        <div
                                            class="bus-detail-col border-bottom"
                                        >
                                            <div
                                                class="bus-type inter-700 medium-font text-black"
                                            >
                                                Khách hàng:
                                            </div>
                                            <div
                                                class="bus-tname second-color inter-400 medium-font"
                                            ></div>
                                        </div>
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
                                        <g clip-path="url(#clip0_5_313)">
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
                                <a class="download-btn" id="generatePDF">
                                    <svg
                                        width="24"
                                        height="24"
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        xmlns="http://www.w3.org/2000/svg"
                                    >
                                        <g clip-path="url(#clip0_5_246)">
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
                                <span class="inter-700 medium-font">Note:</span>
                            </div>
                            <h3
                                class="inter-400 medium-font second-color note-desc mtb-0 text-black"
                            >
                                This is computer generated receipt and does not
                                require physical signature.
                            </h3>
                        </div>
                    </div>
                </section>
            </div>
        </body>
    </div>
</template>
<!-- <script src="https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.js"></script>
<script src="../formHTML/js/custom.js"></script>
<script src="../formHTML/js/html2canvas.min.js"></script>
<script src="../formHTML/js/jquery.min.js"></script>
<script src="../formHTML/js/jspdf.min.js"></script> -->
<script>
export default {
    data() {
        return {
            jobName: "Tên công việc",
            unitPrice: "$100",
            jobAmount: 5,
            jobDiscount: "$20",
            jobTax: "$10",
            parts: [
                {
                    name: "PT 1",
                    unitPrice: 10,
                    unit: "Cái",
                    quantity: 5,
                    total: 50,
                },
                {
                    name: "PT 2",
                    unitPrice: 8,
                    unit: "Cái",
                    quantity: 3,
                    total: 24,
                },
            ],
            customerName: "ABC",
            customerPhone: "0123456789",
            carVersion: "Suzuki",
            carLife: "XYZ",
            carYears: "2023",
            vehicleVersion: "Car",
            seaControl: "29S1-88888",
            vinNumber: "88888888",
            kilometer: "10000",
        };
    },
    computed: {
        totalIntoMoney() {
            const jobIntoMoney =
                parseFloat(String(this.unitPrice).substring(1)) *
                this.jobAmount;
            const partIntoMoney = this.parts.reduce((total, part) => {
                return total + Number(part.unitPrice) * part.quantity;
            }, 0);
            return (jobIntoMoney + partIntoMoney).toFixed(2);
        },
        totalDeductionDetails() {
            const jobDiscount =
                parseFloat(String(this.jobDiscount).substring(1)) *
                this.jobAmount;
            const partDiscount = this.parts.reduce((total, part) => {
                return total + Number(part.jobDiscount) * part.quantity;
            }, 0);
            return (jobDiscount + partDiscount).toFixed(2);
        },
    },
    methods: {
        calculateTotal() {
            return (
                parseFloat(this.unitPrice.substring(1)) * this.jobAmount
            ).toFixed(2);
        },
        calculateGrandTotal() {
            const totalAmount =
                parseFloat(this.unitPrice.substring(1)) * this.jobAmount;
            const totalDiscount = parseFloat(this.jobDiscount.substring(1));
            const totalTax = parseFloat(this.jobTax.substring(1));
            return (totalAmount - totalDiscount + totalTax).toFixed(2);
        },
    },
};
</script>

<style scoped>
@import "./css/custom.css";
@import "./css/media-query.css";
</style>
