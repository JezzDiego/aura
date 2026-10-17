package com.example.aura.data.local.seed

import com.example.aura.domain.model.AccessibilityFeature
import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.Establishment
import com.example.aura.domain.model.EstablishmentPhoto
import com.example.aura.domain.model.FeatureCategory
import com.example.aura.domain.model.FeatureCategory.ACCESS_ENTRY
import com.example.aura.domain.model.FeatureCategory.ADAPTED_RESTROOM
import com.example.aura.domain.model.FeatureCategory.COMMUNICATION
import com.example.aura.domain.model.FeatureCategory.INTERNAL_CIRCULATION
import com.example.aura.domain.model.FeatureCategory.SENSORY
import com.example.aura.domain.model.FeatureStatus
import com.example.aura.domain.model.Review
import com.example.aura.domain.model.ReviewChecklistItem
import com.example.aura.utils.Constants

/**
 * Dados iniciais usados enquanto o app funciona 100% offline.
 * São gravados no Room na primeira leitura dos datasources locais.
 */
object LocalSeedData {

    private fun drawable(name: String) = Constants.drawableUri(name)

    private const val DAY = 24L * 60 * 60 * 1000

    private class FeatureBuilder(private val establishmentId: String) {
        val items = mutableListOf<AccessibilityFeature>()

        fun add(
            category: FeatureCategory,
            title: String,
            shortLabel: String,
            description: String,
            status: FeatureStatus = FeatureStatus.AVAILABLE,
            badge: String? = null,
            filter: AccessibilityFilter? = null,
            highlight: Boolean = false,
        ) {
            items += AccessibilityFeature(
                id = "$establishmentId-f${items.size + 1}",
                establishmentId = establishmentId,
                category = category,
                title = title,
                shortLabel = shortLabel,
                description = description,
                status = status,
                badge = badge,
                filter = filter,
                highlight = highlight,
            )
        }
    }

    private fun features(establishmentId: String, block: FeatureBuilder.() -> Unit) =
        FeatureBuilder(establishmentId).apply(block).items.toList()

    val establishments: List<Establishment> by lazy {
        listOf(
            saborArte(),
            livrariaHarmonia(),
            cineBelasArtes(),
            cafeAcolher(),
            espacoArtes(),
            terracoAromas(),
        )
    }

