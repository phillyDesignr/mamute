package org.mamute.brutauth.auth.rules;

import static org.mamute.auth.rules.PermissionRulesConstants.INACTIVE_QUESTION;
import static org.mamute.auth.rules.Rules.hasKarma;

import javax.inject.Inject;

import org.mamute.dao.QuestionDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class InactiveQuestionRequiresMoreKarmaRule implements CustomBrutauthRule{

	@Inject public LoggedUser user;
	@Inject public ModelUrlMapping urlMapping;
	@Inject public QuestionDAO questions;
	
	public boolean isAllowed(Question question, String onWhat, Long id){
		Class<?> interactedType = urlMapping.getClassFor(onWhat);
		if(question == null && interactedType.isAssignableFrom(Question.class)) {
			question = questions.getById(id);
		}
		
		if(question.isInactiveForOneMonth()) 
			return hasKarma(INACTIVE_QUESTION).isAllowed(user.getCurrent(), question);
		return true;
	}
}
