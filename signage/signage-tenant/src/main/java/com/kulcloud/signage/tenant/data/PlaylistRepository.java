package com.kulcloud.signage.tenant.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

	List<Playlist> findByPlaylistIdIn(List<Long> playListIds);
}
