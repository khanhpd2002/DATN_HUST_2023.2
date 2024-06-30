<template>
    <div
        :style="{ background: props.data.background, color: props.data.color }"
        class="rounded-lg p-4"
        :class="props.data.width"
    >
        <h3 class="font-bold text-xl">{{ props.data.title }}</h3>
        <p class="text-3xl font-bold my-3">
            {{ formatPriceVN(+props.data.result) }}
        </p>

        <span
            v-if="props.data.percent"
            :style="{
                borderRadius: '12px',
                border: `1px solid ${props.data.color}`,
                padding: '3px 8px',
            }"
            class="text-lg"
            >{{ props.data.percent }}
        </span>
    </div>
</template>
<script setup lang="ts">
import { formatPriceVN } from "@/modules/sharedModules/component/constants";

type ResultType = {
    id: number;
    color: string;
    background: string;
    title: string;
    result: string | number;
    percent?: string | number;
    width: string;
};
type ResultProps = {
    data: ResultType;
};
const props = withDefaults(defineProps<ResultProps>(), {});
</script>

<style lang="scss">
.percent {
    border-radius: 30px;
}
</style>
