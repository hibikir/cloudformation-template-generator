package com.monsanto.arch.cloudformation.model.resource

import com.monsanto.arch.cloudformation.model._
import com.monsanto.arch.cloudformation.model.Token.TokenSeq
import spray.json._

/**
 * Created by Ryan Richt on 2/28/15
 */

// Dimensions { "Name" : "InstanceId", "Value" : { "Ref" : "WebServerInstance" }} ],
// MetricName "StatusCheckFailed_System"
case class `AWS::CloudWatch::Alarm`(
  name: String,
  ComparisonOperator:      `AWS::CloudWatch::Alarm::ComparisonOperator`,
  EvaluationPeriods:       String, //BackedInt,
  MetricName:              String,
  Namespace:               `AWS::CloudWatch::Alarm::Namespace`,
  Period:                  String, //BackedInt,
  Statistic:               `AWS::CloudWatch::Alarm::Statistic`,
  Threshold:               String, //BackedInt,
  ActionsEnabled:          Option[Boolean] = None,
  AlarmActions:            Option[TokenSeq[String]] = None,
  AlarmDescription:        Option[String] = None,
  AlarmName:               Option[Token[String]] = None,
  Dimensions:              Option[Seq[`AWS::CloudWatch::Alarm::Dimension`]] = None,
  InsufficientDataActions: Option[TokenSeq[String]] = None,
  TreatMissingData:        Option[MissingDataTreatment] = None,
  OKActions:               Option[TokenSeq[String]] = None,
  Unit:                    Option[`AWS::CloudWatch::Alarm::Unit`] = None,
  override val DependsOn: Option[Seq[String]] = None,
  override val Condition: Option[ConditionRef] = None
  ) extends Resource[`AWS::CloudWatch::Alarm`]{

  def when(newCondition: Option[ConditionRef] = Condition) = copy(Condition = newCondition)
}
object `AWS::CloudWatch::Alarm` extends DefaultJsonProtocol {
  implicit val format: JsonFormat[`AWS::CloudWatch::Alarm`] = jsonFormat19(`AWS::CloudWatch::Alarm`.apply)
}

sealed trait `AWS::CloudWatch::Alarm::ComparisonOperator`
object `AWS::CloudWatch::Alarm::ComparisonOperator` extends DefaultJsonProtocol {
  case object GreaterThanOrEqualToThreshold extends `AWS::CloudWatch::Alarm::ComparisonOperator`
  case object GreaterThanThreshold          extends `AWS::CloudWatch::Alarm::ComparisonOperator`
  case object LessThanOrEqualToThreshold    extends `AWS::CloudWatch::Alarm::ComparisonOperator`
  case object LessThanThreshold             extends `AWS::CloudWatch::Alarm::ComparisonOperator`
  val values = Seq(GreaterThanOrEqualToThreshold, GreaterThanThreshold, LessThanOrEqualToThreshold, LessThanThreshold)
  implicit val format: JsonFormat[`AWS::CloudWatch::Alarm::ComparisonOperator`] =
    new EnumFormat[`AWS::CloudWatch::Alarm::ComparisonOperator`](values)
}

case class `AWS::CloudWatch::Alarm::Dimension`(Name: String, Value: Token[String])
object `AWS::CloudWatch::Alarm::Dimension` extends DefaultJsonProtocol {
  implicit val format: JsonFormat[`AWS::CloudWatch::Alarm::Dimension`] = jsonFormat2(`AWS::CloudWatch::Alarm::Dimension`.apply)
  def from[A <: Resource[A]](name: String, value: ResourceRef[A]): `AWS::CloudWatch::Alarm::Dimension` = `AWS::CloudWatch::Alarm::Dimension`(name, value)
  def from[A <: Resource[A]](name: String, value: Token[String]): `AWS::CloudWatch::Alarm::Dimension` = `AWS::CloudWatch::Alarm::Dimension`(name, value)
}



