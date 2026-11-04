
int FUN_ffff00009077f990(char *param_1,char *param_2)

{
  char cVar1;
  char cVar2;

  do {
    cVar1 = *param_1;
    cVar2 = *param_2;
    if (cVar1 == '\0') break;
    param_1 = param_1 + 1;
    param_2 = param_2 + 1;
  } while (cVar1 == cVar2);
  return (int)(char)(cVar1 - cVar2);
}
