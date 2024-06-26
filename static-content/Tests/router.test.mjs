import router from "../SPA/router.js";


describe('Router', () => {
    before(() => {
        router.addRouteHandler('/user/:id', () => 'User handler');
        router.addRouteHandler('/test', () => 'Test handler');
        router.addDefaultNotFoundRouteHandler(() => 'Not found handler');
    })

    afterEach(() => {
        // Reset routes and handlers after each test
        router.routes = [];
        router.notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" };
    })

    it('returns correct handler for existing route', () => {
        let routehandler = router.getRouteHandler('/test');
        routehandler().should.equal('Test handler');
    })

    it('returns not found handler for non-existing route', () => {
        let routehandler = router.getRouteHandler('/unknown');
        routehandler().should.equal('Not found handler');
    })

    it('returns correct parameters and query for route with parameters and query', () => {
        const { params, query } = router.getRequestParamsAndQuery('/user/123?active=true');
        params.id.should.equal('123');
        query.active.should.equal('true');
    })

    it('returns empty parameters and query for route without parameters and query', () => {
        const { params, query } = router.getRequestParamsAndQuery('/test');
        (Object.keys(params).length).should.equal(0);
        (Object.keys(query).length).should.equal(0);
    })

    it('returns empty parameters and query for route with parameters but without query', () => {
        const { params, query } = router.getRequestParamsAndQuery('/user/123');
        params.id.should.equal('123');
        (Object.keys(query).length).should.equal(0);
    })

    it('returns empty parameters and query for route without parameters but with query', () => {
        const { params, query } = router.getRequestParamsAndQuery('/test?active=true');
        (Object.keys(params).length).should.equal(0);
        query.active.should.equal('true');
    })

})