
void FUN_ffff0000b80050cc(uint param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_14;
  
  uVar1 = DAT_ffff0000b810c070;
  if (param_1 < 0x65) {
    uVar3 = 0;
  }
  else if (param_1 >> 3 < 0x20d) {
    uVar3 = (param_1 - 100 & 0xffff) * 0x47af >> 0x10;
    uVar3 = uVar3 + (((param_1 - 100) - uVar3 & 0xfffe) >> 1) >> 4;
  }
  else {
    uVar3 = 0xa4;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x109,&local_14);
  if (-1 < iVar2) {
    local_14 = local_14 & 0xffffff00 | uVar3;
    iVar2 = FUN_ffff0000b80211d4(uVar1,0x109);
    if (iVar2 == 0) {
      DAT_ffff0000b80ad618 = param_1;
    }
  }
  return;
}


