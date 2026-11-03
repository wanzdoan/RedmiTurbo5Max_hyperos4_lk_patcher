
void FUN_ffff0000be004d54(uint param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_24;
  
  uVar1 = DAT_ffff0000be0f9050;
  if (param_1 < 0x12d) {
    uVar3 = 6;
  }
  else if (param_1 < 4000) {
    uVar3 = (param_1 - 300 >> 1 & 0x7fff) / 0x19 + 6 & 0x3f;
  }
  else {
    uVar3 = 0x10;
  }
  local_24 = 0;
  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x10f,&local_24);
  if (-1 < iVar2) {
    local_24 = local_24 & 0xffffffc0 | uVar3;
    FUN_ffff0000be029920(uVar1,0x10f);
  }
  return;
}


