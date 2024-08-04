import React from "react";
import Card from "./Card";

const StockCard = ({ data, onSelect, selected }) => {
	return (
		<button onMouseDown={onSelect}>
			<Card
				className={
					"flex gap-4 items-center hover:scale-105 hover:shadow-xl transition-all " +
					(selected ? "bg-gray-500 text-white" : "")
				}
			>
				<div className="rounded-lg bg-white w-12 h-12 p-1">
					<img src={data.image} alt={data.name} className="w-full h-full" />
				</div>
				<div className="text-left">
					<h5 className="card-title">{data.name}</h5>
					<p className="card-text">Price: {data.price}</p>
					<p className="card-text">Quantity: {data.quantity}</p>
				</div>
			</Card>
		</button>
	);
};

export default StockCard;
