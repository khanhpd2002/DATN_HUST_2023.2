<template>
    <div class="relative flex items-start py-2">
        <label
            class="font-medium text-gray-900 cursor-pointer flex"
            :class="{ 'flex-row-reverse justify-between w-full': right }"
        >
            <div class="flex h-6 items-center">
                <input
                    v-model="lazyValue"
                    :id="`checkbox-${value}`"
                    aria-describedby="comments-description"
                    :name="`checkbox-${value}`"
                    type="checkbox"
                    :checked="checked"
                    class="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-600 cursor-pointer"
                />
            </div>
            <div
                class="ml-3 text-sm leading-6"
                :class="{ flex: inline, 'ml-0': right }"
            >
                {{ label }}
                <p
                    id="comments-description"
                    class="text-gray-500 font-normal"
                    :class="{ 'ml-1.5': inline }"
                >
                    {{ description }}
                </p>
            </div>
        </label>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    props: {
        label: {
            default: "" as string,
        },
        value: {
            default: null as any,
        },
        modelValue: {
            default: false as boolean,
            required: false,
        },
        description: {
            default: "" as string,
            required: false,
        },
        checked: {
            default: false as boolean,
            required: false,
        },
        inline: {
            default: false as boolean,
            required: false,
        },
        right: {
            default: false as boolean,
            required: false,
        },
    },
    data() {
        return {};
    },
    computed: {
        lazyValue: {
            get() {
                return this.modelValue;
            },
            set(value: boolean) {
                this.$emit("update:modelValue", value);
            },
        },
    },
    emits: ["update:modelValue"],
    mounted() {},
});
</script>
<style lang="scss" scoped>
input[type="checkbox"] {
    appearance: auto !important;
}
</style>
