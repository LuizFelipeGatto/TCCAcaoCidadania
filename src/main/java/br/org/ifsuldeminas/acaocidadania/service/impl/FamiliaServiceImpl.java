package br.org.ifsuldeminas.acaocidadania.service.impl;

import br.org.ifsuldeminas.acaocidadania.domain.Familia;
import br.org.ifsuldeminas.acaocidadania.repository.FamiliaRepository;
import br.org.ifsuldeminas.acaocidadania.service.FamiliaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Familia}.
 */
@Service
@Transactional
public class FamiliaServiceImpl implements FamiliaService {

    private final Logger log = LoggerFactory.getLogger(FamiliaServiceImpl.class);

    private final FamiliaRepository familiaRepository;

    public FamiliaServiceImpl(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    @Override
    public Familia save(Familia familia) {
        log.debug("Request to save Familia : {}", familia);
        return familiaRepository.save(familia);
    }

    @Override
    public Familia update(Familia familia) {
        log.debug("Request to update Familia : {}", familia);
        return familiaRepository.save(familia);
    }

    @Override
    public Optional<Familia> partialUpdate(Familia familia) {
        log.debug("Request to partially update Familia : {}", familia);

        return familiaRepository
            .findById(familia.getId())
            .map(existingFamilia -> {
                if (familia.getNome() != null) {
                    existingFamilia.setNome(familia.getNome());
                }
                if (familia.getAtiva() != null) {
                    existingFamilia.setAtiva(familia.getAtiva());
                }
                if (familia.getRenda() != null) {
                    existingFamilia.setRenda(familia.getRenda());
                }

                return existingFamilia;
            })
            .map(familiaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Familia> findAll(Pageable pageable) {
        log.debug("Request to get all Familias");
        return familiaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Familia> findOne(Long id) {
        log.debug("Request to get Familia : {}", id);
        return familiaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Familia : {}", id);
        familiaRepository.deleteById(id);
    }
}
