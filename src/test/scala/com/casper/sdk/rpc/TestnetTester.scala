package com.casper.sdk.rpc

import com.casper.sdk.crypto.KeyPair
import com.casper.sdk.crypto.hash.{Blake2b256, Hash}
import com.casper.sdk.{CasperSdk, domain}
import com.casper.sdk.domain.deploy.{Deploy, DeployExecutable, DeployNamedArg, ModuleBytes, StoredContractByHash, StoredVersionedContractByHash, StoredVersionedContractByName, _}
import com.casper.sdk.domain.{EraSummary, Peer, deploy, _}
import com.casper.sdk.json.serialize.TimeStampSerializer
import com.casper.sdk.serialization.cltypes.CLValueByteSerializer
import com.casper.sdk.serialization.domain.deploy.{DeployApprovalByteSerializer, DeployExecutableByteSerializer, DeployHeaderByteSerializer}
import com.casper.sdk.types.cltypes._
import com.casper.sdk.util.implicits.idInstance
import com.casper.sdk.util.{ByteUtils, HexUtils, JsonConverter, TimeUtil}
import org.bouncycastle.crypto.KeyGenerationParameters
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMWriter
import org.scalactic.Prettifier.default

import java.io.{File, FileWriter, StringWriter}
import java.math.BigInteger
import java.net.URL
import java.nio
import java.nio.charset.StandardCharsets
import java.security.spec.ECGenParameterSpec
import java.security._
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}
import java.util.regex.Pattern
import scala.collection.mutable.ArrayBuilder
import scala.io.Source
import scala.math.BigInt.int2bigInt

object TestnetTester extends App {

val  bb = Array[Byte](125,-102,-96,-72,100,19,-41,-1,  -102,
  -111,105,24,44,83,-16,-70,-54,-88,13,52,-62,17,-83,-85,0,126,-44,
  -121, 106,-15,112, 119)

val pp = new CLPublicKey(bb,KeyAlgorithm.ED25519)

println(pp.formatAsHexAccount.get)

 println( CLPublicKey("017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077").get.bytes)
val a  = CLPublicKey("017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077").get.bytes

  a.foreach(println)

  val x = JsonConverter.fromJson[Hash]("\"babc9370c6bf12c8ecb5f86490cef813f9aaa8719324f6f0319ce5dc574a58cc\"")
  println(x.toString)
  /*
  val trs = Deploy.transfer(CLPublicKey("017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077").get,
    CLPublicKey("0196948158bf5b35c0c84f680f110b8debaa4e7628e13ba336a95651a214d3b9bd").get,
    10000000000L,300000000L,"casper-test",
    1,1,1800000)

    val tr = DeployTransfer(40000000000L,new AccountHash("account-hash-f2d3278a8d24837f23156b812d72ab7ae5ea81467efe2fb718e292756c88cd76"),1)


  val ff = CLValue.Key("transfer-e330a31701205e3871cb4f7e14d3ff26074735c84b0e54b7a75f553a8405d182")
  println(JsonConverter.toJson(trs))

    println("/////////////////////////////////////////////////////////////////////////////////////")
  val kk = CLKeyValue("transfer-e330a31701205e3871cb4f7e14d3ff26074735c84b0e54b7a75f553a8405d182")
   println(kk.keyType)

    val list = CLValue.List(CLValue.String("String1"),CLValue.String("String2"),CLValue.String("String3"),CLValue.String("String4"))
    println(JsonConverter.toJson(list))

    val oo = """{
               |  "cl_type" : {
               |    "List" : "String"
               |  },
               |  "bytes" : "0400000007000000537472696e673107000000537472696e673207000000537472696e673307000000537472696e6734",
               |  "parsed" : "[\"String1\",\"String2\",\"String3\",\"String1\"]"
               |}""".stripMargin


    println(JsonConverter.fromJson[CLValue](oo).parsed)






    println(JsonConverter.toJson(dp.session))
    val ss1 = CLValue.U512(BigInt.apply("1024"))
    println(HexUtils.toHex(ss1.bytes).get)
  */

