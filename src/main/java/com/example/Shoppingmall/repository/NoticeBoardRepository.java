package com.example.Shoppingmall.repository;

import com.example.Shoppingmall.entity.NoticeBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NoticeBoardRepository extends PagingAndSortingRepository<NoticeBoard, Integer>, CrudRepository<NoticeBoard, Integer> {
    Optional<NoticeBoard> findFirstByIdLessThanOrderByIdDesc(Integer id);
    Optional<NoticeBoard> findFirstByIdGreaterThanOrderByIdAsc(Integer id);
}
