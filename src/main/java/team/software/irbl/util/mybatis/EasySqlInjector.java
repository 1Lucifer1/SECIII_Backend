package team.software.irbl.util.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

public class EasySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList =super.getMethodList(mapperClass);
        methodList.add(new InsertBatchSomeColumn());
        methodList.add(new SelfInsertOrUpdateBatch());
        return methodList;
    }
}
