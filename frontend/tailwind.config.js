/** @type {import('tailwindcss').Config} */
export default {
	content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
	theme: {
		extend: {
			colors: {
				dark_red: "#380510",
				stock_color: "#238c3a",
			},
			width: {
				"1/8": "12.5%",
        "1/10": "10%",
			},
		},
	},
	plugins: [],
};