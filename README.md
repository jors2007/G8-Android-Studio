# 📱 G8-Android-Studio: Gestión de Actividades Personales

Este es el repositorio oficial para la migración de nuestro proyecto del **Grupo 8** desde Java Consola a **Android Studio**. En esta fase, transformaremos nuestra lógica de negocio en una aplicación móvil funcional.

---

## 🧠 Contexto de Migración: De Java a Android

Estamos pasando de un modelo de consola lineal a una **Interfaz Gráfica de Usuario (GUI)**. Es fundamental tener en cuenta:

1. **Adiós al Scanner y al While:** Android funciona por **eventos**. Ya no usaremos `Scanner` ni bucles `while` para esperar órdenes; ahora los botones disparan las acciones.
2. **Ciclo de Vida:** La aplicación ya está "viva"; no necesita un bucle principal. Cada pantalla es una **Activity**.
3. **Persistencia:** Las listas de datos (como el `ArrayList` de actividades) deben manejarse en un repositorio global para que no se borren al cambiar de pantalla.

---

## 🛠️ Requisitos Previos

* **Git:** Instalado y configurado en tu PC.
* **Android Studio:** Versión estable (Otter o posterior).
* **IMPORTANTE:** Al abrir el proyecto, espera a que la barra de **Gradle** (abajo a la derecha) termine de cargar antes de realizar cualquier cambio.

---

## 💻 Guía de Git para el Equipo

Sigan estos pasos para trabajar de forma sincronizada desde la **Terminal** de Android Studio (`Alt + F12`):

### 1. ¿Cómo clonar el proyecto? (Solo la primera vez)
Si aún no tienes el proyecto en tu computadora, abre una carpeta vacía y ejecuta:
```bash
git clone [https://github.com/jors2007/G8-Android-Studio.git](https://github.com/jors2007/G8-Android-Studio.git)
2. ¿Cómo actualizar el repositorio antes de trabajar?
SIEMPRE haz esto antes de tocar una línea de código para evitar conflictos:
git pull origin main

3. ¿Cómo subir tus cambios al terminar?
Una vez que hayas verificado que tu código funciona y no tiene errores rojos:
git add .
git commit -m "Descripción clara de tu cambio (ej: Creado diseño de Hidratación)"
git push origin main

⚠️ Reglas de Oro
❌ No borres archivos que no creaste sin avisar.

❌ No hagas git push --force a menos que el líder lo indique.

✅ Mensajes de commit claros: Ayuda a saber qué hizo cada uno.

✅ Diseño responsivo: Usa el Constraint Widget para que la app se vea bien en todos los celulares.

👥 Integrantes - Grupo 8
Líder de Repositorio: jors2007

Materia: Programación Orientada a Objetos (POO)
