package de.jcup.asp.server.asciidoctorj.provider;
public abstract class AbstractAsciiDoctorProvider {

    private ProviderContext context;

    AbstractAsciiDoctorProvider(ProviderContext context){
        if (context==null ){
            throw new IllegalArgumentException("context may never be null!");
        }
        this.context=context;
    }
    
    ProviderContext getContext() {
        return context;
    }

    protected abstract void reset() ;
}