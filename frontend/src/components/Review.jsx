import React from "react";
import Button, { ButtonVariants } from "./Button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faX } from "@fortawesome/free-solid-svg-icons";
import AxiosClient from "../api/AxiosClient";
import { useNavigate } from "react-router-dom";
const Review = (props) => {
	const navigate = useNavigate();
	const { data, expanded, expand } = props;
	const {stockListId, myReview } = data;
	const minExpand = 250;

	if (!data || !data.username || !data.content) {
		return null;
	}

	const deleteReview = async () => {
		console.log("Deleting review", data);
		try {
			const response = await AxiosClient.delete("reviews/"+ getUsername() + "/" + stockListId);
			console.log(response.data);
			if (response.data.message === "Review deleted successfully") {
				navigate(0);
			}
		} catch (error) {
			console.error("Error deleting review:", error);
		}
	}

	const isAlreadySmall = data.content.length <= minExpand;

	const content = expanded ? data.content : data.content.substring(0, minExpand) + (!isAlreadySmall ? "..." : "");
	return (
		<div className="grid grid-cols-3 grid-rows-1 gap-12 w-full text-left">
			<h4 className="text-base font-semibold">
				{myReview && <FontAwesomeIcon icon={faX} className="mr-2 text-red-700 scale-75 hover:scale-100 cursor-pointer"
				onClick={deleteReview} />}
				{data.username}</h4>
			<p className="col-span-2">
				{content}{" "}
				{!isAlreadySmall && (
					<Button variant={ButtonVariants.TRANSPARENT} onClick={expand} className="!w-fit">
						{!expanded ? "Read More" : "Read Less"}
					</Button>
				)}
			</p>
		</div>
	);
};

export default Review;
