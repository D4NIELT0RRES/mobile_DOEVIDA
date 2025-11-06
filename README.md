# 🩸 Doe Vida Mobile

Este é o **projeto mobile nativo** para Android do **Doe Vida**, desenvolvido como parte do TCC.  
O aplicativo busca oferecer uma **experiência eficiente e moderna**, utilizando as bibliotecas do **Jetpack** para construir interfaces reativas, persistência local e integração com APIs.

---

## 🛠️ Tecnologias e Ferramentas

O projeto utiliza tecnologias modernas para garantir **manutenibilidade, escalabilidade e performance**.  

| Componente              | Versão / Valor               | Propósito                                           |
|-------------------------|-----------------------------|---------------------------------------------------|
| Linguagem               | Kotlin 2.0+                  | Linguagem principal do aplicativo                |
| Android Gradle Plugin   | 8.4.1+                       | Plugin base para build do Android                |
| KSP                     | 2.0.21-1.0.27                | Kotlin Symbol Processing (geração de código do Room) |
| UI                      | Jetpack Compose              | Framework moderno para interfaces declarativas   |
| Persistência Local      | Room Database                | ORM baseado em SQLite para armazenamento local   |
| Rede                    | Retrofit                     | Cliente HTTP para integração com a API           |
| Min SDK                 | 30                            | Versão mínima do Android suportada              |
| Java / JVM Target       | 11                            | Compatibilidade com a JVM                        |

---

## ⚙️ Configuração do Gradle

A configuração do Gradle foi ajustada para um projeto Android **Single Platform**, removendo possíveis conflitos com Kotlin Multiplatform (KMP).  

**Arquivo:** `app/build.gradle.kts`  

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") // KSP versão 2.0.21-1.0.27
    alias(libs.plugins.androidx.room)
}
```
---

## 📱 Funcionalidades Principais

- Cadastro e login de usuários
- Consulta e cadastro de doações de sangue
- Visualização de eventos relacionados à doação
- Interface reativa e intuitiva com **Jetpack Compose**
- Persistência local de dados com **Room Database**
- Comunicação com backend via **Retrofit**


