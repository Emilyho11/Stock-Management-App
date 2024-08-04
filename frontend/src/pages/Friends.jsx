import React from "react";
import Card from "../components/Card";

const Friends = () => {
	// Friendship(friendId, username, status)
	const friendships = [
		// default person profile pic
		{ friendId: 1, username: "Alice", status: "accepted" },
		{ friendId: 2, username: "Bob", status: "pending" },
		{ friendId: 3, username: "Charlie", status: "accepted" },
		{ friendId: 4, username: "David", status: "pending" },
		{ friendId: 5, username: "Eve", status: "accepted" },
	];

	return (
		<div>
			<h1>Friends</h1>
			<div>You have {friendships.length} friends.</div>

			<div className="flex flex-col gap-4 mt-4">
				<div className="w-1/4 min-w-fit">
					{friendships
						.sort((a, b) => Number(a.status != "pending") - Number(b.status != "pending"))
						.map((friendship) => (
							<Card key={friendship.friendId} className="m-4">
								<img
									src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
									alt="profile pic"
									className="w-20 h-20 rounded-md"
								/>
								<h1 className="text-xl">{friendship.username}</h1>
								{friendship.status == "pending" && <p className="ml-auto">{friendship.status}...</p>}
							</Card>
						))}
				</div>
			</div>
		</div>
	);
};

export default Friends;
