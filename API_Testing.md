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

### POST `/logout`'
- [DONE] **Happy Path:** Header bawa Authorization: Bearer <access_token> DAN Body JSON bawa token valid ➡️ `200 OK`
- [DONE] **Bad Path:** Tanpa header Authorization ➡️ `401 Unauthorized` (Ditolak satpam depan)
- [DONE] **Bad Path:** (Fraud Check): Header pakai Token User A, tapi Body JSON minta logout Token User B ➡️ `403 Forbidden`

---

## 2. Account Module (`/api/account`)

### POST `/me`
- [DONE] **Happy Path:** Token JWT valid & payload sesuai ➡️ `201 Created` (Rekening baru berhasil dibuat dan terikat ke user yang login)
- [DONE] **Bad Path:** Tanpa token / token expired ➡️ `401 Unauthorized`
- [DONE] **Bad Path (Limit):** Bikin rekening lagi padahal sudah punya/melewati batas maksimal rekening per user (Jika ada rule ini) ➡️ `409 Conflict` atau `400 Bad Request`

### PATCH `/{accountnumber}`
- [DONE] **Happy Path:** Nomor rekening valid, token sah, & payload benar ➡️ `200 OK` (Status rekening berhasil diubah)
- [DONE] **Bad Path (IDOR):** Token User A nembak rekening User B ➡️ `403 Forbidden`
- [DONE] **Bad Path:** Nomor rekening tidak terdaftar ➡️ `404 Not Found`
- [DONE] **Bad Path:** Payload status tidak valid (misal ngirim "AKTIF" padahal ENUM-nya "ACTIVE") ➡️ `400 Bad Request`

### GET `/{accountnumber}`
- [DONE] **Happy Path:** Nomor rekening valid & token sah ➡️ `200 OK` (Tampil saldo, tipe, status)
- [DONE] **Bad Path (IDOR):** Token User A intip rekening User B ➡️ `403/401`
- [DONE] **Bad Path:** Nomor rekening tidak terdaftar ➡️ `404 Not Found`

---

## 3. Transaction Module (`/api/transaction`)

### POST `/deposit`
- [DONE] **Happy Path:** Nominal valid ke rekening aktif ➡️ `200 OK` (Saldo nambah, catat CASH_DEPOSIT)
- [DONE] **Bad Path:** Nominal minus/nol ➡️ `400 Bad Request`
- [DONE] **Bad Path:** Rekening tidak terdaftar ➡️ `404 Not Found`
- [DONE] **Bad Path:** Status rekening FROZEN/CLOSED ➡️ `400 Bad Request` (ACCOUNT_STATUS_INACTIVE)

### POST `/withdraw`
- [DONE] **Happy Path:** Nominal valid, saldo cukup, rekening sendiri ➡️ `200 OK` (Saldo kurang)
- [DONE] **Bad Path (IDOR):** Token User A tarik dari rekening User B ➡️ `403 Forbidden`
- [DONE] **Bad Path (Saldo):** Tarik tunai lewati sisa saldo ➡️ `400 Bad Request` (INSUFFICIENT_BALANCE)
- [DONE] **Bad Path (Limit):** Tarik tunai di bawah Rp 50.000 ➡️ `400 Bad Request`
- [DONE] **Bad Path:** Status rekening tidak aktif ➡️ `400 Bad Request`

### POST `/transfer`
- [DONE] **Happy Path:** Nominal valid, saldo cukup, target aktif ➡️ `200 OK` (Saldo pindah)
- [DONE] **Bad Path (Self):** Rekening asal = Rekening tujuan ➡️ `400 Bad Request`
- [DONE] **Bad Path (IDOR):** Token User A transfer pakai rekening User B ➡️ `403 Forbidden`
- [DONE] **Bad Path (Saldo):** Transfer lewati sisa saldo pengirim ➡️ `400 Bad Request`
- [DONE] **Bad Path:** Rekening target tidak terdaftar ➡️ `404 Not Found`
- [DONE] **Bad Path:** Rekening target tidak aktif ➡️ `400 Bad Request` 