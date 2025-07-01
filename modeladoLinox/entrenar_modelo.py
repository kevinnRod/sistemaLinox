import pandas as pd
import mysql.connector
from sklearn.linear_model import LinearRegression
import joblib
import os
from datetime import datetime

# Eliminar archivos previos si existen
for archivo in ['modelo_ventas_mes.pkl', 'mes_index_actual.pkl', 'mes_actual_nombre.pkl']:
    if os.path.exists(archivo):
        os.remove(archivo)

# Conexión a la BD
conn = mysql.connector.connect(
    host='localhost',
    user='root',
    password='',
    database='linox'
)

# Leer y agrupar por mes
query = """
SELECT DATE_FORMAT(fechaV, '%Y-%m') AS mes,
       SUM(total) AS total_ventas
FROM venta
WHERE id_estado = 1
GROUP BY mes
ORDER BY mes;
"""
df = pd.read_sql(query, conn)
conn.close()

# Obtener el mes actual (formato 'YYYY-MM')
mes_actual = datetime.now().strftime('%Y-%m')

# Filtrar: solo meses anteriores al actual
df_filtrado = df[df['mes'] < mes_actual].copy()

# Agregar índice de mes
df_filtrado['mes_index'] = range(1, len(df_filtrado)+1)

# Entrenar modelo
X = df_filtrado[['mes_index']]
y = df_filtrado['total_ventas']
modelo = LinearRegression()
modelo.fit(X, y)

# El mes a predecir es el mes actual (junio), será el siguiente índice
mes_index_a_predecir = len(df_filtrado) + 1

# Guardar modelo y datos
joblib.dump(modelo, 'modelo_ventas_mes.pkl')
joblib.dump(int(mes_index_a_predecir), 'mes_index_actual.pkl')  # este es el índice para junio
joblib.dump(str(mes_actual), 'mes_actual_nombre.pkl')

print("✅ Modelo entrenado solo con meses anteriores y preparado para predecir el actual.")
