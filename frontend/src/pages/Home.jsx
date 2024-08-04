import React from "react";
import Button from "../components/Button";
import { Link } from "react-router-dom";

const Home = () => {
	// Check if logged in
	const [loggedIn, setLoggedIn] = React.useState(true);

	React.useEffect(() => {
		const token = localStorage.getItem("token");
		if (token) {
			setLoggedIn(true);
		}
	}, []);

	if (!loggedIn) {
		return (
			<div className="h-[50vh]">
				<h1>Home</h1>
				<p>An account is required to proceed.</p>
				<br />
				<Link to="/login">
					<Button className="mt-4">Login</Button>
				</Link>
			</div>
		);
	}

	return (
		<div>
			<h1>okey dokey</h1>

			<p className="text-left w-1/2 ml-auto mr-auto">
				User(username, password, email) <br />
				Friendship(friendId, username, status) <br />
				Portfolio(portfolioId, cashAccount, name, beta) <br />
				Owns(username, portfolioId) <br />
				StockList(stockListId, name, privacy) <br />
				Created(username, stockListId) <br />
				Contains(portfolioId, stockListId) <br />
				Review(username, stockListId, content) <br />
				StockData(symbol, timestamp, open, close, high, low, volume) <br />
				Lists(stockListId, symbol, quantity) <br />
				Stock(symbol, COV) <br />
				Bought(portfolioId, symbol, quantity, cost) <br />
				Transaction(portfolioId, transactionId, transactionType, transactionDate, price, quantity, symbol){" "}
				<br />
				CashHistory(portfolioId, timestamp, type, balance, amount) <br />
			</p>
		</div>
	);
};

export default Home;
