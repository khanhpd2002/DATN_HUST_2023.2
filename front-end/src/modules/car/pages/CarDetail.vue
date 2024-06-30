<template>
    <div class="p-[16px] bg-white rounded">
        <div class="mb-4 flex gap-4">
            <div class="w-1/4">
                <p class="text-sm mb-1">{{ $t("module.car.carBrand") }}</p>
                <ACCDSelect
                    size="md"
                    v-model="item.carBrandId"
                    :options="carBrandOptions"
                    labelName="label"
                    valueName="value"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.carBrand')"
                />
            </div>
            <div class="w-1/4">
                <p class="text-sm mb-1">{{ $t("module.car.carModel") }}</p>
                <ACCDSelect
                    size="md"
                    v-model="item.carModelId"
                    :options="carModelOptions"
                    labelName="label"
                    valueName="value"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.carModel')"
                />
            </div>
            <div class="w-1/4">
                <p class="text-sm mb-1">{{ $t("module.car.carYear") }}</p>
                <ACCDSelect
                    size="md"
                    v-model="item.carYearId"
                    :options="carYearOptions"
                    labelName="label"
                    valueName="value"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.carYear')"
                />
            </div>
            <div class="w-1/4">
                <p class="text-sm mb-1">{{ $t("module.car.carVersion") }}</p>
                <ACCDSelect
                    size="md"
                    v-model="item.carVersionId"
                    :options="carVersionOptions"
                    labelName="label"
                    valueName="value"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.carVersion')"
                />
            </div>
        </div>
        <div class="mb-4 flex gap-4">
            <div class="w-1/2">
                <p class="text-sm mb-1">{{ $t("module.car.licensePlate") }}</p>
                <ACCDInputText
                    size="md"
                    v-model="item.licensePlate"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.licensePlate')"
                />
            </div>
            <div class="w-1/2">
                <p class="text-sm mb-1">{{ $t("module.car.vinNumber") }}</p>
                <ACCDInputText
                    size="md"
                    v-model="item.vinNumber"
                    :disabled="isView"
                    :placeholder="$t('module.car.placeholder.vinNumber')"
                />
            </div>
        </div>
        <div>
            <h1 class="font-semibold text-2xl mb-4">
                {{ $t("module.car.titleHistory") }}
            </h1>
            <ACCDTable
                ref="table"
                :columns="columnData"
                :rowData="rowData"
                :tableName="tableName"
            >
                <template #cell-action="{ row, col, field }">
                    <ACCDDropdown>
                        <template v-slot:activator="{ props }">
                            <div
                                class="w-8 cursor-pointer flex justify-center"
                                v-bind="props"
                            >
                                <ACCDIcon
                                    v-bind="props"
                                    class="text-base text-black"
                                    name="fa-solid fa-eye"
                                    @click="
                                        () =>
                                            viewDetailHistory(row.repairOrderId)
                                    "
                                ></ACCDIcon>
                            </div>
                        </template>
                    </ACCDDropdown>
                </template>
                <template #cell-appointmentDate="{ row, col, field }">
                    <div class="white-space-nowrap">
                        {{ dayjs(row.appointmentDate).format("DD/MM/YY") }}
                    </div>
                </template>

                <template #cell-totalPrice="{ row, col, field }">
                    <div class="white-space-nowrap">
                        {{
                            row.totalPrice
                                ? row.totalPrice
                                      .toString()
                                      .replace(/\B(?=(\d{3})+(?!\d))/g, ",")
                                : "0,000"
                        }}
                    </div>
                </template>
            </ACCDTable>
        </div>
        <div>
            <div class="flex justify-end mt-4 space-x-4">
                <ACCDButton
                    size="sm"
                    type="secondary"
                    variant="fill"
                    class="w-1/6"
                    @click="backCarList()"
                >
                    <span class="text-info-base font-medium text-sm">{{
                        $t("module.car.action.back")
                    }}</span>
                </ACCDButton>
                <ACCDButton
                    size="sm"
                    type="secondary"
                    variant="fill"
                    class="bg-info-secondary border-none w-1/6"
                    @click="onSubmit"
                    v-if="!isView"
                >
                    <span class="text-white font-medium text-sm">{{
                        $t("module.car.action.save")
                    }}</span>
                </ACCDButton>
            </div>
        </div>
        <!--    <CarForm ref="CarFormRef" @refresh="onRefreshData" />-->
    </div>
</template>

<script lang="ts">
import { getCarById, updateCarById } from "@/modules/car/api";
import CarForm from "@/modules/car/pages/CarForm.vue";
import { ICar, ICarInfo, ICarRepairHistory, ISelectOption } from "@/types";
import { EFormState } from "@/enums";
import { defineComponent } from "vue";
import {
    getCarBrandList,
    getCarModelList,
    getCarVersionList,
    getCarYearList,
} from "@/modules/sharedModules/api";
import { useRoute } from "vue-router";
import router from "@/router";
import { formatPriceVN } from "../../sharedModules/component/constants";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
dayjs.extend(customParseFormat);

const PAGE_SIZE = 10;

