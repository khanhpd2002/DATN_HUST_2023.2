import {instanceI18n} from "@/main";
import {computed} from "vue";

export function useI18n() {
  const $t = computed(() => instanceI18n?.global?.t) ;

  return {
    $t: $t.value
  }
}