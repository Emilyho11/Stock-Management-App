import { useEffect, useState, useRef } from "react";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";

const CreatePortfolioPopup = ({ toggle, username}) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [name, setName] = useState("");
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const navigate = useNavigate();
  const formRef = useRef(null);

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
    navigate(0); // Refresh the page
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
    try {
			AxiosClient.post(`portfolio/${username}/${name}`);
      toggle();
      navigate(0); // Refresh the page
		} catch (error) {
			console.error(error);
		}
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center transition-opacity duration-300 ${
        visible ? "opacity-100" : "opacity-0"
      } z-50`}
      onClick={handleToggle}
    >
      <div className="absolute inset-0 bg-black bg-opacity-40"></div>
      <div
        className="relative flex flex-col bg-white shadow-lg rounded-lg p-2 z-10"
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
          <h1 className="text-2xl">Create New Portfolio</h1>
          <label htmlFor="name">Name: </label>
          <input
            type="text"
            id="name"
            name="name"
            required
            className="border-2 p-2 rounded-sm"
            value={name}
            onChange={(e) => setName(e.target.value)}
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
}

export default CreatePortfolioPopup;
