/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import jp.co.sega.allnet.auth.exception.ApplicationException;
import jp.co.sega.allnet.auth.service.AbstractService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TsuboiY
 * 
 */
public abstract class AbstractRegisterService extends AbstractService {

    private static final Logger _log = LoggerFactory
            .getLogger(AbstractRegisterService.class);

    /**
     * CSVで受け取った登録情報をチェックする。
     * 
     * @param param
     * @return
     */
    protected <T> boolean checkParameter(T param) {
        Set<ConstraintViolation<T>> set = validateParameter(param);
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> cv : set) {
                if (sb.length() > 0) {
                    sb.append(", ");
                } else {
                    sb.append("[");
                }
                sb.append(cv.getPropertyPath());
                sb.append(" ");
                sb.append(cv.getMessage());
            }
            sb.append("]");
            _log.warn(getLogUtils().format("Validation failed:%s",
                    sb.toString()));
            return false;
        }
        return true;
    }

    /**
     * CSVで受け取った登録情報をチェックする。
     * 
     * @param param
     * @return
     */
    protected <T> boolean checkParameter(T param,
            Map<String, InvalidParameterProcessor> processorMap,
            boolean checkAll) {
        Set<ConstraintViolation<T>> set = validateParameter(param);
        if (set.size() > 0) {
            StringBuilder sb = new StringBuilder();
            OUTER: for (ConstraintViolation<T> cv : set) {
                Path path = cv.getPropertyPath();
                if (sb.length() > 0) {
                    sb.append(", ");
                } else {
                    sb.append("[");
                }
                sb.append(cv.getPropertyPath());
                sb.append(" ");
                sb.append(cv.getMessage());
                Iterator<Node> ite = path.iterator();
                while (ite.hasNext()) {
                    Node node = ite.next();
                    InvalidParameterProcessor invalidParam = processorMap
                            .get(node.getName());
                    if (invalidParam == null) {
                        throw new ApplicationException("処理されないパラメータが指定されました。");
                    }
                    invalidParam.process();

                    if (!checkAll) {
                        break OUTER;
                    }
                }
            }
            sb.append("]");
            _log.warn(getLogUtils().format("Validation failed:%s",
                    sb.toString()));
            return false;
        }
        return true;
    }

    /**
     * CSVで受け取った登録情報の有効性を確認する。
     * 
     * @param param
     * @return
     */
    private <T> Set<ConstraintViolation<T>> validateParameter(T param) {
        ValidatorFactory validatorFactory = Validation
                .buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator.validate(param);
    }

    /**
     * CSVで受け取った登録情報をログ出力用にフォーマットする。
     * 
     * @param val
     * @return
     */
    protected String formatCsvValueLog(String val) {
        return getLogUtils().format("Received csv:[%s]", val);
    }

    /**
     * CSVで受け取った登録情報をログ出力用にフォーマットする。
     * 
     * @param status
     * @return
     */
    protected String formatCsvStatusLog(int status) {
        return getLogUtils().format("Status:%s", status);
    }
}
