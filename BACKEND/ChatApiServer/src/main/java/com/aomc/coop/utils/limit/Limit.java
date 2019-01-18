package com.aomc.coop.utils.limit;

import java.lang.annotation.*;

//메소드에 적용
@Target(ElementType.METHOD)
//런타임시까지 참조 가능
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {
}
