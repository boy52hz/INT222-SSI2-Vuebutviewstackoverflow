/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        'primary-blue': '#13293d',
        'primary-blue-hover': '#1e3d5a',
        'secondary-blue': '#006494',
        light: '#f5f5f5',
      },
    },
  },
  plugins: [],
}
