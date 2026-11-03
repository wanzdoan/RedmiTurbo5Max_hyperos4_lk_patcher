// File offset entry: 0xa7ee8

undefined4 FUN_020a7de8(uint *param_1)

{
  undefined4 uVar1;
  uint uVar2;
  
  uVar2 = uRam0000000013260060 >> 0x1c & 7;
  uVar1 = 0x10101b;
  if (uVar2 < 3) {
    uVar1 = 0;
  }
  *param_1 = uVar2;
  return uVar1;
}


