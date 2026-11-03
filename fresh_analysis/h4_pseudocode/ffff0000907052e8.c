
void FUN_ffff0000907052e8(uint param_1)

{
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint local_14;
  
  uVar1 = DAT_ffff00009083d080;
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
  iVar2 = FUN_ffff000090737554(DAT_ffff00009083d080,0x109,&local_14);
  if (-1 < iVar2) {
    local_14 = local_14 & 0xffffff00 | uVar3;
    iVar2 = FUN_ffff00009073751c(uVar1,0x109);
    if (iVar2 == 0) {
      DAT_ffff000090816618 = param_1;
    }
  }
  return;
}


