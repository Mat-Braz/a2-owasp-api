export default function Result({ result }) {
  if (!result) {
    return null
  }
  const body = result.data == null
    ? ''
    : typeof result.data === 'string'
      ? result.data
      : JSON.stringify(result.data, null, 2)
  return <pre className={result.ok ? 'result ok' : 'result bad'}>{`HTTP ${result.status}\n${body}`}</pre>
}
