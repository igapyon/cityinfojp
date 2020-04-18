/*
 * Copyright 2020 Toshiki Iga
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.cityinfojp.thvarmap;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.springframework.ui.Model;
import org.thymeleaf.context.IContext;

/**
 * Thymeleaf のキー値マッピングを抽象化することにより、オンライン処理およびバッチ処理のいずれでも利用できることを実現するための抽象クラス。
 *
 * @author Toshiki Iga
 */
public abstract class AbstractThVarMapBuilder {
    protected abstract LinkedHashMap<String, Object> buildVarMap() throws IOException;

    /**
     * 与えられた map 情報を Context に設定します。これは Thymeleaf バッチ処理で利用します。
     * 
     * @param ctx Target Context. must be instance of Context.
     */
    public void applyContextVariable(IContext ctx) throws IOException {
        LinkedHashMap<String, Object> map = buildVarMap();
        ThVarMapUtil.applyContextVariable(ctx, map);
    }

    /**
     * 与えられた map 情報を Context に設定します。これは Thymeleaf オンライン処理で利用します。
     * 
     * @param model Target model.
     */
    public void applyModelAttr(Model model) throws IOException {
        LinkedHashMap<String, Object> map = buildVarMap();
        ThVarMapUtil.applyModelAttr(model, map);
    }
}
