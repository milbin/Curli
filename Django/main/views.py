from django.shortcuts import render
from django.contrib.auth.models import User
from .serializer import UserSerializer
from rest_framework.renderers import JSONRenderer
from .forms import NewUserForm
# Create your views here.
def get_user(request):
    user_object = User.objects.filter(user_username=request.username)

    result = UserSerializer(user_object)
    result = JSONRenderer().render(result)

    return result

def register(request):
    form = NewUserForm(request.POST)
    if form.is_valid():
        form.save()
    return HttpRespons