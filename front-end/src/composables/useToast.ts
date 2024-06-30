import {toast} from "vue3-toastify";

export function useToast() {
  const $toast = (message: string, isSuccess: boolean) => {
    if (isSuccess) {
      toast["success"](message, {
        autoClose: 3000,
      });
      return;
    }
    toast["error"](message, {
      autoClose: 3000,
    });
  }

  return {
    $toast
  }
}