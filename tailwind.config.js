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
          'dark-shade': '#282c34',
          'dark-blue': '#001946',
          'telegram-blue': '#30A3E6',
          'text-color': '#FFFFFF',
          'light-grey': '#E6EAED',
          'medium-grey': '#9CABB8',
          'white-smoke': '#F2F2F2',
          'grey': '#485C65',
          'red': '#CC2F26',
          'dark-red': '#8B0000',
          'icon-red': '#ff7373',
          'light-red': '#FFD7D5',
          'light-blue': '#DEF4FE',
          'light-green': '#DBF7E0',
          'deep-green': '#2E833C',
          'green': '#359846',
          'outline-color': '#E8E8E8',
          'secondary-bg': '#F4F6F7',
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
        fontWeight: {
          light: 300,
          normal: 400,
          medium: 500,
          semibold: 600,
          bold: 700,
        },
        fontSize: {
          'overline': '0.625rem',     // 10sp
          'caption': '0.75rem',       // 12sp
          'button': '0.875rem',       // 14sp
          'body2': '0.875rem',        // 14sp
          'subtitle2': '0.875rem',    // 14sp
          'body1': '1rem',            // 16sp
          'subtitle1': '1rem',        // 16sp
          'h6': '1.25rem',            // 20sp
          'h5': '1.5rem',             // 24sp
          'h4': '2.125rem',           // 34sp
          'h3': '3rem',               // 48sp
          'h2': '3.688rem',           // 59sp
          'h1': '5.938rem',           // 95sp
        },
      },
    },
    plugins: [],
  }

