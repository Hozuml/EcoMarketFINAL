package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Store;
import cl.duoc.caasoto.ecomarket.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    public List<Store> listarTiendas() {
        return storeRepository.findAll();
    }

    public Optional<Store> obtenerTienda(int id) {
        return storeRepository.findById(id);
    }

    public Store crearTienda(Store store) {
        return storeRepository.save(store);
    }

    public boolean eliminarTienda(int id) {
        if (storeRepository.existsById(id)){
            storeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Store actualizarTienda(int id, Store store) {
        return storeRepository.findById(id).map(tienda -> {
            tienda.setNombre(store.getNombre());
            tienda.setDireccion(store.getDireccion());
            tienda.setCiudad(store.getCiudad());
            tienda.setTelefono(store.getTelefono());
            return storeRepository.save(tienda);
        }).orElse(null);
    }
}
