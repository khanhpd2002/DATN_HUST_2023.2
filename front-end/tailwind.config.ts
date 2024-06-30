const colors = require("tailwindcss/colors");
const defaultTheme = require('tailwindcss/defaultTheme');

module.exports = {
    content: [
        "./public/**/*.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
        // "./.storybook/**/*.{vue,js,ts,jsx,tsx,html}",
        // "./shared-dependence/**/*.{vue,js,ts,jsx,tsx}",
        "./node_modules/vue-tailwind-datepicker/**/*.js",
    ],
    important: true,
    mode: "jit",
    darkMode: "class",
    theme: {
        // container: {
        //     center: true,
        //     padding: {
        //         DEFAULT: "15px",
        //         sm: "15px",
        //         lg: "15px",
        //         xl: "0",
        //         "2xl": "0",

        //     },
        //     // screens: {
        //     //     md:"768px",
        //     //     sm: "640px",
        //     //     lg: "1024px",
        //     //     xl: "1280px",
        //     //     "2xl": "1280px",
        //     //     ...defaultTheme.screens
        //     // },
        // },
        // screens: {
        //     sm: "640px",
        //     md:"768px",
        //     lg: "1024px",
        //     xl: "1280px",
        //     "2xl": "1280px",
        //     ...defaultTheme.screens
        // },
        extend: {
            colors: {
                // layout
                layout: {
                    primary: "var(--color-layout-primary)",
                    secondary: "var(--color-layout-secondary)",
                    muted: 'var(--color-layout-muted)',
                },
                // card
                card: {
                    primary: "var(--color-card-primary)",
                    secondary: "var(--color-card-secondary)",
                },
                // sider
                sider: {
                    primary: "var(--color-sider-primary)",
                    secondary: "var(--color-sider-secondary)",
                },
                // màu border
                line: {
                    DEFAULT: "var(--line-primary)",
                    secondary: "var(--line-secondary)",
                    muted: "var(--line-muted)",
                },
                // màu chữ
                typo: {
                    DEFAULT: "var(--text-primary)",
                    muted: "var(--text-muted)",
                    reverse: "var(--text-primary-reverse)",
                    secondary: "var(--text-secondary)",
                    "secondary-reverse": "var(--text-secondary-reverse)",
                },
                overlay: {
                    DEFAULT: "var(--overlay)",
                },
                default: {
                    DEFAULT: "var(--color-default)",
                    light: "var(--color-default-light)",
                    dark: "var(--color-default-dark)",
                },
                primary: {
                    DEFAULT: "var(--color-primary)",
                    light: "var(--color-primary-light)",
                    dark: "var(--color-primary-dark)",
                },
                secondary: {
                    DEFAULT: "var(--color-secondary)",
                    light: "var(--color-secondary-light)",
                    dark: "var(--color-secondary-dark)",
                },
                success: {
                    DEFAULT: "var(--color-success)",
                    light: "var(--color-success-light)",
                    dark: "var(--color-success-dark)",
                    status: "#ECFDF5",
                },
                danger: {
                    DEFAULT: "var(--color-danger)",
                    light: "var(--color-danger-light)",
                    dark: "var(--color-danger-dark)",
                    status: "#FBD2D9",
                },
                warning: {
                    DEFAULT: "var(--color-warning)",
                    light: "var(--color-warning-light)",
                    dark: "var(--color-warning-dark)",
                    status: "#f2f199",
                },
                info: {
                    DEFAULT: "var(--color-info)",
                    light: "var(--color-info-light)",
                    dark: "var(--color-info-dark)",
                },
                //pagination
                pagination: {
                    DEFAULT: "var(--color-pagination-primary)",
                    secondary: "var(--color-pagination-secondary)",
                },

                // input
                input: {
                    DEFAULT: "var(--color-input-primary)",
                    secondary: "var(--color-input-secondary)",
                    muted: "var(--color-input-muted)",
                },
                background: {
                    DEFAULT: "#ECFDF5",
                    deliveryStatus: "#ECFDF5",
                },
            },

            fontFamily: {
                sans: [
                    "Inter",
                    "ui-sans-serif",
                    "system-ui",
                    "-apple-system",
                    "BlinkMacSystemFont",
                    "Segoe UI",
                    "Roboto",
                    "Helvetica Neue",
                    "Arial",
                    "Noto Sans",
                    "sans-serif",
                    '"Apple Color Emoji"',
                    '"Segoe UI Emoji"',
                    '"Segoe UI Symbol"',
                    '"Noto Color Emoji"',
                ],
                Inter: ["Inter", "sans-serif"],
            },
            boxShadow: {
                base: "0px 0px 1px rgba(40, 41, 61, 0.08), 0px 0.5px 2px rgba(96, 97, 112, 0.16)",
                base2: "0px 2px 4px rgba(40, 41, 61, 0.04), 0px 8px 16px rgba(96, 97, 112, 0.16)",
                base3: "16px 10px 40px rgba(15, 23, 42, 0.22)",
                deep: "-2px 0px 8px rgba(0, 0, 0, 0.16)",
                dropdown: "0px 4px 8px rgba(0, 0, 0, 0.08)",

                testi: "0px 4px 24px rgba(0, 0, 0, 0.06)",
                todo: "rgba(235 233 241, 0.6) 0px 3px 10px 0px",
            },
            keyframes: {
                zoom: {
                    "0%, 100%": { transform: "scale(0.5)" },
                    "50%": { transform: "scale(1)" },
                },
                tada: {
                    "0%": { transform: "scale3d(1, 1, 1)" },
                    "10%, 20%": {
                        transform:
                            "scale3d(1, 1, 0.95) rotate3d(0, 0, 1, -10deg)",
                    },
                    "30%, 50%, 70%, 90%": {
                        transform: "scale3d(1, 1, 1) rotate3d(0, 0, 1, 10deg)",
                    },
                    "40%, 60%, 80%": {
                        transform: "rotate3d(0, 0, 1, -10deg)",
                    },
                    "100%": { transform: "scale3d(1, 1, 1)" },
                },
            },
            animation: {
                "spin-slow": "spin 3s linear infinite",
                zoom: "zoom 1s ease-in-out infinite",
                tada: "tada 1.5s ease-in-out infinite",
            },
        },
    },
    plugins: [],
};
