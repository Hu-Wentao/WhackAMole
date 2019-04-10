package com.example.whackamole.base;

/**
 * 用于处理Fragment的返回事件
 * @Author: hu.wentao@outlook.com
 * @Date: 2019/4/8
 */
public interface BackHandledInterface {
    // 设置当前处于栈顶的 Fragment
    public abstract void setCurrentTopFragment(BaseFragment topFragment);
}
