import { defineStore } from 'pinia'
import {LoginPayloadType, LoginResponseType} from "@/modules/sharedModules/types/User";
import {login, verify} from "@/modules/sharedModules/api";
import {ElNotification} from "element-plus";

interface State {
  userInfo: any
}
export const useUserStore = defineStore('user', {
  state: (): State => {
    return {
      userInfo: null,
    }
  },
  actions: {
    async onLogin(payload: LoginPayloadType) {
      return await login(payload)
    },
    async onVerify() {
      return await verify()
    }
  }
})

