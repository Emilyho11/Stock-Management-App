import React from "react";
import StockCard from "../components/StockCard";
import Card from "../components/Card";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faMoneyBillTransfer, faCreditCard, faChartSimple } from "@fortawesome/free-solid-svg-icons";
import Button, { ButtonVariants } from "../components/Button";
import { Link } from "react-router-dom";
import ReviewBoard from "../components/ReviewBoard";
import PrivacyIcon from "../components/PrivacyIcon";
import { useLocation } from "react-router-dom";
import { useEffect } from "react";
import AxiosClient from "../api/AxiosClient";
import CreateButton from "../components/CreateButton";
import CashflowButton from "../components/CashflowButton";
import { useNavigate } from "react-router-dom";
import PortfolioHistory from "../components/PortfolioHistory";

const ManagePortfolio = () => {
	const [showStocks, setShowStocks] = React.useState(null);
	const { state } = useLocation();
    const { portfolio } = state;
	const [stocks, setStocks] = React.useState([])
	const [stocklists, setStockLists] = React.useState([])
	const [selectedStock, setSelectedStock] = React.useState([]);
	const [selectedList, setSelectedList] = React.useState([]);
	const username = "mirihuang" //replace with logged in user
	const navigate = useNavigate();

	useEffect(() => {
		const getOwnedStocks = async () => {
		  try {
			const response = await AxiosClient.get(`portfolio/getStocks/${portfolio.id}`);
			if (response.data) {
				setStocks(response.data);
			} else {
			  console.error("Unexpected data format:", response.data);
			}
		  } catch (error) {
			console.error("Error fetching data:", error);
		  }
		};

		const getPortfolioStockLists = async () => {
			try {
			  const response = await AxiosClient.get(`stocklist/get/id/${portfolio.id}`);
			  if (response.data) {
				setStockLists(response.data);
			  } else {
				console.error("Unexpected data format:", response.data);
			  }
			} catch (error) {
			  console.error("Error fetching data:", error);
			}
		  };
	
		getOwnedStocks();
		getPortfolioStockLists();
	  }, []);

	const handleStockListDetails = () => {
		//directs them to the stock list page
		if (selectedList){
			navigate(`/stocklist/${selectedList.id}`, { state: { stocklist: selectedList, portfolio: portfolio}});
		}
	}

	const displayDetails = () => {
		if ((showStocks || showStocks == null)  && selectedStock.length > 0){
			return (
				<p>temporary info, need to fill with stock stuff</p>
			)
		} else if (showStocks == false && !Array.isArray(selectedList)){
			console.log(selectedList)
			return (
				<div className="flex flex-row  my-4  gap-4">
					<div className="flex min-w-[20vw] flex-col gap-2">
						<Button className="h-1/7" onClick={() => handleStockListDetails()}>
							View Details
						</Button>
						<CreateButton className="h-1/4" username={username} type={"addstock"} id={selectedList.id}/>
					</div>
					
					<div className="flex min-w-[20vw] flex-col gap-2">
					<h1 className="text-xl text-left">Stocks</h1>
					{stocks.map((stock, index) => (
					<button key={stock.symbol} onMouseDown={() => setSelectedStock(stock)}>
						<Card
							className={ (!(selectedStock[0] == stock[0]) ? ("flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-white") : (
								"flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-blue-500 text-white"
							))}
						>
							<div className="text-left">
								<h5 className="card-title">{stock[0]}</h5>
								<p className="card-text">Quantity: {stock[1]}</p>
							</div>
						</Card>
					</button>
					))}
					
					</div>
					
				</div>
				
				
			)
		} else if (selectedStock.length == 0 || selectedList.length == 0) {
			return (
				<div className="flex flex-row  my-4  gap-4">
						<h1 className="text-sm text-gray-500 text-left">Nothing selected</h1>					
				</div>)
		}
	}

	return (
		<div className="md:w-2/3 ml-auto mr-auto flex flex-col gap-2">
			<Link to="/stock-manager">
				<Button className="flex items-center gap-4" variant={ButtonVariants.TRANSPARENT}>
					<FontAwesomeIcon icon={faArrowLeft} />
					<p className="font-semibold uppercase tracking-wide">Home</p>
				</Button>
			</Link>
			<Card className="min-h-[50vh] !bg-transparent !items-start !p-0 max-lg:flex-col">
				<div className="scale-75 ml-auto ">
					<h1 className="text-left text-4xl">{portfolio.name}</h1>
				</div>
				<div className="bg-white w-full h-full rounded-lg flex-col">
					<div className="w-full h-full !items-start flex flex-row py-8 px-12  rounded-lg">
						<CreateButton className='bg-blue-50'username={username} type={"createlistin"} id={portfolio.id}/>
						<CashflowButton className="bg-green-500 hover:bg-green-800" type={"cash"} id={portfolio.id}></CashflowButton>
						<CashflowButton className='bg-red-500 hover:bg-red-800' type={"stock"} id={portfolio.id}></CashflowButton>
						<Button className='bg-gray-800 hover:bg-black'> <FontAwesomeIcon icon={faChartSimple} /> Statistics </Button>
					</div>
					<div className="w-full h-full !items-start py-8 px-12 ">
						<PortfolioHistory id={portfolio.id}/>
					</div>
				</div>
				
			</Card>
			{/* <hr className="mb-2" /> */}
			<div className="flex flex-row  my-4  gap-4">
				<div className="flex min-w-[20vw] flex-col gap-2">
					<div className="flex gap-4 items-center justify-between">
						<h1 className="text-xl text-left w-fit">Stocks and Lists</h1>

						<div className="sm:hidden">
							<label for="tabs" className="sr-only">
								Portfolio Type
							</label>
							<select
								id="tabs"
								className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
							>
								<option>Stocks</option>
								<option>Lists</option>
							</select>
						</div>

						<ul className="hidden text-sm font-medium text-center text-gray-400 rounded-lg sm:flex">
							<li className="w-full focus-within:z-10">
								<button
									onClick={() => setShowStocks(true)}
									className={
										"inline-block w-full p-2 px-4 bg-gray-400/50 border-r border-gray-200 hover:bg-gray-300 rounded-s-lg active focus:outline-none  " +
										((showStocks == true || showStocks == null) ? "!bg-dark_red/80 !text-white font-bold shadow" : "shadow-inner")
									}
									aria-current="page"
								>
									Stocks
								</button>
							</li>
							<li className="w-full focus-within:z-10">
								<button
									onClick={() => setShowStocks(false)}
									className={
										"inline-block w-full p-2 px-4  bg-gray-400/50 border-s-0 border-gray-200 rounded-e-lg hover:text-gray-700 hover:bg-gray-300 focus:outline-none " +
										((showStocks == false) ? "!bg-dark_red/80 !text-white shadow" : "shadow-inner")
									}
								>
									Lists
								</button>
							</li>
						</ul>
					</div>
					{(showStocks == true || showStocks == null) ? (
						stocks.map((stock, index) => (
							<button key={stock.symbol} onMouseDown={() => setSelectedStock(stock)}>
								<Card
									className={ (!(selectedStock[0] == stock[0]) ? ("flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-white") : (
										"flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-blue-500 text-white"
									))}
								>
									<div className="text-left">
										<h5 className="card-title">{stock[0]}</h5>
										<p className="card-text">Quantity: {stock[1]}</p>
									</div>
								</Card>
							</button>
						))
					) : (
						stocklists.map((list, index) => (
							<button key={list.id} onMouseDown={() => setSelectedList(list)}>
								<Card
									className={ ((selectedList.id != list.id) ? ("flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-white") : (
										"flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all bg-blue-500 text-white"
									))}
								>									<div className="text-left">
										<h5 className="card-title">{list.name}</h5>
										<PrivacyIcon privacy={list.privacy} />
									</div>
								</Card>
							</button>
						))
					)}
				</div>

				<div className="flex-1 flex flex-col gap-2">
					<h1 className="text-xl text-left">Details</h1>
					<Card className="h-full bg-white">{displayDetails()}</Card>
				</div>
			</div>
		</div>
	);
};

export default ManagePortfolio;
