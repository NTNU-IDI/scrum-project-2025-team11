import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        "darkest-blue": "#0D1B2A",
        "dark-blue": "#1B263B",
        "light-blue": "#415A77",
        "lightest-blue": "#778DA9",
<<<<<<< HEAD
        "grey": "#E0E1DD",
        "orange": "#EB5E28",
=======
        grey: "#E0E1DD",
        orange: "#EB5E28",
>>>>>>> c6e4a1f5a1c34fac7a07ec3b4d409cddf548fd2b
        "good-green": "#3D9C5A",
        "bad-red": "#BF0603",
      },
    },
    fontFamily: {
      Roboto: ["Inter", "sans-serif"],
    },
    container: {
      padding: "2rem",
      center: true,
    },
    screens: {
      sm: "640px",
      md: "768px",
    },
  },
  plugins: [],
};

export default config;
