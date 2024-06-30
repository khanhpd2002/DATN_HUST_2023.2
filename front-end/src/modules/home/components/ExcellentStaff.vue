<template>
    <div class="rounded-md bg-white p-4 h-full">
        <p class="font-bold h-[38px] leading-[38px] whitespace-nowrap">
            {{ $t("module.home.service.excellentStaffs") }}
        </p>

        <div
            v-if="props.data.length > 0"
            class="flex justify-between items-center py-1 border-b-2"
            v-for="(staff, i) in props.data"
            :key="i"
        >
            <div class="flex gap-4 items-center">
                <img :src="logo" alt="avatar" width="50px" />

                <div>
                    <h3 class="font-bold text-base">
                        {{ i + 1 }}. {{ staff.name }}
                    </h3>
                    <p>{{ staff.jobTitle }}</p>
                </div>
            </div>
            <p class="text-base">{{ formatPriceVN(staff.money) }} đ</p>
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
import { onMounted } from "vue";
import { formatPriceVN } from "@/modules/sharedModules/component/constants";
import logo from "@/assets/images/logo_cardoctor.svg";

const { $t } = useI18n();

type ExcellentStaffProps = {
    name: string;
    money: number;
    jobTitle: string;
    image?: string;
};

type DataProps = {
    data: ExcellentStaffProps[];
};

const props = withDefaults(defineProps<DataProps>(), {});


</script>
