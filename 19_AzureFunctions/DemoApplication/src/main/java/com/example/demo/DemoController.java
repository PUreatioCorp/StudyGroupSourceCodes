package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DemoController {

	// Jackson
	private static ObjectMapper jsonMapper = new ObjectMapper();
	// Azure Functions Url
	@Value("${azure.functions.base.url.search}")
	private String functionsSearchUrl;
	@Value("${azure.functions.base.url.search.id}")
	private String functionsSearchSpecifyIdUrl;
	@Value("${azure.functions.base.url.add}")
	private String functionsAddUrl;
	@Value("${azure.functions.base.url.update}")
	private String functionsUpdateUrl;
	@Value("${azure.functions.base.url.delete}")
	private String functionsDeleteUrl;
	
	/**
	 * 全メンバー検索処理
	 * 
	 * @return メンバー
	 * @throws URISyntaxException
	 */
	@GetMapping("/api")
	public List<Member> searchMembers() {

		List<Member> members = null;

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			// Azure Functionsにアクセスする。
			HttpGet request = new HttpGet(new URI(this.functionsSearchUrl));
			CloseableHttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				System.err.println(String.format("status err : %d, %s", response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase()));
			}

			// 検索結果を設定する。
			List<String> lines = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			}

			members = jsonMapper.readValue(String.join(System.lineSeparator(), lines),
					new TypeReference<List<Member>>() {
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return members;
	}

	/**
	 * メンバー検索処理
	 * 
	 * @param id メンバーID
	 * @return メンバー
	 */
	@GetMapping("/api/{id}")
	public List<Member> searchMember(@PathVariable String id) {

		List<Member> members = null;

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			// Azure Functionsにアクセスする。
			HttpGet request = new HttpGet(new URI(String.format(this.functionsSearchSpecifyIdUrl, id)));
			CloseableHttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				System.err.println(String.format("status err : %d, %s", response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase()));
			}

			// 検索結果を設定する。
			List<String> lines = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			}

			members = jsonMapper.readValue(String.join(System.lineSeparator(), lines),
					new TypeReference<List<Member>>() {
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return members;
	}

	/**
	 * メンバー登録処理
	 * 
	 * @param member 登録メンバー
	 */
	@PostMapping("/api/regist")
	public void addMember(@RequestBody Member member) {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			// Azure Functionsにアクセスする。
			HttpPost request = new HttpPost(new URI(this.functionsAddUrl));
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("name", member.getName()));
			params.add(new BasicNameValuePair("age", String.valueOf(member.getAge())));
			request.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
			CloseableHttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				System.err.println(String.format("status err : %d, %s", response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * メンバー編集処理
	 * 
	 * @param member 更新メンバー
	 */
	@PutMapping("/api/edit")
	public void editMember(@RequestBody Member member) {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			// Azure Functionsにアクセスする。
			HttpPut request = new HttpPut(new URI(this.functionsUpdateUrl));
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("id", member.getId()));
			params.add(new BasicNameValuePair("name", member.getName()));
			params.add(new BasicNameValuePair("age", String.valueOf(member.getAge())));
			request.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
			CloseableHttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				System.err.println(String.format("status err : %d, %s", response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * メンバー削除処理
	 * @param id 削除メンバーID
	 */
	@DeleteMapping("/api/delete/{id}")
	public void deleteMember(@PathVariable String id) {

		try (CloseableHttpClient client = HttpClients.createDefault()) {

			// Azure Functionsにアクセスする。
			HttpDelete request = new HttpDelete(new URI(String.format(this.functionsDeleteUrl, id)));
			CloseableHttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() != 200) {
				System.err.println(String.format("status err : %d, %s", response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
