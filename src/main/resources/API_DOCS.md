POST /api/auth/register 

Request :

{
"username": "string",
"email": "string",
"password": "string"
}

Respones : 

Success 201 - Created  
{
"status": "success",
"message": "User registered successfully",
"data": {
"id": 1,
"username": "john_doe",
"email": "john@mail.com",
"created_at": "2026-04-11T18:00:00Z"
}
}

Error 400 - Bad request / Validasi gagal
{
"status": "error",
"message": "Validation failed",
"errors": {
"email": "Invalid email format",
"password": "Password must be at least 8 characters"
}
}

Error 409 - Conflict Data 
{
"status": "error",
"message": "Email already exists"
}