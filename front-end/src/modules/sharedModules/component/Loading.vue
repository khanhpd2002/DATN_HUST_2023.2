<template>
    <div class="h-[calc(100vh-64px)]" v-if="props.isLoading">
        <CDPageSpinner>
            <div id="loading-bg">
                <div class="loading-logo text-center">
                    <img
                        src="@/assets/images/logo_cardoctor.svg"
                        alt="Logo"
                        width="100px"
                    />
                </div>
                <div class="loading">
                    <div class="effect-1 effects"></div>
                    <div class="effect-2 effects"></div>
                    <div class="effect-3 effects"></div>
                </div>
            </div>
        </CDPageSpinner>
    </div>
</template>

<script lang="ts" setup>
import { CDPageSpinner } from "@cd/design-system";

type LoadingProps = {
    isLoading: boolean;
};
const props = withDefaults(defineProps<LoadingProps>(), { isLoading: false });
</script>
