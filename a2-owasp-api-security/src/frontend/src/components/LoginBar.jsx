import { useState } from 'react'
import { apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import Result from './Result'

const ACCOUNTS = [
  { label: 'Entrar como Alice', username: 'alice', password: '123456' },
  { label: 'Entrar como Bob', username: 'bob', password: '123456' },
  { label: 'Entrar como Admin', username: 'admin', password: 'admin123' }
]

export default function LoginBar() {
  const { version, token, user, setToken, setUser, logout } = useLab()
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  async function enter(account) {
    setBusy(true)

    const response = await apiFetch({
      version,
      path: '/login',
      method: 'POST',
      body: { username: account.username, password: account.password }
    })

    setResult(response)

    if (response.ok) {
      setToken(response.data.token)
      setUser({
        id: response.data.id,
        username: response.data.username,
        role: response.data.role
      })
    }

    setBusy(false)
  }

  return (
    <section className="card">
      <h2>Login rápido</h2>
      <p className="muted">O mesmo JWT vale nas duas versões. O que muda é o filtro que o recebe.</p>
      <div className="row">
        {ACCOUNTS.map((account) => (
          <button key={account.username} type="button" disabled={busy} onClick={() => enter(account)}>
            {account.label}
          </button>
        ))}
        {token && (
          <button type="button" className="ghost" onClick={logout}>
            Sair
          </button>
        )}
      </div>
      {user && (
        <p className="session">
          Sessão: <strong>{user.username}</strong> · id {user.id} · role <strong>{user.role}</strong>
        </p>
      )}
      <Result result={result} />
    </section>
  )
}
