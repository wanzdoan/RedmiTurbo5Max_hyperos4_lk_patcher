// ffff0000907806b8  size=68  FUN_ffff0000907806b8

void FUN_ffff0000907806b8(long param_1,ulong param_2,uint param_3)

{
  bool bVar1;
  char *pcVar2;
  char *pcVar3;
  
  pcVar2 = s_0123456789abcdef_ffff000090804a48;
  if ((param_3 & 0x100) != 0) {
    pcVar2 = s_0123456789ABCDEF_ffff000090804b08;
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


