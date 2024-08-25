import { useEffect, useState, useRef } from "react";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";

const CreateListPopup = ({ toggle, username, id}) => {
  const [visible, setVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [name, setName] = useState("");
  const [privacy, setPrivacy] = useState("");
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

  //Prevents the popup from closing when clicking inside the popup
  const handlePopupClick = (e) => {
    e.stopPropagation();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(name)
    console.log(privacy)
    if (!id){
      try {
        AxiosClient.post(`stocklist/${username}/${name}/${privacy}`);
        toggle();
        navigate(0); // Refresh the page
      } catch (error) {
        console.error(error);
      }
    } else {
      try {
        AxiosClient.post(`stocklist/${username}/${id}/${name}/${privacy}`);
        toggle();
        navigate(0); // Refresh the page
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center transition-opacity duration-300 ${
        visible ? "opacity-100" : "opacity-0"
      } z-50`}
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
          <h1 className="text-2xl">Create New Stock List</h1>
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
          <label htmlFor="privacy">Privacy: </label>
            <select
              id="privacy"
              name="Privacy"
              required
              className="border-2 p-2 rounded-sm"
              onChange={(e) => setPrivacy(e.target.value)}
              defaultValue=""
            >
              <option value="" disabled>
                Select a privacy setting
              </option>
                <option key={1} value={`private`}>
                  Private
                </option>
                <option key={2} value={`friends`}>
                  Friends
                </option>
                <option key={3} value={`public`}>
                  Public
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

export default CreateListPopup;
