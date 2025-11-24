# 🚀 Deploy en Render

## Endpoints Disponibles

La API está disponible en:

-   **Health Check**: `GET https://examenmercado-y3h3.onrender.com/`
-   **Swagger UI**: `GET https://examenmercado-y3h3.onrender.com/swagger-ui.html`
-   **Detectar Mutante**: `POST https://examenmercado-y3h3.onrender.com/mutant`
-   **Estadísticas**: `GET https://examenmercado-y3h3.onrender.com/stats`

## Ejemplo de Request

```bash
curl -X POST https://examenmercado-y3h3.onrender.com/mutant \
  -H "Content-Type: application/json" \
  -d '{
    "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
  }'
```
