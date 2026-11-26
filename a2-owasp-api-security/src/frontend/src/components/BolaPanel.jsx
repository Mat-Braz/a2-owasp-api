import { useEffect, useState } from 'react'
import { apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import Result from './Result'

export default function BolaPanel() {
  const { version, token } = useLab()
  const [postId, setPostId] = useState('2')
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    setResult(null)
  }, [version])

  async function call(method) {
    if (!token) {
      setResult({ ok: false, status: 0, data: { error: 'Entre com uma conta antes.' } })
      return
    }

    setBusy(true)

    const response = await apiFetch({
      version,
      path: `/posts/${postId}`,
      token,
      method
    })

    setResult(response)
    setBusy(false)
  }

  return (
    <section className="card">
      <h2>Teste de BOLA</h2>
      <p className="muted">
        Logado como Bob, peça o Post 2 (rascunho privado da Alice). A v1 devolve o texto. A v2 responde 403.
      </p>
      <div className="row">
        <input
          aria-label="ID do post"
          value={postId}
          onChange={(event) => setPostId(event.target.value)}
        />
        <button type="button" disabled={busy} onClick={() => call('GET')}>
          Buscar post
        </button>
        <button type="button" className="danger" disabled={busy} onClick={() => call('DELETE')}>
          Apagar post
        </button>
        <button type="button" className="ghost" onClick={() => setPostId('2')}>
          Usar Post 2
        </button>
      </div>
      <Result result={result} />
    </section>
  )
}
