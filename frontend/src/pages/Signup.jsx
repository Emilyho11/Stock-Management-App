import React, { useState, useEffect } from "react";
import Card from "../components/Card";
import Button, { ButtonVariants } from "../components/Button";
import AxiosClient from "../api/AxiosClient";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../components/AuthContext";

const Signup = () => {
	const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
	const navigate = useNavigate();
    const { login } = useAuth();

    const handleCreate = async (event) => {
        event.preventDefault();
        try {
            const response = await AxiosClient.post("/users/", {
				f_username: username,
				f_password: password,
                f_email: email,
            });
            if (response.data.message === "User created successfully") {
                setMessage("Account created successfully");
                login();
				navigate("/");
            } else {
                setMessage("Failed to create account");
            }
        } catch (error) {
            console.error("Error during signup:", error);
            setMessage("Error during signup");
        }
    };

	return (
		<div className="w-full">
			<Card className="!w-full md:!w-1/3 p-4 ml-auto mr-auto flex flex-col ">
				<h1>Signup</h1>

				<form onSubmit={handleCreate} className="flex flex-col  w-full md:w-1/2 [&>input]:bg-gray-200 [&>input]:px-2 [&>input]:py-1 [&>input]:rounded-sm [&>label]:text-left">
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
						required />
					<br className="my-2" />
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required />
                    <br className="my-2" />
					<Button type="submit" variant={ButtonVariants.LIGHT}>
						Login{" "}
					</Button>
				</form>
				{message && <p>{message}</p>}
				<Link className="text-blue-600 hover:text-dark_red hover:underline" to="/login">Have an account? Sign in here.</Link>
			</Card>
		</div>
	);
};

export default Signup