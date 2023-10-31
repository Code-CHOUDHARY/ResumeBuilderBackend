package com.resumebuilder.downloadhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DownloadHistoryRepository extends JpaRepository<DownloadHistory, Long> {

}
