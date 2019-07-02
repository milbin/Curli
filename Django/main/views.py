from django.shortcuts import render
from .models import user

# Create your views here.
def get_user(request):
    result = {}
    user.objects.filter(user_username=request.username)