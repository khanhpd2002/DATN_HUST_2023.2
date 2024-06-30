<template>
    <div class="">
        <h1 class="font-semibold text-2xl mb-4">
            {{ $t("module.sellingManagement.customer-type.tableName") }}
        </h1>
        <div class="mb-4 flex gap-4">
            <div class="w-1/4">
                <p class="text-sm mb-1">
                    {{
                        $t(
                            "module.sellingManagement.customer-type.columnTable.customerTypeName"
                        )
                    }}
                </p>
                <ACCDInputText size="dm" v-model="filterConfig.name.value" :placeholder="$t(
                    'module.sellingManagement.customer-type.columnTable.customerTypeName'
                )
                    " />
            </div>
        </div>
        <div class="mb-4 flex flex-row-reverse">
            <div class="w-1/6">
                <ACCDButton @click="filter" size="sm" type="primary" variant="fill" class="w-full">
                    <span class="text-white font-medium text-base">{{
                        $t(
                            "module.sellingManagement.customer-type.action.search"
                        )
                    }}</span>
                </ACCDButton>
            </div>
            <div class="w-1/6 pr-4">
                <ACCDButton size="lg" class="bg-info-secondary border-none w-full" @click="() => {
                            onOpenFormModal({} as ICustomerType, EFormState.ADD)
                        }">
                    <span class="text-info-base font-medium text-base">{{
                        $t("module.sellingManagement.customer-type.action.add")
                    }}</span>
                </ACCDButton>
            </div>
        </div>
        <div>
            <ACCDTable ref="table" :columns="columnData" :rowData="rowData" :tableName="tableName" :pagination="pagination"
                @changePage="changePage">
                <template #cell-action="{ row, col, field }">
                    <ACCDDropdown>
                        <template v-slot:activator="{ props }">
                            <div class="text-center cursor-pointer" v-bind="props">
                                <ACCDIcon class="text-base text-black" name="icon-ellipsis-vertical"></ACCDIcon>
                            </div>
                        </template>
                        <div class="rounded-md border w-56 bg-white py-2">
                            <div class="flex px-4 py-2 gap-3 cursor-pointer" @click="() => onOpenFormModal(row, EFormState.VIEW)
                                ">
                                <ACCDIcon class="text-sm text-gray-700" name="icon-pen-to-square"></ACCDIcon>
                                <span class="text-sm text-gray-700">
                                    {{
                                        $t(
                                            "module.sellingManagement.customer-type.action.edit"
                                        )
                                    }}
                                </span>
                            </div>
                            <hr />
                            <div class="flex px-4 py-2 gap-3 cursor-pointer">
                                <ACCDIcon class="text-sm text-gray-700" name="icon-file-contract"></ACCDIcon>
                                <span class="text-sm text-gray-700">
                                    {{
                                        $t(
                                            "module.sellingManagement.customer-type.action.view"
                                        )
                                    }}
                                </span>
                            </div>
                        </div>
                    </ACCDDropdown>
                </template>
            </ACCDTable>
        </div>
        <DistributorForm ref="DistributorFormRef" @refresh="onRefreshData" />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getGarageCustomerType } from "@/modules/sellingManagement/api";
import { EFormState } from "@/enums";
import { ICustomerType } from "@/types";

const PAGE_SIZE = 10;

export default defineComponent({
    setup() {
        return {};
    },
    computed: {
        rowData(): ICustomerType[] {
            return (
                this.listItem?.map((item: ICustomerType, index: number) => {
                    return {
                        ...item,
                        order:
                            index +
                            1 +
                            PAGE_SIZE * (this.pagination.currentPage - 1),
                    };
                }) || []
            );
        },
    },
    data() {
        return {
            EFormState: EFormState,
            pagination: {
                perPage: PAGE_SIZE,
                total: 0,
                currentPage: 1,
            },
            tableName: this.$t(
                "module.sellingManagement.customer-type.tableName"
            ),
            listItem: [] as any,
            columnData: [
                {
                    key: "order",
                    headerName: this.$t(
                        "module.sellingManagement.customer-type.columnTable.order"
                    ),
                },
                {
                    key: "customerTypeName",
                    headerName: this.$t(
                        "module.sellingManagement.customer-type.columnTable.customerTypeName"
                    ),
                },
                {
                    key: "description",
                    headerName: this.$t(
                        "module.sellingManagement.customer-type.columnTable.description"
                    ),
                },
                {
                    key: "action",
                    headerName: "",
                },
            ],
            filterConfig: {
                name: {
                    value: "",
                },
            } as any,

            contextActions: [
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer-type.action.view"
                    ),
                    action: (params: any) =>
                        this.onOpenFormModal(params, EFormState.VIEW),
                },
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer-type.edit"
                    ),
                    action: (params: any) =>
                        this.onOpenFormModal(params, EFormState.EDIT),
                },
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer-type.delete"
                    ),
                    action: (params: any) =>
                        this.onOpenFormModal(params, EFormState.DELETE),
                },
            ],
        };
    },
    created() {
        // this.calculateAdressOption();
        this.getTableRowData({
            pageSize: PAGE_SIZE,
            pageNumber: 1,
        });
    },
    methods: {
        onRefreshData() {
            this.listItem = [];
            this.getTableRowData({
                pageSize: PAGE_SIZE,
                pageNumber: 1,
            });
        },
        onOpenFormModal(params: ICustomerType, state: EFormState) { },
        filter() {
            let data = {
                pageSize: PAGE_SIZE,
                pageNumber: 1,
            } as any;
            Object.keys(this.filterConfig).map((field) => {
                if (this.filterConfig[field].value) {
                    data[field] = this.filterConfig[field].value;
                }
            });
            this.getTableRowData(data);
        },

        changePage(val: any) {
            this.pagination.currentPage = val.currentPage;
            this.getTableRowData({
                pageNumber: val.currentPage,
                pageSize: PAGE_SIZE,
            });
        },
        async getTableRowData(params: any) {
            const result = await getGarageCustomerType(params);
            this.listItem = result.data || [];
            this.pagination.total = result.totalElement;
        },
    },
});
</script>
