import { authorName } from '../api'

export default function PostList({ posts, empty }) {
  if (!posts) {
    return null
  }
  if (posts.length === 0) {
    return <p className="muted">{empty}</p>
  }
  return (
    <ul className="posts">
      {posts.map((post) => (
        <li key={post.id} className={post.isPrivate ? 'post private' : 'post'}>
          <div className="post-meta">
            <strong>Post {post.id}</strong>
            <span>{authorName(post.authorId)}</span>
            <em>{post.isPrivate ? 'privado' : 'público'}</em>
          </div>
          <p>{post.content}</p>
        </li>
      ))}
    </ul>
  )
}