  val client = new CasperSdk("http://65.21.227.180:7777/rpc")
  val dp = client.getDeploy("0fe0adccf645e99b9b58493c843516cd354b189e1c3efe62c4f2768716a41932")

  val header = new DeployHeader(
    CLPublicKey("017d9aa0b86413d7ff9a9169182c53f0bacaa80d34c211adab007ed4876af17077").get,
    System.currentTimeMillis(),
    5400000L,
    1,
    None,
    Seq.empty,
    "casper-test"
  )

  println(JsonConverter.toJson(header))

  val sss = new DeployHeaderByteSerializer()
  println(HexUtils.toHex(sss.toBytes(header)).get)


  val arg0 = new DeployNamedArg("amount", CLValue.U512(5000000000L))
  val payment = new ModuleBytes(
    "".getBytes()
    , Seq(Seq(arg0)))

  val arg1 = new DeployNamedArg("amount", CLValue.U512(5000000000L))
  val arg01 = new DeployNamedArg("target",
    CLValue.PublicKey("014d8c494ae85bda4288d0ed02c6bb180ec84efd3d7af0ed6ab9092d8757441427")
  )

  val arg02 = new DeployNamedArg("id",
    CLValue.Option(CLValue.U64(999L))
  )


  val session = new DeployTransfer(Seq(Seq(arg1, arg01, arg02)))
  //println(JsonConverter.toJson(header))


  val session1 = new StoredVersionedContractByName("test", Some(12542), "entry_point", Seq(Seq(arg1, arg01, arg02)))

  val pair = com.casper.sdk.crypto.KeyPair.loadFromPem("/Users/p35862/testnet.pem")

  val serializer = DeployExecutableByteSerializer()
  val builder = new ArrayBuilder.ofByte
  //  builder.addAll(serializer.toBytes(payment)).addAll(serializer.toBytes(session))
  println("SSSSSSS  " + HexUtils.toHex(Deploy.deployBodyHash(payment, session)).get)
  val mm = com.casper.sdk.crypto.KeyPair.loadFromPem("/Users/p35862/pppp.pem")


  val deploy = Deploy.createUnsignedDeploy(header, payment, session)
  println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////")
  println(HexUtils.toHex(Blake2b256.hash(Deploy.deployHeaderHash(deploy.header))).get)
  val signedDeploy = Deploy.signDeploy(deploy, mm)
  println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////")
  println(JsonConverter.toJson(signedDeploy))
  //println(client.putDeploy(signedDeploy).toString)


  /*
    val hexSECP256K1 = "0203e7d5b66b2fd0f66fb0efcceecb673b3762595b30ae1cac48ae8f09d34c952ee4"
    val keyESECP256K1 = new CLPublicKey(hexSECP256K1)
    val header  = new DeployHeader(keyESECP256K1,"1800000","1800000",1,null,null,"casper-test")
    val paiment = new ModuleBytes()
    val session = new StoredContractByHash()
    val deploy = Deploy.createUnsignedDeploy()
  */


  /*
    import org.bouncycastle.jce.provider.BouncyCastleProvider
    import java.security.Security
  //  Security.addProvider(new BouncyCastleProvider())
    //Security.addProvider(new BouncyCastleProvider)
    pemParser.close


    val provider = new BouncyCastleProvider()
    val converter = new JcaPEMKeyConverter().setProvider(provider);

    //val converter = new JcaPEMKeyConverter().setProvider("BC")
    if (obj.isInstanceOf[SubjectPublicKeyInfo]) {
      val bytes = obj.asInstanceOf[SubjectPublicKeyInfo].getEncoded()
      val algo = converter.getPublicKey(obj.asInstanceOf[SubjectPublicKeyInfo]).getAlgorithm

      algo match {
        case "Ed25519" => new CLPublicKey(bytes, KeyAlgorithm.ED25519)
        case "ECDSA" => new CLPublicKey(bytes, KeyAlgorithm.SECP256K1)
      }
    }
    else throw new IllegalArgumentException("not a public key file PEM")


  */


