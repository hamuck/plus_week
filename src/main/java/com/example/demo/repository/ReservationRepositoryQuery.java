package com.example.demo.repository;

import static com.example.demo.entity.QItem.item;
import static com.example.demo.entity.QReservation.reservation;
import static com.example.demo.entity.QUser.user;

import com.example.demo.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	public ReservationRepositoryQuery(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	//QueryDSL 사용, userId와 itemId가 있거나 없을 시를 고려해 작성, left join 사용해 n+1 문제 방지
	public List<Reservation> searchReservation(Long userId, Long itemId){
		return jpaQueryFactory.selectFrom(reservation)
				.leftJoin(reservation.user, user).fetchJoin()
				.leftJoin(reservation.item, item).fetchJoin()
				.where(
					userId != null ? reservation.user.id.eq(userId) : null,
					itemId != null ? reservation.item.id.eq(itemId) : null
				).fetch();
		}
}
