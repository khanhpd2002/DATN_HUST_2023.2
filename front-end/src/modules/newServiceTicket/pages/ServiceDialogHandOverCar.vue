<template>
    <ACCDModal :show-footer="false" class-width="md:w-[600px]">
        <div class="flex gap-3 bg-layout-primary justify-center">
            <span class="text-[28px] font-bold">Bàn giao xe thành công</span>
        </div>

        <div class="w-[100px] border-b-4 border-primary my-4 mx-auto"></div>
        <p class="text-center font-medium pb-6">
        Phiếu dịch vụ đã được xử lý thành công
        </p>

        <template #footer>
            <div class="flex justify-center gap-2 w-full">
                <ACCDButton size="sm" @click="handleCancel" type="secondary" class="px-6 py-3">
                    <span class="text-base"
                        >{{ $t("module.newServiceTicket.action.close") }}
                    </span>
                </ACCDButton>
                <ACCDButton
                    type="primary"
                    size="sm"
                    @click="handleSave"
                    class="px-6 py-3"
                >
                    <span class="text-base">
                        {{ $t("module.newServiceTicket.action.createNewService") }}</span
                    >
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
</template>
<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { EStepperTicket } from "@/enums";
import router from "@/router";
import { watch } from "vue";
import { ref } from "vue";
import { emitter } from "@/utils/mitt";


const { $t } = useI18n();
type ModalProps = {
    stepper: number;
    id: string;
   
};

const props = withDefaults(defineProps<ModalProps>(), {
    stepper: EStepperTicket.RECEIVE,
    id: "",
   
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
    router.push('/app/sell/new-service-tickets')
    setTimeout(() => {
        emitter.emit("on-add-newServiceTicket");
    }, 200);
    
}

</script>

<style lang="scss">
.title {
    font-size: 16px;
}
</style>
