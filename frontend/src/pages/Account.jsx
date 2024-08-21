import React, { useEffect, useState } from "react";
import Card from "../components/Card";
import Button from "../components/Button";
import { Link } from "react-router-dom";
import { useAuth } from "../components/AuthContext";
import { useNavigate } from "react-router-dom";
import AxiosClient from "../api/AxiosClient";

const Account = () => {
    const { getUsername, isLoggedIn } = useAuth();
    const navigate = useNavigate();
    const [isEditing, setIsEditing] = useState(false);
    const [username, setUsername] = useState(getUsername());
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
            navigate(0);
        }
    }, []);

    if (!isLoggedIn()) {
        return <div>Redirecting to login...</div>;
    }

    // Delete account api
    const deleteAccount = async () => {
        try {
            const user = {
                username: getUsername(),
            };
            const response = await AxiosClient.delete("account/", { data: user });
            if (response.data.message === "User deleted successfully") {
                console.log("Account deleted successfully");
                navigate("/login");
            } else {
                console.error("Failed to delete account:", response.data.message);
            }
        } catch (error) {
            console.error("Error deleting account:", error);
        }
    };

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleSave = () => {
        setIsEditing(false);
        // Save the updated fields here
        console.log("Saved:", { username, password, email });
		navigate(0);
    };

    return (
        <div className="mx-16 my-4">
            <h1>Account: {getUsername()}</h1>
            <div className="mt-4 flex flex-col items-center">
                <div className="grid grid-cols-2 gap-4 mb-4">
                    <p className="text-lg">Username:</p>
                    <input
                        type="text"
                        value={username}
                        className={`border p-2 ${isEditing ? "" : "bg-gray-200"}`}
                        readOnly={!isEditing}
						disabled={!isEditing}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className="grid grid-cols-2 gap-4 mb-4">
                    <p className="text-lg">Password:</p>
                    <input
                        type="password"
                        value={password}
						className={`border p-2 ${isEditing ? "" : "bg-gray-200"}`}
                        readOnly={!isEditing}
						disabled={!isEditing}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div className="grid grid-cols-2 gap-4 mb-4">
                    <p className="text-lg">Email:</p>
                    <input
                        type="email"
                        value={email}
						className={`border p-2 ${isEditing ? "" : "bg-gray-200"}`}
                        readOnly={!isEditing}
						disabled={!isEditing}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>
            </div>
			{isEditing ? (
                    <Button className="mr-2 px-7 bg-blue-950" onClick={handleSave}>Save</Button>
                ) : (
                    <Button className="mr-2 px-7 hover:bg-blue-950" onClick={handleEdit}>Edit</Button>
                )}
            <Link to={"/logout"}>
                <Button className="mt-8 hover:bg-blue-950">Logout</Button>
            </Link>
            <Button className="bg-red-700 hover:bg-dark_red mt-4 ml-24" onClick={deleteAccount}>Delete Account</Button>
        </div>
    );
};

export default Account;