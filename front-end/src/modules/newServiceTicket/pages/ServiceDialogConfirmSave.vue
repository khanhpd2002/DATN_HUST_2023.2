<template>
    <ACCDModal :show-footer="false" class-width="md:w-[600px]">
        <div class="flex gap-3 bg-layout-primary justify-center">
            <span class="text-[28px] font-bold">{{
                $t("module.newServiceTicket.title.save")
            }}</span>
        </div>

        <div class="w-[100px] border-b-4 border-primary my-4 mx-auto"></div>
        <p class="text-center font-medium pb-6">
            Bạn có muốn lưu lại các thông tin đã thay đổi?
        </p>

        <template #footer>
            <div class="flex justify-center gap-2 w-full">
                <ACCDButton size="sm" @click="handleCancel" type="secondary" class="px-6 py-3">
                    <span class="text-base"
                        >{{ $t("module.newServiceTicket.title.no") }}
                    </span>
                </ACCDButton>
                <ACCDButton
                    type="primary"
                    size="sm"
                    @click="handleSave"
                    class="px-6 py-3"
                >
                    <span class="text-base">
                        {{ $t("module.newServiceTicket.title.yes") }}</span
                    >
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
</template>
<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { createDiagnoses } from "@/modules/newServiceTicket/api";
import router from "@/router";
import { EStepperTicket } from "@/enums";
import { ref } from "vue";
import { watch } from "vue";
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

const emit = defineEmits<{
     (e: "change-stepper", stepper: number): void;
    (e: "close"): void;
}>();

const currentStep = ref(0);

watch(
    () => props.stepper,
    (val) => {
        currentStep.value = val;
    }
);

const handleCancel = () => {
    emit("close");
    router.push("/app/sell/new-service-tickets");
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
