// ============================================================
// TOURPATH — Main JavaScript
// ============================================================
document.addEventListener('DOMContentLoaded', () => {

  // ── Auto-hide alerts ──────────────────────────────────────
  document.querySelectorAll('.alert').forEach(el => {
    setTimeout(() => {
      el.style.transition = 'opacity .5s, transform .5s';
      el.style.opacity = '0'; el.style.transform = 'translateY(-8px)';
      setTimeout(() => el.remove(), 500);
    }, 4500);
  });

  // ── Navbar scroll ─────────────────────────────────────────
  const navbar = document.querySelector('.navbar');
  if (navbar) {
    window.addEventListener('scroll', () => {
      navbar.style.boxShadow = window.scrollY > 50
        ? '0 4px 24px rgba(0,0,0,.15)' : '0 2px 12px rgba(0,0,0,.05)';
    }, { passive: true });
  }

  // ── Card animations ───────────────────────────────────────
  if ('IntersectionObserver' in window) {
    const obs = new IntersectionObserver(entries => {
      entries.forEach((e, i) => {
        if (e.isIntersecting) {
          setTimeout(() => {
            e.target.style.opacity = '1';
            e.target.style.transform = 'translateY(0)';
          }, i * 60);
          obs.unobserve(e.target);
        }
      });
    }, { threshold: 0.1 });

    document.querySelectorAll('.place-card, .city-card, .stat-card').forEach(el => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(20px)';
      el.style.transition = 'opacity .5s, transform .5s';
      obs.observe(el);
    });
  }

  // ── Star rating hover ─────────────────────────────────────
  const labels = document.querySelectorAll('.star-rating label');
  labels.forEach((lbl, idx) => {
    lbl.addEventListener('mouseenter', () => {
      labels.forEach((l, i) => l.style.color = i >= labels.length - idx - 1 ? '#f59e0b' : '#e2e8f0');
    });
  });
  document.querySelector('.star-rating')?.addEventListener('mouseleave', () => {
    labels.forEach(l => l.style.color = '');
  });

  // ── Table search (admin) ──────────────────────────────────
  const tableSearch = document.getElementById('tableSearch');
  if (tableSearch) {
    tableSearch.addEventListener('input', function () {
      const q = this.value.toLowerCase();
      document.querySelectorAll('.admin-table tbody tr').forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
      const counter = document.getElementById('searchCounter');
      if (counter) {
        const visible = document.querySelectorAll('.admin-table tbody tr:not([style*="none"])').length;
        counter.textContent = visible + ' resultado(s)';
      }
    });
  }

  // ── Stat counter animation (admin) ───────────────────────
  document.querySelectorAll('.stat-value').forEach(el => {
    const target = parseInt(el.textContent);
    if (isNaN(target) || target === 0) return;
    let current = 0;
    const step = Math.max(1, Math.ceil(target / 30));
    const timer = setInterval(() => {
      current = Math.min(current + step, target);
      el.textContent = current;
      if (current >= target) clearInterval(timer);
    }, 40);
  });

  // ── Image preview (forms) ─────────────────────────────────
  const imgInput = document.getElementById('imageUrl');
  if (imgInput) {
    imgInput.addEventListener('input', function () {
      const preview = document.getElementById('imgPreview');
      if (preview) { preview.src = this.value; preview.style.display = this.value ? 'block' : 'none'; }
    });
    if (imgInput.value) {
      const preview = document.getElementById('imgPreview');
      if (preview) preview.style.display = 'block';
    }
  }

  // ── Toggle password visibility ────────────────────────────
  window.togglePwd = (id) => {
    const input = document.getElementById(id);
    if (input) input.type = input.type === 'password' ? 'text' : 'password';
  };

  console.log('%c✈️  TourPath', 'color:#f97316; font-weight:900; font-size:1.1rem;');
});
