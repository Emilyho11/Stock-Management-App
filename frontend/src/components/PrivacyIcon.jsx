import React from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faComments, faEyeSlash } from "@fortawesome/free-solid-svg-icons";

const PrivacyIcon = ({ privacy, className }) => {
	return (
		<>
			{privacy != "public" && (
				<span className={" " + className}>
					<FontAwesomeIcon icon={faEyeSlash} className="mr-2 " />
					PRIVATE
				</span>
			)}
		</>
	);
};

export default PrivacyIcon;
