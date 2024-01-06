// @ts-ignore
import {generateHydrationScript, renderToString} from "solid-js/web"

const allComponents = require.context('./components', true, /\.tsx?$/);

function getComponentByName(name: string) {
    return allComponents(`./${name}.tsx`).default;
}

function renderComponent(name: string, props: any) {
    const component = getComponentByName(name);
    return renderToString(() => component(props));
}

//@ts-ignore
globalThis.generateHydrationScript = generateHydrationScript;
//@ts-ignore
globalThis.renderComponent = renderComponent;