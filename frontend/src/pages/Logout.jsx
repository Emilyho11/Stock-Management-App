import React, { useEffect } from "react";
import { useAuth } from "../components/AuthContext";
import { useNavigate } from "react-router-dom";
const Logout = () => {
	const navigate = useNavigate();
	const { logout } = useAuth();
	useEffect(() => {
		logout();
		// Refresh and redirect to login is handled in both AuthContext and NavBar
	}, []);

	return <div>Logging out...</div>;
};

export default Logout;
