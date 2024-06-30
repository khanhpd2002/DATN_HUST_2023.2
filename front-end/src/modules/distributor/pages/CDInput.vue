<template>
    <div>
        <label
            :class="labelClass"
            for="CDInput"
            class="flex text-sm font-medium leading-6 text-gray-900"
            ><i
                v-if="required"
                style="font-size: 8px"
                class="fa-solid fa-star-of-life text-red-500 mr-2"
            ></i
            >{{ label }}</label
        >
        <!-- <label for="about" class="block text-sm font-medium leading-6 text-gray-900">{{ label }}</label> -->
        <div
            class="mt-2 relative"
            :class="`${errorComputed ? 'relative rounded-md shadow-sm' : ''} ${
                maxLength !== Infinity ? 'relative' : ''
            }
            ${contentClass}
            `"
        >
            <input
                :type="type"
                name="CDInput"
                id="CDInput"
                v-model="lazyValue"
                class="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 disabled:cursor-not-allowed disabled:bg-gray-50 disabled:text-gray-500 disabled:ring-gray-200"
                :class="[
                    errorComputed
                        ? 'pr-10 text-red-900 ring-red-300 placeholder:text-red-300 focus:ring-red-500'
                        : 'text-gray-900 shadow-sm ring-gray-300 placeholder:text-gray-400 focus:ring-indigo-600',
                    iconPrepend !== '' ? 'pl-10' : '',
                ]"
                :pattern="pattern"
                :aria-invalid="errorComputed"
                :placeholder="placeholder"
                :disabled="disabled"
                :readonly="readonly"
                :required="required"
                @focus="handleFocusInput"
                @blur="handleBlurInput"
                @keydown="handleKeyDownInput"
                @keypress="handleKeyPressInput"
                @keyup="handleKeyUpInput"
                @change="handleChangeInput"
                @input="handleInput"
            />
            <div
                class="pointer-events-none absolute inset-y-0 flex items-center left-3"
            >
                <i :class="iconPrepend" style="font-size: 18px"></i>
            </div>
            <div
                class="pointer-events-none absolute inset-y-0 flex items-center pr-3"
                :class="[!errorComputed ? 'right-0' : 'right-7']"
                v-if="maxLength !== Infinity"
            >
                <span class="text-sm text-slate-400" id="CDInput-optional">
                    {{ lazyValue.length }}/{{ maxLength }}
                </span>
            </div>

            <div
                class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-3"
            >
                <i
                    v-if="errorComputed"
                    class="fas fa-exclamation-circle text-red-500"
                ></i>
                <i v-else :class="iconAppend" style="font-size: 18px"></i>
            </div>
        </div>
        <p
            v-if="errorComputed"
            class="mt-2 text-sm text-red-600"
            id="CDInput-error"
        >
            {{ errorMessageString }}
        </p>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

interface ErrorMessages {
    required?: string;
    pattern?: string;
    minlength?: string;
}
export default defineComponent({
    components: {},
    props: {
        labelClass: {
            default: "" as string,
        },
        contentClass: {
            default: "" as string,
        },
        label: {
            default: "" as string,
        },
        modelValue: {
            default: "" as string,
            required: false,
        },
        placeholder: {
            default: "" as string,
            required: false,
        },
        disabled: {
            default: false as boolean,
            required: false,
        },
        readonly: {
            default: false as boolean,
            required: false,
        },
        error: {
            default: false as boolean,
            required: false,
        },
        errorMessages: {
            default: {} as ErrorMessages,
            required: false,
        },
        required: {
            default: false as boolean,
            required: false,
        },
        pattern: {
            default: "" as string,
            required: false,
        },
        minLength: {
            default: 0 as number,
            required: false,
        },
        maxLength: {
            default: Infinity as number,
            required: false,
        },
        type: {
            default: "text" as string,
            required: false,
        },
        iconPrepend: {
            default: "" as string,
            required: false,
        },
        iconAppend: {
            default: "" as string,
            required: false,
        },
    },
    data() {
        return {
            // errorMessageString: "",
            errorStatus: this.error,
        };
    },
    computed: {
        errorMessageString() {
            if (this.patternError) {
                return this.errorMessages.pattern || "Sai định dạng";
            }
            if (this.minLengthError) {
                return (
                    this.errorMessages.minlength ||
                    `Nhập ít nhất ${this.minLength} ký tự!`
                );
            }
            if (this.requiredError) {
                return (
                    this.errorMessages.required ||
                    "Không được để trống trường này!"
                );
            }
            return "";
        },
        lazyValue: {
            get() {
                return this.modelValue;
            },
            set(value: string) {
                this.$emit("update:modelValue", value);
            },
        },
        errorComputed() {
            if (this.error) return true;
            if (
                !this.patternError &&
                !this.minLengthError &&
                !this.requiredError
            ) {
                return false;
            }

            return this.errorStatus;
        },
        minLengthError() {
            if (
                this.modelValue !== "" &&
                this.modelValue?.length < this.minLength
            ) {
                return true;
            } else {
                return false;
            }
        },
        patternError() {
            // let expression = `.*${this.pattern}.*`;
            let expression = this.pattern;
            let regex = new RegExp(expression);
            if (!regex.test(this.modelValue) && this.modelValue !== "") {
                return true;
            } else {
                return false;
            }
        },
        requiredError() {
            if (this.required && this.modelValue === "") {
                return true;
            } else {
                return false;
            }
        },
    },
    emits: [
        "update:modelValue",
        "input",
        "change",
        "focus",
        "blur",
        "keydown",
        "keypress",
        "keyup",
    ],
    mounted() {},
    methods: {
        handleInput(e: any) {
            if (this.maxLength !== Infinity) {
                if (this.modelValue?.length > this.maxLength) {
                    this.lazyValue = this.modelValue?.slice(0, this.maxLength);
                }
            }
            this.$emit("input", e);
        },
        handleChangeInput(e: any) {
            this.$emit("change", e);
        },
        handleFocusInput(e: any) {
            this.$emit("focus", e);
        },

        handleBlurInput(e: any) {
            if (
                this.requiredError ||
                this.patternError ||
                this.minLengthError
            ) {
                this.errorStatus = true;
            } else {
                this.errorStatus = false;
            }
            if (this.minLengthError) {
                this.errorMessageString =
                    this.errorMessages.minlength ||
                    `Nhập ít nhất ${this.minLength} ký tự!`;
            }

            this.$emit("blur", e);
        },

        handleKeyDownInput(e: any) {
            this.$emit("keydown", e);
            if (this.type === "tel") {
                if (/^[a-z]$/i.test(e.key)) {
                    e.preventDefault();
                }
            }
        },

        handleKeyPressInput(e: any) {
            this.$emit("keypress", e);
        },

        handleKeyUpInput(e: any) {
            this.$emit("keyup", e);
        },
    },
});
</script>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>
