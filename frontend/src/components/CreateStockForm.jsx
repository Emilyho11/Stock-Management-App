import React, { useState } from 'react';
import Button from './Button';
import AxiosClient from '../api/AxiosClient';
import { useNavigate } from 'react-router-dom';

const CreateStockForm = () => {
    const [stockData, setStockData] = useState({
        symbol: "",
        close: "",
        low: "",
        high: "",
        open: "",
        volume: ""
    }); // State for stock data inputs
    const navigate = useNavigate();

    // Insert stock data into stock data table api
    const apiInsertStockData = async () => {
        try {
            const response = await AxiosClient.post(`stocks/data`, stockData);
            if (response.data) {
                console.log("Stock data added successfully to stock_data table");
                apiInsertStock();
                // navigate(0);
            } else {
                console.error("Unexpected data format:", response.data);
            }
        } catch (error) {
            console.error("Error adding stock:", error);
        }
    };

    // Add stock data into stocks table
    const apiInsertStock = async () => {
        try {
            const response = await AxiosClient.post(`stocks/`, stockData);
            if (response.data) {
                console.log("Stock added successfully to stocks table");
            } else {
                console.error("Unexpected data format:", response.data);
            }
        } catch (error) {
            console.error("Error adding stock:", error);
        }
    };

    // const apiUpdateStock = async () => {
    //     try {
    //         const response = await AxiosClient.patch(`stocks/`, stockData);
    //         if (response.data) {
    //             console.log("Stock updated successfully in stocks table");
    //         } else {
    //             console.error("Unexpected data format:", response.data);
    //         }
    //     } catch (error) {
    //         console.error("Error updating stock:", error);
    //     }
    // };

    // const stockInsertIfExists = async () => {
    //     try {
    //         const response = await AxiosClient.get(`stocks/${stockData.symbol}`);
    //         response.data ? apiUpdateStock() : apiInsertStock();
    //     } catch (error) {
    //         console.error("Error checking stock:", error);
    //     }
    // };

    // Handle create stock form submission
    const handleCreateStockSubmit = async (e) => {
        e.preventDefault();
        await apiInsertStockData();
    };

    return (
        <>
            <form className="bg-white p-6 rounded-md w-1/3 mb-4 mx-auto shadow-lg border border-gray-300" onSubmit={handleCreateStockSubmit}>
                <h2 className="text-xl mb-4">Create New Stock</h2>
                <div className="flex flex-wrap -mx-2 mb-4">
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">Symbol</label>
                        <input
                            type="text"
                            value={stockData.symbol}
                            onChange={(e) => setStockData({ ...stockData, symbol: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">Close</label>
                        <input
                            type="number"
                            value={stockData.close}
                            onChange={(e) => setStockData({ ...stockData, close: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">Low</label>
                        <input
                            type="number"
                            value={stockData.low}
                            onChange={(e) => setStockData({ ...stockData, low: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">High</label>
                        <input
                            type="number"
                            value={stockData.high}
                            min={stockData.low}
                            onChange={(e) => setStockData({ ...stockData, high: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">Open</label>
                        <input
                            type="number"
                            value={stockData.open}
                            onChange={(e) => setStockData({ ...stockData, open: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                    <div className="w-1/2 px-2 mb-4">
                        <label className="block text-gray-700 font-bold mb-2">Volume</label>
                        <input
                            type="number"
                            value={stockData.volume}
                            onChange={(e) => setStockData({ ...stockData, volume: e.target.value })}
                            className="w-full p-2 border border-gray-300 rounded shadow-sm"
                            required
                        />
                    </div>
                </div>
                <Button type="submit">Submit</Button>
            </form>
        </>
    );
};

export default CreateStockForm;