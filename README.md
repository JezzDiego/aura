# Aura

Projeto Android com Kotlin e Jetpack Compose reorganizado para uma base profissional seguindo Clean Architecture + MVVM.

## Estrutura
```
app/
  src/main/java/com/example/aura/
    di/                # Módulos de injeção de dependência (Hilt/Koin)
    data/
      local/           # DataStore/Room/etc
      remote/          # APIs/Retrofit/etc
      repository/      # Implementações de repositories
    domain/
      model/           # Entidades puras de domínio
      repository/      # Interfaces de repositories
      usecase/         # Casos de uso
    presentation/
      navigation/      # Rotas/graph de navegação
      state/           # UIState/UIEvent
      viewmodel/       # ViewModels por feature
      ui/
        components/    # Componentes reutilizáveis
        theme/         # Tema Material3
        feature_home/  # Exemplo de feature (Home)
    core/              # Classes base/utilitários centrais (ResultWrapper, etc.)
    utils/             # Extensões e constantes
```

## Como rodar
Pelo Android Studio, sincronize o Gradle e rode o app. Via terminal:

```bash
./gradlew :app:assembleDebug
```

