import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";

const ReviewBoard = () => {
	// Review(username, stockListId, content)
	// const [reviews, setReviews] = React.useState([
	// 	{
	// 		username: "Alice Wunderlan",
	// 		stockListId: 1,
	// 		content: "I am determined to find the best stocks and do stock stuff. Blah blah blah blah",
	// 	},
	// 	{
	// 		username: "Bob Barley",
	// 		stockListId: 2,
	// 		content:
	// 			"Lorem, ipsum dolor sit amet consectetur adipisicing elit. Sapiente quisquam quia possimus sequi ducimus enim, nihil suscipit repellendus ea, adipisci explicabo repudiandae. Aliquid, quam?",
	// 	},
	// 	{
	// 		username: "Charlie Grapplin",
	// 		stockListId: 3,
	// 		content:
	// 			"Lorem ipsum dolor sit amet consectetur adipisicing elit. Adipisci explicabo dolorem voluptate repellat quasi veniam consequatur earum harum, sequi, omnis, quis quam et voluptatum id suscipit error fugit sapiente molestias! Dolorum error voluptas debitis repudiandae.",
	// 	},
	// ]);
	const [reviews, setReviews] = useState([]);

	useEffect(() => {
	  const fetchReviews = async () => {
		try {
		  const response = await AxiosClient.get("reviews/");
		  if (response.status === 200) {
			console.log("Data fetched successfully:", response.data);
			setReviews(response.data);
			console.log("Reviews:", reviews);
		  } else {
			console.error("Unexpected data format:", response.data);
		  }
		} catch (error) {
		  console.error("Error fetching data:", error);
		}
	  };
  
	  fetchReviews();
	}, []);

	return (
		<>
			<h2 className="text-xl">{reviews.length} REVIEWS</h2>
			<div className="flex flex-col gap-12">
				{reviews.map((review, index) => (
					<Review key={index} review={review} />
				))}
			</div>
	  </>
	);
};

export default ReviewBoard;
