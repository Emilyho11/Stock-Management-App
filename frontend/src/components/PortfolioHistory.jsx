import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";

const PortfolioHistory = (id) => {
	const [balance, setBalance] = useState([]);

	useEffect(() => {
		const getBalance = async () => {
			try {
				const response = await AxiosClient.get("reviews/");
				if (response.data && Array.isArray(response.data)) {
					// setReviews(response.data);
					const parsedReviews = response.data.map((item) => ({
                        username: item.f_username.trim(),
                        stockListId: item.f_stock_list_id,
                        content: item.f_content.trim(),
                    }));
					setBalance([]);
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
			<h2 className="text-xl">Cash and Transaction History</h2>
			<div className="flex flex-col gap-12">
				{balance.map((bal, index) => (
					<Review key={index} data={bal} />
				))}
			</div>
	  </>
	);
};

export default PortfolioHistory;
