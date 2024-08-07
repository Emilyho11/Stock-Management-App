import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";
import Button from "./Button";
import { useAuth } from "./AuthContext";

const ReviewBoard = (props) => {
	const { stockListId, privacy, isOwner } = props;

	const { getUsername } = useAuth();
	const [newReview, setNewReview] = useState({
		f_username: getUsername(),
		f_stock_list_id: stockListId,
		f_content: "",
	});
	const [reviews, setReviews] = useState([{ username: "", stockListId: 0, content: "", expanded: false }]);
	const [alreadyReviewed, setAlreadyReviewed] = useState(false);
	const myUsername = getUsername();
	const fetchReviews = async () => {
		try {
			const response = await AxiosClient.get("reviews/" + stockListId);

			if (response.data && Array.isArray(response.data.value)) {
				// setReviews(response.data);
				const parsedReviews = response.data.value.map((item, index) => ({
					id: index,
					username: item.f_username.trim(),
					stockListId: item.f_stock_list_id,
					content: item.f_content.trim(),
					expanded: false,
					myReview: item.f_username.trim() === getUsername(),
				}));

				setAlreadyReviewed(
					response.data.message === "Reviewed" ||
						parsedReviews.some((review) => review.username === getUsername())
				);

				setReviews(parsedReviews);
			} else {
				console.error("Unexpected data format:", response.data);
			}
		} catch (error) {
			console.error("Error fetching data:", error);
		}
	};
	useEffect(() => {

		fetchReviews();
	}, []);

	const submitReview = async (e) => {
		e.preventDefault();
		if (!getUsername()) {
			console.error("User not logged in");
			return;
		}
		if (isOwner) {
			console.error("Owner cannot review their own stock list");
			return;
		}
		try {
			const response = await AxiosClient.post("reviews/", newReview);
			console.log(response.data);
			fetchReviews();

		} catch (error) {
			console.error("Error submitting review:", error);
		}
	};

	const ReviewBox = () => {
		if (alreadyReviewed) {
			return <p className="text-sm text-gray-300">You have already reviewed this stock list.</p>;
		} else {
			return (
				<div className="w-full">
					<h2 className="text-xl text-left">Write a Review</h2>
					<p className="text-sm text-gray-400 text-left">*Reviews are moderated.</p>
					<textarea
						className="w-full p-2 rounded-md bg-white border min-h-[15vh]"
						placeholder="Write your review here..."
						onChange={(e) => setNewReview({ ...newReview, f_content: e.target.value })}
					/>
					<Button variant="primary" className="mt-4" onClick={submitReview}>
						Submit Review
					</Button>
				</div>
			);
		}
	};

	const checkMyReview = (review) => {
		if (myUsername == review.username){
			return (
				<Review
						key={review.id}
						data={review}
						expanded={review.expanded}
						expand={() => {
							setReviews(
								reviews.map((item) => {
									if (item.id === review.id) {
										return { ...item, expanded: !item.expanded };
									} else {
										return item;
									}
								})
							);
						}}
					/>
			)
		}
	}

	const displayReviews = () => {
		console.log(privacy)
		console.log(isOwner)
		if (privacy == "public" || isOwner){
			return (reviews.sort( (a, b) => a.myReview ? -1 : 1 || a.id - b.id)
				.map((review, index) => (
					<Review
						key={review.id}
						data={review}
						expanded={review.expanded}
						expand={() => {
							setReviews(
								reviews.map((item) => {
									if (item.id === review.id) {
										return { ...item, expanded: !item.expanded };
									} else {
										return item;
									}
								})
							);
						}}
					/>
				)))
		}
		else if (privacy == "friends"){
			return (reviews.sort( (a, b) => a.myReview ? -1 : 1 || a.id - b.id)
				.map((review, index) => ( checkMyReview(review)
				)))
		}
		
	}

	return (
		<>
			{ReviewBox()}
			<h2 className="text-xl">{reviews.length} REVIEWS</h2>
			<div className="flex flex-col gap-12">
				{displayReviews()}
			</div>
		</>
	);
};

export default ReviewBoard;
