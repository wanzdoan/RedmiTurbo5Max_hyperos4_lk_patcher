// File offset entry: 0x9166c

int FUN_0209156c(char *param_1,char *param_2,int param_3)

{
  char cVar1;
  
  for (; param_3 != 0; param_3 = param_3 + -1) {
    cVar1 = *param_1 - *param_2;
    if ((*param_1 == '\0') || (cVar1 != '\0')) goto code_r0x020915a4;
    param_2 = param_2 + 1;
    param_1 = param_1 + 1;
  }
  cVar1 = '\0';
code_r0x020915a4:
  return (int)cVar1;
}


