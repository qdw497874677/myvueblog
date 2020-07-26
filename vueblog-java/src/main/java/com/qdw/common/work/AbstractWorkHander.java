package com.qdw.common.work;

/**
 * @PackageName:com.qdw.common.work
 * @ClassName: Worker
 * @Description:
 * @date: 2020/7/26 0026 20:03
 */
public abstract class AbstractWorkHander {
    private AbstractWorkHander nextHander;
    public void setNextWork(AbstractWorkHander hander){
        this.nextHander = hander;
    }
    protected abstract boolean resolve(Work work);
    protected abstract void done(Work work);
    protected abstract void fail(Work work);
    public boolean support(Work work){
        if(resolve(work)){
            done(work);
            return true;
        }else {
            if (nextHander == null || !nextHander.resolve(work)){
                fail(work);
                return false;
            }
        }
        return false;
    }
}
