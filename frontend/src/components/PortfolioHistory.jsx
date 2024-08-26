import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";
import Button from "./Button";

const PortfolioHistory = ({id}) => {
	const [balance, setBalance] = useState(0);
	const [value, setValue] = useState(0);
	const [cashData, setCashData] = useState([]);
	const [transactionData, setTransactionData] = useState();

	useEffect(() => {
		const getBalance = async () => {
			try {
				const response = await AxiosClient.get(`portfolio/getBalance/${id}`);
				if (response.data) {
					setBalance(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		const getValue = async () => {
			try {
				const response = await AxiosClient.get(`portfolio/estimateValue/${id}`);
				if (response.data) {
					setValue(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		getBalance();
		getValue();
	}, []);

    const getCashHistory = () => {

    }

    const getTransactionHistory = () => {

    }

	return (
		<>
			<h1 className="text-xl border-t-2 p-2">Cash Account</h1>
			<div className="flex flex-row gap-12 text-lg text-green-500 justify-items-center align-middle content-evenly">
				<div><span className="text-black">Balance:</span> ${balance}</div>
				<div><span className="text-black">Estimated Value:</span> ${value}</div>
				{/* <Button className="bg-gray-800"> Display Cash History</Button>
				<Button className="bg-gray-800"> Display Transaction History</Button> */}
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
