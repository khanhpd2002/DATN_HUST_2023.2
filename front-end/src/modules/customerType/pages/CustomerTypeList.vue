<template>
    <div class="">
        <h1 class="font-semibold text-2xl mb-4">
            {{ $t("module.customerType.title") }}
        </h1>
        <div class="mb-4 flex gap-4">
            <div class="w-1/4">
                <p class="text-sm mb-1">
                    {{ $t("module.customerType.customerTypeName") }}
                </p>
                <ACCDInputText size="md" v-model="filterConfig.name.value"
                    :placeholder="$t('module.customerType.customerTypeName')" />
            </div>
        </div>
        <div class="mb-4 flex flex-row-reverse">
            <div class="w-1/6">
                <ACCDButton @click="onFilter" size="md" type="primary" variant="fill" class="w-full">
                    <span class="text-white font-medium text-sm">{{
                        $t("module.customerType.action.search")
                    }}</span>
                </ACCDButton>
            </div>
            <div class="w-1/6 pr-4">
                <ACCDButton size="md" type="secondary" variant="fill" class="bg-info-secondary border-none w-full" @click="() => {
                            onOpenFormModal();
                        }
                        ">
                    <span class="text-info-base font-medium text-sm">{{
                        $t("module.customerType.action.add")
                    }}</span>
                </ACCDButton>
            </div>
        </div>
        <div>
            <ACCDTable ref="table" :columns="columnData" :rowData="rowData" :tableName="$t('module.customerType.tableName')"
                :pagination="pagination" @changePage="changePage">
                <template #cell-action="{ row, col, field }">
                    <ACCDDropdown>
                        <template v-slot:activator="{ props }">
                            <div class="w-8 cursor-pointer text-center" v-bind="props">
                                <ACCDIcon class="text-base text-black" name="icon-ellipsis-vertical"></ACCDIcon>
                            </div>
                        </template>
                        <div class="rounded-md border w-56 bg-white py-2">
                            <div class="flex px-4 py-2 gap-3 cursor-pointer" @click="() =>
                                    onOpenFormModal(
                                        row.id,
                                        row,
                                        EFormState.EDIT
                                    )
                                ">
                                <ACCDIcon class="text-sm text-gray-700" name="icon-pen-to-square"></ACCDIcon>
                                <span class="text-sm text-gray-700">
                                    {{ $t("module.customerType.action.edit") }}
                                </span>
                            </div>
                            <hr />
                            <div class="flex px-4 py-2 gap-3 cursor-pointer" @click="() =>
                                    onOpenFormModal(
                                        row.id,
                                        row,
                                        EFormState.VIEW
                                    )
                                ">
                                <ACCDIcon class="text-sm text-gray-700" name="icon-file-contract"></ACCDIcon>
                                <span class="text-sm text-gray-700">
                                    {{ $t("module.customerType.action.view") }}
                                </span>
                            </div>
                        </div>
                    </ACCDDropdown>
                </template>
            </ACCDTable>
        </div>
        <CustomerTypeForm ref="CustomerTypeFormRef" @refresh="onRefreshData" />
    </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import { getCustomerTypes } from "@/modules/customerType/api";
import { EFormState } from "@/enums";
import { ICustomerType } from "@/types";
import CustomerTypeForm from "@/modules/customerType/pages/CustomerTypeForm.vue";
import { useI18n } from "@/composables/useI18n";

const PAGE_SIZE = 10;
const listItem = ref([]);
const pagination = ref({
    perPage: 10,
    total: 0,
    currentPage: 1,
});
const { $t } = useI18n();
const filterConfig = ref({
    name: {
        value: "",
    },
});
const columnData = [
    {
        key: "stt",
        headerName: $t("module.customerType.stt"),
    },
    {
        key: "customerTypeName",
        headerName: $t("module.customerType.customerTypeName"),
    },
    {
        key: "description",
        headerName: $t("module.customerType.description"),
    },
    {
        key: "action",
        headerName: $t("module.customerType.action.action"),
        align: "center",
    },
];
const CustomerTypeFormRef = ref<InstanceType<typeof CustomerTypeForm>>();
const onOpenFormModal = (
    id?: number,
    params?: ICustomerType,
    state?: EFormState
) => {
    CustomerTypeFormRef?.value?.openDialog(id, params, state);
};
const contextActions = [
    {
        icon: "EllipsisVerticalIcon",
        name: $t("module.customerType.action.view"),
        action: (params: any) =>
            onOpenFormModal(params.id, params, EFormState.VIEW),
    },
    {
        icon: "EllipsisVerticalIcon",
        name: $t("module.customerType.edit"),
        action: (params: any) =>
            onOpenFormModal(params.id, params, EFormState.EDIT),
    },
];
const rowData = computed(() => {
    return (
        listItem.value?.map((item: ICustomerType, index: number) => {
            return {
                ...item,
                stt:
                    index + 1 + PAGE_SIZE * (pagination.value?.currentPage - 1),
            };
        }) || ([] as ICustomerType[])
    );
});
const getTableRowData = async () => {
    const _params = {
        name: filterConfig.value.name.value,
        pageSize: pagination.value.perPage,
        pageNumber: pagination.value.currentPage,
    };
    const result = await getCustomerTypes(_params);
    listItem.value = result.data || [];
    pagination.value.total = result.totalElement;
};
const onRefreshData = async () => {
    listItem.value = [];
    await getTableRowData();
};
const changePage = async (val: any) => {
    pagination.value.currentPage = val.currentPage;
    await getTableRowData();
};
const onFilter = async () => {
    pagination.value.currentPage = 1;
    await getTableRowData();
};
onMounted(async () => {
    await getTableRowData();
});
</script>
