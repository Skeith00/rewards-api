# rewards-api

Per a generar les claus RSA, tindrem que executar aquestes comandes:

**Generar clau privada**
`openssl genrsa -des3 -out private.pem 2048`

**Generar clau publica a partir de la privada**
`openssl rsa -in private.pem -outform PEM -pubout -out public.pem`

**Convertir clau privada PKCS1 a PKCS8**
`openssl pkcs8 -topk8 -inform pem -in private.pem -outform pem -nocrypt -out privatepkcs8.pem`

https://rietta.com/blog/openssl-generating-rsa-key-from-command/
