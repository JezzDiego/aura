# Entrada Livre (projeto Aura)

App Android (Kotlin + Jetpack Compose) para encontrar estabelecimentos com **acessibilidade verificada pela comunidade**.
As telas seguem o Figma [Acessibilidade](https://www.figma.com/design/L4Opg4SjZlcKVHrNlampOm/Acessibilidade).

> **Status:** o app funciona 100% offline. Todos os dados são lidos e gravados localmente (Room + DataStore).
> A camada remota (Retrofit) já tem os contratos prontos, mas **nenhuma chamada HTTP é feita** por enquanto.

## Telas
| Tela | Descrição |
|------|-----------|
| Boas-vindas | Cria um perfil local (nome, e-mail opcional e perfil de acessibilidade). |
| Início | Busca, filtros rápidos, banner "Filtrar pelo meu perfil", estabelecimentos verificados e últimos relatos. |
| Buscar | Busca por nome/categoria/bairro + filtros de acessibilidade (física, auditiva, visual), lista ou mapa offline. |
| Detalhes | Selo de conformidade, checklist por categoria, galeria de verificação, relatos, localização, favoritar, compartilhar e "Como chegar". |
| Avaliar | Checklist obrigatório, relato (até 800 caracteres), perfil opcional e até 3 fotos comprovatórias. |
| Perfil | Perfil e filtros prioritários, preferências de exibição, locais salvos e meus relatos. |

O botão de acessibilidade do header abre ajustes de **alto contraste (tema escuro)**, **texto ampliado** e **reduzir animações**.

## Arquitetura (Clean Architecture + MVVM)
```
app/src/main/java/com/example/aura/
  di/                     # AppContainer (DI manual)
  core/                   # BaseViewModel, ResultWrapper, UiEvent, OnResumeEffect...
  domain/
    model/                # Establishment, AccessibilityFeature, Review, User, enums de acessibilidade
    repository/           # EstablishmentRepository, ReviewRepository, UserRepository
    usecase/              # establishment/, review/, user/
  data/
    local/
      db, dao, entity     # Room (establishments, accessibility_features, establishment_photos, reviews, user)
      datasource/         # *LocalDataSource (+ PhotoLocalDataSource para fotos dos relatos)
      seed/               # LocalSeedData: dados iniciais gravados no primeiro acesso
      preferences/        # DataStore (tema, texto ampliado, animações)
    remote/               # DTOs, APIs Retrofit e RemoteDataSources (ainda não utilizados)
    mapper/               # Entity <-> Domain <-> DTO
    repository/           # Implementações offline-first
  presentation/
    navigation/           # NavHost, rotas, AppActions
    ui/
      components/         # Header, cards, chips, checkbox, imagens...
      theme/              # Paleta e tipografia (Geist) do Figma
      feature_home | feature_search | feature_establishment | feature_review | feature_profile | feature_login | feature_main_pager
```

### Fluxo de dados offline
`ViewModel → UseCase → Repository → LocalDataSource → Room`.
Na primeira leitura, `EstablishmentLocalDataSource` e `ReviewLocalDataSource` populam o banco com `LocalSeedData`.
Relatos publicados, favoritos, perfil e fotos (copiadas para o armazenamento interno) ficam salvos no aparelho.

### Ligando a API no futuro
Os repositórios já recebem os `RemoteDataSource`s pelo `AppContainer`. Basta implementar a sincronização
(ex.: buscar `EstablishmentDTO`, mapear com `toDomain()` e salvar via datasource local) e ajustar `RetrofitClient.BASE_URL`.

## Como rodar
Pelo Android Studio, sincronize o Gradle e rode o app. Via terminal:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```
