import { useState } from 'react'
import { apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import Result from './Result'

export function AdminMenu() {
  return (
    <section className="card admin">
      <h2>Menu Admin</h2>
      <p>
        Este bloco só aparece porque o React viu <code>role === &quot;ADMIN&quot;</code>. Esconder o menu não protege o endpoint.
      </p>
    </section>
  )
}

export default function BypassPanel() {
  const { version, token } = useLab()
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  async function bypass() {
    setBusy(true)

    const response = await apiFetch({
      version,
      path: '/admin/users/1',
      token,
      method: 'DELETE'
    })

    setResult(response)
    setBusy(false)
  }

  return (
    <section className="card">
      <h2>Prova de segurança só no front</h2>
      <p className="muted">
        O botão chama <code>DELETE /admin/users/1</code> direto. Na v1 o Bob apaga a Alice. Na v2 a resposta é 403. Reinicie o backend para restaurar o seed.
      </p>
      <button type="button" className="danger" disabled={busy} onClick={bypass}>
        Simular Bypass de UI (chamar DELETE /admin/users/1 direto na API)
      </button>
      <Result result={result} />
    </section>
  )
}
