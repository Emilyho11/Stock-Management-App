import { useState } from "react";
import CreateListPopup from "./CreateListPopup"
import CreatePortfolioPopup from "./CreatePortfolioPopup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";;
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";
import Button from "../components/Button";
import AddRemovePopup from "./AddRemovePopup";

const CreateButton = ({className, username, type, id}) => {
  const [display, setDisplay] = useState(false);

  const toggleDisplay = () => {
    setDisplay(!display);
  };

  const choosePopup = (type) => {
    if (type == "stocklist"){
      return (
        <CreateListPopup toggle={toggleDisplay} username={username}/>
      )
    }
    else if (type == "portfolio"){
      return (
        <CreatePortfolioPopup toggle={toggleDisplay} username={username}/>
      )
    }
    else if (type == "createlistin"){
      return (
        <CreateListPopup toggle={toggleDisplay} username={username} id={id}/>
      )
    } else if (type == "add" || type == "remove") {
      return (
        <AddRemovePopup toggle={toggleDisplay} type={type} id={id}/>
      )
    }
  }

  const createType = () => {
    if (type == "stocklist" || type == "createlistin"){
      return "Create Stock List"
    } else if (type == "portfolio") {
      return "Create Portfolio"
    } else if (type == "add") {
      return "Add Stock"
    } else if (type == "remove") {
      return "Remove Stock"
    }
    
  }

  return (
    <>
      <Button className={`${className} items-center`} onClick={toggleDisplay}>
        {type == "remove" ? (<FontAwesomeIcon icon={faMinus} style={{ marginRight: "8px" }}/>):(<FontAwesomeIcon icon={faPlus} style={{ marginRight: "8px" }}/>)}
        {type ? createType() : null}
      </Button>
      {display ? choosePopup(type) : null}
    </>
  );
};

export default CreateButton;
