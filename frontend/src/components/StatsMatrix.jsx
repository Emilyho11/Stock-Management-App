import React, { useState } from 'react';
import AxiosClient from "../api/AxiosClient";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faX } from '@fortawesome/free-solid-svg-icons';

const StatsMatrix = ({ companies, onClose }) => {
  const [matrix, setMatrix] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchMatrixData = async (type) => {
    setLoading(true);
    const newMatrix = Array(companies.length).fill(null).map(() => Array(companies.length).fill(null));
    try {
      for (let i = 0; i < companies.length; i++) {
        for (let j = 0; j < companies.length; j++) {
          if (i === j) {
            newMatrix[i][j] = 1; // Correlation or covariance with itself is 1
          } else {
            const symbol1 = companies[i];
            const symbol2 = companies[j];
            const response = await AxiosClient.get(`/portfolio/calculate${type}/${symbol1}/${symbol2}`);
            newMatrix[i][j] = parseFloat(response.data);
          }
        }
      }
      setMatrix(newMatrix);
    } catch (error) {
      console.error(`Error calculating ${type}:`, error);
    } finally {
      setLoading(false);
    }
  };

  const handleCalculateCOV = () => fetchMatrixData('CovBetween2');
  const handleCalculateCorrelation = () => fetchMatrixData('Correlation');

  return (
    <div className='bg-white p-4 rounded-lg relative'>
      <h2 className='text-2xl pb-4'>Statistics Matrix</h2>
      <button className='absolute top-2 right-2 text-red-600 hover:text-red-300' onClick={onClose}>
        <FontAwesomeIcon icon={faX} />
      </button>
      <div className="flex mb-4 gap-6 justify-center">
        <button
          className="bg-dark_red text-white px-4 py-2 rounded"
          onClick={handleCalculateCorrelation}
        >
          Calculate Correlation
        </button>
        <button
          className="bg-dark_red text-white px-4 py-2 rounded"
          onClick={handleCalculateCOV}
        >
          Calculate Covariance
        </button>
      </div>
      {loading ? (
        <div>Loading...</div>
      ) : (
        <table className="table-auto border-collapse border border-gray-400 w-full">
          <thead>
            <tr>
              <th className="border border-gray-300 px-4 py-2"></th>
              {companies.map((company, index) => (
                <th key={index} className="border border-gray-300 px-4 py-2">
                  {company}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {companies.map((company, rowIndex) => (
              <tr key={rowIndex}>
                <td className="border border-gray-300 px-4 py-2">{company}</td>
                {companies.map((_, colIndex) => (
                  <td key={colIndex} className="border border-gray-300 px-4 py-2">
                    {matrix[rowIndex] && matrix[rowIndex][colIndex] !== undefined
                      ? matrix[rowIndex][colIndex].toFixed(4)
                      : '-'}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default StatsMatrix;