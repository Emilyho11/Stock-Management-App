import React, { useEffect, useState } from "react";
import Review from "./Review";
import AxiosClient from "../api/AxiosClient";
import Button from "./Button";
import { useAuth } from "./AuthContext";

const ReviewBoard = (props) => {
	const { stockListId } = props;

	const { getUsername } = useAuth();
	const [newReview, setNewReview] = useState({
		f_username: getUsername(),
		f_stockListId: stockListId,
		f_content: "",
	});
	const [reviews, setReviews] = useState([{ username: "", stockListId: 0, content: "", expanded: false }]);
	const [alreadyReviewed, setAlreadyReviewed] = useState(false);
	useEffect(() => {
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

		fetchReviews();
	}, []);

	const submitReview = async (e) => {
		e.preventDefault();
		if (!getUsername()) {
			console.error("User not logged in");
			return;
		}

		try {
			const response = await AxiosClient.post("reviews/", newReview);
			console.log(response.data);
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

	return (
		<>
			{ReviewBox()}
			<h2 className="text-xl">{reviews.length} REVIEWS</h2>
			<div className="flex flex-col gap-12">
				{reviews.map((review, index) => (
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
				))}
			</div>
		</>
	);
};

export default ReviewBoard;
