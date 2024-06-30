<template>
    <div class="flex items-center h-[80vh]" v-if="isLoading">
        <CDLoading color="blue" size="large" />
    </div>
    <CDTable
        ref="table"
        :forFilter="true"
        :columns="columnData"
        :rowData="rowData"
        :tableName="tableName"
        :actions="tableActions"
        :forActions="true"
        :contextActions="contextActions"
        usePagination
        :pagination="pagination"
        @changePage="changePage"
    />

    <CDDialog
        v-if="isOpenDialog"
        :title="computedTitleDialog(item.status)"
        @closeDialog="closeDialog"
    >
        <template #content>
            <div class="mt-4 flex flex-col gap-4">
                <CDInput
                    v-bind="{
                        label: $t('module.sellingManagement.car.modal.carName'),
                        placeholder: $t(
                            'module.sellingManagement.car.modal.placeholder.carName'
                        ),
                    }"
                    v-model="item.carName"
                    :readonly="item.status === Status.VIEW"
                    :required="true"
                />

                <CDSelect
                    :modelValue="item.carBrandId"
                    :options="selectOptions.brands"
                    :label="$t('module.sellingManagement.car.modal.carBrand')"
                    :placeholder="
                        $t(
                            'module.sellingManagement.car.modal.placeholder.carBrand'
                        )
                    "
                    :loadingOptions="selectOptions.brands.length === 0"
                    @update:modelValue="(id : number) => {item.carBrandId = id; item.carModelId = undefined; item.carYearId = undefined; item.carEngineId = undefined}"
                    :disabled="item.status === Status.VIEW"
                />
                <CDSelect
                    :modelValue="item.carModelId"
                    :options="selectOptions.car_models"
                    :label="$t('module.sellingManagement.car.modal.carModel')"
                    :placeholder="
                        $t(
                            'module.sellingManagement.car.modal.placeholder.carModel'
                        )
                    "
                    :loadingOptions="selectOptions.car_models.length === 0"
                    @update:modelValue="(id : number) => {item.carModelId = id; item.carYearId = undefined; item.carEngineId = undefined}"
                    :disabled="
                        item.carBrandId === undefined ||
                        item.status === Status.VIEW
                    "
                />
                <CDSelect
                    :modelValue="item.carYearId"
                    :options="selectOptions.car_years"
                    :label="$t('module.sellingManagement.car.modal.carYear')"
                    :placeholder="
                        $t(
                            'module.sellingManagement.car.modal.placeholder.carYear'
                        )
                    "
                    :loadingOptions="selectOptions.car_years.length === 0"
                    @update:modelValue="(id : number) => {item.carYearId = id; item.carEngineId = undefined}"
                    :disabled="
                        item.carModelId === undefined ||
                        item.status === Status.VIEW
                    "
                />
                <CDSelect
                    :modelValue="item.carEngineId"
                    :options="selectOptions.car_engines"
                    :label="$t('module.sellingManagement.car.modal.carEngine')"
                    :placeholder="
                        $t(
                            'module.sellingManagement.car.modal.placeholder.carEngine'
                        )
                    "
                    :loadingOptions="selectOptions.car_engines.length === 0"
                    @update:modelValue="(id : number) => (item.carEngineId = id)"
                    :disabled="
                        item.carYearId === undefined ||
                        item.status === Status.VIEW
                    "
                />
                <CDInput
                    v-bind="{
                        modelValue: undefined,
                        label: $t(
                            'module.sellingManagement.car.modal.licensePlate'
                        ),
                        placeholder: $t(
                            'module.sellingManagement.car.modal.placeholder.licensePlate'
                        ),
                    }"
                    v-model="item.licensePlate"
                    :readonly="item.status === Status.VIEW"
                    required
                />
                <CDInput
                    v-bind="{
                        modelValue: undefined,
                        label: $t(
                            'module.sellingManagement.car.modal.vinNumber'
                        ),
                        placeholder: $t(
                            'module.sellingManagement.car.modal.placeholder.vinNumber'
                        ),
                    }"
                    v-model="item.vinNumber"
                    :readonly="item.status === Status.VIEW"
                    required
                />
            </div>
            <div
                class="text-center text-sm w-full mt-5"
                :class="{
                    'text-red-500': response != null && response.code != 1,
                }"
            >
                {{ response != null ? response.message : null }}
            </div>
        </template>
        <template #action>
            <div class="flex justify-end mt-4">
                <button
                    @click="closeDialog"
                    type="button"
                    class="block rounded-md px-3 py-2 text-center text-sm text-gray-600 border mr-2"
                >
                    {{ $t("module.distributor.back") }}
                </button>
                <button
                    v-if="item.status !== Status.VIEW"
                    @click="onSubmit"
                    type="submit"
                    class="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                    {{ $t("module.distributor.save") }}
                </button>
            </div>
        </template>
    </CDDialog>
