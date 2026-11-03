
void FUN_ffff0000be00666c(undefined4 *param_1,uint *param_2,char *param_3)

{
  uint uVar1;
  long lVar2;
  bool bVar3;
  uint local_14;
  
  local_14 = 0;
  if (DAT_ffff0000be0d5938 == 0) {
    FUN_ffff0000be0057e0(param_1,&local_14,0);
    lVar2 = DAT_ffff0000be0d5930;
    local_14 = local_14 & 0x7fff;
    bVar3 = DAT_ffff0000be0d59e0 != '\0';
    *param_3 = DAT_ffff0000be0d59e0;
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


