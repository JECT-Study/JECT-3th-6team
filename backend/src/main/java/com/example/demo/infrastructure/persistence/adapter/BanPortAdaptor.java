package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorType;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.ban.Ban;
import com.example.demo.domain.model.ban.BanQuery;
import com.example.demo.domain.model.ban.BanType;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.port.BanPort;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.infrastructure.persistence.entity.BanEntity;
import com.example.demo.infrastructure.persistence.mapper.BanEntityMapper;
import com.example.demo.infrastructure.persistence.repository.BanJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.infrastructure.persistence.entity.QBanEntity.banEntity;

@Repository
@RequiredArgsConstructor
public class BanPortAdaptor implements BanPort {

    private final BanJpaRepository banJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final MemberPort memberPort;
    private final PopupPort popupPort;

    @Override
    public Ban save(Ban ban) {
        BanEntity banEntity = BanEntityMapper.toEntity(ban);
        BanEntity savedEntity = banJpaRepository.save(banEntity);
        return ban.withId(savedEntity.getId());
    }

    @Override
    public List<Ban> findByQuery(BanQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        switch (query) {
            case BanQuery.ByMemberIdFromAll banQuery -> {
                builder.and(banEntity.memberId.eq(banQuery.getMemberId()));
            }
            case BanQuery.ByMemberAndPopup banQuery -> {
                builder.and(banEntity.memberId.eq(banQuery.getMemberId())
                        .and(banEntity.popupId.eq(banQuery.getPopupId())));
            }
            case BanQuery.ByBanType banQuery -> {
                if (banQuery.getType() == BanType.STORE) {
                    builder.and(banEntity.popupId.isNotNull());
                } else {
                    builder.and(banEntity.popupId.isNull());
                }
            }
            case null, default -> throw new BusinessException(ErrorType.FEATURE_NOT_IMPLEMENTED);
        }
        List<BanEntity> banEntities = jpaQueryFactory.selectFrom(banEntity)
                .where(builder)
                .fetch();

        return banEntities.stream()
                .map(it -> {
                    Member member = memberPort.findById(it.getMemberId()).orElseThrow();
                    Popup popup = Optional.ofNullable(it.getPopupId()).flatMap(popupPort::findById).orElse(null);
                    return BanEntityMapper.toDomain(it, member, popup);
                })
                .toList();
    }
}
