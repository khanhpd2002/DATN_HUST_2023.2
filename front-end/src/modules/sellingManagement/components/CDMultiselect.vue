<template>
    <Listbox as="div" v-model="lazyValue" :by="(a: any, b: any) => {
            return a == b?.id || a?.id == b;
        }
        " multiple>
        <ListboxLabel :class="labelClass" class="flex text-sm font-medium leading-6 text-gray-900"><i v-if="isRequired"
                style="font-size: 8px" class="fa-solid fa-star-of-life text-red-500 mr-2"></i>{{ label }}</ListboxLabel>
        <div class="relative mt-2" :class="contentClass">
            <ACCDDropdown :close-on-content-click="false">
                <template v-slot:activator="{ props }">
                    <ListboxButton v-bind="props"
                        class="flex flex-wrap gap-x-2 gap-y-1.5 relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-600 sm:text-sm sm:leading-6 disabled:cursor-not-allowed disabled:bg-gray-50 disabled:text-gray-500 disabled:ring-gray-200"
                        :disabled="disabled">
                        <span class="block truncate font-light border rounded px-2 py-1" v-for="item in modelValue">{{
                            options.find((option) => option.id === item)
                                ?.value
                        }}
                        </span>
                        <span class="text-gray-400" v-if="modelValue?.length <= 0">{{ placeholder }}</span>
                        <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                            <ChevronUpDownIcon class="h-5 w-5 text-gray-400" aria-hidden="true" />
                        </span>
                    </ListboxButton>
                </template>
                <transition leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100"
                    leave-to-class="opacity-0">
                    <ListboxOptions
                        class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                        <ListboxOption @click="() => { }" as="template" v-for="option in options" :key="option.id"
                            :value="option" v-slot="{ active, selected }">
                            <li :class="[
                                active
                                    ? 'bg-indigo-600 text-white'
                                    : 'text-gray-900',
                                'relative cursor-default select-none py-2 pl-3 pr-9',
                            ]">
                                <span class="flex items-center">
                                    <span v-if="status" :aria-label="option.online ? 'Online' : 'Offline'
                                        " :class="[
        option.online
            ? 'bg-green-400'
            : 'bg-gray-200',
        'inline-block h-2 w-2 flex-shrink-0 rounded-full mr-3',
    ]" />
                                    <span :class="[
                                        selected
                                            ? 'font-semibold'
                                            : 'font-normal',
                                        'block truncate',
                                        leftCheck && !status ? 'pl-6' : '',
                                    ]">{{ option.value }}</span>
                                </span>
                                <span :class="[
                                    active
                                        ? 'text-white'
                                        : 'text-indigo-600',
                                    'absolute inset-y-0 flex items-center pr-4',
                                    leftCheck && !status
                                        ? 'left-2'
                                        : 'right-0',
                                ]">
                                    <!-- <<CheckIcon class="h-5 w-5" aria-hidden="true" />> -->
                                    <CDCheckbox :modelValue="selected" />
                                </span>
                            </li>
                        </ListboxOption>
                    </ListboxOptions>
                </transition>
            </ACCDDropdown>
        </div>
    </Listbox>
</template>

<script lang="ts">
import {
    Listbox,
    ListboxButton,
    ListboxLabel,
    ListboxOption,
    ListboxOptions,
} from "@headlessui/vue";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";
import CDCheckbox from "./CDCheckbox.vue";
import { defineComponent } from "vue";

export default defineComponent({
    components: {
        CheckIcon,
        ChevronUpDownIcon,
        CDCheckbox,
        Listbox,
        ListboxButton,
        ListboxLabel,
        ListboxOption,
        ListboxOptions,
    },
    data() {
        return {
            loading: false,
            labelOutside: true,
            multiple: true,
        };
    },
    props: {
        status: {
            default: false as boolean,
        },
        leftCheck: {
            default: false as boolean,
        },
        getterFormatter: {
            default: null as any,
        },
        setterFormatter: {
            default: null as any,
        },
        labelClass: {
            default: "" as string,
        },
        isRequired: {
            default: false as boolean,
        },
        contentClass: {
            default: "" as string,
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

        /**
         * Quy định giá trị được chọn
         */
        modelValue: {
            default: null as any,
        },
    },
    computed: {
        lazyValue: {
            get() {
                return this.modelValue;
            },
            set(val: any[]) {
                let result = val.map((item) => {
                    if (item?.id) {
                        return item.id;
                    } else {
                        return item;
                    }
                });
                this.$emit("update:modelValue", result);
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

<style scoped lang="scss"></style>
