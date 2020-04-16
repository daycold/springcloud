dependencies {
    compile("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
    compile("de.codecentric:spring-boot-admin-starter-client")
    compile("org.springframework.boot:spring-boot-starter-web")
//    compile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-undertow")
    compile("javax.servlet:javax.servlet-api")

    compile(project(":cloud-web"))
}