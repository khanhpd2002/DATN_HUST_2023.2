<template>
    <div class="flex flex-col lg:flex-row md:flex-row" :class="wrapClass">
        <slot />
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
type WrapProps = {
    justify?: string;
    align?: string;
    gap?: string;
};

const props = withDefaults(defineProps<WrapProps>(), {
    justify: "",
    align: "",
    gap: "4",
});

const wrapClass = computed(() => {
    let result = {} as any;
    if (props.justify) {
        result["justify-" + props.justify] = true;
    }
    if (props.align) {
        result["items-" + props.align] = true;
    }
    if (props.gap) {
        result["gap-" + props.gap] = true;
    }
    return result;
});
</script>
