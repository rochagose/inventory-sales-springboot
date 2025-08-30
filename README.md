# Sistema de Ventas e Inventario

Este proyecto es un sistema de **ventas e inventario** desarrollado en **Spring Boot** con integración de **Thymeleaf** para la interfaz de usuario y **Spring Security** para la gestión de usuarios. Permite administrar usuarios, productos, ventas y realizar operaciones de punto de venta (POS).

---

## Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring MVC
- Spring Security
- Thymeleaf
- MySQL
- Bootstrap 5
- Maven

---

## Instalación

1. Clonar el repositorio:

```bash
git clone https://github.com/tuusuario/inventario-ventas.git
```

2. Configurar la base de datos en application.properties:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/inventario
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
```

3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

4. Acceder al sistema: 
```bash
http://localhost:8080/login
```

# Screenshots
## Login 
<img width="495" height="524" alt="1" src="https://github.com/user-attachments/assets/1f7d0685-4339-4ba0-929c-22f4021db607" />

## Menú Principal
<img width="1141" height="534" alt="2" src="https://github.com/user-attachments/assets/7296e72c-f91a-4e30-bbf0-471689cd9ba3" />

## POS
<img width="1277" height="435" alt="5" src="https://github.com/user-attachments/assets/4c234eed-f81c-4609-9225-2bf710b6d249" />

