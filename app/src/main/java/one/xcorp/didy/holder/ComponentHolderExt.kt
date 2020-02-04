package one.xcorp.didy.holder

import one.xcorp.didy.ComponentInitializer
import one.xcorp.didy.Injector

fun <Factory : Any, Component : Any> ComponentHolder<Factory, Component>.getComponent(
    initializer: ComponentInitializer<Factory, Component>
) = getComponent(initializer::invoke)

fun <Factory : Any, Component : Any> ComponentHolder<Factory, Component>.injectWith(
    injector: Injector<Component>,
    initializer: ComponentInitializer<Factory, Component>
) = injectWith(injector::invoke, initializer::invoke)

fun <Factory : Any, Component : Any> ComponentHolder<Factory, Component>.injectWith(
    injector: (Component) -> Unit,
    initializer: ComponentInitializer<Factory, Component>
) = injectWith(injector, initializer::invoke)

fun <Factory : Any, Component : Any> ComponentHolder<Factory, Component>.injectWith(
    injector: Injector<Component>,
    initializer: Factory.() -> Component
) = injectWith(injector::invoke, initializer)

fun <Factory : Any, Component : Any> ComponentHolder<Factory, Component>.injectWith(
    injector: (Component) -> Unit,
    initializer: Factory.() -> Component
): ComponentHolder<Factory, Component> {
    injector(getComponent { initializer(this) })
    return this
}
