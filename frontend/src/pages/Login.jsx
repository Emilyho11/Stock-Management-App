import React, { useState } from "react";
import Card from "../components/Card";
import Button, { ButtonVariants } from "../components/Button";
import AxiosClient from "../api/AxiosClient";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../components/AuthContext";
import stocksBg from "../assets/images/bg.jpg";

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
        <div className="relative w-full h-screen flex items-center justify-center">
			<div
				className="absolute top-0 left-0 w-full h-full opacity-60 blur-sm grayscale-[40%]"
				style={{
					backgroundImage: `url(${stocksBg})`,
					backgroundSize: "cover",
					backgroundPosition: "center",
				}}
			></div>
            <Card className="relative z-10 w-full md:w-1/4 px-14 flex flex-col bg-gray-100 py-24">
                <h1>Login</h1>
                <form
                    onSubmit={handleSubmit}
                    className="flex flex-col w-full [&>input]:bg-gray-200 [&>input]:px-2 [&>input]:py-2 [&>input]:rounded-sm [&>label]:text-left"
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
					<div className="my-4"></div>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {message && <p className={`pt-4 message ${message.includes("success") || message.includes("Logging in") ? "text-green-600" : "text-red-600"}`}>{message}</p>}
					<Link className="text-blue-600 text-left hover:text-dark_red hover:underline my-6" to="/signup">
						Don't have an account yet? Sign up here.
					</Link>
                    <Button type="submit" className="w-1/2 mx-auto flex items-center justify-center mt-6">
                        Login
                    </Button>
                </form>
            </Card>
        </div>
    );
};

export default Login;