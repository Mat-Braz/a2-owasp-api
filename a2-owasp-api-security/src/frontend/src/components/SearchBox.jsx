import { useEffect, useState } from 'react'
import { SQLI_PAYLOAD, apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import PostList from './PostList'
import Result from './Result'

export default function SearchBox() {
  const { version, token } = useLab()
  const [query, setQuery] = useState('')
  const [posts, setPosts] = useState(null)
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    setPosts(null)
    setResult(null)
  }, [version])

  async function search(value) {
    if (!token) {
      setResult({ ok: false, status: 0, data: { error: 'Entre com uma conta antes de buscar.' } })
      return
    }

    setBusy(true)

    const response = await apiFetch({
      version,
      path: `/posts/search?q=${encodeURIComponent(value)}`,
      token
    })

    setResult(response.ok ? null : response)
    setPosts(response.ok && Array.isArray(response.data) ? response.data : [])
    setBusy(false)
  }

  return (
    <section className="card">
      <h2>Busca</h2>
      <p className="muted">
        Se o post privado da Alice aparecer, a consulta concatenou SQL. Na v2 o payload é só texto e o post privado continua de fora.
      </p>
      <div className="row">
        <input
          aria-label="Buscar posts"
          value={query}
          onChange={(event) => setQuery(event.target.value)}
          placeholder="Texto do post"
        />
        <button type="button" disabled={busy} onClick={() => search(query)}>
          Buscar
        </button>
        <button
          type="button"
          className="danger"
          disabled={busy}
          onClick={() => {
            setQuery(SQLI_PAYLOAD)
            search(SQLI_PAYLOAD)
          }}
        >
          Testar Payload SQLi
        </button>
      </div>
      <PostList posts={posts} empty="Nenhum post público contém esse texto. O rascunho privado não vazou." />
      <Result result={result} />
    </section>
  )
}
