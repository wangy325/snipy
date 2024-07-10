#include <stdio.h>
#include <stdlib.h>

int myval;

int main(int argc, char *argv[])
{
    myval = atoi(argv[1]);
    int count = 10;
    while(count > 0){
        printf("myval is %d, loc 0x%lx\n", myval, (long)&myval);
        count--;
    }
}