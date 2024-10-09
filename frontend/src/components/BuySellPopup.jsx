import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import AxiosClient from "../api/AxiosClient";

const BuySellPopup = ({ toggle, id, stockList }) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [amount, setAmount] = useState("");
  const [type, setType] = useState("");
  const [symbol, setSymbol] = useState("");
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [filteredSymbols, setFilteredSymbols] = useState([]);
  const [balance, setBalance] = useState(0);
  const [stockPrice, setStockPrice] = useState(0);
  const [boughtStocks, setBoughtStocks] = useState([]);
  const formRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Check form validity on initial render
    setIsButtonDisabled(!formRef.current.checkValidity());
  }, []);

  useEffect(() => {
    if (!isLoading) {
      setVisible(true);
      return;
    }
    setIsLoading(false);
  }, [isLoading]);

  useEffect(() => {
    // Fetch balance and stock price when the component mounts
    const fetchData = async () => {
      await getBalance();
      await getStockPrice(symbol);
    };
    fetchData();
  }, [symbol]);

  const handleToggle = () => {
    toggle();
  };

  const getBalance = async () => {
    try {
      const response = await AxiosClient.get(`portfolio/getBalance/${id}`);
      if (response.data) {
        setBalance(response.data);
      } else {
        console.error("Unexpected data format:", response.data);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const getStockPrice = async (symbol) => {
    try {
      const response = await AxiosClient.get(`stocks/current/${symbol}`)
      if (response.data) {
        setStockPrice(response.data)
      } else {
        console.error("Unexpected data format:", response.data);
      }
      } catch (error) {
      console.error("Error fetching data:", error);
      }
  };

  // Get list of bought stocks
  const getBoughtStocks = async () => {
    try {
      const response = await AxiosClient.get(`portfolio//getStocks/${id}`);
      if (response.data) {
        setBoughtStocks(response.data);
      } else {
        console.error("Unexpected data format:", response.data);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const handleFormChange = () => {
    setIsButtonDisabled(!formRef.current.checkValidity());
  };

  // Prevents the popup from closing when clicking inside the popup
  const handlePopupClick = (e) => {
    e.stopPropagation();
  };

  const handleSymbolChange = (e) => {
    const input = e.target.value.toUpperCase();
    setSymbol(input);
    if (input) {
      const filtered = stockList.filter(stock => stock.includes(input));
      setFilteredSymbols(filtered);
    } else {
      setFilteredSymbols([]);
    }
  };

  const handleSymbolSelect = (symbol) => {
    setSymbol(symbol);
    setFilteredSymbols([]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const trimmedSymbol = symbol.trim().toUpperCase();
    if (!stockList.includes(trimmedSymbol)) {
      alert("Stock does not exist");
      return;
    }
    if (type == "sell" && !boughtStocks.includes(trimmedSymbol)) {
      alert("You do not own this stock");
      return;
    }
    if (amount <= 0) {
      alert("Amount must be greater than 0");
      return;
    }
    if (balance < stockPrice * amount && type == "buy") {
      alert("Insufficient balance. Please deposit more money.");
      return;
    }
    try {
      const response = await AxiosClient.get(`portfolio/check/${id}/${symbol}`);
      const ifBought = response.data;
      const url = `portfolio/trade/${id}/${symbol}/${amount}/${type}`;
      
      if (ifBought == -1) {
        await AxiosClient.post(url);
      } else if (ifBought > -1) {
        await AxiosClient.patch(url);
      }
      
      toggle();
      navigate(0); // Refresh the page
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div
      className={`fixed inset-0 -top-24 flex items-center justify-center transition-opacity duration-300 w-full h-[26rem] ${
        visible ? "opacity-100" : "opacity-0"
      }`}
      onClick={handleToggle}
    >
      {id ? (<div></div>): ( <div className="absolute inset-0 bg-black bg-opacity-40"></div>)}
     
      <div
        className="relative flex flex-col bg-white shadow-2xl border-b-2 border-gray-100 rounded-lg p-2 mt-20 max-h-full overflow-auto"
        onClick={handlePopupClick}
      >
        <button
          className="absolute top-1 right-3 text-lg text-red-600 hover:text-red-300 cursor-pointer"
          onClick={handleToggle}
        >
          x
        </button>
        <form
          className="flex flex-col gap-4 p-4"
          onSubmit={handleSubmit}
          onChange={handleFormChange}
          ref={formRef}
        >
          <h1 className="text-2xl">Buy or Sell a Stock</h1>
          <label htmlFor="symbol">Stock Symbol: </label>
          <input
            type="text" 
            id="symbol"
            name="symbol"
            required
            className="border-2 p-2 rounded-sm"
            value={symbol}
            onChange={handleSymbolChange}
          />
          {filteredSymbols.length > 0 && (
            <ul className="border-2 border-gray-200 rounded-sm max-h-40 overflow-y-auto">
            {filteredSymbols.map((stock, index) => (
              <li
                key={index}
                className="p-2 cursor-pointer hover:bg-gray-200"
                onClick={() => handleSymbolSelect(stock)}
              >
                {stock}
              </li>
            ))}
            </ul>
          )}
          <label htmlFor="amount">Amount: </label>
          <input
            type="number" 
            min="0" 
            step="1"
            id="amount"
            name="amount"
            required
            className="border-2 p-2 rounded-sm"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
          <label htmlFor="type">Type: </label>
            <select
              id="type"
              name="type"
              required
              className="border-2 p-2 rounded-sm"
              onChange={(e) => setType(e.target.value)}
              defaultValue=""
            >
              <option value="" disabled>
                Select an action
              </option>
                <option key={1} value={`buy`}>
                  Buy
                </option>
                <option key={2} value={`sell`}>
                  Sell
                </option>
            </select>
          <button
            type="submit"
            className={`p-2 rounded-lg ${
              isButtonDisabled ? "bg-gray-300" : "bg-blue-500 hover:bg-blue-300 text-white"
            }`}
            disabled={isButtonDisabled}
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
}

export default BuySellPopup;