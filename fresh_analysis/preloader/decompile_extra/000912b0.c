// File offset entry: 0x912b0

uint FUN_020911b0(uint param_1)

{
  uint uVar1;
  
  param_1 = param_1 & 0xff;
  uVar1 = param_1 - 0x37;
  if (5 < param_1 - 0x41) {
    uVar1 = 0;
  }
  if (param_1 - 0x61 < 6) {
    uVar1 = param_1 - 0x57;
  }
  if (param_1 - 0x30 < 10) {
    uVar1 = param_1 - 0x30;
  }
  return uVar1;
}


