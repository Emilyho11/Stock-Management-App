import React, { useEffect, useState } from "react";
import Card from "../components/Card";
import Button from "../components/Button";
import { Link } from "react-router-dom";
import { useAuth } from "../components/AuthContext";
import { useNavigate } from "react-router-dom";
import AxiosClient from "../api/AxiosClient";
import DeletePopup from "../components/DeletePopup";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import SuccessPopup from "../components/SuccessPopup";

const Account = () => {
    const { getUsername, isLoggedIn } = useAuth();
    const navigate = useNavigate();
    const [isEditingEmail, setIsEditingEmail] = useState(false);
    const [isEditingPwd, setIsEditingPwd] = useState(false);
    const [username, setUsername] = useState(getUsername());
    const [password, setPassword] = useState("");
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isPasswordVerified, setIsPasswordVerified] = useState(false);
    const [email, setEmail] = useState("");
    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const [passwordMessage, setPasswordMessage] = useState("");
    const [showSuccessPopup, setShowSuccessPopup] = useState(false);

    // Get user data
    const getUserData = async () => {
        try {
            const response = await AxiosClient.get(`/users/${getUsername()}`);
            if (response.data) {
                setPassword(response.data.password);
                console.log("Password:", response);
                setEmail(response.data.email);
            } else {
                console.error("Unexpected data format:", response.data);
            }
        } catch (error) {
            console.error("Error fetching user data:", error);
        }
    };

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
            navigate(0);
        } else {
            getUserData();
        }
    }, [isLoggedIn, navigate]);

    if (!isLoggedIn()) {
        return <div>Redirecting to login...</div>;
    }

    // Delete Account api
    const deleteAccount = async () => {
        try {
            const user = {
                username: getUsername(),
            };
            const response = await AxiosClient.delete("/users/", { data: user });
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

    const editPassword = async () => {
        try {
            const response = await AxiosClient.patch(`/users/updatePassword/${getUsername()}/${newPassword}`);
            if (response.data.message === "Password updated successfully") {
                console.log("Password updated successfully");
                setIsEditingPwd(false);
                setIsPasswordVerified(false);
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            } else {
                console.error("Failed to update password:", response.data.message);
            }
        } catch (error) {
            console.error("Error updating password:", error);
        }
    };

    const editEmail = async() => {
        try {
            const response = await AxiosClient.patch(`/users/updateEmail/${getUsername()}/${email}`);
            if (response.data.message === "Email updated successfully") {
                console.log("Email updated successfully");
            } else {
                console.error("Failed to update email:", response.data.message);
            }
        } catch (error) {
            console.error("Error updating email:", error);
        }
    };

    const verifyCurrentPassword = () => {
        if (isEditingPwd && currentPassword === password) {
            console.log("Password verified");
            setIsPasswordVerified(true);
        } else {
            console.error("Incorrect password");
        }
    };

    const handleEditPwd = () => {
        setIsEditingPwd(true);
    };

    const handleSavePwd = () => {
        if (newPassword !== confirmPassword) {
            setPasswordMessage("Passwords do not match");
            return;
        }
        setPasswordMessage("");
        setShowSuccessPopup(true);
        editPassword();
        navigate(0);
    };

    const handleEditEmail = () => {
        setIsEditingEmail(true);
    };

    const handleSaveEmail = () => {
        setIsEditingEmail(false);
        setShowSuccessPopup(true);
        console.log("Saved email:", email);
        editEmail();
        navigate(0);
    };

    const handleDeleteClick = () => {
        setShowDeletePopup(true);
    };

    const handleConfirmDelete = () => {
        setShowDeletePopup(false);
        deleteAccount();
    };

    const handleCancelDelete = () => {
        setShowDeletePopup(false);
    };

    const handleClosePopup = () => {
        setShowSuccessPopup(false);
    };

    return (
        <div className="mx-16 my-4">
            <h1>Account: {username}</h1>
            <div className="mt-4 flex flex-col items-center">
                <div className="grid grid-cols-2 mb-4 items-center">
                    <p className="text-lg mr-8">Username:</p>
                    <input
                        type="text"
                        value={username}
                        className={"border p-2 bg-gray-200"}
                        readOnly
                        disabled
                    />
                </div>
                <div className="grid grid-cols-2 mb-4 items-center">
                    <p className="text-lg">Email:</p>
                    <div>
                        <input
                            type="email"
                            value={email}
                            className={`border p-2 ${isEditingEmail ? "" : "bg-gray-200"}`}
                            readOnly={!isEditingEmail}
                            disabled={!isEditingEmail}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        {isEditingEmail ? (
                            <Button className="ml-4" onClick={handleSaveEmail}>Save</Button>
                        ) : (
                            <FontAwesomeIcon icon={faPen} className="hover:text-blue-600 text-md cursor-pointer ml-4" onClick={handleEditEmail} />
                        )}
                        {showSuccessPopup && (
                            <SuccessPopup message="Email Saved!" onClose={handleClosePopup} />
                        )}
                    </div>
                </div>
                <div className="grid grid-cols-2 mb-4">
                    <p className="text-lg">Password:</p>
                    <div>
                        <input
                            type="password"
                            value={currentPassword}
                            placeholder="Enter password to edit"
                            className={`border p-2 ${isEditingPwd ? "" : "bg-gray-200"}`}
                            readOnly={!isEditingPwd}
                            disabled={!isEditingPwd}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                        />
                        {isEditingPwd ? (
                            <Button className="ml-4" onClick={verifyCurrentPassword}>Verify</Button>
                        ) : (
                            <FontAwesomeIcon icon={faPen} className="hover:text-blue-600 text-md cursor-pointer ml-4" onClick={handleEditPwd} />
                        )}
                        {isPasswordVerified && (
                            <div className="flex flex-col gap-4">
                                <input
                                    type="password"
                                    value={newPassword}
                                    placeholder="New password"
                                    className="border p-2"
                                    onChange={(e) => setNewPassword(e.target.value)}
                                />
                                <input
                                    type="password"
                                    value={confirmPassword}
                                    placeholder="Re-type new password"
                                    className="border p-2"
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                                {passwordMessage && <p className="text-red-500">{passwordMessage}</p>}
                                <Button className="ml-4" onClick={handleSavePwd}>Save</Button>
                                {showSuccessPopup && (
                                    <SuccessPopup message="Password Saved!" onClose={handleClosePopup} />
                                )}
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <Link to={"/logout"}>
                <Button className="mt-8 hover:bg-blue-950">Logout</Button>
            </Link>
            <Button className="bg-red-700 hover:bg-dark_red mt-8 ml-4 w-40 h-10" onClick={handleDeleteClick}>Delete Account</Button>
            {showDeletePopup && (
                <DeletePopup
                    message="Are you sure you want to delete your account?"
                    onConfirm={handleConfirmDelete}
                    onCancel={handleCancelDelete}
                />
            )}
        </div>
    );
};

export default Account;