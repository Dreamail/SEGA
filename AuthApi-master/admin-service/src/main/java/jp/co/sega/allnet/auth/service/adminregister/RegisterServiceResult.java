/**
 * 
 */
package jp.co.sega.allnet.auth.service.adminregister;

import java.io.Serializable;
import java.util.List;

/**
 * @author NakanoY
 * @param <T>
 * 
 */
public class RegisterServiceResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int failureCount;

    private List<T> list;

    public RegisterServiceResult(int failureCount, List<T> list) {
        this.failureCount = failureCount;
        this.list = list;
    }

    /**
     * @return failureCount
     */
    public int getFailureCount() {
        return failureCount;
    }

    /**
     * @param failureCount
     *            セットする failureCount
     */
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    /**
     * @return list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * @param list
     *            セットする list
     */
    public void setList(List<T> list) {
        this.list = list;
    }

}
