const state = {
  cart: [],
  descuento: 0, // %
  iva: true,
  ivaRate: 0.16,
  hold: [],
};

// ======= Utilidades =======
const fmt = (n) =>
  n.toLocaleString("es-MX", { style: "currency", currency: "MXN" });
const qs = (s) => document.querySelector(s);

function addToCart(prod) {
  const idx = state.cart.findIndex((i) => i.id === prod.id);
  if (idx >= 0) {
    state.cart[idx].qty++;
  } else {
    state.cart.push({ ...prod, qty: 1 });
  }
  renderCart();
}

function updateQty(id, delta) {
  const item = state.cart.find((i) => i.id === id);
  if (!item) return;
  item.qty = Math.max(1, item.qty + delta);
  renderCart();
}

function removeItem(id) {
  state.cart = state.cart.filter((i) => i.id !== id);
  renderCart();
}

function calcTotals() {
  const subtotal = state.cart.reduce((a, i) => a + i.precio * i.qty, 0);
  const desc = subtotal * (state.descuento / 100);
  const base = subtotal - desc;
  const iva = state.iva ? base * state.ivaRate : 0;
  const total = base + iva;
  return { subtotal, desc, iva, total };
}

function renderCart() {
  const tbody = qs("#cartBody");
  tbody.innerHTML = "";

  state.cart.forEach((item) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>
        <div class="d-flex align-items-center gap-2">
          <span class="fw-semibold">${item.nombre}</span>
        </div>
      </td>
      <td class="text-center">
        <div class="btn-group" role="group">
          <button class="btn btn-outline-secondary btn-sm" title="-1">
            <i class="bi bi-dash"></i>
          </button>
          <span class="btn btn-ghost">${item.qty}</span>
          <button class="btn btn-outline-secondary btn-sm" title="+1">
            <i class="bi bi-plus"></i>
          </button>
        </div>
      </td>
      <td class="text-end">${fmt(item.precio)}</td>
      <td class="text-end">${fmt(item.precio * item.qty)}</td>
      <td class="text-center">
        <button class="btn btn-link text-danger p-0" title="Eliminar">
          <i class="bi bi-x-circle"></i>
        </button>
      </td>
    `;

    // Botones +/- y eliminar
    const btnMinus = tr.querySelector('button[title="-1"]');
    const btnPlus = tr.querySelector('button[title="+1"]');
    const btnRemove = tr.querySelector('button[title="Eliminar"]');

    if (btnMinus)
      btnMinus.addEventListener("click", () => updateQty(item.id, -1));
    if (btnPlus) btnPlus.addEventListener("click", () => updateQty(item.id, 1));
    if (btnRemove)
      btnRemove.addEventListener("click", () => removeItem(item.id));

    tbody.appendChild(tr);
  });

  // Totales
  const { subtotal, desc, iva, total } = calcTotals();
  qs("#subtotalTxt").textContent = fmt(subtotal);
  qs("#descuentoTxt").textContent = "-" + fmt(desc);
  qs("#descLabel").textContent = state.descuento ? `(${state.descuento}%)` : "";
  qs("#ivaTxt").textContent = fmt(iva);
  qs("#totalTxt").textContent = fmt(total);
  qs("#totalCobrarTxt").textContent = fmt(total);
}

function renderProductos(list) {
  const cont = qs("#gridProductos");
  cont.innerHTML = "";
  list.forEach((p) => {
    const col = document.createElement("div");
    col.className = "col";
    col.innerHTML = `
          <div class="card h-100 product-card" data-id="${p.id}">
            <div class="card-body d-flex flex-column">
              <h6 class="card-title mb-1">${p.nombre}</h6>
              <div class="text-muted small mb-2">SKU: ${p.codigo}</div>
              <div class="mt-auto d-flex align-items-center justify-content-between">
                <span class="fw-semibold">${fmt(p.precio)}</span>
                <span class="badge text-bg-${
                  p.existencia > 0 ? "success" : "secondary"
                }">Stock: ${p.existencia}</span>
              </div>
            </div>
          </div>`;
    col
      .querySelector(".product-card")
      .addEventListener("click", () => addToCart(p));
    cont.appendChild(col);
  });
}

// ======= Eventos UI =======
qs("#descuentoInput").addEventListener("input", (e) => {
  const v = Number(e.target.value);
  state.descuento = isNaN(v) ? 0 : Math.min(100, Math.max(0, v));
  renderCart();
});

qs("#ivaSwitch").addEventListener("change", (e) => {
  state.iva = e.target.checked;
  renderCart();
});

qs("#btnClear").addEventListener("click", () => {
  state.cart = [];
  renderCart();
  qs("#scanInput").focus();
});

qs("#btnCobrar").addEventListener("click", () => {
  const modal = new bootstrap.Modal("#modalPago");
  modal.show();
  setTimeout(() => qs("#recibidoInput").focus(), 200);
});

// Pago
document.querySelectorAll('input[name="metodoPago"]').forEach((r) =>
  r.addEventListener("change", (e) => {
    const val = e.target.value;
    qs("#sectionEfectivo").classList.toggle("d-none", val !== "efectivo");
    qs("#sectionMixto").classList.toggle("d-none", val !== "mixto");
  })
);

qs("#recibidoInput").addEventListener("input", () => {
  const { total } = calcTotals();
  const rec = Number(qs("#recibidoInput").value || 0);
  const cambio = Math.max(0, rec - total);
  qs("#cambioInput").value = fmt(cambio);
});

qs("#btnConfirmarPago").addEventListener("click", async () => {
  if (state.cart.length === 0) {
    alert("El carrito está vacío.");
    return;
  }

  const cliente = qs("#clienteSelect").value;
  const metodo = document.querySelector(
    'input[name="metodoPago"]:checked'
  ).value;

  // Preparar el array de articulos según lo que espera el backend
  const articulos = state.cart.map((i) => ({
    productoId: i.id, // coincide con el @RequestBody
    cantidad: i.qty,
    total: i.precio * i.qty,
  }));

  try {
    const res = await fetch("/admin/menu/pos/registrar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(articulos),
    });

    if (!res.ok) throw new Error(`Error registrando la venta: ${res.status}`);

    // Venta registrada correctamente
    bootstrap.Modal.getInstance("#modalPago").hide();
    state.cart = [];
    renderCart();
    qs("#scanInput").focus();

    // Toast de éxito
    const toast = document.createElement("div");
    toast.className = "position-fixed bottom-0 end-0 p-3";
    toast.innerHTML = `
      <div class="toast align-items-center text-bg-success" role="alert">
        <div class="d-flex">
          <div class="toast-body">Venta registrada correctamente.</div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
      </div>`;
    document.body.appendChild(toast);
    const t = new bootstrap.Toast(toast.querySelector(".toast"));
    t.show();
    setTimeout(() => toast.remove(), 4000);
  } catch (err) {
    console.error(err);
    alert("Ocurrió un error al registrar la venta. Revisa la consola.");
  }
});

// Buscar / escanear
function doSearch() {
  const term = qs("#scanInput").value.trim().toLowerCase();
  if (!term) {
    renderProductos(productos);
    return;
  }
  const filtered = productos.filter(
    (p) =>
      p.id.toLowerCase().includes(term) || p.nombre.toLowerCase().includes(term)
  );
  renderProductos(filtered);
}

qs("#btnBuscar").addEventListener("click", doSearch);
qs("#scanInput").addEventListener("keydown", (e) => {
  if (e.key === "Enter") {
    doSearch();
  }
});

document.querySelectorAll("[data-sort]").forEach((a) =>
  a.addEventListener("click", (e) => {
    e.preventDefault();
    const type = a.getAttribute("data-sort");
    const list = [...productos];
    list.sort((x, y) => {
      if (type === "price") return x.precio - y.precio;
      if (type === "stock") return x.stock - y.stock;
      return x.nombre.localeCompare(y.nombre);
    });
    renderProductos(list);
  })
);

// Inicializar
renderProductos(productos);
renderCart();
