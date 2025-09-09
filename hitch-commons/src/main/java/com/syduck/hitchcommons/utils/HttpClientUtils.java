package com.syduck.hitchcommons.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.NameValuePair;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * HttpClient工具类
 *
 * @author wangmx
 */
public class HttpClientUtils {
    private final static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
    private final static PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
    private final static RequestConfig requestConfig;
    private static final int MAX_TOTAL = 100;
    private static final int MAX_TIMEOUT = 7000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 40000;
    private static final String CHARSET = "UTF-8";

    public HttpClientUtils() {
    }

    public static String doGet(String url) throws Exception {
        return doGet(url, new HashMap());
    }

    public static String doGet(String url, Map params) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doGet url is null or '' ");
        } else {
            List pairList = new ArrayList(params.size());

            for (Object object : params.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                NameValuePair pair = new BasicNameValuePair((String) entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }

            CloseableHttpResponse response = null;
            InputStream instream = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();

            try {
                URIBuilder URIBuilder = new URIBuilder(url);
                URIBuilder.addParameters(pairList);
                URI uri = URIBuilder.build();
                HttpGet httpGet = new HttpGet(uri);
                LOG.info("uri:{}", uri);
                response = httpclient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doGet statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var16) {
                LOG.error("doGet  IO ERROR :{}", var16.getMessage());
            } catch (URISyntaxException var17) {
                LOG.error("doGet URISyntaxException :{}", var17.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpclient) {
                    httpclient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

        }
        return result;
    }

    public static String doGet(String url, Map params, String charset) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doGet url is null or '' ");
        } else {
            List pairList = new ArrayList(params.size());

            for (Object object : params.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                NameValuePair pair = new BasicNameValuePair((String) entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }

            CloseableHttpResponse response = null;
            InputStream instream = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();

            try {
                URIBuilder URIBuilder = new URIBuilder(url);
                URIBuilder.addParameters(pairList);
                URI uri = URIBuilder.build();
                HttpGet httpGet = new HttpGet(uri);
                response = httpclient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doGet statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, charset);
                }
            } catch (IOException var17) {
                LOG.error("doGet  IO ERROR :{}", var17.getMessage());
            } catch (URISyntaxException var18) {
                LOG.error("doGet URISyntaxException :{}", var18.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpclient) {
                    httpclient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

        }
        return result;
    }

    public static String doPost(String apiUrl) throws Exception {
        return doPost(apiUrl, new HashMap());
    }

    public static String doPost(String url, Map params) throws Exception {
        String result = null;
        StringBuilder param = new StringBuilder();
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doPost url is null or '' ");
        } else {
            List pairList = new ArrayList(params.size());

            for (Object object : params.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                NameValuePair pair = new BasicNameValuePair((String) entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
                if (param.isEmpty()) {
                    param = new StringBuilder(entry.getKey() + "=" + entry.getValue());
                } else {
                    param.append("&").append((String) entry.getKey()).append("=").append(entry.getValue());
                }
            }

            LOG.info("http请求地址:" + url + "?" + param);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                httpPost.setEntity(new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8));
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                    LOG.info("doPost Result:{}", result);
                }
            } catch (IOException var14) {
                LOG.error("doPost  ERROR :{}", var14.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

        }
        return result;
    }

    public static String doPost(String url, String xml) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doPost url is null or '' ");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                LOG.info("短信请求服务器地址:" + url + "?" + xml);
                httpPost.setConfig(requestConfig);
                httpPost.setEntity(new StringEntity(xml, "GBK"));
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var12) {
                LOG.error("doPost  ERROR :{}", var12.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

        }
        return result;
    }

    public static String doPost(String url, Object json) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doPostByJson url is null or '' ");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var13) {
                LOG.error("doPost BY JSON ERROR :{}", var13.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

            }

        }
        return result;
    }

    public static String doPostPay(String url, Object json) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doPostByJson url is null or '' ");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var13) {
                LOG.error("doPost BY JSON ERROR :{}", var13.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

            }

        }
        return result;
    }

    public static String doPostSSL(String apiUrl, Map params) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(apiUrl)) {
            LOG.info("warn:doPostSSL url is null or '' ");
        } else {
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
            HttpPost httpPost = new HttpPost(apiUrl);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                List pairList = new ArrayList(params.size());
                Iterator var8 = params.entrySet().iterator();

                Map.Entry entry;
                while (var8.hasNext()) {
                    entry = (Map.Entry) var8.next();
                    NameValuePair pair = new BasicNameValuePair((String) entry.getKey(), entry.getValue().toString());
                    pairList.add(pair);
                }

                httpPost.setEntity(new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8));
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    LOG.info("doPostSSL statusCode:{}", statusCode);
                    return String.valueOf((Object) null);
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (Exception var14) {
                LOG.error("doPostSSL ERROR :{}", var14.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

        }
        return result;
    }

    public static String doPostSSL(String apiUrl, Object json) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(apiUrl)) {
            LOG.info("warn:doPostSSL By Json url is null or '' ");
            return null;
        } else {
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
            HttpPost httpPost = new HttpPost(apiUrl);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            HttpEntity entity;
            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    entity = response.getEntity();
                    if (entity != null) {
                        instream = entity.getContent();
                        result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                    }

                    return result;
                }

                LOG.info("doPostSSL by json statusCode:{}", statusCode);
            } catch (Exception var13) {
                LOG.error("doPostSSL BY JSON ERROR :{}", var13.getMessage());
                return null;
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

                LOG.info("close  instream response httpClient  connection succ");
            }

            return String.valueOf((Object) null);
        }
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;

        try {
            SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial(null, (chain, authType) -> true).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) {
                }

                public void verify(String host, X509Certificate cert) {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) {
                }
            });
        } catch (GeneralSecurityException var2) {
            LOG.error("createSSLConnSocketFactory ERROR :{}", var2.getMessage());
        }

        return sslsf;
    }

    public static String doPostPay(String url, Object json, String authorization) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("warn:doPostByJson url is null or '' ");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Authorization", authorization);
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("doPost statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var14) {
                LOG.error("doPost BY JSON ERROR :{}", var14.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

            }

        }
        return result;
    }

    public static String doPostPayUpgraded(String url, Object json, String authorization) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {
            LOG.info("新支付接口url不能为空！");
        } else {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {
                httpPost.setConfig(requestConfig);
                StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                httpPost.setHeader("Authorization", authorization);
                stringEntity.setContentEncoding("UTF-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                LOG.info("新支付请求状态 statusCode:{}", statusCode);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException var14) {
                LOG.error("新支付接口发送异常:{}", var14.getMessage());
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

            }

        }
        return result;
    }

    static {
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(100);
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(10000);
        configBuilder.setSocketTimeout(40000);
        configBuilder.setConnectionRequestTimeout(7000);
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }
}
