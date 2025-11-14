package com.linearizability.common.base;

import com.linearizability.common.exception.ValidationException;

/**
 * 验证器接口 定义数据验证规范，用于对象验证
 *
 * @param  <T> 待验证的对象类型
 * @author     ZhangBoyuan
 * @since      2025-11-07
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * 验证对象
     *
     * @param  target 待验证的对象
     * @return        验证结果，true表示验证通过，false表示验证失败
     */
    boolean validate(T target);

    /**
     * 验证对象并返回错误消息
     *
     * @param  target 待验证的对象
     * @return        验证结果，如果验证通过返回null，否则返回错误消息
     */
    default String validateWithMessage(T target) {
        return validate(target) ? null : "Validation failed";
    }

    /**
     * 验证对象，失败时抛出异常
     *
     * @param  target              待验证的对象
     * @throws ValidationException 验证失败时抛出
     */
    default void validateOrThrow(T target) {
        if (!validate(target)) {
            throw new ValidationException(validateWithMessage(target));
        }
    }

    /**
     * 验证对象，失败时抛出异常（带自定义消息）
     *
     * @param  target              待验证的对象
     * @param  message             验证失败时的错误消息
     * @throws ValidationException 验证失败时抛出
     */
    default void validateOrThrow(T target, String message) {
        if (!validate(target)) {
            throw new ValidationException(message);
        }
    }

    /**
     * 组合验证器（AND逻辑） 所有验证器都通过才算通过
     *
     * @param  other 另一个验证器
     * @return       组合后的验证器
     */
    default Validator<T> and(Validator<? super T> other) {
        return target -> validate(target) && other.validate(target);
    }

    /**
     * 组合验证器（OR逻辑） 任意一个验证器通过就算通过
     *
     * @param  other 另一个验证器
     * @return       组合后的验证器
     */
    default Validator<T> or(Validator<? super T> other) {
        return target -> validate(target) || other.validate(target);
    }

    /**
     * 取反验证器
     *
     * @return 取反后的验证器
     */
    default Validator<T> negate() {
        return target -> !validate(target);
    }
}
