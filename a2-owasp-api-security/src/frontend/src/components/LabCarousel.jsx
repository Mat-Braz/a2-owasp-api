import { useEffect, useState } from 'react'
import { useLab } from '../context/LabContext'
import Feed from './Feed'
import SearchBox from './SearchBox'
import BolaPanel from './BolaPanel'
import ProfileEditor from './ProfileEditor'
import { JwtDemo, CrashDemo } from './SecurityDemos'
import BypassPanel, { AdminMenu } from './AdminPanel'

const SLIDES = [
  { id: 'feed', label: 'Feed', needsLogin: false, render: () => <Feed /> },
  { id: 'search', label: 'Busca', needsLogin: false, render: () => <SearchBox /> },
  { id: 'bola', label: 'BOLA', needsLogin: true, render: () => <BolaPanel /> },
  { id: 'profile', label: 'Perfil', needsLogin: true, render: () => <ProfileEditor /> },
  { id: 'jwt', label: 'JWT', needsLogin: true, render: () => <JwtDemo /> },
  { id: 'crash', label: 'Erro 500', needsLogin: true, render: () => <CrashDemo /> },
  {
    id: 'bypass',
    label: 'Bypass',
    needsLogin: true,
    render: () => <BypassSlide />
  }
]

function NeedsLogin({ children }) {
  const { user } = useLab()

  if (!user) {
    return (
      <section className="card">
        <p className="muted">Entre com uma conta para executar esta ação.</p>
      </section>
    )
  }

  return children
}

function BypassSlide() {
  const { user } = useLab()

  return (
    <div className="slide-stack">
      {user?.role === 'ADMIN' && <AdminMenu />}
      <BypassPanel />
    </div>
  )
}

export default function LabCarousel() {
  const [index, setIndex] = useState(0)
  const slide = SLIDES[index]

  function go(step) {
    setIndex((current) => (current + step + SLIDES.length) % SLIDES.length)
  }

  useEffect(() => {
    function onKey(event) {
      const tag = event.target.tagName

      if (tag === 'INPUT' || tag === 'TEXTAREA') {
        return
      }

      if (event.key === 'ArrowRight') {
        go(1)
      }

      if (event.key === 'ArrowLeft') {
        go(-1)
      }
    }

    window.addEventListener('keydown', onKey)

    return () => window.removeEventListener('keydown', onKey)
  }, [])

  const body = slide.needsLogin ? <NeedsLogin>{slide.render()}</NeedsLogin> : slide.render()

  return (
    <section className="carousel" aria-label="Ações do laboratório">
      <div className="carousel-bar">
        <button type="button" className="nav-arrow" onClick={() => go(-1)} aria-label="Ação anterior">
          ‹
        </button>
        <div className="carousel-title">
          <strong>{slide.label}</strong>
          <small>{index + 1} de {SLIDES.length}</small>
        </div>
        <button type="button" className="nav-arrow" onClick={() => go(1)} aria-label="Próxima ação">
          ›
        </button>
      </div>
      <div className="carousel-steps" role="tablist" aria-label="Passos">
        {SLIDES.map((item, itemIndex) => (
          <button
            key={item.id}
            type="button"
            role="tab"
            aria-selected={itemIndex === index}
            className={itemIndex === index ? 'on' : ''}
            onClick={() => setIndex(itemIndex)}
          >
            {item.label}
          </button>
        ))}
      </div>
      <div className="carousel-stage">
        {body}
      </div>
    </section>
  )
}
