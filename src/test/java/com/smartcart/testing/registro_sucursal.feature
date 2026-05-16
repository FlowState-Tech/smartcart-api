
# Relacionado con la US10: Registro de Sucursal
# Actor: Gerente Tienda | Prioridad: Alta | Épica: EP02

Característica: Registro de Sucursal
Como gerente de sucursal, deseo registrar mi tienda de conveniencia para que aparezca en el mapa.

Escenario: E1: Validación de RUC de la cadena
Dado que el gerente ingresa el RUC de la cadena
Cuando el sistema valida con el cuerpo JSON:
"""
      {
        "merchantId": "M-999",
        "name": "Metro - Monterrico",
        "ruc": "20100435671",
        "address": {
          "street": "Av. San Jinés 123",
          "district": "Santiago de Surco",
          "latitude": -12.112,
          "longitude": -77.014
        },
        "operatingHours": [
          { "day": "Monday", "open": "08:00", "close": "22:00" }
        ]
      }
      """
Entonces permite añadir la ubicación específica de la sucursal.

Escenario: E2: Posicionamiento geográfico exacto de un local nuevo
Dado un local nuevo
Cuando se registra la dirección en Surquillo con el cuerpo JSON:
"""
      {
        "merchantId": "M-102",
        "name": "Tienda Tambo Surquillo",
        "ruc": "20601234567",
        "address": {
          "street": "Av. Angamos Este 456",
          "district": "Surquillo",
          "latitude": -12.118,
          "longitude": -77.021
        },
        "operatingHours": [
          { "day": "Everyday", "open": "00:00", "close": "23:59" }
        ]
      }
      """
Entonces el sistema lo posiciona geográficamente de forma exacta.

Escenario: E3: Denegación de registro por duplicidad de coordenadas
Dado que la sucursal ya existe
Cuando el sistema detecta la duplicidad por coordenadas con el cuerpo JSON:
"""
      {
        "merchantId": "M-105",
        "name": "Tienda Clon Coordenadas",
        "ruc": "20601234567",
        "address": {
          "street": "Av. Angamos Este 456",
          "district": "Surquillo",
          "latitude": -12.118,
          "longitude": -77.021
        },
        "operatingHours": []
      }
      """
Entonces deniega el nuevo registro.