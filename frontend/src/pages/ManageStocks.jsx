import React from "react";
import StockCard from "../components/StockCard";
import Card from "../components/Card";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import Button, { ButtonVariants } from "../components/Button";
import { Link } from "react-router-dom";
import ReviewBoard from "../components/ReviewBoard";
import PrivacyIcon from "../components/PrivacyIcon";

const ManageStocks = () => {
	const [onlyOwned, setOnlyOwned] = React.useState(true);
	const [selectedStock, setSelectedStock] = React.useState({ image: "", name: "", price: 0, quantity: 0 });
	const myStocks = [
		{ image: `https://logo.clearbit.com/apple.com`, name: "AAPL", price: 150.25, quantity: 10 },
		{ image: `https://logo.clearbit.com/google.com`, name: "GOOGL", price: 2500.25, quantity: 15 },
		{ image: `https://logo.clearbit.com/AMZN.com`, name: "AMZN", price: 3500.25, quantity: 20 },
	];

	return (
		<div className="md:w-2/3 ml-auto mr-auto flex flex-col gap-2">
			<Link to="/stock-manager">
				<Button className="flex items-center gap-4" variant={ButtonVariants.TRANSPARENT}>
					<FontAwesomeIcon icon={faArrowLeft} />
					<p className="font-semibold uppercase tracking-wide">View Stock Lists</p>
				</Button>
			</Link>
			<Card className="min-h-[50vh] !bg-transparent !items-start !p-0 max-lg:flex-col">
				<div className="scale-75 ml-auto ">
					<h1 className="text-left text-4xl">STOCKLIST NAME</h1>
					<PrivacyIcon privacy="private" />
				</div>
				<Card className="w-full h-full !items-start flex-col py-8 px-12">
					<ReviewBoard />
				</Card>
			</Card>
			{/* <hr className="mb-2" /> */}
			<div className="flex flex-row  my-4  gap-4">
				<div className="flex min-w-[20vw] flex-col gap-2">
					<div className="flex gap-4 items-center justify-between">
						<h1 className="text-xl text-left w-fit">Stocks Portfolio</h1>

						<div className="sm:hidden">
							<label for="tabs" className="sr-only">
								Portfolio Type
							</label>
							<select
								id="tabs"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							>
								<option>Owned</option>
								<option>Available</option>
							</select>
						</div>

						<ul className="hidden text-sm font-medium text-center text-gray-400 rounded-lg sm:flex">
							<li className="w-full focus-within:z-10">
								<button
									onClick={() => setOnlyOwned(true)}
									className={
										"inline-block w-full p-2 px-4 bg-gray-400/50 border-r border-gray-200 hover:bg-gray-300 rounded-s-lg active focus:outline-none  " +
										(onlyOwned ? "!bg-dark_red/80 !text-white font-bold shadow" : "shadow-inner")
									}
									aria-current="page"
								>
									Owned
								</button>
							</li>
							<li className="w-full focus-within:z-10">
								<button
									onClick={() => setOnlyOwned(false)}
									className={
										"inline-block w-full p-2 px-4  bg-gray-400/50 border-s-0 border-gray-200 rounded-e-lg hover:text-gray-700 hover:bg-gray-300 focus:outline-none " +
										(!onlyOwned ? "!bg-dark_red/80 !text-white shadow" : "shadow-inner")
									}
								>
									Available
								</button>
							</li>
						</ul>
					</div>
					{myStocks.map((stock, index) => (
						<StockCard
							key={stock.name}
							data={stock}
							onSelect={() => setSelectedStock(stock)}
							selected={selectedStock.name == stock.name}
						/>
					))}
				</div>

				<div className="flex-1 flex flex-col gap-2">
					<h1 className="text-xl text-left">Stocks Details</h1>
					<Card className="h-full">Some Details</Card>
				</div>
			</div>
		</div>
	);
};

export default ManageStocks;
