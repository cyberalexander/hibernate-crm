package by.leonovich.hibernatecrm.common.model.converter;

import by.leonovich.hibernatecrm.common.model.Category;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created : 08/01/2021 19:15
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String> {
    @Override
    public String convertToDatabaseColumn(final Category category) {
        return Objects.nonNull(category) ? category.getCategoryCode() : null;
    }

    @Override
    public Category convertToEntityAttribute(final String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        return Arrays.stream(Category.values())
            .filter(cat -> cat.getCategoryCode().equals(code))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
