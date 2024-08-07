import StocksGraph from "../components/StocksGraph";
import { useAuth } from "../components/AuthContext";
import React, { useState, useEffect } from "react";
import AxiosClient from "../api/AxiosClient";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronDown, faChevronUp } from "@fortawesome/free-solid-svg-icons";
import Button, { ButtonVariants } from "../components/Button";
import { useNavigate } from "react-router-dom";
import CreateStockForm from "../components/CreateStockForm";

const Stocks = () => {
    const { login } = useAuth();

    // Get stocks data from api
	const [dateBounds, setDateBounds] = useState({ min: null, max: null });
    const [symbol, setSymbol] = useState("");
    const [symbolList, setSymbolList] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [search, setSearch] = useState("");
    const [viewType, setViewType] = useState("present"); // New state for view type
    const [time, setTime] = useState(0.1); // New state for time
    const [graphData, setGraphData] = useState({}); // State for graph data
    const [clickedSymbol, setClickedSymbol] = useState(null);
    const [clickedButton, setClickedButton] = useState(null); // New state for clicked button
    const [showCreateStockForm, setShowCreateStockForm] = useState(false); // State to show/hide create stock form
    const [buttonColor, setButtonColor] = useState("bg-dark_red");

    // Filtered symbols based on search
    const filteredSymbols = symbolList.filter((symbolItem) =>
        symbolItem.symbol.toLowerCase().includes(search.toLowerCase())
    );

    // Get all the different companies (all symbols)
    useEffect(() => {
        const fetchSymbols = async () => {
            try {
                const response = await AxiosClient.get("stocks/");
                if (response.data && Array.isArray(response.data)) {
                    setSymbolList(response.data);
                } else {
                    console.error("Unexpected data format:", response.data);
                }
            } catch (error) {
                console.error("Error fetching symbols:", error);
            }
        };

        fetchSymbols();
    }, []);

	useEffect(() => {
		if (dateBounds.min && dateBounds.max) {
		setStartDate(dateBounds.min);
		setEndDate(dateBounds.max);
		}
	}, [dateBounds]);
    // Format date to YYYY-MM-DD
    const formatDate = (date) => {
        return date ? date.toISOString().split("T")[0] : null;
    };

    // Handle submit for date range
    const handleDateRangeSubmit = () => {
        // Log the current state values
        console.log("Date range submitted:", { startDate, endDate });
        setGraphData((prev) => ({
            ...prev, 
            startDate: formatDate(startDate), 
            endDate: formatDate(endDate)}));
    };

	useEffect( () => {
		if (!startDate || !endDate) {
			return;
		}
		console.log("Date bounds changed:", { startDate, endDate });
		handleDateRangeSubmit();
	}, [startDate, endDate]);

    // Handle submit for future stocks
    const handleFutureStocksSubmit = () => {
        // Log the current state values
        setGraphData({
            symbol,
            startDate: formatDate(startDate),
            endDate: formatDate(endDate),
            viewType,
            time,
        });
    };

    // Handle symbol button click
    const handleSymbolClick = (symbol) => {
        setSymbol(symbol);
        setClickedSymbol(symbol);
        setGraphData({
            symbol,
            startDate: formatDate(startDate),
            endDate: formatDate(endDate),
            viewType,
            time,
        });
    };

    const toggleColor = () => {
        setButtonColor(prevColor => prevColor === "bg-dark_red" ? "bg-red-800" : "bg-dark_red");
    };
	
    return (
        <div className="flex min-h-screen">
            <div className="w-1/6 p-4 overflow-y-auto bg-gray-100 max-h-screen">
                <h1>Stocks</h1>
                <Button
                    onClick={() => {
                        setShowCreateStockForm(!showCreateStockForm);
                        setClickedButton("create");
                        toggleColor();
                    }}
                    className={`${buttonColor} hover:bg-red-800 font-bold rounded mb-4`}
                >
                    + Create Stock
                </Button>
                <input
                    type="text"
                    placeholder="Search for a stock symbol..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="w-full p-2 mb-4 border border-gray-300 rounded"
                />
                {filteredSymbols.map((symbolItem, index) => (
                    <button
                        key={index}
                        onClick={() => {
                            handleSymbolClick(symbolItem.symbol);
                            setShowCreateStockForm(false);
                        }}
                        className={`${
                            clickedSymbol === symbolItem.symbol ? 'bg-red-800' : 'bg-dark_red'
                        } hover:bg-red-800 text-white font-bold py-2 px-4 rounded w-full mb-2`}
                    >
                        {symbolItem.symbol}
                    </button>
                ))}
                
            </div>
            
            <div className="w-3/4 p-4 h-fit">
                <div className="flex space-x-2 mb-4">
                    <Button
                        onClick={() => {
                            setViewType("future");
                            setClickedButton("future");
                            setShowCreateStockForm(false);
                        }}
                        className={`${
                            clickedButton === "future" ? 'bg-blue-400' : ''
                        }`}
                    >
                        View Future Stocks
                    </Button>
                    <Button
                        onClick={() => {
                            setViewType("present");
                            handleDateRangeSubmit();
                            setClickedButton("present");
                            setShowCreateStockForm(false);
                        }}
                        className={`${
                            clickedButton === "present" ? 'bg-blue-400' : ''
                        }`}
                    >
                        View Historical/Present Stocks
                    </Button> 
                </div>
                {showCreateStockForm && (
                    <CreateStockForm />
                )}
                {!showCreateStockForm && (
                    <>
                        <div className="mt-1">
                            <div className="bg-white p-4 rounded-md w-full">
                                <div className="flex items-center mb-4 gap-4">
                                    {viewType === "present" ? (
                                        <>
                                            <label className="block text-gray-700 font-bold mr-2">Date Range: </label>
                                            <DatePicker
                                                selected={startDate}
                                                onChange={(date) => setStartDate(date)}
                                                dateFormat="yyyy/MM/dd"
                                                minDate={dateBounds.min}
                                                maxDate={endDate ? endDate : dateBounds.max}
                                                isClearable
                                                placeholderText="Select a start date"
                                                className="w-full p-2 border border-gray-300 rounded"
                                            />
                                            <p className="">-</p>
                                            <DatePicker
                                                selected={endDate}
                                                onChange={(date) => setEndDate(date)}
                                                minDate={startDate ? startDate : dateBounds.min}
                                                maxDate={dateBounds.max}
                                                dateFormat="yyyy/MM/dd"
                                                isClearable
                                                placeholderText="Select an end date"
                                                className="w-full p-2 border border-gray-300 rounded"
                                            />
                                            <Button onClick={handleDateRangeSubmit}>Submit</Button>
                                        </>
                                    ) : (
                                        <>
                                            <div className="inline-flex items-center">
                                                <label className="min-w-fit block text-gray-700 font-bold mb-2 mr-2">Time in Years</label>
                                                <input
                                                    type="number"
                                                    step="0.1"
                                                    value={time}
                                                    onChange={(e) => {
                                                        const value = e.target.value;
                                                        if (value > 0) {
                                                            setTime(value);
                                                        }
                                                    }}
                                                    className="w-full p-2 border border-gray-300 rounded"
                                                    placeholder="Enter time"
                                                />
                                            </div>
                                            <Button onClick={handleFutureStocksSubmit}>Submit</Button>
                                        </>
                                    )}
                                </div>
                            </div>
                        </div>
                        <StocksGraph
                            symbol={graphData.symbol}
                            startDate={graphData.startDate}
                            endDate={graphData.endDate}
                            viewType={viewType}
                            time={graphData.time}
                            setDateBounds={setDateBounds}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default Stocks;