</template>

<script lang="ts">
import {
    addNewGarageCar,
    getCarFeatures,
    getGarageCars,
    updateGarageCar,
} from "@/modules/sellingManagement/api/index";
import { defineComponent } from "vue";

import CDSelect from "../components/CDSelect.vue";

const undefinedItem = {
    id: undefined,
    carName: undefined,
    carBrandId: undefined,
    carModelId: undefined,
    carYearId: undefined,
    carEngineId: undefined,
    licensePlate: undefined,
    vinNumber: undefined,
    driverCarId: undefined,
    status: undefined,
};

const Status = {
    ADD: 0,
    EDIT: 1,
};

const PAGE_SIZE = 10;

export default defineComponent({
    components: {
        CDSelect,
    },
    setup() {
        return {};
    },
    data() {
        return {
            Status: {
                ADD: 0,
                EDIT: 1,
                VIEW: 2,
            },
            pagination: {
                perPage: PAGE_SIZE,
                total: 0,
                currentPage: 1,
            },
            response: {} as any,
            isOpenDialog: false as boolean,
            tableName: this.$t("module.sellingManagement.car.tableName"),
            rowData: [] as Array<object>,
            listGarageCustomer: [] as any,
            customerTypes: {} as any,
            customerTypeOptions: {} as any, // Select options
            item: undefinedItem as any,
            columnData: [
                {
                    field: "order",
                    headerName: this.$t(
                        "module.sellingManagement.customer.columnTable.order"
                    ),
                },
                {
                    field: "carName",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.carName"
                    ),
                    maxLength: 20,
                },
                {
                    field: "carBrand",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.carBrand"
                    ),
                },
                {
                    field: "carModel",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.carModel"
                    ),
                },
                {
                    field: "carYear",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.carYear"
                    ),
                },
                {
                    field: "carEngine",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.carEngine"
                    ),
                },
                {
                    field: "licensePlate",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.licensePlate"
                    ),
                },
                {
                    field: "vinNumber",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.vinNumber"
                    ),
                },
                {
                    field: "action",
                    headerName: this.$t(
                        "module.sellingManagement.car.columnTable.action"
                    ),
                },
            ],
            selectModelValue: {
                text: undefined,
                id: undefined,
            },
            contextActions: [
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer.action.view"
                    ),
                    action: (params: any) => {
                        this.openModalRowAction(params);
                        this.item.status = this.Status.VIEW;
                    },
                },
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer.action.edit"
                    ),
                    action: (params: any) => {
                        this.openModalRowAction(params);
                        this.item.status = this.Status.EDIT;
                    },
                },
            ],
            tableActions: {
                name: this.$t(
                    "module.sellingManagement.customer.action.addNew"
                ),
                action: this.onClickTableActionButton,
            },
            totalItem: 7,
            carFeatures: undefined,
            isLoading: true,
            selectOptions: {
                brands: [] as any,
                car_models: [] as any,
                car_years: [] as any,
                car_engines: [] as any,
            },
        };
    },
    created() {
        this.getTableRowData({
            pageSize: PAGE_SIZE,
            pageNumber: 1,
            currentPage: 1,
        });
        this.getCarBrandFeature();
    },

    watch: {
        "item.carBrandId": {
            handler(newVal, oldVal) {
                if (newVal != oldVal) {
                    this.selectOptions.car_models = [];
                    getCarFeatures({
                        type: "model",
                        brand_id: newVal,
                    }).then((res) => {
                        this.selectOptions.car_models =
                            res.data.data.car_models.map((item: any) => {
                                return {
                                    id: item.id,
                                    value: item.title,
                                };
                            });
                    });
                }
            },
        },
        "item.carModelId": {
            handler(newVal, oldVal) {
                if (newVal != oldVal) {
                    this.selectOptions.car_years = [];
                    getCarFeatures({
                        type: "year",
                        model_id: newVal,
                    }).then((res) => {
                        this.selectOptions.car_years =
                            res.data.data.car_years.map((item: any) => {
                                return {
                                    id: item.id,
                                    value: item.title,
                                };
                            });
                    });
                }
            },
        },
        "item.carYearId": {
            handler(newVal, oldVal) {
                if (newVal != oldVal) {
                    this.selectOptions.car_engines = [];
                    getCarFeatures({
                        type: "engine",
                        year_id: newVal,
                    }).then((res) => {
                        this.selectOptions.car_engines =
                            res.data.data.car_engines.map((item: any) => {
                                return {
                                    id: item.id,
                                    value: item.title,
                                };
                            });
                    });
                }
            },
        },
    },

    methods: {
        onSubmit() {
            if (this.item.status == Status.ADD) {
                this.addNewItem();
            } else if (this.item.status == Status.EDIT) {
                this.updateItem();
            }
        },
        getTitleById(feature: any, id: any) {
            const featureItem = feature.find((item: any) => item.id === id);
            return featureItem.title;
        },
        getIdByTitle(feature: any, title: any) {
            const featureItem = feature.find(
                (item: any) => item.title === title
            );
            return featureItem.id;
        },
        computedTitleDialog(status: number) {
            if (status === this.Status.ADD) {
                return this.$t(
                    "module.sellingManagement.car.modal.addNewTitle"
                );
            } else if (status === this.Status.EDIT) {
                return this.$t("module.sellingManagement.car.modal.editTitle");
            } else if (status === this.Status.VIEW) {
                return this.$t("module.sellingManagement.car.modal.viewTitle");
            }
        },
        changePage(val: any) {
            this.totalItem = -1;
            this.pagination.currentPage = val.currentPage;
            this.rowData = [];
            this.getTableRowData({
                pageNumber: this.pagination.currentPage,
                pageSize: PAGE_SIZE,
            });
        },

        openDialog() {
            this.isOpenDialog = true;
        },
        closeDialog() {
            this.isOpenDialog = false;
        },
        getKeyByValue(object: any, value: any) {
            return Object.keys(object).find((key) => object[key] === value);
        },
        onClickTableActionButton() {
            this.item = { ...undefinedItem };
            this.item.status = this.Status.ADD;
            this.response = null;
            this.openDialog();
        },
        openModalRowAction(value: any) {
            this.response = null;
            this.item = { ...undefinedItem };
            this.item = {
                id: value.id,
                carName: value.carName,
                carBrandId: value.carBrandId,
                carModelId: value.carModelId,
                carYearId: value.carYearId,
                carEngineId: value.carEngineId,
                licensePlate: value.licensePlate,
                vinNumber: value.vinNumber,
                driverCarId: value.driverCarId,
            };
            this.openDialog();
        },
        async getCarBrandFeature() {
            getCarFeatures({
                type: "brand",
            }).then((res) => {
                this.selectOptions.brands = res.data.data.brands.map(
                    (item: any) => {
                        return {
                            id: item.id,
                            value: item.title,
                        };
                    }
                );
            });
        },

        async addNewItem() {
            const res = await addNewGarageCar(this.item);
            this.response = { ...res };

            // Update success
            if (this.response.code == 1) {
                this.rowData = [];
                this.getTableRowData({
                    pageNumber: this.pagination.currentPage,
                    pageSize: PAGE_SIZE,
                });
                this.closeDialog();
                this.$toast(
                    this.$t(
                        "module.sellingManagement.customer.toast.addTypeSuccess"
                    ),
                    true
                );
            }
        },
        async getTableRowData(params: any) {
            getGarageCars(params).then((data: any) => {
                const listItem = data.data;
                this.rowData = listItem.map((item: any, index: number) => {
                    return {
                        id: item.id,
                        order: index + 1 + (params.pageNumber - 1) * PAGE_SIZE,
                        carName: item.carName,
                        carBrandId: item.carBrandId,
                        carBrand: item.carBrandName,
                        carModelId: item.carModelId,
                        carModel: item.carModelName,
                        carYearId: item.carYearId,
                        carYear: item.carYearName,
                        carEngineId: item.carEngineId,
                        carEngine: item.carEngineName,
                        licensePlate: item.licensePlate,
                        vinNumber: item.vinNumber,
                        driverCarId: item.driverCarId,
                    };
                });
                this.totalItem = listItem.length;
                this.pagination.total = data.totalElement;
                this.isLoading = false;
            });
        },
        async updateItem() {
            const res = await updateGarageCar(this.item, this.item.id);
            this.response = { ...res };

            // Update success
            if (this.response.code == 1) {
                this.rowData = [];
                this.getTableRowData({
                    pageNumber: this.pagination.currentPage,
                    pageSize: PAGE_SIZE,
                });
                this.closeDialog();
                this.$toast(
                    this.$t(
                        "module.sellingManagement.customer.toast.editTypeSuccess"
                    ),
                    true
                );
            }
        },
    },
});
</script>
