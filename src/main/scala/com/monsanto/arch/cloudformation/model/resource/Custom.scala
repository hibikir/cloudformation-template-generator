package com.monsanto.arch.cloudformation.model.resource

import com.monsanto.arch.cloudformation.model.{ConditionRef, Token}
import spray.json.JsonFormat

/**
  * Created by bkrodg on 1/11/16.
  */
case class `Custom::NatGateway`(name: String,
                                ServiceToken: Token[String],
                                AllocationId: Token[String],
                                SubnetId: Token[`AWS::EC2::Subnet`],
                                WaitHandle: Token[`AWS::CloudFormation::WaitConditionHandle`],
                                override val Condition: Option[ConditionRef] = None,
                                override val DependsOn: Option[Seq[String]] = None)
  extends Resource[`Custom::NatGateway`] {
  def when(newCondition: Option[ConditionRef] = Condition) = copy(Condition = newCondition)
}

object `Custom::NatGateway` {

  import spray.json.DefaultJsonProtocol._

  implicit def format: JsonFormat[`Custom::NatGateway`] = jsonFormat7(`Custom::NatGateway`.apply)
}

case class `Custom::NatGatewayRoute`(name: String,
                                     ServiceToken: Token[String],
                                     RouteTableId: Token[`AWS::EC2::RouteTable`],
                                     DestinationCidrBlock: Token[CidrBlock],
                                     NatGatewayId: Token[`Custom::NatGateway`],
                                     override val Condition: Option[ConditionRef] = None,
                                     override val DependsOn: Option[Seq[String]] = None)
  extends Resource[`Custom::NatGatewayRoute`] {
  def when(newCondition: Option[ConditionRef] = Condition) = copy(Condition = newCondition)
}

object `Custom::NatGatewayRoute` {
  import spray.json.DefaultJsonProtocol._
  implicit def format: JsonFormat[`Custom::NatGatewayRoute`] = jsonFormat7(`Custom::NatGatewayRoute`.apply)
}
