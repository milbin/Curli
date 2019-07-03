from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

class NewUserForm(UserCreationForm):
    mail = forms.EmailField(required=True)

    class Meta:
        model = User
        fields = ("id", "username", "password1", "password2")

    def save(self, commit=True):
        user = super(NewUserForm, self).save(commit=False)
        if commit:
            user.save()
        return user