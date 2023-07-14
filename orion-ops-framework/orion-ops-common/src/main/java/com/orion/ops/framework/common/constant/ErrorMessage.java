package com.orion.ops.framework.common.constant;

/**
 * 错误信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 18:51
 */
public interface ErrorMessage {

    String PARAM_MISSING = "{} 不能为空";

    String ID_MISSING = "id 不能为空";

    String USERNAME_PASSWORD_ERROR = "用户名或密码错误";

    String DATA_PRESENT = "数据已存在";

    String DATA_ABSENT = "数据不存在";

}
