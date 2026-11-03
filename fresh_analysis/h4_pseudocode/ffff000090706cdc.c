
void FUN_ffff000090706cdc(undefined4 *param_1,uint *param_2,char *param_3)

{
  uint uVar1;
  long lVar2;
  bool bVar3;
  uint local_14;
  
  local_14 = 0;
  if (DAT_ffff0000908168e8 == 0) {
    FUN_ffff000090705e50(param_1,&local_14,0);
    lVar2 = DAT_ffff0000908168e0;
    local_14 = local_14 & 0x7fff;
    bVar3 = DAT_ffff000090816990 != '\0';
    *param_3 = DAT_ffff000090816990;
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


