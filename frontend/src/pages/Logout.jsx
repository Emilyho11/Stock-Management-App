import React, { useEffect } from "react";

const Logout = () => {
	useEffect(() => {
		localStorage.removeItem("token");
		window.location.href = "/";
	}, []);

	return <div>Logging out...</div>;
};

export default Logout;
