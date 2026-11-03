// ffff000090706f04  size=128  FUN_ffff000090706f04

void FUN_ffff000090706f04(undefined4 *param_1,uint *param_2,char *param_3)

{
  uint uVar1;
  long lVar2;
  bool bVar3;
  uint local_14;
  
  local_14 = 0;
  if (DAT_ffff000090816990 == 0) {
    FUN_ffff000090705ef4(param_1,&local_14,1);
    lVar2 = DAT_ffff000090816988;
    local_14 = local_14 & 0x7fff;
    bVar3 = DAT_ffff000090816a38 != '\0';
    *param_3 = DAT_ffff000090816a38;
    uVar1 = -local_14;
    if (bVar3) {
      uVar1 = local_14;
    }
    *param_2 = uVar1;
    *(undefined4 *)(lVar2 + 8) = *param_1;
    *(uint *)(lVar2 + 0xc) = local_14;
  }
  return;
}


