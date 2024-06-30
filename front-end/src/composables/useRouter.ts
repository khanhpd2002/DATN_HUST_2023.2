import { useRouter, useRoute } from 'vue-router'
import type { RouteLocationRaw, RouteLocationNormalized } from 'vue-router'
import { instanceI18n } from '@/main'
import { emitter } from '@/utils/mitt'

export default function useCDRouter() {
	const route = useRoute()
	const router = useRouter()
	const push = (to: RouteLocationRaw) => {
		return router.push(to)
	}
	const replace = (to: RouteLocationRaw) => {
		return router.replace(to)
	}
	const go = (to: number) => {
		return router.go(to)
	}
	const back = () => {
		return router.back()
	}
	const pushByName = (to: RouteLocationNormalized) => {
		return router.push(instanceI18n.i18nRouteByName(to))
	}
	const pushByPath = (to: RouteLocationRaw) => {
		return router.push(instanceI18n.i18nRouteByPath(to))
	}
	const replaceByPath = (to: RouteLocationRaw) => {
		return router.replace(instanceI18n.i18nRouteByPath(to))
	}
	const replaceByName = (to: RouteLocationRaw) => {
		return router.replace(instanceI18n.i18nRouteByPath(to))
	}
	function objectToQueryString(params: any) {
		const queryString = Object.keys(params)
			.map(
				(key) => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`
			)
			.join('&')
		return queryString
	}
	const query = (params: any) => {
		history.pushState(
			{
				current: route.fullPath,
			},
			'',
			route.path + '?' + objectToQueryString(params)
		)
		router.options.history.push(route.path + '?' + objectToQueryString(params))
		// route.query = params
		emitter.emit('router-on-set-query', params)
	}
	return {
		query,
		router,
		pushByName,
		pushByPath,
		replaceByPath,
		replaceByName,
		push,
		go,
		back,
		replace,
		route,
	}
}
