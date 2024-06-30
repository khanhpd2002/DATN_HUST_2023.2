export default function auth(params: any) {
    if (localStorage.activeUser) {
        return params.next({ name: "home" });
    }
    return params.next();
}
