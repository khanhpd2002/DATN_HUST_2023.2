<template>
    <ACCDModal
        v-model="lazyValue"
        :title="computedTitle"
        class-width="lg:w-[1000px] md:w-[730px] sm:w-[500px]"
    >
        <div class="flex flex-col gap-4 py-4">
            <WrapFlexContainer>
                <div class="w-full lg:w-1/4">
                    <p class="mb-1">
                        {{ $t("module.inventory.order.form.distributorCode") }}
                    </p>
                    <ACCDSelect
                        :placeholder="
                            $t('module.inventory.order.form.distributorCode')
                        "
                        :disabled="stateView == EFormState.VIEW"
                        :options="listDistributor"
                        v-model="data.distributorId"
                    >
                    </ACCDSelect>
                </div>
                <div class="w-full lg:w-1/4">
                    <p class="mb-1">
                        {{ $t("module.inventory.order.form.distributorName") }}
                    </p>
                    <ACCDSelect
                        :disabled="stateView == EFormState.VIEW"
                        :options="listDistributorName"
                        :placeholder="
                            $t('module.inventory.order.form.distributorName')
                        "
                        v-model="data.distributorId"
                    >
                    </ACCDSelect>
                </div>
                <div class="w-full lg:w-1/4">
                    <p class="mb-1">
                        {{ $t("module.inventory.order.form.contactPhone") }}
                    </p>
                    <ACCDInputText
                        :disabled="stateView == EFormState.VIEW"
                        :readonly="true"
                        :placeholder="
                            $t('module.inventory.order.form.contactPhone')
                        "
                        v-model="distributorInfo.contactPhone"
                    >
                    </ACCDInputText>
                </div>
                <div class="w-full lg:w-1/4">
                    <p class="mb-1">
                        {{ $t("module.inventory.order.form.deliveryType") }}
                    </p>
                    <ACCDInputText
                        :disabled="stateView == EFormState.VIEW"
                        :placeholder="
                            $t('module.inventory.order.form.deliveryType')
                        "
                        v-model="data.deliveryType"
                    >
                    </ACCDInputText>
                </div>
            </WrapFlexContainer>
            <div class="flex flex-col lg:flex-row-reverse items-end">
                <div class="w-full lg:w-1/5 flex flex-col gap-4 items-end">
                    <div class="w-full">
                        <p class="mb-1">
                            {{
                                $t("module.inventory.order.form.deliveryStatus")
                            }}
                        </p>
                        <ACCDSelect
                            :disabled="stateView == EFormState.VIEW"
                            :placeholder="
                                $t('module.inventory.order.form.deliveryStatus')
                            "
                            :options="deliveryOptions"
                            v-model="data.deliveryStatus"
                        >
                        </ACCDSelect>
                    </div>
                    <ACCDButton
                        type="primary"
                        size="md"
                        class="w-4/5"
                        v-if="stateView == EFormState.VIEW"
                    >
                        {{ $t("module.inventory.order.form.printBill") }}
                    </ACCDButton>
                </div>
                <div class="w-4/5">
                    <h1 class="text-lg font-semibold">
                        {{ $t("module.inventory.order.form.productList") }}
                    </h1>
                </div>
            </div>
            <ACCDTable :columns="columnData" :rowData="data.products">
                <template
                    #cell-action="{ row, rowIndex }"
                    v-if="stateView != EFormState.VIEW"
                >
                    <td>
                        <ACCDIcon
                            @click="
                                () => {
                                    deleteProductRow(row, rowIndex);
                                }
                            "
                            class="text-xl text-gray-700 cursor-pointer"
                            name="fa-solid fa-trash-can"
                        ></ACCDIcon>
                    </td>
                </template>
                <template #cell-productId="{ row, rowIndex }">
                    <td class="w-[450px]">
                        <ACCDSelect
                            :readonly="stateView == EFormState.VIEW"
                            
                            v-model="row.productId"
                            :options="listProduct"
                            @change="(val: any) => onSelectProduct(row, rowIndex, val)"
                        >
                        </ACCDSelect>
                    </td>
                </template>
                <template #cell-quantity="{ row, rowIndex, field }">
                    <td class="w-36">
                        <ACCDInputText
                            @keypress="preFilterInputNumberOnly"
                            
                            v-model="row.quantity"
                            :readonly="stateView == EFormState.VIEW"
                        >
                        </ACCDInputText>
                    </td>
                </template>
                <template #cell-discount="{ row, rowIndex }">
                    <td class="w-36">
                        <ACCDInputText
                            @keypress="preFilterInputNumberOnly"
                            
                            v-model="row.discount"
                            :readonly="stateView == EFormState.VIEW"
                        >
                            <template #inner-append>VNĐ</template>
                        </ACCDInputText>
                    </td>
                </template>
                <template #cell-tax="{ row, rowIndex }">
                    <td class="w-36">
                        <ACCDInputText
                            @keypress="preFilterInputNumberOnly"
                            
                            v-model="row.tax"
                            :readonly="stateView == EFormState.VIEW"
                        >
                            <template #inner-append>%</template>
                        </ACCDInputText>
                    </td>
                </template>
                <template #cell-price="{ row, rowIndex }">
                    <td class="w-36">
                        {{
                            (
                                (row.unitPrice * row.quantity - row.discount) *
                                (1 + row.tax / 100)
                            )
                                .toFixed(0)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </td>
                </template>
                <template #cell-unitPrice="{ row }">
                    <td class="w-36">
                        <ACCDInputText
                            @keypress="preFilterInputNumberOnly"
                            
                            v-model="row.unitPrice"
                            :readonly="stateView == EFormState.VIEW"
                        >
                            <template #inner-append>VNĐ</template>
                        </ACCDInputText>
                    </td>
                </template>
                <template #cell-totalPrice="{ row }">
                    <td>
                        {{
                            (row.unitPrice * row.quantity)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </td>
                </template>
            </ACCDTable>
            <span v-if="errorMessage" class="italic text-red-500">
                {{ errorMessage }}
            </span>
            <span
                v-if="stateView != EFormState.VIEW"
                class="cursor-pointer"
                style="color: #25b3e8"
                @click="addProductRowData"
                >{{
                    $t("module.serviceTicket.form.product.addProductRowData")
                }}</span
            >
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
                    <p class="font-semibold">
                        {{
                            data.products
                                .reduce((o: number, n: any) => {
                                    return (
                                        o +
                                        (n.unitPrice * n.quantity -
                                            n.discount) *
                                            (1 + n.tax / 100)
                                    );
                                }, 0)
                                .toFixed(0)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </p>
                    <p>
                        {{
                            data.products
                                .reduce((o: number, n: any) => {
                                    return o + n.unitPrice * n.quantity;
                                }, 0)
                                .toFixed(0)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </p>
                    <p>
                        {{
                            data.products
                                .reduce((o: number, n: any) => {
                                    return (
                                        o +
                                        (n.unitPrice * n.quantity -
                                            n.discount) *
                                            (n.tax / 100)
                                    );
                                }, 0)
                                .toFixed(0)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </p>
                    <p>
                        {{
                            data.products
                                .reduce((o: number, n: any) => {
                                    return Number(o) + Number(n.discount);
                                }, 0)
                                .toFixed(0)
                                .toString()
                                .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                        }}
                    </p>
                </div>
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
                    @click="onSubmit"
                    type="primary"
                    size="md"
                    class="w-1/6"
                    v-if="stateView != EFormState.VIEW"
                >
                    {{ $t("module.inventory.order.save") }}
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
    <OrderDistributorForm
        v-if="onShowBill"
        @close="onShowBill = false"
        :productInfo="data.products"
        :distributorName="distributorInfo.distributorName"
        :distributorContactPhone="distributorInfo.contactPhone"
    >
    </OrderDistributorForm>
</template>

<script setup lang="ts">
import OrderDistributorForm from "@/modules/sharedModules/pages/formHTML/OrderDistributorForm.vue";
import WrapFlexContainer from "@/modules/sharedModules/component/WrapFlexContainer.vue";
import { getListDistributors } from "@/modules/distributor/api";
import { EFormState } from "@/enums";
import { ref, computed, onMounted, watch } from "vue";
import { useI18n } from "@/composables/useI18n";
import { useToast } from "@/composables/useToast";
import { getListProducts } from "@/modules/product/api";
import { cloneDeep } from "lodash";
import { useRouter, useRoute } from "vue-router";
import { emitter } from "@/utils/mitt";
import { preFilterInputNumberOnly } from "@/modules/sharedModules/component/constants";
import {
    createOrderDistributor,
    getDetailOrderDistributorController,
    updateOrderDistributor,
} from "@/modules/inventory/api";
const { $t } = useI18n();
const { $toast } = useToast();

type ModalProps = {
    modelValue: boolean;
    state: EFormState | undefined;
    orderId?: number | string;
};

const props = withDefaults(defineProps<ModalProps>(), {
    modelValue: true,
    state: EFormState.ADD,
});
const distributorInfo = ref({
    contactPhone: "",
    distributorName: "",
});
const onShowBill = ref(false);
const deliveryOptions = ref([
    {
        value: 1,
        label: $t("module.inventory.order.deliveryStatus.1"),
    },
    {
        value: 3,
        label: $t("module.inventory.order.deliveryStatus.3"),
    },
    {
        value: 4,
        label: $t("module.inventory.order.deliveryStatus.4"),
    },
    {
        value: 5,
        label: $t("module.inventory.order.deliveryStatus.5"),
    },
    {
        value: 6,
        label: $t("module.inventory.order.deliveryStatus.6"),
    },
]);
const columnData = [
    {
        key: "productId",
        headerName: $t("module.inventory.order.form.table.productId"),
    },
    {
        key: "unit",
        headerName: $t("module.inventory.order.form.table.unit"),
    },
    {
        key: "quantity",
        headerName: $t("module.inventory.order.form.table.quantity"),
    },
    {
        key: "unitPrice",
        headerName: $t("module.inventory.order.form.table.unitPrice"),
    },
    {
        key: "totalPrice",
        headerName: $t("module.inventory.order.form.table.totalPrice"),
    },
    {
        key: "discount",
        headerName: $t("module.inventory.order.form.table.discount"),
    },
    {
        key: "tax",
        headerName: $t("module.inventory.order.form.table.tax"),
    },
    {
        key: "price",
        headerName: $t("module.inventory.order.form.table.price"),
    },
    {
        key: "action",
        headerName: "",
    },
];

const data = ref({
    distributorId: 0,
    deliveryType: "",
    deliveryStatus: 1,
    paymentStatus: 1,
    discount: 0,
    tax: 0,
    totalPrice: 0,
    inventoryId: 0,
    products: [] as (typeof baseProduct)[],
});
const baseProduct = {
    productId: 0,
    productCode: "",
    productName: "",
    quantity: 0,
    unit: "",
    unitPrice: 0,
    discount: 0,
    tax: 0,
    price: 0,
};
const errorMessage = ref("");

const stateView = computed(() => {
	
    return props.state;
});
const emit = defineEmits<{
    (e: "update:modelValue", value: boolean): void;
    (e: "refresh"): void;
}>();

const lazyValue = computed({
    get() {
        return props.modelValue;
    },
    set(val: boolean) {
        emit("update:modelValue", val);
    },
});

const computedTitle = computed(() => {
    if (stateView.value == EFormState.ADD) {
        return $t("module.inventory.order.form.create");
    }
    if (stateView.value == EFormState.EDIT) {
        return $t("module.inventory.order.form.edit");
    }
    if (stateView.value == EFormState.VIEW) {
        return $t("module.inventory.order.form.view");
    }
});

const listDistributor = ref([]);
const listDistributorName = ref([]);

const listProduct = ref([]);

const deleteProductRow = (row: typeof baseProduct, index: number) => {
    data.value.products = data.value.products.filter((sRow, i) => {
        return i != index;
    });
};

const addProductRowData = () => {
    data.value.products.push(cloneDeep(baseProduct));
};

const onSelectProduct = (row: any, rowIndex: number, productId: number) => {
    let originProduct = listProduct.value.find((p: any) => {
        return p.value == productId;
    }) as any;

    row.productCode = originProduct.origin.code;
    row.productName = originProduct.origin.name;
    row.unit = originProduct.origin.unit;
    // row.unitPrice = originProduct.origin.purchasePrice;
};

watch(
    () => data.value.distributorId,
    (val, oldVal) => {
        let distributor = listDistributor.value.find((d: any) => {
            return val == d.value;
        }) as any;
        if (distributor) {
            distributorInfo.value.contactPhone =
                distributor.origin.contactPhone;
            distributorInfo.value.distributorName = distributor.origin.name;
        }
    }
);
const onClickBack = () => {
    if (stateView.value != EFormState.VIEW) {
        emitter.emit("layout-pages-open-confirmClose", instanceKey);
    } else {
        lazyValue.value = false;
    }
};
const onSubmit = () => {
    let postData = cloneDeep(data.value);
    postData.tax = data.value.products.reduce((o: number, n: any) => {
        return (
            Number(o) +
            Number((n.unitPrice * n.quantity - n.discount) * (n.tax / 100))
        );
    }, 0);
    postData.discount = Number(
        data.value.products
            .reduce((o: number, n: any) => {
                return Number(o) + Number(n.discount);
            }, 0)
            .toFixed(0)
    );
    postData.products.forEach((p) => {
        p.price = (p.unitPrice * p.quantity - p.discount) * (p.tax / 100);
        p.tax = p.tax / 100;
    });
    if (stateView.value == EFormState.ADD) {
        createOrderDistributor(postData).then((res) => {
            if (res.code == 1) {
                $toast(
                    $t("module.inventory.order.toast.createOrderSuccess"),
                    true
                );
                emit("refresh");
                lazyValue.value = false;
            } else {
                $toast(
                    $t("module.inventory.order.toast.createOrderFailse"),
                    false
                );
                errorMessage.value = res.message;
            }
        });
    } else {
        let id = props.orderId;
        updateOrderDistributor(postData, id as string).then((res) => {
            if (res.code == 1) {
                $toast(
                    $t("module.inventory.order.toast.updateOrderSuccess"),
                    true
                );
                emit("refresh");
                lazyValue.value = false;
            } else {
                $toast(
                    $t("module.inventory.order.toast.updateOrderFailse"),
                    false
                );
                errorMessage.value = res.message;
            }
        });
    }
};
const instanceKey = new Date().getTime();

watch(
    () => data.value.distributorId,
    () => {
        getListProducts({
            pageSize: 10000,
            pageNumber: 1,
            type: 1,
            distributorId: data.value.distributorId,
        }).then((res) => {
            listProduct.value = res.data.map((e: any) => {
                return {
                    value: e.id,
                    label: e.code + " - " + e.name,
                    origin: e,
                };
            });
        });
    },
    { deep: true }
);

onMounted(() => {

    emitter.on("pages-layout-on-confirmClose", (ik) => {
        if (ik == instanceKey) {
            lazyValue.value = false;
        }
    });
    if (stateView.value != EFormState.ADD) {
        let id = props.orderId;
        getDetailOrderDistributorController(id as string).then((res) => {
            if (res.code == 1) {
                data.value = {
                    ...res.data,
                    products: res.data.products.map((a: any) => {
                        return { ...a, tax: a.tax * 100 };
                    }),
                };
            }
        });
    }
    getListDistributors({
        pageNumber: 1,
        pageSize: 10000,
    }).then((res) => {
        if (res.code == 1) {
            listDistributor.value = res.data.map((a: any) => {
                return {
                    value: a.id,
                    label: a.code + " - " + a.name,
                    origin: a,
                };
            });
            listDistributorName.value = res.data.map((a: any) => {
                return {
                    value: a.id,
                    label: a.name,
                };
            });
        }
    });
    getListProducts({
        pageSize: 10000,
        pageNumber: 1,
        type: 1,
    }).then((res) => {
        listProduct.value = res.data.map((e: any) => {
            return {
                value: e.id,
                label: e.code + " - " + e.name,
                origin: e,
            };
        });
    });
});
</script>
