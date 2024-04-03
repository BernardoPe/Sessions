// A dsl to create html elements

function createElement(tagName, ...children) {
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

function a(href, ...children) {
    const element = createElement('a', ...children);
    element.href = href;
    return element;
}

function button(...children) {
    return createElement('button', ...children);
}

function input(type, ...children) {
    const element = createElement('input', ...children);
    element.type = type;
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
