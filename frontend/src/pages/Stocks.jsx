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
  const [search, setSearch] = useState("");

  // Filtered symbols based on search
  const filteredSymbols = symbolList.filter(symbolItem =>
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
  console.log(symbolList);

  // Format date to YYYY-MM-DD
  const formatDate = (date) => {
    return date ? date.toISOString().split('T')[0] : null;
  };

  return (
    <div className="flex h-screen">
      <div className="w-1/6 p-4 overflow-y-auto bg-gray-100">
        <h1>My Stocks</h1>
        <div className="mt-1">
          <div className={showDatePickers ? 'p-4 rounded-lg' : ''}>
            <button
              onClick={() => setShowDatePickers(!showDatePickers)}
              className="text-dark_red hover:underline hover:text-blue-600 font-bold py-2 px-2 rounded w-full text-left">
              {showDatePickers ? "Hide Date Range" : "Choose a Date Range"}
              <FontAwesomeIcon icon={showDatePickers ? faChevronUp : faChevronDown } className='ml-2 text-xl float-right' />
            </button>
            {showDatePickers && (
              <div className=" bg-white p-4 rounded-md shadow-md w-full">
                <div className="flex items-center mb-4">
                  <label className="block text-gray-700 font-bold mr-2">Start Date: </label>
                  <DatePicker
                    selected={startDate}
                    onChange={(date) => setStartDate(date)}
                    dateFormat="yyyy/MM/dd"
                    isClearable
                    placeholderText="Select a start date"
                    className="w-full p-2 border border-gray-300 rounded"
                  />
                </div>
                <div className='flex'>
                  <label className="block text-gray-700 font-bold">End Date: </label>
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
            onClick={() => setSymbol(symbolItem.symbol)}
            className="bg-dark_red hover:bg-red-800 text-white font-bold py-2 px-4 rounded w-full mb-2">
              {symbolItem.symbol}
          </button>
        ))}
        </div>
        <div className="w-3/4 p-4">
            <StocksGraph symbol={symbol} startDate={formatDate(startDate)} endDate={formatDate(endDate)}/>
        </div>
    </div>
  );
}

export default Stocks