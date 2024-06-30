<template>
    <div class="rounded-md bg-white p-4 h-full">
        <p class="font-bold h-[46px] leading-[46px] whitespace-nowrap">
            {{ $t("module.home.service.title") }}
        </p>

        <div
            v-if="props.data.length > 0"
            class="flex justify-between p-4 border-b-2"
            v-for="(service, i) in props.data"
            :key="i"
        >
            <h3 class="font-bold text-base">{{ i + 1 }}. {{ service.name }}</h3>
            <p class="text-base">{{ formatPriceVN(service.money) }} đ</p>
        </div>

        <div v-else class="relative h-full">
            <p class="no-data">
                {{ $t("module.home.service.noData") }}
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useI18n } from "@/composables/useI18n";
import { formatPriceVN } from "@/modules/sharedModules/component/constants";

const { $t } = useI18n();

type ServicePopularProps = {
    name: string;
    money: number;
};

type DataProps = {
    data: ServicePopularProps[];
};

const props = withDefaults(defineProps<DataProps>(), {});
</script>

<style lang="scss">
.no-data {
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>
