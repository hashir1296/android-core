package com.darvis.androidcore

object LoginMockResponse {

   operator fun invoke(): String =
        "{\n" +
                "  \"code\": 200,\n" +
                "  \"message\": \"success\",\n" +
                "  \"data\": {\n" +
                "    \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW1vMSIsImlhdCI6MTY3ODg4NTkwOSwibmJmIjoxNjc4ODg1OTA5LCJqdGkiOiIxYWUxMjRhZC0wYWNmLTRjYzQtYjhkZC0zMmQxNjNlODJlMWQiLCJleHAiOjE2Nzg4ODk1MDksInR5cGUiOiJhY2Nlc3MiLCJmcmVzaCI6ZmFsc2V9.Vnn4KE9TQmR2mYHJIPjDqVf3vCJDvrVZjC3r4_OLXbk\",\n" +
                "    \"refresh_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW1vMSIsImlhdCI6MTY3ODg4NTkwOSwibmJmIjoxNjc4ODg1OTA5LCJqdGkiOiJkZWEwZDkzOS1iMTM4LTQ3ZmItYmZhNy05NDM3NjhmMjI0ODgiLCJleHAiOjE2ODE0Nzc5MDksInR5cGUiOiJyZWZyZXNoIn0.bD4EI8MnmtbtBDh6z0mOKJWW10w-FyJlAxqQ13AkR-8\",\n" +
                "    \"expiry\": 1678889509,\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"user_role\": \"Nurse\"\n" +
                "  }\n" +
                "}"
}