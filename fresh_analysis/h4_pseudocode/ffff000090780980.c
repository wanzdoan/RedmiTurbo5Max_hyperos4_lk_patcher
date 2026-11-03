
void FUN_ffff000090780980(long param_1,ulong param_2,uint param_3)

{
  bool bVar1;
  char *pcVar2;
  char *pcVar3;
  
  pcVar2 = s_0123456789abcdef_ffff000090804c70;
  if ((param_3 & 0x100) != 0) {
    pcVar2 = s_0123456789ABCDEF_ffff000090804d30;
  }
  pcVar3 = (char *)(param_1 + 0x1f);
  *pcVar3 = '\0';
  do {
    pcVar3 = pcVar3 + -1;
    *pcVar3 = pcVar2[param_2 & 0xf];
    bVar1 = 0xf < param_2;
    param_2 = param_2 >> 4;
  } while (bVar1);
  return;
}


