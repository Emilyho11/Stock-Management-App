import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import ManageStocks from "./pages/ManageStocks";
import Account from "./pages/Account";
import Navbar from "./components/Navbar.jsx";
import Footer from "./components/Footer.jsx";
import React from "react";
import NoPage from "./pages/NoPage.jsx";
import Friends from "./pages/Friends.jsx";
import Logout from "./pages/Logout.jsx";
import Login from "./pages/Login.jsx";
import StockLists from "./pages/StockLists.jsx";
import StocksManager from "./pages/StocksManager.jsx";
import Signup from "./pages/Signup.jsx";
import { AuthProvider } from "./components/AuthContext.jsx";

function App() {
	return (
		<AuthProvider>
			<BrowserRouter>
				<div className="min-h-screen flex flex-col">
					<Navbar />
					<div className="flex-1 bg-gray-100 relative pt-4">
						<Routes>
							<Route path="/" element={<Home />} />
							<Route path="/manage-my-stocks" element={<ManageStocks />} />
							<Route path="/stock-manager" element={<StocksManager />} />
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
			</BrowserRouter>
		</AuthProvider>
	);
}

export default App;
