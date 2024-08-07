import React from "react";

const Modal = (props) => {
	return (
		<div
			id="progress-modal"
			aria-hidden="true"
			className="overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-screen max-h-full bg-black/30 flex"
		>
			<div className="relative p-4 w-1/2 max-h-full">
				{/* <!-- Modal content --> */}
				<div className="relative bg-white rounded-lg shadow m-2">{props.children}</div>
			</div>
		</div>
	);
};

export default Modal;
