package com.fredrik.bookitinfra.action;

import java.io.IOException;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tools to get application version info.
 */
public class AppInstanceVersions {

	public static void main(String[] args) {

		getAppInstanceVersions();
	}

	public static void getAppInstanceVersions() {
		
		info("");
		info("");
		info("Listing information about Application environments:");
		info("");

		requestSpringActuatorInfo("localdev", 	"http://localhost:8888/actuator/info");
		requestSpringActuatorInfo("test", 		"http://tullockautomation.asuscomm.com:8886/actuator/info");
		requestSpringActuatorInfo("prod", 		"http://tullockautomation.asuscomm.com:8888/actuator/info");		
	}
	
	private static void requestSpringActuatorInfo(String env, String url) {

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse getResponse = null;
		String version = "Unavailable";

		try {
//          SSLContext sslContext = new SSLContextBuilder()
//                  .loadTrustMaterial(null, (certificate, authType) -> true).build();

			httpclient = HttpClients.custom()
//                  .setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

			HttpGet httpGet = new HttpGet(url);

			info(env + ": (" + httpGet.getURI() + "):");

			getResponse = httpclient.execute(httpGet);
			// The underlying HTTP connection is still held by the response object
			// to allow the response content to be streamed directly from the network
			// socket.
			// In order to ensure correct deallocation of system resources
			// the user MUST call CloseableHttpResponse#close() from a finally clause.
			// Please note that if response content is not fully consumed the underlying
			// connection cannot be safely re-used and will be shut down and discarded
			// by the connection manager.

			HttpEntity entity1 = getResponse.getEntity();

			String entityContents = EntityUtils.toString(getResponse.getEntity());
			ObjectMapper jsonmapper = new ObjectMapper();
			JsonNode json = jsonmapper.readTree(entityContents);
			
			version = json.get("app").get("version").asText();

			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);

		} catch (IOException e) {
//			 e.printStackTrace();
//      } catch (KeyManagementException e) {
//          //            e.printStackTrace();
//      } catch (NoSuchAlgorithmException e) {
//          //            e.printStackTrace();
//      } catch (KeyStoreException e) {
//          //            e.printStackTrace();
		} finally {
			try {
				if (Objects.nonNull(getResponse)) {
					getResponse.close();
				}
				if (Objects.nonNull(httpclient)) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			info(version);
			info("");
		}

	}

	private static void info(String string) {

		System.out.println("" + string);
	}

}
