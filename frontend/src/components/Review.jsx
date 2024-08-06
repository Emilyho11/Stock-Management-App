import React from "react";
import Card from "./Card";
import Button, { ButtonVariants } from "./Button";

const Review = (props) => {
	const { data, expanded, expand } = props;
	const minExpand = 250;

	if (!data || !data.username || !data.content) {
		return null;
	}

	const isAlreadySmall = data.content.length <= minExpand;

	const content = expanded ? data.content : data.content.substring(0, minExpand) + "...";
	return (
		<div className="grid grid-cols-3 grid-rows-1 gap-12 w-full text-left">
			<h4 className="text-base font-semibold">{data.username}</h4>
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
