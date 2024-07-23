import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);

  const myLinks = [
    { to: '/', text: 'Home' },
    { to: '/manage-my-stocks', text: 'Manage My Stocks' },
    { to: '/account', text: 'Account' }
  ];

  return (
    <div className="header w-full h-[60px] bg-dark_red relative">
      <div className="m-4 absolute top-0 right-0 gap-14 text-lg hidden md:flex">
        {myLinks.map((link, index) => (
          <NavLink
            key={index}
            to={link.to}
            className={({ isActive }) =>
              [
                'text-white hover:text-stock_color',
                !isActive ? 'active' : 'font-bold underline text-stock_color',
              ].join(' ')
            }
          >
            {link.text}
          </NavLink>
        ))}
      </div>
      <div className="md:hidden flex items-center absolute top-0 right-0 m-4">
        <button onClick={() => setIsOpen(!isOpen)} className="text-white hover:text-stock_color focus:outline-none">
          <FontAwesomeIcon icon={faBars} className="text-2xl text-white hover:text-stock_color" />
        </button>
      </div>
      {isOpen && (
        <div className="md:hidden absolute top-12 right-4 w-48 bg-dark_red rounded-lg shadow-lg flex flex-col text-lg">
          {myLinks.map((link, index) => (
            <NavLink
              key={index}
              to={link.to}
              onClick={() => setIsOpen(false)}
              className={({ isActive }) =>
                [
                  !isActive ? 'active' : 'font-bold underline text-stock_color',
                  'py-2 px-4 text-white hover:text-stock_color'
                ].join(' ')
              }
            >
              {link.text}
            </NavLink>
          ))}
        </div>
      )}
    </div>
  );
};

export default Navbar;
