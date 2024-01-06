import {hydrate, render} from "solid-js/web";

const allComponents = require.context('./components', true, /\.tsx?$/);

class ReactiveComponent extends HTMLElement {
    constructor() {
        super();
    }

    get component() {
        const name = this.getAttribute('name');
        if(!name) throw new Error('No name attribute provided');
        const props = this.getAttribute('props');
        if(!props) console.warn('No props attribute provided, using empty object');
        return () => allComponents(`./${name}.tsx`).default(JSON.parse(props || '{}'));
    }

    connectedCallback() {
        const ssr = this.hasAttribute('ssr');
        setTimeout(() => {
            if(ssr) hydrate(this.component, this);
            else render(this.component, this);
        })
    }
}

customElements.define('reactive-component', ReactiveComponent);