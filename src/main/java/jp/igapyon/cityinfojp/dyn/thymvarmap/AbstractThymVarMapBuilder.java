package jp.igapyon.cityinfojp.dyn.thymvarmap;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.springframework.ui.Model;
import org.thymeleaf.context.IContext;

public abstract class AbstractThymVarMapBuilder {
    protected abstract LinkedHashMap<String, Object> buildVarMap() throws IOException;

    /**
     * 与えられた map 情報を Context に設定します。
     * 
     * @param ctx Target Context. must be instance of Context.
     */
    public void applyContextVariable(IContext ctx) throws IOException {
        LinkedHashMap<String, Object> map = buildVarMap();
        ThymVarMapUtil.applyContextVariable(ctx, map);
    }

    /**
     * 与えられた map 情報を Model に設定します。
     * 
     * @param model Target model.
     */
    public void applyModelAttr(Model model) throws IOException {
        LinkedHashMap<String, Object> map = buildVarMap();
        ThymVarMapUtil.applyModelAttr(model, map);
    }
}
