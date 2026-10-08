# Práctica 12 — Charla

Código de arranque de la app de la Práctica 12 de TC2007B.

Charla es un chat en tiempo real cuya sala vive en **tu** computadora. Este repo
trae las pantallas ya dibujadas con el sistema de diseño de la Práctica 7, pero
sin nada detrás: hoy les escribes el WebSocket, la invitación con QR, las tres
formas de entrar a la sala de alguien más, la reconexión y sus pruebas.

El servidor va en su propio repo: `practica-12-charla-servidor`. Lo construyes
en la Parte A de la guía, antes de tocar esta app.

## Cómo empezar

1. Clona el repositorio y ábrelo en Android Studio.
2. En `local.properties` agrega `charla.clave=` con la misma clave del `.env` de tu servidor.
3. Corre la app una vez: se ve la sala vacía, «Sin conexión».
4. Sigue la guía: https://startdroid.com/practicas/charla.html

## Cómo trabajar

Haz un commit en cada checkpoint de la guía:

    git add -A ; git commit -m "checkpoint b4"

El experimento del Bloque D va en la rama `experimento-guardia`, con commit en la
rama antes de volver a `main`.

## Uso de IA

Todo commit con código generado por IA debe declararlo con un trailer
`Co-Authored-By`. Ver la política completa en la guía.

## Entrega

Ver la rúbrica en la guía. Sube todas las ramas: `git push origin --all`.
