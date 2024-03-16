package pt.isel.ls.api

/**
class GameEndpointsTest {

    private val api = SessionsApi(playerService(), gameService(), sessionsService())
    @Test
    fun `test create game should create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test3","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create game empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"","developer":"Test","genres":[""]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test create game with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test create game, game with name already exists`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create game no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details should return game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_GAME_DETAILS)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<Game>(gameDetailsJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameDetails.name == "TestName")
        assert(gameDetails.developer == "TestDeveloper")
        assert(gameDetails.genres == listOf("TestGenre"))
        assert(gameDetails.gid == 1)
    }
    @Test
    fun `test get game details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/games/2")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_GAME_DETAILS)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get game list should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]}""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<List<Game>>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.size == 2)
        assert(gameList[0].name == "TestName1")
        assert(gameList[0].developer == "TestDeveloper1")
        assert(gameList[0].genres == listOf("TestGenre1"))
        assert(gameList[0].gid == 1)
        assert(gameList[1].name == "TestName2")
        assert(gameList[1].developer == "TestDeveloper2")
        assert(gameList[1].genres == listOf("TestGenre2"))
        assert(gameList[1].gid == 2)
    }

    @Test
    fun `test get game list empty fields should return empty array`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"","genres":[]}""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(response.bodyString() == """{"games":[]}""")
    }

    @Test
    fun `test get game list invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }



    @Test
    fun `test get game list limit and skip should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<List<Game>>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.size == 1)
        assert(gameList[0].name == "TestName2")
        assert(gameList[0].developer == "TestDeveloper2")
        assert(gameList[0].genres == listOf("TestGenre2"))
        assert(gameList[0].gid == 2)
    }

}*/