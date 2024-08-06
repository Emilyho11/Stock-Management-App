import React, { createContext, useState, useContext } from "react";

const AuthContext = createContext();
const UserContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState("");

    const login = (username) => {
        setIsLoggedIn(true);
        setUsername(username);
    };

    const logout = () => {
        setIsLoggedIn(false);
        setUsername("");
    };

    return (
        <AuthContext.Provider value={{ login, logout, isLoggedIn }}>
            <UserContext.Provider value={{ username, setUsername }}>
                {children}
            </UserContext.Provider>
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
export const useUser = () => useContext(UserContext);