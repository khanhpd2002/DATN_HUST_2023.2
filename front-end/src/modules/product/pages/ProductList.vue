<template>
    <div>
        <CDTable
            v-if="listProducts.length === totalItem"
            ref="table"
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
        <ProductForm ref="ProductFormRef" @refresh="onRefreshData" />
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getListProducts } from "@/modules/product/api";
import { useProducts } from "@/modules/product/store";
import ProductForm from "@/modules/product/pages/ProductForm.vue";

const PAGE_SIZE = 10;

export default defineComponent({
    name: "ListCustomer",
    components: { ProductForm },
    setup() {
        return {};
    },
    computed: {
        rowData(): any {
            return (
                this.listProducts?.map((item: any, index: number) => {
                    return {
                        ...item,
                        order:
                            index +
                            1 +
                            PAGE_SIZE * (this.pagination.currentPage - 1),
                        sparePartType: item.sparePartType ? this.$t(
                            `module.product.sparePartTypes.${
                                item.sparePartType
                            }`
                        ) : '',
                    };
                }) || []
            );
        },
    },
    data() {
        return {
            pagination: {
                perPage: PAGE_SIZE,
                total: 0,
                currentPage: 1,
            },
            tableName: this.$t("module.product.list"),
            // rowData: [] as Array<object>,
            listProducts: [] as any,
            columnData: [
                {
                    field: "order",
                    headerName: this.$t("module.product.order"),
                },
                {
                    field: "code",
                    headerName: this.$t("module.product.code"),
                },
                {
                    field: "name",
                    headerName: this.$t("module.product.name"),
                    maxLength: 255,
                },
                {
                    field: "carBrandName",
                    headerName: this.$t("module.product.carBrand"),
                },
                {
                    field: "carModelName",
                    headerName: this.$t("module.product.carModel"),
                },
                {
                    field: "carYearName",
                    headerName: this.$t("module.product.carYear"),
                },
                {
                    field: "carEngineName",
                    headerName: this.$t("module.product.carEngine"),
                },
                {
                    field: "color",
                    headerName: this.$t("module.product.color"),
                },
                {
                    field: "purchasePrice",
                    headerName: this.$t("module.product.purchasePrice"),
                },
                {
                    field: "sparePartType",
                    headerName: this.$t("module.product.sparePartType"),
                },
                {
                    field: "action",
                    headerName: this.$t(
                        "module.sellingManagement.customer.columnTable.action"
                    ),
                },
            ],
            selectModelValue: {
                value: undefined,
                id: undefined,
            },
            contextActions: [
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t(
                        "module.sellingManagement.customer.action.view"
                    ),
                    action: (params: any) =>
                        this.onOpenFormModal(
                            params?.id,
                            params?.inventoryId,
                            true
                        ),
                },
                {
                    icon: "EllipsisVerticalIcon",
                    name: this.$t("module.product.editAction"),
                    action: (params: any) =>
                        this.onOpenFormModal(params?.id, params?.inventoryId),
                },
            ],
            tableActions: {
                name: this.$t(
                    "module.sellingManagement.customer.action.addNew"
                ),
                action: () => this.onOpenFormModal(),
            },
            totalItem: -1,
        };
    },
    created() {
        this.getTableRowData({
            pageSize: PAGE_SIZE,
            pageNumber: 1,
        });
    },
    methods: {
        onRefreshData() {
            this.listProducts = [];
            this.getTableRowData({
                pageSize: PAGE_SIZE,
                pageNumber: 1,
            });
        },
        onOpenFormModal(id?: number, inventoryId?: number, isShow?: boolean) {
            (
                this.$refs.ProductFormRef as InstanceType<typeof ProductForm>
            ).openDialog(id, inventoryId, isShow);
        },
        changePage(val: any) {
            this.totalItem = -1;
            this.pagination.currentPage = val.currentPage;
            this.getTableRowData({
                pageNumber: val.currentPage,
                pageSize: PAGE_SIZE,
            });
        },
        async getTableRowData(params: any) {
            const result = await getListProducts(params);
            this.listProducts = result.data || [];
            this.totalItem = this.listProducts.length;
            this.pagination.total = result.totalElement;
        },
        getPartProductCode(id: number): string {
            id = id ? id : 0;
            return (
                useProducts().listSparePartType.find((item) => item.id === id)
                    ?.code || ""
            );
        },
    },
});
</script>
