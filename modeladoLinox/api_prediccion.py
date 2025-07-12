from flask import Flask, jsonify
import joblib
import pandas as pd
import mysql.connector
from sklearn.ensemble import RandomForestRegressor  # ✅ Importar RandomForest
import os
from datetime import datetime

app = Flask(__name__)

def entrenar_modelo_si_es_nuevo_mes():
    mes_actual = datetime.now().strftime('%Y-%m')
    archivo_mes = 'mes_actual_nombre.pkl'

    if os.path.exists(archivo_mes):
        mes_guardado = joblib.load(archivo_mes)
        if mes_guardado == mes_actual:
            print("Modelo ya entrenado este mes. Cargando modelos...")
            modelo = joblib.load('modelo_ventas_mes.pkl')
            mes_index = joblib.load('mes_index_actual.pkl')
            return modelo, mes_index, mes_guardado

    print("Entrenando modelo para un nuevo mes...")

    for archivo in ['modelo_ventas_mes.pkl', 'mes_index_actual.pkl', 'mes_actual_nombre.pkl']:
        if os.path.exists(archivo):
            os.remove(archivo)

    conn = mysql.connector.connect(
        host=os.getenv("PYMYSQL_HOST", "localhost"),
        user=os.getenv("PYMYSQL_USER", "root"),
        password=os.getenv("PYMYSQL_PASS", "12345"),
        database=os.getenv("PYMYSQL_DB", "linox")
    )

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

    df_filtrado = df[df['mes'] < mes_actual].copy()
    df_filtrado['mes_index'] = range(1, len(df_filtrado) + 1)

    X = df_filtrado[['mes_index']]
    y = df_filtrado['total_ventas']

    # ✅ Usamos RandomForestRegressor
    modelo = RandomForestRegressor(n_estimators=100, random_state=42)
    modelo.fit(X, y)

    mes_index_a_predecir = len(df_filtrado) + 1

    joblib.dump(modelo, 'modelo_ventas_mes.pkl')
    joblib.dump(mes_index_a_predecir, 'mes_index_actual.pkl')
    joblib.dump(mes_actual, 'mes_actual_nombre.pkl')

    return modelo, mes_index_a_predecir, mes_actual

# Entrenamiento si corresponde
modelo, mes_index_actual, mes_nombre_actual = entrenar_modelo_si_es_nuevo_mes()

@app.route('/api/prediccion-mensual', methods=['GET'])
def prediccion_mes():
    prediccion = modelo.predict([[mes_index_actual]])
    return jsonify({
        'mes_index': mes_index_actual,
        'mes': mes_nombre_actual,
        'prediccion': round(float(prediccion[0]), 2)
    })

if __name__ == '__main__':
    print("Flask se está ejecutando desde __main__")
    app.run(debug=False, use_reloader=False, port=5000)
