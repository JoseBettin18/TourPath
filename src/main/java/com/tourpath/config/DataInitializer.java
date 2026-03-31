package com.tourpath.config;

import com.tourpath.model.City;
import com.tourpath.model.Place;
import com.tourpath.repository.CityRepository;
import com.tourpath.repository.PlaceRepository;
import com.tourpath.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private CityRepository cityRepository;
    @Autowired private PlaceRepository placeRepository;
    @Autowired private UserService userService;

    @Override
    public void run(String... args) {
        userService.createSuperAdminIfNotExists();
        if (cityRepository.count() == 0) loadCities();
        if (placeRepository.count() == 0) loadPlaces();
    }

    private void loadCities() {
        List<City> cities = List.of(
            City.builder().name("Cartagena").country("Colombia").countryCode("CO")
                .continent("América del Sur").flagEmoji("🇨🇴")
                .description("Ciudad amurallada Patrimonio de la Humanidad, con playas caribeñas y arquitectura colonial.")
                .mainImage("https://images.unsplash.com/photo-1583001931096-959e9a1a6223?w=800")
                .active(true).featured(true).latitude(10.4236).longitude(-75.5512)
                .tags(List.of("caribe","colonial","playas","historia")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("París").country("Francia").countryCode("FR")
                .continent("Europa").flagEmoji("🇫🇷")
                .description("La Ciudad de la Luz, capital del arte, la gastronomía y la moda mundial.")
                .mainImage("https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800")
                .active(true).featured(true).latitude(48.8566).longitude(2.3522)
                .tags(List.of("arte","gastronomia","moda","romantico")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Tokio").country("Japón").countryCode("JP")
                .continent("Asia").flagEmoji("🇯🇵")
                .description("Metrópolis que fusiona tradición milenaria con tecnología de vanguardia.")
                .mainImage("https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800")
                .active(true).featured(true).latitude(35.6762).longitude(139.6503)
                .tags(List.of("tecnologia","cultura","gastronomia","templos")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Nueva York").country("Estados Unidos").countryCode("US")
                .continent("América del Norte").flagEmoji("🇺🇸")
                .description("La Gran Manzana, capital cultural y financiera del mundo.")
                .mainImage("https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800")
                .active(true).featured(true).latitude(40.7128).longitude(-74.0060)
                .tags(List.of("manhattan","museos","broadway","rascacielos")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Barcelona").country("España").countryCode("ES")
                .continent("Europa").flagEmoji("🇪🇸")
                .description("Ciudad mediterránea de Gaudí, tapas y vida nocturna inigualable.")
                .mainImage("https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=800")
                .active(true).featured(true).latitude(41.3851).longitude(2.1734)
                .tags(List.of("gaudi","playa","tapas","modernismo")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Ciudad de México").country("México").countryCode("MX")
                .continent("América del Norte").flagEmoji("🇲🇽")
                .description("Megalópolis llena de historia azteca, gastronomía y arte muralista.")
                .mainImage("https://images.unsplash.com/photo-1518105779142-d975f22f1b0a?w=800")
                .active(true).featured(false).latitude(19.4326).longitude(-99.1332)
                .tags(List.of("historia","gastronomia","arte","azteca")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Buenos Aires").country("Argentina").countryCode("AR")
                .continent("América del Sur").flagEmoji("🇦🇷")
                .description("La París de Sudamérica: tango, asado, cultura y arquitectura europea.")
                .mainImage("https://images.unsplash.com/photo-1589909202802-8f4aadce1849?w=800")
                .active(true).featured(false).latitude(-34.6037).longitude(-58.3816)
                .tags(List.of("tango","gastronomia","teatro","cultura")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Dubái").country("Emiratos Árabes Unidos").countryCode("AE")
                .continent("Asia").flagEmoji("🇦🇪")
                .description("Ciudad del futuro: rascacielos récord, lujo extremo y desierto dorado.")
                .mainImage("https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=800")
                .active(true).featured(true).latitude(25.2048).longitude(55.2708)
                .tags(List.of("lujo","rascacielos","desierto","compras")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Roma").country("Italia").countryCode("IT")
                .continent("Europa").flagEmoji("🇮🇹")
                .description("La Ciudad Eterna: 2800 años de historia, arte y la mejor pasta del mundo.")
                .mainImage("https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=800")
                .active(true).featured(false).latitude(41.9028).longitude(12.4964)
                .tags(List.of("historia","coliseo","vaticano","gastronomia")).createdAt(LocalDateTime.now()).build(),

            City.builder().name("Bogotá").country("Colombia").countryCode("CO")
                .continent("América del Sur").flagEmoji("🇨🇴")
                .description("Capital colombiana a 2600 metros: museos, grafiti y el mejor café del mundo.")
                .mainImage("https://images.unsplash.com/photo-1622543925917-763c34d1a86e?w=800")
                .active(true).featured(false).latitude(4.7110).longitude(-74.0721)
                .tags(List.of("cafe","museos","arte","montaña")).createdAt(LocalDateTime.now()).build()
        );
        cityRepository.saveAll(new ArrayList<>(cities));
        System.out.println("✅ " + cities.size() + " ciudades cargadas.");
    }

    private void loadPlaces() {
        // Obtener IDs de ciudades para vincular los lugares
        cityRepository.findByNameIgnoreCase("Cartagena").ifPresent(cartagena -> {
            List<Place> places = List.of(
                Place.builder().name("Ciudad Amurallada")
                    .description("Patrimonio UNESCO. Murallas coloniales del siglo XVI, calles empedradas y arquitectura barroca que te transportan al pasado.")
                    .category(Place.Category.SITIO_TURISTICO)
                    .cityId(cartagena.getId()).cityName("Cartagena").countryName("Colombia")
                    .address("Centro Histórico, Cartagena").latitude(10.4236).longitude(-75.5512)
                    .mainImage("https://images.unsplash.com/photo-1583001931096-959e9a1a6223?w=800")
                    .averageRating(4.9).totalReviews(1240).featured(true).active(true)
                    .priceRange("$").openingHours("24 horas")
                    .bookingUrl("https://www.cartagena.gov.co/turismo").bookingLabel("Ver tours")
                    .tags(List.of("historia","UNESCO","colonial")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),

                Place.builder().name("La Cevichería")
                    .description("El restaurante de mariscos más famoso de Cartagena. Ceviches creativos con los mejores mariscos del Caribe colombiano.")
                    .category(Place.Category.RESTAURANTE)
                    .cityId(cartagena.getId()).cityName("Cartagena").countryName("Colombia")
                    .address("Calle Stuart #7-14, Centro").phone("+57 5 660 1492")
                    .latitude(10.4257).longitude(-75.5498)
                    .mainImage("https://images.unsplash.com/photo-1565299507177-b0ac66763828?w=800")
                    .averageRating(4.7).totalReviews(632).featured(true).active(true)
                    .priceRange("$$$").openingHours("12:00 PM - 10:00 PM")
                    .bookingUrl("https://www.opentable.com").bookingLabel("Reservar mesa")
                    .tags(List.of("mariscos","ceviche","gourmet")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),

                Place.builder().name("Café del Mar")
                    .description("Bar icónico sobre las murallas coloniales. El mejor lugar para ver el atardecer caribeño con un cóctel tropical.")
                    .category(Place.Category.BAR_DISCOTECA)
                    .cityId(cartagena.getId()).cityName("Cartagena").countryName("Colombia")
                    .address("Baluarte de Santo Domingo, Murallas").latitude(10.4248).longitude(-75.5541)
                    .mainImage("https://images.unsplash.com/photo-1572116469696-31de0f17cc34?w=800")
                    .averageRating(4.6).totalReviews(891).featured(true).active(true)
                    .priceRange("$$$").openingHours("4:00 PM - 2:00 AM")
                    .bookingUrl("https://www.cafedelmar.com").bookingLabel("Ver menú")
                    .tags(List.of("cocktails","atardecer","murallas")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
            );
            placeRepository.saveAll(new ArrayList<>(places));
        });

        cityRepository.findByNameIgnoreCase("París").ifPresent(paris -> {
            List<Place> places = List.of(
                Place.builder().name("Torre Eiffel")
                    .description("El monumento más visitado del mundo. Vistas panorámicas espectaculares de París desde 330 metros de altura.")
                    .category(Place.Category.SITIO_TURISTICO)
                    .cityId(paris.getId()).cityName("París").countryName("Francia")
                    .address("Champ de Mars, 5 Av. Anatole France").latitude(48.8584).longitude(2.2945)
                    .mainImage("https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800")
                    .averageRating(4.8).totalReviews(5420).featured(true).active(true)
                    .priceRange("$$").openingHours("9:00 AM - 12:00 AM")
                    .bookingUrl("https://www.toureiffel.paris/en").bookingLabel("Comprar tickets")
                    .tags(List.of("icónico","vistas","romántico")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build(),

                Place.builder().name("Museo del Louvre")
                    .description("El museo más grande del mundo con más de 35,000 obras incluyendo la Mona Lisa y la Venus de Milo.")
                    .category(Place.Category.MUSEO)
                    .cityId(paris.getId()).cityName("París").countryName("Francia")
                    .address("Rue de Rivoli, 75001 Paris").latitude(48.8606).longitude(2.3376)
                    .mainImage("https://images.unsplash.com/photo-1565099824688-bb8e3b29b29b?w=800")
                    .averageRating(4.9).totalReviews(8930).featured(true).active(true)
                    .priceRange("$$").openingHours("Lun-Sáb: 9:00 AM - 6:00 PM")
                    .bookingUrl("https://www.louvre.fr").bookingLabel("Reservar entrada")
                    .tags(List.of("arte","historia","monaLisa")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
            );
            placeRepository.saveAll(new ArrayList<>(places));
        });

        cityRepository.findByNameIgnoreCase("Tokio").ifPresent(tokio -> {
            List<Place> places = List.of(
                Place.builder().name("Templo Senso-ji")
                    .description("El templo budista más antiguo de Tokio en el barrio de Asakusa. Arquitectura tradicional japonesa del siglo VII.")
                    .category(Place.Category.SITIO_TURISTICO)
                    .cityId(tokio.getId()).cityName("Tokio").countryName("Japón")
                    .address("2 Chome-3-1 Asakusa, Taito City").latitude(35.7148).longitude(139.7967)
                    .mainImage("https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=800")
                    .averageRating(4.8).totalReviews(3210).featured(true).active(true)
                    .priceRange("$").openingHours("6:00 AM - 5:00 PM")
                    .bookingUrl("https://www.senso-ji.jp").bookingLabel("Ver información")
                    .tags(List.of("budismo","historia","cultura")).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()
            );
            placeRepository.saveAll(new ArrayList<>(places));
        });

        System.out.println("✅ Lugares de ejemplo cargados.");
    }
}
