<template>
    <div>
        <label for="email" :class="labelClass" class="flex text-sm font-medium leading-6 text-gray-900"><i v-if="isRequired"
                style="font-size: 8px" class="fa-solid fa-star-of-life text-red-500 mr-2"></i>{{ label }}</label>
        <div :class="contentClass">
            <ACCDDropdown>
                <template v-slot:activator="{ props }">
                    <CDInput :disabled="disabled" :placeholder="placeholder" v-model="displayText" :readonly="true"
                        v-bind="props" />
                </template>
                <v-list class="max-h-60">
                    <CDLoading v-if="loadingOptions" />
                    <v-list-item class="cursor-pointer hover:bg-gray-200" v-for="(item, index) in options" :key="index"
                        @click="() => {
                                onSelect(item, index);
                            }
                            ">
                        <v-list-item-title>{{ item.value }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </ACCDDropdown>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import CDInput from "./CDInput.vue";
import CDLoading from "./CDLoading.vue";

export default defineComponent({
    methods: {
        onSelect(item: any, index: number) {
            this.lazyValue = item.id;
        },
    },
    components: {
        CDLoading,
        CDInput,
    },
    data() {
        return {
            loading: false,
            labelOutside: true,
        };
    },
    props: {
        loadingOptions: {
            default: false as boolean,
        },
        contentClass: {
            default: "" as string,
        },
        labelClass: {
            default: "" as string,
        },
        isRequired: {
            default: false as boolean,
        },
        /**
         * Quy định mảng các giá trị để chọn
         */
        options: {
            default: [] as any[],
        },
        /**
         * Quy định hiển thị label bên ngoài select
         */

        /**
         * Quy định label của select
         */
        label: {
            default: "" as string,
        },
        /**
         * Quy định placeholder của select
         */
        placeholder: {
            default: "" as string,
        },
        /**
         * Quy định select bị disable
         */
        disabled: {
            default: false as boolean,
        },
        /**
         * Quy định icon bên trong đầu vào của select
         */
        prependInnerIcon: {
            default: "" as string,
        },
        /**
         * Quy định thông báo lỗi khi nhập liệu
         */
        errorMessages: {
            default: "" as string,
        },
        /**
         * Quy định hiển thị dạng mini single
         */
        miniSingleSelect: {
            default: false as boolean,
        },
        /**
         * Quy định cho phép chọn nhiều giá trị
         */
        multiple: {
            default: false as boolean,
        },
        /**
         * Quy định giá trị được chọn
         */
        modelValue: {
            default: null as any,
        },
    },
    computed: {
        displayText() {
            let selected = this.options.find((a) => {
                return a.id == this.modelValue;
            });
            if (selected) {
                return selected.value;
            }
            return "";
        },
        lazyValue: {
            get() {
                return this.modelValue;
            },
            set(val: any) {
                this.$emit("update:modelValue", val);
            },
        },
    },
    // emits: [
    //     /**
    //      * Xem tất cả các sự kiện được emit của component tại link : https://vuetifyjs.com/en/api/v-select/#events
    //      */
    //     'vuetify-event'
    // ]
});
</script>

<style scoped lang="scss">
.CD-list-item-on-select {
    @apply bg-gray-200;
}

input {
    border: none !important;
    box-shadow: none !important;
}

.v-autocomplete--single .v-field--variant-outlined input {
    top: 0;
    transform: unset !important;
}

.label-out-side {
    line-height: 14px;
}

.CD-select :deep(.v-field__field) {
    // color: map-get($colors, gr-600);
    @apply text-gray-600;
}

.CD-select :deep(.v-field-label:not(.v-field-label--floating)),
.CD-select:not(.v-input--error) :deep(.v-input__details) {
    display: none;
}

.CD-select :deep(.v-field-label--floating) {
    opacity: 1;
    transform: translateY(-70%) !important;
}

.CD-select :deep(.v-field-label--floating),
.label-out-side,
.CD-select :deep(.v-field__input) {
    // color: map-get($colors, gr-900) !important;
    @apply text-gray-900;
}

.CD-select .v-text-field .v-field__input input {
    background: red !important;
}

.CD-select :deep(.v-field__append-inner),
.CD-select :deep(.v-field__prepend-inner),
.CD-select :deep(.v-field__clearable),
.CD-select :deep(.v-icon) {
    // color: map-get($colors, gr-600) !important;
    @apply text-gray-600;
    font-size: 16px;
    padding-top: 0px;
    align-items: center;
}

.CD-select :deep(.v-field__prepend-inner) {
    margin-right: 10px;
}

.CD-select :deep(.v-input__control) {
    width: 100%;
}

//.CD-select :deep(.v-input__control:hover .v-field__outline),
.CD-select :deep(.v-field--focused .v-field__outline) {
    // color: map-get($colors, br-pr);
    @apply border-0;
    color: rgb(79 70 229);
    border-color: rgb(79 70 229);
}

.CD-select :deep(.v-input__control:hover .v-field__outline),
.CD-select :deep(.v-field--error:not(.v-field--disabled) .v-field__outline) {
    --v-field-border-width: 2px;
}

.CD-select :deep(.v-field__outline__end) {
    border-radius: 0 8px 8px 0 !important;
}

.CD-select :deep(.v-field__outline__start) {
    border-radius: 8px 0 0 8px !important;
}

.CD-select :deep(.v-field) {
    // color: map-get($colors, gr-300);
    @apply text-gray-300;
    padding-left: 12px;
}

.CD-select :deep(.v-field__input) {
    min-height: 32px !important;
    padding-left: 0px;
    opacity: 1;
    align-items: center;

    input {
        top: 0;
        border: none;
        box-shadow: none;
        transform: unset !important;
    }
}

.CD-select :deep(.v-field--error:not(.v-field--disabled) .v-field__outline),
.CD-select :deep(.v-messages__message) {
    // color: map-get($colors, re-pr);
    @apply text-gray-500;
}

.CD-select :deep(input) {
    font-size: 14px;
    top: 0px;
    opacity: 1;
    // color: map-get($colors, gr-900);
    @apply text-gray-900;
    height: 100%;
    padding: 0px 4px 0px 0px !important;
}

.CD-select :deep(.v-input__details) {
    padding-top: 2px;
    padding-left: 12px;
    align-items: center;
}

.CD-select :deep(input:focus::placeholder) {
    @apply text-gray-400;
}

.CD-select :deep(input::placeholder) {
    opacity: 1;
    // color: map-get($colors, gr-600);
    @apply text-gray-400;
}

.v-list-item--active {
    // background-color: map-get($colors, blu-50) !important;
    // @apply text-blue-100;
    border: none !important;
}

.v-list-item {
    min-height: 28px !important;
}

.v-list-item--active :deep(.v-list-item__overlay) {
    opacity: 0 !important;
}

.v-list-item :deep(.v-list-item-title) {
    font-size: 14px;
    // color: map-get($colors, gr-900);
    @apply text-gray-900;
}

.CD-select :deep(.v-autocomplete__selection) {
    line-height: 15px;
    align-items: center;
    font-size: 14px;
}

.mini-single-select {
    width: 60px;
}

.mini-single-select :deep(.v-field__input) {
    padding: 4px 0px 4px 6px;
    min-height: 24px !important;
}

.mini-single-select :deep(.v-field) {
    padding-right: 4px;
    padding-left: 0px;
}

.mini-single-select :deep(.v-autocomplete__selection) {
    margin: 0px !important;
    margin-bottom: -1px !important;
}

.mini-single-select :deep(input) {
    margin-top: 0px !important;
    padding-left: 6px !important;
}

.mini-single-select :deep(.v-field__append-inner i) {
    font-size: 12px;
}

.CD-mini-select-item :deep(.v-list-item-title),
.mini-single-select :deep(input),
.mini-single-select :deep(.v-autocomplete__selection) {
    font-size: 12px !important;
}

.CD-select :deep(.v-chip) {
    // background-color: map-get($colors, br-100) !important;
    @apply text-gray-900 border-gray-200;
    border-radius: 8px;
    // border: 1px solid map-get($colors, br-200);

    height: 24px;
    top: 1px;
    padding: 0px 4px;
}

.CD-select :deep(.v-chip__underlay) {
    opacity: 0;
}

.CD-select :deep(.v-chip__close) {
    background: #fff;
    border-radius: 25px;
    margin: 0px 0px 0px 4px !important;
}

.CD-select :deep(.v-field__loader) {
    top: 37px;
}

.CD-select-loading {
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
    // color: map-get($colors, gr-500);
    @apply text-gray-500 border-gray-200;
    background: #ffffff;
    // border: 0.5px solid map-get($colors, gr-200);

    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.2);
    border-radius: 4px;
}

.CD-rotating {
    -webkit-animation: spin 1s infinite linear;
}

@-webkit-keyframes spin {
    0% {
        -webkit-transform: rotate(0deg);
    }

    100% {
        -webkit-transform: rotate(360deg);
    }
}

.CD-select :deep(.v-field__clearable:hover i) {
    // background: map-get($colors, gr-pr) !important;
    @apply text-gray-900;
    border-radius: 28px;
}

.CD-select :deep(.v-field__clearable) {
    opacity: 1;
}

.CD-select :deep(.v-field__append-inner i) {
    margin: 0px !important;
}

.CD-select :deep(.v-autocomplete__selection-text) {
    overflow: hidden;
}</style>
