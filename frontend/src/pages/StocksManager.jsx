import React from "react";
import PrivacyIcon from "../components/PrivacyIcon";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import DndStockCard from "../components/DndStockCard";
import Button from "../components/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import AddStocksModal from "../components/AddStocksModal";
import CreateStockListModal from "../components/CreateStockListModal";

const StocksManager = () => {
	// StockList(stockListId, name, privacy)
	const [lists, setLists] = React.useState([
		{ id: 1, name: "AAPL", privacy: "public", data: { type: "stocks" } },
		{ id: 2, name: "Banking Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 3, name: "Retail Stocks", privacy: "public", data: { type: "stocklist" } },
		{ id: 4, name: "Tech Giants", privacy: "public", data: { type: "stocks" } },
		{ id: 5, name: "Energy Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 6, name: "Healthcare Stocks", privacy: "public", data: { type: "stocklist" } },
		{ id: 7, name: "Financial Services", privacy: "private", data: { type: "portfolio" } },
		{ id: 8, name: "Consumer Goods", privacy: "public", data: { type: "stocks" } },
		{ id: 9, name: "Automotive Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 10, name: "Telecom Stocks", privacy: "public", data: { type: "stocklist" } },
		{ id: 11, name: "Pharmaceutical Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 12, name: "Utility Stocks", privacy: "public", data: { type: "stocks" } },
		{ id: 13, name: "Real Estate Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 14, name: "Tech Startups", privacy: "public", data: { type: "stocklist" } },
		{ id: 15, name: "Green Energy", privacy: "private", data: { type: "portfolio" } },
		{ id: 16, name: "Biotech Stocks", privacy: "public", data: { type: "stocks" } },
		{ id: 17, name: "Insurance Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 18, name: "Retail Giants", privacy: "public", data: { type: "stocklist" } },
		{ id: 19, name: "Tech Innovators", privacy: "private", data: { type: "portfolio" } },
		{ id: 20, name: "Food & Beverage", privacy: "public", data: { type: "stocks" } },
		{ id: 21, name: "Travel & Leisure", privacy: "private", data: { type: "portfolio" } },
		{ id: 22, name: "Media Stocks", privacy: "public", data: { type: "stocklist" } },
		{ id: 23, name: "Construction Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 24, name: "Mining Stocks", privacy: "public", data: { type: "stocks" } },
		{ id: 25, name: "Agriculture Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 26, name: "Luxury Goods", privacy: "public", data: { type: "stocklist" } },
		{ id: 27, name: "Tech Pioneers", privacy: "private", data: { type: "portfolio" } },
		{ id: 28, name: "Industrial Stocks", privacy: "public", data: { type: "stocks" } },
		{ id: 29, name: "Transportation Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 30, name: "Entertainment Stocks", privacy: "public", data: { type: "stocklist" } },
		{ id: 31, name: "Tech Disruptors", privacy: "private", data: { type: "portfolio" } },
		{ id: 32, name: "Financial Tech", privacy: "public", data: { type: "stocks" } },
		{ id: 33, name: "E-commerce Stocks", privacy: "private", data: { type: "portfolio" } },
		{ id: 34, name: "Telecom Giants", privacy: "public", data: { type: "stocklist" } },
		{ id: 35, name: "Tech Leaders", privacy: "private", data: { type: "portfolio" } },
		{ id: 36, name: "Renewable Energy", privacy: "public", data: { type: "stocks" } },
		{ id: 37, name: "Tech Visionaries", privacy: "private", data: { type: "portfolio" } },
		{ id: 38, name: "Consumer Electronics", privacy: "public", data: { type: "stocklist" } },
		{ id: 39, name: "Tech Innovators", privacy: "private", data: { type: "portfolio" } },
		{ id: 40, name: "Food Tech", privacy: "public", data: { type: "stocks" } },
	]);

	return (
		<>
			{/* <CreateStockListModal /> */}
			<div className="w-2/3 ml-auto mr-auto flex flex-col md:flex-row m-4 gap-4">
				<div className="flex min-w-[20vw] flex-col gap-2">
					<h1 className="text-base text-left w-fit text-gray-500 tracking-wide uppercase">My Portfolio</h1>

					<div className="flex">
						<Button className="items-center gap-2 flex w-fit">
							<FontAwesomeIcon icon={faPlus} /> Create StockList
						</Button>
						<Button className="items-center gap-2 flex w-fit">
							<FontAwesomeIcon icon={faPlus} />
							Create Portfolio
						</Button>
						<Button className="items-center gap-2 flex w-fit">
							<FontAwesomeIcon icon={faPlus} />
							Add Stocks
						</Button>
					</div>
					<p>Note: Drag the cards around to group.</p>
					<DndProvider backend={HTML5Backend}>
						<div className="flex gap-4 flex-wrap w-full">
							{/* Prioritize in the order: portfolio, stocklist, stocks */}
							{lists
								.sort(
									(a, b) =>
										(a.data.type === "portfolio" ? -1 : 1) - (b.data.type === "stocks" ? 1 : 0)
								)
								.map((list) => (
									<DndStockCard
										key={list.id}
										className="flex gap-4 items-center px-8 hover:!bg-white"
										itemData={list.data}
									>
										<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
											{list.data.type}
										</p>
										<h1 className="text-xl font-normal">{list.name}</h1>
										<span className="scale-75 ml-auto ">
											<PrivacyIcon privacy={list.privacy} />
										</span>
									</DndStockCard>
								))}
						</div>
					</DndProvider>
				</div>
			</div>
		</>
	);
};

export default StocksManager;
