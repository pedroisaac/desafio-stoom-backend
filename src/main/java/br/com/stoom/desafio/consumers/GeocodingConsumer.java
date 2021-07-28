package br.com.stoom.desafio.consumers;

import br.com.stoom.desafio.model.Address;
import br.com.stoom.desafio.service.AddressService;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class GeocodingConsumer {

    @Autowired
    AddressService addressService;

    @Value("${api.key.google.geocoding}")
    String apiKeyGeocoding;

    @JmsListener(destination = "buscar-geocoding-google", containerFactory = "myFactory")
    public void receiveMessage(Address address) throws Exception {
        System.out.println("Recebido pedido consulta de geocoding: " + address.toString());

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKeyGeocoding)
                .build();

        String addressToFind = address.toStringForGoogleGeocoding();

        GeocodingResult[] results = GeocodingApi.geocode(context, addressToFind).await();

        address.setLatitude(results[0].geometry.location.lat);
        address.setLongitude(results[0].geometry.location.lng);

        addressService.update(address);
    }
}
