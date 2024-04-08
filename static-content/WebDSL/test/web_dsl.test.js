/** Must use mocha tests */
// import mocha
const { it, expect, describe, before } = require('mocha');
const { JSDOM } = require('jsdom');
// import web_dsl
const { a, button, div, input, li, ul } = require('../web_dsl.js');

describe('WebDSL', () => {
    before(() => {
        const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
        global.document = dom.window.document;
        global.window = dom.window;
    });

    it('div creates a div element with no properties or children', () => {
        const element = div();
        expect(element.tagName).toBe('DIV');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('div creates a div element with properties but no children', () => {
        const element = div({ id: 'test' });
        expect(element.tagName).toBe('DIV');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(0);
    });

    it('div creates a div element with children but no properties', () => {
        const element = div(null, 'Hello', 'World');
        expect(element.tagName).toBe('DIV');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(2);
    });

    it('div creates a div element with properties and children', () => {
        const element = div({ id: 'test' }, 'Hello', 'World');
        expect(element.tagName).toBe('DIV');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(2);
    });

    it('a creates an anchor element with no properties or children', () => {
        const element = a();
        expect(element.tagName).toBe('A');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('a creates an anchor element with properties but no children', () => {
        const element = a('https://www.google.com', { id: 'test' });
        expect(element.tagName).toBe('A');
        expect(element.getAttribute('href')).toBe('https://www.google.com');
        expect(element.childNodes.length).toBe(0);
    });

    it('a creates an anchor element with children but no properties', () => {
        const element = a(null, null, 'Google');
        expect(element.tagName).toBe('A');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('a creates an anchor element with properties and children', () => {
        const element = a( 'https://www.google.com',{ id: 'test' }, 'Google');
        expect(element.tagName).toBe('A');
        expect(element.getAttribute('href')).toBe('https://www.google.com');
        expect(element.childNodes.length).toBe(1);
    });

    it('button creates a button element with no properties or children', () => {
        const element = button();
        expect(element.tagName).toBe('BUTTON');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('button creates a button element with properties but no children', () => {
        const element = button({ type: 'submit' });
        expect(element.tagName).toBe('BUTTON');
        expect(element.getAttribute('type')).toBe('submit');
        expect(element.childNodes.length).toBe(0);
    });

    it('button creates a button element with children but no properties', () => {
        const element = button(null, 'Submit');
        expect(element.tagName).toBe('BUTTON');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('button creates a button element with properties and children', () => {
        const element = button({ type: 'submit' }, 'Submit');
        expect(element.tagName).toBe('BUTTON');
        expect(element.getAttribute('type')).toBe('submit');
        expect(element.childNodes.length).toBe(1);
    });

    it('input creates an input element with no properties or children', () => {
        const element = input();
        expect(element.tagName).toBe('INPUT');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('input creates an input element with properties but no children', () => {
        const element = input({ type: 'text' });
        expect(element.tagName).toBe('INPUT');
        expect(element.getAttribute('type')).toBe('text');
        expect(element.childNodes.length).toBe(0);
    });

    it('input creates an input element with children but no properties', () => {
        const element = input(null, 'Hello');
        expect(element.tagName).toBe('INPUT');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('input creates an input element with properties and children', () => {
        const element = input({ type: 'text' }, 'Hello');
        expect(element.tagName).toBe('INPUT');
        expect(element.getAttribute('type')).toBe('text');
        expect(element.childNodes.length).toBe(1);
    });

    it('li creates a list item element with no properties or children', () => {
        const element = li();
        expect(element.tagName).toBe('LI');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('li creates a list item element with properties but no children', () => {
        const element = li({ id: 'test' });
        expect(element.tagName).toBe('LI');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(0);
    });

    it('li creates a list item element with children but no properties', () => {
        const element = li(null, 'Hello');
        expect(element.tagName).toBe('LI');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('li creates a list item element with properties and children', () => {
        const element = li({ id: 'test' }, 'Hello');
        expect(element.tagName).toBe('LI');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(1);
    });

    it('ul creates an unordered list element with no properties or children', () => {
        const element = ul();
        expect(element.tagName).toBe('UL');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(0);
    });

    it('ul creates an unordered list element with properties but no children', () => {
        const element = ul({ id: 'test' });
        expect(element.tagName).toBe('UL');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(0);
    });

    it('ul creates an unordered list element with children but no properties', () => {
        const element = ul(null, li(null, 'Hello'));
        expect(element.tagName).toBe('UL');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('ul creates an unordered list element with properties and children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'));
        expect(element.tagName).toBe('UL');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(1);
    });

    it('ul creates an unordered list element with multiple children', () => {
        const element = ul(null, li(null, 'Hello'), li(null, 'World'));
        expect(element.tagName).toBe('UL');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(2);
    });

    it('ul creates an unordered list element with properties and multiple children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'), li(null, 'World'));
        expect(element.tagName).toBe('UL');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(2);
    });

    it('ul creates an unordered list element with nested children', () => {
        const element = ul(null, li(null, 'Hello', ul(null, li(null, 'World'))));
        expect(element.tagName).toBe('UL');
        expect(element.attributes.length).toBe(0);
        expect(element.childNodes.length).toBe(1);
    });

    it('ul creates an unordered list element with properties and nested children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'), li(null, 'World'));
        expect(element.tagName).toBe('UL');
        expect(element.getAttribute('id')).toBe('test');
        expect(element.childNodes.length).toBe(2);
    });

});