import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";
import Button from "./Button";

const PortfolioHistory = () => {
	const [balance, setBalance] = useState(0);
	const [cashData, setCashData] = useState([]);
	const [transactionData, setTransactionData] = useState();

	useEffect(() => {
		const getBalance = async () => {
			try {
				const response = await AxiosClient.get(`portfolio/getBalance/${1}`);
				if (response.data) {
					setBalance(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		getBalance();
	}, []);

    const getCashHistory = () => {

    }

    const getTransactionHistory = () => {

    }

	return (
		<>
			<h2 className="text-xl border-t-2">Cash and Transaction History</h2>
			<div className="flex flex-row gap-12 text-lg text-green-500 justify-items-center align-middle content-evenly">
				<div><span className="text-black">Balance:</span> ${balance}</div>
				<Button className="bg-gray-800"> Display Cash History</Button>
				<Button className="bg-gray-800"> Display Transaction History</Button>
			</div>
			{/* <div className="flex flex-col gap-12">
				{balance.map((bal, index) => (
					<Review key={index} data={bal} />
				))}
			</div> */}
	  </>
	);
};

export default PortfolioHistory;
