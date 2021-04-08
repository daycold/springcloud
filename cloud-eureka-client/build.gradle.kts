dependencies {
//    compile("org.springframework.boot:spring-boot-starter-undertow")
//    compile("org.springframework.boot:spring-boot-starter-web")
//    compile("org.springframework.cloud:spring-cloud-starter-config")
    compileOnly("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    compileOnly("de.codecentric:spring-boot-admin-starter-client")
    compileOnly(project(":cloud-web-netty"))
}