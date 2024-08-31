import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import AxiosClient from "../api/AxiosClient";

const BuySellPopup = ({ toggle, id }) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [amount, setAmount] = useState("");
  const [type, setType] = useState("");
  const [symbol, setSymbol] = useState("");
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
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

  // Prevents the popup from closing when clicking inside the popup
  const handlePopupClick = (e) => {
    e.stopPropagation();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
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
      className={`fixed inset-0 flex items-center justify-center transition-opacity duration-300 w-full ${
        visible ? "opacity-100" : "opacity-0"
      }`}
      onClick={handleToggle}
    >
      {id ? (<div></div>): ( <div className="absolute inset-0 bg-black bg-opacity-40"></div>)}
     
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
          <h1 className="text-2xl">Buy or Sell a Stock</h1>
          <label htmlFor="symbol">Stock Symbol: </label>
          <input
            type="text" 
            id="symbol"
            name="symbol"
            required
            className="border-2 p-2 rounded-sm"
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
          />
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
}

export default BuySellPopup;