import React from "react";
import Modal from "./Modal";

const CreateStocksModal = () => {
	return (
		<Modal>
			<div className="flex flex-col gap-4 p-4">
				<h1 className="text-2xl font-semibold">Add Stocks</h1>

				<div className="flex flex-col gap-2">
					<label htmlFor="stock-name">Stock Name</label>

					<input type="text" id="stock-name" className="p-2 border border-gray-300 rounded-md" />
				</div>

				<div className="flex flex-col gap-2">
					<label htmlFor="stock-symbol">Stock Symbol</label>

					<input type="text" id="stock-symbol" className="p-2 border border-gray-300 rounded-md" />
				</div>

				<div className="flex flex-col gap-2">
					<label htmlFor="stock-price">Stock Price</label>

					<input type="number" id="stock-price" className="p-2 border border-gray-300 rounded-md" />
				</div>

				<div className="flex flex-col gap-2">
					<label htmlFor="stock-quantity">Stock Quantity</label>

					<input type="number" id="stock-quantity" className="p-2 border border-gray-300 rounded-md" />
				</div>

				<div className="flex gap-4">
					<button className="p-2 bg-green-500 text-white rounded-md">Add Stock</button>

					<button className="p-2 bg-red-500 text-white rounded-md">Cancel</button>
				</div>
			</div>
		</Modal>
	);
};

export default CreateStocksModal;
