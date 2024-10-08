import React, { useState } from "react";
import StockCard from "../components/StockCard";
import Card from "../components/Card";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";
import Button, { ButtonVariants } from "../components/Button";
import { Link } from "react-router-dom";
import ReviewBoard from "../components/ReviewBoard";
import PrivacyIcon from "../components/PrivacyIcon";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import CreateButton from "../components/CreateButton";
import AxiosClient from "../api/AxiosClient";
import { useAuth } from "../components/AuthContext";
 
const ManageStockList = () => {
	const { state } = useLocation();
  	const { stocklist, privacy, portfolio, isOwner } = state;
	const [stocks, setStocks] = React.useState([])
	const [selectedStock, setSelectedStock] = React.useState([]);
	const [stockPrice, setStockPrice] = React.useState(0);
	const [stockCov, setStockCov] = React.useState(0);
	const { getUsername, isLoggedIn } = useAuth();
	const [allStocks, setAllStocks] = React.useState([]);
	const username = getUsername();
	const navigate = useNavigate();

	useEffect(() => {
		const getAllStocks = async () => {
			try {
				const response = await AxiosClient.get("/stocks/");
				if (response.data) {
					const symbols = response.data.map(stock => stock.f_symbol.trim());
            		setAllStocks(symbols);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
			}
		};

		const getListedStocks = async () => {
		  try {
			const response = await AxiosClient.get(`stocklist/getStocks/${stocklist.id}`);
			if (response.data) {
				setStocks(response.data);
			} else {
			  console.error("Unexpected data format:", response.data);
			}
		  } catch (error) {
			console.error("Error fetching data:", error);
		  }
		}

		const getStockPrice = async () => {
			try {
				const response = await AxiosClient.get(`stocks/current/${selectedStock[0]}`)
				if (response.data) {
					setStockPrice(response.data)
				} else {
				  console.error("Unexpected data format:", response.data);
				}
			  } catch (error) {
				console.error("Error fetching data:", error);
			  }
		}

		const getStockCOV = async () => {
			try {
				const response = await AxiosClient.get(`stocks/COV/${selectedStock[0]}`)
				if (response.data) {
					setStockCov(response.data)
				} else {
				  console.error("Unexpected data format:", response.data);
				}
			  } catch (error) {
				console.error("Error fetching data:", error);
			  }
		}
		getAllStocks();
		getStockCOV();
		getListedStocks();
		getStockPrice();
	}, [selectedStock, isLoggedIn]);

	  const displayDetails = () => {
		if (selectedStock[0]){
			return (
				<div className="flex flex-col  my-4  gap-4">
					<div className="flex min-w-[20vw] flex-col gap-2">
					<h1 className="text-xl text-left">Information</h1>
					<p className="text-lg text-left">Current Market Value per Holding: ${stockPrice}</p>
					<p className="text-lg text-left">Current Market COV: {stockCov}</p>
					</div>
				</div>
			)
		} else {
			return (
			<div className="flex flex-row  my-4  gap-4">
					<h1 className="text-sm text-gray-500 text-left">No stock selected</h1>					
			</div>)
		}
	}
	
	const goBack = (e) => {
		e.preventDefault();
		return (portfolio ? (navigate(`/portfolio/${portfolio.id}`, { state: { portfolio: portfolio } })) : (navigate("/")));
	}

	const handlePrivacyClick = async () => {
		if (privacy.trim() === "public"){
			changePrivacy(stocklist.id, "friends")
		}
		if (privacy.trim() === "friends"){
			changePrivacy(stocklist.id, "private")			
		}
		if (privacy.trim() === "private"){
			console.log(stocklist.id)
			changePrivacy(stocklist.id, "public")	
		}
	}

	const changePrivacy = (id, newPrivacy) => {
		AxiosClient.patch(`stocklist/changePrivacy/${id}/${newPrivacy}`)
		navigate(`/stocklist/${id}`, { state: { stocklist: stocklist, privacy: newPrivacy, portfolio: portfolio, isOwner: isOwner}});
	}

	return (
		<div className="md:w-2/3 ml-auto mr-auto flex flex-col gap-2">
			<Link to={goBack} onClick={(e) => goBack(e)}>
				<Button className="flex items-center gap-4" variant={ButtonVariants.TRANSPARENT}>
					<FontAwesomeIcon icon={faArrowLeft} />
					<p className="font-semibold uppercase tracking-wide">Return</p>
				</Button>
			</Link>
			<Card className="flex flex-col min-h-[45vh] lg:min-h-[38vh] !bg-transparent !items-start !p-0 lg:flex-row">
				<div>
					<h1 className="text-left text-2xl">{stocklist.name}</h1>
					<div className="flex text-sm mb-2" onClick={(e) => {
						if (isOwner) {
							handlePrivacyClick(stocklist);
						}
					}}>
						<PrivacyIcon className={`pt-4 text-gray-500 ${isOwner ? "hover:text-blue-600 cursor-pointer" : ""}`} privacy={privacy}/>
					</div>
				</div>
				<Card className="w-full h-full !items-start flex-col py-8 px-12 bg-white">
					<div className="flex flex-row">
						{isOwner ? (<>
						<CreateButton className=" bg-green-500 hover:bg-green-800" username={username} type={"add"} id={stocklist.id} stockList={allStocks}/>
						<CreateButton className=" bg-red-500 hover:bg-red-800" username={username} type={"remove"} id={stocklist.id} stockList={allStocks}/>
						</>) : (<></>)}
					</div>			
					<ReviewBoard stockListId={stocklist.id} privacy={privacy} isOwner={isOwner} />
				</Card>
			</Card>
			<div className="flex gap-4 items-center justify-between">
				<h1 className="text-xl text-left">Stocks</h1>
			</div>
			<div className="flex my-4 gap-4">
				<div className="flex min-w-[20vw] flex-col gap-2 overflow-y-scroll h-52 lg:h-72">
					{
						stocks.map((stock, index) => (
							<button key={stock.symbol} onMouseDown={() => setSelectedStock(stock)}>
								<Card
									className={ (!(selectedStock[0] == stock[0]) ? ("flex gap-4 items-center hover:scale-100 hover:shadow-xl transition-all bg-white") : (
										"flex gap-4 items-center hover:scale-100 hover:shadow-xl transition-all bg-blue-500 text-white"
									))}
								>
									<div className="text-left">
										<h5 className="card-title">{stock[0]}</h5>
										<p className="card-text">Quantity: {stock[1]}</p>
									</div>
								</Card>
							</button>
						))
					}
				</div>

				<div className="flex-1 flex flex-col gap-2">
					<h1 className="text-xl text-left">Details</h1>
					<Card className="h-full bg-white">{displayDetails()}</Card>
				</div>
			</div>
		</div>
	);
};

export default ManageStockList;
