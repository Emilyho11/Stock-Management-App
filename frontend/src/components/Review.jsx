import React from "react";
import Card from "./Card";

const Review = (props) => {
	// Review(username, stockListId, content)
	//const { username, stockListId, content } = props.data;

	return (
		<div className="grid grid-cols-3 grid-rows-1 gap-12 w-full text-left">
			<h4 className="text-base font-semibold">{props.username}</h4>
			<p className="col-span-2">{props.content}</p>
		</div>
	);
};

export default Review;