export default defineComponent({
    components: {
        // CarForm,
    },
    setup() {
        const route = useRoute();
        const carId = route.params.id;
        const isView = route.path.includes("/info");
        return { carId, isView };
    },
    computed: {
        rowData(): ICarRepairHistory[] {
            return (
                this.listItem?.map((item: ICarRepairHistory, index: number) => {
                    return {
                        ...item,
                        stt: index + 1,
                    };
                }) || []
            );
        },
    },
    mounted() {
        setTimeout(() => {
            this.$evtBus.emit(
                "layout-pages-change-navName",
                this.isView
                    ? this.$t("module.car.titleDetail")
                    : this.$t("module.car.titleEdit")
            );
        }, 200);
    },
    data() {
        return {
            dayjs: dayjs,
            responseErrorMessages: "" as string,
            item: {} as ICar,
            listItem: [] as any,
            state: EFormState,
            EFormState: EFormState,
            tableName: this.$t("module.customerType.tableName"),
            carBrandOptions: [] as ISelectOption[],
            carModelOptions: [] as ISelectOption[],
            carYearOptions: [] as ISelectOption[],
            carVersionOptions: [] as ISelectOption[],
            columnData: [
                {
                    key: "stt",
                    headerName: this.$t("module.car.stt"),
                },
                {
                    key: "customerName",
                    headerName: this.$t("module.car.detail.customerName"),
                    maxWidth: "150px",
                },
                {
                    key: "appointmentDate",
                    headerName: this.$t("module.car.detail.appointmentDate"),
                    maxWidth: "150px",
                },
                {
                    key: "products",
                    headerName: this.$t("module.car.detail.products"),
                },
                {
                    key: "services",
                    headerName: this.$t("module.car.detail.services"),
                },
                {
                    key: "totalPrice",
                    headerName: this.$t("module.car.detail.totalPrice"),
                },
                {
                    key: "action",
                    headerName: this.$t("module.car.action.action"),
                    align: "center",
                },
            ],
        };
    },
    async created() {
        await getCarBrandList().then((res) => {
            this.carBrandOptions = res.data.data.map((item: ICarInfo) => {
                return {
                    value: item.id,
                    label: item.title,
                };
            });
        });
        await this.getTableRowData(Number(this.carId));
    },
    watch: {
        "item.carBrandId": {
            handler(newVal: number, oldVal: number) {
                if (newVal != oldVal) {
                    this.carModelOptions = [];
                    getCarModelList(newVal).then((res) => {
                        this.carModelOptions = res.data.data.map(
                            (item: ICarInfo) => {
                                return {
                                    value: item.id,
                                    label: item.title,
                                };
                            }
                        );
                    });
                }
            },
        },
        "item.carModelId": {
            handler(newVal: number, oldVal: number) {
                if (newVal != oldVal) {
                    this.carYearOptions = [];
                    getCarYearList(newVal).then((res) => {
                        this.carYearOptions = res.data.data.map(
                            (item: ICarInfo) => {
                                return {
                                    value: item.id,
                                    label: item.title,
                                };
                            }
                        );
                    });
                }
            },
        },
        "item.carYearId": {
            handler(newVal: number, oldVal: number) {
                if (newVal != oldVal) {
                    this.carVersionOptions = [];
                    getCarVersionList(newVal).then((res) => {
                        this.carVersionOptions = res.data.data.map(
                            (item: ICarInfo) => {
                                return {
                                    value: item.id,
                                    label: item.title,
                                };
                            }
                        );
                    });
                }
            },
        },
    },
    methods: {
        formatPriceVN,
        onRefreshData() {
            this.listItem = [];
            this.getTableRowData(Number(this.carId));
        },
        onOpenFormModal(id?: number, params?: ICar, state?: EFormState) {
            (this.$refs.CarFormRef as InstanceType<typeof CarForm>).openDialog(
                id,
                params,
                state
            );
        },
        backCarList() {
            this.$router.back();
            // this.$router.push({ path: `/app/cars` });
        },
        viewDetailHistory(id: number) {
            if (id) router.push(`/app/sell/new-service-tickets-detail/${id}`);
        },
        async getTableRowData(carId: number) {
            const result = await getCarById(carId);
            this.item = result.data;
            this.listItem = result.data.listHistoryRepairOrders || [];
        },
        onSubmit() {
            updateCarById(this.item, this.item.id).then((res) => {
                if (res.code == 1) {
                    this.$toast(this.$t("module.car.success.edit"), true);
                    this.$emit("refresh");
                } else {
                    this.responseErrorMessages = res.message;
                    this.$toast(this.responseErrorMessages, false);
                }
            });
        },
        async getCarFeatureOption(data: any) {
            const dataRes = {
                carBrands: [],
                carModels: [],
                carYears: [],
                carVersions: [],
            };
            const carBrandId = data.carBrandId;
            const carModelId = data.carModelId;
            const carYearId = data.carYearId;
            let res: any;
            if (carBrandId == 0) {
                res = await getCarBrandList();
                dataRes.carBrands = res.data.data;
            } else {
                res = await getCarModelList(carBrandId);
                dataRes.carModels = res.data.data;
                if (carModelId == 0) {
                    res = await getCarModelList(carModelId);
                    dataRes.carYears = res.data.data;
                } else {
                    res = await getCarYearList(carModelId);
                    dataRes.carYears = res.data.data;
                    if (carYearId != 0) {
                        res = await getCarVersionList(carYearId);
                        dataRes.carVersions = res.data.data;
                    }
                }
            }
            return dataRes;
        },
    },
});
</script>

<style>
.white-space-nowrap {
    white-space: nowrap !important;
}
</style>
