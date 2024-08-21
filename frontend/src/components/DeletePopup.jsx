import React from 'react'
import Button from './Button'

const DeletePopup = ( {message, onConfirm, onCancel}) => {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50">
      <div className="bg-white p-6 rounded shadow-lg">
        <h2 className="text-xl mb-4">{message}</h2>
        <div className="flex justify-end">
          <Button className="mr-2 hover:bg-blue-950" onClick={onCancel}>Cancel</Button>
          <Button className="bg-red-700 hover:bg-dark_red" onClick={onConfirm}>Delete</Button>
        </div>
      </div>
    </div>
  )
}

export default DeletePopup