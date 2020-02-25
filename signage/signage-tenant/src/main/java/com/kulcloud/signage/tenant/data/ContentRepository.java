package com.kulcloud.signage.tenant.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.kulcloud.signage.tenant.data.entity.Content;

public interface ContentRepository extends JpaRepository<Content, String> {

	List<Content> findByContentIdIn(List<String> contentIds);
	
	@Transactional
    @Modifying
    @Query(value="delete from tb_content where content_id not in :ids and type <> 'link'", nativeQuery=true)
    void deleteAllByIdNotInQuery(@Param("ids") List<String> contentIds);
	
	@Transactional
    @Modifying
    @Query(value="delete from tb_content where type <> 'link'", nativeQuery=true)
    void deleteAllNotLink();
}
