package org.ksug.springcamp.testmvc.core.domain.support.conversion;

import com.google.common.collect.ImmutableSet;
import org.ksug.springcamp.testmvc.core.common.lang.CodeEncodableEnum;
import org.ksug.springcamp.testmvc.core.domain.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;

public class CodeEncodableEnumConverter implements GenericConverter{

    private static final Logger log = LoggerFactory.getLogger(CodeEncodableEnumConverter.class);

    private final Set<ConvertiblePair> convertiblePairs;

    public CodeEncodableEnumConverter() {
        this.convertiblePairs = ImmutableSet.<ConvertiblePair> builder()
                .add(new ConvertiblePair(String.class, Sex.class))
                .build();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return convertiblePairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String code = (String) source;

        log.debug("find code:{} in {}.", new Object[]{code, targetType.getType().getName()});

        Class<? extends CodeEncodableEnum> targetClass = (Class<? extends CodeEncodableEnum>) targetType.getType();
        for(CodeEncodableEnum codeEncodableEnum : targetClass.getEnumConstants())
            if(codeEncodableEnum.getCode().equals(code)) return codeEncodableEnum;

        throw new IllegalArgumentException("Unknown code '" + code + "' for enum type " + targetType.getType().getName());
    }
}
