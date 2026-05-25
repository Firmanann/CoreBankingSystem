# API Testing Documentation

---

## 1. Auth Module (`/api/auth`)

### POST `/register`
- [DONE] **Happy Path:** Payload valid & email baru ➡️ `201/200` (Data masuk DB, password hash)
- [DONE] **Bad Path:** Email sudah terdaftar ➡️ `400/409` (Error: Email exists)
- [DONE] **Bad Path:** Format email salah (tanpa `@`) ➡️ `400 Bad Request`
- [DONE] **Bad Path:** Field mandatory kosong (null/blank) ➡️ `400 Bad Request`
- [DONE] **Bad Path:** Password kurang dari 8 karakter ➡️ `400 Bad Request`

### POST `/login`
- [DONE] **Happy Path:** Email & password benar ➡️ `200 OK` (Return Token JWT)
- [DONE] **Bad Path:** Password salah ➡️ `401 Unauthorized` (Error: Invalid credentials)
- [DONE] **Bad Path:** Email tidak terdaftar ➡️ `401/404`
- [DONE] **Bad Path:** Request body kosong ➡️ `400 Bad Request`

### POST `/refresh`
- [DONE] **Happy Path:** Refresh Token valid ➡️ `200 OK` (Return Access Token baru)
- [DONE] **Bad Path:** Token expired ➡️ `401 Unauthorized`
- [DONE] **Bad Path:** Format token invalid ➡️ `401 Unauthorized`

### POST `/logout`
- [ ] **Happy Path:** Header bawa `Authorization: Bearer <token>` ➡️ `200 OK` (Token dimatikan)
- [ ] **Bad Path:** Tanpa header Authorization ➡️ `401 Unauthorized`
- [ ] **Bad Path:** Double logout (token sama 2x) ➡️ `401 Unauthorized`

---

## 2. Account Module (`/api/account`)

### POST `/me`
- [ ] **Happy Path:** Token JWT valid & payload sesuai ➡️ `201 Created` (Rekening baru berhasil dibuat dan terikat ke user yang login)
- [ ] **Bad Path:** Tanpa token / token expired ➡️ `401 Unauthorized`
- [ ] **Bad Path:** Payload kosong atau tidak memenuhi syarat ➡️ `400 Bad Request`
- [ ] **Bad Path (Limit):** Bikin rekening lagi padahal sudah punya/melewati batas maksimal rekening per user (Jika ada rule ini) ➡️ `409 Conflict` atau `400 Bad Request`

### PATCH `/{accountnumber}`
- [ ] **Happy Path:** Nomor rekening valid, token sah, & payload benar ➡️ `200 OK` (Status rekening berhasil diubah)
- [ ] **Bad Path (IDOR):** Token User A nembak rekening User B ➡️ `403 Forbidden`
- [ ] **Bad Path:** Nomor rekening tidak terdaftar ➡️ `404 Not Found`
- [ ] **Bad Path:** Payload status tidak valid (misal ngirim "AKTIF" padahal ENUM-nya "ACTIVE") ➡️ `400 Bad Request`

### GET `/{accountnumber}`
- [ ] **Happy Path:** Nomor rekening valid & token sah ➡️ `200 OK` (Tampil saldo, tipe, status)
- [ ] **Bad Path (IDOR):** Token User A intip rekening User B ➡️ `403/401`
- [ ] **Bad Path:** Nomor rekening tidak terdaftar ➡️ `404 Not Found`

---

## 3. Transaction Module (`/api/transaction`)

### POST `/deposit`
- [ ] **Happy Path:** Nominal valid ke rekening aktif ➡️ `200 OK` (Saldo nambah, catat CASH_DEPOSIT)
- [ ] **Bad Path:** Nominal minus/nol ➡️ `400 Bad Request`
- [ ] **Bad Path:** Rekening tidak terdaftar ➡️ `404 Not Found`
- [ ] **Bad Path:** Status rekening FROZEN/CLOSED ➡️ `400 Bad Request` (ACCOUNT_STATUS_INACTIVE)

### POST `/withdraw`
- [ ] **Happy Path:** Nominal valid, saldo cukup, rekening sendiri ➡️ `200 OK` (Saldo kurang)
- [ ] **Bad Path (IDOR):** Token User A tarik dari rekening User B ➡️ `403 Forbidden`
- [ ] **Bad Path (Saldo):** Tarik tunai lewati sisa saldo ➡️ `400 Bad Request` (INSUFFICIENT_BALANCE)
- [ ] **Bad Path (Limit):** Tarik tunai di bawah Rp 50.000 ➡️ `400 Bad Request`
- [ ] **Bad Path:** Status rekening tidak aktif ➡️ `400 Bad Request`

### POST `/transfer`
- [ ] **Happy Path:** Nominal valid, saldo cukup, target aktif ➡️ `200 OK` (Saldo pindah)
- [ ] **Bad Path (Self):** Rekening asal = Rekening tujuan ➡️ `400 Bad Request`
- [ ] **Bad Path (IDOR):** Token User A transfer pakai rekening User B ➡️ `403 Forbidden`
- [ ] **Bad Path (Saldo):** Transfer lewati sisa saldo pengirim ➡️ `400 Bad Request` (Saldo target aman)
- [ ] **Bad Path:** Rekening target tidak terdaftar ➡️ `404 Not Found`
- [ ] **Bad Path:** Rekening target tidak aktif ➡️ `400 Bad Request` (Saldo asal aman)