  /*
   val client = new CasperSdk("http://65.108.1.10:7777/rpc")

    val deploy = client.getDeploy("5545207665f6837F44a6BCC274319280B73a6f0997F957A993e60f878A736678")



   val fileWriter = new FileWriter(new File("auction.json"))
   fileWriter.write(JsonConverter.toJson(client.getAuctionInfo("9F2D38d6b6d139DAA70fEF85FE7f1907A1ce055ca87e260995E4B84D161d42E5")))
   fileWriter.close()
   */


  /*
    println("sd "+deploy.payment.getClass)
    println("de "+deploy)

    val str = """[["amount",{"cl_type":"U512","bytes":"04005670E3","parsed":"3815790080"}]] """

   val l= JsonConverter.fromJson[Seq[Seq[DeployNamedArg]]](str)


    println(l)


    val storedValue = client.getStateItem("30cE5146268305AeeFdCC05a5f7bE7aa6dAF187937Eed9BB55Af90e1D49B7956","account-hash-46dE97966cfc2F00C326e654baD000AB7a5E26bEBc316EF4D74715335cF32A88",Seq.empty)
    client.getBalance("30cE5146268305AeeFdCC05a5f7bE7aa6dAF187937Eed9BB55Af90e1D49B7956",new URef("uref-9cC6877ft07c211e44068D5dCc2cC28A67Cb582C3e239E83Bb0c3d067C4D0363-007"))

    assert(storedValue.Contract == null)

    assert(storedValue.Account != null)

    println("kkkk"+storedValue.Account.main_purse.format.toLowerCase)
    assert(storedValue.Account.main_purse.format.toLowerCase== "uref-9cC68775d07c211e44068D5dCc2cC28A67Cb582C3e239E83Bb0c3d067C4D0363-007".toLowerCase)




    val u512 =
      """  {
        |                                "cl_type": "U512",
        |                                "bytes": "0400ca9A3B",
        |                                "parsed": "1000000000"
        |                            }""".stripMargin

    val value = JsonConverter.fromJson[CLValue](u512)

    assert(value != null)

    assert(value.parsed == "1000000000")

    assert(value.cl_infoType.cl_Type == CLType.U512)

    assert(value.bytes.sameElements(HexUtils.fromHex("0400ca9A3B")))




    val hexkey =  "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"
    val jsonkey =  """ "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537" """
    println(KeyAlgorithm.matchKeyWithAlgo(KeyAlgorithm.ED25519,hexkey))
    val pubKey = JsonConverter.fromJson[CLPublicKey](jsonkey)
    println(pubKey.formatAsHexAccount)
    assert(pubKey!=null)

    val p = new CLPublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")
    assert(pubKey.bytes.sameElements(p.bytes))
    println(p.formatAsHexAccount)

   //val b = BigInt.apply("123456789101112131415")// .int2bigInt(b)
   //var bytes = b.toByteArray

  val b = CLValue.I64(-4700)//

  println(HexUtils.toHex(b.bytes))

  val str = """ {
              |    "hash": "01da3c604f71e0e7df83ff1ab4ef15bb04de64ca02e3d2b78de6950e8b5ee187",
              |    "header": {
              |        "account": "01d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900c",
              |        "timestamp": "1605573564072",
              |        "ttl": "3600000",
              |        "gas_price": 1,
              |        "body_hash": "4811966d37fe5674a8af4001884ea0d9042d1c06668da0c963769c3a01ebd08f",
              |        "dependencies": [
              |            "0101010101010101010101010101010101010101010101010101010101010101"
              |        ],
              |        "chain_name": "casper-example"
              |    },
              |    "payment": {
              |        "StoredContractByName": {
              |        "name": "casper-example",
              |        "entry_point": "example-entry-point",
              |        "args": [
              |            [
              |                "quantity",
              |                {
              |                    "cl_type": "I32",
              |                    "bytes": "e8030000",
              |                    "parsed": 1000
              |                }
              |            ]
              |        ]
              |        }
              |    },
              |    "session": {
              |        "Transfer": {
              |        "args": [
              |            [
              |                "amount",
              |                {
              |                    "cl_type": "I32",
              |                    "bytes": "e8030000",
              |                    "parsed": 1000
              |                }
              |            ]
              |        ]
              |        }
              |    },
              |    "approvals": [
              |        {
              |            "signer": "01d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900c",
              |            "signature": "012dbf03817a51794a8e19e0724884075e6d1fbec326b766ecfa6658b41f81290da85e23b24e88b1c8d9761185c961daee1adab0649912a6477bcd2e69bd91bd08"
              |        }
              |    ]
              |}""".stripMargin




  val str1= """{
              |        "StoredContractByName": {
              |        "name": "casper-example",
              |        "entry_point": "example-entry-point",
              |        "args": [
              |            [
              |                "quantity",
              |                {
              |                    "cl_type": "I32",
              |                    "bytes": "e8030000",
              |                    "parsed": 1000
              |                }
              |            ]
              |        ]
              |        }
              |    }""".stripMargin



  val deploy = JsonConverter.fromJson[Deploy](str)
    println(deploy)
  val deployApprovalByteSerializer = new DeployByteSerializer()

   // deployApprovalByteSerializer.toBytes(deploy)

  println(HexUtils.toHex(deployApprovalByteSerializer.toBytes(deploy)))

    //println("B-"+HexUtils.toHex(CLValue.String(storedContractByName.entry_point).bytes))


    println(TimeUtil.ToEpochMs("2020-11-17T00:39:24.072Z"))
    import com.casper.sdk.serialization.cltypes.CLValueByteSerializer

    val cLValueByteSerializer = new CLValueByteSerializer()

    val clValue = CLValue.List()

    println(HexUtils.toHex(clValue.bytes))

    var bytes = cLValueByteSerializer.toBytes(clValue)

    println(HexUtils.toHex(bytes))

  //  assert("08000000000000000000000003"== HexUtils.toHex(bytes))


    val serializer = new DeployApprovalByteSerializer()
    val signer= new CLPublicKey("01d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900c")
    val signature= new Signature("012dbf03817a51794a8e19e0724884075e6d1fbec326b766ecfa6658b41f81290da85e23b24e88b1c8d9761185c961daee1adab0649912a6477bcd2e69bd91bd08")

    val approval = new DeployApproval(signer,signature)

    bytes = serializer.toBytes(approval)

    println(HexUtils.toHex(bytes))

    val ser = new CLPublicKeySerializer()
    val pbukey= new CLPublicKey("01d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900c")

    bytes = ser.toBytes(pbukey)

    val ipaddr = Array[Byte](192.toByte, 168.toByte, 1, 9)


    val kk = Array[Byte](1, -39, -65, 33, 72, 116, -118, -123, -56, -99, -91, -86, -40, -18, 11, 15, -62, -47, 5, -3, 57, -44, 26, 76, 121, 101, 54, 53, 79, 10, -30, -112, 12)


    val srr = new DeployNamedArgByteSerializer()

    var arg = new DeployNamedArg("test",CLValue.String("Hello, World!"))


    println(HexUtils.toHex(srr.toBytes(arg)))


    val builder = new ArrayBuilder.ofByte
    builder.addAll(CLValue.U32("test".getBytes().length).bytes)
      .addAll("test".getBytes())
     // .addAll(new CLValueByteSerializer().toBytes(CLValue.String("Hello, World!")))


    builder .addAll(new CLValueByteSerializer().toBytes(new URef("uref-9cC68775d07c211e44068D5dCc2cC28A67Cb582C3e239E83Bb0c3d067C4D0363-007")))
    var xxx = new DeployNamedArg("test",new URef("uref-9cC68775d07c211e44068D5dCc2cC28A67Cb582C3e239E83Bb0c3d067C4D0363-007"))
    assert(srr.toBytes(xxx).sameElements(builder.result()))

  */
}
