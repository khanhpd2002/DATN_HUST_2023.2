<template>
    <ACCDModal>
        <template #header>
            <div class="px-6 pt-6">
                <h1 class="text-gray-900 font-semibold text-lg pb-1 border-b">
                    {{ computedTitle }}
                </h1>
            </div>
        </template>
        <template #footer>
            <div class="flex justify-end gap-2.5 w-full">
                <ACCDButton
                    size="sm"
                    variant="fill"
                    type="secondary"
                    class="bg-info-secondary border-none w-1/6"
                    @click="closeDialog"
                >
                    <span class="text-info-base font-medium text-sm">{{
                        $t("module.customerType.action.back")
                    }}</span></ACCDButton
                >
                <ACCDButton
                    v-if="props.state !== EFormState.VIEW"
                    size="sm"
                    type="primary"
                    variant="fill"
                    class="bg-info-secondary border-none w-1/6"
                    @click="onSubmit"
                >
                    <span class="text-white font-medium text-sm">{{
                        $t("module.customerType.action.save")
                    }}</span></ACCDButton
                >
            </div>
        </template>
    </ACCDModal>
</template>

<script setup lang="ts">
import { EFormState } from "@/enums";
type ModalProps = {
    modelValue: boolean;
    state: EFormState | undefined;
    sellSparePartId?: number | string;
};
</script>
