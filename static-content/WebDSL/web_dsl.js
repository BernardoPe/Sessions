// A dsl to create html elements

function createElement(tagName, ...children) {
    if (typeof tagName !== 'string') throw new Error('tagName must be a string');
    const element= document.createElement(tagName);
    children.forEach(child => {
        if (typeof child === 'string') {
            element.appendChild(document.createTextNode(child));
        } else {
            element.appendChild(child);
        }
    });
    return element;
}

// For now using free functions. In the future, we can create a class to represent the elements

function div(...children) {
    return createElement('div', ...children);
}

function ul(...children) {
    return createElement('ul', ...children);
}

function li(...children) {
    return createElement('li', ...children);
}

function a(href: String, ...children) {
    const element = createElement('a', ...children);
    if (href !== undefined) element.href = href;
    return element;
}

function button(...children) {
    return createElement('button', ...children);
}

function input(type: String, ...children) {
    const element = createElement('input', ...children);
    if (type !== undefined) element.type = type;
    return element;
}

// Test
function _test() {
    const element = div(
        ul(
            li(a('https://www.google.com', 'Google')),
            li(a('https://www.bing.com', 'Bing'))
        ),
        button('Click me'),
        input('text')
    );

    // print the element
    console.log(element.outerHTML);
}
