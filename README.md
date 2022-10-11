# ProTasks
## _Aplicación Android para la gestión y organización de proyectos_

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Albertocalib_ProTasks&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Albertocalib_ProTasks)

Protasks es una aplicación compuesta por dos partes, un backend formado por una API REST y un frontend que sería la aplicación Android. El objetivo de la aplicación es el uso de un tablero Kanban con la inclusión de las características más relevantes de aplicaciones conocidas como pueden ser Trello y ClickUp entre otras.

## Funcionalidades destacadas

- Crear tableros compartidos entre diferentes usuarios
- Creación de listas y tareas
- Visibilidad de las listas en tareas en modo tablero o en modo lista
- Visibilidad y escritura restringida dependiendo del rol por usuario y tablero
- Drag and drop en listas y tareas
- Adicción de Imágenes y archivos a las tareas
- Gráficos de completitud de tareas por usuario y tablero
- Métricas agile como pueden ser el Cycle y el lead time y una gráfica para mostrar dicha información
- Posibilidad de limitar el WIP (Work in progress)

## Tecnologías Usadas
- SpringBoot para el desarrollo de la API REST
- MySQL como base de datos.
- Android para el desarrollo de la aplicación
- Swagger para la documentación de la api

## Librerías
- [ColorPicker](https://github.com/duanhong169/ColorPicker/) -  Selectior de color para Android
- [DragListView](https://github.com/woxblom/DragListView/) -  Drag and drop para reordenar elementos en una lista, en una cuadrícula o en un tablero para Android basado en RecyclerViews.
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart/) -  Librería de gráficos para android
- [Retrofit](https://github.com/square/retrofit) - Librería para hacer peticiones http a la API REST desde Android

## Diagramas de Arquitectura
### Diagrama de clases de las entidades
![diagrama-clases-entidades](https://user-images.githubusercontent.com/43534336/195104642-3bd7aad6-a29d-40c2-a131-f5d6236e9547.png)
### Diagrama de componentes frontend
![diagrama-componentes-frontend](https://user-images.githubusercontent.com/43534336/195104779-d70a205c-450f-4db7-be27-ae8fc19dfbdc.png)
### Diagrama de componentes backend
![diagrama-componentes-backend](https://user-images.githubusercontent.com/43534336/195104891-dc6104c3-1149-4093-9184-ed16b66e19ae.png)

## License

MIT
