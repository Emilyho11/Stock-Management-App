import React from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash, faUserGroup } from "@fortawesome/free-solid-svg-icons";

const PrivacyIcon = (props) => {
	const checkPrivacy = (privacy) => {
		if (props.privacy.trim() === "private"){
			return (
				<span className={" " + props.className}>
					<FontAwesomeIcon icon={faEyeSlash} className="mr-2 " />
					PRIVATE
				</span>
			)
		} else if (props.privacy.trim() === "friends"){
			return (
				<span className={" " + props.className}>
					<FontAwesomeIcon icon={faUserGroup} className="mr-2 " />
					FRIENDS ONLY
				</span>
			)
		}
		return (
			<span className={" " + props.className}>
				<FontAwesomeIcon icon={faEye} className="mr-2 " />
				PUBLIC
			</span>
		)
	}
	return (
		<>
			{props.privacy ? ( checkPrivacy(props.privacy)):null}
		</>
	);
};

export default PrivacyIcon;
