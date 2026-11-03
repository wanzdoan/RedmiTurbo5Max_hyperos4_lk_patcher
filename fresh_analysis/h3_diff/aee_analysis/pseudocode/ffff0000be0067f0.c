
void FUN_ffff0000be0067f0(undefined4 *param_1,uint *param_2,char *param_3)

{
  uint uVar1;
  long lVar2;
  bool bVar3;
  uint local_14;
  
  local_14 = 0;
  if (DAT_ffff0000be0d59f0 == 0) {
    FUN_ffff0000be0057e0(param_1,&local_14,1);
    lVar2 = DAT_ffff0000be0d59e8;
    local_14 = local_14 & 0x7fff;
    bVar3 = DAT_ffff0000be0d5a98 != '\0';
    *param_3 = DAT_ffff0000be0d5a98;
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


