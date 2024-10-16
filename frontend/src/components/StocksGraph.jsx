import React, { useState, useEffect } from "react";
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { useAuth } from "../components/AuthContext";
import AxiosClient from "../api/AxiosClient";

const StocksGraph = ({ symbol, startDate, endDate, viewType, time, setDateBounds }) => {
    ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);
    
    // Get stocks data from api
    const [backupStocks, setBackupStocks] = useState([]);
    const [stocks, setStocks] = useState([]);
    const [lastSymbol, setLastSymbol] = useState("");

    const fetchStocksData = async (viewTypeChanged = false) => {
        try {
            let response = "";
            let data = "";

            if ((symbol !== lastSymbol && symbol !== "") || viewTypeChanged) {
                if (viewType === "future") {
                    response = await AxiosClient.get(`stocks/future/${symbol}/${time}`);
                    if (!(response.data && Array.isArray(response.data))) {
                        console.error("Unexpected data format:", response.data);
                        return;
                    }
                    data = response.data;
                } else {
                    if (startDate && endDate) {
                        response = await AxiosClient.get(`stocks/${symbol}/data?start_date=${startDate}&end_date=${endDate}`);
                    } else {
                        response = await AxiosClient.get(`stocks/${symbol}/data`);
                    }

                    if (!(response.data && Array.isArray(response.data.value))) {
                        console.error("Unexpected data format:", response.data);
                        return;
                    }
                    data = response.data.value;
                    // Set the date bounds
                    if (data.length > 0) {
                        console.log("Setting date bounds:", {
                            min: new Date(response.data.minDate),
                            max: new Date(response.data.maxDate),
                        });
                        
                        setDateBounds({
                            min: new Date(response.data.minDate),
                            max: new Date(response.data.maxDate),
                        });
                    }
                }
                setStocks(data);
                if (lastSymbol !== symbol && symbol !== "") {
                    console.log("Setting last symbol to:", symbol);
                    setBackupStocks(data);
                }
            }

        } catch (error) {
            console.error("Error fetching stock data:", error);
        }
    };

    useEffect(() => {
        console.log("View type changed:", viewType);
        fetchStocksData(true);
    }, [viewType, time]);

    useEffect(() => {
        fetchStocksData();
        
        if (lastSymbol !== symbol) {
            setLastSymbol(symbol);
        } 
    }, [symbol, time]);

    useEffect(() => {
        console.log("Date bounds changed:", { startDate, endDate });
        // Filter the stocks based on the date range
        const newStocksArray = backupStocks.filter(stock => {
            const timestamp = new Date(stock.timestamp);
            if (startDate && timestamp < new Date(startDate)) {
                return false;
            }
            if (endDate && timestamp > new Date(endDate)) {
                return false;
            }
            return true;
        });

        setStocks(newStocksArray);
    
        fetchStocksData();
    }, [endDate, startDate]); 

    const daysInYear = 365;
    const daysToShow = Math.abs(time) * daysInYear;

    const filteredStocks = stocks.slice(0, daysToShow);

    const generateLabels = () => {
        if (time > 100) {
            const labels = [];
            const yearsToShow = Math.ceil(time);
            for (let i = 1; i <= yearsToShow; i++) {
                labels.push(`Year ${i}`);
            }
            return labels;
        } else if (time > 10) {
            const labels = [];
            const monthsToShow = Math.ceil(daysToShow / 30);
            for (let i = 1; i <= monthsToShow; i++) {
                labels.push(`Month ${i}`);
            }
            return labels;
        } else if (time > 3) {
            const labels = [];
            const daysToShow2 = Math.ceil(daysToShow / 10);
            for (let i = 1; i <= daysToShow2; i++) {
                labels.push(`Day ${i * 10}`);
            }
            return labels;
        } else {
            return filteredStocks.map((_, index) => `Day ${index + 1}`);
        }
    };

    const data = {
        labels: viewType === "future" 
            ? generateLabels() 
            : stocks.map(stock => new Date(stock.timestamp).toLocaleDateString()),
        datasets: [
            {
                label: viewType === "future" ? 'Predicted Close Price' : 'Close Price',
                data: viewType === "future" ? stocks : stocks.map(stock => stock.close),
                fill: false,
                backgroundColor: 'rgba(54,162,235,0.2)',
                borderColor: 'rgba(54,162,235,1)',
            },
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
                    text: 'Time',
                    font: {
                        size: 18
                    }
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Price',
                    font: {
                        size: 18
                    }
                }
            }
        }
    };

    return (
        <div className="relative h-[80vh] w-[110%] pt-4">
            {!symbol ? (
                <h2>Click on a stock to view the graph</h2>
            ) : (
                <div className="h-[80vh]">
                    <Line data={data} options={options} width={"100%"} height={"100%"}/>
                </div>
            )}
        </div>
    );
};

export default StocksGraph;