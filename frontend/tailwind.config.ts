import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        "Darkest-blue": "#0D1B2A",
        "Dark-blue": "#1B263B",
        "Light-blue": "#415A77",
        "Lightest-blue": "#778DA9",
        Grey: "#E0E1DD",
        Orange: "#EB5E28",
        "Good-green": "#3D9C5A",
        "Bad-red": "#BF0603",
      },
    },
    fontFamily: {
      Roboto: ["Inter, sans-serif"],
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
