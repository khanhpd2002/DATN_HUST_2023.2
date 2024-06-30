export default function auth(params: any) {
    if (localStorage.getItem("garage_token")) {
        return params.next();
    }
    return params.next({ name: "Login" });
}
