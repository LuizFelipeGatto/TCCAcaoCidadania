package br.org.ifsuldeminas.acaocidadania.service.impl;

import br.org.ifsuldeminas.acaocidadania.domain.Pessoa;
import br.org.ifsuldeminas.acaocidadania.repository.PessoaRepository;
import br.org.ifsuldeminas.acaocidadania.service.PessoaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pessoa}.
 */
@Service
@Transactional
public class PessoaServiceImpl implements PessoaService {

    private final Logger log = LoggerFactory.getLogger(PessoaServiceImpl.class);

    private final PessoaRepository pessoaRepository;

    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        log.debug("Request to save Pessoa : {}", pessoa);
        return pessoaRepository.save(pessoa);
    }

    @Override
    public Pessoa update(Pessoa pessoa) {
        log.debug("Request to update Pessoa : {}", pessoa);
        return pessoaRepository.save(pessoa);
    }

    @Override
    public Optional<Pessoa> partialUpdate(Pessoa pessoa) {
        log.debug("Request to partially update Pessoa : {}", pessoa);

        return pessoaRepository
            .findById(pessoa.getId())
            .map(existingPessoa -> {
                if (pessoa.getNome() != null) {
                    existingPessoa.setNome(pessoa.getNome());
                }
                if (pessoa.getCpf() != null) {
                    existingPessoa.setCpf(pessoa.getCpf());
                }
                if (pessoa.getCidade() != null) {
                    existingPessoa.setCidade(pessoa.getCidade());
                }
                if (pessoa.getLogradouro() != null) {
                    existingPessoa.setLogradouro(pessoa.getLogradouro());
                }
                if (pessoa.getCodigoIbge() != null) {
                    existingPessoa.setCodigoIbge(pessoa.getCodigoIbge());
                }
                if (pessoa.getCelular() != null) {
                    existingPessoa.setCelular(pessoa.getCelular());
                }
                if (pessoa.getEmail() != null) {
                    existingPessoa.setEmail(pessoa.getEmail());
                }
                if (pessoa.getBeneficio() != null) {
                    existingPessoa.setBeneficio(pessoa.getBeneficio());
                }

                return existingPessoa;
            })
            .map(pessoaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pessoa> findAll(Pageable pageable) {
        log.debug("Request to get all Pessoas");
        return pessoaRepository.findAll(pageable);
    }

    public Page<Pessoa> findAllWithEagerRelationships(Pageable pageable) {
        return pessoaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pessoa> findOne(Long id) {
        log.debug("Request to get Pessoa : {}", id);
        return pessoaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pessoa : {}", id);
        pessoaRepository.deleteById(id);
    }
}
