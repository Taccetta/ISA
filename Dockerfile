FROM ubuntu:latest

# Update inicial e instalacion de git y nodejs
RUN apt update -y && apt upgrade -y && apt install python3-pip -y && apt install python3 -y && apt install git -y && apt install nodejs && apt-get install -y curl zip unzip

# Instalar Java 18
RUN curl -s "https://get.sdkman.io" | bash
RUN /bin/bash -c "source /root/.sdkman/bin/sdkman-init.sh"
RUN /bin/bash -c "sdk install java 18.0.1-open"

# Instalar Jhipster
RUN npm install -g generator-jhipster

# Instalar MySQL
RUN apt-get update && apt-get install -y mysql-server

# Configurar el usuario y la contraseña de MySQL (cambia 'myuser' y 'mypassword' según tus preferencias)
ENV MYSQL_ROOT_PASSWORD=1234
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=1234

RUN echo "mysql-server mysql-server/root_password password $MYSQL_ROOT_PASSWORD" | debconf-set-selections && \
    echo "mysql-server mysql-server/root_password_again password $MYSQL_ROOT_PASSWORD" | debconf-set-selections && \
    apt-get install -y mysql-server

# Exponer el puerto de MySQL
EXPOSE 3306

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos al directorio de trabajo
COPY . /app

# Exponer puerto aplicacion
EXPOSE 8080

# Ejecutar la aplicacion
CMD ["./mvnw", "spring-boot:run"]