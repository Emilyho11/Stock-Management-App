import React from "react";
import Card from "../components/Card";
import Button from "../components/Button";
import { Link } from "react-router-dom";

const Account = () => {
	return (
		<div className="mx-16 my-4">
			<h1>Account</h1>

			<Card className="flex flex-col w-1/4">
				<h2 className="text-xl">Transaction History</h2>
				<div className="w-1/3">
					<p>Some Details</p>
					<p>Some Details</p>
					<p>Some Details</p>
					<p>Some Details</p>
					<p>Some Details</p>
					<p>Some Details</p>
					<p>Some Details</p>
				</div>
			</Card>

			<Link to={"/logout"}>
				<Button className="mt-4">Logout</Button>
			</Link>
		</div>
	);
};

export default Account;
