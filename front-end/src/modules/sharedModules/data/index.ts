import { instanceI18n } from "@/main.js";

const $t = instanceI18n.global.t;

export const sideBarItem = [
    {
        isHeadr: true,
        title: $t("common.appName"),
    },

    {
        title: "Garage",
        icon: "heroicons-outline:truck",
        isOpen: true,
        link: "/app/garage/list",
    },
    {
        title: $t("module.sharedModules.sidebar.tag"),
        icon: "heroicons-outline:tag",
        isOpen: true,
        link: "/app/tag/list",
    },
    {
        title: $t("module.sharedModules.sidebar.subSystem"),
        icon: "heroicons-outline:home",
        isOpen: true,
        link: "/app/subSystem/list",
    },

    {
        title: $t("module.sharedModules.sidebar.service"),
        icon: "heroicons-outline:wrench-screwdriver",
        isOpen: true,
        link: "/app/service/list",
    },
    {
        title: $t("module.sharedModules.sidebar.rescues"),
        icon: "heroicons:exclamation-triangle",
        isOpen: true,
        link: "/app/rescues/list",
    },
    {
        title: $t("module.sharedModules.sidebar.insurances"),
        icon: "heroicons-outline:banknotes",
        isOpen: true,
        link: "/app/insurances/list",
    },
    {
        title: $t("module.sharedModules.sidebar.garageOwner"),
        icon: "heroicons-outline:user-circle",
        for: "createGarageOwner",
        isHeadr: true,
    },
];
