<template>
    <TransitionRoot appear :show="true" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-100">
            <TransitionChild
                as="template"
                enter="duration-300 ease-out"
                enter-from="opacity-0"
                enter-to="opacity-100"
                leave="duration-200 ease-in"
                leave-from="opacity-100"
                leave-to="opacity-0"
            >
                <div class="fixed inset-0 bg-opacity-25" />
            </TransitionChild>

            <div class="fixed inset-0 overflow-y-auto bg-gray-200/80 z-50">
                <div
                    class="flex min-h-full items-center justify-center p-4 text-center"
                >
                    <TransitionChild
                        as="template"
                        enter="duration-300 ease-out"
                        enter-from="opacity-0 scale-95"
                        enter-to="opacity-100 scale-100"
                        leave="duration-200 ease-in"
                        leave-from="opacity-100 scale-100"
                        leave-to="opacity-0 scale-95"
                    >
                        <DialogPanel
                            :class="width"
                            class="max-w-6xl transform overflow-hidden rounded-2xl bg-white text-left align-middle shadow-xl transition-all"
                        >
                            <DialogTitle
                                as="h3"
                                class="text-lg font-medium leading-6 text-gray-900"
                            >
                                <slot name="title">
                                    {{ title }}
                                </slot>
                                <XMarkIcon
                                    v-if="withClose"
                                    class="absolute right-5 top-5 cursor-pointer w-7 h-7"
                                    @click="closeModal"
                                />
                            </DialogTitle>
                            <div class="max-h-[800px] overflow-y-auto">
                                <slot name="content">
                                    <div class="mt-2">
                                        <p class="text-sm mb-1 text-gray-500">
                                            {{ content }}
                                        </p>
                                    </div>
                                </slot>
                            </div>
                            <slot name="action">
                                <div class="mt-4 flex justify-around">
                                    <button
                                        type="button"
                                        class="inline-flex justify-center rounded-md border border-transparent bg-green-100 px-4 py-2 text-sm font-medium text-green-900 hover:bg-green-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                                        @click="accept"
                                    >
                                        accept
                                    </button>
                                    <button
                                        type="button"
                                        class="inline-flex justify-center rounded-md border border-transparent bg-blue-100 px-4 py-2 text-sm font-medium text-blue-900 hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                                        @click="closeModal"
                                    >
                                        cancel
                                    </button>
                                </div>
                            </slot>
                        </DialogPanel>
                    </TransitionChild>
                </div>
            </div>
        </Dialog>
    </TransitionRoot>
</template>
<script lang="ts">
import { XMarkIcon } from "@heroicons/vue/20/solid";
import { defineComponent } from "vue";
import {
    TransitionRoot,
    TransitionChild,
    Dialog,
    DialogPanel,
    DialogTitle,
} from "@headlessui/vue";

export default defineComponent({
    components: {
        XMarkIcon,
        TransitionRoot,
        TransitionChild,
        Dialog,
        DialogPanel,
        DialogTitle,
    },
    props: {
        withClose: {
            default: true as boolean,
        },
        width: {
            default: "w-96",
        },
        title: {
            default: "" as string,
        },
        content: {
            default: "" as string,
        },
    },
    methods: {
        accept() {
            this.$emit("accept");
        },
        closeModal() {
            this.$emit("closeDialog");
        },
    },
});
</script>