    private fun saborArte(): Establishment {
        val id = "sabor-arte"
        return Establishment(
            id = id,
            name = "Restaurante Sabor & Arte",
            category = "Gastronomia",
            categoryDetail = "Restaurante & Culinária Brasileira",
            address = "Rua Augusta, 1420 - Consolação",
            neighborhood = "Consolação",
            city = "São Paulo - SP",
            imageUrl = drawable("img_sabor_arte_interior"),
            heroImageUrl = drawable("img_sabor_arte_fachada"),
            rating = 4.8,
            reviewCount = 64,
            distanceMeters = 450,
            verifiedAt = "Outubro/24",
            auditorsCount = 14,
            conformityPercent = 94,
            summary = "Espaço com excelente estrutura física para cadeirantes, baixa sobrecarga acústica e atendimento inclusivo no turno noturno.",
            nearbyTransit = "Metrô acessível próximo (Higienópolis-Mackenzie)",
            surroundings = "A 180 metros da Estação Higienópolis-Mackenzie (Linha 4-Amarela do Metrô, com elevadores funcionais).",
            latitude = -23.5532,
            longitude = -46.6562,
            features = features(id) {
                add(ACCESS_ENTRY, "Entrada principal sem degraus", "Entrada sem degrau",
                    "Rampa suave com inclinação regulamentada (6%) e corrimão duplo contínuo em ambos os lados, sem barreiras no solo.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE, highlight = true)
                add(ACCESS_ENTRY, "Portas automáticas deslizantes", "Portas automáticas",
                    "Vão livre de passagem de 100cm, sensor de presença ajustado na altura de cadeiras de rodas e pessoas de baixa estatura.")
                add(ACCESS_ENTRY, "Calçada rebaixada em frente ao local", "Calçada rebaixada",
                    "Guia rebaixada nivelada com a faixa de pedestres com piso podotátil direcional e de alerta em ótimo estado de conservação.",
                    filter = AccessibilityFilter.TACTILE_FLOOR)
                add(INTERNAL_CIRCULATION, "Mesas acessíveis", "Mesas acessíveis",
                    "Altura livre de 78cm sob a superfície, possibilitando aproximação frontal total e confortável para cadeiras de rodas motorizadas e manuais.",
                    filter = AccessibilityFilter.LOWERED_COUNTER)
                add(INTERNAL_CIRCULATION, "Espaço entre mesas amplo", "Espaço amplo entre mesas",
                    "Corredores principais desobstruídos com manobra livre de giro de 360 graus em todos os módulos do salão.",
                    badge = "Mínimo 1,20m", filter = AccessibilityFilter.WIDE_CORRIDORS)
                add(INTERNAL_CIRCULATION, "Piso antiderrapante e regular", "Piso antiderrapante",
                    "Revestimento uniforme e fosco, sem reflexos fortes, sem tapetes soltos ou desníveis repentinos.")
                add(ADAPTED_RESTROOM, "Banheiro unissex independente", "Banheiro adaptado",
                    "Cabine ampla dedicada, sem necessidade de transitar por vestiários segregados, permitindo acompanhante de qualquer gênero.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM, highlight = true)
                add(ADAPTED_RESTROOM, "Barras de apoio e alarme de emergência", "Alarme de emergência",
                    "Barras horizontais e articuladas em aço inoxidável e cordão de emergência sonoro/luminoso posicionado a 40cm do solo.",
                    filter = AccessibilityFilter.VISUAL_ALERT)
                add(COMMUNICATION, "Atendente capacitado em Libras", "Atendimento em Libras",
                    "Equipe da noite conta com o colaborador Marcelo, fluente na Língua Brasileira de Sinais, pronto para recepcionar e tirar dúvidas sobre o cardápio.",
                    badge = "Turno Noite", filter = AccessibilityFilter.LIBRAS_SERVICE, highlight = true)
                add(COMMUNICATION, "Cardápio digital acessível e impresso 18pt", "Cardápio digital acessível",
                    "Menu em QR Code totalmente compatível com TalkBack e VoiceOver, e versão física em tipografia de alto contraste com corpo 18pt.")
                add(COMMUNICATION, "Cardápio físico em Braille", "Cardápio em Braille",
                    "Ainda não possui exemplar físico tátil em relevo Braille. O restaurante recomenda o uso do cardápio digital por leitores de tela no momento.",
                    status = FeatureStatus.UNAVAILABLE, filter = AccessibilityFilter.BRAILLE_MATERIALS)
            },
            photos = listOf(
                EstablishmentPhoto("$id-p1", id, drawable("img_sabor_arte_rampa"),
                    "Rampa de entrada com corrimão duplo",
                    "Inclinação suave testada com cadeira manual sem exigir esforço excessivo."),
                EstablishmentPhoto("$id-p2", id, drawable("img_sabor_arte_banheiro"),
                    "Sanitário adaptado unissex",
                    "Área de giro livre superior a 1,50m e alarme de chamada ativo."),
                EstablishmentPhoto("$id-p3", id, drawable("img_sabor_arte_entrada"),
                    "Entrada principal e nivelamento com a calçada",
                    "Sem degraus ou canaletas que possam travar as rodas dianteiras."),
            ),
        )
    }

    private fun cineBelasArtes(): Establishment {
        val id = "cine-belas-artes"
        return Establishment(
            id = id,
            name = "Cine Belas Artes",
            category = "Cinema & Cultura",
            categoryDetail = "Cinema de Rua & Centro Cultural",
            address = "Rua da Consolação, 2423 - Cerqueira César",
            neighborhood = "Cerqueira César",
            city = "São Paulo - SP",
            imageUrl = drawable("img_cine_belas_artes"),
            heroImageUrl = null,
            rating = 4.9,
            reviewCount = 51,
            distanceMeters = 1200,
            verifiedAt = "Fevereiro/24",
            auditorsCount = 10,
            conformityPercent = 91,
            summary = "Salas com elevador sonorizado, assentos reservados e sessões com legenda descritiva e audiodescrição.",
            nearbyTransit = "Metrô acessível próximo (Consolação / Paulista)",
            surroundings = "A 250 metros da Estação Consolação (Linha 2-Verde do Metrô, com elevadores e piso tátil).",
            latitude = -23.5560,
            longitude = -46.6625,
            features = features(id) {
                add(ACCESS_ENTRY, "Rampa de acesso com corrimão", "Rampa com corrimão",
                    "Rampa externa larga com corrimão em dois níveis conduzindo à bilheteria sem degraus.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE)
                add(INTERNAL_CIRCULATION, "Elevador com áudio e Braille", "Elevador com áudio",
                    "Elevador com anúncio sonoro dos andares, botoeira em Braille e espaço para cadeira de rodas e acompanhante.",
                    filter = AccessibilityFilter.WIDE_CORRIDORS, highlight = true)
                add(INTERNAL_CIRCULATION, "Assentos acessíveis reservados", "Assento acessível",
                    "Espaços reservados para cadeira de rodas em todas as salas, com assento ao lado para acompanhante.",
                    highlight = true)
                add(ADAPTED_RESTROOM, "Sanitário adaptado em cada andar", "Banheiro adaptado",
                    "Cabines com barras de apoio, lavatório suspenso e espelho inclinado.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM)
                add(COMMUNICATION, "Sessões com legenda descritiva", "Legenda descritiva",
                    "Sessões semanais com legenda descritiva (LSE) e janela de Libras nas exibições especiais.",
                    filter = AccessibilityFilter.ACCESSIBLE_VIDEO, highlight = true)
                add(SENSORY, "Audiodescrição por aplicativo", "Audiodescrição",
                    "Audiodescrição disponível via aplicativo parceiro com fones fornecidos na bilheteria.",
                    status = FeatureStatus.PARTIAL, filter = AccessibilityFilter.BRAILLE_MATERIALS)
                add(COMMUNICATION, "Atendimento em Libras", "Atendimento em Libras",
                    "Atendimento em Libras disponível apenas em eventos agendados.",
                    status = FeatureStatus.PARTIAL, filter = AccessibilityFilter.LIBRAS_SERVICE)
            },
        )
    }

    private fun livrariaHarmonia(): Establishment {
        val id = "livraria-harmonia"
        return Establishment(
            id = id,
            name = "Livraria & Café Harmonia",
            category = "Café & Literatura",
            categoryDetail = "Livraria & Cafeteria",
            address = "Alameda Santos, 1100 - Cerqueira César",
            neighborhood = "Cerqueira César",
            city = "São Paulo - SP",
            imageUrl = drawable("img_livraria_harmonia"),
            heroImageUrl = null,
            rating = 4.7,
            reviewCount = 37,
            distanceMeters = 800,
            verifiedAt = "Janeiro/24",
            auditorsCount = 9,
            conformityPercent = 89,
            summary = "Referência em acessibilidade visual: piso tátil contínuo, cardápio em Braille e QR Codes com audiodescrição.",
            nearbyTransit = "Metrô acessível próximo (Trianon-Masp)",
            surroundings = "A 400 metros da Estação Trianon-Masp (Linha 2-Verde do Metrô).",
            latitude = -23.5650,
            longitude = -46.6550,
            features = features(id) {
                add(ACCESS_ENTRY, "Entrada nivelada", "Entrada sem degrau",
                    "Porta de 110cm totalmente nivelada com a calçada.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE)
                add(SENSORY, "Piso tátil contínuo", "Piso tátil contínuo",
                    "Piso tátil direcional da calçada até o caixa, café e sanitários.",
                    filter = AccessibilityFilter.TACTILE_FLOOR, highlight = true)
                add(SENSORY, "Cardápio em Braille e fonte ampliada", "Cardápio em Braille",
                    "Cardápio impresso em Braille e versão em fonte ampliada de alto contraste.",
                    filter = AccessibilityFilter.BRAILLE_MATERIALS, highlight = true)
                add(SENSORY, "QR Codes com audiodescrição", "QR Audiodescrição",
                    "QR Codes nas estantes e no cardápio com audiodescrição dos produtos e rotas.",
                    highlight = true)
                add(SENSORY, "Cão-guia bem-vindo", "Cão-guia bem-vindo",
                    "Área com pote de água e espaço para descanso do cão-guia.",
                    filter = AccessibilityFilter.GUIDE_DOG_WELCOME)
                add(ADAPTED_RESTROOM, "Sanitário adaptado", "Banheiro adaptado",
                    "Banheiro adaptado com barras de apoio no térreo.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM)
                add(INTERNAL_CIRCULATION, "Corredores entre estantes", "Corredores largos",
                    "Corredores com 85cm em alguns trechos, abaixo do recomendado para giro completo.",
                    status = FeatureStatus.PARTIAL, filter = AccessibilityFilter.WIDE_CORRIDORS)
            },
        )
    }

    private fun cafeAcolher(): Establishment {
        val id = "cafe-acolher"
        return Establishment(
            id = id,
            name = "Café Acolher & Livros",
            category = "Café & Confeitaria",
            categoryDetail = "Café, Confeitaria & Livros",
            address = "Rua dos Pinheiros, 420 - Pinheiros",
            neighborhood = "Pinheiros",
            city = "São Paulo - SP",
            imageUrl = drawable("img_cafe_acolher"),
            heroImageUrl = null,
            rating = 4.9,
            reviewCount = 42,
            distanceMeters = 450,
            verifiedAt = "Agosto/24",
            auditorsCount = 8,
            conformityPercent = 96,
            summary = "Café acolhedor com rampa nível zero, banheiro amplo e dois atendentes fluentes em Libras.",
            nearbyTransit = "Metrô acessível próximo (Fradique Coutinho)",
            surroundings = "A 300 metros da Estação Fradique Coutinho (Linha 4-Amarela do Metrô, com elevadores).",
            latitude = -23.5660,
            longitude = -46.6840,
            features = features(id) {
                add(ACCESS_ENTRY, "Rampa nível zero", "Rampa nível zero (Sem degrau)",
                    "Entrada em rampa de madeira com inclinação suave e corrimão.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE, highlight = true)
                add(ADAPTED_RESTROOM, "Banheiro amplo com giro 360°", "Banheiro amplo com giro 360°",
                    "Sanitário com área de manobra completa e barras de apoio.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM, highlight = true)
                add(COMMUNICATION, "Atendimento em Libras", "2 atendentes fluentes em Libras",
                    "Dois atendentes fluentes em Libras em todos os turnos.",
                    filter = AccessibilityFilter.LIBRAS_SERVICE, highlight = true)
                add(INTERNAL_CIRCULATION, "Balcão rebaixado", "Balcão rebaixado",
                    "Balcão de pedidos com trecho a 80cm do piso.",
                    filter = AccessibilityFilter.LOWERED_COUNTER)
                add(SENSORY, "Cardápio em fonte ampliada", "Cardápio ampliado",
                    "Cardápio em fonte ampliada; versão em Braille sob pedido.",
                    status = FeatureStatus.PARTIAL, filter = AccessibilityFilter.BRAILLE_MATERIALS)
            },
        )
    }

    private fun espacoArtes(): Establishment {
        val id = "espaco-artes"
        return Establishment(
            id = id,
            name = "Espaço das Artes Paulista",
            category = "Centro Cultural & Teatro",
            categoryDetail = "Centro Cultural, Teatro & Exposições",
            address = "Av. Paulista, 1100 - Bela Vista",
            neighborhood = "Bela Vista",
            city = "São Paulo - SP",
            imageUrl = drawable("img_espaco_artes"),
            heroImageUrl = null,
            rating = 5.0,
            reviewCount = 88,
            distanceMeters = 1200,
            verifiedAt = "Setembro/24",
            auditorsCount = 12,
            conformityPercent = 98,
            summary = "Centro cultural com acesso plano, cabines sinalizadas e mediadores em Libras em todas as exposições.",
            nearbyTransit = "Metrô acessível próximo (Trianon-Masp)",
            surroundings = "A 150 metros da Estação Trianon-Masp (Linha 2-Verde do Metrô, com elevadores).",
            latitude = -23.5617,
            longitude = -46.6560,
            features = features(id) {
                add(ACCESS_ENTRY, "Acesso plano sem desnível", "Acesso plano sem desnível",
                    "Entrada direta pela Av. Paulista sem degraus ou soleiras.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE, highlight = true)
                add(ADAPTED_RESTROOM, "Cabine acessível sinalizada", "Cabine acessível sinalizada",
                    "Sanitários acessíveis com sinalização visual, tátil e alarme luminoso.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM, highlight = true)
                add(COMMUNICATION, "Mediadores em Libras", "Equipe com mediadores em Libras",
                    "Equipe educativa com mediadores fluentes em Libras em todas as exposições.",
                    filter = AccessibilityFilter.LIBRAS_SERVICE, highlight = true)
                add(INTERNAL_CIRCULATION, "Circulação ampla", "Corredores largos",
                    "Corredores e galerias com mais de 1,50m de largura.",
                    filter = AccessibilityFilter.WIDE_CORRIDORS)
                add(SENSORY, "Piso tátil", "Piso tátil",
                    "Piso tátil direcional da entrada à recepção e elevadores.",
                    filter = AccessibilityFilter.TACTILE_FLOOR)
                add(COMMUNICATION, "Alerta visual de emergência", "Alerta visual",
                    "Luzes estroboscópicas integradas ao sistema de alarme.",
                    filter = AccessibilityFilter.VISUAL_ALERT)
                add(COMMUNICATION, "Vídeos com Libras e legenda", "Vídeos acessíveis",
                    "Conteúdos audiovisuais das exposições com janela de Libras e legenda.",
                    filter = AccessibilityFilter.ACCESSIBLE_VIDEO)
            },
        )
    }

    private fun terracoAromas(): Establishment {
        val id = "terraco-aromas"
        return Establishment(
            id = id,
            name = "Terraço Aromas & Sentidos",
            category = "Restaurante & Bistrô",
            categoryDetail = "Bistrô Contemporâneo",
            address = "Rua Oscar Freire, 810 - Cerqueira César",
            neighborhood = "Cerqueira César",
            city = "São Paulo - SP",
            imageUrl = drawable("img_terraco_aromas"),
            heroImageUrl = null,
            rating = 4.7,
            reviewCount = 19,
            distanceMeters = 1800,
            verifiedAt = "Maio/24",
            auditorsCount = 5,
            conformityPercent = 78,
            summary = "Bistrô com rampa externa em conformidade e vídeo-cardápio em Libras; circulação interna pode ficar apertada nos horários de pico.",
            nearbyTransit = "Metrô acessível próximo (Oscar Freire)",
            surroundings = "A 500 metros da Estação Oscar Freire (Linha 4-Amarela do Metrô).",
            latitude = -23.5630,
            longitude = -46.6700,
            features = features(id) {
                add(ACCESS_ENTRY, "Rampa externa em conformidade", "Rampa externa em conformidade",
                    "Rampa externa com inclinação de 8% e corrimão em um dos lados.",
                    filter = AccessibilityFilter.STEP_FREE_ENTRANCE, highlight = true)
                add(ADAPTED_RESTROOM, "Sanitário adaptado unissex", "Sanitário adaptado unissex",
                    "Sanitário unissex com barras de apoio.",
                    filter = AccessibilityFilter.ADAPTED_RESTROOM, highlight = true)
                add(COMMUNICATION, "Vídeo cardápio com Libras", "Vídeo cardápio com Libras",
                    "Cardápio em vídeo com intérprete de Libras via QR Code.",
                    filter = AccessibilityFilter.LIBRAS_SERVICE, highlight = true)
                add(INTERNAL_CIRCULATION, "Espaço entre mesas", "Espaço entre mesas",
                    "Circulação entre mesas abaixo de 90cm nos horários de pico.",
                    status = FeatureStatus.PARTIAL, filter = AccessibilityFilter.WIDE_CORRIDORS)
                add(SENSORY, "Cardápio em Braille", "Cardápio em Braille",
                    "Não possui cardápio em Braille.",
                    status = FeatureStatus.UNAVAILABLE, filter = AccessibilityFilter.BRAILLE_MATERIALS)
            },
        )
    }

    fun reviews(now: Long = System.currentTimeMillis()): List<Review> = listOf(
        Review(
            id = "seed-review-1",
            establishmentId = "sabor-arte",
            establishmentName = "Restaurante Sabor & Arte",
            authorId = "seed-rafael",
            authorName = "Rafael Mendes",
            authorProfile = AccessibilityProfile.WHEELCHAIR,
            comment = "Porta de entrada verdadeiramente larga, rampa sem inclinação excessiva e espaço entre mesas impecável no Restaurante Sabor & Arte. Não precisei pedir ajuda em nenhum momento!",
            checklist = mapOf(
                ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.ACCESSIBLE_RESTROOM to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.LIBRAS_SERVICE to FeatureStatus.UNKNOWN,
                ReviewChecklistItem.INTERNAL_CIRCULATION to FeatureStatus.AVAILABLE,
            ),
            createdAt = now - 2 * DAY,
            helpfulCount = 14,
        ),
        Review(
            id = "seed-review-2",
            establishmentId = "livraria-harmonia",
            establishmentName = "Livraria & Café Harmonia",
            authorId = "seed-camila",
            authorName = "Camila Silveira",
            authorProfile = AccessibilityProfile.VISUAL,
            comment = "A Livraria Harmonia dá uma aula: o QR code com audiodescrição funcionou rápido e os atendentes conheciam as rotas acessíveis do local sem constrangimentos. Recomendo demais!",
            checklist = mapOf(
                ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.ACCESSIBLE_RESTROOM to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.LIBRAS_SERVICE to FeatureStatus.UNKNOWN,
                ReviewChecklistItem.INTERNAL_CIRCULATION to FeatureStatus.PARTIAL,
            ),
            createdAt = now - 1 * DAY,
            helpfulCount = 28,
        ),
        Review(
            id = "seed-review-3",
            establishmentId = "espaco-artes",
            establishmentName = "Espaço das Artes Paulista",
            authorId = "seed-joao",
            authorName = "João Pereira",
            authorProfile = AccessibilityProfile.DEAF,
            comment = "Fui recebido por uma mediadora fluente em Libras logo na entrada. Os vídeos da exposição têm janela de Libras e legenda. Experiência completa!",
            checklist = mapOf(
                ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.ACCESSIBLE_RESTROOM to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.LIBRAS_SERVICE to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.INTERNAL_CIRCULATION to FeatureStatus.AVAILABLE,
            ),
            createdAt = now - 4 * DAY,
            helpfulCount = 9,
        ),
        Review(
            id = "seed-review-4",
            establishmentId = "terraco-aromas",
            establishmentName = "Terraço Aromas & Sentidos",
            authorId = "seed-beatriz",
            authorName = "Beatriz Lima",
            authorProfile = AccessibilityProfile.REDUCED_MOBILITY,
            comment = "A rampa externa é boa, mas no horário de almoço o espaço entre as mesas fica bem apertado. Vale ir em horários mais tranquilos.",
            checklist = mapOf(
                ReviewChecklistItem.ENTRANCE_RAMP to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.ACCESSIBLE_RESTROOM to FeatureStatus.AVAILABLE,
                ReviewChecklistItem.LIBRAS_SERVICE to FeatureStatus.PARTIAL,
                ReviewChecklistItem.INTERNAL_CIRCULATION to FeatureStatus.PARTIAL,
            ),
            createdAt = now - 6 * DAY,
            helpfulCount = 5,
        ),
    )
}
