import router from "../router.js";


describe('Router', () => {
    beforeEach(() => {
        router.addRouteHandler('/test', () => 'Test handler')
        router.addDefaultNotFoundRouteHandler(() => 'Not found handler')
    })

    afterEach(() => {
        // Reset routes and handlers after each test
        router.routes = []
        router.notFoundRouteHandler = () => { throw "Route handler for unknown routes not defined" }
    })

    it('returns correct handler for existing route', () => {
        let routehandler = router.getRouteHandler('/test')
        routehandler().should.equal('Test handler')
    })

    it('returns not found handler for non-existing route', () => {
        let routehandler = router.getRouteHandler('/unknown')
        routehandler().should.equal('Not found handler')
    })

    it('returns correct parameters and query for route with parameters and query', () => {
        router.addRouteHandler('/user/:id', () => 'User handler')
        const { params, query } = router.getRequestParamsAndQuery('/user/123?active=true')
        params.should.equal({ id: '123' })
        query.should.equal({ active: 'true' })
    })

    it('returns empty parameters and query for route without parameters and query', () => {
        const { params, query } = router.getRequestParamsAndQuery('/test')
        (params).should.equal({})
        (query).should.equal({})
    })
})