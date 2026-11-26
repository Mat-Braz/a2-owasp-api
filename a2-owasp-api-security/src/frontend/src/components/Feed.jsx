import { useEffect, useState } from 'react'
import { apiFetch } from '../api'
import { useLab } from '../context/LabContext'
import PostList from './PostList'
import Result from './Result'

export default function Feed() {
  const { version, token, user } = useLab()
  const [posts, setPosts] = useState(null)
  const [result, setResult] = useState(null)

  useEffect(() => {
    if (!token) {
      setPosts(null)
      setResult(null)
      return undefined
    }

    let cancelled = false

    apiFetch({ version, path: '/posts', token }).then((response) => {
      if (cancelled) return
      setResult(response.ok ? null : response)
      setPosts(response.ok && Array.isArray(response.data) ? response.data : [])
    })
    return () => {
      cancelled = true
    }
  }, [version, token, user?.id])

  return (
    <section className="card">
      <h2>Feed</h2>
      <p className="muted">Posts públicos. O rascunho da Alice não deve aparecer aqui.</p>
      {!token && <p className="muted">Entre com uma conta para carregar o feed.</p>}
      <PostList posts={posts} empty="Nenhum post público." />
      <Result result={result} />
    </section>
  )
}
