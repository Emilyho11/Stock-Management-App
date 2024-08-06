import React, { useState, useEffect } from "react";
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { useAuth } from "../components/AuthContext";
import AxiosClient from "../api/AxiosClient";

const StocksGraph = ( {symbol, startDate, endDate} ) => {
	ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);
	
	// Get stocks data from api
	const [stocks, setStocks] = useState([]);

	useEffect(() => {
		const fetchStocksData = async () => {
            try {
                let response;
                if (startDate && endDate) {
                    response = await AxiosClient.get(`stocks/${symbol}/data?start_date=${startDate}&end_date=${endDate}`);
                } else {
				    response = await AxiosClient.get(`stocks/${symbol}/data`);
                }
                if (response.data && Array.isArray(response.data)) {
					setStocks(response.data);
				} else {
					console.error("Unexpected data format:", response.data);
				}
			} catch (error) {
				console.error("Error fetching stock data:", error);
			}
		};

		fetchStocksData();
	}, [symbol]);


    const data = {
        labels: stocks.map(stock => stock.timestamp),
        datasets: [
            // {
            //     label: 'Open Price',
            //     data: stocks.map(stock => stock.open),
            //     fill: false,
            //     backgroundColor: 'rgba(75,192,192,0.2)',
            //     borderColor: 'rgba(75,192,192,1)',
            // },
            {
                label: 'Close Price',
                data: stocks.map(stock => stock.close),
                fill: false,
                backgroundColor: 'rgba(54,162,235,0.2)',
                borderColor: 'rgba(54,162,235,1)',
            },
            // {
            //     label: 'Low Price',
            //     data: stocks.map(stock => stock.low),
            //     fill: false,
            //     backgroundColor: 'rgba(255,159,64,0.2)',
            //     borderColor: 'rgba(255,159,64,1)',
            // },
            // {
            //     label: 'High Price',
            //     data: stocks.map(stock => stock.high),
            //     fill: false,
            //     backgroundColor: 'rgba(255,99,132,0.2)',
            //     borderColor: 'rgba(255,99,132,1)',
            // },
            // {
            //     label: 'Volume',
            //     data: stocks.map(stock => stock.volume),
            //     fill: false,
            //     backgroundColor: 'rgba(54,162,235,0.2)',
            //     borderColor: 'rgba(54,162,235,1)',
            // },
        ],
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: `Stock Price Over Time for ${symbol}`,
                font: {
                    size: 24
                }
            },
        },
		scales: {
            x: {
                title: {
                    display: true,
                    text: 'Day',
                    font: {
                        size: 18
                    }
                }
            },
			y: {
				title: {
					display: true,
					text: 'Price / Volume',
                    font: {
                        size: 18
                    }
				}
			}
		}
    };

    return (
        <>
            <div className="relative h-[80vh] w-[110%]">
                {!symbol && <h2>Click on a stock to view the graph</h2>}
                <Line data={data} options={options} />
            </div>
        </>
    );
};

export default StocksGraph;
