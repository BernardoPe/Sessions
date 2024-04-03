
// import jest
const { test, expect, describe } = require('@jest/globals');
const { createElement, div, ul, li, a, button, input } = require('../web_dsl.js');

describe('WebDSL', () => {
    test('createElement creates an element with correct tag and children', () => {
        const element = createElement('div', 'Hello, world!');
        expect(element.tagName).toBe('DIV');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('div creates a div element', () => {
        const element = div('Hello, world!');
        expect(element.tagName).toBe('DIV');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('ul creates an ul element', () => {
        const element = ul('Hello, world!');
        expect(element.tagName).toBe('UL');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('li creates an li element', () => {
        const element = li('Hello, world!');
        expect(element.tagName).toBe('LI');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('a creates an a element with href', () => {
        const element = a('https://example.com', 'Hello, world!');
        expect(element.tagName).toBe('A');
        expect(element.href).toBe('https://example.com/');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('button creates a button element', () => {
        const element = button('Hello, world!');
        expect(element.tagName).toBe('BUTTON');
        expect(element.textContent).toBe('Hello, world!');
    });

    test('input creates an input element with type', () => {
        const element = input('text', 'Hello, world!');
        expect(element.tagName).toBe('INPUT');
        expect(element.type).toBe('text');
        expect(element.value).toBe('Hello, world!');
    });
});