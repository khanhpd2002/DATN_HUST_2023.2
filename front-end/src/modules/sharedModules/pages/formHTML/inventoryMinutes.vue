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
                                        <div class="headerLogo inter-700">
                                            <p class="logo">{{ title }}</p>
                                        </div>
                                    </div>
                                </header>
                                <section
                                    class="agency-service-content white-content-section"
                                    id=""
                                >
                                    <div
                                        class="bus-type medium-font text-black minutes-date flex date-inventory"
                                    >
                                        <h1 class="mr-4">
                                            Ngày
                                            {{
                                                dayjs(
                                                    props.inventoryDate,
                                                    "DD/MM/YYYY"
                                                ).date()
                                            }}
                                        </h1>
                                        <h1 class="mr-4">
                                            Tháng
                                            {{
                                                dayjs(
                                                    props.inventoryDate,
                                                    "DD/MM/YYYY"
                                                ).month() + 1
                                            }}
                                        </h1>
                                        <h1>
                                            Năm
                                            {{
                                                dayjs(
                                                    props.inventoryDate,
                                                    "DD/MM/YYYY"
                                                ).year()
                                            }}
                                        </h1>
                                    </div>
                                    <div class="container">
                                        <div>
                                            <div class="text-black mt-20">
                                                <p>Người thực hiện kiểm kê:</p>
                                            </div>
                                            <div
                                                class="text-black mt-5 container"
                                            >
                                                <p v-for="e in listEmployee">
                                                    - Ông/Bà:
                                                    {{ e.counterName }}
                                                </p>
                                            </div>
                                        </div>
                                        <div class="table-wrapper dark-table">
                                            <div class="text-black mb-5">
                                                <p>
                                                    Đã kiểm kê vật tư phụ tùng,
                                                    kết quả như sau:
                                                </p>
                                            </div>
                                            <table class="table-primary w-full">
                                                <thead>
                                                    <tr
                                                        class="whitespace-nowrap"
                                                    >
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black align-top"
                                                            colspan="1"
                                                            rowspan="2"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Vật tư
                                                                        phụ
                                                                        tùng</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black align-top"
                                                            colspan="1"
                                                            rowspan="2"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >ĐVT</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black align-top"
                                                            colspan="1"
                                                            rowspan="2"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Đơn
                                                                        giá</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black"
                                                            colspan="2"
                                                            rowspan="1"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl mx-auto"
                                                                        >Theo hệ
                                                                        thống</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black mx-auto"
                                                            colspan="2"
                                                            rowspan="1"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Thực
                                                                        tế</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black"
                                                            colspan="2"
                                                            rowspan="1"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Chênh
                                                                        lệch</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                    </tr>
                                                    <tr>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black border-t-black-800"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >SL</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black border-t-black-800"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Thành
                                                                        tiền</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black border-t-black-800"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >SL</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Thành
                                                                        tiền</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >SL</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                        <th
                                                            class="bg-table-secondary px-4 h-table-header-md text-xs font-semibold tracking-wider text-typo border border-black"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            <div
                                                                class="flex items-center"
                                                            >
                                                                <div
                                                                    class="grow"
                                                                >
                                                                    <a
                                                                        class="link group items-center inline-flex focus:outline-none w-full justify-undefined text-black weight-nomarl"
                                                                        >Thành
                                                                        tiền</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr
                                                        class=""
                                                        v-for="p in productInfo"
                                                    >
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{ p.productName }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{ p.unit }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{ p.unitPrice }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                p.systemInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                p.systemInventory *
                                                                p.unitPrice
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                p.realityInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                p.realityInventory *
                                                                p.unitPrice
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                p.realityInventory -
                                                                p.systemInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-primary px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border-b border-line"
                                                            style="
                                                                max-width: unset;
                                                            "
                                                        >
                                                            {{
                                                                Math.abs(
                                                                    p.realityInventory -
                                                                        p.systemInventory
                                                                ) * p.unitPrice
                                                            }}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            <p
                                                                class="inter-700"
                                                            >
                                                                Tổng
                                                            </p>
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        ></td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        ></td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalSystemInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalPriceSystemInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalRealityInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalPriceRealityInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalDifferenceInventory
                                                            }}
                                                        </td>
                                                        <td
                                                            class="whitespace-normal bg-table-muted px-4 py-2.5 text-sm font-medium text-typo overflow-hidden border border-black"
                                                        >
                                                            {{
                                                                totalPriceDifferenceInventory
                                                            }}
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                            <div
                                                class="text-black flex footer-inventory inter-700 mt-10"
                                            >
                                                <p>GIÁM ĐỐC</p>
                                                <p>NGƯỜI THỰC HIỆN KIỂM KÊ</p>
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
import { computed } from "vue";
import { onMounted } from "vue";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";

dayjs.extend(customParseFormat);

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
    inventoryDate: string;
    productInfo: any;
    listEmployee: any;
};

const printPdf = () => {
    var downloadSection = document.getElementById(
        "download_section"
    ) as HTMLElement;
};

const props = withDefaults(defineProps<BillProps>(), {
    title: "Biên bản kiểm kê",
    productInfo: [] as any[],
    inventoryDate: "",
    listEmployee: [] as any[],
});
const emit = defineEmits<{
    (e: "close"): void;
}>();
const totalSystemInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result += p.systemInventory;
    });
    return result;
});

const totalPriceSystemInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result += p.systemInventory * p.unitPrice;
    });
    return result;
});

const totalRealityInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result += p.realityInventory;
    });
    return result;
});
const totalPriceRealityInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result += p.realityInventory * p.unitPrice;
    });
    return result;
});
const totalDifferenceInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result += Math.abs(p.realityInventory - p.systemInventory);
    });
    return result;
});

const totalPriceDifferenceInventory = computed(() => {
    let result = 0;
    props.productInfo.forEach((p: any) => {
        result +=
            Math.abs(p.realityInventory - p.systemInventory) * p.unitPrice;
    });
    return result;
});
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
