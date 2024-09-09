import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";;
import { faMoneyBillTransfer, faCreditCard } from "@fortawesome/free-solid-svg-icons";
import Button from "../components/Button";
import DepositWithdrawPopup from "./DepositWithdrawPopup";
import BuySellPopup from "./BuySellPopup";

const CashflowButton = (props, stockList) => {
  const [display, setDisplay] = useState(false);

  const toggleDisplay = () => {
    setDisplay(!display);
  };

  const choosePopup = () => {
    if (props.type == "cash"){
      return (
        <DepositWithdrawPopup toggle={toggleDisplay} id={props.id}/>
      )
    }
    else if (props.type == "stock"){
      return (
        <BuySellPopup toggle={toggleDisplay} id={props.id} stockList={stockList}/>
      )
    }
  }

  const cashflowType = () => {
    if (props.type == "cash"){
      return <><FontAwesomeIcon icon={faMoneyBillTransfer} style={{ marginRight: "8px" }}/> Deposit/Withdraw</>
    } else if (props.type == "stock") {
      return <><FontAwesomeIcon icon={faCreditCard} style={{ marginRight: "8px" }}/> Buy/Sell Stock</>
    }
    
  }

  return (
    <>
      <Button className={`${props.className} items-center`} onClick={toggleDisplay}>
        {props.type ? cashflowType() : null}
      </Button>
      {display ? choosePopup() : null}
    </>
  );
};

export default CashflowButton;
