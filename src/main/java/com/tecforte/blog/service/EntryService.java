package com.tecforte.blog.service;

import com.tecforte.blog.domain.Entry;
import com.tecforte.blog.repository.BlogRepository;
import com.tecforte.blog.repository.EntryRepository;
import com.tecforte.blog.service.dto.EntryDTO;
import com.tecforte.blog.service.mapper.EntryMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Entry}.
 */
@Service
@Transactional
public class EntryService {

    private final Logger log = LoggerFactory.getLogger(EntryService.class);

    private final EntryRepository entryRepository;
    
    private final BlogRepository blogRepository;

    private final EntryMapper entryMapper;

    public EntryService(EntryRepository entryRepository, EntryMapper entryMapper, BlogRepository blogRepository) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
        this.blogRepository = blogRepository;
    }

    /**
     * Save a entry.
     *
     * @param entryDTO the entity to save.
     * @return the persisted entity.
     */
    public EntryDTO save(EntryDTO entryDTO) {
        log.debug("Request to save Entry : {}", entryDTO);
        Entry entry = entryMapper.toEntity(entryDTO);
        entry = entryRepository.save(entry);
        return entryMapper.toDto(entry);
    }

    /**
     * Get all the entries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entries");
        return entryRepository.findAll(pageable)
            .map(entryMapper::toDto);
    }


    /**
     * Get one entry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntryDTO> findOne(Long id) {
        log.debug("Request to get Entry : {}", id);
        return entryRepository.findById(id)
            .map(entryMapper::toDto);
    }

    /**
     * Delete the entry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Entry : {}", id);
        entryRepository.deleteById(id);
    }
    
    public void deleteBlogEntries(String keyword) {
        List<Entry> entries = entryRepository.findAll();
        
        for (int i = 0; i < entries.size(); i++) {
            Entry tempEntry = entries.get(i);
            
            String entryTitle = tempEntry.getTitle();
            String entryContent = tempEntry.getContent();
            
            if ((entryTitle.toLowerCase().contains(keyword.toLowerCase())) || (entryContent.toLowerCase().contains(keyword.toLowerCase()))) {
                entryRepository.deleteById(tempEntry.getId());
            }
        }
    }
    
    public void deleteBlogEntriesByBlog(Long id, String keyword) {
        List<Entry> entries = entryRepository.findByBlogId(id);
        
        for (int i = 0; i < entries.size(); i++) {
            Entry tempEntry = entries.get(i);
            
            String entryTitle = tempEntry.getTitle();
            String entryContent = tempEntry.getContent();
            
            if ((entryTitle.toLowerCase().contains(keyword.toLowerCase())) || (entryContent.toLowerCase().contains(keyword.toLowerCase()))) {
                entryRepository.deleteById(tempEntry.getId());
            }
        }
    }
}
