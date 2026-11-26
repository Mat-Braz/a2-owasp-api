import { useEffect, useState } from 'react'
import { apiFetch, forgeRole } from '../api'
import { useLab } from '../context/LabContext'
import Result from './Result'

export function JwtDemo() {
  const { version, token } = useLab()
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    setResult(null)
  }, [version])

  async function forge() {
    setBusy(true)

    try {
      const forged = forgeRole(token, 'ADMIN')
      const response = await apiFetch({ version, path: '/posts', token: forged.token })

      setResult({
        ok: response.ok,
        status: response.status,
        data: {
          licao: response.ok
            ? 'A v1 aceitou o JWT. A role foi trocada para ADMIN e a assinatura antiga foi mantida.'
            : 'A v2 recusou o JWT. A assinatura não confere com o payload alterado.',
          payload: forged.claims,
          resposta: response.data
        }
      })
    } catch (error) {
      setResult({ ok: false, status: 0, data: { error: error.message } })
    }

    setBusy(false)
  }

  return (
    <section className="card">
      <h2>JWT</h2>
      <p className="muted">
        O botão troca a role para ADMIN e reaproveita a assinatura antiga.
      </p>
      <button type="button" className="danger" disabled={busy} onClick={forge}>
        Forjar JWT (role ADMIN, sem reassinar)
      </button>
      <Result result={result} />
    </section>
  )
}

export function CrashDemo() {
  const { version, token } = useLab()
  const [result, setResult] = useState(null)
  const [busy, setBusy] = useState(false)

  useEffect(() => {
    setResult(null)
  }, [version])

  async function crash() {
    setBusy(true)

    const response = await apiFetch({ version, path: '/demo/crash', token })

    setResult(response)
    setBusy(false)
  }

  return (
    <section className="card">
      <h2>Erro 500</h2>
      <p className="muted">
        A v1 devolve a stack. A v2 devolve só uma frase genérica.
      </p>
      <button type="button" disabled={busy} onClick={crash}>
        Forçar erro 500
      </button>
      <Result result={result} />
    </section>
  )
}
