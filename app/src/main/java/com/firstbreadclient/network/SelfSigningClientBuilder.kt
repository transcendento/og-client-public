package com.firstbreadclient.network

import com.firstbreadclient.network.OkHttpClientInstance.getOkHttpClientInstance
import okhttp3.OkHttpClient
import java.io.IOException
import java.net.URL
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

internal object SelfSigningClientBuilder {
    private const val CRT_URL = "https://order.ru/order.crt"

    fun createClient(): OkHttpClient? {
        var okHttpClient: OkHttpClient? = null
        try {
            val cf = CertificateFactory.getInstance("X.509")
            val cert = URL(CRT_URL).openStream()

            val ca = cf.generateCertificate(cert)
            cert.close()

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            val trustManagers = tmf.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + Arrays.toString(trustManagers) }

            val trustManager = trustManagers[0] as X509TrustManager

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)
            val sslSocketFactory = sslContext.socketFactory

            okHttpClient = getOkHttpClientInstance(sslSocketFactory, trustManager)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        return okHttpClient
    }
}