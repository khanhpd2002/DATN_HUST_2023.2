<template>
    <ACCDModal
        v-model="lazyValue"
        :title="computedTitle"
        class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
    >
        <div>
            <ACCDForm :show-footer="false" class="w-full">
                <div class="flex flex-col lg:flex-row">
                    <ACCDFormItem
                        :label="
                            $t(
                                'module.debt.orderDistributorController.form.distributorId'
                            )
                        "
                        required
                        class="w-full lg:w-1/4 mr-4 font-semibold"
                    >
                        <ACCDInputText
                            size="md"
                            :placeholder="
                                $t(
                                    'module.debt.orderDistributorController.form.distributorId'
                                )
                            "
                            v-model="distributorInfo.distributorCode"
                            :disabled="true"
                            class="font-medium"
                        />
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="
                            $t(
                                'module.debt.orderDistributorController.form.distributorName'
                            )
                        "
                        required
                        class="w-full lg:w-1/4 mr-4 font-semibold"
                    >
                        <ACCDInputText
                            size="md"
                            :placeholder="
                                $t(
                                    'module.debt.orderDistributorController.form.distributorName'
                                )
                            "
                            v-model="distributorInfo.distributorName"
                            :disabled="true"
                             class="font-medium"
                        />
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="
                            $t(
                                'module.debt.orderDistributorController.form.phoneNumber'
                            )
                        "
                        required
                        class="w-full lg:w-1/4 mr-4 font-semibold"
                    >
                        <ACCDInputText
                            size="md"
                            :placeholder="
                                $t(
                                    'module.debt.orderDistributorController.form.phoneNumber'
                                )
                            "
                            v-model="distributorInfo.distributorContact"
                            :disabled="true"
                             class="font-medium"
                        />
                    </ACCDFormItem>
                    <ACCDFormItem
                        :label="
                            $t(
                                'module.debt.orderDistributorController.form.deliveryType'
                            )
                        "
                        required
                        class="w-full lg:w-1/4 font-semibold"
                    >
                        <ACCDInputText
                            size="md"
                            :placeholder="
                                $t(
                                    'module.debt.orderDistributorController.form.deliveryType'
                                )
                            "
                            v-model="originData.deliveryType"
                            :disabled="true"
                             class="font-medium"
                        />
                    </ACCDFormItem>
                </div>
                <div class="flex justify-end font-semibold">
                    <ACCDFormItem
                        :label="$t('module.debt.order.modal.paymentStatus')"
                        required
                        class="w-full lg:w-1/4"
                    >
                        <ACCDSelect
                            size="md"
                            :placeholder="
                                $t('module.debt.order.modal.paymentStatus')
                            "
                            v-model="originData.paymentStatus"
                            :options="paymentStatusOptions"
                            :disabled="props.state == EFormState.VIEW"
                             class="font-medium"
                        />
                    </ACCDFormItem>
                </div>
            </ACCDForm>
        </div>

        <div>
            <div class="flex justify-between">
                <h1 class="text-gray-900 font-semibold text-lg pb-1 mb-4">
                    {{ $t("module.sellSparePart.form.products.title") }}
                </h1>
                <ACCDButton
                    type="primary"
                    class="w-full lg:w-1/5"
                    size="md"
                    v-if="props.state == EFormState.VIEW"
                >
                    {{ $t("module.inventory.order.form.printBill") }}
                </ACCDButton>
            </div>
            <ACCDTable
                ref="table"
                :columns="columnData"
                :rowData="originData.products"
            >
                <template #cell-id="{ row, col, field }">
                    <td class="w-52 cd-table__td px-4">
                        {{
                            productOptions.find((a) => a.value == row.productId)
                                ?.label
                        }}
                    </td>
                </template>
                <template #cell-unit="{ row, col, field }">
                    <td class="cd-table__td px-4">
                        <ACCDInputText
                            :readonly="true"
                            size="md"
                            :placeholder="
                                $t('module.sellSparePart.form.products.unit')
                            "
                            v-model="row.unit"
                        
                        />
                    </td>
                </template>
                <template #cell-quantity="{ row, col, field }">
                    <td class="cd-table__td px-4">
                        <ACCDInputText
                            class="w-full"
                            size="md"
                            :placeholder="
                                $t(
                                    'module.sellSparePart.form.products.quantity'
                                )
                            "
                            :readonly="true"
                            v-model="row.quantity"
                      
                            @keypress="validatePrice"
                        />
                    </td>
                </template>
                <template #cell-unitPrice="{ row, col, field }">
                    <td class="w-36 cd-table__td px-4">
                        {{
                            row.unitPrice
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </td>
                </template>
                <template #cell-originalPrice="{ row, col, field }">
                    <td class="w-36 cd-table__td px-4">
                        {{
                            Math.ceil(row.unitPrice * row.quantity)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </td>
                </template>
                <template #cell-discount="{ row, col, field }">
                    <td class="w-36 cd-table__td px-4">
                        {{
                            row.discount
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </td>
                </template>
                <template #cell-tax="{ row, col, field }">
                    <td class="w-16 cd-table__td px-4">
                        {{ Number(row.tax) }}%
                    </td>
                </template>
                <template #cell-price="{ row, col, field }">
                    <td class="w-36 cd-table__td px-4">
                        {{ formatPriceVN(row.price) }}
                    </td>
                </template>
            </ACCDTable>

            <br />
            <span class="italic text-red-500" v-if="invalid">{{
                $t("module.sellSparePart.error.missingRequiredField")
            }}</span>
            <span class="italic text-red-500" v-if="responseErrorMessages">{{
                responseErrorMessages
            }}</span>
        </div>
        <div class="flex justify-end gap-20">
            <div class="flex flex-col items-end">
                <p class="font-semibold">
                    {{ $t("module.sellSparePart.form.totalPrice") }}
                </p>
                <p>{{ $t("module.sellSparePart.form.originalPrice") }}</p>
                <p>{{ $t("module.sellSparePart.form.tax") }}</p>
                <p>{{ $t("module.sellSparePart.form.discount") }}</p>
            </div>
            <div class="flex flex-col justify-end">
                <p class="font-semibold">{{ formatPriceVN(totalPrice) }}</p>
                <p>{{ formatPriceVN(originalPrice) }}</p>
                <p>{{ formatPriceVN(tax) }}</p>
                <p>{{ formatPriceVN(discount) }}</p>
            </div>
        </div>
        <template #footer>
            <div class="flex justify-end gap-2.5 w-full">
                <ACCDButton
                    variant="fill"
                    type="secondary"
                    class="w-1/6"
                    @click="closeDialog"
                >
                    <span class="text-info-base font-medium">{{
                        $t("module.customerType.action.back")
                    }}</span>
                </ACCDButton>
                <ACCDButton
                    v-if="props.state !== EFormState.VIEW"
                    type="primary"
                    variant="fill"
                    class="bg-info-secondary border-none w-1/6"
                    @click="onSubmit"
                >
                    <span class="text-white font-medium">{{
                        $t("module.customerType.action.save")
                    }}</span>
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
    <OrderDistributorForm
        v-if="onShowBill"
        @close="onShowBill = false"
        :productInfo="originData.products"
        :distributorName="distributorInfo.distributorName"
        :distributorContactPhone="distributorInfo.contactPhone"
    >
    </OrderDistributorForm>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import OrderDistributorForm from "@/modules/sharedModules/pages/formHTML/OrderDistributorForm.vue";
import { EFormState } from "@/enums";
import type { IDistributor, ISelectOption } from "@/types";
import { useI18n } from "@/composables/useI18n";
import { useToast } from "@/composables/useToast";
import {
    getDetailOrderDistributorController,
    updatePaymentStatusDistributorController,
} from "@/modules/debt/api";
import { getListDistributors } from "@/modules/distributor/api";
import { getParentProduct } from "@/modules/accessary/api";
import { emitter } from "@/utils/mitt";
import {
    formatPriceVN,
    validatePrice,
} from "@/modules/sharedModules/component/constants";
const { $t } = useI18n();
const { $toast } = useToast();
const MAX_INT = import.meta.env.VITE_MAX_INTEGER;

const item = ref<SellSparePart>({
    id: 0,
    sellCode: "",
    customerId: undefined,
    customerName: "",
    customerPhone: "",
    customerTypeId: undefined,
    createdAt: new Date(),
    deliveryStatus: undefined,
    paymentStatus: undefined,
    totalPrice: 0,
    originalPrice: 0,
    tax: 0,
    discount: 0,
    products: [],
});
const paymentStatusOptions = ref([
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
]);

const products = ref<SellSparePartProduct[]>([]);
const count = ref(0);
const totalPrice = ref(0);
const originalPrice = ref(0);
const tax = ref(0);
const discount = ref<number>(0);
const responseErrorMessages = ref("");
const id = ref<number | null | string>(null);
const onShowBill = ref(false);

const customerTypeOptions = ref([]);
const deliveryStatusOptions = ref<ISelectOption[]>([]);
const productOptions = ref<ISelectOption[]>([]);
const invalid = ref(false);

const originData = ref({
    id: 0,
    orderCode: "string",
    distributorId: 0,
    deliveryType: "string",
    phoneNumber: "",
    distributorName: "",
    deliveryStatus: 0,
    paymentStatus: 0,
    discount: 0,
    tax: 0,
    totalPrice: 0,
    garageId: 0,
    inventoryId: 0,
    products: [] as SellSparePartProduct[],
});

type ModalProps = {
    modelValue: boolean;
    state: EFormState | undefined;
    sellSparePartId?: number | string;
};

const distributorInfo = computed(() => {
    let distributorId = originData.value.distributorId;
    let distributor = listDistributors.value.find((a: any) => {
        return a.id == distributorId;
    });
    return distributor ? distributor : ({} as any);
});

const lazyValue = computed({
    get() {
        return props.modelValue;
    },
    set(val: boolean) {
        emit("update:modelValue", val);
    },
});

const emit = defineEmits<{
    (e: "update:modelValue", value: boolean): void;
    (e: "refresh"): void;
}>();

const props = withDefaults(defineProps<ModalProps>(), {
    modelValue: true,
    state: EFormState.ADD,
});

const columnData = [
    {
        key: "id",
        headerName: $t("module.sellSparePart.form.products.sparePart"),
    },
    {
        key: "unit",
        headerName: $t("module.sellSparePart.form.products.unit"),
    },
    {
        key: "quantity",
        headerName: $t("module.sellSparePart.form.products.quantity"),
    },
    {
        key: "unitPrice",
        headerName: $t("module.sellSparePart.form.products.unitPrice"),
    },
    {
        key: "originalPrice",
        headerName: $t("module.sellSparePart.form.products.originalPrice"),
    },
    {
        key: "discount",
        headerName: $t("module.sellSparePart.form.products.discount"),
    },
    {
        key: "tax",
        headerName: $t("module.sellSparePart.form.products.tax"),
    },
    {
        key: "price",
        headerName: $t("module.sellSparePart.form.products.price"),
    },
];

const computedTitle = computed(() => {
    switch (props.state) {
        case EFormState.ADD:
            return $t("module.sellSparePart.form.addTitle");
        case EFormState.EDIT:
            return $t("module.debt.orderDistributorController.form.editTitle");
        case EFormState.VIEW:
            return $t("module.debt.orderDistributorController.form.viewTitle");
        default:
            return "";
    }
});
const instanceKey = new Date().getTime();
const listDistributors = ref([]);
onMounted(async () => {
    if (props.sellSparePartId) {
        id.value = props.sellSparePartId;
        const result = await getDetailOrderDistributorController(
            Number(props.sellSparePartId)
        );
        originData.value = result.data;
        totalPrice.value = originData.value.totalPrice as number;
        let computedOriginalPrice = 0;
        let computedTax = 0;
        let computedDiscount = 0;
        originData.value.products.forEach((p) => {
            p.tax = p.tax * 100;
            computedOriginalPrice += p.quantity * p.unitPrice;
            computedDiscount += p.discount;
            computedTax += (p.quantity * p.unitPrice - p.discount) * p.tax;
        });
        discount.value = computedDiscount;
        tax.value = computedTax / 100;
        originalPrice.value = computedOriginalPrice;
    }
    emitter.on("pages-layout-on-confirmClose", (ik) => {
        if (ik == instanceKey) {
            lazyValue.value = false;
        }
    });
    let productList = await getParentProduct({
        pageSize: MAX_INT,
        pageNumber: 1,
    });
    productOptions.value = productList.data.map((a: any) => {
        return {
            label: `${a.code} - ${a.name}`,
            value: a.id,
        };
    });
    getListDistributors({
        pageSize: 100000,
        pageNumber: 1,
    }).then((res) => {
        listDistributors.value = res.data.map((a: IDistributor) => {
            return {
                id: a.id,
                distributorName: a.name,
                distributorContact: a.contactPhone,
                distributorCode: a.code,
            };
        });
    });
    for (let i = 1; i < 6; ++i) {
        deliveryStatusOptions.value.push({
            label: $t(`module.sellSparePart.deliveryStatus.${i}`),
            value: i,
            rawValue: null,
        });
    }
});
const closeDialog = () => {
    emitter.emit("layout-pages-open-confirmClose", instanceKey);
    invalid.value = false;
};
const resetForm = () => {
    responseErrorMessages.value = "";
};

const onSubmit = async () => {
    item.value.products.map((product) => {
        product.tax = product.tax / 100;
    });
    let data = originData.value;
    const response = await updatePaymentStatusDistributorController(
        id.value as string,
        originData.value.paymentStatus
    );
    const successMessage = $t("module.sellSparePart.toast.editSuccess");
    if (response.code === 1) {
        $toast(successMessage, true);
        lazyValue.value = false;
        emit("refresh");
        resetForm();
    } else {
        responseErrorMessages.value = "* " + response.message;
    }
};
</script>

<style scoped></style>
