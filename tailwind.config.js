/** @type {import('tailwindcss').Config} */
export default {
    content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
    theme: {
      extend: {
        colors: {
          "app-bg": "#FEF9EF",
          "app-primary": "#227C9D",
          "app-secondary": "#17C3B2",
          "app-tertiary": "#FFCB77",
          "app-text-primary": "#0B132B",
          "app-text-secondary": "#333333",
          'dark-background': '#21252b',
          'medium-grey': '#9e9e9e'
        },
        spacing: {
          'micro': '4px',
          'small': '8px',
          'default': '16px',
          'large': '24px',
        },
        screens: {
          sm: '576px',
          md: '768px',
          lg: '992px',
          xl: '1200px',
          xxl: '1400px',
          uw: '2000px',
        },
      },
    },
    plugins: [],
  }

