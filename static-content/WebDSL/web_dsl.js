// A dsl to create html elements

function createElement(tagName, props, ...children) {
    const element = document.createElement(tagName);

    // Set the properties
    if (props !== null && props !== undefined) {
        for (const key in props) {
            element[key] = props[key];
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

function body(props, ...children) {
    return createElement('body', props, ...children);
}

function div(props, ...children) {
    return createElement('div', props, ...children);
}

function ul(props, ...children) {
    return createElement('ul', props, ...children);
}

function li(props, ...children) {
    return createElement('li', props, ...children);
}

function a(href, props, ...children) {
    return createElement('a', { href, ...props }, ...children);
}

function button(props, ...children) {
    return createElement('button', props, ...children);
}

function input(props, ...children) {
    return createElement('input', props, ...children);
}

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
