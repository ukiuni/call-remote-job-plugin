package org.ukiuni.callOtherJenkins.CallOtherJenkins;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.ukiuni.callOtherJenkins.CallOtherJenkins.JenkinsRemoteIF.LastCompleteBuild;

/**
 * Call other jenkins job
 * 
 * @author ukiuni
 */
public class CallOtherJenkinsBuilder extends Builder {

	private final String hostName;
	private final String jobName;
	private final String span;
	private final String retry;
	private final String userName;
	private final String password;
	private final String parameters;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public CallOtherJenkinsBuilder(String hostName, String jobName, String span, String retry, String userName, String password, String parameters) {
		this.hostName = hostName;
		this.jobName = jobName;
		this.span = span;
		this.retry = retry;
		this.userName = userName;
		this.password = password;
		this.parameters = parameters;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * We'll use this from the <tt>config.jelly</tt>.
	 */
	public String getHostName() {
		return hostName;
	}

	public String getJobName() {
		return jobName;
	}

	public String getSpan() {
		return span;
	}

	public String getRetry() {
		return retry;
	}

	@Override
	public boolean perform(@SuppressWarnings("rawtypes") AbstractBuild build, Launcher launcher, BuildListener listener) {
		try {
			JenkinsRemoteIF jenkinsRemoteIF = new JenkinsRemoteIF(getHostName(), getJobName(), getDescriptor().getHttps());
			if (null != getUserName() && !"".equals(getUserName())) {
				jenkinsRemoteIF.setAuthentication(getUserName(), getPassword());
			}
			if (null != getParameters() && !"".equals(getParameters())) {
				jenkinsRemoteIF.setParameters(getParameters());
			}
			long nextBuildNumber = jenkinsRemoteIF.loadNextBuildNumber(listener.getLogger());
			jenkinsRemoteIF.exec(listener.getLogger());
			LastCompleteBuild lastCompleteBuild = jenkinsRemoteIF.seekEnd(listener.getLogger(), nextBuildNumber, Long.valueOf(getSpan()), Long.valueOf(getRetry()));
			listener.getLogger().println("build number " + lastCompleteBuild.number + " :success ? " + lastCompleteBuild.success);
			return lastCompleteBuild.success;
		} catch (Exception e) {
			e.printStackTrace(listener.getLogger());
			return false;
		}
	}

	// Overridden for better type safety.
	// If your plugin doesn't really define any property on Descriptor,
	// you don't have to do this.
	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	public String getParameters() {
		return parameters;
	}

	/**
	 * Descriptor for {@link CallOtherJenkinsBuilder}. Used as a singleton. The
	 * class is marked as public so that it can be accessed from views.
	 * 
	 * <p>
	 * See
	 * <tt>src/main/resources/org/ukiuni/callOtherJenkins/CallOtherJenkinsBuilder/*.jelly</tt>
	 * for the actual HTML fragment for the configuration screen.
	 */
	@Extension
	// This indicates to Jenkins that this is an implementation of an extension
	// point.
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 * 
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */
		private boolean https;

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 * 
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the
		 *         browser.
		 */
		public FormValidation doCheckSpan(@QueryParameter String value) throws IOException, ServletException {
			try {
				Long.parseLong(value);
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error("Please enter number");
			}
			// return FormValidation.warning("Isn't the name too short?");
		}

		public FormValidation doCheckRetry(@QueryParameter String value) throws IOException, ServletException {
			try {
				Long.parseLong(value);
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error("Please enter number");
			}
			// return FormValidation.warning("Isn't the name too short?");
		}

		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> aClass) {
			// Indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "Call remote jenkins job";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			https = formData.getBoolean("https");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

		/**
		 * This method returns true if the global configuration says we should
		 * speak French.
		 * 
		 * The method name is bit awkward because global.jelly calls this method
		 * to determine the initial state of the checkbox by the naming
		 * convention.
		 */
		public boolean getHttps() {
			return https;
		}
	}
}
