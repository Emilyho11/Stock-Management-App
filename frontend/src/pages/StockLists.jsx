import React from "react";
import Card from "../components/Card";
import ReviewBoard from "../components/ReviewBoard";
import PrivacyIcon from "../components/PrivacyIcon";

const StockLists = () => {
	// StockList(stockListId, name, privacy)
	const [lists, setLists] = React.useState([
		{ id: 1, name: "Tech Stocks", privacy: "public" },
		{ id: 2, name: "Banking Stocks", privacy: "private" },
		{ id: 3, name: "Retail Stocks", privacy: "public" },
	]);

	return (
		<div className="w-2/3 ml-auto mr-auto flex flex-col md:flex-row m-4 gap-4">
			<div className="flex min-w-[20vw] flex-col gap-2">
				<h1 className="text-base text-left w-fit text-gray-500 tracking-wide uppercase">My Portfolio</h1>
				{lists.map((list) => (
					<Card key={list.id} className="flex gap-4 items-center px-8 !bg-transparent hover:!bg-white">
						<h1 className="text-xl font-normal">{list.name}</h1>
						<span className="scale-75 ml-auto ">
							(<PrivacyIcon privacy={list.privacy} />)
						</span>
					</Card>
				))}
			</div>
		</div>
	);
};

export default StockLists;
