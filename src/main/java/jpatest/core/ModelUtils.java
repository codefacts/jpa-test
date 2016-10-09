package jpatest.core;

import com.google.common.collect.ImmutableMap;
import jpatest.core.jpa.models.*;
import jpatest.core.jpa.models.core.JavaType;
import jpatest.models.Br;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.helper.DatabaseTable;
import org.eclipse.persistence.internal.jpa.metamodel.ListAttributeImpl;
import org.eclipse.persistence.internal.jpa.metamodel.SingularAttributeImpl;
import org.eclipse.persistence.mappings.CollectionMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.RelationTableMechanism;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Map;

/**
 * Created by Jango on 10/9/2016.
 */
public class ModelUtils {
    private static final String PU = "jpatest";

    public static ModelInfo toModelInfo(EntityType<?> entity) {

        ImmutableMap.Builder<String, PropInfo> propInfoMapBuilder = ImmutableMap.builder();

        entity.getAttributes().forEach(attr -> {

            if (!attr.isAssociation() && !attr.isCollection()) {

                propInfoMapBuilder.put(attr.getName(), singularProp((SingularAttributeImpl) attr));

            } else if (attr.isAssociation() && !attr.isCollection()) {

//                propInfoMapBuilder.put(attr.getName(), assProp(attr));
                System.out.println("kola");

            } else if (attr.isAssociation() && attr.isCollection()) {

                System.out.println("kola");

                propInfoMapBuilder.put(attr.getName(), pluralProp((ListAttributeImpl) attr));

            }
        });

        return new ModelInfoBuilder()
            .setName(entity.getName())
            .setPropInfoMap(propInfoMapBuilder.build())
            .createModelInfo();
    }

    private static PropInfo assProp(Attribute<?, ?> attr) {
        return null;
    }

    private static PropInfo pluralProp(ListAttributeImpl sngAttr) {

        ManyToManyMapping mapping = (ManyToManyMapping) sngAttr.getCollectionMapping();

        RelationTableMechanism mechanism = mapping.getRelationTableMechanism();

        DatabaseField srcRelationColumn = mechanism.getSourceRelationKeyFields().get(0);

        DatabaseField targetRelationColumn = mechanism.getTargetRelationKeyFields().get(0);

        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(sngAttr.getMapping().getField().getName())
            .setRelationInfo(
                new RelationInfoBuilder()
                    .setChildModelInfo(
                        new ChildModelInfoBuilder()
                            .setChildModel("")
                            .setJoinField("")
                            .createJoinTableInfo()
                    )
                    .setRelationTable(
                        new RelationTableBuilder()
                            .setTableName(mapping.getRelationTableName())
                            .setLeftColumn(srcRelationColumn.getName())
                            .setLeftColumnType(javaType(srcRelationColumn.getType()))
                            .setRightColumn(targetRelationColumn.getName())
                            .setRightColumnType(javaType(targetRelationColumn.getType()))
                            .createRelationTable()
                    )
                    .setRelationType(relationType(sngAttr.getPersistentAttributeType()))
                    .createRelationInfo()
            )
            .createPropInfo();
    }

    private static JavaType javaType(Class type) {
        return JavaType.of(type);
    }

    private static RelationType relationType(Attribute.PersistentAttributeType persistentAttributeType) {
        switch (persistentAttributeType) {
            case ONE_TO_ONE:
                return RelationType.ONE_TO_ONE;
            case ONE_TO_MANY:
                return RelationType.ONE_TO_MANY;
            case MANY_TO_ONE:
                return RelationType.MANY_TO_ONE;
            case MANY_TO_MANY:
                return RelationType.MANY_TO_MANY;
        }
        return null;
    }

    private static PropInfo singularProp(SingularAttributeImpl sngAttr) {

        DatabaseMapping mapping = sngAttr.getMapping();

        String column = mapping.getField().getName();

        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(column)
            .setRelationInfo(null)
            .createPropInfo();
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU);

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            toModelInfo(em.getMetamodel().entity(Br.class));

        } finally {

            em.close();

            emf.close();
        }
    }
}
