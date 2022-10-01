package br.org.ifsuldeminas.acaocidadania.service.impl;

import br.org.ifsuldeminas.acaocidadania.domain.Doacao;
import br.org.ifsuldeminas.acaocidadania.repository.DoacaoRepository;
import br.org.ifsuldeminas.acaocidadania.service.DoacaoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Doacao}.
 */
@Service
@Transactional
public class DoacaoServiceImpl implements DoacaoService {

    private final Logger log = LoggerFactory.getLogger(DoacaoServiceImpl.class);

    private final DoacaoRepository doacaoRepository;

    public DoacaoServiceImpl(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    @Override
    public Doacao save(Doacao doacao) {
        log.debug("Request to save Doacao : {}", doacao);
        return doacaoRepository.save(doacao);
    }

    @Override
    public Doacao update(Doacao doacao) {
        log.debug("Request to update Doacao : {}", doacao);
        return doacaoRepository.save(doacao);
    }

    @Override
    public Optional<Doacao> partialUpdate(Doacao doacao) {
        log.debug("Request to partially update Doacao : {}", doacao);

        return doacaoRepository
            .findById(doacao.getId())
            .map(existingDoacao -> {
                if (doacao.getData() != null) {
                    existingDoacao.setData(doacao.getData());
                }

                return existingDoacao;
            })
            .map(doacaoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Doacao> findAll(Pageable pageable) {
        log.debug("Request to get all Doacaos");
        return doacaoRepository.findAll(pageable);
    }

    public Page<Doacao> findAllWithEagerRelationships(Pageable pageable) {
        return doacaoRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Doacao> findOne(Long id) {
        log.debug("Request to get Doacao : {}", id);
        return doacaoRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Doacao : {}", id);
        doacaoRepository.deleteById(id);
    }
}
