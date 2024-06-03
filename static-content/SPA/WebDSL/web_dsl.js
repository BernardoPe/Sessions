// A dsl to create html elements

function createElement(tagName, props, ...children) {
    const element = document.createElement(tagName);

    // Set the properties
    if (props !== null && props !== undefined) {
        for (const key in props) {
            if (key.startsWith('on')) {
                const eventName = key.slice(2); // remove 'on' prefix
                element.addEventListener(eventName, props[key]);
            } else {
                element.setAttribute(key, props[key]);
            }
        }
    }

    // Append the children
    for (const child of children) {
        if (typeof child === 'string') {
            element.appendChild(document.createTextNode(child));
        } else {
            element.appendChild(child);
        }
    }

    return element;
}

function createElementNS(namespace, tagName, props, ...children) {
    const element = document.createElementNS(namespace, tagName);

    // Set the properties
    if (props !== null && props !== undefined) {
        for (const key in props) {
            element.setAttribute(key, props[key])
        }
    }

    // Append the children
    for (const child of children) {
        if (typeof child === 'string') {
            element.appendChild(document.createTextNode(child));
        } else {
            element.appendChild(child);
        }
    }

    return element;
}

function body(props=null, ...children) {
    return createElement('body', props, ...children);
}

export function path(props=null, ...children) {
    return createElementNS('http://www.w3.org/2000/svg', 'path', props, ...children);
}

function svg(props=null, ...children) {
    return createElementNS('http://www.w3.org/2000/svg', 'svg', props, ...children);
}

function div(props=null, ...children) {
    return createElement('div', props, ...children);
}

function ul(props=null, ...children) {
    return createElement('ul', props, ...children);
}

function i(props=null, ...children) {
    return createElement('i', props, ...children);
}

function li(props=null, ...children) {
    return createElement('li', props, ...children);
}

function a(href, props=null, ...children) {
    props = href ? { href, ...props } : props;
    return createElement('a', props, ...children);
}

function input(props=null, ...children) {
    return createElement('input', props, ...children);
}

function h1(props=null, ...children) {
    return createElement('h1', props, ...children);
}

function h2(props=null, ...children) {
    return createElement('h2', props, ...children);
}

function h3(props=null, ...children) {
    return createElement('h3', props, ...children);
}

function h4(props=null, ...children) {
    return createElement('h4', props, ...children);
}

function h5(props=null, ...children) {
    return createElement('h5', props, ...children);
}

function h6(props=null, ...children) {
    return createElement('h6', props, ...children);
}

function p(props=null, ...children) {
    return createElement('p', props, ...children);
}

function span(props=null, ...children) {
    return createElement('span', props, ...children);
}

function img(src, props=null, ...children) {
    props = src ? { src, ...props } : props;
    return createElement('img', props, ...children);
}

function br(props = null, ...children) {
    return createElement('br', null, ...children);
}
function button(props, ...children) {
    return createElement('button', props, ...children);
}

function ol(props = null, ...children) {
    return createElement('ol', props, ...children);
}

function label(forProp, props = null, ...children) {
    props = forProp ? { for: forProp, ...props } : props;
    return createElement('label', props, ...children);
}

function form(props = null, ...children) {
    return createElement('form', props, ...children);
}

function textarea(props = null, ...children) {
    return createElement('textarea', props, ...children);
}

function fieldset(props = null, ...children) {
    return createElement('fieldset', props, ...children);
}

function legend(props = null, ...children) {
    return createElement('legend', props, ...children);
}

function nav(props = null, ...children) {
    return createElement('nav', props, ...children);
}

export {
    body,
    div,
    ul,
    li,
    a,
    input,
    h1,
    h2,
    h3,
    h4,
    h5,
    h6,
    p,
    span,
    img,
    br,
    button,
    ol,
    label,
    form,
    textarea,
    fieldset,
    legend,
    svg,
    nav,
    i
};
