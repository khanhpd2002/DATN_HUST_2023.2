<template>
    <Combobox as="div" v-model="lazyValue" :disabled="disabled">
        <ComboboxLabel
            class="block text-sm font-medium leading-6 text-gray-900"
            :class="labelClass"
            ><i
                v-if="isRequired"
                style="font-size: 8px"
                class="fa-solid fa-star-of-life text-red-500 mr-2"
            ></i
            >{{ label }}</ComboboxLabel
        >

        <div class="relative mt-2" :class="`${contentClass}`">
            <ComboboxInput
                class="w-full rounded-md border-0 bg-white py-1.5 pl-3 pr-10 text-gray-900 shadow-sm ring-1 ring-inset focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 placeholder:text-gray-400"
                @change="onChangeInput"
                @click="openOptions"
                :class="`${
                    !isValidate
                        ? 'pr-10 text-red-900 ring-red-300 placeholder:text-red-300 focus:ring-red-500'
                        : 'text-gray-900 shadow-sm ring-gray-300 placeholder:text-gray-400 focus:ring-indigo-600'
                }
                `"
                :display-value="
                    (value: any) =>
                        options.find((o: any) => o.value == value) &&
                        !createForNew
                            ? options.find((o: any) => o.value == value).text
                            : value
                "
                :placeholder="placeholder"
            />
            <ComboboxButton
                ref="comboboxButton"
                class="absolute inset-y-0 right-0 flex items-center rounded-r-md px-2 focus:outline-none"
            >
                <span
                    class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2"
                >
                    <ChevronUpDownIcon
                        class="h-5 w-5 text-gray-400"
                        aria-hidden="true"
                    />
                </span>
            </ComboboxButton>

            <ComboboxOptions
                v-if="true"
                class="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"
            >
                <div
                    v-if="
                        !createForNew &&
                        query !== '' &&
                        filteredOption.length === 0
                    "
                    class="relative cursor-default select-none py-2 px-4 text-gray-700"
                >
                    Nothing found.
                </div>
                <ComboboxOption
                    v-if="
                        !createForNew &&
                        query !== '' &&
                        filteredOption.length === 0
                    "
                >
                </ComboboxOption>
                <ComboboxOption
                    v-if="queryOption && createForNew"
                    :value="queryOption"
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
                                    queryOption.online ? 'Online' : 'Offline'
                                "
                                :class="[
                                    queryOption.online
                                        ? 'bg-green-400'
                                        : 'bg-gray-200',
                                    'inline-block h-2 w-2 flex-shrink-0 rounded-full mr-3',
                                ]"
                            />
                            <span
                                :class="[
                                    selected ? 'font-semibold' : 'font-normal',
                                    'block truncate',
                                    leftCheck && !status ? 'pl-6' : '',
                                ]"
                                >{{ queryOption.text }}</span
                            >
                            <span class="text-sm text-slate-400 ml-2">new</span>
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
                </ComboboxOption>
                <ComboboxOption
                    as="template"
                    v-for="option in filteredOption"
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
                                    selected ? 'font-semibold' : 'font-normal',
                                    'block truncate',
                                    leftCheck && !status ? 'pl-6' : '',
                                ]"
                                >{{ option.text }}</span
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
                </ComboboxOption>
            </ComboboxOptions>
        </div>
    </Combobox>
</template>
<script lang="ts">
import { defineComponent } from "vue";
interface Option {
    id: number;
    text: string;
    value?: string;
    online?: boolean;
}
import {
    Combobox,
    ComboboxButton,
    ComboboxLabel,
    ComboboxOption,
    ComboboxOptions,
    ComboboxInput,
} from "@headlessui/vue";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/vue/20/solid";
export default defineComponent({
    components: {
        Combobox,
        ComboboxButton,
        ComboboxLabel,
        ComboboxOption,
        ComboboxOptions,
        ComboboxInput,
        CheckIcon,
        ChevronUpDownIcon,
    },
    data() {
        return {
            focused: false,
            query: "",
        };
    },
    props: {
        isRequired: {
            default: false as boolean,
        },
        isValidate: {
            default: true as boolean,
        },
        labelClass: {
            default: "" as string,
        },
        contentClass: {
            default: "" as string,
        },
        required: {
            default: false as boolean,
        },
        createForNew: {
            default: false as boolean,
        },
        label: {
            default: "" as string,
        },
        placeholder: {
            default: "Select an option" as string,
        },
        options: {
            default: [] as any[],
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
        disabled: {
            default: false as boolean,
            required: false,
        },
    },
    methods: {
        openOptions() {
            this.focused = true;
            let button = this.$refs.comboboxButton as any;
            button.el.click();
        },
        onChangeInput(e: any) {
            this.$emit("onSearch", e.target.value);
            this.query = e.target.value;
        },
    },
    computed: {
        lazyValue: {
            get() {
                return this.modelValue;
            },
            set(val: any) {
                this.$emit("update:modelValue", val.value);
            },
        },
        listOptions() {
            return this.options as any;
        },
        filteredOption() {
            return this.query === ""
                ? this.listOptions
                : this.listOptions.filter((option: Option) => {
                      if (option?.text == null) return false;
                      return option?.text
                          .toLowerCase()
                          .includes(this.query.toLowerCase());
                  });
        },
        queryOption() {
            {
                return this.query === ""
                    ? null
                    : {
                          id: null,
                          text: this.query,
                          online: true,
                          value: this.query,
                      };
            }
        },
    },
});
</script>
