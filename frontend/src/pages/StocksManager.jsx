import React from "react";
import { useState, useEffect } from "react";
import PrivacyIcon from "../components/PrivacyIcon";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import DndStockCard from "../components/DndStockCard";
import Button from "../components/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import AddStocksModal from "../components/AddStocksModal";
import CreateStockListModal from "../components/CreateStockListModal";
import AxiosClient from "../api/AxiosClient";
import CreateButton from "../components/CreateButton";
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../components/AuthContext";

const StocksManager = () => {
	const [portfolios, setPortfolios] = useState([]);
	const [stockLists, setStockLists] = useState([]);
	const { getUsername, isLoggedIn } = useAuth();
	const username = getUsername();
	const navigate = useNavigate();

	useEffect(() => {
		if (!isLoggedIn()) {
			navigate("/login");
			navigate(0);
		}
		const getPortfolios = async () => {
			try {
				const response = await AxiosClient.get(`portfolio/get/${username}`);
				if (portfolios) {
					setPortfolios([]);
				}
				if (response.data) {
					setPortfolios(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		const getStockLists = async () => {
			try {
				const response = await AxiosClient.get("stocklist/get/" + username);
				if (stockLists) {
					setStockLists([]);
				}
				if (response.data) {
					console.log(response.data);
					setStockLists(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		getPortfolios();
		getStockLists();
	}, [isLoggedIn]);

	const handleOpenPortfolio = (portfolio) => {
		navigate(`/portfolio/${portfolio.id}`, { state: { portfolio: portfolio } });
	}
	const handleOpenStockList = (stocklist) => {
		navigate(`/stocklist/${stocklist.id}`, { state: { stocklist: stocklist } });
	}

	return (
		<>
			{/* <CreateStockListModal /> */}
			<div className="w-2/3 ml-auto mr-auto flex flex-col md:flex-row m-4 gap-4">
				<div className="flex min-w-[20vw] flex-col gap-2">
					<div className="flex">
						<CreateButton username={username} type={"portfolio"} id={null}></CreateButton>
						<CreateButton username={username} type={"stocklist"} id={null}></CreateButton>
					</div>
					<h1 className="text-base text-left w-fit text-gray-500 tracking-wide uppercase">Portfolios</h1>
					{/* <DndProvider backend={HTML5Backend}> */}
						<div className="flex gap-4 flex-wrap w-full">
							{portfolios
								.map((portfolio) => (
									<div className="flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg " onClick={() => handleOpenPortfolio(portfolio)}>
										<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
											Portfolio
										</p>
										<h1 className="text-xl font-normal">{portfolio.name}</h1>
									</div>
									// <DndStockCard
									// 	key={portfolio.id}
									// 	className="flex gap-4 items-center px-8 hover:!bg-white"
									// 	itemData={portfolio}
									// 	onClick={(portfolio) => handleOpenPortfolio(portfolio)}
									// >
									// 	<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
									// 		Portfolio
									// 	</p>
									// 	<h1 className="text-xl font-normal">{portfolio.name}</h1>
									// </DndStockCard>
								))}
						</div>
					{/* </DndProvider> */}
					<h1 className="text-base text-left w-fit text-gray-500 tracking-wide uppercase">Stock Lists</h1>
					<div className="flex gap-4 flex-wrap w-full">
							{stockLists
								.map((list) => (
									<div className="flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg " onClick={() => handleOpenStockList(list)}>
										<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
											Stock List
										</p>
										<h1 className="text-xl font-normal">{list.name}</h1>
										<span className="scale-75 ml-auto ">
											<PrivacyIcon privacy={list.privacy} />
										</span>
									</div>
								))}
						</div>
					{/* <DndProvider backend={HTML5Backend}>
						<div className="flex gap-4 flex-wrap w-full">
							{stockLists
								.map((list) => (
									<DndStockCard 
										key={list.id}
										className="flex gap-4 items-center px-8 hover:!bg-white"
										itemData={list}
									>
										<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
											Stock List
										</p>
										<h1 className="text-xl font-normal">{list.name}</h1>
										<span className="scale-75 ml-auto ">
											<PrivacyIcon privacy={list.privacy} />
										</span>
									</DndStockCard>
								))}
						</div>
					</DndProvider> */}
					
				</div>
			</div>
		</>
	);
};

export default StocksManager;
