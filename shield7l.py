import json, datetime, os, subprocess, time

class Shield7L:
    def __init__(self):
        self.nombre = "SHIELD7L"
        self.titular = "J.E.Y.M.7"
        self.version = "1.0"
        self.registro = "shield_registro.json"
        self.bloqueados = ["31.56.23.113", "184.255", "103.166", "tiktok.com", "pangle.io", "mvne1.com", "imhernet"]
        if not os.path.exists(self.registro):
            self._guardar({"eventos": []})

    def _guardar(self, datos):
        with open(self.registro, "w") as f:
            json.dump(datos, f, indent=2)

    def _leer(self):
        with open(self.registro, "r") as f:
            return json.load(f)

    def registrar(self, tipo, mensaje):
        entrada = {
            "fecha": datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "tipo": tipo,
            "mensaje": mensaje
        }
        datos = self._leer()
        datos["eventos"].append(entrada)
        self._guardar(datos)
        print(f"✅ [{entrada['fecha']}] {tipo}: {mensaje}")

    def verificar_red(self):
        print("\n" + "="*50)
        print("📡 VERIFICACIÓN DE RED — SHIELD7L")
        print("="*50)
        try:
            res = subprocess.run(
                ["ss", "-tun", "state", "established"],
                capture_output=True, text=True, timeout=5
            )
            salida = res.stdout
            amenazas = []
            for b in self.bloqueados:
                if b in salida:
                    amenazas.append(b)
            if amenazas:
                print(f"🚨 DETECTADO: {', '.join(amenazas)}")
                self.registrar("ALERTA", f"Conexión detectada: {', '.join(amenazas)}")
            else:
                print("✅ TODO LIMPIO — Sin conexiones bloqueadas")
                self.registrar("OK", "Red verificada — sin amenazas")
        except Exception as e:
            print(f"⚠️ Error: {e}")
        print("="*50)

    def monitoreo_continuo(self, intervalo=30):
        print("\n🔒 MONITOREO ACTIVO — Ctrl+C para detener")
        print(f"👤 Titular: {self.titular} | Versión: {self.version}\n")
        try:
            while True:
                ahora = datetime.datetime.now().strftime("%H:%M:%S")
                print(f"[{ahora}] Comprobando...", end="\r")
                self.verificar_red()
                time.sleep(intervalo)
        except KeyboardInterrupt:
            print("\n🔓 Monitoreo detenido — SHIELD7L")

    def ver_registros(self):
        print("\n" + "="*50)
        print("📋 REGISTRO DE EVENTOS")
        print("="*50)
        datos = self._leer()
        if not datos["eventos"]:
            print("📭 Sin eventos registrados")
        else:
            for ev in datos["eventos"][-15:]:
                icono = "✅" if ev["tipo"] == "OK" else "🚨" if ev["tipo"] == "ALERTA" else "📝"
                print(f"{icono} {ev['fecha']} | {ev['tipo']} | {ev['mensaje']}")
        print("="*50)

    def panel(self):
        while True:
            print("\n" + "="*50)
            print(f"🛡️ {self.nombre} — SISTEMA DE SEGURIDAD v{self.version}")
            print(f"👤 Titular: {self.titular}")
            print("🔒 PROTEGIDO — SOLO TÚ")
            print("="*50)
            print("1. Verificar red ahora")
            print("2. Ver registros")
            print("3. Iniciar monitoreo continuo")
            print("4. Salir")
            op = input("\nElige: ")
            if op == "1":
                self.verificar_red()
            elif op == "2":
                self.ver_registros()
            elif op == "3":
                self.monitoreo_continuo()
            elif op == "4":
                print("🔒 Saliendo — SHIELD7L")
                break

if __name__ == "__main__":
    shield = Shield7L()
    shield.panel()
    "5.255.255", "yandex.com", "yandex.ru"
    "5.255.255", "yandex.com", "yandex.ru"
