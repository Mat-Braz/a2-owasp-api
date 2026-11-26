import { useLab } from '../context/LabContext'

export default function Header() {
  const { version, setVersion } = useLab()

  const vulnerable = version === 'v1'

  return (
    <header className={vulnerable ? 'top is-v1' : 'top is-v2'}>
      <div className="brand">
        <p className="eyebrow">Avaliação A2 · OWASP API Security Top 10</p>
        <div className="brand-row">
          <h1>SocialConnect</h1>
          <a
            className="swagger-link"
            href="http://localhost:8080/swagger-ui.html"
            target="_blank"
            rel="noreferrer"
          >
            Swagger
          </a>
        </div>
      </div>
      <div className="toggle" role="group" aria-label="Versão da API">
        <button
          type="button"
          className={vulnerable ? 'on' : ''}
          onClick={() => setVersion('v1')}
        >
          Modo vulnerável
          <small>/api/v1</small>
        </button>
        <button
          type="button"
          className={!vulnerable ? 'on' : ''}
          onClick={() => setVersion('v2')}
        >
          Modo seguro
          <small>/api/v2</small>
        </button>
      </div>
      <p className="mode-line">
        {vulnerable
          ? 'Todas as chamadas vão para /api/v1. As falhas desta página são de propósito.'
          : 'Todas as chamadas vão para /api/v2. O backend recusa o que a v1 aceita.'}
      </p>
    </header>
  )
}
