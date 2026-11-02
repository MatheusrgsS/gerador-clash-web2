package web2_matheus.gerador_clash.core.validation.util;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;

/**
 * Utilitário de reflexão para extração de valores de campos em objetos Java,
 * com suporte a classes tradicionais (via campos) e Records (via componentes).
 */
public class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * Extrai o valor de um campo de um objeto usando reflexão.
     * Suporta tanto classes tradicionais quanto Records do Java 14+.
     *
     * @param object    o objeto do qual se deseja extrair o valor
     * @param fieldName o nome do campo a ser lido
     * @return o valor do campo, ou {@code null} se não encontrado ou acessível
     */
    public static Object extrairValor(Object object, String fieldName) {
        if (object == null || fieldName == null || fieldName.isBlank()) {
            return null;
        }

        Class<?> clazz = object.getClass();

        // Suporte a Records (Java 14+)
        if (clazz.isRecord()) {
            for (RecordComponent component : clazz.getRecordComponents()) {
                if (component.getName().equals(fieldName)) {
                    try {
                        component.getAccessor().setAccessible(true);
                        return component.getAccessor().invoke(object);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }

        // Suporte a classes tradicionais (busca na hierarquia)
        Class<?> searchClass = clazz;
        while (searchClass != null && searchClass != Object.class) {
            try {
                Field field = searchClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                searchClass = searchClass.getSuperclass();
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
}
