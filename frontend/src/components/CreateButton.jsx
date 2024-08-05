import { useState } from "react";
import CreateListPopup from "./CreateListPopup"
import CreatePortfolioPopup from "./CreatePortfolioPopup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";;
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import Button from "../components/Button";

const CreateButton = ({username, type, id}) => {
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
        <CreateListPopup toggle={toggleDisplay} username={username}/>
      )
    }
  }

  const createType = () => {
    if (type == "stocklist" || type == "stocklistin"){
      return "Create Stock List"
    } else if (type == "portfolio") {
      return "Create Portfolio"
    }
  }

  return (
    <>
      <Button className="items-center gap-2 flex w-fit" onClick={toggleDisplay}>
        <FontAwesomeIcon icon={faPlus} />
        {type ? createType() : null}
      </Button>
      {display ? choosePopup(type) : null}
    </>
  );
};

export default CreateButton;
