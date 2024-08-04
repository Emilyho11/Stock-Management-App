import React from "react";

export const ButtonVariants = {
	DEFAULT: "default",
	ALTERNATIVE: "alternative",
	DARK: "dark",
	LIGHT: "light",
	PRIMARY: "primary",
	SECONDARY: "secondary",
	TERTIARY: "tertiary",
	QUATERNARY: "quaternary",
	TRANSPARENT: "transparent",
};

const Button = (props) => {
	const { variant = ButtonVariants.DEFAULT, children, className } = props;

	const rest = { ...props };
	delete rest.variant;
	delete rest.children;
	delete rest.className;

	switch (variant) {
		case ButtonVariants.TRANSPARENT:
			return (
				<button
					type="button"
					className={
						"py-2.5 px-5 me-2 mb-2 text-sm font-medium text-gray-900 focus:outline-none rounded-full border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.ALTERNATIVE:
			return (
				<button
					type="button"
					className={
						"py-2.5 px-5 me-2 mb-2 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-full border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100  " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.DARK:
			return (
				<button
					type="button"
					className={
						"text-white bg-gray-800 hover:bg-gray-900 focus:outline-none focus:ring-4 focus:ring-gray-300 font-medium rounded-full text-sm px-5 py-2.5 me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.LIGHT:
			return (
				<button
					type="button"
					className={
						"text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 focus:ring-4 focus:ring-gray-100 font-medium rounded-full text-sm px-5 py-2.5 me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.PRIMARY:
			return (
				<button
					type="button"
					className={
						"text-white bg-green-700 hover:bg-green-800 focus:outline-none focus:ring-4 focus:ring-green-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.SECONDARY:
			return (
				<button
					type="button"
					className={
						"text-white bg-red-700 hover:bg-red-800 focus:outline-none focus:ring-4 focus:ring-red-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.TERTIARY:
			return (
				<button
					type="button"
					className={
						"text-white bg-yellow-400 hover:bg-yellow-500 focus:outline-none focus:ring-4 focus:ring-yellow-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.QUATERNARY:
			return (
				<button
					type="button"
					className={
						"text-white bg-purple-700 hover:bg-purple-800 focus:outline-none focus:ring-4 focus:ring-purple-300 font-medium rounded-full text-sm px-5 py-2.5 text-center mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
		case ButtonVariants.DEFAULT:
		default:
			return (
				<button
					type="button"
					className={
						"text-white bg-blue-700 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full text-sm px-5 py-2.5 text-center me-2 mb-2 " +
						className
					}
					{...rest}
				>
					{children}
				</button>
			);
	}
};

export default Button;
