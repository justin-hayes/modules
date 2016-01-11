package org.motechproject.ivr.metrics;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.motechproject.metrics.api.HealthCheck;
import org.motechproject.metrics.builder.HealthCheckResultBuilder;

import java.io.IOException;

public class IvrHealthCheck implements HealthCheck {
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
    public Result check() throws IOException {
        HttpUriRequest request = new HttpGet(uri);

        if (isAuthRequired) {
            request.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(username, password),
                    "UTF-8",
                    false));

        }

        HttpResponse response = new DefaultHttpClient().execute(request);

        if (response.getStatusLine().getStatusCode() == 200) {
            return HealthCheckResultBuilder.healthy();
        }

        return HealthCheckResultBuilder.unhealthy("Could not ping IVR provider");
    }
}
