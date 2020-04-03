package com.hooro.ri.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**  用于自动填充到datatable
 * @author admin
 *
 */
@Target({ElementType.FIELD,ElementType.TYPE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldHeader {
	/** 显示顺序 */
    int order() ;
    
	/** 显示列标识 */
    String fieldName() ;
}
