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
			const response = await AxiosClient.delete("account/");
			if (response.status === 204) {
				navigate("/logout");
			} else {
				console.error("Unexpected response:", response);
			}
		} catch (error) {
			console.error("Error deleting account:", error);
		}
	}

	return (
		<div className="mx-16 my-4">
			<h1>Account: {getUsername()}</h1>

			<Card className="flex flex-col w-1/4">
				<h2 className="text-xl">Transaction History</h2>
				<div className="w-1/3">
					<p className="text-base">Stocks Bought: 0</p>
					<p className="text-base">Stocks Sold: 0</p>
				</div>
			</Card>

			<div className="mt-4">
				<Button className="mr-2">Edit Username</Button>
				<Button className="mr-2">Edit Password</Button>
				<Button className="mr-2">Edit Email</Button>
			</div>

			<Link to={"/logout"}>
				<Button className="mt-4">Logout</Button>
			</Link>
			<Button className="bg-red-700">Delete Account</Button>
		</div>
	);
};

export default Account;
