import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ManageStocks from "./pages/ManageStockList.jsx";
import Account from "./pages/Account";
import Navbar from "./components/Navbar.jsx";
import Footer from "./components/Footer.jsx";
import React from "react";
import NoPage from "./pages/NoPage.jsx";
import Friends from "./pages/Friends.jsx";
import Logout from "./pages/Logout.jsx";
import Login from "./pages/Login.jsx";
import StockLists from "./pages/StockLists.jsx";
import ManagePortfolio from "./pages/ManagePortfolio.jsx";
import StocksManager from "./pages/StocksManager.jsx";
import Signup from "./pages/Signup.jsx";
import { AuthProvider } from "./components/AuthContext.jsx";
import Stocks from "./pages/Stocks.jsx";

function App() {
	return (
		<BrowserRouter>
			<AuthProvider>
				<div className="min-h-screen flex flex-col">
					<Navbar />
					<div className="flex-1 bg-gray-100 relative pt-4">
						<Routes>
							<Route path="/stocks" element={<Stocks />} />
							<Route path="/portfolio/:id" element={<ManagePortfolio />} />
							<Route path="/stocklist/:id" element={<ManageStocks />} />
							<Route path="/" element={<StocksManager />} />
							<Route path="/account" element={<Account />} />
							<Route path="/friends" element={<Friends />} />
							<Route path="/logout" element={<Logout />} />
							<Route path="/login" element={<Login />} />
							<Route path="/signup" element={<Signup />} />
							<Route path="*" element={<NoPage />} />
						</Routes>
					</div>
					<Footer />
				</div>
			</AuthProvider>
		</BrowserRouter>
	);
}

export default App;
