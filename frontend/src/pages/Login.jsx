import React from "react";
import Card from "../components/Card";
import Button, { ButtonVariants } from "../components/Button";

const Login = () => {
	return (
		<div className="w-full">
			<Card className="!w-full md:!w-1/3 p-4 ml-auto mr-auto flex flex-col ">
				<h1>Login</h1>

				<form className="flex flex-col  w-full md:w-1/2 [&>input]:bg-gray-200 [&>input]:px-2 [&>input]:py-1 [&>input]:rounded-sm [&>label]:text-left">
					<label htmlFor="username">Username:</label>
					<input type="text" id="username" name="username" required />
					<br className="my-2" />
					<label htmlFor="password">Password:</label>
					<input type="password" id="password" name="password" required />
					<br className="my-2" />
					<Button type="submit" variant={ButtonVariants.LIGHT}>
						Login{" "}
					</Button>
				</form>
			</Card>
		</div>
	);
};

export default Login;
