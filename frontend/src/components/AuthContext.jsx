import React, { createContext, useState, useContext, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
	const navigate = useNavigate();

	const login = (username) => {
		console.log("username", username);
		// Save in localstorage
		localStorage.setItem("token", username);
	};

	const logout = () => {
		localStorage.removeItem("token");
		navigate("/login");
		navigate(0);
	};

	useEffect(() => {
		const user = localStorage.getItem("token");
		if (user) {
			login(user);
		}
	}, []);

	const isLoggedIn = () => {
		return localStorage.getItem("token");
	};

	const getUsername = () => {
		return localStorage.getItem("token");
	};

	const value = useMemo(() => ({ login, logout, isLoggedIn, getUsername }), []);

	return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);
