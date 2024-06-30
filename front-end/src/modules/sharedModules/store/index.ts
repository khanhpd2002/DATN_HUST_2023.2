export type AppS = {
    currentModule: string;
    headerActions: any;
    functionSidebar: any;
    changeCurrentModule: Function;
};
import { defineStore } from "pinia";

export const useAppStore = defineStore("app", {
    state: () => ({
        currentModule: "garage",
        headerActions: {
            headerButton: "",
            actions: null,
        },
        functionSidebar: {
            componentName: "",
            props: {},
            display: false,
            actionName: "",
        },
    }),
    actions: {
        changeCurrentModule(data: any) {
            this.currentModule = data.moduleName;
        },
        changeHeaderActions(data: any) {
            this.headerActions.headerButton = data.title;
            this.headerActions.actions = data.actions;
        },
        changeFunctionSidebar(data: any) {
            this.functionSidebar.display = true;
            this.functionSidebar.actionName = data.actionName;
            this.functionSidebar.props = data.props;
            this.functionSidebar.componentName = data.componentName;
        },
        closeSidebar() {
            this.functionSidebar.display = false;
            this.functionSidebar.componentName = "";
            this.functionSidebar.props = {};
        },
    },
});

const dataHeaderConfig = {
    actions: {
        garage: {
            title: "create",
            actionDefinition: "redirect",
            params: {
                path: "/app/garage/create",
            },
        },
        garageById: {
            title: "edit",
            actionDefinition: "redirect",
            params: {
                nextName: "ediGarageDetail",
                id: "",
            },
        },
    } as any,
};

// export const useAppStore = defineStore("app", app);
