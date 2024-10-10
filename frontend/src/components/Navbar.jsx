import React, { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars, faHouse, faBoxesStacked, faUserFriends, faArrowTrendUp } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "./AuthContext";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";

const Navbar = () => {
	const [isOpen, setIsOpen] = useState(false);
	const { isLoggedIn } = useAuth();

	const navigate = useNavigate();
	const location = useLocation();

	useEffect(() => {
		if (location.pathname !== "/login" && !isLoggedIn()) {
			navigate("/login");
		}
	}, []);

	// if (!isLoggedIn()) {
	// 	return <> </>;
	// }
	const myLinks = [
		{ to: "/", text: "Manage My Stocks", icon: faHouse },
		{ to: "/stocks", text: "Stocks", icon: faBoxesStacked },
		{ to: "/friends", text: "Friends", icon: faUserFriends },
		{ to: "/account", text: "Account", icon: faBoxesStacked },
	];

	return (
		<header className="header flex w-full h-[72px] relative shadow z-10">
			<div className="flex items-center ml-16">
				<FontAwesomeIcon icon={faArrowTrendUp} className="text-4xl text-green-500 m-4" />
				<h1 className="text-xl tracking-wide">Stock Management</h1>
			</div>
			{isLoggedIn() && (
				<div className="m-4 mr-32 absolute top-5 right-0 gap-14 text-base hidden lg:flex">
					{myLinks.map((link, index) => (
						<NavLink
							key={index}
							to={link.to}
							className={({ isActive }) =>
								[
									"text-dark_red hover:text-stock_color transition-all pb-2 ",
									!isActive ? "active" : "!text-stock_color scale-110 border-b-2 border-stock_color",
								].join(" ")
							}
						>
							<FontAwesomeIcon icon={link.icon} className="mr-2" />
							{link.text}
						</NavLink>
					))}
				</div>
			)}
			<div className="lg:hidden flex items-center absolute top-0 right-0 m-4">
				<button
					onClick={() => setIsOpen(!isOpen)}
					className="text-dark_red hover:text-stock_color focus:outline-none"
				>
					<FontAwesomeIcon icon={faBars} className="text-2xl text-black hover:text-stock_color" />
				</button>
			</div>
			{isOpen && (
				<div className="lg:hidden absolute top-14 right-0 w-1/2 md:w-1/3 bg-gray-200 shadow-lg flex flex-col text-lg z-10 gap-2 p-2">
					{myLinks.map((link, index) => (
						<NavLink
							key={index}
							to={link.to}
							onClick={() => setIsOpen(false)}
							className={({ isActive }) =>
								[
									"text-dark_red hover:text-stock_color transition-all text-right mr-8",
									!isActive ? "active" : "!text-stock_color scale-110",
								].join(" ")
							}
						>
							<FontAwesomeIcon icon={link.icon} className="mr-2" />
							{link.text}
						</NavLink>
					))}
				</div>
			)}
		</header>
	);
};

export default Navbar;
