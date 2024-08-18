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
				<div className="flex items-center gap-2">
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
					<SendFriendRequestButton className="" username={user} target={targetname}></SendFriendRequestButton>
				</div>
			</div>
			<div className="flex flex-row space-x-reverse ml-auto mr-auto">
				<div className="flex flex-col mt-4 ml-auto mr-auto">
					<div className="w-1/3 min-w-fit flex flex-col h-full">
						<h1 className="text-xl">Incoming Requests</h1>
						<div className="h-max-screen w-full rounded-lg">
							{incoming.map((friendship) => (
										<Card key={friendship.username} className="m-4 bg-white scale-90">
											<img
												src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
												alt="profile pic"
												className="w-20 h-20 rounded-md"
											/>
											<h1 className="text-xl">{friendship.username}</h1>
											<FontAwesomeIcon icon={faCircleCheck} className="text-green-500 text-2xl" onClick={(e) => handleAcceptFriend(friendship.username)}/>
											<FontAwesomeIcon icon={faCircleXmark} className="text-red-500 text-2xl" onClick={(e) => handleRejectFriend(friendship.username)}/>
										</Card>
									))}
						</div>
					</div>
				</div>
				<div className="flex flex-col gap-4 mt-4 mr-auto">
					<div className="w-1/3 min-w-fit ">
					<h1 className="text-xl">Sent Requests</h1>
					<div className="h-max-screen w-full rounded-lg">
						{outgoing.map((friendship) => (
								<Card key={friendship.username} className="m-4 bg-white scale-90">
									<img
										src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
										alt="profile pic"
										className="w-20 h-20 rounded-md"
									/>
									<h1 className="text-xl">{friendship.username}</h1>
									{friendship.status == "Pending" && <p className="ml-auto">{friendship.status}...</p>}
								</Card>
							))}
					</div>
					</div>
				</div>
				<div className="flex flex-col gap-4 mt-4 mr-auto">
					<div className="w-1/3 min-w-fit flex flex-col">
						<h1 className="text-xl">Your Friends</h1>
						<div className="h-max-screen w-full rounded-lg">
							{friends.map((friendship) => (
									<Card key={friendship.username} className="m-4 bg-white scale-90 pr-32">
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
		</div>
	);
};

export default Friends;
