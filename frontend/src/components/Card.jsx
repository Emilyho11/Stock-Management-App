import React from 'react'

const Card = (props) => {
  return (
    <div className={"w-full bg-white rounded-lg p-4 drop-shadow-md flex gap-4 items-center " + props.className} >
        {props.children}
    </div>
  )
}

export default Card