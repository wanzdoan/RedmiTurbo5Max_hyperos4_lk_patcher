
void FUN_ffff0000b80046b4(uint param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_24;
  
  uVar1 = DAT_ffff0000b810c070;
  if (param_1 < 0x65) {
    uVar3 = 1;
  }
  else if (param_1 < 2000) {
    uVar3 = (param_1 - 100 >> 1 & 0x7fff) / 0x19 + 1 & 0x1f;
  }
  else {
    uVar3 = 7;
  }
  local_24 = 0;
  iVar2 = FUN_ffff0000b802120c(DAT_ffff0000b810c070,0x117,&local_24);
  if (-1 < iVar2) {
    local_24 = local_24 & 0xffffffe0 | uVar3;
    FUN_ffff0000b80211d4(uVar1,0x117);
  }
  return;
}


