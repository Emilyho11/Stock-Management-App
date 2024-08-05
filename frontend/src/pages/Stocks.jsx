import StocksGraph from '../components/StocksGraph'
import { useAuth } from '../components/AuthContext'
import React, { useState, useEffect } from 'react'
import AxiosClient from '../api/AxiosClient'
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronUp } from '@fortawesome/free-solid-svg-icons';

const Stocks = () => {
    const { login } = useAuth();
    login();

    // Get stocks data from api
	const [symbol, setSymbol] = useState("");
    const [symbolList, setSymbolList] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [showDatePickers, setShowDatePickers] = useState(false);

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

    return (
        <>
            <h1>My Stocks</h1>
            {symbolList.map((symbolItem, index) => (
                <button
                    key={index}
                    onClick={() => setSymbol(symbolItem.symbol)}
                    className="bg-dark_red hover:bg-red-800 text-white font-bold py-2 px-4 rounded">
                        {symbolItem.symbol}
                </button>
            ))}
            <div className="mt-4 flex justify-center">
            <div className={showDatePickers ? 'bg-slate-400 p-4 rounded-lg w-full max-w-md' : 'w-full max-w-md'}>
                <button
                    onClick={() => setShowDatePickers(!showDatePickers)}
                    className="text-dark_red hover:underline hover:text-blue-600 font-bold py-2 px-4 rounded w-full text-left">
                    {showDatePickers ? "Hide More Filters" : "Show More Filters"}
                    <FontAwesomeIcon icon={showDatePickers ? faChevronUp : faChevronDown } className='ml-2 text-xl float-right' />
                </button>
                {showDatePickers && (
                    <div className="mt-4 bg-white p-4 rounded-lg shadow-lg">
                        <div className="mb-4">
                            <label className="block text-gray-700 font-bold mb-2">Start Date: </label>
                            <DatePicker
                                selected={startDate}
                                onChange={(date) => setStartDate(date)}
                                dateFormat="yyyy/MM/dd"
                                isClearable
                                placeholderText="Select a start date"
                                className="w-full p-2 border border-gray-300 rounded"
                            />
                        </div>
                        <div>
                            <label className="block text-gray-700 font-bold mb-2">End Date: </label>
                            <DatePicker
                                selected={endDate}
                                onChange={(date) => setEndDate(date)}
                                dateFormat="yyyy/MM/dd"
                                isClearable
                                placeholderText="Select an end date"
                                className="w-full p-2 border border-gray-300 rounded"
                            />
                        </div>
                    </div>
                )}
            </div>
            </div>
        <StocksGraph symbol={symbol} startDate={startDate} endDate={endDate}/>
        </>
    );
}

export default Stocks