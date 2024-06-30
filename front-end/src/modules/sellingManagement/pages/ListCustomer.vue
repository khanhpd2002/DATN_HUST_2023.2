<template>
    <div>
        <ACCDTable
            v-if="rowData.length > 0"
            :columns="columns"
            :rowData="rowData"
        >
        </ACCDTable>
    </div>
</template>
<script setup lang="ts">
import {
    getCustomersByGarageId,
    getGarageCustomerType,
    updateGarageCustomerById,
    addNewGarageCustomer,
    getGarageCars,
    getGarageCustomerInfo,
} from "@/modules/sellingManagement/api/index";
import { ref, onMounted } from "vue";
import {useI18n} from "@/composables/useI18n";
const MAX_INTEGER = import.meta.env.VITE_MAX_INTEGER;
const PAGE_SIZE = 10;
interface Car {
    id: Number;
    carName: String;
    carBrandId: Number;
    carModelId: Number;
    carYearId: Number;
    carEngineId: Number;
    licensePlate: String;
    vinNumber: String;
    garageId: Number;
    driverCarId: Number;
}
interface Option {
    value: String;
    id: Number;
}
const {$t} = useI18n();
const columns = [
    {
        key: "customerTypeName",
        headerName: $t(
            "module.sellingManagement.customer.columnTable.customerTypeName"
        ),
    },
    {
        key: "fullName",
        headerName: $t(
            "module.sellingManagement.customer.columnTable.fullName"
        ),
    },
    {
        key: "phoneNumber",
        headerName: $t(
            "module.sellingManagement.customer.columnTable.phoneNumber"
        ),
    },
    {
        key: "action",
        headerName: "",
    },
];
const customerTypeOptions = ref([] as any[]);
const customerTypes = ref({} as any);
const listGarageCustomer = ref([] as any[]);
const rowData = ref([] as any);
const carOptions = ref([] as Option[]);
const pagination = ref({
    perPage: PAGE_SIZE,
    total: 0,
    currentPage: 1,
});
const listCars = ref([] as Car[]);
const getCustomerType = async () => {
    const data = await getGarageCustomerType({
        pageSize: MAX_INTEGER,
        pageNumber: 1,
    });
    const listCustomerType = data.data;
    customerTypeOptions.value = listCustomerType.map((item: any) => {
        customerTypes.value[item.id] = item.customerTypeName;
        return {
            text: item.customerTypeName,
            value: item.customerTypeName,

            id: item.id,
        };
    });
};
const getTableRowData = (params: any) => {
    getCustomersByGarageId(params).then((data: any) => {
        listGarageCustomer.value = data.data;
        rowData.value = listGarageCustomer.value.map(
            (item: any, index: number) => {
                return {
                    id: item.id,
                    order:
                        index +
                        1 +
                        PAGE_SIZE * (pagination.value.currentPage - 1),
                    customerTypeName: customerTypes[item.customerTypeId],
                    fullName: item.fullName,
                    phoneNumber: item.phoneNumber,
                    customerTypeId: item.customerTypeId,
                };
            }
        );
        pagination.value.total = data.totalElement;
    });
};
const getCars = async () => {
    const data = await getGarageCars({
        pageSize: MAX_INTEGER,
        pageNumber: 1,
    });
    listCars.value = data.data;
    carOptions.value = listCars.value.map((item: any) => {
        return {
            value: item.carBrandName + " - " + item.licensePlate,
            id: item.id,
        };
    });
};
onMounted(() => {
    getCustomerType().then(() => {
        getTableRowData({
            pageSize: PAGE_SIZE,
            pageNumber: 1,
        });
    });
    getCars();
});
</script>
<style>
.table-primary {
    @apply min-w-full divide-y;
}

.table-primary > thead,
.table-primary > thead th {
    @apply bg-box;
}

.table-primary > thead > tr > th {
    @apply border-y px-6 py-3 text-xs font-semibold uppercase tracking-wider text-typo;
}

table.table-condensed > thead > tr > th {
    @apply px-2 py-2.5;
}

.table-sticky-header .table-primary > thead > tr > th {
    @apply bg-opacity-75 backdrop-blur backdrop-filter;
}

.table-primary > tbody > tr:not(:last-child) td {
    @apply border-b border-line;
}

.table-primary > tbody > tr > td {
    @apply bg-container px-6 py-4 text-sm font-medium text-typo;
}

table.table-condensed > tbody > tr > td {
    @apply px-2 py-1.5;
}

table.table-condensed > tbody > tr > td .link {
    @apply text-base;
}

.table-responsive {
    @apply touch-auto overflow-x-auto;
}

.table-sticky-header {
    @apply max-h-80 overflow-y-auto;
}

.table-sticky-header table > thead > tr > th {
    @apply sticky top-0 z-10;
}

.table-sticky-header table > tbody > tr > .table-sticky-column,
.table-responsive table > tbody > tr > .table-sticky-column,
.table-sticky-header table > tfoot > tr > .table-sticky-column,
.table-responsive table > tfoot > tr > .table-sticky-column {
    @apply relative lg:sticky lg:left-0;
}

.table-sticky-header table > thead > tr > .table-sticky-column,
.table-responsive table > thead > tr > .table-sticky-column {
    @apply lg:sticky lg:left-0;
}

.table-sticky-header table > thead > tr > .table-sticky-column,
.table-responsive table > thead > tr > .table-sticky-column {
    @apply z-20;
}

.table-sticky-header table > tbody > tr > .table-sticky-column,
.table-responsive table > tbody > tr > .table-sticky-column,
.table-sticky-header table > tfoot > tr > .table-sticky-column,
.table-responsive table > tfoot > tr > .table-sticky-column {
    @apply z-10;
}

tr.has-warning > td:first-child:after,
tr.has-warning > td:first-child:before {
    content: "";
    position: absolute;
    left: 0;
    width: auto;
    height: 100%;
    top: 0;
    border-left: 3px solid transparent;
}

tr.has-warning.warning-confirmed > td:first-child:before,
tr.has-warning.warning-confirmed > td:first-child:after {
    border-left: 3px solid rgba(var(--color-danger), 1);
}
</style>
