package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Archive;
import com.mycompany.myapp.repository.ArchiveRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Archive}.
 */
@RestController
@RequestMapping("/api/archives")
@Transactional
public class ArchiveResource {

    private final Logger log = LoggerFactory.getLogger(ArchiveResource.class);

    private static final String ENTITY_NAME = "archive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArchiveRepository archiveRepository;

    public ArchiveResource(ArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    /**
     * {@code POST  /archives} : Create a new archive.
     *
     * @param archive the archive to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archive, or with status {@code 400 (Bad Request)} if the archive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Archive> createArchive(@RequestBody Archive archive) throws URISyntaxException {
        log.debug("REST request to save Archive : {}", archive);
        if (archive.getId() != null) {
            throw new BadRequestAlertException("A new archive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Archive result = archiveRepository.save(archive);
        return ResponseEntity
            .created(new URI("/api/archives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /archives/:id} : Updates an existing archive.
     *
     * @param id the id of the archive to save.
     * @param archive the archive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archive,
     * or with status {@code 400 (Bad Request)} if the archive is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Archive> updateArchive(@PathVariable(value = "id", required = false) final Long id, @RequestBody Archive archive)
        throws URISyntaxException {
        log.debug("REST request to update Archive : {}, {}", id, archive);
        if (archive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archive.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Archive result = archiveRepository.save(archive);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archive.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /archives/:id} : Partial updates given fields of an existing archive, field will ignore if it is null
     *
     * @param id the id of the archive to save.
     * @param archive the archive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archive,
     * or with status {@code 400 (Bad Request)} if the archive is not valid,
     * or with status {@code 404 (Not Found)} if the archive is not found,
     * or with status {@code 500 (Internal Server Error)} if the archive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Archive> partialUpdateArchive(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Archive archive
    ) throws URISyntaxException {
        log.debug("REST request to partial update Archive partially : {}, {}", id, archive);
        if (archive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archive.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Archive> result = archiveRepository
            .findById(archive.getId())
            .map(existingArchive -> {
                if (archive.getTimestamp() != null) {
                    existingArchive.setTimestamp(archive.getTimestamp());
                }
                if (archive.getProficiencyLvl() != null) {
                    existingArchive.setProficiencyLvl(archive.getProficiencyLvl());
                }

                return existingArchive;
            })
            .map(archiveRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archive.getId().toString())
        );
    }

    /**
     * {@code GET  /archives} : get all the archives.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archives in body.
     */
    @GetMapping("")
    public List<Archive> getAllArchives(@RequestParam(name = "filter", required = false) String filter) {
        if ("userprofile-is-null".equals(filter)) {
            log.debug("REST request to get all Archives where userProfile is null");
            return StreamSupport
                .stream(archiveRepository.findAll().spliterator(), false)
                .filter(archive -> archive.getUserProfile() == null)
                .toList();
        }
        log.debug("REST request to get all Archives");
        return archiveRepository.findAll();
    }

    /**
     * {@code GET  /archives/:id} : get the "id" archive.
     *
     * @param id the id of the archive to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archive, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Archive> getArchive(@PathVariable("id") Long id) {
        log.debug("REST request to get Archive : {}", id);
        Optional<Archive> archive = archiveRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(archive);
    }

    /**
     * {@code DELETE  /archives/:id} : delete the "id" archive.
     *
     * @param id the id of the archive to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArchive(@PathVariable("id") Long id) {
        log.debug("REST request to delete Archive : {}", id);
        archiveRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
