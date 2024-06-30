<template>
    <div>
        <label for="email" :class="labelClass" class="block text-sm font-medium leading-6 text-gray-900">{{ label }}</label>
        <div :class="contentClass">
            <ACCDDropdown :close-on-content-click="false">
                <template v-slot:activator="{ props }">
                    <CDInput :disabled="disabled" :placeholder="placeholder" v-model="modelValue" @focus="onInputFocus"
                        :readonly="true" v-bind="props" />
                </template>

                <div :style="{ display: showDatePicker ? 'flex' : 'none' }">
                    <v-date-picker :min="min" :max="max" v-model="lazyValue" @click:save="showDatePicker = false" />
                </div>
            </ACCDDropdown>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { VDatePicker } from "vuetify/labs/VDatePicker";
import "@vuepic/vue-datepicker/dist/main.css";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
dayjs.extend(customParseFormat);
export default defineComponent({
    data() {
        return {
            showDatePicker: false,
        };
    },
    props: {
        min: {
            default: null as any,
        },
        max: {
            default: null as any,
        },
        placeholder: {
            default: "" as string,
        },
        labelClass: {
            default: "" as string,
        },
        contentClass: {
            default: "" as string,
        },
        modelValue: {
            default: undefined as any,
        },
        label: {
            default: "" as string,
        },
        disabled: {
            default: false as boolean,
        },
        formatter: {
            default: "YYYY-MM-DD" as string,
        },
        formatterDisplay: {
            default: null as any,
        },
    },
    computed: {
        textDisplay() {
            if (this.formatterDisplay) {
                return this.formatterDisplay(this.modelValue);
            }
            return this.modelValue;
        },
        lazyValue: {
            get() {
                if (!this.modelValue) {
                    const date = new Date();
                    return [date];
                } else {
                    let date = dayjs(this.modelValue, this.formatter).toDate();
                    return [date];
                }
            },
            set(val: any) {
                let date = dayjs(val).format(this.formatter);
                this.$emit("update:modelValue", date);
            },
        },
    },
    methods: {
        onInputFocus() {
            this.showDatePicker = true;
        },
    },
    components: {
        VDatePicker,
    },
});
</script>
<style lang="scss">
.v-picker-title {
    display: none;
}

.v-date-picker-header {
    display: none;
}

.v-btn__content {
    // @apply text-gray-400;
    font-size: 12px;
}
</style>
