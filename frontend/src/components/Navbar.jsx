import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars, faHouse, faBoxesStacked, faUserFriends, faArrowTrendUp } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "./AuthContext";

const Navbar = () => {
	const [isOpen, setIsOpen] = useState(false);
	const { isLoggedIn, logout } = useAuth();

    if (!isLoggedIn) {
		// Don't render the Navbar if the user is not logged in
        return null;
    }

	const myLinks = [
		{ to: "/", text: "Stocks", icon: faBoxesStacked },
		{ to: "/manage-my-stocks", text: "Manage My Stocks", icon: faHouse },
		{ to: "/friends", text: "Friends", icon: faUserFriends },
		{ to: "/account", text: "Account", icon: faBoxesStacked },
	];

	return (
		<header className="header flex w-full h-[72px] relative shadow z-10">
			<div className="flex items-center ml-16">
				<FontAwesomeIcon icon={faArrowTrendUp} className="text-4xl text-green-500 m-4" />
				<h1 className="text-xl tracking-wide">Stock Management</h1>
			</div>
			<div className="m-4 mr-32 absolute top-5 right-0 gap-14 text-base hidden md:flex">
				{myLinks.map((link, index) => (
					<NavLink
						key={index}
						to={link.to}
						className={({ isActive }) =>
							[
								"text-gray-600 hover:text-stock_color transition-all pb-2 ",
								!isActive ? "active" : "!text-dark_red scale-110 border-b-2 border-dark_red",
							].join(" ")
						}
					>
						<FontAwesomeIcon icon={link.icon} className="mr-2" />
						{link.text}
					</NavLink>
				))}
			</div>
			<div className="md:hidden flex items-center absolute top-0 right-0 m-4">
				<button
					onClick={() => setIsOpen(!isOpen)}
					className="text-white hover:text-stock_color focus:outline-none"
				>
					<FontAwesomeIcon icon={faBars} className="text-2xl text-black hover:text-stock_color" />
				</button>
			</div>
			{isOpen && (
				<div className="md:hidden absolute top-12 w-screen bg-dark_red shadow-lg flex flex-col text-lg z-10 gap-2 p-4">
					{myLinks.map((link, index) => (
						<NavLink
							key={index}
							to={link.to}
							onClick={() => setIsOpen(false)}
							className={({ isActive }) =>
								[
									"text-gray-400 hover:text-stock_color transition-all text-right mr-8",
									!isActive ? "active" : "!text-white scale-110",
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
