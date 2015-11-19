package org.motechproject.ivr.metric;

import com.codahale.metrics.health.HealthCheck;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class IvrHealthCheck extends HealthCheck {
    private String uri;
    private boolean isAuthRequired;
    private String username;
    private String password;

    public IvrHealthCheck(String uri, boolean isAuthRequired, String username, String password) {
        this.uri = uri;
        this.isAuthRequired = isAuthRequired;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Result check() throws IOException {
        HttpUriRequest request = new HttpGet(uri);

        if (isAuthRequired) {
            request.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(username, password),
                    "UTF-8",
                    false));

        }

        HttpResponse response = new DefaultHttpClient().execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return Result.healthy();
        }

        return Result.unhealthy("Could not ping IVR provider");
    }
}
