<template>
    <Listbox as="div">
        <ListboxLabel
            class="block text-sm font-medium leading-6 text-gray-900"
            >{{ label }}</ListboxLabel
        >
        <div class="relative mt-2">
            <ListboxButton
                class="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-600 sm:text-sm sm:leading-6 disabled:cursor-not-allowed disabled:bg-gray-50 disabled:text-gray-500 disabled:ring-gray-200"
            >
                <span class="flex items-center">
                    <span
                        class="block truncate text-sm"
                        :class="{
                            ' text-gray-400': !options?.find(
                                (a) => a.id == modelValue
                            ),
                        }"
                        >{{
                            options?.find((a) => a.id == modelValue)
                                ? options?.find((a) => a.id == modelValue)
                                      ?.value
                                : placeholder
                        }}
                    </span>
                </span>
                <span
                    class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2"
                >
                    <ChevronUpDownIcon
                        class="h-5 w-5 text-gray-400"
                        aria-hidden="true"
                    />
                </span>
            </ListboxButton>

            <transition
                leave-active-class="transition ease-in duration-100"
                leave-from-class="opacity-100"
                leave-to-class="opacity-0"
            >
                <ListboxOptions
                    class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"
                >
                    <ListboxOption
                        @click="() => onSelect(option)"
                        as="template"
                        v-for="option in options"
                        :key="option.id"
                        :value="option"
                        v-slot="{ active, selected }"
                    >
                        <li
                            :class="[
                                active
                                    ? 'bg-indigo-600 text-white'
                                    : 'text-gray-900',
                                'relative cursor-default select-none py-2 pl-3 pr-9',
                            ]"
                        >
                            <span class="flex items-center">
                                <span
                                    v-if="status"
                                    :aria-label="
                                        option.online ? 'Online' : 'Offline'
                                    "
                                    :class="[
                                        option.online
                                            ? 'bg-green-400'
                                            : 'bg-gray-200',
                                        'inline-block h-2 w-2 flex-shrink-0 rounded-full mr-3',
                                    ]"
                                />
                                <span
                                    :class="[
                                        selected
                                            ? 'font-semibold'
                                            : 'font-normal',
                                        'block truncate',
                                        leftCheck && !status ? 'pl-6' : '',
                                    ]"
                                    >{{ option.value }}</span
                                >
                            </span>
                            <span
                                v-if="selected"
                                :class="[
                                    active ? 'text-white' : 'text-indigo-600',
                                    'absolute inset-y-0 flex items-center pr-4',
                                    leftCheck && !status ? 'left-2' : 'right-0',
                                ]"
                            >
                                <CheckIcon class="h-5 w-5" aria-hidden="true" />
                            </span>
                        </li>
                    </ListboxOption>
                </ListboxOptions>
            </transition>
        </div>
    </Listbox>
</template>

<!-- <script setup lang="ts">

import { ref, computed, watch } from "vue";
interface Option {
    id: number;
    value: string;
    online?: boolean;
}
import {
    Listbox,
    ListboxButton,
    ListboxLabel,
    ListboxOption,
    ListboxOptions,
} from "@headlessui/vue";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";

const props = defineProps({
    label: {
        default: "" as string,
    },
    placeholder: {
        default: "Select an option" as string,
    },
    options: {
        default: undefined as Option[] | undefined,
    },
    modelValue: {
        default: undefined as Option | undefined,
    },
    leftCheck: {
        default: false as boolean,
        required: false,
    },

    status: {
        default: false as boolean,
        required: false,
    },
});

const emit = defineEmits(["update:modelValue"]);

const modelValue = ref(
    props.modelValue !== undefined ? props.modelValue : undefined,
);

watch(
    modelValue,
    (option) => {
        emit("update:modelValue", option);
    },
    {
        immediate: true,
    },
);
</script> -->

<script lang="ts">
import { defineComponent } from "vue";
interface Option {
    id: number;
    value: string;
    online?: boolean;
}
import {
    Listbox,
    ListboxButton,
    ListboxLabel,
    ListboxOption,
    ListboxOptions,
} from "@headlessui/vue";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";

export default defineComponent({
    components: {
        Listbox,
        ListboxButton,
        ListboxLabel,
        ListboxOption,
        ListboxOptions,
    },
    props: {
        label: {
            default: "" as string,
        },
        placeholder: {
            default: "Select an option" as string,
        },
        options: {
            default: undefined as Option[] | undefined,
        },
        modelValue: {
            default: undefined as any,
        },
        leftCheck: {
            default: false as boolean,
            required: false,
        },

        status: {
            default: false as boolean,
            required: false,
        },
    },
    methods: {
        onSelect(val: any) {
            this.$emit("update:modelValue", val.id);
        },
    },
    watch: {
        modelValue(option: any) {
            this.$emit("update:modelValue", option);
        },
    },
});
</script>
