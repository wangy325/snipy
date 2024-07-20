"""
Author: wangy325
Date: 2024-07-19 17:57:35
Description: 
"""
from django.urls import path
from . import views

urlpatterns = [
    path("", views.index, name="index")
]


