<template>
    <ACCDModal
        class-width="md:w-[592px]"
        v-model="lazyValue"
        :title="$t('module.inventory.sparePartsInventory.updateForm.code')"
    >
        <!-- <template> -->
        <div class="flex gap-2.5 mb-4 py-4">
            <div class="w-2/5">
                <p class="mb-1">
                    {{
                        $t(
                            "module.inventory.sparePartsInventory.updateForm.code"
                        )
                    }}
                </p>
                <ACCDInputText :disabled="true" v-model="sparePartsForm.code">
                </ACCDInputText>
            </div>
            <div class="w-3/5">
                <p class="mb-1">
                    {{
                        $t(
                            "module.inventory.sparePartsInventory.updateForm.productName"
                        )
                    }}
                </p>
                <ACCDInputText :disabled="true" v-model="sparePartsForm.name">
                </ACCDInputText>
            </div>
        </div>
        <div class="flex">
            <div class="w-1/4">
                <p class="mb-1">
                    {{
                        $t(
                            "module.inventory.sparePartsInventory.updateForm.quantity"
                        )
                    }}
                </p>
                <ACCDInputText
                    v-model="sparePartsForm.quantity"
                    @keypress="validateNumber"
                >
                </ACCDInputText>
            </div>
        </div>
        <!-- </template> -->
        <template #footer>
            <div class="flex justify-end gap-2.5 w-full">
                <ACCDButton
                    @click="onClickBack"
                    type="secondary"
                    size="md"
                    class="w-full"
                >
                    {{ $t("module.inventory.sparePartsInventory.action.back") }}
                </ACCDButton>
                <ACCDButton
                    @click="update"
                    type="primary"
                    size="md"
                    class="w-full"
                >
                    {{ $t("module.inventory.sparePartsInventory.action.save") }}
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { emitter } from "@/utils/mitt";
import {
    getDetailParentSparePart,
    getDetailSparePartsInventory,
    updateSparePartsInventory,
} from "@/modules/inventory/api";
import { $toast } from "@/main";
import {
    validateNumber,
    validatePrice,
} from "@/modules/sharedModules/component/constants";
import { useI18n } from "@/composables/useI18n";
const { $t } = useI18n();

const sparePartsForm = ref({
    code: "",
    name: "",
    quantity: 0,
});
const props = withDefaults(defineProps<ModalProps>(), {
    modelValue: false,
    id: "",
});
const originData = ref({} as any);
type ModalProps = {
    modelValue: boolean;
    id?: string;
};
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
const onClickBack = () => {
    emitter.emit("layout-pages-open-confirmClose", instanceKey);
};
const instanceKey = new Date().getTime();
onMounted(() => {
    emitter.on("pages-layout-on-confirmClose", (ik) => {
        if (ik == instanceKey) {
            lazyValue.value = false;
        }
    });
    getDetailSparePart();
});

const getDetailSparePart = async () => {
    await getDetailParentSparePart(props.id).then((res) => {
        originData.value = res.data;
        sparePartsForm.value.code = res.data.code;
        sparePartsForm.value.name = res.data.name;
        sparePartsForm.value.quantity = res.data.quantity;
    });
};

const update = async () => {
    let data = originData.value;
    data.quantity = sparePartsForm.value.quantity;
    updateSparePartsInventory(props.id, data).then((res) => {
        if (res.code == 1) {
            if (res.code == 1) {
                $toast(
                    $t(
                        "module.inventory.sparePartsInventory.updateForm.toast.updateSuccess"
                    ),
                    true
                );
                lazyValue.value = false;
                emit("refresh");
            } else {
                $toast(
                    $t(
                        "module.inventory.sparePartsInventory.updateForm.toast.updateFailse"
                    ),
                    false
                );
            }
        }
    });
};
</script>
