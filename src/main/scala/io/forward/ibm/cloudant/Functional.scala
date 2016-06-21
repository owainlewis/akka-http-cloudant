package io.forward.ibm.cloudant

import akka.http.scaladsl.model.HttpResponse
import cats.data.Xor
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Future

// Misc ideas
object Functional {

  def tranform(response: HttpResponse): Xor[HttpResponse, Future[String]] =
    if (Set(200, 201, 202).contains(response.status.intValue()))
      Xor.Right(Unmarshal(response.entity).to[String])
    else Xor.Left(response)
}
