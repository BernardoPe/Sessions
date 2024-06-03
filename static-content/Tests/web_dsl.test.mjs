
import { div, a, button, input, li, ul } from '../SPA/WebDSL/web_dsl.js';

describe('WebDSL', () => {

    it('div creates a div element with no properties or children', () => {
        const element = div();
        (element.tagName).should.equal('DIV');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('div creates a div element with properties but no children', () => {
        const element = div({ id: 'test' });
        (element.tagName).should.equal('DIV');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(0);
    });

    it('div creates a div element with children but no properties', () => {
        const element = div(null, 'Hello', 'World');
        (element.tagName).should.equal('DIV');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(2);
    });

    it('div creates a div element with properties and children', () => {
        const element = div({ id: 'test' }, 'Hello', 'World');
        (element.tagName).should.equal('DIV');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(2);
    });

    it('a creates an anchor element with no properties or children', () => {
        const element = a();
        (element.tagName).should.equal('A');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('a creates an anchor element with properties but no children', () => {
        const element = a('https://www.google.com', { id: 'test' });
        (element.tagName).should.equal('A');
        (element.getAttribute('href')).should.equal('https://www.google.com');
        (element.childNodes.length).should.equal(0);
    });

    it('a creates an anchor element with children but no properties', () => {
        const element = a(null, null, 'Google');
        (element.tagName).should.equal('A');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('a creates an anchor element with properties and children', () => {
        const element = a( 'https://www.google.com',{ id: 'test' }, 'Google');
        (element.tagName).should.equal('A');
        (element.getAttribute('href')).should.equal('https://www.google.com');
        (element.childNodes.length).should.equal(1);
    });

    it('button creates a button element with no properties or children', () => {
        const element = button();
        (element.tagName).should.equal('BUTTON');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('button creates a button element with properties but no children', () => {
        const element = button({ type: 'submit' });
        (element.tagName).should.equal('BUTTON');
        (element.getAttribute('type')).should.equal('submit');
        (element.childNodes.length).should.equal(0);
    });

    it('button creates a button element with children but no properties', () => {
        const element = button(null, 'Submit');
        (element.tagName).should.equal('BUTTON');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('button creates a button element with properties and children', () => {
        const element = button({ type: 'submit' }, 'Submit');
        (element.tagName).should.equal('BUTTON');
        (element.getAttribute('type')).should.equal('submit');
        (element.childNodes.length).should.equal(1);
    });

    it('input creates an input element with no properties or children', () => {
        const element = input();
        (element.tagName).should.equal('INPUT');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('input creates an input element with properties but no children', () => {
        const element = input({ type: 'text' });
        (element.tagName).should.equal('INPUT');
        (element.getAttribute('type')).should.equal('text');
        (element.childNodes.length).should.equal(0);
    });

    it('input creates an input element with children but no properties', () => {
        const element = input(null, 'Hello');
        (element.tagName).should.equal('INPUT');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('input creates an input element with properties and children', () => {
        const element = input({ type: 'text' }, 'Hello');
        (element.tagName).should.equal('INPUT');
        (element.getAttribute('type')).should.equal('text');
        (element.childNodes.length).should.equal(1);
    });

    it('li creates a list item element with no properties or children', () => {
        const element = li();
        (element.tagName).should.equal('LI');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('li creates a list item element with properties but no children', () => {
        const element = li({ id: 'test' });
        (element.tagName).should.equal('LI');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(0);
    });

    it('li creates a list item element with children but no properties', () => {
        const element = li(null, 'Hello');
        (element.tagName).should.equal('LI');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('li creates a list item element with properties and children', () => {
        const element = li({ id: 'test' }, 'Hello');
        (element.tagName).should.equal('LI');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(1);
    });

    it('ul creates an unordered list element with no properties or children', () => {
        const element = ul();
        (element.tagName).should.equal('UL');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(0);
    });

    it('ul creates an unordered list element with properties but no children', () => {
        const element = ul({ id: 'test' });
        (element.tagName).should.equal('UL');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(0);
    });

    it('ul creates an unordered list element with children but no properties', () => {
        const element = ul(null, li(null, 'Hello'));
        (element.tagName).should.equal('UL');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('ul creates an unordered list element with properties and children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'));
        (element.tagName).should.equal('UL');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(1);
    });

    it('ul creates an unordered list element with multiple children', () => {
        const element = ul(null, li(null, 'Hello'), li(null, 'World'));
        (element.tagName).should.equal('UL');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(2);
    });

    it('ul creates an unordered list element with properties and multiple children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'), li(null, 'World'));
        (element.tagName).should.equal('UL');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(2);
    });

    it('ul creates an unordered list element with nested children', () => {
        const element = ul(null, li(null, 'Hello', ul(null, li(null, 'World'))));
        (element.tagName).should.equal('UL');
        (element.attributes.length).should.equal(0);
        (element.childNodes.length).should.equal(1);
    });

    it('ul creates an unordered list element with properties and nested children', () => {
        const element = ul({ id: 'test' }, li(null, 'Hello'), li(null, 'World'));
        (element.tagName).should.equal('UL');
        (element.getAttribute('id')).should.equal('test');
        (element.childNodes.length).should.equal(2);
    });

});