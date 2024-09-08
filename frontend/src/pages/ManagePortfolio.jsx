import React from "react";
import StockCard from "../components/StockCard";
import Card from "../components/Card";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faX, faChartSimple } from "@fortawesome/free-solid-svg-icons";
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
import { useAuth } from "../components/AuthContext";
import StatsMatrix from "../components/StatsMatrix";

const ManagePortfolio = () => {
	const [showStocks, setShowStocks] = React.useState(null);
	const { state } = useLocation();
  const { portfolio } = state;
	const [stocks, setStocks] = React.useState([]);
	const [stocklists, setStockLists] = React.useState([]);
	const [listStocks, setListStocks] = React.useState([]);
	const [selectedStock, setSelectedStock] = React.useState([]);
	const [selectedList, setSelectedList] = React.useState([]);
	const [stockPrice, setStockPrice] = React.useState(0);
	const [stockCov, setStockCov] = React.useState(0);
	const [showMatrix, setShowMatrix] = React.useState(false);
  const [matrixData, setMatrixData] = React.useState([]);
	const { getUsername, isLoggedIn } = useAuth();
	const username = getUsername();
	const navigate = useNavigate();
	const [allStocks, setAllStocks] = React.useState([]);

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
		}

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
		  }
		
		const getListStocks = async () => {
		
			if (selectedList){
				try {
					const response = await AxiosClient.get(`stocklist/getStocks/${selectedList.id}`);
					console.log(response.data)
					if (response.data) {
						const stonks = response.data;
					  setListStocks(stonks);
					} else {
					  console.error("Unexpected data format:", response.data);
					}
				  } catch (error) {
					console.error("Error fetching data:", error);
				  }
			}
		}

		const getStockPrice = async () => {
			try {
				const response = await AxiosClient.get(`stocks/current/${selectedStock[0]}`)
				if (response.data) {
					console.log(response.data)
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
		getStockPrice();
		getOwnedStocks();
		getPortfolioStockLists();
		getListStocks();
	  }, [selectedStock, selectedList, isLoggedIn]);

	const handleStockListDetails = () => {
		//directs them to the stock list page
		if (selectedList) {
			navigate(`/stocklist/${selectedList.id}`, { state: { stocklist: selectedList, privacy: selectedList.privacy, portfolio: portfolio, isOwner: true}});
		}
	}

	const handleDeleteList = (id) => {
		AxiosClient.delete(`stocklist/delete/${id}`);
		navigate(0);
	}	

	const displayDetails = () => {
		if ((showStocks || showStocks == null)  && selectedStock.length > 0){
			return (
				<Card className="h-full">
				<div className="flex flex-col my-4 gap-4">
					<div className="flex min-w-[20vw] flex-col gap-2">
					<h1 className="text-xl text-left">Information</h1>
					<p className="text-lg text-left">Current Market Value per Holding: ${stockPrice}</p>
					<p className="text-lg text-left">Current Market COV: {stockCov}</p>
					</div>
				</div>
				</Card>
			)
		} else if (showStocks == false && !Array.isArray(selectedList)){
			return (
				<div className="md:flex md:flex-row gap-4 pt-4">
					<div className="min-w-[20vw] flex flex-col items-center xl:gap-4">
						<Button className="h-1/7 w-3/4" onClick={() => handleStockListDetails()}>
							View Details
						</Button>
						<div className="xl:flex">
							<CreateButton className=" bg-green-500 hover:bg-green-800 w-[170px] xl:w-[130px] 2xl:w-[150px]" username={username} type={"add"} id={selectedList.id} stockList={allStocks}/>
							<CreateButton className=" bg-red-500 hover:bg-red-800 w-[170px] xl:w-[130px] 2xl:w-[150px]" username={username} type={"remove"} id={selectedList.id} stockList={allStocks}/>
						</div>
					</div>
					
					<div className={`flex w-full flex-col gap-2 h-52 ${listStocks.length > 0 ? 'overflow-y-scroll' : ''}`}>
					<div className="-space-y-1">
						<h1 className="text-xl text-left">Stocks</h1>
						<p className="text-left">Number of companies: {listStocks.length}</p>
						<p className="text-left">Number of stocks: {listStocks.reduce((acc, stock) => acc + stock[1], 0)}</p>
					</div>
					{listStocks.map((liststock, index) => (
						<button key={liststock[0]} className="min-w-[20vw] px-4 py-4 rounded-lg flex gap-4 items-center hover:shadow-xl transition-all bg-gray-800 text-white border-e-2 border-gray-300">
								<div className="text-left">
									<h5 className="card-title">{liststock[0]}</h5>
									<p className="card-text">Quantity: {liststock[1]}</p>
								</div>
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
			<Link to="/">
				<Button className="flex items-center gap-4" variant={ButtonVariants.TRANSPARENT}>
					<FontAwesomeIcon icon={faArrowLeft} />
					<p className="font-semibold uppercase tracking-wide">Home</p>
				</Button>
			</Link>
			<Card className="flex flex-col !bg-transparent !items-start !p-0 lg:flex-row mb-14">
				<h1 className="text-left text-2xl mb-2">{portfolio.name}</h1>
				<div className="bg-white w-full h-full rounded-lg flex-col">
					<div className="w-full h-full !items-start grid  md:grid-cols-2 2xl:grid-cols-4 py-8 px-16 rounded-lg">
						<CreateButton className='bg-blue-50'username={username} type={"createlistin"} id={portfolio.id}/>
						<CashflowButton className="bg-green-500 hover:bg-green-800" type={"cash"} id={portfolio.id}></CashflowButton>
						<CashflowButton className='bg-red-500 hover:bg-red-800' type={"stock"} id={portfolio.id}></CashflowButton>
						<Button onClick={() => setShowMatrix(true)} className='bg-gray-800 hover:bg-black'> <FontAwesomeIcon icon={faChartSimple} style={{ marginRight: "8px" }}/> Statistics </Button>
					</div>
					<div className="w-full h-full !items-start py-4 px-12 ">
						<PortfolioHistory id={portfolio.id}/>
					</div>
				</div>
			</Card>
			{showMatrix && (
						<div>
							<StatsMatrix companies={stocks.map(stock => stock[0])} onClose={() => { setShowMatrix(false); setMatrixData([]); }}/>
						</div>
					)}
			<div className="flex flex-row gap-4 pt-8">
				<div className="flex min-w-[20vw] flex-col gap-2 w-1/3">
					<div className="lg:flex gap-4 items-center justify-between">
						<h1 className="text-xl text-left w-fit">Stocks and Lists</h1>
						{/* 
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
						</div> */}

						<ul className="text-sm font-medium text-center text-gray-400 rounded-lg flex">
							<li className="w-full focus-within:z-10">
								<button
									onClick={() => setShowStocks(true)}
									className={
										"inline-block w-full p-2 px-4 bg-gray-400/50 border-r border-gray-200 hover:bg-gray-300 hover:text-gray-700 rounded-s-lg active focus:outline-none  " +
										((showStocks == true || showStocks == null) ? "!bg-dark_red/80 !text-white font-bold shadow" : "shadow-inner")
									}
									aria-current="page"
								>
									Stocks
								</button>
							</li>
							<li className="w-full focus-within:z-10 mr-5">
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
						<div className="overflow-y-scroll h-48 mb-8">
							{(showStocks == true || showStocks == null) ? (
								stocks.map((stock, index) => (
									<button className="w-full pt-1" key={stock.symbol} onMouseDown={() => setSelectedStock(stock)}>
										<Card
											className={ (!(selectedStock[0] == stock[0]) ? ("items-center scale-95 hover:shadow-xl transition-all bg-white") : (
												"items-center hover:scale-100 hover:shadow-xl transition-all bg-blue-500 text-white"
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
									<button className="w-full pt-1"key={list.id} onMouseDown={() => setSelectedList(list)}>
										<Card
											className={ ((selectedList.id != list.id) ? ("items-center scale-95 hover:scale-100 hover:shadow-xl transition-all bg-white") : (
												"items-center hover:scale-100 hover:shadow-xl transition-all bg-blue-500 text-white"
											))}
										>	<div className="text-left">
												<div className="absolute top-2 right-2">
													<button><FontAwesomeIcon icon={faX} className="text-red-500 hover:text-red-300 text-lg ml-4" onClick={(e) => handleDeleteList(selectedList.id)}/></button>
												</div>
												<p className="card-title text-xl">{list.name}</p>
												<PrivacyIcon privacy={list.privacy} className='text-sm' />
											</div>
										</Card>
									</button>
								))
							)}
						</div>
				</div>
				<div className="flex-1 flex flex-col gap-6 mb-8">
					<h1 className="text-xl text-left">Details</h1>
					<Card className="bg-white">{displayDetails()}</Card>
				</div>
			</div>
		</div>
	);
};

export default ManagePortfolio;