sealed trait `AWS::CloudWatch::Alarm::Namespace`
object `AWS::CloudWatch::Alarm::Namespace` extends DefaultJsonProtocol {
  case object `AWS/ApiGateway`        extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/AppStream`         extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/AutoScaling`       extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Billing`           extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/CloudFront`        extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/CloudSearch`       extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Events`            extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Logs`              extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/DMS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/DX`                extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/DynamoDB`          extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/EC2`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/EC2Spot`           extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ECS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ElasticBeanstalk`  extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/EBS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/EFS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ELB`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ApplicationELB`    extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/NetworkELB`        extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ElasticTranscoder` extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ElastiCache`       extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ES`                extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ElasticMapReduce`  extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/GameLift`          extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Inspector`         extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/IoT`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/KMS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/KinesisAnalytics`  extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Firehose`          extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Kinesis`           extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Lambda`            extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Lex`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/ML`                extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/OpsWorks`          extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Polly`             extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Redshift`          extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/RDS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/Route53`           extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/DDoSProtection`    extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/SES`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/SNS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/SQS`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/S3`                extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/SWF`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/States`            extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/StorageGateway`    extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/NATGateway`        extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/VPN`               extends `AWS::CloudWatch::Alarm::Namespace`
  case object `WAF`                   extends `AWS::CloudWatch::Alarm::Namespace`
  case object `AWS/WorkSpaces`        extends `AWS::CloudWatch::Alarm::Namespace`

  val values = Seq(
    `AWS/ApiGateway`,
    `AWS/AppStream`,
    `AWS/AutoScaling`,
    `AWS/Billing`,
    `AWS/CloudFront`,
    `AWS/CloudSearch`,
    `AWS/Events`,
    `AWS/Logs`,
    `AWS/DMS`,
    `AWS/DX`,
    `AWS/DynamoDB`,
    `AWS/EC2`,
    `AWS/EC2Spot`,
    `AWS/ECS`,
    `AWS/ElasticBeanstalk`,
    `AWS/EBS`,
    `AWS/EFS`,
    `AWS/ELB`,
    `AWS/ApplicationELB`,
    `AWS/NetworkELB`,
    `AWS/ElasticTranscoder`,
    `AWS/ElastiCache`,
    `AWS/ES`,
    `AWS/ElasticMapReduce`,
    `AWS/GameLift`,
    `AWS/Inspector`,
    `AWS/IoT`,
    `AWS/KMS`,
    `AWS/KinesisAnalytics`,
    `AWS/Firehose`,
    `AWS/Kinesis`,
    `AWS/Lambda`,
    `AWS/Lex`,
    `AWS/ML`,
    `AWS/OpsWorks`,
    `AWS/Polly`,
    `AWS/Redshift`,
    `AWS/RDS`,
    `AWS/Route53`,
    `AWS/DDoSProtection`,
    `AWS/SES`,
    `AWS/SNS`,
    `AWS/SQS`,
    `AWS/S3`,
    `AWS/SWF`,
    `AWS/States`,
    `AWS/StorageGateway`,
    `AWS/NATGateway`,
    `AWS/VPN`,
    `WAF`,
    `AWS/WorkSpaces`
  )

  implicit val format : JsonFormat[`AWS::CloudWatch::Alarm::Namespace`] = new JsonFormat[`AWS::CloudWatch::Alarm::Namespace`] {
    val _format = new EnumFormat[`AWS::CloudWatch::Alarm::Namespace`](values)
    def write(namespace : `AWS::CloudWatch::Alarm::Namespace`) : JsValue = namespace match {
      case CustomNamespace(ns) => JsString(ns)
      case _ => _format.write(namespace)
    }
    def read(js : JsValue) = ???
  }

  case class CustomNamespace(namespace : String) extends `AWS::CloudWatch::Alarm::Namespace`
  def apply(namespace : String) : `AWS::CloudWatch::Alarm::Namespace` = CustomNamespace(namespace)
  import scala.language.implicitConversions
  implicit def customNamespace(namespace : String) : `AWS::CloudWatch::Alarm::Namespace` = apply(namespace)
}

sealed trait `AWS::CloudWatch::Alarm::Statistic`
object `AWS::CloudWatch::Alarm::Statistic` extends DefaultJsonProtocol {
  case object SampleCount extends `AWS::CloudWatch::Alarm::Statistic`
  case object Average     extends `AWS::CloudWatch::Alarm::Statistic`
  case object Sum         extends `AWS::CloudWatch::Alarm::Statistic`
  case object Minimum     extends `AWS::CloudWatch::Alarm::Statistic`
  case object Maximum     extends `AWS::CloudWatch::Alarm::Statistic`
  val values = Seq(SampleCount, Average, Sum, Minimum, Maximum)
  implicit val format: JsonFormat[`AWS::CloudWatch::Alarm::Statistic`] =
    new EnumFormat[`AWS::CloudWatch::Alarm::Statistic`](values)
}

sealed trait `AWS::CloudWatch::Alarm::Unit`
object `AWS::CloudWatch::Alarm::Unit` extends DefaultJsonProtocol {
  case object Seconds            extends `AWS::CloudWatch::Alarm::Unit`
  case object Microseconds       extends `AWS::CloudWatch::Alarm::Unit`
  case object Milliseconds       extends `AWS::CloudWatch::Alarm::Unit`
  case object Bytes              extends `AWS::CloudWatch::Alarm::Unit`
  case object Kilobytes          extends `AWS::CloudWatch::Alarm::Unit`
  case object Megabytes          extends `AWS::CloudWatch::Alarm::Unit`
  case object Gigabytes          extends `AWS::CloudWatch::Alarm::Unit`
  case object Terabytes          extends `AWS::CloudWatch::Alarm::Unit`
  case object Bits               extends `AWS::CloudWatch::Alarm::Unit`
  case object Kilobits           extends `AWS::CloudWatch::Alarm::Unit`
  case object Megabits           extends `AWS::CloudWatch::Alarm::Unit`
  case object Gigabits           extends `AWS::CloudWatch::Alarm::Unit`
  case object Terabits           extends `AWS::CloudWatch::Alarm::Unit`
  case object Percent            extends `AWS::CloudWatch::Alarm::Unit`
  case object Count              extends `AWS::CloudWatch::Alarm::Unit`
  case object `Bytes/Second`     extends `AWS::CloudWatch::Alarm::Unit`
  case object `Kilobytes/Second` extends `AWS::CloudWatch::Alarm::Unit`
  case object `Megabytes/Second` extends `AWS::CloudWatch::Alarm::Unit`
  case object `Gigabytes/Second` extends `AWS::CloudWatch::Alarm::Unit`
  case object `Terabytes/Second` extends `AWS::CloudWatch::Alarm::Unit`
  case object `Bits/Second`      extends `AWS::CloudWatch::Alarm::Unit`
  case object `Kilobits/Second`  extends `AWS::CloudWatch::Alarm::Unit`
  case object `Megabits/Second`  extends `AWS::CloudWatch::Alarm::Unit`
  case object `Gigabits/Second`  extends `AWS::CloudWatch::Alarm::Unit`
  case object `Terabits/Second`  extends `AWS::CloudWatch::Alarm::Unit`
  case object `Count/Second`     extends `AWS::CloudWatch::Alarm::Unit`
  case object UnitNone           extends `AWS::CloudWatch::Alarm::Unit`
  val values = Seq(
    Seconds,
    Microseconds,
    Milliseconds,
    Bytes,
    Kilobytes,
    Megabytes,
    Gigabytes,
    Terabytes,
    Bits,
    Kilobits,
    Megabits,
    Gigabits,
    Terabits,
    Percent,
    Count,
    `Bytes/Second`,
    `Kilobytes/Second`,
    `Megabytes/Second`,
    `Gigabytes/Second`,
    `Terabytes/Second`,
    `Bits/Second`,
    `Kilobits/Second`,
    `Megabits/Second`,
    `Gigabits/Second`,
    `Terabits/Second`,
    `Count/Second`,
    UnitNone
  )
  implicit val format: JsonFormat[`AWS::CloudWatch::Alarm::Unit`] =
    new EnumFormat[`AWS::CloudWatch::Alarm::Unit`](values)
}

sealed trait MissingDataTreatment
object MissingDataTreatment extends DefaultJsonProtocol {
  case object breaching extends MissingDataTreatment
  case object notBreaching extends MissingDataTreatment
  case object ignore extends MissingDataTreatment
  case object missing extends MissingDataTreatment

  val values = Seq(
    breaching,
    notBreaching,
    ignore,
    missing
  )

  implicit val format: EnumFormat[MissingDataTreatment] =
    new EnumFormat[MissingDataTreatment](values)
}
