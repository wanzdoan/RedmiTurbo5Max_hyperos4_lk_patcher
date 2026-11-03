// ffff00009077eab8  size=48  FUN_ffff00009077eab8

char * FUN_ffff00009077eab8(char *param_1,char param_2,long param_3)

{
  while( true ) {
    if (param_3 == 0) {
      return (char *)0x0;
    }
    if (*param_1 == param_2) break;
    param_1 = param_1 + 1;
    param_3 = param_3 + -1;
  }
  return param_1;
}


