package ponder.narrathon.model

import kabinet.api.*
import ponder.narrathon.model.data.Example
import ponder.narrathon.model.data.NewExample

object Api: ApiNode(null, apiPrefix) {
    object Examples : GetByIdEndpoint<Example>(this, "/example") {
        object User : GetEndpoint<List<Example>>(this, "/user")
        object Create: PostEndpoint<NewExample, Long>(this)
        object Delete: DeleteEndpoint<Long>(this)
        object Update: UpdateEndpoint<Example>(this)
    }
}

val apiPrefix = "/api/v1"
