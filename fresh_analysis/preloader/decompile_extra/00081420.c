// File offset entry: 0x81420

void FUN_02081320(int param_1,uint param_2)

{
  uint uVar1;
  
  if (param_2 == 0) {
    uVar1 = 0xfffc;
  }
  else {
    uVar1 = (param_2 / 1000 & 0x3fff) << 2;
  }
  uRam000000001800d108 = uRam000000001800d108 | 1;
  uRam000000001800d100 = 0;
  uRam000000001800d080 = uVar1 | 0x444c0000 | (uint)(param_1 != 0);
  return;
}


