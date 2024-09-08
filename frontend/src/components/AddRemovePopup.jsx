import { useEffect, useState, useRef } from "react";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";

const AddRemovePopup = ({ toggle, type, id, stockList }) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [amount, setAmount] = useState("");
  const [symbol, setSymbol] = useState("");
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [filteredSymbols, setFilteredSymbols] = useState([]);
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

  const handleToggle = () => {
    toggle();
  };

  const handleFormChange = () => {
    setIsButtonDisabled(!formRef.current.checkValidity());
  };

  //Prevents the popup from closing when clicking inside the popup
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
    if (!stockList.includes(symbol.trim().toUpperCase())) {
      alert("Stock does not exist");
      return;
    }
    try {
      const response = await AxiosClient.get(`stocklist/check/${id}/${symbol}`);
      const ifListed = response.data;
      if (ifListed == -1){
        try {
          AxiosClient.post(`stocklist/list/${id}/${symbol}/${amount}/${type}`);
          toggle();
          navigate(0);
        } catch (error) {
          console.error(error);
        }
      }
      else if (ifListed > -1) {
        try {
          AxiosClient.patch(`stocklist/list/${id}/${symbol}/${amount}/${type}`);
          toggle();
          navigate(0);
        } catch (error) {
          console.error(error);
        }
      }
    } catch (error) {
      console.error(error);
    }
  };

  const checkAdd = () => {
    return (type === "add");
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center transition-opacity duration-300 w-full ${
        visible ? "opacity-100" : "opacity-0"
      }`}
      onClick={handleToggle}
    >
      {id ? (<div></div>) : ( <div className="absolute inset-0 bg-black bg-opacity-40"></div>)}
     
      <div
        className="relative flex flex-col bg-white shadow-2xl border-b-2 border-gray-100 rounded-lg p-2 z-10"
        onClick={handlePopupClick}
      >
        <button
          className="absolute top-1 right-3 text-lg text-red-600 hover:text-red-300"
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
          <h1 className="text-2xl">{checkAdd() ? ("Add Stock to the List"):("Remove Stock From the List")}</h1>
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
            min="1" 
            step="1"
            id="amount"
            name="amount"
            required
            className="border-2 p-2 rounded-sm"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
          <button
            type="submit"
            className={`p-2 rounded-lg ${
              isButtonDisabled ? "bg-gray-300" : "bg-blue-500 text-white"
            }`}
            disabled={isButtonDisabled}
          >
            Submit
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddRemovePopup;
