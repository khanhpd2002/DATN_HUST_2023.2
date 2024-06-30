<template>
    <CDDialog
        :title="$t(`module.product.${id ? 'edit' : 'create'}`)"
        @closeDialog="closeDialog"
        width="xs:w-full lg:w-2/4"
        v-if="isOpen"
    >
        <template #content>
            <div class="grid lg:grid-cols-6 gap-2">
                <CDSelect
                    class="col-span-3"
                    v-model="form.type"
                    :model-value="form.type"
                    :label="$t('module.product.type')"
                    :placeholder="$t('module.product.type')"
                    :options="listServiceTypes"
                    required
                    :disabled="!!id || isShow"
                    @update:modelValue="onChangeType"
                />
                <CDSelect
                    class="col-span-3"
                    v-model="form.inventoryId"
                    :model-value="form.inventoryId"
                    :label="$t('module.product.inventory')"
                    :placeholder="$t('module.product.inventory')"
                    :options="listInventories"
                    :disabled="!!id || isShow"
                    required
                />
                <CDInput
                    class="col-span-3"
                    v-model="form.code"
                    :model-value="form.code"
                    :label="$t('module.product.code')"
                    :placeholder="$t('module.product.code')"
                    :disabled="!!id || isShow"
                    required
                />
                <CDInput
                    class="col-span-3"
                    v-model="form.name"
                    :model-value="form.name"
                    :label="$t('module.product.name')"
                    :placeholder="$t('module.product.name')"
                    :disabled="isShow"
                    required
                />
                <template v-if="form.type === 1">
                    <CDSelect
                        class="col-span-3"
                        v-model="form.carBrandId"
                        :model-value="form.carBrandId"
                        :label="$t('module.product.carBrand')"
                        :placeholder="$t('module.product.carBrand')"
                        :options="listAllBrands"
                        :disabled="isShow"
                        required
                        @update:modelValue="onChangeCarBrand"
                    />
                    <CDSelect
                        class="col-span-3"
                        v-model="form.carModelId"
                        :model-value="form.carModelId"
                        :label="$t('module.product.carModel')"
                        :placeholder="$t('module.product.carModel')"
                        :options="listAllModels"
                        :disabled="isShow"
                        required
                        @update:modelValue="onChangeCarModel"
                    />
                    <CDSelect
                        class="col-span-3"
                        v-model="form.carYearId"
                        :model-value="form.carYearId"
                        :label="$t('module.product.carYear')"
                        :placeholder="$t('module.product.carYear')"
                        :options="listAllYear"
                        :disabled="isShow"
                        required
                        @update:modelValue="onChangeCarYear"
                    />
                    <CDSelect
                        class="col-span-3"
                        v-model="form.carEngineId"
                        :model-value="form.carEngineId"
                        :label="$t('module.product.carEngine')"
                        :placeholder="$t('module.product.carEngine')"
                        :options="listAllEngines"
                        :disabled="isShow"
                        required
                    />
                    <CDSelect
                        class="col-span-3"
                        v-model="form.sparePartType"
                        :model-value="form.sparePartType"
                        :label="$t('module.product.sparePartType')"
                        :placeholder="$t('module.product.sparePartType')"
                        :options="optionsSparePartType"
                        :disabled="isShow"
                        required
                    />
                    <CDSelect
                        class="col-span-3"
                        v-model="form.distributorId"
                        :model-value="form.distributorId"
                        :label="$t('module.product.distributor')"
                        :placeholder="$t('module.product.distributor')"
                        :options="listDistributors"
                        :disabled="isShow"
                        required
                    />
                    <CDInput
                        class="col-span-3"
                        v-model="form.color"
                        :model-value="form.color"
                        :label="$t('module.product.color')"
                        :placeholder="$t('module.product.color')"
                        :disabled="isShow"
                        required=""
                    />
                    <CDInput
                        class="col-span-3"
                        v-model="form.unit"
                        :model-value="form.unit"
                        :label="$t('module.product.unit')"
                        :placeholder="$t('module.product.unit')"
                        :disabled="isShow"
                        required
                    />
                    <CDInput
                        class="col-span-3"
                        v-model="form.quantity"
                        :model-value="form.quantity"
                        :label="$t('module.product.quantity')"
                        :placeholder="$t('module.product.quantity')"
                        :disabled="isShow"
                        type="number"
                        required
                    />
                    <CDInput
                        class="col-span-3"
                        v-model="form.purchasePrice"
                        :model-value="form.purchasePrice"
                        :label="$t('module.product.purchasePrice')"
                        :placeholder="$t('module.product.purchasePrice')"
                        :disabled="isShow"
                        type="number"
                        required
                    />
                </template>
            </div>
        </template>
        <template #action>
            <div class="flex justify-end mt-4">
                <button
                    @click="closeDialog"
                    type="button"
                    class="block rounded-md px-3 py-2 text-center text-sm text-gray-600 border mr-2"
                >
                    {{ $t("module.product.back") }}
                </button>
                <button
                    v-if="!isShow"
                    @click="onSubmit"
                    type="submit"
                    class="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                    {{ $t("module.product.save") }}
                </button>
            </div>
        </template>
    </CDDialog>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useProducts } from "@/modules/product/store";
