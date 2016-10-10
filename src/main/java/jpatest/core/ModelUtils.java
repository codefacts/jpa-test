package jpatest.core;

import com.google.common.collect.ImmutableMap;
import jpatest.core.jpa.models.*;
import jpatest.core.jpa.models.core.JavaType;
import jpatest.models.Br;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.helper.DatabaseTable;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;
import org.eclipse.persistence.internal.jpa.metamodel.ListAttributeImpl;
import org.eclipse.persistence.internal.jpa.metamodel.SingularAttributeImpl;
import org.eclipse.persistence.mappings.*;

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

        String primaryKey = null;

        for (Attribute attr : entity.getAttributes()) {

            if (!attr.isAssociation() && !attr.isCollection()) {

                SingularAttributeImpl sngAttr = (SingularAttributeImpl) attr;

                primaryKey = sngAttr.isId() ? sngAttr.getName() : primaryKey;

                propInfoMapBuilder.put(attr.getName(), singularProp(sngAttr));

            } else if (attr.isAssociation() && !attr.isCollection()) {

                propInfoMapBuilder.put(attr.getName(), assProp((SingularAttributeImpl) attr));
                System.out.println("kola");

            } else if (attr.isAssociation() && attr.isCollection()) {

                System.out.println("kola");

                propInfoMapBuilder.put(attr.getName(), pluralProp((ListAttributeImpl) attr));

            }
        }

        ClassDescriptor descriptor = ((EntityTypeImpl) entity).getDescriptor();

        return new ModelInfoBuilder()
            .setName(entity.getName())
            .setTable(descriptor.getTableName())
            .setPrimaryKey(primaryKey)
            .setPropInfoMap(propInfoMapBuilder.build())
            .createModelInfo();
    }

    private static PropInfo assProp(SingularAttributeImpl sngAttr) {
        if (sngAttr.getMapping() instanceof OneToOneMapping) {
            return oneToOne(sngAttr, (OneToOneMapping) sngAttr.getMapping());
        } else {
            return manyToOne(sngAttr, (ManyToOneMapping) sngAttr.getMapping());
        }
    }

    private static PropInfo manyToOne(SingularAttributeImpl sngAttr, ManyToOneMapping mapping) {
        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(mapping.getSourceToTargetKeyFields().entrySet().stream().findAny().get().getKey().getName())
            .setRelationInfo(
                new RelationInfoBuilder()
                    .setChildModelInfo(
                        new ChildModelInfoBuilder()
                            .setChildModel(mapping.getReferenceClassName())
                            .setJoinField(mapping.getSourceToTargetKeyFields().entrySet().stream().findAny().get().getValue().getName())
                            .createJoinTableInfo()
                    )
                    .setRelationTable(null)
                    .setRelationType(relationType(sngAttr.getPersistentAttributeType()))
                    .createRelationInfo()
            )
            .createPropInfo();
    }

    private static PropInfo oneToOne(SingularAttributeImpl sngAttr, OneToOneMapping mapping) {
        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(mapping.getSourceToTargetKeyFields().entrySet().stream().findAny().get().getKey().getName())
            .setRelationInfo(
                new RelationInfoBuilder()
                    .setChildModelInfo(
                        new ChildModelInfoBuilder()
                            .setChildModel(mapping.getReferenceClassName())
                            .setJoinField(mapping.getSourceToTargetKeyFields().entrySet().stream().findAny().get().getValue().getName())
                            .createJoinTableInfo()
                    )
                    .setRelationTable(null)
                    .setRelationType(relationType(sngAttr.getPersistentAttributeType()))
                    .createRelationInfo()
            )
            .createPropInfo();
    }

    private static PropInfo pluralProp(ListAttributeImpl sngAttr) {

        if (sngAttr.getCollectionMapping() instanceof OneToManyMapping) {

            return oneToMany(sngAttr, (OneToManyMapping) sngAttr.getCollectionMapping());
        } else {

            return manyToMany(sngAttr, (ManyToManyMapping) sngAttr.getCollectionMapping());
        }
    }

    private static PropInfo oneToMany(ListAttributeImpl sngAttr, OneToManyMapping mapping) {

        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(sngAttr.getMapping().getField() == null ? null : sngAttr.getMapping().getField().getName())
            .setRelationInfo(
                new RelationInfoBuilder()
                    .setChildModelInfo(
                        new ChildModelInfoBuilder()
                            .setChildModel(mapping.getReferenceClass().getSimpleName())
                            .setJoinField(mapping.getMappedBy())
                            .createJoinTableInfo()
                    )
                    .setRelationTable(null)
                    .setRelationType(relationType(sngAttr.getPersistentAttributeType()))
                    .createRelationInfo()
            )
            .createPropInfo();
    }

    private static PropInfo manyToMany(ListAttributeImpl sngAttr, ManyToManyMapping mapping) {
        RelationTableMechanism mechanism = mapping.getRelationTableMechanism();

        DatabaseField srcRelationColumn = mechanism.getSourceRelationKeyFields().get(0);

        DatabaseField targetRelationColumn = mechanism.getTargetRelationKeyFields().get(0);

        return new PropInfoBuilder()
            .setName(sngAttr.getName())
            .setColumn(sngAttr.getMapping().getField() == null ? null : sngAttr.getMapping().getField().getName())
            .setRelationInfo(
                new RelationInfoBuilder()
                    .setChildModelInfo(
                        new ChildModelInfoBuilder()
                            .setChildModel(mapping.getReferenceClass().getSimpleName())
                            .setJoinField(mapping.getMappedBy())
                            .createJoinTableInfo()
                    )
                    .setRelationTable(
                        new RelationTableBuilder()
                            .setTableName(mapping.getRelationTableName())
                            .setLeftColumn(srcRelationColumn.getName())
                            .setRightColumn(targetRelationColumn.getName())
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

            ModelInfo modelInfo = toModelInfo(em.getMetamodel().entity(Br.class));

            System.out.println(modelInfo);

        } finally {

            em.close();

            emf.close();
        }
    }
}
