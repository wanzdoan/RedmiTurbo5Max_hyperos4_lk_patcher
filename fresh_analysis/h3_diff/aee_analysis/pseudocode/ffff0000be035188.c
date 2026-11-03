
void FUN_ffff0000be035188(uint param_1,uint param_2,uint param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  if ((DAT_ffff0000be121818 != 0) && (*(int *)(DAT_ffff0000be121818 + 0xc) - 1U < 5)) {
    uVar1 = param_1 >> 3 & 0x1f;
    uVar2 = param_3 >> 3 & 0x1f;
    switch(*(int *)(DAT_ffff0000be121818 + 0xc)) {
    case 1:
      DAT_ffff0000be121828 =
           uVar1 << 0x1a | (param_2 >> 3 & 0x1f) << 0x15 | uVar2 << 0x10 | uVar1 << 10 | uVar1 << 5
           | uVar2;
      break;
    case 2:
      uVar3 = param_2 >> 2 & 0x3f;
      DAT_ffff0000be121828 =
           (param_1 >> 3) << 0x1b | uVar3 << 0x15 | uVar2 << 0x10 | uVar1 << 0xb | uVar3 << 5 |
           uVar2;
      break;
    case 3:
      DAT_ffff0000be121828 = (param_1 & 0xff) << 0x10 | (param_2 & 0xff) << 8 | param_3 & 0xff;
      break;
    case 5:
      DAT_ffff0000be121828 =
           (param_2 >> 3 & 0x1c | param_1 & 0xe0 | (param_3 & 0xff) >> 6) * 0x1010101;
    }
  }
  DAT_ffff0000be12182c = DAT_ffff0000be121828 ^ DAT_ffff0000be121824;
  return;
}