import { getCarFeatures } from "@/modules/sellingManagement/api";
import {
    createProduct,
    detailProduct,
    updateProduct,
} from "@/modules/product/api";
import CDSelect from "@/modules/product/components/CDSelect.vue";
export default defineComponent({
    name: "ProductForm",
    components: {
        CDSelect,
    },
    data() {
        return {
            isOpen: false,
            form: {
                code: "",
                name: "",
                carBrandId: undefined,
                carModelId: undefined,
                carYearId: undefined,
                carEngineId: undefined,
                color: "",
                distributorId: undefined,
                inventoryId: undefined,
                purchasePrice: 0,
                unit: "",
                quantity: 0,
                sparePartType: undefined,
                type: undefined,
            },
            listAllBrands: [],
            listAllModels: [],
            listAllEngines: [],
            listAllYear: [],
            id: 0,
            isShow: false,
        };
    },
    computed: {
        optionsSparePartType() {
            return useProducts().listSparePartType?.map((item) => ({
                id: item.id,
                value: this.$t(`module.product.${item.code}`),
            }));
        },
        listInventories() {
            return useProducts().listInventories || [];
        },
        listDistributors() {
            return useProducts().listDistributors || [];
        },
        listServiceTypes() {
            return (
                useProducts().listServiceTypes?.map((item) => ({
                    id: item.id,
                    value: this.$t(`module.product.${item.code}`),
                })) || []
            );
        },
    },
    async created() {
        await Promise.all([
            useProducts().getListInventories(),
            useProducts().getListDistributors(),
            this.onGetListCarBrand(),
        ]);
    },
    methods: {
        onChangeType() {
            if (this.form.type === 2) {
                this.form = {
                    ...this.form,
                    carBrandId: undefined,
                    carModelId: undefined,
                    carYearId: undefined,
                    carEngineId: undefined,
                    color: "",
                    distributorId: undefined,
                    purchasePrice: 0,
                    unit: "",
                    quantity: 0,
                    sparePartType: undefined,
                };
            }
        },
        onChangeCarBrand(e: number) {
            this.listAllModels = [];
            this.listAllYear = [];
            this.listAllEngines = [];
            this.form.carModelId = undefined;
            this.form.carYearId = undefined;
            this.form.carEngineId = undefined;
            this.onGetListCarModel(e);
        },
        onChangeCarModel(e: number) {
            this.listAllYear = [];
            this.listAllEngines = [];
            this.form.carYearId = undefined;
            this.form.carEngineId = undefined;
            this.onGetListCarYear(e);
        },
        onChangeCarYear(e: number) {
            this.listAllEngines = [];
            this.form.carEngineId = undefined;
            this.onGetListCarEngine(e);
        },
        async onGetListCarBrand() {
            const result = await getCarFeatures({
                type: "brand",
            });
            this.listAllBrands =
                result?.data?.data?.brands?.map((item: any) => ({
                    ...item,
                    value: item.title,
                })) || [];
        },
        async onGetListCarModel(brand_id: number) {
            const result = await getCarFeatures({
                type: "model",
                brand_id,
            });
            this.listAllModels =
                result?.data?.data?.car_models?.map((item: any) => ({
                    ...item,
                    value: item.title,
                })) || [];
        },
        async onGetListCarYear(model_id: number) {
            const result = await getCarFeatures({
                type: "year",
                model_id,
            });
            this.listAllYear =
                result?.data?.data?.car_years?.map((item: any) => ({
                    ...item,
                    value: item.title,
                })) || [];
        },
        async onGetListCarEngine(year_id: number) {
            const result = await getCarFeatures({
                type: "engine",
                year_id,
            });
            this.listAllEngines =
                result?.data?.data?.car_engines?.map((item: any) => ({
                    ...item,
                    value: item.title,
                })) || [];
        },
        async openDialog(id?: number, inventoryId?: number, isShow?: boolean) {
            this.isOpen = true;
            this.id = id || 0;
            this.isShow = isShow || false;
            await this.onGetListCarBrand();
            if (id && inventoryId) {
                const result = await detailProduct(inventoryId, id);
                if (result?.code === 1) {
                    this.form = {
                        ...result?.data,
                        carBrandId: result?.data?.carBrandId || 2,
                    };
                    if (this.form.carBrandId) {
                        await this.onGetListCarModel(this.form.carBrandId);
                    }
                    if (this.form.carModelId) {
                        await this.onGetListCarYear(this.form.carModelId);
                    }
                    if (this.form.carYearId) {
                        await this.onGetListCarEngine(this.form.carYearId);
                    }
                }
            }
        },
        closeDialog() {
            this.isOpen = false;
            this.id = 0;
            this.isShow = false;
            this.listAllBrands = [];
            this.listAllModels = [];
            this.listAllEngines = [];
            this.listAllYear = [];
            this.resetForm();
        },
        resetForm() {
            this.form = {
                code: "",
                name: "",
                carBrandId: undefined,
                carModelId: undefined,
                carYearId: undefined,
                carEngineId: undefined,
                color: "",
                distributorId: undefined,
                inventoryId: undefined,
                purchasePrice: 0,
                unit: "",
                quantity: 0,
                sparePartType: undefined,
                type: undefined,
            };
        },
        async onSubmit() {
            const inventoryId = this.form.inventoryId;
            const payload = {
                ...this.form,
            };
            delete payload.inventoryId;
            if (inventoryId) {
                const result = this.id
                    ? await updateProduct(inventoryId, payload, this.id)
                    : await createProduct(inventoryId, payload);
                if (result?.code === 1) {
                    this.$toast(
                        this.$t(
                            `module.product.${
                                this.id ? "editSuccess" : "createSuccess"
                            }`
                        ),
                        true
                    );
                    this.$emit("refresh");
                    this.closeDialog();
                } else {
                    this.$toast(
                        result?.message ||
                            this.$t(
                                `module.product.${
                                    this.id ? "editFailed" : "createFailed"
                                }`
                            ),
                        false
                    );
                }
            }
        },
    },
});
</script>

<style scoped></style>
