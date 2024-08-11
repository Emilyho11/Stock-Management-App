import React, { useEffect } from "react";
import Card from "../components/Card";
import Button from "../components/Button";
import { Link } from "react-router-dom";
import { useAuth } from "../components/AuthContext";
import { useNavigate } from "react-router-dom";
import AxiosClient from "../api/AxiosClient";

const Account = () => {
	const { getUsername, isLoggedIn } = useAuth();
	const navigate = useNavigate();

	useEffect(() => {
		if (!isLoggedIn()) {
			navigate("/login");
			navigate(0);
		}
	}, []);

	if (!isLoggedIn()) {
		return <div>Redirecting to login...</div>;
	}

	// Delete account api
	const deleteAccount = async () => {
		try {
			const user = {
				username: getUsername(),
			};
			const response = await AxiosClient.delete("account/", { data: user });
			if (response.data.message === "User deleted successfully") {
				console.log("Account deleted successfully");
				navigate("/login");
			} else {
				console.error("Failed to delete account:", response.data.message);
			}
		} catch (error) {
			console.error("Error deleting account:", error);
		}
	}

	return (
		<div className="mx-16 my-4">
			<h1>Account: {getUsername()}</h1>
			<div className="mt-4">
				<Button className="mr-2">Edit Username</Button>
				<Button className="mr-2">Edit Password</Button>
				<Button className="mr-2">Edit Email</Button>
			</div>

			<Link to={"/logout"}>
				<Button className="mt-4">Logout</Button>
			</Link>
			<Button className="bg-red-700" onClick={deleteAccount}>Delete Account</Button>
		</div>
	);
};

export default Account;
