package se.magnus.microservices.core.recommendation.services;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import se.magnus.microservices.api.core.recommendation.Recommendation;
import se.magnus.microservices.core.recommendation.persistence.RecommendationEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-13T10:24:44+0100",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.1.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class RecommendationMapperImpl implements RecommendationMapper {

    @Override
    public Recommendation entityToApi(RecommendationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Recommendation recommendation = new Recommendation();

        recommendation.setRate( entity.getRating() );
        recommendation.setProductId( entity.getProductId() );
        recommendation.setRecommendationId( entity.getRecommendationId() );
        recommendation.setAuthor( entity.getAuthor() );
        recommendation.setContent( entity.getContent() );

        return recommendation;
    }

    @Override
    public RecommendationEntity apiToEntity(Recommendation api) {
        if ( api == null ) {
            return null;
        }

        RecommendationEntity recommendationEntity = new RecommendationEntity();

        recommendationEntity.setRating( api.getRate() );
        recommendationEntity.setProductId( api.getProductId() );
        recommendationEntity.setRecommendationId( api.getRecommendationId() );
        recommendationEntity.setAuthor( api.getAuthor() );
        recommendationEntity.setContent( api.getContent() );

        return recommendationEntity;
    }

    @Override
    public List<Recommendation> entityListToApiList(List<RecommendationEntity> entity) {
        if ( entity == null ) {
            return null;
        }

        List<Recommendation> list = new ArrayList<Recommendation>( entity.size() );
        for ( RecommendationEntity recommendationEntity : entity ) {
            list.add( entityToApi( recommendationEntity ) );
        }

        return list;
    }

    @Override
    public List<RecommendationEntity> apiListToEntityList(List<Recommendation> api) {
        if ( api == null ) {
            return null;
        }

        List<RecommendationEntity> list = new ArrayList<RecommendationEntity>( api.size() );
        for ( Recommendation recommendation : api ) {
            list.add( apiToEntity( recommendation ) );
        }

        return list;
    }
}
