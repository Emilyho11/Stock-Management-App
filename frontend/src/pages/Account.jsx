import React from "react";
import Card from "../components/Card";
import Button from "../components/Button";
import { Link } from "react-router-dom";
import { useAuth } from "../components/AuthContext";

const Account = () => {
	const { username } = useAuth();
	console.log("username", username);

	return (
		<div className="mx-16 my-4">
			<h1>Account</h1>

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
			<p>Logged in as: {username}</p>
		</div>
	);
};

export default Account;
