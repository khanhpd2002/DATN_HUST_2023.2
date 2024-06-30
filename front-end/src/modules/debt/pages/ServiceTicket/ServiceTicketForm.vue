<template>
    <ACCDModal
        v-model="lazyValue"
        class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
        :title="computedTitle"
    >
        <div class="cd-modal__body py-4">
            <ACCDForm :show-footer="false">
                <div class="flex flex-col lg:flex-row md:flex-row gap-4">
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.phone')"
                        required
                        class="w-full lg:w-1/3"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t('module.debt.serviceTicket.form.phone')
                            "
                            size="md"
                            :disabled="true"
                            v-model="serviceTicketForm.customerPhone.value"
                            :options="serviceTicketForm.customerPhone.options"
                            :forCreate="true"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="
                            $t('module.debt.serviceTicket.form.customerType')
                        "
                        required
                        class="w-full lg:w-1/3"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t(
                                    'module.debt.serviceTicket.form.customerType'
                                )
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.customerTypeId.value"
                            :options="serviceTicketForm.customerTypeId.options"
                            size="md"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="
                            $t('module.debt.serviceTicket.form.customerName')
                        "
                        required
                        class="w-full lg:w-1/3"
                    >
                        <ACCDInputText
                            :disabled="true"
                            :placeholder="
                                $t(
                                    'module.debt.serviceTicket.form.customerName'
                                )
                            "
                            v-model="serviceTicketForm.customerName.value"
                            size="md"
                        ></ACCDInputText>
                    </ACCDFormItem>
                </div>
                <div class="flex flex-col lg:flex-row gap-4">
                    <ACCDFormItem
                        :label="
                            $t('module.debt.serviceTicket.form.licensePlate')
                        "
                        required
                        class="w-full lg:w-1/3"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t(
                                    'module.debt.serviceTicket.form.licensePlate'
                                )
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.licensePlate.value"
                            :options="serviceTicketForm.licensePlate.options"
                            size="md"
                            :forCreate="true"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.vinNumber')"
                        class="w-full lg:w-1/3"
                    >
                        <ACCDInputText
                            :placeholder="
                                $t('module.debt.serviceTicket.form.vinNumber')
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.vinNumber.value"
                            size="md"
                        ></ACCDInputText>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.journey')"
                        class="w-full lg:w-1/3"
                    >
                        <ACCDInputText
                            :placeholder="
                                $t('module.debt.serviceTicket.form.journey')
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.odo.value"
                            size="md"
                        ></ACCDInputText>
                    </ACCDFormItem>
                </div>
                <div class="flex flex-col lg:flex-row gap-4">
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.carBrand')"
                        required
                        class="w-full lg:w-1/4"
                    >
                        <ACCDSelect
                            :disabled="true"
                            :placeholder="
                                $t('module.debt.serviceTicket.form.carBrand')
                            "
                            v-model="serviceTicketForm.carBrandId.value"
                            :options="serviceTicketForm.carBrandId.options"
                            size="md"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.carModel')"
                        required
                        class="w-full lg:w-1/4"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t('module.debt.serviceTicket.form.carModel')
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.carModelId.value"
                            :options="serviceTicketForm.carModelId.options"
                            size="md"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.carYear')"
                        required
                        class="w-full lg:w-1/4"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t('module.debt.serviceTicket.form.carYear')
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.carYearId.value"
                            :options="serviceTicketForm.carYearId.options"
                            size="md"
                        ></ACCDSelect>
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="$t('module.debt.serviceTicket.form.carVersion')"
                        required
                        class="w-full lg:w-1/4"
                    >
                        <ACCDSelect
                            :placeholder="
                                $t('module.debt.serviceTicket.form.carVersion')
                            "
                            :disabled="true"
                            v-model="serviceTicketForm.carVersionId.value"
                            :options="serviceTicketForm.carVersionId.options"
                            size="md"
                        ></ACCDSelect>
                    </ACCDFormItem>
                </div>
                <div>
                    <ACCDFormItem
                        :label="
                            $t('module.debt.serviceTicket.form.requirement')
                        "
                    >
                        <ACCDTextArea
                            :disabled="true"
                            v-model="serviceTicketForm.description.value"
                            :placeholder="
                                $t('module.debt.serviceTicket.form.requirement')
                            "
                        >
                        </ACCDTextArea>
                    </ACCDFormItem>
                </div>
            </ACCDForm>
        </div>

        <div class="flex gap-4 flex-col">
            <div class="flex justify-end">
                <div class="w-full lg:w-1/4">
                    <span>
                        {{ $t("module.debt.serviceTicket.form.paymentStatus") }}
                    </span>
                    <ACCDSelect
                        :placeholder="
                            $t('module.debt.serviceTicket.form.paymentStatus')
                        "
                        :disabled="action != EFormState.EDIT"
                        v-model="serviceTicketForm.paymentStatus.value"
                        :options="serviceTicketForm.paymentStatus.options"
                    ></ACCDSelect>
                </div>
            </div>
            <div class="flex justify-between items-end">
                <span class="font-semibold text-xl">{{
                    $t("module.debt.serviceTicket.form.service.title")
                }}</span>
                <ACCDButton
                    type="primary"
                    size="md"
                    @click="onShowBill = true"
                    class="w-1/6"
                    v-if="action == EFormState.VIEW"
                >
                    {{ $t("module.debt.serviceTicket.print") }}
                </ACCDButton>
            </div>
            <ACCDTable :columns="serviceColumns" :rowData="serviceRowData">
                <template #cell-servieTypeId="{ row, col, field }">
                    <div class="w-full">
                        {{
                            row.garageServiceTypeId.options.filter(
                                (garageServiceType: any) =>
                                    garageServiceType.value ===
                                    row.garageServiceTypeId.value
                            )[0].label
                        }}
                    </div>
                </template>
                <template #cell-productId="{ row, col, field }">
                    <div class="w-full">
                        {{
                            row.garageServiceId.options.filter(
                                (garageService: any) =>
                                    garageService.value ===
                                    row.garageServiceId.value
                            )[0].label
                        }}
                    </div>
                </template>
                <template #cell-employeeId="{ row, col, field }">
                    <div class="w-full">
                        {{
                            row.employeeId.options.filter(
                                (employee: any) =>
                                    employee.value === row.employeeId.value
                            )[0].label
                        }}
                    </div>
                </template>
                <template #cell-unitPrice="{ row, col, field }">
                    <div
                        class="whitespace-normal w-28 bg-table-primary px-4 py-2.5 font-medium text-typo overflow-hidden= width-row"
                    >
                        {{
                            row.unitPrice
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </div>
                </template>
                <template #cell-money="{ row, col, field }">
                    <div class="w-28">
                        {{ formatPriceVN(row.money) }}
                    </div>
                </template>
                <template #cell-discount="{ row, col, field }">
                    <div class="w-full">
                        <span>
                            {{
                                row.discount
                                    ? `${formatPriceVN(row.discount) + "VNĐ"}`
                                    : "0,000 VNĐ"
                            }}</span
                        >
                    </div>
                </template>
                <template #cell-quantity="{ row, col, field }">
                    <div class="w-full">
                        {{ row.quantity ? row.quantity : "N/A" }}
                    </div>
                </template>
                <template #cell-tax="{ row, col, field }">
                    <div class="w-full">{{ row.tax }} %</div>
                </template>
                <template #cell-total="{ row, col, field }">
                    <div class="w-28">
                        {{ formatPriceVN(row.total) }}
                    </div>
                </template>
            </ACCDTable>
        </div>
        <div>
            <span class="font-semibold text-xl">{{
                $t("module.debt.serviceTicket.form.product.title")
            }}</span>
            <ACCDTable :columns="productColumns" :rowData="productRowData">
                <template #cell-productId="{ row, col, field }">
                    <div class="w-full">
                        {{
                            row.productId.options.filter(
                                (product: any) =>
                                    product.value === row.productId.value
                            )[0].label
                        }}
                    </div>
                </template>
                <template #cell-unit="{ row, col, field }">
                    <div class="w-16">
                        {{ row.unit }}
                    </div>
                </template>
                <template #cell-discount="{ row, col, field }">
                    <div class="w-full">
                        <span>
                            {{
                                row.discount
                                    ? `${formatPriceVN(row.discount) + "VNĐ"}`
                                    : "0,000 VNĐ"
                            }}</span
                        >
                    </div>
                </template>
                <template #cell-tax="{ row, col, field }">
                    <div class="w-full">{{ row.tax }} %</div>
                </template>
                <template #cell-total="{ row, col, field }">
                    <div class="w-36">
                        {{ formatPriceVN(row.total) }}
                    </div>
                </template>
                <template #cell-quantity="{ row, col, field }">
                    <div class="w-16">
                        {{ row.quantity ? row.quantity : "N/A" }}
                    </div>
                </template>
                <template #cell-unitPrice="{ row, col, field }">
                    <div
                        class="whitespace-normal w-36 bg-table-primary px-4 py-2.5 font-medium text-typo overflow-hidden= width-row"
                    >
                        {{
                            row.unitPrice
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </div>
                </template>
                <template #cell-money="{ row, col, field }">
                    <div class="w-36">
                        {{ formatPriceVN(row.money) }}
                    </div>
                </template>
            </ACCDTable>
        </div>
        <span class="italic text-red-500" v-if="invalid">
            {{ $t("module.employee.error.missingRequiredField") }}
        </span>
        <span v-if="errorMessage" class="italic text-red-500">
            {{ errorMessage }}
        </span>
        <div class="flex justify-end gap-20">
            <div class="flex flex-col items-end">
                <p class="font-semibold">
                    {{ $t("module.debt.serviceTicket.form.cash.totalMoney") }}
                </p>
                <p>
                    {{ $t("module.debt.serviceTicket.form.cash.intoMoney") }}
                </p>
                <p>
                    {{ $t("module.debt.serviceTicket.form.cash.totalTax") }}
                </p>
                <p>
                    {{
                        $t("module.debt.serviceTicket.form.cash.totalDiscount")
                    }}
                </p>
            </div>
            <div class="flex flex-col justify-end">
                <p class="font-semibold">
                    {{ formatPriceVN(Number(totalMoney)) }}
                </p>
                <p>{{ formatPriceVN(Number(intoMoney)) }}</p>
                <p>{{ formatPriceVN(Number(totalTax)) }}</p>
                <p>{{ formatPriceVN(Number(totalDiscount)) }}</p>
            </div>
        </div>
        <template #footer>
            <div class="flex justify-end gap-2.5 w-full">
                <ACCDButton
                    @click="onClickBack"
                    type="secondary"
                    size="md"
                    class="w-1/6"
                >
                    {{ $t("module.debt.serviceTicket.action.back") }}
                </ACCDButton>
                <ACCDButton
                    v-if="action != EFormState.VIEW"
                    @click="onSubmit"
                    type="primary"
                    size="md"
                    class="w-1/6"
                >
                    {{ $t("module.debt.serviceTicket.action.confirm") }}
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
    <RepairOrder
        v-if="onShowBill"
        :title="$t('module.debt.serviceTicket.moduleName')"
        :customerInfo="customerInfo"
        :carInfo="carInfo"
        :serviceInfo="computedServiceInfo"
        :productInfo="computedProductInfo"
        :description="serviceTicketForm.description.value"
    ></RepairOrder>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { emitter } from "@/utils/mitt";
import { EFormState } from "@/enums";
import { $toast } from "@/main";
import { ISelectOption } from "@/types";
import { getCars } from "@/modules/car/api";
import {
    getCarBrandList,
    getCarModelList,
    getCarVersionList,
    getCarYearList,
} from "@/modules/sharedModules/api";
import { detailPriceByCustomerTypeAndProductAndService } from "@/modules/productPrice/api";
import { getCustomerTypes } from "@/modules/customerType/api";
import {
    getListGarageServiceId,
    getListGarageServiceTypeId,
} from "@/modules/garageServiceController/api";
import { getListProducts } from "@/modules/product/api";
import { createOrder, getDetailOrder } from "@/modules/serviceTicket/api";
import { getGarageEmployees } from "@/modules/employee/api";
import { getCustomersByGarageId } from "@/modules/sellingManagement/api";
import useVuelidate from "@vuelidate/core";
import { helpers, required } from "@vuelidate/validators";
import RepairOrder from "@/modules/sharedModules/pages/formHTML/repairOrder.vue";
import {
    formatPriceVN,
    validatePrice,
} from "@/modules/sharedModules/component/constants";
import { updatePaymentStatusServiceTicket } from "@/modules/debt/api";
import { useI18n } from "@/composables/useI18n";
import { getParentProduct } from "@/modules/accessary/api";

const { $t } = useI18n();
type ModalProps = {
    modelValue: boolean;
    action: number;
    id?: string;
};

const baseServiceRowData = {
    garageServiceTypeId: {
        value: 0,
        options: [] as (ISelectOption & { origin: any })[],
    },
    garageServiceId: {
        value: 0,
        options: [] as (ISelectOption & { origin: any })[],
    },
    quantity: 0 as number,
    unitPrice: 0 as number,
    money: 0 as number,
    discount: 0,
    tax: 0,
    total: 0 as number | string,
    employeeId: {
        value: 0,
        options: [] as (ISelectOption & { origin: any })[],
    },
    instanceKey: 0,
};

const baseProductRowData = {
    productId: {
        value: 0,
        options: [] as (ISelectOption & { origin: any })[],
    },
    unit: "",
    quantity: 0,
    unitPrice: 0,
    price: 0,
    money: 0,
    discount: 0,
    tax: 0,
    total: 0,
    instanceKey: 0,
};

const props = withDefaults(defineProps<ModalProps>(), {
    modelValue: true,
    actions: EFormState.ADD,
    id: "",
});

const serviceColumns = ref([
    {
        key: "servieTypeId",
        headerName: $t("module.debt.serviceTicket.form.service.serviceId"),
        minWidth: "180px",
        maxWidth: "350px",
    },
    {
        key: "productId",
        headerName: $t("module.debt.serviceTicket.form.service.serviceTypeId"),
        minWidth: "250px",
        maxWidth: "350px",
    },
    {
        key: "quantity",
        headerName: $t("module.debt.serviceTicket.form.service.quantity"),
        minWidth: "50px",
    },
    {
        key: "unitPrice",
        headerName: $t("module.debt.serviceTicket.form.service.price"),
    },
    {
        key: "money",
        headerName: $t("module.debt.serviceTicket.form.service.money"),
    },
    {
        key: "discount",
        headerName: $t("module.debt.serviceTicket.form.service.discount"),
        minWidth: "150px",
    },
    {
        key: "tax",
        headerName: $t("module.debt.serviceTicket.form.service.tax"),
        minWidth: "100px",
    },
    {
        key: "total",
        headerName: $t("module.debt.serviceTicket.form.service.total"),
    },
    {
        key: "employeeId",
        headerName: $t("module.debt.serviceTicket.form.service.assignee"),
    },
    {
        key: "action",
        headerName: "",
    },
]);
const serviceRowData = ref([] as (typeof baseServiceRowData)[]);
const productColumns = ref([
    {
        key: "productId",
        headerName: $t("module.debt.serviceTicket.form.product.name"),
        minWidth: "250px",
        maxWidth: "350px",
    },
    {
        key: "unit",
        headerName: $t("module.debt.serviceTicket.form.product.unit"),
    },
    {
        key: "quantity",
        headerName: $t("module.debt.serviceTicket.form.product.quantity"),
    },
    {
        key: "unitPrice",
        headerName: $t("module.debt.serviceTicket.form.product.price"),
    },
    {
        key: "money",
        headerName: $t("module.debt.serviceTicket.form.product.money"),
    },
    {
        key: "discount",
        headerName: $t("module.debt.serviceTicket.form.product.discount"),
        minWidth: "150px",
    },
    {
        key: "tax",
        headerName: $t("module.debt.serviceTicket.form.product.tax"),
        minWidth: "100px",
    },
    {
        key: "total",
        headerName: $t("module.debt.serviceTicket.form.product.total"),
    },
    {
        key: "action",
        headerName: "",
    },
]);
const productRowData = ref([] as (typeof baseProductRowData)[]);

const serviceTicketForm = ref({
    paymentStatus: {
        value: 1,
        options: [
            {
                value: 1,
                label: $t("module.debt.serviceTicket.paymentStatus.1"),
            },
            {
                value: 2,
                label: $t("module.debt.serviceTicket.paymentStatus.2"),
            },
            {
                value: 3,
                label: $t("module.debt.serviceTicket.paymentStatus.3"),
            },
            {
                value: 4,
                label: $t("module.debt.serviceTicket.paymentStatus.4"),
            },
            {
                value: 5,
                label: $t("module.debt.serviceTicket.paymentStatus.5"),
            },
        ],
    },
    customerPhone: {
        value: "",
        options: [] as (ISelectOption & { origin: any })[],
        disabled: false,
    },
    customerTypeId: {
        value: "",
        options: [] as ISelectOption[],
        disabled: false,
    },
    customerName: {
        value: "",
        disabled: false,
    },

    licensePlate: {
        value: "",
        options: [] as (ISelectOption & { origin: any })[],
        disabled: false,
    },
    vinNumber: {
        value: "",
        disabled: false,
    },
    odo: {
        value: "",
        disabled: false,
    },
    carBrandId: {
        value: "",
        options: [] as ISelectOption[],
        disabled: false,
    },
    carModelId: {
        value: "",
        options: [] as ISelectOption[],
        disabled: false,
    },
    carYearId: {
        value: "",
        options: [] as ISelectOption[],
        disabled: false,
    },
    carVersionId: {
        value: "",
        options: [] as ISelectOption[],
        disabled: false,
    },
    description: {
        value: "",
        disabled: false,
    },
    //hidden field
    discount: {
        value: null,
    },
    totalPrice: {
        value: null,
    },
    carId: {
        value: "" as string | null,
    },
    customerId: {
        value: 0 as number | null,
    },
});
const listServiceOptions = ref([] as (ISelectOption & { origin: any })[]);
const listServiceTypeIdOptions = ref([] as (ISelectOption & { origin: any })[]);
const listGarageEmployees = ref([] as (ISelectOption & { origin: any })[]);
const listGarageProducts = ref([] as (ISelectOption & { origin: any })[]);
const errorMessage = ref("");
const invalid = ref(false);
const onShowBill = ref(false);
const allCarOfGarageOptions = ref([]);
watch(
    () => serviceTicketForm.value.carBrandId.value,
    (newVal: string) => {
        serviceTicketForm.value.carModelId.options = [];
        getCarModelList(newVal).then((res) => {
            serviceTicketForm.value.carModelId.options = res.data.data.map(
                (item: any) => {
                    return {
                        value: item.id,
                        label: item.title,
                    };
                }
            );
        });
    }
);
watch(
    () => serviceTicketForm.value.carModelId.value,
    (newVal: string) => {
        serviceTicketForm.value.carYearId.options = [];
        getCarYearList(newVal).then((res) => {
            serviceTicketForm.value.carYearId.options = res.data.data.map(
                (item: any) => {
                    return {
                        value: item.id,
                        label: item.title,
                    };
                }
            );
        });
    }
);
watch(
    () => serviceTicketForm.value.carYearId.value,
    (newVal: string) => {
        serviceTicketForm.value.carVersionId.options = [];
        getCarVersionList(newVal).then((res) => {
            serviceTicketForm.value.carVersionId.options = res.data.data.map(
                (item: any) => {
                    return {
                        value: item.id,
                        label: item.title,
                    };
                }
            );
        });
    }
);
// thay đổi phoneNumber, thay đổi customerType, customerId, name, listCars
watch(
    () => serviceTicketForm.value.customerPhone.value,
    async (val) => {
        let originOptions = serviceTicketForm.value.customerPhone.options.find(
            (a) => {
                if (a.value == val) {
                    return true;
                }
            }
        );
        serviceTicketForm.value.licensePlate.value = "";
        serviceTicketForm.value.customerId.value = null;
        serviceTicketForm.value.customerTypeId.value = "";
        serviceTicketForm.value.customerName.value = "";
        if (originOptions) {
            let origin = originOptions.origin;
            serviceTicketForm.value.customerTypeId.value =
                origin.customerTypeId;
            serviceTicketForm.value.customerName.value = origin.fullName;
            serviceTicketForm.value.customerId.value = origin.id;
            serviceTicketForm.value.customerTypeId.disabled = true;
            serviceTicketForm.value.customerName.disabled = true;
            serviceTicketForm.value.licensePlate.options =
                allCarOfGarageOptions.value.filter((car: any) => {
                    return (
                        car.origin.customerId ==
                        serviceTicketForm.value.customerId.value
                    );
                });
        } else {
            serviceTicketForm.value.customerTypeId.disabled = false;
            serviceTicketForm.value.customerName.disabled = false;
            serviceTicketForm.value.licensePlate.options = [];
            serviceTicketForm.value.carId.value = null;
        }
    }
);
watch(
    () => serviceTicketForm.value.licensePlate.value,
    (val) => {
        let originOptions = serviceTicketForm.value.licensePlate.options.find(
            (a) => {
                return a.value == val;
            }
        );
        if (originOptions) {
            serviceTicketForm.value.vinNumber.value =
                originOptions.origin.vinNumber;
            serviceTicketForm.value.carBrandId.value =
                originOptions.origin.carBrandId;
            serviceTicketForm.value.carModelId.value =
                originOptions.origin.carModelId;
            serviceTicketForm.value.carYearId.value =
                originOptions.origin.carYearId;
            serviceTicketForm.value.carVersionId.value =
                originOptions.origin.carVersionId;
            serviceTicketForm.value.carId.value = originOptions.origin.id;
            //disable các field được fill
            serviceTicketForm.value.vinNumber.disabled = true;
            serviceTicketForm.value.carBrandId.disabled = true;
            serviceTicketForm.value.carModelId.disabled = true;
            serviceTicketForm.value.carYearId.disabled = true;
            serviceTicketForm.value.carVersionId.disabled = true;
        } else {
            serviceTicketForm.value.vinNumber.disabled = false;
            serviceTicketForm.value.odo.disabled = false;
            serviceTicketForm.value.carBrandId.disabled = false;
            serviceTicketForm.value.carModelId.disabled = false;
            serviceTicketForm.value.carYearId.disabled = false;
            serviceTicketForm.value.carVersionId.disabled = false;
        }
    }
);
const instanceKey = new Date().getTime();
onMounted(async () => {
    emitter.on("pages-layout-on-confirmClose", (ik) => {
        if (ik == instanceKey) {
            lazyValue.value = false;
        }
    });
    const garageId = Number(localStorage.getItem("garageId"));

    await getCarBrandList().then((res) => {
        serviceTicketForm.value.carBrandId.options = res.data.data.map(
            (item: any) => {
                return {
                    value: item.id,
                    label: item.title,
                };
            }
        );
    });
    await getCarByUser({}).then((res) => {
        if (res.code == 1) {
            allCarOfGarageOptions.value = res.data.map((c: any) => {
                return {
                    value: c.licensePlate,
                    label: c.licensePlate,
                    origin: c,
                };
            });
        }
    });
    await getCustomerTypes({
        pageSize: 10000,
        pageNumber: 1,
    }).then((res) => {
        serviceTicketForm.value.customerTypeId.options = res.data.map(
            (a: any) => {
                return {
                    value: a.id,
                    label: a.customerTypeName,
                };
            }
        );
    });
    await getCustomersByGarageId({
        pageSize: 10000,
        pageNumber: 1,
    }).then((res) => {
        serviceTicketForm.value.customerPhone.options = res.data.map(
            (a: any) => {
                return {
                    value: a.phoneNumber,
                    label: a.phoneNumber,
                    origin: a,
                };
            }
        );
    });
    await getListGarageServiceTypeId().then((res) => {
        listServiceTypeIdOptions.value = res.data.map((s: any) => {
            return {
                value: s.id,
                label: s.name,
                origin: s,
            } as ISelectOption & { origin: any };
        });
    });
    await getListGarageServiceId({ pageSize: 10000, pageNumber: 1 }).then(
        (res: any) => {
            listServiceOptions.value = res.data.map((s: any) => {
                return {
                    value: s.id,
                    label: s.code + " - " + s.name,
                    origin: s,
                } as ISelectOption & { origin: any };
            });
        }
    );
    await getGarageEmployees({
        pageSize: 10000,
        pageNumber: 1,
    }).then((res) => {
        listGarageEmployees.value = res.data.map((e: any) => {
            return {
                value: e.id,
                label: e.fullName,
            };
        });
    });
    await getParentProduct({
        pageSize: 10000,
        pageNumber: 1,
        type: 1,
    }).then((res) => {
        listGarageProducts.value = res.data.map((e: any) => {
            return {
                value: e.id,
                label: e.name,
                origin: e,
            };
        });
    });
    if (props.action == EFormState.EDIT || props.action == EFormState.VIEW)
        onGetDetailOrder();
});

const emit = defineEmits<{
    (e: "update:modelValue", value: boolean): void;
    (e: "refresh"): void;
}>();

const customerInfo = computed(() => {
    return {
        customerName: serviceTicketForm.value.customerName.value,
        customerPhone: serviceTicketForm.value.customerPhone.value,
    };
});

const carInfo = computed(() => {
    let carBrand = serviceTicketForm.value.carBrandId.options.find((c) => {
        return c.value == serviceTicketForm.value.carBrandId.value;
    });
    let carModel = serviceTicketForm.value.carModelId.options.find((c) => {
        return c.value == serviceTicketForm.value.carModelId.value;
    });
    let carYear = serviceTicketForm.value.carYearId.options.find((c) => {
        return c.value == serviceTicketForm.value.carYearId.value;
    });
    let carVersion = serviceTicketForm.value.carVersionId.options.find((c) => {
        return c.value == serviceTicketForm.value.carVersionId.value;
    });
    return {
        licensePlate: serviceTicketForm.value.licensePlate.value,
        vinNumber: serviceTicketForm.value.vinNumber.value,
        odo: serviceTicketForm.value.odo.value,
        carBrand: carBrand?.label,
        carModel: carModel?.label,
        carYear: carYear?.label,
        carVersion: carVersion?.label,
    };
});

const computedServiceInfo = computed(() => {
    let services = serviceRowData.value.map((s) => {
        let service = s.garageServiceId.options.find((p) => {
            return p.value == s.garageServiceId.value;
        });
        let employee = s.employeeId.options.find((p) => {
            return p.value == s.employeeId.value;
        });
        let result = {} as any;
        Object.keys(s).map((k) => {
            if (typeof s == "object" && "value" in s) {
                result[k] = (s as any)[k as keyof typeof s].value;
            } else {
                result[k] = (s as any)[k as keyof typeof s];
            }
        });
        return {
            ...result,
            serviceName: service?.label,
            employeeName: employee?.label,
        };
    });
    return services;
});

const computedProductInfo = computed(() => {
    let products = productRowData.value.map((s) => {
        let product = s.productId.options.find((p) => {
            return p.value == s.productId.value;
        });

        let result = {} as any;
        Object.keys(s).map((k) => {
            if (typeof s == "object" && "value" in s) {
                result[k] = (s as any)[k as keyof typeof s].value;
            } else {
                result[k] = (s as any)[k as keyof typeof s];
            }
        });
        return {
            ...result,
            money: result.money.toFixed(0),
            productName: product?.label,
        };
    });
    return products;
});

const lazyValue = computed({
    get() {
        return props.modelValue;
    },
    set(val: boolean) {
        emit("update:modelValue", val);
    },
});
const getCarByUser = (params: any) => {
    return getCars({ ...params, pageSize: 10000, pageNumber: 1 });
};

const onGetDetailOrder = () => {
    let id = props.id;
    getDetailOrder(id).then((res: any) => {
        let data = res.data;
        serviceTicketForm.value.customerPhone.value =
            data.customerResponse.phoneNumber;
        setTimeout(() => {
            serviceTicketForm.value.licensePlate.value =
                data.carResponse.licensePlate;
        }, 300);
        serviceTicketForm.value.odo.value = data.odo;
        serviceTicketForm.value.paymentStatus.value = data.paymentStatus;
        serviceTicketForm.value.description.value = data.description;
        serviceRowData.value = data.quotation.quotationLabours.map(
            (a: any, index: number) => {
                let employeeId = 0;
                listGarageEmployees.value.some((e) => {
                    if (a.employeeName == e.label) {
                        employeeId = e.value;
                        return true;
                    }
                });
                return {
                    garageServiceTypeId: {
                        value: a.garageServiceTypeId,
                        options: listServiceTypeIdOptions.value,
                    },
                    garageServiceId: {
                        value: a.garageServiceTypeId,
                        options: listServiceOptions.value,
                    },
                    quantity: a.quantity,
                    unitPrice: a.unitPrice,
                    money: Number(a.quantity) * Number(a.unitPrice),
                    tax: a.tax * 100,
                    total:
                        (Number(a.quantity) * Number(a.unitPrice) -
                            Number(a.discount)) *
                        (1 + a.tax),
                    discount: a.discount,
                    employeeId: {
                        value: employeeId,
                        options: listGarageEmployees.value,
                    },
                    instanceKey: index,
                };
            }
        );

        productRowData.value = data.quotation.quotationSpareParts.map(
            (a: any, index: number) => {
                return {
                    productId: {
                        value: a.productId,
                        options: listGarageProducts.value,
                    },
                    unit: a.unit,
                    quantity: a.quantity,
                    unitPrice: a.unitPrice,
                    price: a.price,
                    money: (Number(a.quantity) * Number(a.unitPrice)).toFixed(
                        0
                    ),
                    total:
                        (Number(a.quantity) * Number(a.unitPrice) -
                            Number(a.discount)) *
                        (1 + a.tax),
                    tax: a.tax * 100,
                    discount: a.discount,
                    instanceKey: index,
                };
            }
        );
    });
};

const onSelectService = (row: typeof baseServiceRowData) => {
    let sRow = serviceRowData.value.find((r) => {
        return r.instanceKey == row.instanceKey;
    });
    if (sRow) {
        let service = sRow.garageServiceId.options.find((o) => {
            return o.value == row.garageServiceId.value;
        })?.origin;
        row.quantity = 1;
        row.unitPrice = service.purchasePrice;
        detailPriceByCustomerTypeAndProductAndService(
            serviceTicketForm.value.customerTypeId.value,
            service.id,
            2
        ).then((res) => {
            row.money = res.data.price;
            row.total = res.data.price;
        });

        row.discount = 0;
    }
};
const onChangeRowService = (
    row: typeof baseServiceRowData,
    field: keyof typeof baseServiceRowData
) => {
    let sRow = serviceRowData.value.find((r) => {
        return r.instanceKey == row.instanceKey;
    });
    if (sRow) {
        let val = Number(sRow[field]);
        sRow.money = sRow.unitPrice * sRow.quantity;
        sRow.total = Number(
            (sRow.unitPrice * sRow.quantity - sRow.discount) *
                (1 + sRow.tax / 100)
        ).toFixed(0);
    }
};
const onSelectProduct = (row: typeof baseProductRowData) => {
    let sRow = productRowData.value.find((r) => {
        return r.instanceKey == row.instanceKey;
    });
    if (sRow) {
        let product = sRow.productId.options.find((o) => {
            return o.value == row.productId.value;
        })?.origin;

        row.quantity = 1;
        detailPriceByCustomerTypeAndProductAndService(
            serviceTicketForm.value.customerTypeId.value,
            product.id,
            1
        ).then((res) => {
            row.unitPrice = res.data.price;
            row.total = res.data.price;
            row.money = res.data.price;
            row.total = res.data.price;
        });

        row.unit = product.unit;

        row.discount = 0;
    }
};
const onChangeRowProduct = (
    row: typeof baseProductRowData,
    field: keyof typeof baseProductRowData
) => {
    let sRow = productRowData.value.find((r) => {
        return r.instanceKey == row.instanceKey;
    });
    if (sRow) {
        let val = Number(sRow[field]);
        sRow.money = sRow.unitPrice * sRow.quantity;
        sRow.total =
            (sRow.unitPrice * sRow.quantity - row.discount) *
            (1 + sRow.tax / 100);
    }
};
const validations = computed(() => {
    return {
        item: {
            customerPhone: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            customerTypeId: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            customerName: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            licensePlate: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            carBrandId: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            carModelId: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            carYearId: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            carVersionId: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
        },
    };
});
const item = computed(() => {
    let item = {} as any;
    Object.keys(serviceTicketForm.value).forEach((k) => {
        item[k] =
            serviceTicketForm.value[
                k as keyof typeof serviceTicketForm.value
            ].value;
    });
    return item;
});
const v$ = useVuelidate(validations, { item });
const computedTitle = computed(() => {
    if (props.action == EFormState.EDIT) {
        return $t("module.debt.serviceTicket.form.edit");
    }
    return $t("module.debt.serviceTicket.form.detail");
});
const totalMoney = computed(() => {
    let result = 0;
    serviceRowData.value.forEach((r) => {
        result += Number(r.total);
    });
    productRowData.value.forEach((r) => {
        result += Number(r.total);
    });
    return Number(result).toFixed(0);
});

const intoMoney = computed(() => {
    let result = 0;
    serviceRowData.value.forEach((r) => {
        result += Number(r.money);
    });
    productRowData.value.forEach((r) => {
        result += Number(r.money);
    });
    return Number(result).toFixed(0);
});

const totalTax = computed(() => {
    let result = 0;
    serviceRowData.value.forEach((r) => {
        result += (r.tax / 100) * (r.money - r.discount);
    });
    productRowData.value.forEach((r) => {
        result += (r.tax / 100) * (r.money - r.discount);
    });
    return Number(result).toFixed(0);
});

const totalDiscount = computed(() => {
    let result = 0;
    serviceRowData.value.forEach((r) => {
        result += Number(r.discount);
    });
    productRowData.value.forEach((r) => {
        result += Number(r.discount);
    });
    return Number(result).toFixed(0);
});

const onClickBack = () => {
    if (props.action != EFormState.VIEW) {
        emitter.emit("layout-pages-open-confirmClose", instanceKey);
    } else {
        lazyValue.value = false;
    }
};

const onSubmit = async () => {
    const result = await v$.value.$validate();
    if (!result) {
        invalid.value = true;
        return;
    }
    let data = {} as any;
    Object.keys(serviceTicketForm.value).forEach((key: string) => {
        data[key] =
            serviceTicketForm.value[
                key as keyof typeof serviceTicketForm.value
            ].value;
    });
    data.quotationLabours = serviceRowData.value
        .filter((r) => {
            return r.garageServiceId.value != 0;
        })
        .map((r) => {
            return {
                ...r,
                tax: r.tax / 100,
                employeeId: r.employeeId.value,
                productId: r.garageServiceId.value,
            };
        });
    data.quotationSpareParts = productRowData.value
        .filter((r) => {
            return r.productId.value;
        })
        .map((r) => {
            return { ...r, tax: r.tax / 100, productId: r.productId.value };
        });
    if (props.action == EFormState.ADD) {
        let res = await createOrder(data);
        if (res.code == 1) {
            $toast(
                $t("module.debt.serviceTicket.form.toast.createSuccess"),
                true
            );
            lazyValue.value = false;
            emit("refresh");
        } else {
            errorMessage.value = "* " + res.message;
        }
    } else {
        let res = await updatePaymentStatusServiceTicket(
            props.id,
            serviceTicketForm.value.paymentStatus.value
        );
        if (res.code == 1) {
            $toast(
                $t("module.debt.serviceTicket.form.toast.updateSuccess"),
                true
            );
            lazyValue.value = false;
            emit("refresh");
        } else {
            errorMessage.value = "* " + res.message;
            $toast(
                $t("module.debt.serviceTicket.form.toast.updateFailse"),
                false
            );
        }
    }
};
</script>

<style lang="scss">
.width-row {
    min-width: 100px;
    max-width: 200px;
}
</style>
