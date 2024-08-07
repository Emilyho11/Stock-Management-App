import { useEffect, useState, useRef } from "react";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";

const DepositWithdrawPopup = ({ toggle, id}) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [amount, setAmount] = useState("");
  const [type, setType] = useState("");
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
    navigate(0);
  };

  const handleFormChange = () => {
    setIsButtonDisabled(!formRef.current.checkValidity());
  };

  //Prevents the popup from closing when clicking inside the popup
  const handlePopupClick = (e) => {
    e.stopPropagation();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (type == "withdraw"){
      try {
        AxiosClient.post(`portfolio/withdraw/${id}/${amount}`);
        toggle();
        navigate(0);
      } catch (error) {
        console.error(error);
      }
    } else if (type == "deposit") {
      try {
        AxiosClient.post(`portfolio/deposit/${id}/${amount}`);
        toggle();
        navigate(0);
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center transition-opacity duration-300 ${
        visible ? "opacity-100" : "opacity-0"
      }`}
      onClick={handleToggle}
    >
      {id ? (<div></div>): ( <div className="absolute inset-0 bg-black bg-opacity-40"></div>)}
     
      <div
        className="relative flex flex-col bg-white shadow-lg rounded-lg p-2 z-10"
        onClick={handlePopupClick}
      >
        <button
          className="absolute top-1 right-3 text-lg text-gray-300"
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
          <h1 className="text-2xl">Deposit or Withdraw Cash</h1>
          <label htmlFor="amount">Amount: </label>
          <input
            type="number" 
            min="0" 
            step="0.01"
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
                <option key={1} value={`deposit`}>
                  Deposit
                </option>
                <option key={2} value={`withdraw`}>
                  Withdraw
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

export default DepositWithdrawPopup;
