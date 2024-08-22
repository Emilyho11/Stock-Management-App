import React, { useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';

const SuccessPopup = ({ message, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 5000);

    return () => clearTimeout(timer);
  }, [onClose]);

  return (
    <div className="fixed bottom-0 left-0 right-0 flex items-center justify-center pointer-events-none">
      <div className="bg-green-600 text-white p-6 rounded shadow-lg mt-4 flex items-center pointer-events-auto animate-bounce">
        <FontAwesomeIcon icon={faCheckCircle} className="text-white mr-2" />
        <h2 className="text-xl">{message}</h2>
      </div>
    </div>
  );
};

export default SuccessPopup;