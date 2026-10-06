import { useEffect, useState } from 'react'

const API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

function Badge({ children, type }) {
  return <span className={`badge ${type?.toLowerCase() || ''}`}>{children}</span>
}

function App() {
  const [tickets, setTickets] = useState([])
  const [stats, setStats] = useState({})
  const [statusFilter, setStatusFilter] = useState('')
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState('')
  const [form, setForm] = useState({
    customerName: '', email: '', subject: '', description: ''
  })

  async function load() {
    try {
      const query = statusFilter ? `?status=${statusFilter}` : ''
      const [ticketRes, statsRes] = await Promise.all([
        fetch(`${API}/tickets${query}`),
        fetch(`${API}/tickets/stats`)
      ])
      if (!ticketRes.ok || !statsRes.ok) throw new Error('Backend unavailable')
      setTickets(await ticketRes.json())
      setStats(await statsRes.json())
    } catch (e) {
      setMessage('Cannot connect to backend. Start Spring Boot on port 8080.')
    }
  }

  useEffect(() => { load() }, [statusFilter])

  function change(name, value) {
    setForm(prev => ({ ...prev, [name]: value }))
  }

  async function submit(e) {
    e.preventDefault()
    setLoading(true)
    setMessage('')
    try {
      const res = await fetch(`${API}/tickets`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      })
      if (!res.ok) throw new Error('Ticket creation failed')
      setForm({ customerName: '', email: '', subject: '', description: '' })
      setMessage('Ticket created and analyzed successfully.')
      await load()
    } catch (e) {
      setMessage(e.message)
    } finally {
      setLoading(false)
    }
  }

  async function updateStatus(id, status) {
    await fetch(`${API}/tickets/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status })
    })
    load()
  }

  return (
    <div className="app">
      <header>
        <div>
          <p className="eyebrow">AI-ASSISTED SUPPORT</p>
          <h1>Resolve<span>X</span></h1>
          <p className="subtitle">Intelligent Ticket Resolution System</p>
        </div>
        <div className="status-dot">● System ready</div>
      </header>

      <section className="stats">
        {[
          ['Total', stats.total ?? 0],
          ['Open', stats.open ?? 0],
          ['In Progress', stats.inProgress ?? 0],
          ['Resolved', stats.resolved ?? 0]
        ].map(([label, value]) =>
          <div className="stat" key={label}><small>{label}</small><strong>{value}</strong></div>
        )}
      </section>

      <main>
        <section className="card form-card">
          <h2>Create Support Ticket</h2>
          <p className="muted">Submit a customer issue and let ResolveX analyze it.</p>
          <form onSubmit={submit}>
            <input required placeholder="Customer name" value={form.customerName}
              onChange={e => change('customerName', e.target.value)} />
            <input required type="email" placeholder="Email" value={form.email}
              onChange={e => change('email', e.target.value)} />
            <input required placeholder="Subject" value={form.subject}
              onChange={e => change('subject', e.target.value)} />
            <textarea required rows="5" placeholder="Describe the issue..."
              value={form.description} onChange={e => change('description', e.target.value)} />
            <button disabled={loading}>{loading ? 'Analyzing...' : 'Create & Analyze Ticket'}</button>
          </form>
          {message && <p className="message">{message}</p>}
        </section>

        <section className="card">
          <div className="toolbar">
            <div>
              <h2>Support Tickets</h2>
              <p className="muted">Track and manage incoming requests.</p>
            </div>
            <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)}>
              <option value="">All statuses</option>
              <option value="OPEN">Open</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="RESOLVED">Resolved</option>
              <option value="CLOSED">Closed</option>
            </select>
          </div>

          <div className="tickets">
            {tickets.length === 0 && <div className="empty">No tickets yet. Create one above.</div>}
            {tickets.map(t => (
              <article className="ticket" key={t.id}>
                <div className="ticket-head">
                  <div>
                    <span className="ticket-id">#{t.id}</span>
                    <h3>{t.subject}</h3>
                    <p>{t.customerName} · {t.email}</p>
                  </div>
                  <div className="badges">
                    <Badge type={t.priority}>{t.priority}</Badge>
                    <Badge type={t.status}>{t.status.replace('_', ' ')}</Badge>
                  </div>
                </div>
                <p>{t.description}</p>
                <div className="ticket-meta">
                  <span>Category: <b>{t.category}</b></span>
                  <select value={t.status} onChange={e => updateStatus(t.id, e.target.value)}>
                    <option value="OPEN">Open</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="RESOLVED">Resolved</option>
                    <option value="CLOSED">Closed</option>
                  </select>
                </div>
                {t.aiReply && (
                  <div className="ai-box">
                    <b>AI Suggested Reply</b>
                    <p>{t.aiReply}</p>
                  </div>
                )}
              </article>
            ))}
          </div>
        </section>
      </main>
    </div>
  )
}

export default App
