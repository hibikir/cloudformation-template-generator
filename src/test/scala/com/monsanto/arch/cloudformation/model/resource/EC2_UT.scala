package com.monsanto.arch.cloudformation.model.resource

import org.scalatest.{Matchers, FunSpec}
import spray.json._

class EC2_UT extends FunSpec with Matchers {

  describe("VPNGateway"){
    val vpnType = "ipsec.1"
    val vpnGateway = `AWS::EC2::VPNGateway`(
      "vpnGateway",
      vpnType,
      Seq()
    )
    it("should create a VPNGateway") {
      val expected = JsObject(
        "vpnGateway" -> JsObject(
          "Type" -> JsString(vpnType),
          "Properties" -> JsObject(
            "Tags" -> JsArray()
          )
        )
      )
    }
  }
  describe("CidrBlock"){

    val cidr = CidrBlock(192,168,1,2,32)

    it("should write valid CidrBlock"){
      cidr.toJson shouldEqual JsString("192.168.1.2/32")
    }

    it("should read valid CidrBlock") {
      JsString("192.168.1.2/32").convertTo[CidrBlock] shouldEqual cidr
    }

  }

  describe("IPAddress"){
    val ipAddress = IPAddress(192,168,1,2)

    it("should write valid IPAddress"){
      ipAddress.toJson shouldEqual JsString("192.168.1.2")
    }

    it("should read valid IPAddress"){
      JsString("192.168.1.2").convertTo[IPAddress] shouldEqual ipAddress
    }

  }
}
