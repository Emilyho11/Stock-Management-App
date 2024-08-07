import React, { useRef } from "react";
import { useDrop, useDrag } from "react-dnd";
import { useNavigate } from "react-router-dom";
const type = "StockCard"; // Need to pass which type element can be draggable, its a simple string or Symbol. This is like an Unique ID so that the library know what type of element is dragged or dropped on.

const DndStockCard = (props) => {
	const { itemData, children } = props;
	const navigate = useNavigate();

	const myType = itemData.type;

	function handleDropBehaviour(item, monitor) {
		// Scenarios:
		// 1. Stock on Stock = Create StockList
		// 2. Stock on Portfolio = Add to Portfolio
		// 3. Stock on StockList = Add to StockList
		// 4. StockList on Stocklist = Nothing
		// 5. StockList on Portfolio = Add to Portfolio
		const dropType = item.type;

		const counter = { stocks: 0, stocklist: 0, portfolio: 0 };

		counter[dropType] += 1;
		counter[myType] += 1;

		if (counter["stocks"] === 2) {
			console.log("Create StockList");
		} else if (counter["stocks"] === 1 && counter["portfolio"] === 1) {
			console.log("Add to Portfolio");
		} else if (counter["stocks"] === 1 && counter["stocklist"] === 1) {
			console.log("Add to StockList");
		} else if (counter["stocklist"] === 2) {
			console.log("Nothing");
		} else if (counter["stocklist"] === 1 && counter["portfolio"] === 1) {
			console.log("Add to Portfolio");
		}
	}

	// Drop functionality
	const [{ dropItem, droppedOn }, dropRef] = useDrop(() => ({
		accept: type,

		drop: handleDropBehaviour,

		collect: (monitor) => ({
			dropItem: monitor.getItem(),
			droppedOn: monitor.isOver(),
		}),
	}));

	const [{ isDragging }, dragRef] = useDrag(() => ({
		type: type,
		item: { ...itemData },
		options: { dropEffect: "move" },
		collect: (monitor) => ({
			isDragging: monitor.isDragging(),
		}),
	}));

	const gotoItem = (item) => {
		navigate("/manage-my-stocks");
	};

	// Add the reference to the element
	return (
		<button
			ref={dragRef}
			style={{
				opacity: isDragging ? 0.2 : 1,
				pointerEvents: isDragging ? "none" : "auto",
			}}
			className={
				"transition-all bg-white p-2 rounded-md w-72 min-h-3 hover:shadow-lg " +
				(droppedOn ? "scale-110 bg-green-300" : "") +
				(isDragging ? " border-2 border-dashed border-gray-300 scale-75" : "")
			}
			onClick={() => gotoItem(itemData)}
		>
			<div ref={dropRef} className="w-full h-full">
				{children}
			</div>
		</button>
	);
};

export default DndStockCard;
