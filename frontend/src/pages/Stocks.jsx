import StocksGraph from "../components/StocksGraph";
import { useAuth } from "../components/AuthContext";
import React, { useState, useEffect } from "react";
import AxiosClient from "../api/AxiosClient";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronDown, faChevronUp } from "@fortawesome/free-solid-svg-icons";
import Button from "../components/Button";
import { useNavigate } from "react-router-dom";

const Stocks = () => {
    const { login } = useAuth();

    // Get stocks data from api
	const [dateBounds, setDateBounds] = useState({ min: null, max: null });
    const [symbol, setSymbol] = useState("");
    const [symbolList, setSymbolList] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [showDatePickers, setShowDatePickers] = useState(false);
    const [search, setSearch] = useState("");
    const [viewType, setViewType] = useState("present"); // New state for view type
    const [rate, setRate] = useState(10.0); // New state for rate
    const [time, setTime] = useState(1); // New state for time
    const [graphData, setGraphData] = useState({}); // State for graph data
    const decimalRate = rate / 100;
    const navigate = useNavigate();

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
        setGraphData({
            symbol,
            startDate: formatDate(startDate),
            endDate: formatDate(endDate),
            viewType,
            rate: decimalRate,
            time,
        });
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
        console.log("Future stocks submitted:", { rate, time });
        setGraphData({
            symbol,
            startDate: formatDate(startDate),
            endDate: formatDate(endDate),
            viewType,
            rate: decimalRate,
            time,
        });
    };

    // Handle symbol button click
    const handleSymbolClick = (symbol) => {
        setSymbol(symbol);
        setGraphData({
            symbol,
            startDate: formatDate(startDate),
            endDate: formatDate(endDate),
            viewType,
            rate: decimalRate,
            time,
        });
    };
	
    return (
        <div className="flex min-h-screen">
            <div className="w-1/6 p-4 overflow-y-auto bg-gray-100 max-h-screen">
                <h1>Stocks</h1>
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
                        onClick={() => handleSymbolClick(symbolItem.symbol)}
                        className="bg-dark_red hover:bg-red-800 text-white font-bold py-2 px-4 rounded w-full mb-2"
                    >
                        {symbolItem.symbol}
                    </button>
                ))}
            </div>
            <div className="w-3/4 p-4 h-fit">
                <div className="flex space-x-2 mb-4">
                    <Button onClick={() => setViewType("future")}>View Future Stocks</Button>
                    <Button onClick={() => setViewType("present")}>View Historical/Present Stocks</Button>
                </div>
                <div className="mt-1">
                    <div className={showDatePickers ? "p-4 rounded-lg" : ""}>
                        <button
                            onClick={() => setShowDatePickers(!showDatePickers)}
                            className="text-dark_red hover:underline hover:text-blue-600 font-bold py-2 px-2 rounded w-full text-left"
                        >
                            {showDatePickers ? "Hide Date Range" : "Choose a Date Range"}
                            <FontAwesomeIcon
                                icon={showDatePickers ? faChevronUp : faChevronDown}
                                className="ml-2 text-xl float-right"
                            />
                        </button>
                        {showDatePickers && (
                            <div className=" bg-white p-4 rounded-md shadow-md w-full">
                                <div className="flex items-center mb-4 gap-4">
                                    <label className="block text-gray-700 font-bold mr-2">Date Range: </label>
                                    <DatePicker
                                        selected={startDate}
                                        onChange={(date) => setStartDate(date)}
                                        dateFormat="yyyy/MM/dd"
										minDate = {dateBounds.min}
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
										</div>
                            </div>
                        )}
                    </div>
                </div>
                {viewType === "future" && (
                    <div className="flex space-x-4 mb-4">
                        <div>
                            <label className="block text-gray-700 font-bold mb-2">Rate (default 10%):</label>
                            <input
                                type="number"
                                step="1"
                                value={rate}
                                onChange={(e) => setRate(e.target.value)}
                                className="w-full p-2 border border-gray-300 rounded"
                                placeholder="Enter rate"
                            />
                        </div>
                        <div>
                            <label className="block text-gray-700 font-bold mb-2">Time in Years:</label>
                            <input
                                type="number"
                                step="1"
                                value={time}
                                onChange={(e) => setTime(e.target.value)}
                                className="w-full p-2 border border-gray-300 rounded"
                                placeholder="Enter time"
                            />
                        </div>
                        <Button onClick={handleFutureStocksSubmit}>Submit</Button>
                    </div>
                )}
                <StocksGraph
                    symbol={graphData.symbol}
                    startDate={graphData.startDate}
                    endDate={graphData.endDate}
                    viewType={graphData.viewType}
                    rate={graphData.rate}
                    time={graphData.time}
					setDateBounds={setDateBounds}
                />
            </div>
        </div>
    );
};

export default Stocks;