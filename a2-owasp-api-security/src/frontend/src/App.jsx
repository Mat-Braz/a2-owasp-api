import Header from './components/Header'
import LoginBar from './components/LoginBar'
import LabCarousel from './components/LabCarousel'

export default function App() {
  return (
    <div className="page">
      <Header />
      <div className="stage">
        <LoginBar />
        <LabCarousel />
      </div>
    </div>
  )
}
