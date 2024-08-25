import React, { useState } from "react";
import Card from "../components/Card";
import { useAuth } from "../components/AuthContext";
import { useEffect } from "react";
import AxiosClient from "../api/AxiosClient";
import SendFriendRequestButton from "../components/SendFriendRequestButton";
import { faCircleCheck, faCircleXmark, faX } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";

const Friends = () => {
	const {getUsername, isLoggedIn} = useAuth();
	const user = getUsername();
	const [incoming, setIncoming] = useState([]);
	const [outgoing, setOutgoing] = useState([]);
	const [friends, setFriends] = useState([]);
	const [targetname, setTargetname] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		const getFriends = async () => {
		  try {
			const response = await AxiosClient.get(`friends/viewFriends/${user}`);
			if (response.data) {
				setFriends(response.data);
			} else {
			  console.error("Unexpected data format:", response.data);
			}
		  } catch (error) {
			console.error("Error fetching data:", error);
		  }
		};

		const getIncoming = async () => {
			try {
			  const response = await AxiosClient.get(`friends/viewIncoming/${user}`);
			  if (response.data) {
				setIncoming(response.data);
			  } else {
				console.error("Unexpected data format:", response.data);
			  }
			} catch (error) {
			  console.error("Error fetching data:", error);
			}
		  };
		
		  const getSent = async () => {
			try {
			  const response = await AxiosClient.get(`friends/viewSent/${user}`);
			  if (response.data) {
				setOutgoing(response.data);
			  } else {
				console.error("Unexpected data format:", response.data);
			  }
			} catch (error) {
			  console.error("Error fetching data:", error);
			}
		  };
	
		getFriends();
		getIncoming();
		getSent();
	  }, [isLoggedIn]);

	const handleAcceptFriend = (targ) => {
		AxiosClient.post(`friends/accept/${user}/${targ}`);
		navigate(0);
	}

	const handleRejectFriend = (targ) => {
		AxiosClient.patch(`friends/reject/${user}/${targ}`);
		navigate(0);
	}

	const handleDeleteFriend = (targ) => {
		AxiosClient.delete(`friends/delete/${user}/${targ}`);
		navigate(0);
	}
	return (
		<div>
			<h1 className="text-center">Friends</h1>
			<div className="flex flex-col items-center w-full gap-4">
				<div className="text-center">You have {friends.length} friends.</div>
				<div className="flex place-items-center gap-2">
					<input
						type="search"
						id="targetname"
						name="targetname"
						required
						className="border-2 p-2 rounded-md border-gray-500"
						placeholder="Search friends..."
						value={targetname}
						onChange={(e) => setTargetname(e.target.value)}
					/>
					<SendFriendRequestButton className="mt-2" username={user} target={targetname}></SendFriendRequestButton>
				</div>
			</div>
			<div className="grid sm:grid-cols-3 mt-8 place-items-center">
				<div className="w-1/3 min-w-fit h-full">
					<h1 className="text-xl">Incoming Requests</h1>
					<div className="h-max-screen w-full rounded-lg">
						{incoming.map((friendship) => (
								<Card key={friendship.username} className="m-4 bg-white scale-90 w-[330px] sm:w-[230px] md:w-[280px] lg:w-[350px] flex items-center justify-between">
									<div className="flex items-center">
										<img
											src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
											alt="profile pic"
											className="w-20 h-20 rounded-md"
										/>
										<h1 className="text-xl ml-4">{friendship.username}</h1>
									</div>
									<div className="flex items-center">
										<FontAwesomeIcon icon={faCircleCheck} className="text-green-500 hover:text-green-300 text-2xl mr-2" onClick={(e) => handleAcceptFriend(friendship.username)}/>
										<FontAwesomeIcon icon={faCircleXmark} className="text-red-500 hover:text-red-300 text-2xl" onClick={(e) => handleRejectFriend(friendship.username)}/>
									</div>
								</Card>
							))}
					</div>
				</div>
				<div className="w-1/3 min-w-fit flex flex-col h-full">
					<h1 className="text-xl">Sent Requests</h1>
					<div className="h-max-screen w-full rounded-lg">
						{outgoing.map((friendship) => (
								<Card key={friendship.username} className="m-4 bg-white flex scale-90 w-[330px] sm:w-[230px] md:w-[280px] lg:w-[350px]">
									<img
										src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
										alt="profile pic"
										className="w-20 h-20 rounded-md"
									/>
									<div className="text-left flex flex-col gap-2 md:flex-row md:items-center md:justify-between md:w-full">
										<h1 className="text-xl">{friendship.username}</h1>
										{friendship.status == "Pending" && <p>{friendship.status}...</p>}
									</div>
								</Card>
							))}
					</div>
				</div>
				<div className="w-1/3 min-w-fit flex flex-col h-full">
					<h1 className="text-xl">Your Friends</h1>
					<div className="h-max-screen w-full rounded-lg">
						{friends.map((friendship) => (
								<Card key={friendship.username} className="m-4 bg-white scale-90 flex w-[330px] sm:w-[230px] md:w-[280px] lg:w-[350px]">
									<img
										src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
										alt="profile pic"
										className="w-20 h-20 rounded-md"
									/>
									<h1 className="text-xl">{friendship.username}</h1>
									{friendship.status == "pending" && <p className="ml-auto">{friendship.status}...</p>}
									<FontAwesomeIcon icon={faX} className="absolute top-4 right-4 text-red-500 hover:text-red-300 text-xl cursor-pointer" onClick={(e) => handleDeleteFriend(friendship.username)}/>
								</Card>
							))}
					</div>
				</div>
			</div>
		</div>
	);
};

export default Friends;
