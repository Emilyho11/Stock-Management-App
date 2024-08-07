import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";;
import { faUserPlus } from "@fortawesome/free-solid-svg-icons";
import Button from "../components/Button";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";

const SendFriendRequestButton = ({className, username, target}) => {
  const navigate = useNavigate();

  const sendFriendReq = () => {
    try {
      AxiosClient.post(`friends/${username}/${target}`);
      navigate(0);
    } catch (error) {
      console.error(error);
    }
  }

  return (
      <Button className={`${className} items-center gap-2 flex w-fit`} onClick={sendFriendReq}>
        <FontAwesomeIcon icon={faUserPlus} />
        Send Friend Request
      </Button>
  );
};

export default SendFriendRequestButton;
