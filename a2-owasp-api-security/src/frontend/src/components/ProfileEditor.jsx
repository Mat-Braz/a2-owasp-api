import { useEffect, useState } from 'react'
import { apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import Result from './Result'

export default function ProfileEditor() {
  const { version, token, user, setUser } = useLab()
  const [bio, setBio] = useState('')
  const [loaded, setLoaded] = useState(null)
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    if (!user || !token) return undefined

    let cancelled = false

    apiFetch({ version, path: `/users/${user.id}`, token }).then((response) => {
      if (cancelled) return
      setLoaded(response)
      if (response.ok && typeof response.data?.bio === 'string') {
        setBio(response.data.bio)
      }
    })
    return () => {
      cancelled = true
    }
  }, [user?.id, version, token])

  async function send(body) {
    setBusy(true)

    const response = await apiFetch({
      version,
      path: `/users/${user.id}`,
      token,
      method: 'PATCH',
      body
    })

    setResult(response)

    if (response.ok && response.data?.role) {
      setUser({
        id: user.id,
        username: response.data.username || user.username,
        role: response.data.role
      })
      if (typeof response.data.bio === 'string') {
        setBio(response.data.bio)
      }
    }

    setBusy(false)
  }

  const leakedPassword = loaded?.ok ? loaded.data?.password : undefined

  return (
    <section className="card">
      <h2>Perfil</h2>
      <p className="muted">A tela só mostra a bio. O botão extra manda role no JSON mesmo assim.</p>
      {leakedPassword && (
        <p className="leak">A v1 devolveu a senha neste GET: {leakedPassword}</p>
      )}
      <label className="field">
        Bio
        <textarea value={bio} onChange={(event) => setBio(event.target.value)} rows={3} />
      </label>
      <div className="row">
        <button type="button" disabled={busy} onClick={() => send({ bio })}>
          Salvar bio
        </button>
        <button
          type="button"
          className="danger"
          disabled={busy}
          onClick={() => send({ bio, role: 'ADMIN' })}
        >
          Simular Mass Assignment (enviar role: ADMIN)
        </button>
      </div>
      <Result result={result} />
    </section>
  )
}
