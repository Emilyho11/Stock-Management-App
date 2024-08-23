import React from "react";
import { useState, useEffect } from "react";
import PrivacyIcon from "../components/PrivacyIcon";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import DndStockCard from "../components/DndStockCard";
import Button from "../components/Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faX } from "@fortawesome/free-solid-svg-icons";
import AddStocksModal from "../components/AddStocksModal";
import CreateStockListModal from "../components/CreateStockListModal";
import AxiosClient from "../api/AxiosClient";
import CreateButton from "../components/CreateButton";
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../components/AuthContext";

const StocksManager = () => {
	const [portfolios, setPortfolios] = useState([]);
	const [stockLists, setStockLists] = useState([]);
	const [friendLists, setFriendLists] = useState([]);
	const [publicLists, setPublicLists] = useState([]);
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
				const response = await AxiosClient.get(`stocklist/get/${username}`);
				if (stockLists) {
					setStockLists([]);
				}
				if (response.data) {
					setStockLists(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		const getFriendLists = async () => {
			try {
				const response = await AxiosClient.get(`stocklist/getFriends/${username}`);
				if (friendLists) {
					setFriendLists([]);
				}
				if (response.data) {
					setFriendLists(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		const getPublicLists = async () => {
			try {
				const response = await AxiosClient.get(`stocklist/getPublic/${username}`);
				if (publicLists) {
					setPublicLists([]);
				}
				if (response.data) {
					setPublicLists(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		getPortfolios();
		getStockLists();
		getFriendLists();
		getPublicLists();
	}, [isLoggedIn]);

	const handleOpenPortfolio = (portfolio) => {
		if (!(portfolio.id)){
			handleDeletePortfolio(portfolio.id)
		}
		else {navigate(`/portfolio/${portfolio.id}`, { state: { portfolio: portfolio } })};
	}
	const handleOpenStockList = (stocklist, name) => {
		if (!(stocklist.id)){
			handleDeleteList(stocklist.id)
		}
		else if (name==username){
			navigate(`/stocklist/${stocklist.id}`, { state: { stocklist: stocklist, privacy: stocklist.privacy, isOwner: true } });
		}
		else {
			navigate(`/stocklist/${stocklist.id}`, { state: { stocklist: stocklist, privacy: stocklist.privacy, isOwner: false } });
		}
	}

	const handleDeletePortfolio = (id) => {
		AxiosClient.delete(`portfolio/delete/${id}`)
		navigate(0);
	}
	
	const handleDeleteList = (id) => {
		AxiosClient.delete(`stocklist/delete/${id}`)
		navigate(0);
	}

	return (
		<>
			<div className="w-full items-center justify-center">
				<div className="flex min-w-[20vw] flex-col gap-2 w-2/3 ml-auto mr-auto p-10">
					<div className="flex">
						<CreateButton username={username} type={"portfolio"} id={null}></CreateButton>
						<CreateButton username={username} type={"stocklist"} id={null}></CreateButton>
					</div>
					<div className="text-black">
						<h1 className="text-base text-left w-fit tracking-wide uppercase">Portfolios</h1>
							<div className="flex gap-4 flex-wrap w-full max-h-screen py-4">
								{portfolios
									.map((portfolio) => (
										<div key={portfolio.id} className="relative flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg flex-col" onClick={() => handleOpenPortfolio(portfolio)}>
											<FontAwesomeIcon icon={faX} className="absolute top-2 right-2 text-red-500 hover:text-red-300 text-md cursor-pointer" onClick={(e) => handleDeletePortfolio(portfolio.id)}/>
											<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
												Portfolio
											</p>
											<h1 className="text-xl font-normal">{portfolio.name}</h1>
										</div>
									))}
							</div>
						<h1 className="text-base text-left w-fit tracking-wide uppercase">My Stock Lists</h1>
						<div className="flex gap-4 flex-wrap w-full max-h-screen py-4">
								{stockLists
									.map((list) => (
										<div key={list.id} className="relative flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg flex-col" onClick={() => handleOpenStockList(list, username)}>
											<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
												Stock List
											</p>
											<FontAwesomeIcon icon={faX} className="absolute top-2 right-2 text-red-500 hover:text-red-300 text-md ml-4" onClick={(e) => handleDeleteList(list.id)}/>
											<h1 className="text-xl font-normal">{list.name}</h1>
											<PrivacyIcon privacy={list.privacy} />
											<span className="scale-75 ml-auto ">
						
											</span>
										</div>
									))}
							</div>
						<h1 className="text-base text-left w-fit tracking-wide uppercase">Friends' Stock Lists</h1>
						<div className="flex gap-4 flex-wrap w-full max-h-screen py-4">
								{friendLists
									.map((list) => (
										<div key={list.id} className="flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg flex-col" onClick={() => handleOpenStockList(list[1], list[0])}>
											<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
												Stock List
											</p>
											<h1 className="text-xl font-normal">{list[1].name}</h1>
											<h1 className="text-sm font-normal">Creator: {list[0]}</h1>
											<span className="scale-75 ml-auto ">
											</span>
										</div>
									))}
							</div>
							<h1 className="text-base text-left w-fit tracking-wide uppercase">Public Stock Lists</h1>
						<div className="flex gap-4 flex-wrap w-full max-h-screen py-4">
								{publicLists
									.map((list) => (
										<div key={list[1].id} className="flex gap-4 items-center px-8 hover:!bg-white transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg flex-col" onClick={() => handleOpenStockList(list[1], list[0])}>
											<p className="uppercase text-sm bg-gray-100  w-fit rounded-md px-2 py-1 ml-auto mr-auto">
												Stock List
											</p>
											<h1 className="text-xl font-normal">{list[1].name}</h1>
											<h1 className="text-sm font-normal">Creator: {list[0]}</h1>
											<span className="scale-75 ml-auto ">
						
											</span>
										</div>
									))}
							</div>
					</div>
				</div>
			</div>
		</>
	);
};

export default StocksManager;
