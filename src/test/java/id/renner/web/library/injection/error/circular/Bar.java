package id.renner.web.library.injection.error.circular;

import id.renner.web.library.injection.Inject;

@Inject
public class Bar {
    private final Foo foo;

    public Bar(Foo foo) {
        this.foo = foo;
    }
}