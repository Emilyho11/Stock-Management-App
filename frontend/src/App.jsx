import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import ManageStocks from './pages/ManageStocks'
import Account from './pages/Account'
import Navbar from './components/Navbar.jsx'
import Footer from './components/Footer.jsx'

function App() {

  return (
    <>
      <BrowserRouter>
        <Navbar/>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/manage-my-stocks" element={<ManageStocks />} />
          <Route path="/account" element={<Account />} />
        </Routes>
      </BrowserRouter>
      <Footer/>
    </>
  )
}

export default App
