package id.renner.web.library.controller.sunshine;

import id.renner.web.library.injection.Inject;

@Inject
public class Foo {
    private final Bar bar;
    private final Baz baz;

    public Foo(Bar bar, Baz baz) {
        this.bar = bar;
        this.baz = baz;
    }

    public String foo(){
        return "foo " + bar.bar() + " " + baz.baz();
    }
}