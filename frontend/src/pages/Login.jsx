import React, { useState, useEffect } from "react";
import Card from "../components/Card";
import Button, { ButtonVariants } from "../components/Button";
import AxiosClient from "../api/AxiosClient";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../components/AuthContext";

const Login = () => {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [message, setMessage] = useState("");
	const { login } = useAuth();
	const navigate = useNavigate();

	const handleSubmit = async (event) => {
		setMessage("Logging in...");
		event.preventDefault();
		try {
			const response = await AxiosClient.post("/users/login", {
				f_username: username,
				f_password: password,
			});
			if (response.data.message === "Logged in") {
				setMessage("Login successful");
				login(username);
				navigate("/");
			} else {
				setMessage("Login failed");
			}
		} catch (error) {
			console.error("Error during login:", error);
			setMessage("Error during login");
		}
	};

	return (
		<div className="w-full">
			<Card className="!w-full md:!w-1/3 p-4 ml-auto mr-auto flex flex-col ">
				<h1>Login</h1>

				<form
					onSubmit={handleSubmit}
					className="flex flex-col  w-full md:w-1/2 [&>input]:bg-gray-200 [&>input]:px-2 [&>input]:py-1 [&>input]:rounded-sm [&>label]:text-left"
				>
					<label htmlFor="username">Username:</label>
					<input
						type="text"
						id="username"
						name="username"
						value={username}
						onChange={(e) => setUsername(e.target.value)}
						required
					/>
					<br className="my-2" />
					<label htmlFor="password">Password:</label>
					<input
						type="password"
						id="password"
						name="password"
						value={password}
						onChange={(e) => setPassword(e.target.value)}
						required
					/>
					<br className="my-2" />
					<Button type="submit" variant={ButtonVariants.LIGHT}>
						Login{" "}
					</Button>
				</form>
				{message && <p>{message}</p>}
				<Link className="text-blue-600 hover:text-dark_red hover:underline" to="/signup">
					Don't have an account yet? Sign up here.
				</Link>
			</Card>
		</div>
	);
};

export default Login;
