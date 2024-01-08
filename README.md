<p align="center">
    <img src="https://cdn.vertcodedevelopment.com/logo-text.png"/>
</p>

---

## About

This repository contains the source code the VCore Framework. The VCore framework is usable in any project using Java 17+. 

## Modules

- Common - Contains common utilities and classes that can be used in all java projects.
- Storage - Contains storage utilities and classes that can be used in all java projects.
- Spigot - Contains spigot utilities and classes that can be used in all spigot/paper projects.

## How to use

### Maven

```xml
<repositories>
    <repository>
        <id>vertcode-repo</id>
        <url>https://maven.vertcode.dev/repository/vertcodedev-public/</url>
    </repository>
</repositories>
<dependency>
    <groupId>dev.vertcode.vcore</groupId>
    <artifactId>{type}</artifactId>
    <version>{version}</version>
</dependency>
```

### Gradle

```groovy
repositories {
    maven {
        url "https://maven.vertcode.dev/repository/vertcodedev-public/"
    }
}
dependencies {
    implementation "dev.vertcode.vcore:{type}:{version}"
}
```