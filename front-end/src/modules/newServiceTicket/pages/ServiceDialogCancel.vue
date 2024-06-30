<template>
    <ACCDModal :show-footer="false" class-width="md:w-[600px]">
        <div class="flex gap-3 bg-layout-primary justify-center">
            <span class="text-[28px] font-bold">{{
                $t("module.newServiceTicket.title.cancel")
            }}</span>
        </div>
        <div class="w-[100px] border-b-4 border-error my-4 mx-auto"></div>
        <p class="text-center font-medium pb-6">
            Bạn có muốn hủy phiếu dịch vụ này? Sau khi hủy sẽ không thể hoàn tác
        </p>

        <template #footer>
            <div class="flex justify-center gap-2 w-full">
                <ACCDButton size="sm" @click="handleCancel" class=" px-6 py-3" type="secondary">
                    {{ $t("module.newServiceTicket.title.no") }}
                </ACCDButton>
                <ACCDButton
                    type="primary"
                    size="sm"
                    @click="handleSave"
                    class="bg-error border-none px-6 py-3"
                >
                    {{ $t("module.newServiceTicket.title.yes") }}
                </ACCDButton>
            </div>
        </template>
    </ACCDModal>
</template>
<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { cancelRepair } from "@/modules/newServiceTicket/api";
import router from "@/router";
import { $toast } from "@/main";
const { $t } = useI18n();
type ModalProps = {
    id: string;
};

const props = withDefaults(defineProps<ModalProps>(), {
    id: "",
});

// watch(() => props.stepper, () => { })

const emit = defineEmits<{
    (e: "close"): void;
}>();

const handleCancel = () => {
    emit("close");
};
const handleSave = async () => {
    const res = await cancelRepair(props.id);
    if (res.code == 1) {
        router.push("/app/sell/new-service-tickets");
    } else {
        $toast(res.message, false);
    }
};
</script>

<style lang="scss">
.title {
    font-size: 16px;
}
</style>
