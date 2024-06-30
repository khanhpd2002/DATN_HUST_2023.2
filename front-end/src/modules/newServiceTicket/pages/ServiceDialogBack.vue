<template>
    <ACCDModal :show-footer="false" class-width="md:w-[325px]">
        <div class="flex gap-3 bg-layout-primary">
            <span class="text-2xl">{{
                $t("module.newServiceTicket.title.save")
            }}</span>
        </div>
        <template #footer>
            <div class="flex justify-end gap-2 w-full">
                <ACCDButton size="sm" @click="handleCancel" class="w-1/2">
                    {{ $t("module.newServiceTicket.title.no") }}
                </ACCDButton>
                <ACCDButton
                    type="primary"
                    size="sm"
                    @click="handleSave"
                    class="w-1/2"
                >
                    {{ $t("module.newServiceTicket.title.yes") }}
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
</template>
<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { EStepperTicket } from "@/enums";
import { createDiagnoses } from "@/modules/newServiceTicket/api";
import router from "@/router";
import { watch } from "vue";
import { ref } from "vue";
import { onMounted } from "vue";

const { $t } = useI18n();
type ModalProps = {
    stepper: number;
    id: string;
    description?: string;
};

const props = withDefaults(defineProps<ModalProps>(), {
    stepper: EStepperTicket.RECEIVE,
    id: "",
    description: "",
});

const currentStep = ref(0);

watch(
    () => props.stepper,
    (val) => {
        currentStep.value = val;
    }
);

const emit = defineEmits<{
    (e: "close"): void;
    (e: "change-stepper", stepper: number): void;
}>();

const handleCancel = () => {
    emit("close");
};

const handleSave = async () => {
    if (currentStep.value == EStepperTicket.RECEIVE) {
        const params = { description: props.description };
        const res = await createDiagnoses(params, props.id, false);
        if (res.code == 1) {
            setTimeout(() => {
                router.push("/app/sell/new-service-tickets");
            }, 500);
        }
    } else if (currentStep.value == EStepperTicket.SELL_PRICE) {
        emit("change-stepper", EStepperTicket.SELL_PRICE);
        setTimeout(() => {
            router.push("/app/sell/new-service-tickets");
        }, 500);
        emit("close");
    } else if (currentStep.value == EStepperTicket.PROCEED) {
        setTimeout(() => {
            router.push("/app/sell/new-service-tickets");
        }, 500);

        emit("close");
    } else if (currentStep.value == EStepperTicket.PAYMENT) {
        setTimeout(() => {
            router.push("/app/sell/new-service-tickets");
        }, 500);
        emit("close");
    } else if (currentStep.value == EStepperTicket.HAND_OVER) {
        setTimeout(() => {
            router.push("/app/sell/new-service-tickets");
        }, 500);
        emit("close");
    } else {
        setTimeout(() => {
            router.push("/app/sell/new-service-tickets");
        }, 500);
        emit("close");
    }
};
</script>

<style lang="scss">
.title {
    font-size: 16px;
}
</style>
