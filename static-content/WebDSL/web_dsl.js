// A dsl to create html elements

function createElement(tagName, props, ...children) {
    const element = document.createElement(tagName);

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

// For now using free functions. In the future, we can create a class to represent the elements

function body(props=null, ...children) {
    return createElement('body', props, ...children);
}

function div(props=null, ...children) {
    return createElement('div', props, ...children);
}

function ul(props=null, ...children) {
    return createElement('ul', props, ...children);
}

function li(props=null, ...children) {
    return createElement('li', props, ...children);
}

function a(href, props=null, ...children) {
    return createElement('a', { href, ...props }, ...children);
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
    return createElement('img', { src, ...props }, ...children);
}

function br(...children) {
    return createElement('br', null, ...children);
}
function button(props, ...children) {
    return createElement('button', props, ...children);
}

function ol(props, ...children) {
    return createElement('ol', props, ...children);
}

export { body, div, ul, li, a, button, input, p, h1, h2, br, img, span, h3, h4, h5, h6, ol};

// Test
function _test() {
    const element = body(null,
        div({ id: 'container' },
            ul(null,
                li(null, a('https://www.google.com', null, 'Google')),
                li(null, a('https://www.facebook.com', null, 'Facebook')),
                li(null, a('https://www.twitter.com', null, 'Twitter'))
            ),
            button({ onclick: () => alert('Hello') }, 'Click me'),
            input({ type: 'text', placeholder: 'Enter your name' })
        )
    );

    document.body.appendChild(element);
}
