/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.showcase.twitter;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TwitterTimelineController {

	private final Provider<TwitterApi> twitterApiProvider;
	
	@Inject
	public TwitterTimelineController(Provider<TwitterApi> twitterApiProvider) {
		this.twitterApiProvider = twitterApiProvider;
	}
	
	@RequestMapping(value="/twitter/timeline", method=RequestMethod.GET)
	public String showTimeline(Model model) {
		return showTimeline("Home", model);
	}
	
	@RequestMapping(value="/twitter/timeline/{timelineType}", method=RequestMethod.GET)
	public String showTimeline(@PathVariable("timelineType") String timelineType, Model model) {
		TwitterApi twitterApi = getTwitterApi();
		if (timelineType.equals("Home")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getHomeTimeline());
		} else if(timelineType.equals("Public")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getPublicTimeline());
		} else if(timelineType.equals("Friends")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getFriendsTimeline());
		} else if(timelineType.equals("User")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getUserTimeline());
		} else if(timelineType.equals("Mentions")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getMentions());
		} else if(timelineType.equals("Favorites")) {
			model.addAttribute("timeline", twitterApi.timelineOperations().getFavorites());
		}
		model.addAttribute("timelineName", timelineType);
		return "twitter/timeline";
	}
	

	@RequestMapping(value="/twitter/tweet", method=RequestMethod.POST)
	public String postTweet(String message) {
		getTwitterApi().timelineOperations().updateStatus(message);
		return "redirect:/twitter";
	}

	private TwitterApi getTwitterApi() {
		return twitterApiProvider.get();
	}
	
}