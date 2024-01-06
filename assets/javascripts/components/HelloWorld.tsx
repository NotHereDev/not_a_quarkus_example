import {createSignal} from "solid-js";

export default function HelloWorld({name}: {name: string}) {
    const [count, setCount] = createSignal(0);

    return <>
        <div>Hello {name}</div>
        <button onClick={() => setCount(count() + 1)}>
            Count: {count()} heeeeeeee
        </button>
    </>;
}