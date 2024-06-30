<template>
    <CDDialog
        @closeDialog="closeDialog"
        width="xs:w-full lg:w-2/4 p-5"
        v-if="isOpen"
    >
        <template #title>
            <h1 class="text-gray-900 font-semibold text-2xl">
                {{ computedTitle }}
            </h1>
        </template>
        <template #content v-if="modalState !== EFormState.DELETE">
            <div class="mt-4 flex flex-col gap-4 w-full justify-center p-1">
                <div>
                    <CDForm :show-footer="false">
                        <CDFormItem
                            :label="$t('module.customerType.customerTypeName')"
                            required
                            :message="
                                v$.item.customerTypeName.$errors[0]?.$message
                            "
                        >
                            <ACCDInputText
                                size="md"
                                :placeholder="
                                    $t('module.customerType.customerTypeName')
                                "
                                v-model="item.customerTypeName"
                                :disabled="modalState === EFormState.VIEW"
                            />
                        </CDFormItem>
                        <CDFormItem
                            :label="$t('module.customerType.description')"
                            required
                            :message="v$.item.description.$errors[0]?.$message"
                        >
                            <ACCDInputText
                                size="md"
                                :placeholder="
                                    $t('module.customerType.description')
                                "
                                v-model="item.description"
                                :disabled="modalState === EFormState.VIEW"
                            />
                        </CDFormItem>
                    </CDForm>
                </div>
            </div>
        </template>
        <template #action>
            <div class="flex justify-end mt-4 space-x-4">
                <ACCDButton
                    size="sm"
                    variant="fill"
                    type="secondary"
                    class="bg-info-secondary border-none w-1/4"
                    @click="closeDialog"
                >
                    <span class="text-info-base font-medium text-sm">{{
                        $t("module.customerType.action.back")
                    }}</span></ACCDButton
                >
                <ACCDButton
                    v-if="
                        modalState !== EFormState.VIEW &&
                        modalState !== EFormState.DELETE
                    "
                    size="sm"
                    type="primary"
                    variant="fill"
                    class="bg-info-secondary border-none w-1/4"
                    @click="onSubmit"
                >
                    <span class="text-white font-medium text-sm">{{
                        $t("module.customerType.action.save")
                    }}</span></ACCDButton
                >
            </div>
        </template>
    </CDDialog>
</template>

<script lang="ts" setup>
import { computed, ref } from "vue";
import { EFormState } from "@/enums";
import type { ICustomerType } from "@/types";
import {
    createCustomerType,
    updateCustomerType,
} from "@/modules/customerType/api";
import { CDForm, CDFormItem } from "@cd/design-system";
import { helpers, required } from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import { useI18n } from "@/composables/useI18n";
import { useToast } from "@/composables/useToast";

const { $t } = useI18n();
const { $toast } = useToast();

const isOpen = ref(false);
const modalState = ref<EFormState>(EFormState.VIEW);
const item = ref({
    customerTypeName: "",
    description: "",
});
const responseErrorMessages = ref("");
const id = ref<number | null>(null);
const computedTitle = computed(() => {
    switch (modalState.value) {
        case EFormState.ADD:
            return $t("module.customerType.create");
        case EFormState.EDIT:
            return $t("module.customerType.edit");
        case EFormState.VIEW:
            return $t("module.customerType.view");
        default:
            return "";
    }
});
const validations = computed(() => {
    return {
        item: {
            customerTypeName: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
            description: {
                required: helpers.withMessage("Thiếu thông tin rồi", required),
                $lazy: true,
            },
        },
    };
});
const v$ = useVuelidate(validations, { item });
const openDialog = (_id?: number, data?: ICustomerType, state?: EFormState) => {
    isOpen.value = true;
    id.value = _id || null;
    modalState.value = state || EFormState.ADD;
    item.value = data ? { ...data } : ({} as ICustomerType);
    v$.value.$reset();
};
defineExpose({
    openDialog,
});
const closeDialog = () => {
    isOpen.value = false;
    resetForm();
};
const resetForm = () => {
    responseErrorMessages.value = "";
    item.value = {} as ICustomerType;
};
const emit = defineEmits<{
    (e: "refresh"): void;
}>();
const onSubmit = async () => {
    const result = await v$.value.$validate();
    if (!result) return;
    // TODO: Đoạn này có thể viết async await cho gọn hơn
    // const response = id.value ? await updateCustomerType(item.value as ICustomerType, id.value) : await createCustomerType(item.value as ICustomerType)
    // const successMessage = id.value ? $t("module.customerType.success.edit") : $t("module.customerType.success.create")
    // if (response.code === 1) {
    //   Xử lý logic
    // } else {
    //   Xử lý logic
    // }

    if (modalState.value === EFormState.ADD) {
        createCustomerType(item.value as ICustomerType).then((res) => {
            if (res.code == 1) {
                $toast($t("module.customerType.success.create"), true);
                emit("refresh");
                closeDialog();
            } else {
                responseErrorMessages.value = res.message || res.msg;
                $toast(responseErrorMessages.value, false);
            }
        });
    }
    if (modalState.value === EFormState.EDIT && id.value) {
        updateCustomerType(item.value as ICustomerType, id.value).then(
            (res) => {
                if (res.code == 1) {
                    $toast($t("module.customerType.success.edit"), true);
                    emit("refresh");
                    closeDialog();
                } else {
                    responseErrorMessages.value = res.message;
                    $toast(responseErrorMessages.value, false);
                }
            }
        );
    }
};
</script>

<style scoped></style>
