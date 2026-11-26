import { createContext, useContext, useEffect, useMemo, useState } from 'react'

const LabContext = createContext(null)

function readUser() {
  try {
    const raw = localStorage.getItem('sc-user')
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function LabProvider({ children }) {
  const [version, setVersion] = useState(() => localStorage.getItem('sc-version') || 'v1')
  const [token, setToken] = useState(() => localStorage.getItem('sc-token') || '')
  const [user, setUser] = useState(readUser)

  useEffect(() => {
    localStorage.setItem('sc-version', version)
  }, [version])

  useEffect(() => {
    if (token) {
      localStorage.setItem('sc-token', token)
    } else {
      localStorage.removeItem('sc-token')
    }
  }, [token])

  useEffect(() => {
    if (user) {
      localStorage.setItem('sc-user', JSON.stringify(user))
    } else {
      localStorage.removeItem('sc-user')
    }
  }, [user])

  const value = useMemo(() => ({
    version,
    setVersion,
    token,
    setToken,
    user,
    setUser,
    logout() {
      setToken('')
      setUser(null)
    }
  }), [version, token, user])

  return <LabContext.Provider value={value}>{children}</LabContext.Provider>
}

export function useLab() {
  const context = useContext(LabContext)
  if (!context) {
    throw new Error('useLab deve ficar dentro de LabProvider')
  }
  return context
}
