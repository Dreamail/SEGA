/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.adminregister;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.opencsv.CSVWriter;

/**
 * @author TsuboiY
 * 
 */
public abstract class AbstractRegisterParameter {

    private final String[] inputCsv;

    /**
     * コンストラクタ
     * 
     * @param csv
     * @param defaultColumnLength
     */
    public AbstractRegisterParameter(String[] csv, int defaultColumnLength) {
        // 規定の長さの配列を作成する
        inputCsv = new String[defaultColumnLength];
        // 渡された配列をコピーする
        int length = csv.length;
        if (length > defaultColumnLength) {
            length = defaultColumnLength;
        }
        System.arraycopy(csv, 0, inputCsv, 0, length);
        // 配列の要素にトリムをかける
        for (int i = 0; i < inputCsv.length; i++) {
            if (inputCsv[i] != null) {
                inputCsv[i] = inputCsv[i].trim();
            }
        }
        // メンバーにアサインする
        assign(inputCsv);
    }

    /**
     * CSV配列をメンバーにアサインする
     * 
     * @param csv
     */
    protected abstract void assign(String[] inputCsv);

    /**
     * 1行CSVを文字列で返却する
     * 
     * @return
     */
    public String getInputCsvStr() {
		Writer writer = new StringWriter();
		try (CSVWriter w = new CSVWriter(writer, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END)) {
			w.writeNext(getInputCsv());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer.toString();
    }

    /**
     * @return the inputCsv
     */
    public String[] getInputCsv() {
        return inputCsv;
    }

}
